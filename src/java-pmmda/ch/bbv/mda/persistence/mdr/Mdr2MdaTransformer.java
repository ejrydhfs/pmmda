/* copyright 2000-2006 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence");
 * you may not use this file except in compliance with the License.
 * $Id$
 */
package ch.bbv.mda.persistence.mdr;

import java.util.HashMap;
import java.util.Map;

import javax.jmi.model.AggregationKindEnum;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.omg.uml.foundation.core.AssociationEnd;
import org.omg.uml.foundation.core.ModelElement;

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
import ch.bbv.mda.operators.MdaUtilities;
import ch.bbv.mda.persistence.ViewTransformer;

public class Mdr2MdaTransformer extends ViewTransformer {

  private static final String DOT = ".";
  private static final String VOLATILE = "volatile";
  private static final String TRANSIENT = "transient";

  /**
   * Logger of the class.
   */
  private static Log log = LogFactory.getLog(Mdr2MdaTransformer.class);

  /**
   * Maps between the source objects used as keys and the target objects used as
   * values. Source objects are wrapped MDR entities, target objects are MDA
   * instances.
   */
  private Map<Object, MetaElement> elements;

  private MdrUmlModel sourceModel;

  /**
   * Default constructor of the class.
   */
  public Mdr2MdaTransformer() {
    super();
  }

  /**
   * Synchronize the MDA model with the MDR model.
   * 
   * @param mdrModel
   *          MDR model used as source of the synchronization.
   * @param mdaModel
   *          MDA model to synchronize.
   */
  public synchronized void synchronize(MdrUmlModel mdrModel, MetaModel mdaModel) {
    log.info("start MDR XMI 1.2 import...");
    elements = new HashMap<Object, MetaElement>();
    sourceModel = mdrModel;
    setMetaModel(mdaModel);

    mdaModel.setName(mdrModel.getName());
    extractDatatypes(mdrModel, mdaModel);
    extractStereotypes(mdrModel, mdaModel);

    // extract packages
    for (MdrUmlPackage mdrUmlPackage : mdrModel.getPackages()) {
      // process root package specially
      if (mdrModel.getName().equals(mdrUmlPackage.getName())) {
        for (MdrUmlClass mdrUmlClass : mdrUmlPackage.getClasses()) {
          extractClass(mdrUmlClass, mdaModel);
        }
        MdrTaggedValue taggedValue = mdrUmlPackage.getTaggedValue(VIEW_TAG);
        if(taggedValue != null){
          addCollectedView(null, mdaModel, taggedValue.getMdaTaggedValue());
        }
      } else {
        extractPackage(mdrUmlPackage, mdaModel);
      }
    }
    
    extractRelations(mdaModel, mdrModel);
    postProcessModel(mdaModel);
    extractViews(mdaModel);
  }

  /**
   * Extracts the information from the MDR package and overwrites the ones of
   * the MDA package defined in the given context.
   * 
   * @param source
   *          MDR package used as source of the synchronization.
   * @param context
   *          MDA context containing the package to synchronize.
   */
  private void extractPackage(MdrUmlPackage source, MetaModel context) {
    String qualifiedName = source.getQualifiedName();

    if (qualifiedName.indexOf(DOT) == 0) {
      return;
    }

    MetaPackage metaPackage = context.findPackage(qualifiedName);

    if (metaPackage == null) {
      metaPackage = new MetaPackage();

      // truncate the model name from the qualified name
      if (qualifiedName.length() > sourceModel.getName().length()) {
        qualifiedName = qualifiedName
            .substring(sourceModel.getName().length() + 1);
      }

      metaPackage.setName(qualifiedName);
      context.addPackage(metaPackage);
    }

    // extract stereotypes and tagged values
    extractStereotypeAndTags(source, metaPackage);

    // process owned packages
    for (MdrUmlPackage mSubPackage : source.getPackages()) {
      extractPackage(mSubPackage, context);
    }

    // process owned classes
    for (MdrUmlClass mClass : source.getClasses()) {
      extractClass(mClass, metaPackage);
    }

    
    MdrTaggedValue value = source.getTaggedValue(VIEW_TAG);
    if(value != null){
      this.addCollectedView(null, metaPackage, new TaggedValue(value.getName(),
          value.getValue()));
    }

  }

  /**
   * Extracts the information from the MDR class and overwrites the ones of the
   * MDA class defined in the given context.
   * 
   * @param source
   *          MDR class used as source of the synchronization.
   * @param context
   *          MDA context containing the class to synchronize.
   */
  private void extractClass(MdrUmlClass source, MetaPackage context) {
    assert (source != null) && (context != null);
    String name = source.getName();
    if (name == null) {
      if (log.isErrorEnabled()) {
        log.error("Parsed class without name in package " + context.getName());
      }
      return;
    }
    MetaClass metaClass = context.getClass(name);
    if (metaClass == null) {
      metaClass = new MetaClass();
      metaClass.setName(name);
      //metaClass.setDescription(source.getDescription());
      context.addClass(metaClass);
    }

    elements.put(source.getModelElement(), metaClass);
    metaClass.setAbstract(source.isAbstract());
    metaClass.setFinal(source.isFinal());
    metaClass.setStatic(source.isStatic());

    extractStereotypeAndTags(source, metaClass);

    // set interface
    metaClass.setInterface(source.isInterface());

    // process attributes
    for (MdrUmlAttribute attribute : source.getAttributes()) {
      extractProperty(attribute, metaClass);
    }

    // process inner classes
    for (MdrUmlClass innerClass : source.getInnerClasses()) {
      extractNestedClass(innerClass, metaClass);
    }
  }

  /**
   * Extracts the information from the MDR class and overwrites the ones of the
   * MDA nested classes defined in the given context.
   * 
   * @param source
   *          MDR class used as source of the synchronization.
   * @param context
   *          MDA context containing the class to synchronize.
   */
  private void extractNestedClass(MdrUmlClass source, MetaClass context) {
    assert (source != null) && (context != null);

    String name = source.getName();
    if (name == null) {
      if (log.isErrorEnabled()) {
        log.error("Parsed nested class without name in class "
            + context.getName());
      }
      return;
    }

    MetaClass metaNestedClass = context.getNestedClass(name);

    if (metaNestedClass == null) {
      metaNestedClass = new MetaClass();
      metaNestedClass.setName(name);
      context.addNestedClass(metaNestedClass);
    }

    elements.put(source.getModelElement(), metaNestedClass);
    metaNestedClass.setAbstract(source.isAbstract());
    extractStereotypeAndTags(source, metaNestedClass);

    if (source.isInterface()) {
      metaNestedClass.setInterface(true);
    }

    // attributes
    for (MdrUmlAttribute attribute : source.getAttributes()) {
      extractProperty(attribute, metaNestedClass);
    }

    // process inner classes
    for (MdrUmlClass innerClass : source.getInnerClasses()) {
      extractNestedClass(innerClass, metaNestedClass);
    }
  }

  /**
   * Extracts the property from the attribute and synchronizes the target.
   * 
   * @param attribute
   *          attribute containing the data.
   * @param context
   *          context in which the property will be updated.
   */
  private void extractProperty(MdrUmlAttribute attribute, MetaClass context) {
    Multiplicity mMultiplicity = attribute.getMultiplicity();
    MetaProperty property;
     
    
    // Decide: Property OR Indexed Property?
    if (mMultiplicity.getUpperBound() == 1) {
      property = new MetaProperty();
      context.addProperty(property);
    } else {
      property = new MetaIndexedProperty();
      context.addIndexedProperty((MetaIndexedProperty)property);
    }
    property.setAggregation(attribute.isAggregation());
    
    // set the name
    property.setName(attribute.getName());

    // set the initial value
    if (attribute.getInitialValue() == null) {
      property.setInitialValue(attribute.getInitialValue());
    }
    
    // set data type
    if (attribute.getType().getQualifiedName() != null) {
      property.setTypeName(attribute.getType().getQualifiedName());
    } else {
      log.info("Attribute '" + attribute.getName() + "' of class '"
          + context.getName()
          + "' has no type associated (set to datatype integer)");
      property.setTypeName("int");
    }

    // set multiplicity
    Multiplicity multiplicity = attribute.getMultiplicity();
    if (multiplicity != null) {
      property.setMultiplicity(multiplicity);
    }
    
    // extract stereotypes
    extractStereotypeAndTags(attribute, property);
    collectViewProperties(property, context, attribute);
    //property.setAggregation(true);
    
  }

  /**
   * Extracts tags and stereotypes from the MDR element and add them to the meta
   * element. The visibility of the item is also set.
   * 
   * @param source
   *          MDR element to parse
   * @param item
   *          meta item to synchronize
   */
  private void extractStereotypeAndTags(MdrUmlElement source, MetaElement item) {
    for (MdrUmlStereotype mStereotype : source.getStereotypes()) {
      if (mStereotype != null) {
        MdaUtilities.addStereotype(item, mStereotype.getName());
      }
    }
    item.setVisibility(source.getVisibility());
    for (MdrTaggedValue tag : source.getTaggedValues()) {
      String tagName = tag.getName();
      if (item instanceof MetaProperty) {
        MetaProperty property = (MetaProperty) item;
        if (TRANSIENT.equals(tagName)) {
          property.setTransient(booleanValue(tag.getValue()));
        } else if (VOLATILE.equals(tagName)) {
          property.setVolatile(booleanValue(tag.getValue()));
        }
      }
      if (UmlConstants.DOCUMENTATION_TAG.equals(tagName)) {
        item.setDescription(tag.getValue());
      } else {
        if (tagName == null) {
          log.warn("Tagged Value has no name and has value." + tag.getValue());
          continue;
        }
        TaggedValue taggedValue = item.getTaggedValue(tagName);
        if (taggedValue == null) {
          taggedValue = new TaggedValue(tag.getName(), tag.getValue());
          item.addTaggedValue(taggedValue);
        } else {
          taggedValue.setValue(tag.getValue());
        }
      }
    }
  }

  /**
   * Extracts all inheritance, implement, composition and aggregation relations
   * defined in the given package and its children. Generalizations are
   * inheritance relations and abstractions are implement relations in the MDR
   * terminology. The relations are mapped to the model of modern programming
   * languages. Aggregations with a upper cardinality of 1 are mapped to simple
   * properties, all others are mapped to indexed properties.
   * 
   * @param target
   *          meta model to complete with the defined relations.
   * @param source
   *          package, which relations should be extracted.
   * @pre (target != null) && (mPackage != null)
   */

  private void extractRelations(MetaModel target, MdrUmlModel source) {
    assert (target != null) && (source != null);

   processGeneralization(source);
   processAbstractions(source);
   processAssociations(target, source);

    // process abstractions
  }
  
  /**
   * Process Generalizations. (see {@link #extractRelations(MetaModel, MdrUmlModel)}
   * @param metaModel
   * @param source
   */
  private void processGeneralization(MdrUmlModel source){
    // process generalizations
    for (MdrUmlGeneralization generalization : source.getGeneralizations()) {
      MetaClass superClass = (MetaClass) elements.get(generalization
          .getParent());
      MetaClass metaClass = (MetaClass) elements.get(generalization.getChild());

      if ((superClass != null) && (metaClass != null)) {
        metaClass.addAncestor(superClass);
      }
    }
  }
  
  /**
   * Process abstractions (see {@link #extractRelations(MetaModel, MdrUmlModel)}
   * @param metaModel
   * @param source
   */
  private void processAbstractions(MdrUmlModel source){
    for (MdrUmlAbstraction abstraction : source.getAbstractions()) {
      if ((abstraction.getSuppliers().size() == 0)
          || (abstraction.getClients().size() == 0)) {
        if (log.isErrorEnabled()) {
          log.error("Association (MAbstraction) without end "
              + abstraction.getName() + " in package " + source.getName());
        }
        continue;
      }
      ModelElement supplier = (ModelElement) abstraction.getSuppliers()
          .iterator().next();
      ModelElement client = (ModelElement) abstraction.getClients().iterator()
          .next();
      MetaClass metaClass = (MetaClass) elements.get(client);
      MetaClass superClass = (MetaClass) elements.get(supplier);
      if ((superClass != null) && (metaClass != null)) {
        metaClass.addAncestor(superClass);
      }
    }
  }
  
  /**
   * Process Associations. (see {@link #extractRelations(MetaModel, MdrUmlModel)}
   * @param metaModel
   * @param source
   */
  private void processAssociations(MetaModel target, MdrUmlModel source){
    for (MdrUmlAssociation association : source.getAssociations()) {
      AssociationEnd e1 = association.getConnection(0);
      AssociationEnd e2 = association.getConnection(1);
      association.getName();
      if ((e1 == null) || (e2 == null)) {
        if (log.isErrorEnabled()) {
          log.error("Association (UmlAssociation) without end(s) "
              + association.getName() + " in package " + source.getName());
        }
        continue;
      }

      MetaClass owner = (MetaClass) elements.get(e1.getParticipant());
      MetaClass owned = (MetaClass) elements.get(e2.getParticipant());

      String name = e2.getName();
      if ((name == null) || (name.length() == 0)) {
        name = association.getName();
      }
      
      if (owner == null) {
        if (log.isErrorEnabled()) {
          log.error("No owner for association " + association.getName()
              + " in package " + source.getName());
        }
        continue;
      }

      Multiplicity multiplicity = MdrUtilities.convertMultiplicity(e2.getMultiplicity());
      
      MetaProperty property = null;
      
      if (((multiplicity != null) && (multiplicity.getUpperBound() == Multiplicity.N_MULTIPLICITY))
          || (multiplicity == null)) {
        property = owner.findIndexedProperty(name);
        if(property == null){
          property = new MetaIndexedProperty();
          owner.addIndexedProperty((MetaIndexedProperty) property);
        }
      } else {
        property = owner.findProperty(name);
        if(property == null){
          property = new MetaProperty();
          owner.addProperty(property);
        }
      }
      
      property.setName(name);
      
      if (owned != null) {
        property.setTypeName(owned.getQualifiedName());
      }

      if (e1.getAggregation().equals(
          org.omg.uml.foundation.datatypes.AggregationKindEnum.AK_AGGREGATE)) {
        property.setAggregation(true);
      }

      if (e1.getAggregation().equals(
          org.omg.uml.foundation.datatypes.AggregationKindEnum.AK_COMPOSITE)) {
        property.setComposition(true);
      }

      extractStereotypeAndTags(association, property);

      if (multiplicity != null) {
        property.setMultiplicity(multiplicity);
      }

      org.omg.uml.foundation.core.TaggedValue tag = MdrUtilities.getTag(
          association.getModelElement(), TRANSIENT);
      if (tag != null) {
        property.setTransient(booleanValue(tag.getDataValue().iterator().next()
            .toString()));
      }
      tag = MdrUtilities.getTag(association.getModelElement(), VOLATILE);
      if (tag != null) {
        property.setVolatile(booleanValue(tag.getDataValue().iterator().next()
            .toString()));
      }
    }
  }

  /**
   * Helper method to extract the DataTypes out of the model.
   * 
   * @param source
   *          the MDR source model
   * @param target
   *          the PMMDA target model
   */
  private void extractDatatypes(MdrUmlModel source, MetaModel target) {
    for (MdrUmlDataType mDatatype : source.getDataTypes()) {
      String name = mDatatype.getName();
      if (target.findDatatype(name) == null) {
        MetaDatatype datatype = new MetaDatatype();
        datatype.setName(mDatatype.getName());
        extractStereotypeAndTags(mDatatype, datatype);
        target.addDatatype(datatype);
      }
    }
  }

  /**
   * Helper method to extract the stereotypes out of the model.
   * 
   * @param source
   *          the MDR source model
   * @param target
   *          the pmMDA target model
   */
  private void extractStereotypes(MdrUmlModel source, MetaModel target) {
    for (MdrUmlStereotype mStereotype : source.getStereotypes()) {
      String name = mStereotype.getName();
      if ((name != null) && (name.trim().length() > 0)) {
        Stereotype stereotype = target.findStereotype(name);
        if (stereotype == null) {
          stereotype = new Stereotype();
          stereotype.setName(name);
          target.addStereotype(stereotype);
        }
      }
    }
  }

  private void collectViewProperties(MetaProperty property, MetaClass clazz,
      MdrUmlAttribute attribute) {
    assert (property != null) && (clazz != null) && (attribute != null);
    MdrTaggedValue taggedValue = attribute.getTaggedValue(VIEW_TAG);
    if(taggedValue != null) {
      addCollectedProperty(property, clazz.retrievePackage(), taggedValue.getMdaTaggedValue());
    }
  }
}