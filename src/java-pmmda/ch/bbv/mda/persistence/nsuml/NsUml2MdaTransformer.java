/* copyright 2000-2006 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence");
 * you may not use this file except in compliance with the License.
 * $Id$
 */
package ch.bbv.mda.persistence.nsuml;

import java.io.*;
import java.util.*;
import java.util.zip.ZipInputStream;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import ru.novosoft.uml.behavior.collaborations.MAssociationEndRole;
import ru.novosoft.uml.foundation.core.*;
import ru.novosoft.uml.foundation.data_types.MAggregationKind;
import ru.novosoft.uml.foundation.data_types.MExpression;
import ru.novosoft.uml.foundation.data_types.MMultiplicity;
import ru.novosoft.uml.foundation.data_types.MVisibilityKind;
import ru.novosoft.uml.foundation.extension_mechanisms.MStereotype;
import ru.novosoft.uml.foundation.extension_mechanisms.MTaggedValue;
import ru.novosoft.uml.model_management.MModel;
import ru.novosoft.uml.model_management.MPackage;
import ru.novosoft.uml.xmi.IncompleteXMIException;
import ru.novosoft.uml.xmi.XMIWriter;
import ch.bbv.mda.*;
import ch.bbv.mda.operators.MdaUtilities;
import ch.bbv.mda.persistence.ExternalTransformer;
import ch.bbv.mda.persistence.FilterIterator;
import ch.bbv.mda.ui.ViewConstants;
import ch.persi.xml.XmlNode;

/**
 * The NSUML transformer defines rules how to transform a NSUML element into the
 * corresponding MDA representation. Special is the mapping of aggregations and
 * compositions to indexed properties to reflect the capabilities of modern
 * object oriented programming languages.
 * @author Marcel Baumann
 * @version $Revision$
 */
public class NsUml2MdaTransformer extends ExternalTransformer implements ViewConstants{
  /**
   * Logger of the class.
   */
  private static Log log = LogFactory.getLog(NsUml2MdaTransformer.class);

  /**
   * Maps between the source objects used as keys and the target objects used as
   * values. Source objects are NSUML entities, target objects are MDA
   * instances.
   */
  private Map<Object, MetaElement> elements;
  
  private class ViewElement {
  	public MetaProperty metaProperty;
  	public TaggedValue taggedValue;
  	public MetaPackage pack;
  }
  /**
   * contains view definitions per property. Each property having a ViewConstants.VIEW_TAG tagged value associated is stored in here.
   */
  private List<ViewElement> collectedViews; 
  
  /**
   * Default constructor of the class.
   */
  public NsUml2MdaTransformer() {
    elements = new HashMap<Object, MetaElement>();
  }

  /**
   * Loads the provided file, opens the file containing the XMI model and
   * creates a NSUML model.
   * @param file file containing the NSUML model.
   * @return the NSUML model defined in the file.
   * @throws FileNotFoundException the given file could not be found.
   * @throws IOException an error occured when reading data.
   * @throws ParserConfigurationException the SAX parser could not be created.
   * @throws SAXException a parsing error occured when reading the XMI file.
   */
  public MModel loadArgoModel(File file) throws FileNotFoundException,
      IOException, ParserConfigurationException, SAXException {
    InputSource source = null;
    ZipInputStream input = null;
    FileInputStream inputStream = new FileInputStream(file);
    if (!file.getName().endsWith(".xmi")) {
      input = new ZipInputStream(inputStream);
      String name = input.getNextEntry().getName();
      while (!name.endsWith(".xmi")) {
        name = input.getNextEntry().getName();
      }
      source = new InputSource(new BufferedInputStream(input));
    } else {
      source = new InputSource(new FileInputStream(file));
    }
    source.setEncoding(XML_ENCODING);

    NsumlXmiReader xmiReader = new NsumlXmiReader(file);
    MModel model = xmiReader.parse(source);
    inputStream.close();
    if (input != null) {
      input.close();
    }
    return model;
  }

  /**
   * Writes the NSUML model into the file.
   * @param mModel NSUML model to write.
   * @param file file in which the model should be written.
   * @throws FileNotFoundException if the file could not be found.
   * @throws IOException if an error occured during the write operation.
   * @throws IncompleteXMIException if the XMI model has an error.
   */
  public void writeArgoModel(MModel mModel, File file)
      throws FileNotFoundException, IOException, IncompleteXMIException {
    Writer writer = new BufferedWriter(new OutputStreamWriter(
        new FileOutputStream(file), XML_ENCODING));
    XMIWriter xmiWriter = new XMIWriter(mModel, writer);
    xmiWriter.gen();
    xmiWriter.close();
  }

  /**
   * Synchronize the MDA model with the NSUML model.
   * @param source NSUML model used as source of the synchronization.
   * @param target MDA model to synchronize.
   */
  public void synchronize(MModel source, MetaModel target) {
    target.setName(source.getName());
    extractDatatypes(source, target);
    extractStereotypes(source, target);
    for (Iterator<MPackage> i = new FilterIterator<MPackage>(source
        .getOwnedElements(), MPackage.class); i.hasNext();) {
      MPackage mSubPackage = i.next();
      extractPackage(mSubPackage, target);
    }
    for (Iterator<MClass> i = new FilterIterator<MClass>(source
        .getOwnedElements(), MClass.class); i.hasNext();) {
      MClassifier mClass = i.next();
      extractClass(mClass, target);
    }
    for (Iterator<MInterface> i = new FilterIterator<MInterface>(source
        .getOwnedElements(), MInterface.class); i.hasNext();) {
      MClassifier mClass = i.next();
      extractClass(mClass, target);
    }
    extractRelations(source, target);
    postProcessModel(target);
    extractViews(target);
  }


/**
   * Extracts the information from the NSUML package and overwrites the ones of
   * the MDA package defined in the given context.
   * @param source NSUML package used as source of the synchronization.
   * @param context MDA context containing the package to synchronize.
   */
  protected void extractPackage(MPackage source, MetaModel context) {
    String qualifiedName = NsUmlUtilities.getQualifiedName(source);
    MetaPackage metaPackage = context.findPackage(qualifiedName);
    if (metaPackage == null) {
      metaPackage = new MetaPackage();
      metaPackage.setName(qualifiedName);
      context.addPackage(metaPackage);
    }
    extractStereotypeAndTags(source, metaPackage);
    if ( collectedViews == null){
    	collectedViews = new ArrayList<ViewElement>();
    }
    for (Iterator<MClassifier> i = new FilterIterator<MClassifier>(source
        .getOwnedElements(), MClassifier.class); i.hasNext();) {
      MClassifier mClass = i.next();
      extractClass(mClass, metaPackage);
    }

    for (Iterator<MPackage> i = new FilterIterator<MPackage>(source
        .getOwnedElements(), MPackage.class); i.hasNext();) {
      MPackage mSubPackage = i.next();
      extractPackage(mSubPackage, context);
    }
  }
  
  private void collectViews(MetaProperty property, MetaClass clazz, MAttribute attr){
	  assert (property != null) && (clazz != null) && (attr != null);
	  for (Iterator i = attr.getTaggedValues().iterator(); i.hasNext();) {
	      MTaggedValue tag = (MTaggedValue) i.next();
	      if (tag.getTag().equals(VIEW_TAG)){
	      	ViewElement viewElement = new ViewElement();
	      	viewElement.metaProperty = property;
	      	viewElement.pack = clazz.retrievePackage();
	      	viewElement.taggedValue = new TaggedValue(tag.getTag(), tag.getValue());
	    	  collectedViews.add(viewElement);
	      }
	  }
  }
  
  protected void extractViews(MetaModel metaModel){
	  if (collectedViews == null){
		  return;
	  }
	  
	  for (ViewElement v : collectedViews) {
  	  XmlNode xml = new XmlNode();
  	  xml.parse(v.taggedValue.getValue());
  	  
  	  for(Iterator<XmlNode> iter = xml.getAll(VIEW_NODE); iter.hasNext();){
    	  MetaView metaView = new MetaView();
  		  XmlNode view = iter.next();
  		  String name = view.getAttribute(NAME);
  		  metaView.setName(name.substring(name.lastIndexOf(".")+1));
  		  metaView.setDescription(view.getAttribute(DESCRIPTION));
  		  metaView.setVisibility(VisibilityCode.getCode(Integer.parseInt(view.getAttribute(VISIBILITY))));
   		  MetaView.FieldDefinition fieldDefinition = new MetaView.FieldDefinition(v.metaProperty);
  		  metaView.appendField(fieldDefinition);
  		  int idx = name.lastIndexOf(".");
  		  MetaPackage metaPackage = null;
  		  if(idx >=0) {
    		  metaPackage = metaModel.findPackage(name.substring(0, idx));
  		  }
  		  if(metaPackage == null) {
  		  	metaPackage = metaModel;
  		  }
  		  if(!metaPackage.getViews().contains(metaView)) {
  		  	metaPackage.addView(metaView);
  		  }
  	  }
	  }
  }

  
  /**
   * Extracts the information from the NSUML class and overwrites the ones of
   * the MDA class defined in the given context.
   * @param source NSUML class used as source of the synchronization.
   * @param context MDA context containing the class to synchronize.
   */
  protected void extractClass(MClassifier source, MetaPackage context) {
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
      context.addClass(metaClass);
    }
    elements.put(source, metaClass);
    metaClass.setAbstract(source.isAbstract());
    extractStereotypeAndTags(source, metaClass);
    if (source instanceof MInterface) {
      metaClass.setInterface(true);
    }

    for (Iterator<MAttribute> i = new FilterIterator<MAttribute>(source
        .getFeatures(), MAttribute.class); i.hasNext();) {
    	MAttribute attr = i.next();
      extractProperty(attr, metaClass);
    }
    for (Iterator<MAttribute> i = new FilterIterator<MAttribute>(source
        .getOwnedElements(), MAttribute.class); i.hasNext();) {
    	MAttribute attr = i.next();
      extractProperty(attr, metaClass);
    }

    for (Iterator<MClassifier> i = new FilterIterator<MClassifier>(source
        .getOwnedElements(), MClassifier.class); i.hasNext();) {
      MClassifier mNestedClass = i.next();
      extractNestedClass(mNestedClass, metaClass);
    }
  }

  /**
   * Extracts the information from the NSUML class and overwrites the ones of
   * the MDA nested classes defined in the given context.
   * @param source NSUML class used as source of the synchronization.
   * @param context MDA context containing the class to synchronize.
   */
  protected void extractNestedClass(MClassifier source, MetaClass context) {
    assert (source != null) && (context != null);

    String name = source.getName();
    if (name == null) {
      if (log.isErrorEnabled()) {
        log.error("Parsed nested class without name in class "
            + context.getName());
      }
      return;
    }

    MetaClass metaNestedClass = context.getModel().findClass(name);
    if (metaNestedClass == null) {
      metaNestedClass = new MetaClass();
      metaNestedClass.setName(name);
      context.addNestedClass(metaNestedClass);
    }
    elements.put(source, metaNestedClass);
    metaNestedClass.setAbstract(source.isAbstract());
    extractStereotypeAndTags(source, metaNestedClass);
    if (source instanceof MInterface) {
      metaNestedClass.setInterface(true);
    }
    for (Iterator<MAttribute> i = new FilterIterator<MAttribute>(source
        .getFeatures(), MAttribute.class); i.hasNext();) {
      extractProperty(i.next(), metaNestedClass);
    }
    for (Iterator<MClassifier> i = new FilterIterator<MClassifier>(source
        .getOwnedElements(), MClassifier.class); i.hasNext();) {
      MClassifier mNestedNestedClass = i.next();
      extractNestedClass(mNestedNestedClass, metaNestedClass);
    }
  }

  /**
   * Extracts the property from the attribute and synchronizes the target.
   * @param attribute attribute containing the data.
   * @param context context in which the property will be updated.
   */
  private void extractProperty(MAttribute attribute, MetaClass context) {
    String name = attribute.getName();
    MMultiplicity mMultiplicity = attribute.getMultiplicity();
    MetaProperty property;
    if (mMultiplicity.getUpper() == 1) {
      property = new MetaProperty();
      context.addProperty(property);
    } else {
      MetaIndexedProperty metaIndexedProperty = new MetaIndexedProperty();
      property = metaIndexedProperty;
      context.addIndexedProperty(metaIndexedProperty);
    }
    property.setName(name);
    MExpression initialValue = attribute.getInitialValue();
    if (initialValue != null) {
      property.setInitialValue(initialValue.getBody());
    }
    MClassifier mClassifier = attribute.getType();
    if (mClassifier != null) {
      property
          .setTypeName(NsUmlUtilities.getQualifiedName(attribute.getType()));
    } else {
      log.info("Attribute '" + attribute.getName() + "' of class '"
          + context.getName()
          + "' has no type associated (set to datatype integer)");
      property.setTypeName("int");
    }

    Multiplicity multiplicity = convert(attribute.getMultiplicity());
    if (multiplicity != null) {
      property.setMultiplicity(multiplicity);
    }
    extractStereotypeAndTags(attribute, property);
    collectViews(property, context, attribute);
  }

  /**
   * Extracts tags and stereotypes from the NSUML element and add them the meta
   * element. The visibility of the item is also set.
   * @param source NSUML element to parse
   * @param item meta item to synchronize
   */
  private void extractStereotypeAndTags(MModelElement source, MetaElement item) {
    MStereotype mStereotype = source.getStereotype();
    if (mStereotype != null) {
      String name = mStereotype.getName();
      MdaUtilities.addStereotype(item, name);
    }
    item.setVisibility(convert(source.getVisibility()));
    for (Iterator i = source.getTaggedValues().iterator(); i.hasNext();) {
      MTaggedValue tag = (MTaggedValue) i.next();
      String tagName = tag.getTag();
      if (item instanceof MetaProperty) {
        if ("transient".equals(tagName)) {
          ((MetaProperty) item).setTransient(booleanValue(tag.getValue()));
        } else if ("volatile".equals(tagName)) {
          ((MetaProperty) item).setVolatile(booleanValue(tag.getValue()));
        }
      }
      if (UmlConstants.DOCUMENTATION_TAG.equals(tagName)) {
        item.setDescription(tag.getValue());
      } else {
        TaggedValue taggedValue = item.getTaggedValue(tagName);
        if (taggedValue == null) {
          taggedValue = new TaggedValue(tag.getTag(), tag.getValue());
          item.addTaggedValue(taggedValue);
        } else {
          taggedValue.setValue(tag.getValue());
        }
      }
    }
  }

  private MTaggedValue getTag(MModelElement source, String tagName) {
    assert ((source != null) && (tagName != null));
    MTaggedValue tag = null;
    for (Iterator i = source.getTaggedValues().iterator(); i.hasNext();) {
      tag = (MTaggedValue) i.next();
      if (tagName.equals(tag.getTag())) {
        break;
      }
      tag = null;
    }
    return tag;
  }

  /**
   * Extracts all inheritance, implement, composition and aggregation relations
   * defined in the given package and its children. Generalizations are
   * inheritance relations and abstractions are implement relations in the NSUML
   * terminology. The relations are mapped to the model of modern programming
   * languages. Aggregations with a upper cardinality of 1 are mapped to simple
   * properties, all others are mapped to indexed properties.
   * @param mPackage package, which relations should be extracted.
   * @param target meta model to complete with the defined relations.
   * @pre (target != null) && (mPackage != null)
   */
  private void extractRelations(MPackage mPackage, MetaModel target) {
    assert (target != null) && (mPackage != null);
    for (Iterator<MGeneralization> i = new FilterIterator<MGeneralization>(
        mPackage.getOwnedElements(), MGeneralization.class); i.hasNext();) {
      MGeneralization generalization = i.next();
      MetaClass superClass = (MetaClass) elements.get(generalization
          .getParent());
      MetaClass metaClass = (MetaClass) elements.get(generalization.getChild());
      if ((superClass != null) && (metaClass != null)) {
        metaClass.addAncestor(superClass);
      }
    }

    for (Iterator<MAbstraction> i = new FilterIterator<MAbstraction>(mPackage
        .getOwnedElements(), MAbstraction.class); i.hasNext();) {
      MAbstraction ma = i.next();
      if ((ma.getSuppliers().size() == 0) || (ma.getClients().size() == 0)) {
        if (log.isErrorEnabled()) {
          log.error("Association (MAbstraction) without end " + ma.getName()
              + " in package " + mPackage.getName());
        }
        continue;
      }
      MModelElement supplier = (MModelElement) ma.getSuppliers().iterator()
          .next();
      MModelElement client = (MModelElement) ma.getClients().iterator().next();
      MetaClass metaClass = (MetaClass) elements.get(client);
      MetaClass superClass = (MetaClass) elements.get(supplier);
      if ((superClass != null) && (metaClass != null)) {
        metaClass.addAncestor(superClass);
      }
    }

    Iterator<MAssociation> associations = new FilterIterator<MAssociation>(
        mPackage.getOwnedElements(), MAssociation.class);
    while (associations.hasNext()) {
      MAssociation association = associations.next();
      MAssociationEnd e1 = association.getConnection(0);
      MAssociationEnd e2 = association.getConnection(1);
      if ((e1 == null) || (e2 == null)) {
        if (log.isErrorEnabled()) {
          log.error("Association (MAssociation) without end(s) "
              + association.getName() + " in package " + mPackage.getName());
        }
        continue;
      }
      MetaClass owner = (MetaClass) elements.get(e1.getType());
      MetaClass owned = (MetaClass) elements.get(e2.getType());
      String name = e2.getName();
      if ((name == null) || (name.length() == 0)) {
        name = association.getName();
      }
      if (owner == null) {
        if (log.isErrorEnabled()) {
          log.error("No owner for association " + association.getName()
              + " in package " + mPackage.getName());
        }
        continue;
      }

      Multiplicity multiplicity = convert(e2.getMultiplicity());
      MetaProperty property = null;
      if (((multiplicity != null) && (multiplicity.getUpperBound() == Multiplicity.N_MULTIPLICITY))
          || (multiplicity == null)) {
        property = new MetaIndexedProperty();
        owner.addIndexedProperty((MetaIndexedProperty) property);
      } else {
        property = new MetaProperty();
        owner.addProperty(property);
      }
      property.setName(name);
      if (owned != null) {
        property.setTypeName(owned.getQualifiedName());
      }
      property.setIsRelation(true);
      if (e1.getAggregation() == MAggregationKind.AGGREGATE) {
        property.setAggregation(true);
      }
      if (e1.getAggregation() == MAggregationKind.COMPOSITE) {
        property.setComposition(true);
      }
      property.setIsNavigableToContext(e1.isNavigable());
      property.setIsNavigableToClassifier(e2.isNavigable());
      extractStereotypeAndTags(association, property);
      if (multiplicity != null) {
        property.setMultiplicity(multiplicity);
      }

      MTaggedValue tag = getTag(association, "transient");
      if (tag != null) {
        property.setTransient(booleanValue(tag.getValue()));
      }
      tag = getTag(association, "volatile");
      if (tag != null) {
        property.setVolatile(booleanValue(tag.getValue()));
      }
    }

    for (Iterator<MPackage> i = new FilterIterator<MPackage>(mPackage
        .getOwnedElements(), MPackage.class); i.hasNext();) {
      extractRelations(i.next(), target);
    }
  }

  /**
   * Helper method to extract the datatypes out of the model.
   * @param source the NSUML source model
   * @param target the PMMDA target model
   */
  private void extractDatatypes(MModel source, MetaModel target) {
    for (Iterator<MDataType> i = new FilterIterator<MDataType>(source
        .getOwnedElements(), MDataType.class); i.hasNext();) {
      MDataType mDatatype = i.next();
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
   * @param source the MDR source model
   * @param target the PMMDA target model
   */
  private void extractStereotypes(MModel source, MetaModel target) {
    for (Iterator<MStereotype> i = new FilterIterator<MStereotype>(source
        .getOwnedElements(), MStereotype.class); i.hasNext();) {
      MStereotype mStereotype = i.next();
      String name = mStereotype.getName();
      Stereotype stereotype = target.findStereotype(name);
      if (stereotype == null) {
        stereotype = new Stereotype(name);
        target.addStereotype(stereotype);
        extractStereotypeAndTags(mStereotype, stereotype);
      }
    }
  }

  /**
   * Converts a NSUML multiplicity into a MDA multiplicity.
   * @param multiplicity NSUML multiplicity
   * @return the equivalent MDA multiplicity of NSUML object
   * @pre multiplicity != null
   */
  private Multiplicity convert(MMultiplicity multiplicity) {
    assert (multiplicity != null);
    Multiplicity metaMultiplicity = null;
    int lower = multiplicity.getLower();
    int upper = multiplicity.getUpper();
    if ((multiplicity == MMultiplicity.M0_1) || ((lower == 0) && (upper == 1))) {
      metaMultiplicity = Multiplicity.MULTIPLICITY_0_1;
    } else if ((multiplicity == MMultiplicity.M0_N)
        || ((lower == 0) && (upper == Multiplicity.N_MULTIPLICITY))) {
      metaMultiplicity = Multiplicity.MULTIPLICITY_0_N;
    } else if ((multiplicity == MMultiplicity.M1_1)
        || ((lower == 1) && (upper == 1))) {
      metaMultiplicity = Multiplicity.MULTIPLICITY_1_1;
    } else if ((multiplicity == MMultiplicity.M1_N)
        || ((lower == 1) && (upper == Multiplicity.N_MULTIPLICITY))) {
      metaMultiplicity = Multiplicity.MULTIPLICITY_1_N;
    } else {
      metaMultiplicity = new Multiplicity(lower, upper);
    }
    return metaMultiplicity;
  }

  /**
   * Converts a NSUML visibility into a MDA visibility. If visibility kind is
   * null public visibility is returned.
   * @param visibilityKind NSUML visibility.
   * @return the equivalent MDA visibility of NSUML object.
   */
  private VisibilityCode convert(MVisibilityKind visibilityKind) {
    VisibilityCode visibility = VisibilityCode
        .getCode(VisibilityCode.PUBLIC_ID);
    if (MVisibilityKind.PUBLIC.equals(visibilityKind)) {
      visibility = VisibilityCode.getCode(VisibilityCode.PUBLIC_ID);
    } else if (MVisibilityKind.PROTECTED.equals(visibilityKind)) {
      visibility = VisibilityCode.getCode(VisibilityCode.PROTECTED_ID);
    } else if (MVisibilityKind.PRIVATE.equals(visibilityKind)) {
      visibility = VisibilityCode.getCode(VisibilityCode.PRIVATE_ID);
    }
    return visibility;
  }
}