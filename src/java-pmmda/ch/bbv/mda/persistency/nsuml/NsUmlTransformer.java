/* copyright 2000-2004 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence);
 * you may not use this file except in compliance with the License.
 * $Id$
 */
package ch.bbv.mda.persistency.nsuml;

import java.io.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.zip.ZipInputStream;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import ru.novosoft.uml.foundation.core.*;
import ru.novosoft.uml.foundation.data_types.MExpression;
import ru.novosoft.uml.foundation.data_types.MMultiplicity;
import ru.novosoft.uml.foundation.data_types.MVisibilityKind;
import ru.novosoft.uml.foundation.extension_mechanisms.MStereotype;
import ru.novosoft.uml.foundation.extension_mechanisms.MTaggedValue;
import ru.novosoft.uml.model_management.MModel;
import ru.novosoft.uml.model_management.MPackage;
import ru.novosoft.uml.xmi.IncompleteXMIException;
import ru.novosoft.uml.xmi.XMIReader;
import ru.novosoft.uml.xmi.XMIWriter;
import ch.bbv.mda.*;

/**
 * The NSUML transformer defines rules how to transform a NS-UML element into
 * the corresponding MDA representation. Special is the mapping of aggregations
 * and compositions to indexed properties to reflect the capabilities of modern
 * object oriented programming languages.
 * @author Marcel Baumann
 * @version $Revision$
 */
public class NsUmlTransformer {
  /**
   * UML modeling tool using NSUML library stores the documentation of an item
   * in a tagged value named documentation. This tag is the standard one defined
   * in the UML 1.4 notation.
   */
  public static final String DOCUMENTATION_TAG = "documentation";

  /**
   * Encoding rule of the XML file containing the XMI data.
   */
  public static final String XML_ENCODING = "UTF-8";

  /**
   * Logger of the class.
   */
  private static Log log = LogFactory.getLog(NsUmlTransformer.class);

  private MetaFactory factory;

  /**
   * Maps between the source objects used as keys and the target objects used as
   * values. Source objects are NSUML entities, target objects are MDA
   * instances.
   */
  private Map elements;

  /**
   * Default constructor of the class.
   */
  public NsUmlTransformer() {
    factory = MetaFactory.getInstance();
    elements = new HashMap();
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
    FileInputStream inputStream = new FileInputStream(file);
    ZipInputStream input = new ZipInputStream(inputStream);
    String name = input.getNextEntry().getName();
    while (!name.endsWith(".xmi")) {
      name = input.getNextEntry().getName();
    }

    InputSource source = new InputSource(new BufferedInputStream(input));
    source.setEncoding(XML_ENCODING);

    XMIReader xmiReader = new XMIReader();
    MModel model = xmiReader.parse(source);
    inputStream.close();
    input.close();
    return model;
  }

  public void writeArgoModel(MModel mModel, File file)
      throws FileNotFoundException, IOException, IncompleteXMIException {
    Writer writer = new BufferedWriter(new OutputStreamWriter(
        new FileOutputStream(file), XML_ENCODING));
    XMIWriter xmiWriter = new XMIWriter(mModel, writer);
    xmiWriter.gen();
    xmiWriter.close();
  }

  public void synchronize(MModel source, MetaModel target) {
    target.setName(source.getName());

    for (Iterator i = new FilterIterator(source.getOwnedElements(),
        MPackage.class); i.hasNext();) {
      MPackage mSubPackage = (MPackage) i.next();
      extractPackage(mSubPackage, target);
    }

    extractRelations(source, target);

    for (Iterator i = new FilterIterator(source.getOwnedElements(),
        MClassifier.class); i.hasNext();) {
      MClassifier mClass = (MClassifier) i.next();
      extractDataType(mClass, target);
    }
    identifySystemPackages(target);
  }

  public void synchronize(MetaPackage metaPackage, MPackage item) {
    synchronize(metaPackage, item);
  }

  public void synchronize(MetaClass metaClass, MClass item) {
    synchronize(metaClass, item);
  }

  public void synchronize(MetaProperty metaProperty, MAttribute item) {
    synchronize(metaProperty, item);
  }

  public void extractStereotypeAndTags(MModelElement source, MetaElement item) {
    MStereotype stereotype = source.getStereotype();
    if (stereotype != null) {
      item.setStereotype(stereotype.getName());
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
      if (DOCUMENTATION_TAG.equals(tagName)) {
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

  protected void extractPackage(MPackage mPackage, MetaModel metaModel) {
    String qualifiedName = getQualifiedName(mPackage);
    MetaPackage metaPackage = metaModel.findPackage(qualifiedName);
    if (metaPackage == null) {
      metaPackage = new MetaPackage();
      metaPackage.setName(qualifiedName);
      factory.initialize(metaPackage);
    }
    extractStereotypeAndTags(mPackage, metaPackage);
    metaModel.addPackage(metaPackage);

    for (Iterator i = new FilterIterator(mPackage.getOwnedElements(),
        MClassifier.class); i.hasNext();) {
      MClassifier mClass = (MClassifier) i.next();
      extractClass(mClass, metaPackage);
    }

    for (Iterator i = new FilterIterator(mPackage.getOwnedElements(),
        MPackage.class); i.hasNext();) {
      MPackage mSubPackage = (MPackage) i.next();
      extractPackage(mSubPackage, metaModel);
    }
  }

  /**
   * Returns the qualified name of the model element. The root context is the
   * model itself and does not belong to the qualified name as defined in the
   * Java language.
   * @param element element, which qualified name should be returned.
   * @return the qualified name of the element if defined otherwise an empty
   *         string.
   * @pre element != null
   */
  protected String getQualifiedName(MModelElement element) {
    assert (element != null);

    MModelElement context = element;
    StringBuffer buffer = new StringBuffer();
    if ((context != null) && (context.getModelElementContainer() != null)) {
      buffer.append(element.getName());
      context = context.getModelElementContainer();
      while ((context != null) && (context.getModelElementContainer() != null)) {
        buffer.insert(0, ".").insert(0, context.getName());
        context = context.getModelElementContainer();
      }
    }
    return buffer.toString();
  }

  protected void extractClass(MClassifier mClass, MetaPackage metaPackage) {
    assert ((mClass != null) && (metaPackage != null));

    String name = mClass.getName();
    if (name == null) {
      if (log.isErrorEnabled()) {
        log.error("Parsed class without name in package "
            + metaPackage.getName());
      }
      return;
    }

    MetaClass metaClass = metaPackage.findClass(name);
    if (metaClass == null) {
      metaClass = new MetaClass();
      metaClass.setName(name);
      factory.initialize(metaClass);
      metaPackage.addClass(metaClass);
    }
    elements.put(mClass, metaClass);
    metaClass.setAbstract(mClass.isAbstract());
    extractStereotypeAndTags(mClass, metaClass);

    if (mClass instanceof MInterface) {
      metaClass.setInterface(true);
    }

    for (Iterator i = new FilterIterator(mClass.getOwnedElements(), MAttribute.class); i
        .hasNext();) {
      extractProperty((MAttribute) i.next(), metaClass);
    }
  }

  protected void extractDataType(MClassifier mClass, MetaModel metaModel) {
    assert ((mClass != null) && (metaModel != null));

    String name = mClass.getName();
    if (name == null) {
      if (log.isErrorEnabled()) {
        log.error("Parsed data type without name in model ");
      }
      return;
    }

    MetaDataType metaDataType = metaModel.findDataType(name);
    if (metaDataType == null) {
      metaDataType = new MetaDataType();
      metaDataType.setName(name);
      metaModel.addDataType(metaDataType);
    }
  }

  protected void extractProperty(MAttribute attribute, MetaClass metaClass) {
    String name = attribute.getName();
    MMultiplicity mMultiplicity = attribute.getMultiplicity();
    MetaProperty property;
    if (mMultiplicity.getUpper() == 1) {
      property = new MetaProperty();
      metaClass.addProperty(property);
    } else {
      MetaIndexedProperty metaIndexedProperty = new MetaIndexedProperty();
      property = metaIndexedProperty;
      metaClass.addIndexedProperty(metaIndexedProperty);
    }
    property.setName(name);
    MExpression initialValue = attribute.getInitialValue();
    if (initialValue != null) {
      property.setInitialValue(initialValue.getBody());
    }
    MClassifier mClassifier = attribute.getType();
    if (mClassifier != null) {
      property.setTypeName(getQualifiedName(attribute.getType()));
    }

    Multiplicity multiplicity = convert(attribute.getMultiplicity());
    if (multiplicity != null) {
      property.setMultiplicity(multiplicity);
    }
    extractStereotypeAndTags(attribute, property);
  }

  protected MTaggedValue getTag(MModelElement source, String tagName) {
    MTaggedValue tag = null;
    for (Iterator i = source.getTaggedValues().iterator(); i.hasNext()
        && (tag != null);) {
      tag = (MTaggedValue) i.next();
      if (tagName.equals(tag.getTag())) {
        break;
      }
      tag = null;
    }
    return tag;
  }

  protected void extractRelations(MModel source, MetaModel target) {
    for (Iterator i = new FilterIterator(source.getOwnedElements(),
        MPackage.class); i.hasNext();) {
      extractRelations(target, (MPackage) i.next());
    }
  }

  /**
   * Extracts all inheritance, implement, composition and aggregation relations
   * defined in the given package and its children. Generalizations are
   * inheritance relations and abstractions are implement relations in the NSUML
   * terminology. The relations are mapped to the model of modern programming
   * languages. Aggregations with a upper cardinality of 1 are mapped to simple
   * properties, all others are mapped to indexed properties.
   * @param target meta model to complete with the defined relations.
   * @param mPackage package, which relations should be extracted.
   * @pre (target != null) && (mPackage != null)
   */
  protected void extractRelations(MetaModel target, MPackage mPackage) {
    assert ((target != null) && (mPackage != null));
    for (Iterator i = new FilterIterator(mPackage.getOwnedElements(),
        MGeneralization.class); i.hasNext();) {
      MGeneralization generalization = (MGeneralization) i.next();
      MetaClass superClass = (MetaClass) elements.get(generalization
          .getParent());
      MetaClass metaClass = (MetaClass) elements.get(generalization.getChild());
      if ((superClass != null) && (metaClass != null)) {
        metaClass.addAncestor(superClass);
      }
    }

    for (Iterator i = new FilterIterator(mPackage.getOwnedElements(),
        MAbstraction.class); i.hasNext();) {
      MAbstraction ma = (MAbstraction) i.next();
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

    Iterator associations = new FilterIterator(mPackage.getOwnedElements(),
        MAssociation.class);
    while (associations.hasNext()) {
      MAssociation association = (MAssociation) associations.next();
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
      property.setTypeName(owned.qualifiedName());
      property.setAggregation(true);
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

    for (Iterator i = new FilterIterator(mPackage.getOwnedElements(),
        MPackage.class); i.hasNext();) {
      extractRelations(target, (MPackage) i.next());
    }
  }

  /**
   * Converts a NSUML multiplicity into a MDA multiplicity.
   * @param multiplicity NSUML multiplicity.
   * @return the equivalent MDA multiplicity of NSUML object.
   */
  protected Multiplicity convert(MMultiplicity multiplicity) {
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
    } else if (multiplicity != null) {
      metaMultiplicity = new Multiplicity(lower, upper);
    }
    return metaMultiplicity;
  }

  protected VisibilityCode convert(MVisibilityKind visibilityKind) {
    VisibilityCode visibility = VisibilityCode
        .getCode(VisibilityCode.PUBLIC_VISIBILITY_CODE);
    if (MVisibilityKind.PUBLIC.equals(visibilityKind)) {
      visibility = VisibilityCode.getCode(VisibilityCode.PUBLIC_VISIBILITY_CODE);
    } else if (MVisibilityKind.PROTECTED.equals(visibilityKind)) {
      visibility = VisibilityCode.getCode(VisibilityCode.PROTECTED_VISIBILITY_CODE);
    } else if (MVisibilityKind.PRIVATE.equals(visibilityKind)) {
      visibility = VisibilityCode.getCode(VisibilityCode.PRIVATE_VISIBILITY_CODE);
    }
    return visibility;
  }

  protected void identifySystemPackages(MetaModel model) {
    for (Iterator i = model.getPackages().iterator(); i.hasNext();) {
      MetaPackage metaPackage = (MetaPackage) i.next();
      if (isSystemPackage(metaPackage)) {
        metaPackage.setSystem(true);

        for (Iterator j = metaPackage.getClasses().iterator(); j.hasNext();) {
          MetaClass clazz = (MetaClass) j.next();
          clazz.setSystem(true);
        }
      }
    }
  }

  protected boolean isSystemPackage(MetaPackage metaPackage) {
    String name = metaPackage.getName();
    return ((name != null) && (name.startsWith("java.")
        || name.startsWith("javax.") || name.equals("java") || name
        .equals("javax")));
  }

  protected boolean booleanValue(String value) {
    return (value != null && value.equalsIgnoreCase("true"));
  }
}