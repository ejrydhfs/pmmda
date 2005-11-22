/* copyright 2000-2005 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence");
 * you may not use this file except in compliance with the License.
 * $Id$
 */
package ch.bbv.mda.persistence.nsuml;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.StringTokenizer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ru.novosoft.uml.MFactory;
import ru.novosoft.uml.foundation.core.*;
import ru.novosoft.uml.foundation.data_types.MExpression;
import ru.novosoft.uml.foundation.data_types.MMultiplicity;
import ru.novosoft.uml.foundation.extension_mechanisms.MStereotype;
import ru.novosoft.uml.foundation.extension_mechanisms.MTaggedValue;
import ru.novosoft.uml.foundation.extension_mechanisms.MTaggedValueImpl;
import ru.novosoft.uml.model_management.MModel;
import ru.novosoft.uml.model_management.MPackage;
import ru.novosoft.uml.model_management.MPackageImpl;
import ch.bbv.mda.*;

/**
 * The MDA transformer defines rules how to transform a MDA element into the
 * corresponding NS-UML representation. The NSUML representation is XMI 1.0 /
 * UML 1.3 compatible. A feature allows the controlled removal of superfluous
 * tagged values from the NSUML model. A list of tag names, which should be
 * removed can be specified and passed to the transformer. During the
 * synchronization all tags with such names will be removed from the NSUML
 * model.
 * @author marcelbaumann
 * @version $Revision$
 */
public class MdaTransformer {
  /**
   * Logger of the instances of the class.
   */
  private static Log log = LogFactory.getLog(MdaTransformer.class);

  /**
   * UML modeling tool using NSUML library stores the documentation of an item
   * in a tagged value named documentation. This tag is the standard one defined
   * in the UML 1.4 notation.
   */
  public static final String DOCUMENTATION_TAG = "documentation";

  /**
   * UML modeling tool using NSUML library stores the volatile flag of an item
   * in a tagged value named volatile.
   */
  public static final String VOLATILE_TAG = "volatile";

  /**
   * UML modeling tool using NSUML library stores the transient flag of an item
   * in a tagged value named transient.
   */
  public static final String TRANSIENT_TAG = "transient";

  /**
   * Boolean true value as stored in a NSUML XMI file.
   */
  private static final String TRUE_STRING = "TRUE";

  /**
   * Boolean false value as stored in a NSUML XMI file.
   */
  private static final String FALSE_STRING = "FALSE";

  /**
   * Maps between the source objects used as keys and the target objects used as
   * values. Source objects are MDA entities, target objects are NSUML
   * instances.
   */
  private Map<Object, Object> elements;

  /**
   * This map is needed because type references to MClasses not created at the
   * time we look it up have to be post processed.
   */
  private Map<MAttribute, MetaClass> missingAttributeTypes;

  /**
   * NSUML model read from a XMI file.
   */
  private MModel mModel;

  /**
   * Meta model representation of the NSUML model.
   */
  private MetaModel metaModel;

  /**
   * Default constructor of the class.
   */
  public MdaTransformer() {
    elements = new HashMap<Object, Object>();
    missingAttributeTypes = new HashMap<MAttribute, MetaClass>();
  }

  /**
   * Synchronize the NSUML model with the MDA model.
   * @param source MDA model used as source of the synchronization.
   * @param target NSUML model to synchronize.
   */
  public void synchronize(MetaModel source, MModel target) {
    mModel = target;
    target.setName(source.getName());
    this.metaModel = source;
    extractDataTypes(source, target);
    for (MetaPackage item : source.getPackages()) {
      extractPackage(item, target);
    }
    for (MetaClass item : source.getClasses()) {
      extractClass(item, target);
    }
    processMissingAttributeTypes();
  }

  /**
   * Extracts the information from the MDA package and overwrites the ones of
   * the NSUML package defined in the given context.
   * @param source MDA package used as source of the synchronization.
   * @param context NSUML context containing the package to synchronize.
   */
  protected void extractPackage(MetaPackage source, MModel context) {
    assert ((source != null) && (context != null));
    String qualifiedName = source.getName();
    MPackage mPackage = createPackage(context, qualifiedName);
    elements.put(source, mPackage);
    extractTags(source, mPackage);
    extractStereotypeAndVisibility(source, mPackage);
    for (MetaClass item : source.getClasses()) {
      extractClass(item, mPackage);
    }
  }

  /**
   * Extracts the information from the MDA class and overwrites the ones of the
   * NSUML class defined in the given context.
   * @param source MDA class used as source of the synchronization.
   * @param context NSUML context containing the class to synchronize.
   */
  protected void extractClass(MetaClass source, MPackage context) {
    assert (source != null) && (context != null);
    MClass mClass = createClass(context, source.getName());
    elements.put(source, mClass);
    mClass.setAbstract(source.isAbstract());
    extractTags(source, mClass);
    extractStereotypeAndVisibility(source, mClass);
    for (MetaProperty item : source.getProperties()) {
      extractProperty(item, mClass);
    }
    for (MetaIndexedProperty item : source.getIndexedProperties()) {
      extractIndexedProperty(item, mClass);
    }
    for (MetaClass item : source.getClasses()) {
      extractNestedClass(item, mClass);
    }
  }

  /**
   * Extracts the information from the MDA class and overwrites the ones of the
   * NSUML nested classes defined in the given context.
   * @param source MDA class used as source of the synchronization.
   * @param context NSUML context containing the class to synchronize.
   */
  protected void extractNestedClass(MetaClass source, MClass context) {
    assert (source != null) && (context != null);
    MClass mClass = createClass(context, source.getName());
    elements.put(source, mClass);
    mClass.setAbstract(source.isAbstract());
    extractTags(source, mClass);
    extractStereotypeAndVisibility(source, mClass);
    for (MetaProperty item : source.getProperties()) {
      extractProperty(item, mClass);
    }
    for (MetaIndexedProperty item : source.getIndexedProperties()) {
      extractIndexedProperty(item, mClass);
    }
    for (MetaClass item : source.getClasses()) {
      extractNestedClass(item, mClass);
    }
  }

  /**
   * Extracts the information from the MDA property and overwrites the ones of
   * the NSUML class defined in the given context.
   * @param source MDA property used as source of the synchronization.
   * @param context NSUML context containing the property to synchronize.
   */
  protected void extractProperty(MetaProperty source, MClass context) {
    assert (source != null) && (context != null);
    if (source.isAggregation()) {
      MetaElement mElement = source.getContext().getContext();
      while (!(mElement instanceof MetaPackage)) {
        mElement = mElement.getContext();
      }
      MetaPackage metaPackage = (MetaPackage) mElement;
      MPackage mPackage = (MPackage) elements.get(metaPackage);
      MAssociation association = findAssociation(mPackage, context, source
          .getName());
      if (association != null) {
        updatePropertyFlags(source, association);
        extractTags(source, association);
        extractStereotypeAndVisibility(source, association);
      }
    } else {
      MAttribute mAttribute = createAttribute(context, source.getName());
      MClassifier mClassifier = mAttribute.getType();
      if (mClassifier == null
          || !NsUmlUtilities.getQualifiedName(mClassifier).equals(
              source.getTypeName())) {
        mClassifier = createClassifier(source.getTypeName(), mAttribute);
        mAttribute.setType(mClassifier);
      }
      MMultiplicity mMultiplicity = mAttribute.getMultiplicity();
      MMultiplicity newMultiplicity = convert(source.getMultiplicity());
      if ((newMultiplicity != null) && (mMultiplicity != newMultiplicity)) {
        mAttribute.setMultiplicity(newMultiplicity);
      }
      updateInitialValue(source, mAttribute);
      updatePropertyFlags(source, mAttribute);
      extractTags(source, mAttribute);
      extractStereotypeAndVisibility(source, mAttribute);
    }
  }

  /**
   * Extracts the information from the MDA indexed roperty and overwrites the
   * ones of the NSUML class defined in the given context.
   * @param source MDA property used as source of the synchronization.
   * @param context NSUML context containing the property to synchronize.
   */
  protected void extractIndexedProperty(MetaIndexedProperty source,
      MClass context) {
    assert (source != null) && (context != null);
    if (source.isAggregation()) {
      MetaPackage metaPackage = (MetaPackage) source.getContext().getContext();
      MPackage mPackage = (MPackage) elements.get(metaPackage);
      MAssociation association = findAssociation(mPackage, context, source
          .getName());
      if (association != null) {
        updatePropertyFlags(source, association);
        extractTags(source, association);
        extractStereotypeAndVisibility(source, association);
      }
    } else {
      MAttribute attribute = createAttribute(context, source.getName());
      MMultiplicity multiplicity = attribute.getMultiplicity();
      MMultiplicity newMultiplicity = convert(source.getMultiplicity());
      if ((newMultiplicity != null) && (multiplicity != newMultiplicity)) {
        attribute.setMultiplicity(newMultiplicity);
      }
      updatePropertyFlags(source, attribute);
      extractTags(source, attribute);
      extractStereotypeAndVisibility(source, attribute);
    }
  }

  /**
   * Extracts the tagged values from the source and updates the corresponding
   * item. As a side effect the obsolete tagged values are removed from the
   * item.
   * @param source MDA source item containing the tags to synchronize.
   * @param item target item to synchronize.
   */
  private void extractTags(MetaElement source, MModelElement item) {
    String description = source.getDescription();
    if ((description != null) && (description.length() > 0)) {
      MTaggedValue taggedValue = createTag(item, DOCUMENTATION_TAG);
      taggedValue.setValue(description);
    } else {
      MTaggedValue taggedValue = findTag(item, DOCUMENTATION_TAG);
      if (taggedValue != null) {
        taggedValue.setValue("");
      }
    }
    for (TaggedValue tag : source.getTags()) {
      String tagName = tag.getTag();
      String tagValue = tag.getValue();
      MTaggedValue taggedValue = createTag(item, tagName);
      taggedValue.setValue(tagValue != null ? tagValue : "");
    }
  }

  /**
   * Extracts all data types from the source model and add them to the target
   * model if not already existing.
   * @param source source MDA model containing the data types.
   * @param target target NSUML model to synchronize.
   */
  private void extractDataTypes(MetaModel source, MModel target) {
    MDataType mDataType;
    for (MetaDatatype metaDataType : source.getDatatypes()) {
      boolean found = false;
      Iterator<MDataType> mDatatypes = new FilterIterator<MDataType>(target
          .getOwnedElements(), MDataType.class);
      while (mDatatypes.hasNext()) {
        mDataType = mDatatypes.next();
        if (mDataType.getName().equals(metaDataType.getName())) {
          found = true;
          break;
        }
      }
      if (!found) {
        mDataType = MFactory.getDefaultFactory().createDataType();
        mDataType.setName(metaDataType.getName());
        target.addOwnedElement(mDataType);
      }
    }
  }

  /**
   * Finds the tag with the given name.
   * @param item item to look for the tag.
   * @param tagName name of the tag to find.
   * @return the requested tag if found otherwise null
   * @pre (item != null) && (tagName != null)
   */
  private MTaggedValue findTag(MModelElement item, String tagName) {
    assert (item != null) && (tagName != null);
    for (Iterator i = item.getTaggedValues().iterator(); i.hasNext();) {
      MTaggedValue tag = (MTaggedValue) i.next();
      if (tagName.equals(tag.getTag())) {
        return tag;
      }
    }
    return null;
  }

  /**
   * Creates a tag with the given name in the item.
   * @param item item in which the tag should be created.
   * @param tagName the name of the tag.
   * @return the newly created tag.
   * @pre (item != null) && (tagName != null)
   */
  private MTaggedValue createTag(MModelElement item, String tagName) {
    assert (item != null) && (tagName != null);
    MTaggedValue taggedValue = findTag(item, tagName);
    if (taggedValue == null) {
      taggedValue = new MTaggedValueImpl();
      taggedValue.setTag(tagName);
      item.addTaggedValue(taggedValue);
    }
    return taggedValue;
  }

  /**
   * Creates the package with the given qualified name. All necessary surounding
   * packages are created. If the package already exists no creation action is
   * performed.
   * @param model model containing the package.
   * @param qualifiedName qualified name of the package to create.
   * @return the requested package either found or newly created.
   */
  private MPackage createPackage(MModel model, String qualifiedName) {
    assert (model != null) && (qualifiedName != null);
    MPackage mPackage = null;
    StringTokenizer tokenizer = new StringTokenizer(qualifiedName, ".");
    MPackage context = model;
    while (tokenizer.hasMoreTokens()) {
      String name = tokenizer.nextToken();
      mPackage = findPackage(context, name);
      if (mPackage == null) {
        mPackage = new MPackageImpl();
        mPackage.setName(name);
        context.addOwnedElement(mPackage);
      }
      context = mPackage;
    }
    return mPackage;
  }

  /**
   * Returns the package with the given name member of the context.
   * @param context context in which to search the package.
   * @param name name of the requested package.
   * @return the requested package if found otherwise null.
   */
  public MPackage findPackage(MPackage context, String name) {
    assert (context != null) && (name != null);

    if (name.equals(context.getName())) {
      return context;
    }
    for (Iterator<MPackage> i = new FilterIterator<MPackage>(context
        .getOwnedElements(), MPackage.class); i.hasNext();) {
      MPackage mPackage = i.next();
      if (name.equals(mPackage.getName())) {
        return mPackage;
      }
    }
    return null;
  }

  private MClass findClass(MPackage context, String name) {
    assert (context != null) && (name != null);
    MClass mClass = null;
    for (Iterator<MClass> i = new FilterIterator<MClass>(context
        .getOwnedElements(), MClass.class); i.hasNext();) {
      mClass = i.next();
      if (name.equals(mClass.getName())) {
        break;
      }
      mClass = null;
    }
    return mClass;
  }

  private MClass findClass(MClass context, String name) {
    assert (context != null) && (name != null);
    MClass mClass = null;
    for (Iterator<MClass> i = new FilterIterator<MClass>(context
        .getOwnedElements(), MClass.class); i.hasNext();) {
      mClass = i.next();
      if (name.equals(mClass.getName())) {
        break;
      }
      mClass = null;
    }
    return mClass;
  }

  private MClass createClass(MPackage context, String name) {
    assert (context != null) && (name != null);
    MClass item = findClass(context, name);
    if (item == null) {
      item = new MClassImpl();
      item.setName(name);
      context.addOwnedElement(item);
    }
    return item;
  }

  private MClass createClass(MClass context, String name) {
    assert (context != null) && (name != null);
    MClass item = findClass(context, name);
    if (item == null) {
      item = new MClassImpl();
      item.setName(name);
      context.addOwnedElement(item);
    }
    return item;
  }

  private MAttribute findAttribute(MClass context, String name) {
    assert (context != null) && (name != null);
    MAttribute mAttribute = null;
    for (Iterator<MAttribute> i = new FilterIterator<MAttribute>(context
        .getFeatures(), MAttribute.class); i.hasNext();) {
      mAttribute = i.next();
      if (name.equals(mAttribute.getName())) {
        break;
      }
      mAttribute = null;
    }
    if (mAttribute == null) {
      for (Iterator<MAttribute> i = new FilterIterator<MAttribute>(context
          .getOwnedElements(), MAttribute.class); i.hasNext();) {
        mAttribute = i.next();
        if (name.equals(mAttribute.getName())) {
          break;
        }
        mAttribute = null;
      }
    }
    return mAttribute;
  }

  /**
   * Finds an attribute with the given name. If not found creates an attribute
   * in the given class.
   * @param context class in which the attribute should be created.
   * @param name name of the attribute to create.
   * @return the requested attribute.
   */
  private MAttribute createAttribute(MClass context, String name) {
    assert (context != null) && (name != null);
    MAttribute item = findAttribute(context, name);
    if (item == null) {
      item = new MAttributeImpl();
      item.setName(name);
      context.addFeature(item);
    }
    return item;
  }

  private MClassifier findDataType(String name) {
    for (Iterator<MDataType> i = new FilterIterator<MDataType>(mModel
        .getOwnedElements(), MDataType.class); i.hasNext();) {
      MDataType mDataType = i.next();
      if (mDataType.getName().equals(name)) {
        return mDataType;
      }
    }
    return null;
  }

  private MClassifier createClassifier(String name, MAttribute mAttribute) {
    assert (name != null);

    MClassifier item = null;
    MClass tempMClass = null;

    MetaClass mc = metaModel.findClass(name);
    if (mc != null) {
      tempMClass = (MClass) elements.get(mc);
      if (tempMClass == null) {
        missingAttributeTypes.put(mAttribute, mc);
      }
    } else {
      item = findDataType(name);
      if (item == null) {
        item = MFactory.getDefaultFactory().createDataType();
        item.setName(name);
        mModel.addOwnedElement(item);
      }
    }
    return item;
  }

  private void processMissingAttributeTypes() {
    MClass mClass;
    MetaClass metaClass;
    for (MAttribute mAttribute : missingAttributeTypes.keySet()) {
      metaClass = missingAttributeTypes.get(mAttribute);
      mClass = (MClass) elements.get(metaClass);
      mAttribute.setType(mClass);
    }
  }

  /**
   * finds the association with the given name defined in the context.
   * @param context context where to look for the association.
   * @param clazz class owning the association.
   * @param name name of the requested association
   * @return the requested association if found otherwise null.
   */
  private MAssociation findAssociation(MPackage context, MClass clazz,
      String name) {
    assert (context != null) && (clazz != null) && (name != null);
    MAssociation association = null;
    for (Iterator<MAssociation> i = new FilterIterator<MAssociation>(context
        .getOwnedElements(), MAssociation.class); i.hasNext();) {
      association = i.next();
      MAssociationEnd e1 = association.getConnection(0);
      MAssociationEnd e2 = association.getConnection(1);
      MClassifier owner = (MClassifier) e1.getType();
      if (owner == clazz) {
        String associationName = e2.getName();
        if ((associationName == null) || (associationName.length() == 0)) {
          associationName = association.getName();
        }
        if (name.equals(associationName)) {
          break;
        }
        association = null;
      }
    }
    return association;
  }

  private void updatePropertyFlags(MetaProperty property, MModelElement item) {
    assert (property != null) && (item != null);
    if (property.isVolatile()) {
      MTaggedValue taggedValue = createTag(item, VOLATILE_TAG);
      taggedValue.setValue(TRUE_STRING);
    } else {
      MTaggedValue taggedValue = findTag(item, VOLATILE_TAG);
      if (taggedValue != null) {
        taggedValue.setValue(FALSE_STRING);
      }
    }
    if (property.isTransient()) {
      MTaggedValue taggedValue = createTag(item, TRANSIENT_TAG);
      taggedValue.setValue(TRUE_STRING);
    } else {
      MTaggedValue taggedValue = findTag(item, TRANSIENT_TAG);
      if (taggedValue != null) {
        taggedValue.setValue(FALSE_STRING);
      }
    }
  }

  private void updateInitialValue(MetaProperty property, MAttribute attribute) {
    String initialValue = property.getInitialValue();
    MExpression expression = attribute.getInitialValue();
    if (expression == null) {
      if (initialValue != null) {
        attribute.setInitialValue(new MExpression("Java", initialValue));
      }
    } else {
      if (expression.getBody() != initialValue) {
        attribute.setInitialValue(new MExpression("Java", initialValue));
      }
    }
  }

  /**
   * Converts a MDA multiplicity into a NSUML multiplicity
   * @param multiplicity MDA multiplicity to convert.
   * @return the requested NSUML multiplicity
   */
  private MMultiplicity convert(Multiplicity multiplicity) {
    MMultiplicity mMultiplicity = null;
    int lowerBound = multiplicity.getLowerBound();
    int upperBound = multiplicity.getUpperBound();
    if (lowerBound == 0) {
      if (upperBound == 1) {
        mMultiplicity = MMultiplicity.M0_1;
      } else if (upperBound == Multiplicity.N_MULTIPLICITY) {
        mMultiplicity = MMultiplicity.M0_N;
      }
    } else if (lowerBound == 1) {
      if (upperBound == 1) {
        mMultiplicity = MMultiplicity.M1_1;
      } else if (upperBound == Multiplicity.N_MULTIPLICITY) {
        mMultiplicity = MMultiplicity.M1_N;
      }
    }
    return mMultiplicity;
  }

  /**
   * Extracts the information from the MDA element and overwrites the ones of
   * the NSUML element defined in the given context.
   * @param source MDA element used as source of the synchronization.
   * @param context NSUML context containing the class to synchronize.
   */
  private void extractStereotypeAndVisibility(MetaElement source,
      MModelElement context) {
    String stereotype = source.getStereotype();
    MStereotype mStereotype = context.getStereotype();
    if (stereotype != null) {
      if (mStereotype == null)
        mStereotype = MFactory.getDefaultFactory().createStereotype();
      mStereotype.setName(stereotype);
      context.setStereotype(mStereotype);
      if (context instanceof MClassifier) {
        ((MClassifier) context).addOwnedElement(mStereotype);
      } else if (context instanceof MPackage) {
        ((MPackage) context).addOwnedElement(mStereotype);
      } else {
        log.info("Cannot associate stereotype '" + stereotype + "' to "
            + context.getName() + " : " + context.getClass());
      }
    } else if (mStereotype != null) {
      mStereotype.setName("");
    }

    NsUmlUtilities.setVisibility(context, source.getVisibility());
  }

}