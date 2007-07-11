/* copyright 2000-2006 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence");
 * you may not use this file except in compliance with the License.
 * $Id$
 */
package ch.bbv.mda.persistence.mdr;

import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import org.omg.uml.foundation.core.Abstraction;
import org.omg.uml.foundation.core.AssociationClass;
import org.omg.uml.foundation.core.AssociationEnd;
import org.omg.uml.foundation.core.Attribute;
import org.omg.uml.foundation.core.Classifier;
import org.omg.uml.foundation.core.DataType;
import org.omg.uml.foundation.core.Generalization;
import org.omg.uml.foundation.core.Interface;
import org.omg.uml.foundation.core.Namespace;
import org.omg.uml.foundation.core.UmlClass;
import org.omg.uml.foundation.datatypes.ChangeableKindEnum;
import org.omg.uml.foundation.datatypes.MultiplicityRange;
import org.omg.uml.foundation.datatypes.OrderingKindEnum;
import org.omg.uml.foundation.datatypes.ScopeKindEnum;
import org.omg.uml.foundation.datatypes.VisibilityKindEnum;
import org.omg.uml.modelmanagement.Model;

import ch.bbv.mda.MetaClass;
import ch.bbv.mda.MetaDatatype;
import ch.bbv.mda.MetaElement;
import ch.bbv.mda.MetaIndexedProperty;
import ch.bbv.mda.MetaModel;
import ch.bbv.mda.MetaPackage;
import ch.bbv.mda.MetaProperty;
import ch.bbv.mda.Multiplicity;
import ch.bbv.mda.Stereotype;
import ch.bbv.mda.TaggedValue;
import ch.bbv.mda.UmlConstants;
import ch.bbv.mda.VisibilityCode;

/**
 * The MDA transformer defines rules how to transform a MDA element into the
 * corresponding MDR representation. The MDR representation is XMI 1.2 / UML 1.4
 * compatible.
 * @author Marcel Baumann
 * @author Ueli Kurmann
 * @version $Revision$
 */
public class Mda2MdrTransformer {
  private static final String REALIZE = "realize";

  private static final String JAVA = "Java";

  /**
   * Boolean true value as stored in a XMI file.
   */
  private static final String TRUE_STRING = "TRUE";

  /**
   * Boolean false value as stored in a XMI file.
   */
  private static final String FALSE_STRING = "FALSE";

  /**
   * Maps between the source objects used as keys and the target objects used as
   * values. Source objects are MDA entities, target objects are wrapped MDR
   * instances.
   */
  private Map<Object, Object> elements;

  /**
   * This map is needed because type references to MClasses not created at the
   * time we look it up have to be post processed.
   */
  private Map<MdrUmlAttribute, MetaClass> missingAttributeTypes;

  /**
   * MDR model read from a XMI file.
   */
  private MdrUmlModel mModel;

  /**
   * Meta model representation of the MDR model.
   */
  private MetaModel metaModel;

  private MdrLoader mdrLoader;

  private Model mdrModel;

  private Map<String, MdrUmlStereotype> stereotypeCache;

  /**
   * Default constructor of the class.
   */
  public Mda2MdrTransformer() {
    elements = new HashMap<Object, Object>();
    missingAttributeTypes = new HashMap<MdrUmlAttribute, MetaClass>();
  }

  /**
   * Synchronize the MDR model with the MDA model.
   * 
   * @param source
   *          MDA model used as source of the synchronization.
   * @param target
   *          MDR model to synchronize.
   * @param mdrLoader
   * @pre (source != null) && (target != null)
   */
  public void synchronize(MetaModel source, MdrUmlModel target,
      MdrLoader mdrLoader) {
    assert (source != null) && (target != null);
    this.mdrLoader = mdrLoader;
    mModel = target;

    mdrModel = mModel.getUmlPackage().getModelManagement().getModel()
        .createModel();

    mdrModel.setName(source.getName());

    target.setName(source.getName());
    this.metaModel = source;
    extractDataTypes(source, target);

    for (MetaPackage metaPackage : source.getPackages()) {
      extractPackage(metaPackage, target);
    }

    for (MetaPackage item : source.getPackages()) {
      processPackage(item, target);
    }

    // classes in the default namespace
    // default package erstellen
    MdrUmlPackage defaultPackage = new MdrUmlPackage(mdrLoader
        .createUmlPackage(target.getName()), target);
    mModel.addPackage(target.getName(), defaultPackage);
    defaultPackage.getModelElement().setNamespace(mdrModel);
    for (MetaClass item : source.getClasses()) {
      extractClass(item, defaultPackage);
    }

    // process relations
    for (MetaPackage item : source.getPackages()) {
      processAllRelations(item, target);
    }

    // process classes in the default namespace
    for (MetaClass item : source.getClasses()) {
      extractRelation(item, defaultPackage);
    }
    processMissingAttributeTypes();
  }

  /**
   * Extracts the information from the MDA package and overwrites the ones of
   * the MDR package defined in the given context.
   * 
   * @param source
   *          MDA package used as source of the synchronization.
   * @param context
   *          MDR context containing the package to synchronize.
   * @pre (source != null) && (context != null)
   */
  protected void extractPackage(MetaPackage source, MdrUmlModel context) {
    assert (source != null) && (context != null);
    String qualifiedName = source.getName();
    MdrUmlPackage mPackage = createPackage(context, qualifiedName);
    elements.put(source, mPackage);
    extractTags(source, mPackage);
    extractStereotypeAndVisibility(source, mPackage);
  }

  /**
   * Process package elements
   * 
   * @param source
   *          MDA package to be processed
   * @param context
   *          MDR context containing the package to be processed.
   * @pre (source != null) && (context != null)
   */
  protected void processPackage(MetaPackage source, MdrUmlModel context) {
    assert (source != null) && (context != null);
    MdrUmlPackage mPackage = findPackageQualified(context, source.getName());

    for (MetaClass item : source.getClasses()) {
      extractClass(item, mPackage);
    }
  }

  /**
   * Processes relations within a package
   * 
   * @param source
   *          MDA package to be processed
   * @param context
   *          MDR context containing the package to be processed.
   * @pre (source != null) && (context != null)
   */
  protected void processAllRelations(MetaPackage source, MdrUmlModel context) {
    assert (source != null) && (context != null);
    MdrUmlPackage mPackage = findPackageQualified(context, source.getName());
    if (mPackage == null)
      return;
    for (MetaClass item : source.getClasses()) {
      extractRelation(item, mPackage);
    }
  }

  /**
   * Extracts the information from the MDA class and overwrites the ones of the
   * MDR class defined in the given context.
   * 
   * @param source
   *          MDA class used as source of the synchronization.
   * @param context
   *          MDR context containing the class to synchronize.
   * @pre (source != null) && (context != null)
   */
  protected void extractClass(MetaClass source, MdrUmlPackage context) {
    assert (source != null) && (context != null);
    MdrUmlClass mClass;
    if (source.isInterface()) {
      mClass = createInterface(context, source.getName());
    } else {
      mClass = createClass(context, source.getName());
    }
    mClass.getModelElement().setNamespace(
        (org.omg.uml.modelmanagement.UmlPackage) context.getModelElement());

    elements.put(source, mClass);
    mClass.setAbstract(source.isAbstract());
    mClass.setFinal(source.isFinal());
    mClass.setStatic(source.isStatic());

    extractTags(source, mClass);
    extractStereotypeAndVisibility(source, mClass);
    for (MetaClass item : source.getNestedClasses()) {
      extractNestedClass(item, mClass);
    }
  }

  /**
   * Extracts aggregations, indexed properties, properties, realizes and
   * specializes relations from a classifier
   * 
   * @param source
   *          MDA class used as source of the synchronization.
   * @param context
   *          MDR context containing the class to synchronize.
   * @pre (source != null) && (context != null)
   */
  protected void extractRelation(MetaClass source, MdrUmlPackage context) {
    assert (source != null) && (context != null);
    // find class the relations relate to
    MdrUmlClass mClass = mModel.getClass(source.getQualifiedName());

    for (MetaProperty item : source.getProperties()) {
      extractProperty(item, mClass);
    }
    for (MetaIndexedProperty item : source.getIndexedProperties()) {
      extractIndexedProperty(item, mClass);
    }
    for (MetaClass ancestor : source.retrieveAncestors()) {
      extractAncestor(ancestor, mClass);
    }
    for (MetaClass ancestorIf : source.retrieveInterfaces()) {
      extractInterface(ancestorIf, mClass);
    }
    for (MetaClass item : source.getNestedClasses()) {
      extractRelation(item, context);
    }
  }

  /**
   * Creates a generalization relation within the MDR model. If ancestor class
   * is already defined then it will be found. If not it will be created.
   * Detailed information of the ancestor class will be added during processing
   * the corresponding extractClass method
   * 
   * @param ancestor
   *          of the generalization relation
   * @param child
   *          of the generalization relation
   */
  protected void extractAncestor(MetaClass ancestor, MdrUmlClass child) {
    MdrUmlClass mAncestor = mModel.getClass(ancestor.getQualifiedName());
    MdrUmlPackage ancestorContext = mModel.getPackage(ancestor
        .retrievePackage().getName());
    if (mAncestor == null) {
      mAncestor = createClass(ancestorContext, ancestor.getName());
    }
    MdrUmlGeneralization generalization = findGeneralization(ancestorContext,
        mAncestor, child);
    if (generalization == null) {
      Generalization tmpGeneralization = mModel.getUmlPackage().getCore()
          .getGeneralization().createGeneralization();
      tmpGeneralization.setNamespace(ancestorContext.getNamespace());
      generalization = new MdrUmlGeneralization(tmpGeneralization);

      generalization.setChild((Classifier) child.getModelElement());
      generalization.setParent((Classifier) mAncestor.getModelElement());
    }
  }

  /**
   * Creates a generalization relation within the MDR model. If ancestor
   * interface is already defined then it will be found. If not it will be
   * created. Detailed information of the ancestor interface will be added
   * during the corresponding extractInterface method. Note: A realize relation
   * will be mapped to a generalization relation
   * 
   * @param ancestorIf
   *          of the generalization relation
   * @param child
   *          implements the interface
   */

  protected void extractInterface(MetaClass ancestorIf, MdrUmlClass child) {
    MdrUmlClass parent = mModel.getClass(ancestorIf.getQualifiedName());
    MdrUmlPackage ancestorContext = mModel.getPackage(ancestorIf
        .retrievePackage().getName());

    if (parent == null) {
      parent = createInterface(ancestorContext, ancestorIf.getName());
    }

    MdrUmlAbstraction abstraction = findAbstraction(mModel, parent, child);

    if (abstraction == null) {
      Abstraction umlAbstraction = mModel.getUmlPackage().getCore()
          .getAbstraction().createAbstraction();
      // TODO correct?
      umlAbstraction.setNamespace(ancestorContext.getNamespace());

      abstraction = new MdrUmlAbstraction(umlAbstraction);
      abstraction.getClients().add(child.getModelElement());
      abstraction.getSuppliers().add(parent.getModelElement());

      abstraction = new MdrUmlAbstraction(umlAbstraction);

      MdrUmlStereotype mStereotype = findStereotype(REALIZE);
      if (mStereotype == null) {
        mStereotype = new MdrUmlStereotype(mdrLoader.createStereotype(REALIZE));

      }
      abstraction.addStereotype(mStereotype);
    }
  }

  /**
   * Extracts the information from the MDA class and overwrites the ones of the
   * MDR nested classes defined in the given context.
   * 
   * @param source
   *          MDA class used as source of the synchronization.
   * @param context
   *          MDR context containing the class to synchronize.
   */
  protected void extractNestedClass(MetaClass source, MdrUmlClass context) {
    assert (source != null) && (context != null);
    MdrUmlClass mClass;
    if (source.isInterface()) {
      mClass = createInterface(context, source.getName());
    } else {
      mClass = createClass(context, source.getName());
    }
    mClass.getModelElement()
        .setNamespace((Namespace) context.getModelElement());
    elements.put(source, mClass);
    mClass.setAbstract(source.isAbstract());
    extractTags(source, mClass);
    extractStereotypeAndVisibility(source, mClass);
    for (MetaClass item : source.getNestedClasses()) {
      extractNestedClass(item, mClass);
    }
  }

  /**
   * Extracts the information from the MDA property and overwrites the ones of
   * the MDR class defined in the given context.
   * 
   * @param source
   *          MDA property used as source of the synchronization.
   * @param context
   *          MDR context containing the property to synchronize.
   */
  protected void extractProperty(MetaProperty source, MdrUmlClass context) {
    assert (source != null) && (context != null);
    if (source.isAggregation() || source.isComposition()) {
      MetaElement mElement = source.getContext().getContext();
      while (!(mElement instanceof MetaPackage)) {
        mElement = mElement.getContext();
      }
      MetaPackage metaPackage = (MetaPackage) mElement;
      MdrUmlPackage mPackage = (MdrUmlPackage) elements.get(metaPackage);
      MdrUmlAssociation association = findAssociation(mPackage, context, source
          .getName());
      if (association != null) {
        updatePropertyFlags(source, association);
        extractTags(source, association);
        extractStereotypeAndVisibility(source, association);
        if (source.isAggregation()) {
          association
              .getConnection(0)
              .setAggregation(
                  org.omg.uml.foundation.datatypes.AggregationKindEnum.AK_AGGREGATE);
        }
        if (source.isComposition()) {
          association
              .getConnection(0)
              .setAggregation(
                  org.omg.uml.foundation.datatypes.AggregationKindEnum.AK_COMPOSITE);
        }
      } else {
        association = createAssociation(source, mPackage);
      }

    } else {

      MdrUmlAttribute mAttribute = createAttribute(context, source);

      MdrUmlClass mClassifier = mAttribute.getType();

      if (mClassifier == null
          || mClassifier.getQualifiedName().equals(source.getTypeName())) {
        // TODO: problem, if no attribute type is set
        if (source.getTypeName() == null) {
          source.setTypeName("void");
        }
        mClassifier = createClassifier(source.getTypeName(), mAttribute);
        mAttribute.setType(mClassifier);
      }
      Multiplicity mMultiplicity = mAttribute.getMultiplicity();
      Multiplicity newMultiplicity = source.getMultiplicity();
      if ((newMultiplicity != null) && (mMultiplicity != newMultiplicity)) {
        if (mMultiplicity == null) {
          org.omg.uml.foundation.datatypes.Multiplicity mdrMultiplicity;
          mdrMultiplicity = mModel.getUmlPackage().getDataTypes()
              .getMultiplicity().createMultiplicity();
          MultiplicityRange mdrMultiplicityrange = mModel.getUmlPackage()
              .getDataTypes().getMultiplicityRange().createMultiplicityRange();
          mdrMultiplicityrange.setLower(newMultiplicity.getLowerBound());
          mdrMultiplicityrange.setUpper(newMultiplicity.getUpperBound());
          mdrMultiplicity.getRange().add(mdrMultiplicityrange);
          mAttribute.setMultiplicity(mdrMultiplicity);

        } else {
          mAttribute.setMultiplicity(newMultiplicity);
        }

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
   * ones of the MDR class defined in the given context.
   * 
   * @param source
   *          MDA property used as source of the synchronization.
   * @param context
   *          MDR context containing the property to synchronize.
   */
  protected void extractIndexedProperty(MetaIndexedProperty source,
      MdrUmlClass context) {
    assert (source != null) && (context != null);
    if (source.isAggregation() || source.isComposition()) {
      MetaElement mElement = source.getContext().getContext();
      while (!(mElement instanceof MetaPackage)) {
        mElement = mElement.getContext();
      }
      MdrUmlPackage mPackage = (MdrUmlPackage) elements.get(mElement);
      MdrUmlAssociation association = findAssociation(mPackage, context, source
          .getName());
      if (association != null) {
        updatePropertyFlags(source, association);
        extractTags(source, association);
        extractStereotypeAndVisibility(source, association);
      } else {
        association = createAssociation(source, mPackage);
      }
    } else {

      MdrUmlAttribute mAttribute = createAttribute(context, source);

      MdrUmlClass mClassifier = mAttribute.getType();

      if (mClassifier == null
          || !mClassifier.getQualifiedName().equals(source.getTypeName())) {
        mClassifier = createClassifier(source.getTypeName(), mAttribute);
        mAttribute.setType(mClassifier);
      }

      Multiplicity multiplicity = mAttribute.getMultiplicity();
      Multiplicity newMultiplicity = source.getMultiplicity();

      if ((newMultiplicity != null) && (multiplicity != newMultiplicity)) {
        if (multiplicity == null) {
          org.omg.uml.foundation.datatypes.Multiplicity mdrMultiplicity;
          mdrMultiplicity = mModel.getUmlPackage().getDataTypes()
              .getMultiplicity().createMultiplicity();
          MultiplicityRange mdrMultiplicityrange = mModel.getUmlPackage()
              .getDataTypes().getMultiplicityRange().createMultiplicityRange();
          mdrMultiplicityrange.setLower(newMultiplicity.getLowerBound());
          mdrMultiplicityrange.setUpper(newMultiplicity.getUpperBound());
          mdrMultiplicity.getRange().add(mdrMultiplicityrange);
          mAttribute.setMultiplicity(mdrMultiplicity);

        } else {
          mAttribute.setMultiplicity(newMultiplicity);
        }
      }
      updatePropertyFlags(source, mAttribute);
      extractTags(source, mAttribute);
      extractStereotypeAndVisibility(source, mAttribute);
    }
  }

  /**
   * Extracts the tagged values from the source and updates the corresponding
   * item. As a side effect the obsolete tagged values are removed from the
   * item.
   * 
   * @param source
   *          MDA source item containing the tags to synchronize.
   * @param item
   *          target item to synchronize.
   */
  private void extractTags(MetaElement source, MdrUmlElement item) {
    String description = source.getDescription();
    if ((description != null) && (description.length() > 0)) {
      MdrTaggedValue taggedValue = createTag(item,
          UmlConstants.DOCUMENTATION_TAG);
      taggedValue.setValue(description);
    } else {
      MdrTaggedValue taggedValue = findTag(item, UmlConstants.DOCUMENTATION_TAG);
      if (taggedValue != null) {
        taggedValue.setValue("");
      }
    }
    // synchronizes all the source tags with the target ones
    for (TaggedValue tag : source.getTags()) {
      String tagName = tag.getTag();
      String tagValue = tag.getValue();
      MdrTaggedValue taggedValue = createTag(item, tagName);
      taggedValue.setValue(tagValue != null ? tagValue : "");
    }
    // removes the tags no more available in the source. These tags are
    // the ones declared obsolete in the loaded cartridges.
    for (MdrTaggedValue taggedValue : item.getTaggedValues()) {
      if (source.getTaggedValue(taggedValue.getName()) == null) {
        taggedValue.getTaggedValue().refDelete();
      }
    }

  }

  /**
   * Extracts all data types from the source model and add them to the target
   * model if not already existing.
   * 
   * @param source
   *          source MDA model containing the data types.
   * @param target
   *          target MDR model to synchronize.
   */
  private void extractDataTypes(MetaModel source, MdrUmlModel target) {
    MdrUmlDataType mDataType;
    for (MetaDatatype metaDataType : source.getDatatypes()) {
      // test if datatype already exists in the mdr model
      boolean found = false;
      for (MdrUmlDataType dataType : target.getDataTypes()) {
        if (dataType.getName().equals(metaDataType.getName())) {
          found = true;
          break;
        }
      }
      if (!found) {
        mDataType = mdrLoader.createDataType(metaDataType.getName());
        mDataType.getModelElement().setNamespace(mdrModel);
      }
    }
  }

  /**
   * Creates a tag with the given name in the item.
   * 
   * @param item
   *          item in which the tag should be created.
   * @param tagName
   *          the name of the tag.
   * @return the newly created tag.
   * @pre (item != null) && (tagName != null)
   */
  private MdrTaggedValue createTag(MdrUmlElement item, String tagName) {
    assert (item != null) && (tagName != null);
    MdrTaggedValue taggedValue = findTag(item, tagName);
    if (taggedValue == null) {
      org.omg.uml.foundation.core.TaggedValue tag = mdrLoader
          .createTaggedValue(item, tagName);

      taggedValue = new MdrTaggedValue(tag);
      item.addTaggedValue(taggedValue);
    }
    return taggedValue;
  }

  /**
   * Creates the package with the given qualified name. All necessary surounding
   * packages are created. If the package already exists no creation action is
   * performed.
   * 
   * @param model
   *          model containing the package.
   * @param qualifiedName
   *          qualified name of the package to create.
   * @return the requested package either found or newly created.
   */

  private MdrUmlPackage createPackage(MdrUmlModel model, String qualifiedName) {
    assert (model != null) && (qualifiedName != null);
    MdrUmlPackage mPackage = null;
    StringTokenizer tokenizer = new StringTokenizer(qualifiedName, ".");
    org.omg.uml.modelmanagement.UmlPackage context = mdrModel;

    String tmpQualifiedName = "";
    while (tokenizer.hasMoreTokens()) {
      String name = tokenizer.nextToken();
      if (tmpQualifiedName.length() > 0) {
        tmpQualifiedName = tmpQualifiedName + "." + name;
      } else {
        tmpQualifiedName = name;
      }
      mPackage = model.getPackage(tmpQualifiedName);
      if (mPackage == null) {
        org.omg.uml.modelmanagement.UmlPackage umlPackage = mdrLoader
            .createUmlPackage(name);
        umlPackage.setNamespace(context);
        mPackage = model.createPackage(tmpQualifiedName, umlPackage);
      }
      context = (org.omg.uml.modelmanagement.UmlPackage) mPackage
          .getModelElement();
    }
    return mPackage;
  }

  /**
   * Creates a new mdr classifier instance
   * 
   * @param context
   *          the context of the class
   * @param name
   *          the name of the class
   * @return a wrapped mdr classifier
   */
  private MdrUmlClass createClass(MdrUmlPackage context, String name) {
    assert (context != null) && (name != null);
    MdrUmlClass item = findClass(context, name);
    if (item == null) {
      UmlClass umlClass = mdrLoader.createUmlClass(name, 0);
      umlClass.setNamespace((Namespace) context.getModelElement());
      item = new MdrUmlClass(umlClass);
      context.addClass(item);
      mModel.addClass(item);
    }
    return item;
  }

  /**
   * Creates a new mdr classifier instance
   * 
   * @param context
   *          the context of the class
   * @param name
   *          the name of the classifier
   * @return a wrapped mdr classifier
   */
  private MdrUmlClass createClass(MdrUmlClass context, String name) {
    assert (context != null) && (name != null);
    MdrUmlClass item = findClass(context, name);
    if (item == null) {
      UmlClass umlClass = mdrLoader.createUmlClass(name, 0);
      umlClass.setNamespace((Namespace) context.getModelElement());
      item = new MdrUmlClass(umlClass);
      mModel.addClass(item);
    }
    return item;
  }

  /**
   * Creates a new mdr interface instance
   * 
   * @param context
   *          the context of the instance
   * @param name
   *          the name of the interface
   * @return a wrapped mdr interface
   */
  private MdrUmlClass createInterface(MdrUmlPackage context, String name) {
    assert (context != null) && (name != null);
    MdrUmlClass item = findInterface(context, name);
    if (item == null) {
      Interface umlInterface = mdrLoader.createUmlInterface(name, 0);
      item = new MdrUmlClass(umlInterface);
      context.addClass(item);
      mModel.addClass(item);
    }
    return item;
  }

  /**
   * Creates a new mdr interface instance
   * 
   * @param context
   *          the context of the instance
   * @param name
   *          the name of the interface
   * @return a wrapped mdr interface
   */
  private MdrUmlClass createInterface(MdrUmlClass context, String name) {
    assert (context != null) && (name != null);
    MdrUmlClass item = mModel.getClass(name);
    if (item == null) {
      Interface umlInterface = mdrLoader.createUmlInterface(name, 0);
      item = new MdrUmlClass(umlInterface);
    }
    return item;
  }

  /**
   * Finds an attribute with the given name. If not found creates an attribute
   * in the given class.
   * 
   * @param context
   *          class in which the attribute should be created.
   * @param source
   *          name of the attribute to create.
   * @return the requested attribute.
   */

  private MdrUmlAttribute createAttribute(MdrUmlClass context,
      MetaProperty source) {
    assert (context != null) && (source.getName() != null);
    String name = source.getName();
    MdrUmlAttribute item = findAttribute(context, name);

    if (item == null) {
      String typeName = source.getTypeName();
      Classifier attributeType = null;

      try {
        attributeType = (Classifier) mModel.getClass(typeName)
            .getModelElement();
      } catch (NullPointerException e) {

      }

      if ((attributeType == null) && (typeName != null)
          && (mModel.getDataType(typeName) != null)) {
        attributeType = (Classifier) mModel.getDataType(typeName)
            .getModelElement();
      }

      Attribute tmpAttribute = mdrLoader.createUmlAttribute(name,
          VisibilityCode.PUBLIC_ID, attributeType, (Classifier) context
              .getModelElement());

      item = new MdrUmlAttribute(tmpAttribute);
    }
    return item;
  }

  /**
   * Returns a package within the context specified with qualified name. If you
   * set the context different from the entire model please take care to omitt
   * leading package names within the qualified name.
   * 
   * @param context
   *          the entire model or a package to look for the package specified
   *          with the qualified name.
   * @param qualifiedName
   *          qualified name of the package.
   * @return null if no package found or otherwise the specified package.
   * @pre (context != null) && (qualifiedName != null)
   */
  public MdrUmlPackage findPackageQualified(MdrUmlModel context,
      String qualifiedName) {
    assert (context != null) && (qualifiedName != null);

    return context.getPackage(qualifiedName);
  }

  /**
   * Finds the tag with the given name.
   * 
   * @param item
   *          item to look for the tag.
   * @param tagName
   *          name of the tag to find.
   * @return the requested tag if found otherwise null
   * @pre (item != null) && (tagName != null)
   */
  private MdrTaggedValue findTag(MdrUmlElement item, String tagName) {
    assert (item != null) && (tagName != null);
    for (MdrTaggedValue taggedValue : item.getTaggedValues()) {
      if (tagName.equals(taggedValue.getName())) {
        return taggedValue;
      }
    }
    return null;
  }

  /**
   * Returns the package with the given name member of the context.
   * 
   * @param context
   *          context in which to search the package.
   * @param unqualifiedName
   *          name of the requested package.
   * @return the requested package if found otherwise null.
   * @pre (context != null) && (unqualifiedName != null)
   */
  public MdrUmlPackage findPackage(MdrUmlPackage context, String unqualifiedName) {
    assert (context != null) && (unqualifiedName != null);
    if (unqualifiedName.equals(context.getName())) {
      return context;
    }
    for (MdrUmlPackage mPackage : context.getPackages()) {
      if (unqualifiedName.equals(mPackage.getName())) {
        return mPackage;
      }
    }
    return null;
  }

  /**
   * Finds a class within a package.
   * 
   * @param context
   *          package to search
   * @param unqualifiedName
   *          unqualified name of the class to look up
   * @return null if not found or the contained class
   * @pre (context != null) && (unqualifiedName != null)
   */
  private MdrUmlClass findClass(MdrUmlPackage context, String unqualifiedName) {
    assert (context != null) && (unqualifiedName != null);
    MdrUmlClass mClass = null;
    for (MdrUmlClass clazz : context.getClasses()) {
      if (unqualifiedName.equals(clazz.getName())) {
        mClass = clazz;
        break;
      }
      mClass = null;
    }
    return mClass;
  }

  /**
   * Finds a class in the given context
   * 
   * @param context
   *          the context the class is located
   * @param unqualifiedName
   *          the name of the class
   * @return a wrapped mdr class
   */
  private MdrUmlClass findClass(MdrUmlClass context, String unqualifiedName) {
    assert (context != null) && (unqualifiedName != null);
    MdrUmlClass mClass = null;
    for (MdrUmlClass clazz : context.getInnerClasses()) {
      if (unqualifiedName.equals(clazz.getName())) {
        mClass = clazz;
        break;
      }
      mClass = null;
    }
    return mClass;
  }

  /**
   * Searches an interface directly contained within the passed context
   * 
   * @param context
   *          to be searched for the interface
   * @param unqualifiedName
   *          of the interface searched
   * @return Interface if found otherwise null
   * @pre (context != null) && (unqualifiedName != null)
   */
  private MdrUmlClass findInterface(MdrUmlPackage context,
      String unqualifiedName) {
    assert (context != null) && (unqualifiedName != null);
    MdrUmlClass mInterface = null;
    for (MdrUmlClass mdrUmlClass : context.getClasses()) {
      if (unqualifiedName.equals(mdrUmlClass.getName())) {
        mInterface = mdrUmlClass;
        break;
      }
      mInterface = null;
    }
    return mInterface;
  }

  /**
   * Finds an attribute by name
   * 
   * @param context
   *          the context of the attribute
   * @param name
   *          the name of the a ttribute
   * @return a wrapped mdr attribute
   */
  private MdrUmlAttribute findAttribute(MdrUmlClass context, String name) {
    assert (context != null) && (name != null);
    MdrUmlAttribute mAttribute = null;
    for (MdrUmlAttribute candidate : context.getAttributes()) {
      mAttribute = candidate;
      if (name.equals(mAttribute.getName())) {
        break;
      }
      mAttribute = null;
    }
    return mAttribute;
  }

  /**
   * Finds a data type
   * 
   * @param name
   *          of the data type
   * @return a wrapped mdr datatype
   */
  private MdrUmlClass findDataType(String name) {
    try {
      return new MdrUmlClass((Classifier) mModel.getDataType(name)
          .getModelElement());
    } catch (Exception e) {
      return null;
    }
  }

  /**
   * Creates a new classifier instance
   * 
   * @param name
   *          the name of the classifier
   * @param mAttribute
   *          a wrapped mdr attribute
   * @return a wrapped mdr classifier
   */
  private MdrUmlClass createClassifier(String name, MdrUmlAttribute mAttribute) {
    assert (name != null);

    // work a round to handle error afflicted models
    if (name == null) {
      name = "void";
    }

    MdrUmlClass item = null;

    MetaClass mc = metaModel.findClass(name);
    if (mc != null) {
      item = (MdrUmlClass) elements.get(mc);
      if (item == null) {
        missingAttributeTypes.put(mAttribute, mc);
      }
    } else {
      item = findDataType(name);
      if (item == null) {
        DataType dataType = mModel.getUmlPackage().getCore().getDataType()
            .createDataType();
        item = new MdrUmlClass(dataType);
        item.setName(name);
        mModel.getDataTypes().add(new MdrUmlDataType(dataType));
      }
    }
    return item;
  }

  /**
   * Process the attributes with missing type information
   */
  private void processMissingAttributeTypes() {
    MdrUmlClass mClass;
    MetaClass metaClass;
    for (MdrUmlAttribute mAttribute : missingAttributeTypes.keySet()) {
      metaClass = missingAttributeTypes.get(mAttribute);
      mClass = (MdrUmlClass) elements.get(metaClass);
      mAttribute.setType(mClass);
    }
  }

  /**
   * finds the association with the given name defined in the context.
   * 
   * @param context
   *          context where to look for the association.
   * @param clazz
   *          class owning the association.
   * @param name
   *          name of the requested association
   * @return the requested association if found otherwise null.
   */
  private MdrUmlAssociation findAssociation(MdrUmlPackage context,
      MdrUmlClass clazz, String name) {
    assert (context != null) && (clazz != null) && (name != null);
    MdrUmlAssociation association = null;
    for (MdrUmlAssociation candidate : mModel.getAssociations()) {
      association = candidate;
      AssociationEnd e1 = candidate.getConnection(0);
      AssociationEnd e2 = candidate.getConnection(1);
      MdrUmlClass owner = new MdrUmlClass(e1.getParticipant());
      if (owner == clazz) {
        String associationName = e2.getName();
        if ((associationName == null) || (associationName.length() == 0)) {
          associationName = association.getName();
        }
        if (name.equals(associationName)) {
          break;
        }
      }
      association = null;
    }
    return association;
  }

  /**
   * Creates a mdr association
   * 
   * @param source
   *          the mda source
   * @param context
   *          the context of the association
   * @return a wrapped mdr association
   */
  private MdrUmlAssociation createAssociation(MetaProperty source,
      MdrUmlPackage context) {
    assert (source != null) && (context != null);

    AssociationClass tmpAssociation = context.getModel().getUmlPackage()
        .getCore().getAssociationClass().createAssociationClass();
    tmpAssociation.setNamespace(context.getNamespace());

    AssociationEnd myClass = context.getModel().getUmlPackage().getCore()
        .getAssociationEnd().createAssociationEnd();
    AssociationEnd otherEnd = context.getModel().getUmlPackage().getCore()
        .getAssociationEnd().createAssociationEnd();

    tmpAssociation.getConnection().add(0, myClass);
    tmpAssociation.getConnection().add(1, otherEnd);

    tmpAssociation.setName(source.getName());

    myClass.setNavigable(false);
    myClass.setVisibility(VisibilityKindEnum.VK_PUBLIC);
    myClass.setOrdering(OrderingKindEnum.OK_UNORDERED);
    if (source.isAggregation()) {
      myClass
          .setAggregation(org.omg.uml.foundation.datatypes.AggregationKindEnum.AK_AGGREGATE);
    }
    if (source.isComposition()) {
      myClass
          .setAggregation(org.omg.uml.foundation.datatypes.AggregationKindEnum.AK_COMPOSITE);
    }

    myClass.setChangeability(ChangeableKindEnum.CK_CHANGEABLE);
    myClass.setTargetScope(ScopeKindEnum.SK_INSTANCE);

    org.omg.uml.foundation.datatypes.Multiplicity multiplicity = myClass
        .getMultiplicity();

    MultiplicityRange rangeMy = (MultiplicityRange) myClass.getMultiplicity()
        .getRange().iterator().next();
    rangeMy.setLower(source.getMultiplicity().getLowerBound());
    rangeMy.setUpper(source.getMultiplicity().getUpperBound());
    otherEnd.setMultiplicity(rangeMy.getMultiplicity());

    myClass.setParticipant((Classifier) elements.get(source.getContext()));
    Object dataType = elements.get(source.getModel().findClass(
        source.getTypeName()));
    if (dataType == null) {
      dataType = findDataType(source.getTypeName());
    }
    otherEnd.setParticipant((Classifier) dataType);
    otherEnd.setNavigable(true);
    otherEnd.setVisibility(VisibilityKindEnum.VK_PUBLIC);
    otherEnd.setOrdering(OrderingKindEnum.OK_UNORDERED);
    otherEnd
        .setAggregation(org.omg.uml.foundation.datatypes.AggregationKindEnum.AK_NONE);
    otherEnd.setChangeability(ChangeableKindEnum.CK_CHANGEABLE);
    otherEnd.setTargetScope(ScopeKindEnum.SK_INSTANCE);

    MdrUmlAssociation association = new MdrUmlAssociation(tmpAssociation);

    updatePropertyFlags(source, association);
    extractTags(source, association);
    extractStereotypeAndVisibility(source, association);

    MultiplicityRange range = (MultiplicityRange) otherEnd.getMultiplicity()
        .getRange().iterator().next();
    range.setLower(source.getMultiplicity().getLowerBound());
    range.setUpper(source.getMultiplicity().getUpperBound());
    otherEnd.setMultiplicity(range.getMultiplicity());

    return association;
  }

  /**
   * Finds the generalization between passed ancestor and child. It searches
   * within the given package.
   * 
   * @param context
   *          context where to look for the generalization.
   * @param ancestor
   *          class of the generalization relation to be searched.
   * @param child
   *          class of the generalization.
   * @return the requested generalization if found otherwise null.
   * @pre (context != null) && (ancestor != null) && (child != null)
   */
  private MdrUmlGeneralization findGeneralization(MdrUmlPackage context,
      MdrUmlClass ancestor, MdrUmlClass child) {
    assert (context != null) && (ancestor != null) && (child != null);
    MdrUmlGeneralization generalization = null;
    for (MdrUmlGeneralization candidate : context.getModel()
        .getGeneralizations()) {
      if (candidate.getChild().equals(child)
          && candidate.getParent().equals(ancestor)) {
        generalization = candidate;
        break;
      }
    }

    return generalization;
  }

  /**
   * Finds the specialization between passed ancestor interface and child. It
   * searches within the given package.
   * 
   * @param context
   *          context where to look for the generalization.
   * @param ancestor
   *          class of the generalization relation to be searched.
   * @param child
   *          class of the generalization.
   * @return the requested generalization if found otherwise null.
   * @pre (context != null) && (ancestor != null) && (child != null)
   */
  private MdrUmlAbstraction findAbstraction(MdrUmlModel context,
      MdrUmlClass ancestor, MdrUmlClass child) {
    assert (context != null) && (ancestor != null) && (child != null);
    for (MdrUmlPackage mPackage : mModel.getPackages()) {
      for (MdrUmlAbstraction abstraction : mPackage.getAbstractions()) {
        Boolean foundSupplier = false;
        for (Object obj : abstraction.getSuppliers()) {
          if (ancestor.equals(obj)) {
            foundSupplier = true;
            break;
          }
        }
        Boolean foundChild = false;
        for (Object obj : abstraction.getClients()) {
          if (child.equals(obj)) {
            foundChild = true;
            break;
          }
        }
        if (foundSupplier && foundChild) {
          return abstraction;
        }
      }
    }
    return null;
  }

  /**
   * Updates the property flags.
   * 
   * @param property
   *          property with the data to update.
   * @param item
   *          the item to update.
   * @pre (property != null) && (item != null)
   */
  private void updatePropertyFlags(MetaProperty property, MdrUmlElement item) {
    assert (property != null) && (item != null);
    if (property.isVolatile()) {
      MdrTaggedValue taggedValue = createTag(item, UmlConstants.VOLATILE_TAG);
      taggedValue.setValue(TRUE_STRING);
    } else {
      MdrTaggedValue taggedValue = findTag(item, UmlConstants.VOLATILE_TAG);
      if (taggedValue != null) {
        taggedValue.setValue(FALSE_STRING);
      }
    }
    if (property.isTransient()) {
      MdrTaggedValue taggedValue = createTag(item, UmlConstants.TRANSIENT_TAG);
      taggedValue.setValue(TRUE_STRING);
    } else {
      MdrTaggedValue taggedValue = findTag(item, UmlConstants.TRANSIENT_TAG);
      if (taggedValue != null) {
        taggedValue.setValue(FALSE_STRING);
      }
    }
  }

  /**
   * Updates the initial value of a property
   * 
   * @param property
   *          the mda property
   * @param attribute
   *          the mdr attribute
   */
  private void updateInitialValue(MetaProperty property,
      MdrUmlAttribute attribute) {
    String initialValue = property.getInitialValue();
    String expression = attribute.getInitialValue();
    if (expression == null) {
      if (initialValue != null) {
        attribute.setInitialValue(JAVA, initialValue);
      }
    } else {
      if (!expression.equals(initialValue)) {
        attribute.setInitialValue(JAVA, initialValue);
      }
    }
  }

  /**
   * Extracts the information from the MDA element and overwrites the ones of
   * the MDR element defined in the given context.
   * 
   * @param source
   *          MDA element used as source of the synchronization.
   * @param context
   *          MDR context containing the class to synchronize.
   */
  private void extractStereotypeAndVisibility(MetaElement source,
      MdrUmlElement context) {
    // for all stereotypes in pmmda model
    for (Stereotype stereotype : source.getStereotypes()) {
      String stereotypeName = stereotype.getName();
      MdrUmlStereotype mStereotype = findStereotype(stereotypeName);
      if (mStereotype == null) {
        mStereotype = new MdrUmlStereotype(mdrLoader
            .createStereotype(stereotypeName));
      }
      context.addStereotype(mStereotype);
      stereotypeCache = null;
    }
    context.setVisibility(source.getVisibility());
  }

  /**
   * Finds a stereotype with given name at model level
   * 
   * @param name
   *          of the stereotype to look up
   * @return the stereotype or null if not found
   */
  private MdrUmlStereotype findStereotype(String name) {
    if (stereotypeCache == null) {
      stereotypeCache = new HashMap<String, MdrUmlStereotype>();
      for (MdrUmlStereotype stereotype : mModel.getStereotypes()) {
        stereotypeCache.put(stereotype.getName(), stereotype);
      }
    }
    return stereotypeCache.get(name);
  }
}