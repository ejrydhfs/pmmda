/* copyright 2000-2006 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence");
 * you may not use this file except in compliance with the License.
 * $Id$
 */
package ch.bbv.mda.persistence.nsuml;

import java.util.*;

import ru.novosoft.uml.MFactory;
import ru.novosoft.uml.foundation.core.*;
import ru.novosoft.uml.foundation.data_types.*;
import ru.novosoft.uml.foundation.extension_mechanisms.MStereotype;
import ru.novosoft.uml.foundation.extension_mechanisms.MTaggedValue;
import ru.novosoft.uml.foundation.extension_mechanisms.MTaggedValueImpl;
import ru.novosoft.uml.model_management.MModel;
import ru.novosoft.uml.model_management.MPackage;
import ru.novosoft.uml.model_management.MPackageImpl;
import ch.bbv.mda.*;
import ch.bbv.mda.persistence.FilterIterator;

/**
 * The MDA transformer defines rules how to transform a MDA element into the
 * corresponding NS-UML representation. The NSUML representation is XMI 1.0 /
 * UML 1.3 compatible. A feature allows the controlled removal of superfluous
 * tagged values from the NSUML model. A list of tag names, which should be
 * removed can be specified and passed to the transformer. During the
 * synchronization all tags with such names will be removed from the NSUML
 * model.
 * @author Marcel Baumann
 * @version $Revision$
 */
public class Mda2NsUmlTransformer {
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
  public Mda2NsUmlTransformer() {
    elements = new HashMap<Object, Object>();
    missingAttributeTypes = new HashMap<MAttribute, MetaClass>();
  }

  /**
   * Synchronize the NSUML model with the MDA model.
   * @param source MDA model used as source of the synchronization.
   * @param target NSUML model to synchronize.
   * @pre (source != null) && (target != null)
   */
  public void synchronize(MetaModel source, MModel target) {
    assert (source != null) && (target != null);
    mModel = target;
    this.metaModel = source;
    target.setName(source.getName());
    elements.put(source, target);
    
    extractTags(source, target);
    extractStereotypeAndVisibility(source, target);

    extractDataTypes(source, target);
    for (MetaPackage item : source.getPackages()) {
      extractPackage(item, target);
    }
    for (MetaPackage item : source.getPackages()) {
      processPackage(item, target);
    }
    for (MetaClass item : source.getClasses()) {
      extractClass(item, target);
    }
    for (MetaPackage item : source.getPackages()) {
      processAllRelations(item, target);
    }
    for (MetaClass item : source.getClasses()) {
      extractRelation(item, target);
    }
    processMissingAttributeTypes();
  }

  /**
   * Extracts the information from the MDA package and overwrites the ones of
   * the NSUML package defined in the given context.
   * @param source MDA package used as source of the synchronization.
   * @param context NSUML context containing the package to synchronize.
   * @pre (source != null) && (context != null)
   */
  protected void extractPackage(MetaPackage source, MModel context) {
    assert (source != null) && (context != null);
    String qualifiedName = source.getName();
    MPackage mPackage = createPackage(context, qualifiedName);
    elements.put(source, mPackage);
    extractTags(source, mPackage);
    extractStereotypeAndVisibility(source, mPackage);
  }

  /**
   * Processes childeren (classes) of a package
   * @param source MDA package to be processed
   * @param context NSUML context containing the package to be processed.
   * @pre (source != null) && (context != null)
   */
  protected void processPackage(MetaPackage source, MModel context) {
    assert (source != null) && (context != null);
    MPackage mPackage = findPackageQualified(context, source.getName());
    if (mPackage == null)
      return;
    for (MetaClass item : source.getClasses()) {
      extractClass(item, mPackage);
    }
  }

  /**
   * Processes relations within a package
   * @param source MDA package to be processed
   * @param context NSUML context containing the package to be processed.
   * @pre (source != null) && (context != null)
   */
  protected void processAllRelations(MetaPackage source, MModel context) {
    assert (source != null) && (context != null);
    MPackage mPackage = findPackageQualified(context, source.getName());
    if (mPackage == null)
      return;
    for (MetaClass item : source.getClasses()) {
      extractRelation(item, mPackage);
    }
  }

  /**
   * Extracts the information from the MDA class and overwrites the ones of the
   * NSUML class defined in the given context.
   * @param source MDA class used as source of the synchronization.
   * @param context NSUML context containing the class to synchronize.
   * @pre (source != null) && (context != null)
   */
  protected void extractClass(MetaClass source, MPackage context) {
    assert (source != null) && (context != null);
    MClassifier mClass;
    if (source.isInterface()) {
      mClass = createInterface(context, source.getName());
    } else {
      mClass = createClass(context, source.getName());
    }
    elements.put(source, mClass);
    mClass.setAbstract(source.isAbstract());
    extractTags(source, mClass);
    extractStereotypeAndVisibility(source, mClass);
    for (MetaClass item : source.getNestedClasses()) {
      extractNestedClass(item, mClass);
    }
  }

  /**
   * Extracts aggregations, indexed properties, properties, realizes and
   * specializes relations from a classifier
   * @param source MDA class used as source of the synchronization.
   * @param context NSUML context containing the class to synchronize.
   * @pre (source != null) && (context != null)
   */
  protected void extractRelation(MetaClass source, MPackage context) {
    assert (source != null) && (context != null);
    // find class the relations relate to
    MClassifier mClass = findClassifierQualified(context, source
        .getQualifiedName());
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
   * Creates a generalization relation within the NSUML model. If ancestor class
   * is already defined then it will be found. If not it will be created.
   * Detailed information of the ancestor class will be added during processing
   * the corresponding extractClass method
   * @param ancestor of the generalization relation
   * @param child of the generalization relation
   */
  protected void extractAncestor(MetaClass ancestor, MClassifier child) {
    MClassifier mAncestor = findClassifierQualified(mModel, ancestor
        .getQualifiedName());
    MPackage ancestorContext = findPackageQualified(mModel, ancestor
        .retrievePackage().getName());
    if (mAncestor == null) {
      mAncestor = createClass(ancestorContext, ancestor.getName());
    }
    MGeneralization generalization = findGeneralization(ancestorContext,
        mAncestor, child);
    if (generalization == null) {
      generalization = new MGeneralizationImpl();
      generalization.setNamespace(mModel);
      generalization.setChild(child);
      generalization.setParent(mAncestor);
      ancestorContext.addOwnedElement(generalization);
    }
  }

  /**
   * Creates a generalization relation within the NSUML model. If ancestor
   * interface is already defined then it will be found. If not it will be
   * created. Detailed information of the ancestor interface will be added
   * during the corresponding extractInterface method. Note: A realize relation
   * will be mapped to a generalization relation
   * @param ancestorIf of the generalization relation
   * @param child implements the interface
   */

  protected void extractInterface(MetaClass ancestorIf, MClassifier child) {
    MClassifier parent = findClassifierQualified(mModel, ancestorIf
        .getQualifiedName());
    MPackage ancestorContext = findPackageQualified(mModel, ancestorIf
        .retrievePackage().getName());
    if (parent == null) {
      parent = createInterface(ancestorContext, ancestorIf.getName());
    }
    MAbstraction abstraction = findAbstraction(mModel, parent, child);
    if (abstraction == null) {
      abstraction = new MAbstractionImpl();
      abstraction.setNamespace(mModel);
      abstraction.addClient(child);
      abstraction.addSupplier(parent);
      ancestorContext.addOwnedElement(abstraction);
      MStereotype mStereotype = findStereotype("realize");
      if (mStereotype == null) {
        mStereotype = MFactory.getDefaultFactory().createStereotype();
        mStereotype.setName("realize");
        mModel.addOwnedElement(mStereotype);
      }
      abstraction.setStereotype(mStereotype);
    }
  }

  /**
   * Extracts the information from the MDA class and overwrites the ones of the
   * NSUML nested classes defined in the given context.
   * @param source MDA class used as source of the synchronization.
   * @param context NSUML context containing the class to synchronize.
   */
  protected void extractNestedClass(MetaClass source, MClassifier context) {
    assert (source != null) && (context != null);
    MClassifier mClass;
    if (source.isInterface()) {
      mClass = createInterface(context, source.getName());
    } else {
      mClass = createClass(context, source.getName());
    }
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
   * the NSUML class defined in the given context.
   * @param source MDA property used as source of the synchronization.
   * @param context NSUML context containing the property to synchronize.
   */
  protected void extractProperty(MetaProperty source, MClassifier context) {
    assert (source != null) && (context != null);
    if (source.isAggregation() || source.isComposition() || source.isRelation()) {
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
        if (source.isAggregation()) {
          association.getConnection(0).setAggregation(
              MAggregationKind.AGGREGATE);
        }
        if (source.isComposition()) {
          association.getConnection(0).setAggregation(
              MAggregationKind.COMPOSITE);
        }
      } else {
        association = createAssociation(source, mPackage);
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
      MClassifier context) {
    assert (source != null) && (context != null);
    if (source.isAggregation() || source.isComposition() || source.isRelation()) {
      MetaElement mElement = source.getContext().getContext();
      while (!(mElement instanceof MetaPackage)) {
        mElement = mElement.getContext();
      }
      MPackage mPackage = (MPackage) elements.get(mElement);
      MAssociation association = findAssociation(mPackage, context, source
          .getName());
      if (association != null) {
        updatePropertyFlags(source, association);
        extractTags(source, association);
        extractStereotypeAndVisibility(source, association);
      } else {
        association = createAssociation(source, mPackage);
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
      MMultiplicity multiplicity = mAttribute.getMultiplicity();
      MMultiplicity newMultiplicity = convert(source.getMultiplicity());
      if ((newMultiplicity != null) && (multiplicity != newMultiplicity)) {
        mAttribute.setMultiplicity(newMultiplicity);
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
   * @param source MDA source item containing the tags to synchronize.
   * @param item target item to synchronize.
   */
  private void extractTags(MetaElement source, MModelElement item) {
    String description = source.getDescription();
    if ((description != null) && (description.length() > 0)) {
      MTaggedValue taggedValue = createTag(item, UmlConstants.DOCUMENTATION_TAG);
      taggedValue.setValue(description);
    } else {
      MTaggedValue taggedValue = findTag(item, UmlConstants.DOCUMENTATION_TAG);
      if (taggedValue != null) {
        taggedValue.setValue("");
      }
    }
    // synchronizes all the source tags with the target ones
    for (TaggedValue tag : source.getTags()) {
      String tagName = tag.getTag();
      String tagValue = tag.getValue();
      MTaggedValue taggedValue = createTag(item, tagName);
      taggedValue.setValue(tagValue != null ? tagValue : "");
    }
    // removes the tags no more available in the source. These tags are the 
    // ones declared obsolete in the loaded cartridges - take care of 
    // documentation tag
    for (Iterator i = item.getTaggedValues().iterator(); i.hasNext();) {
      MTaggedValue tag = (MTaggedValue) i.next();
      String tagName = tag.getTag();
      if (!UmlConstants.DOCUMENTATION_TAG.equalsIgnoreCase(tagName)
          && (source.getTaggedValue(tag.getTag()) == null)) {
        item.removeTaggedValue(tag);
      }
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

  private MClass createClass(MClassifier context, String name) {
    assert (context != null) && (name != null);
    MClass item = findClass(context, name);
    if (item == null) {
      item = new MClassImpl();
      item.setName(name);
      context.addOwnedElement(item);
    }
    return item;
  }

  private MInterface createInterface(MPackage context, String name) {
    assert (context != null) && (name != null);
    MInterface item = findInterface(context, name);
    if (item == null) {
      item = new MInterfaceImpl();
      item.setName(name);
      context.addOwnedElement(item);
    }
    return item;
  }

  private MInterface createInterface(MClassifier context, String name) {
    assert (context != null) && (name != null);
    MInterface item = findInterface(context, name);
    if (item == null) {
      item = new MInterfaceImpl();
      item.setName(name);
      context.addOwnedElement(item);
    }
    return item;
  }

  /**
   * Finds an attribute with the given name. If not found creates an attribute
   * in the given class.
   * @param context class in which the attribute should be created.
   * @param name name of the attribute to create.
   * @return the requested attribute.
   */
  private MAttribute createAttribute(MClassifier context, String name) {
    assert (context != null) && (name != null);
    MAttribute item = findAttribute(context, name);
    if (item == null) {
      item = new MAttributeImpl();
      item.setName(name);
      context.addFeature(item);
    }
    return item;
  }

  /**
   * Returns a package within the context specified with qualified name. If you
   * set the context different from the entire model please take care to omitt
   * leading package names within the qualified name.
   * @param context the entire model or a package to look for the package
   *          specified with the qualified name.
   * @param qualifiedName qualified name of the package.
   * @return null if no package found or otherwise the specified package.
   * @pre (context != null) && (qualifiedName != null)
   */
  public MPackage findPackageQualified(MPackage context, String qualifiedName) {
    assert (context != null) && (qualifiedName != null);
    MPackage mPackage = null;
    StringTokenizer tokenizer = new StringTokenizer(qualifiedName, ".");
    while (tokenizer.hasMoreTokens()) {
      String name = tokenizer.nextToken();
      mPackage = findPackage(context, name);
      if (mPackage == null) {
        return null;
      }
      context = mPackage;
    }
    return mPackage;
  }

  /**
   * Returns a class within the context specified with qualified name. If you
   * set the context different from the entire model please take care to omitt
   * leading package names within the qualified name.
   * @param context the entire model or a package to look for the class
   *          specified with the qualified name.
   * @param qualifiedName of the class.
   * @return null if no corresponding class found or the class found.
   * @pre (context != null) && (qualifiedName != null)
   */
  public MClassifier findClassifierQualified(MPackage context,
      String qualifiedName) {
    assert (context != null) && (qualifiedName != null);
    // test whether context qualified name is at the beginning of qualifiedName
    String qualifiedContext = NsUmlUtilities.getQualifiedName(context);
    if (qualifiedName.startsWith(qualifiedContext)
        && qualifiedName.charAt(qualifiedContext.length()) == '.') {
      qualifiedName = qualifiedName.substring(qualifiedContext.length() + 1);
    }
    StringTokenizer tokenizer = new StringTokenizer(qualifiedName, ".");
    MPackage currentPackage = null;
    MClassifier currentClassifier = null;
    String residuum = "";
    while (tokenizer.hasMoreTokens()) {
      String name = tokenizer.nextToken();
      residuum += name + ".";
      currentPackage = findPackage(context, name);
      if (currentPackage == null) {
        currentClassifier = findClass(context, name);
      }
      if (currentPackage == null && currentClassifier == null) {
        return null;
      }
      if (currentPackage != null) {
        context = currentPackage;
      } else {
        if (tokenizer.hasMoreTokens()) {
          qualifiedName = qualifiedName.substring(residuum.length());
          return findClassifierQualified(currentClassifier, qualifiedName);
        }
      }
    }
    return currentClassifier;
  }

  public MClassifier findClassifierQualified(MClassifier context,
      String qualifiedName) {
    assert (context != null) && (qualifiedName != null);
    String qualifiedContext = NsUmlUtilities.getQualifiedName(context);
    if (qualifiedName.startsWith(qualifiedContext)
        && qualifiedName.charAt(qualifiedContext.length()) == '.') {
      qualifiedName = qualifiedName.substring(qualifiedContext.length() + 1);
    }
    StringTokenizer tokenizer = new StringTokenizer(qualifiedName, ".");
    MClassifier currentClassifier = null;
    while (tokenizer.hasMoreTokens()) {
      String name = tokenizer.nextToken();
      currentClassifier = findClass(context, name);
      if (currentClassifier == null)
        return null;
      context = currentClassifier;
    }
    return context;
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
   * Returns the package with the given name member of the context.
   * @param context context in which to search the package.
   * @param unqualifiedName name of the requested package.
   * @return the requested package if found otherwise null.
   * @pre (context != null) && (unqualifiedName != null)
   */
  public MPackage findPackage(MPackage context, String unqualifiedName) {
    assert (context != null) && (unqualifiedName != null);
    if (unqualifiedName.equals(context.getName())) {
      return context;
    }
    for (Iterator<MPackage> i = new FilterIterator<MPackage>(context
        .getOwnedElements(), MPackage.class); i.hasNext();) {
      MPackage mPackage = i.next();
      if (unqualifiedName.equals(mPackage.getName())) {
        return mPackage;
      }
    }
    return null;
  }

  /**
   * Finds a class within a package.
   * @param context package to search
   * @param unqualifiedName unqualified name of the class to look up
   * @return null if not found or the contained class
   * @pre (context != null) && (unqualifiedName != null)
   */
  private MClass findClass(MPackage context, String unqualifiedName) {
    assert (context != null) && (unqualifiedName != null);
    MClass mClass = null;
    for (Iterator<MClass> i = new FilterIterator<MClass>(context
        .getOwnedElements(), MClass.class); i.hasNext();) {
      mClass = i.next();
      if (unqualifiedName.equals(mClass.getName())) {
        break;
      }
      mClass = null;
    }
    return mClass;
  }

  /**
   * Finds a class within a class (nested class) or, because the model supports
   * it, within an interface
   * @param context class or interface to search
   * @param unqualifiedName unqualified name of the class to look up
   * @return null if not found or the contained class
   * @pre (context != null) && (unqualifiedName != null)
   */
  private MClass findClass(MClassifier context, String unqualifiedName) {
    assert (context != null) && (unqualifiedName != null);
    MClass mClass = null;
    for (Iterator<MClass> i = new FilterIterator<MClass>(context
        .getOwnedElements(), MClass.class); i.hasNext();) {
      mClass = i.next();
      if (unqualifiedName.equals(mClass.getName())) {
        break;
      }
      mClass = null;
    }
    return mClass;
  }

  /**
   * Searches an interface directly contained within the passed context
   * @param context to be searched for the interface
   * @param unqualifiedName of the interface searched
   * @return MInterface if found otherwise null
   * @pre (context != null) && (unqualifiedName != null)
   */
  private MInterface findInterface(MPackage context, String unqualifiedName) {
    assert (context != null) && (unqualifiedName != null);
    MInterface mInterface = null;
    for (Iterator<MInterface> i = new FilterIterator<MInterface>(context
        .getOwnedElements(), MInterface.class); i.hasNext();) {
      mInterface = i.next();
      if (unqualifiedName.equals(mInterface.getName())) {
        break;
      }
      mInterface = null;
    }
    return mInterface;
  }

  /**
   * Searches an interface directly contained within the passed context
   * @param context to be searched for the interface
   * @param unqualifiedName of the interface searched
   * @return MInterface if found otherwise null
   */
  private MInterface findInterface(MClassifier context, String unqualifiedName) {
    assert (context != null) && (unqualifiedName != null);
    MInterface mClass = null;
    for (Iterator<MInterface> i = new FilterIterator<MInterface>(context
        .getOwnedElements(), MInterface.class); i.hasNext();) {
      mClass = i.next();
      if (unqualifiedName.equals(mClass.getName())) {
        break;
      }
      mClass = null;
    }
    return mClass;
  }

  private MAttribute findAttribute(MClassifier context, String name) {
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

    MetaClass mc = metaModel.findClass(name);
    if (mc != null) {
      item = (MClassifier) elements.get(mc);
      if (item == null) {
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
  private MAssociation findAssociation(MPackage context, MClassifier clazz,
      String name) {
    assert (context != null) && (clazz != null) && (name != null);
    MAssociation association = null;
    for (Iterator<MAssociation> i = new FilterIterator<MAssociation>(context
        .getOwnedElements(), MAssociation.class); i.hasNext();) {
      association = i.next();
      MAssociationEnd e1 = association.getConnection(0);
      MAssociationEnd e2 = association.getConnection(1);
      MClassifier owner = e1.getType();
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

  private MAssociation createAssociation(MetaProperty source, MPackage context) {
    assert (source != null) && (context != null);

    MAssociation association = new MAssociationImpl();
    association.setNamespace(mModel);
    MAssociationEnd myClass = new MAssociationEndImpl();
    MAssociationEnd otherEnd = new MAssociationEndImpl();
    association.addConnection(0, myClass);
    association.addConnection(1, otherEnd);

    association.setName(source.getName());
    context.addOwnedElement(association);

    myClass.setNavigable(false);
    myClass.setVisibility(MVisibilityKind.PUBLIC);
    myClass.setOrdering(MOrderingKind.UNORDERED);
    if (source.isAggregation()) {
      myClass.setAggregation(MAggregationKind.AGGREGATE);
    }
    if (source.isComposition()) {
      myClass.setAggregation(MAggregationKind.COMPOSITE);
    }
    myClass.setChangeability(MChangeableKind.CHANGEABLE);
    myClass.setTargetScope(MScopeKind.INSTANCE);
    myClass.setMultiplicity(MMultiplicity.M1_1);

    myClass.setType((MClassifier) elements.get(source.getContext()));
    Object o = elements.get(source.getModel().findClass(source.getTypeName()));
    if (o == null) {
      o = findDataType(source.getTypeName());
    }
    otherEnd.setType((MClassifier) o);
    otherEnd.setNavigable(true);
    otherEnd.setVisibility(MVisibilityKind.PUBLIC);
    otherEnd.setOrdering(MOrderingKind.UNORDERED);
    otherEnd.setAggregation(MAggregationKind.NONE);
    otherEnd.setChangeability(MChangeableKind.CHANGEABLE);
    otherEnd.setTargetScope(MScopeKind.INSTANCE);
    updatePropertyFlags(source, association);
    extractTags(source, association);
    extractStereotypeAndVisibility(source, association);
    MMultiplicity newMultiplicity = convert(source.getMultiplicity());
    otherEnd.setMultiplicity(newMultiplicity);
    return association;
  }

  /**
   * Finds the generalization between passed ancestor and child. It searches
   * within the given package.
   * @param context context where to look for the generalization.
   * @param ancestor class of the generalization relation to be searched.
   * @param child class of the generalization.
   * @return the requested generalization if found otherwise null.
   * @pre (context != null) && (ancestor != null) && (child != null)
   */
  private MGeneralization findGeneralization(MPackage context,
      MClassifier ancestor, MClassifier child) {
    assert (context != null) && (ancestor != null) && (child != null);
    MGeneralization generalization = null;
    for (Iterator<MGeneralization> i = new FilterIterator<MGeneralization>(
        context.getOwnedElements(), MGeneralization.class); i.hasNext();) {
      MGeneralization candidate = i.next();
      if (candidate.getChild().equals(child)
          && candidate.getParent().equals(ancestor)) {
        generalization = candidate;
        break;
      }
    }
    if (generalization == null) {
      context = NsUmlUtilities.getContext(context);
      if (context != null) {
        generalization = findGeneralization(context, ancestor, child);
      }
    }
    return generalization;
  }

  /**
   * Finds the specialization between passed ancestor interface and child. It
   * searches within the given package.
   * @param context context where to look for the generalization.
   * @param ancestor class of the generalization relation to be searched.
   * @param child class of the generalization.
   * @return the requested generalization if found otherwise null.
   * @pre (context != null) && (ancestor != null) && (child != null)
   */
  private MAbstraction findAbstraction(MPackage context, MClassifier ancestor,
      MClassifier child) {
    assert (context != null) && (ancestor != null) && (child != null);
    MAbstraction abstraction = null;
    for (Iterator<MPackage> p = new FilterIterator<MPackage>(context
        .getOwnedElements(), MPackage.class); p.hasNext();) {
      MPackage mPackage = p.next();
      for (Iterator<MAbstraction> i = new FilterIterator<MAbstraction>(mPackage
          .getOwnedElements(), MAbstraction.class); i.hasNext();) {
        abstraction = i.next();
        Boolean foundSupplier = false;
        for (Iterator<MClassifier> s = new FilterIterator<MClassifier>(
            abstraction.getSuppliers(), MClassifier.class); s.hasNext();) {
          if (s.next().equals(ancestor)) {
            foundSupplier = true;
            break;
          }
        }
        Boolean foundChild = false;
        for (Iterator<MClassifier> c = new FilterIterator<MClassifier>(
            abstraction.getClients(), MClassifier.class); c.hasNext();) {
          if (c.next().equals(child)) {
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
   * @param property property with the data to update.
   * @param item the item to update.
   * @pre (property != null) && (item != null)
   */
  private void updatePropertyFlags(MetaProperty property, MModelElement item) {
    assert (property != null) && (item != null);
    if (property.isVolatile()) {
      MTaggedValue taggedValue = createTag(item, UmlConstants.VOLATILE_TAG);
      taggedValue.setValue(TRUE_STRING);
    } else {
      MTaggedValue taggedValue = findTag(item, UmlConstants.VOLATILE_TAG);
      if (taggedValue != null) {
        taggedValue.setValue(FALSE_STRING);
      }
    }
    if (property.isTransient()) {
      MTaggedValue taggedValue = createTag(item, UmlConstants.TRANSIENT_TAG);
      taggedValue.setValue(TRUE_STRING);
    } else {
      MTaggedValue taggedValue = findTag(item, UmlConstants.TRANSIENT_TAG);
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
  /**
   * @param source
   * @param context
   */
  private void extractStereotypeAndVisibility(MetaElement source,
      MModelElement context) {
    List<Stereotype> stereotypes = source.getStereotypes();
    MStereotype mStereotype = context.getStereotype();
    if ((stereotypes.size()) > 0 && (stereotypes.get(0).getName().length() > 0)) {
      String stereotype = stereotypes.get(0).getName();
      mStereotype = findStereotype(stereotype);
      if (mStereotype == null) {
        mStereotype = MFactory.getDefaultFactory().createStereotype();
        mStereotype.setName(stereotype);
        mModel.addOwnedElement(mStereotype);
      }
      context.setStereotype(mStereotype);
    } else if (mStereotype != null) {
      mStereotype.setName("");
    }

    NsUmlUtilities.setVisibility(context, source.getVisibility());
  }

  /**
   * Finds a stereotype with given name at model level
   * @param name of the stereotype to look up
   * @return the stereotype or null if not found
   */
  private MStereotype findStereotype(String name) {
    for (Iterator<MStereotype> i = new FilterIterator<MStereotype>(mModel
        .getOwnedElements(), MStereotype.class); i.hasNext();) {
      MStereotype mStereotype = i.next();
      if (mStereotype.getName().equals(name)) {
        return mStereotype;
      }
    }
    return null;
  }
}