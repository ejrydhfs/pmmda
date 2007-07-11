/* copyright 2000-2006 Marcel Baumann 
 * Licensed under the Apache License, Version 2.0 (the "Licence"); 
 * you may not use this file except in compliance with the License. 
 * $Id$ 
 */
package ch.bbv.mda.persistence.mdr;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Modifier;
import java.util.List;

import javax.jmi.model.ModelPackage;
import javax.jmi.model.MofPackage;
import javax.jmi.xmi.MalformedXMIException;
import javax.jmi.xmi.XmiReader;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.netbeans.api.mdr.CreationFailedException;
import org.netbeans.api.mdr.MDRManager;
import org.netbeans.api.mdr.MDRepository;
import org.netbeans.api.xmi.XMIReaderFactory;
import org.netbeans.api.xmi.XMIWriterFactory;
import org.netbeans.lib.jmi.xmi.ReaderFactory;
import org.netbeans.lib.jmi.xmi.WriterFactory;
import org.omg.uml.foundation.core.Attribute;
import org.omg.uml.foundation.core.Classifier;
import org.omg.uml.foundation.core.DataType;
import org.omg.uml.foundation.core.Generalization;
import org.omg.uml.foundation.core.Interface;
import org.omg.uml.foundation.core.Operation;
import org.omg.uml.foundation.core.Parameter;
import org.omg.uml.foundation.core.Stereotype;
import org.omg.uml.foundation.core.TagDefinition;
import org.omg.uml.foundation.core.TaggedValue;
import org.omg.uml.foundation.core.UmlClass;
import org.omg.uml.foundation.datatypes.ParameterDirectionKindEnum;
import org.omg.uml.foundation.datatypes.VisibilityKind;
import org.omg.uml.foundation.datatypes.VisibilityKindEnum;
import org.omg.uml.modelmanagement.UmlPackage;

import ch.bbv.mda.persistence.PersistenceException;

/**
 * Reads and writes a UML 1.4 model in XMI compliant format as supported in the
 * Netbeans MDR. The in-memory model of MDR is used.
 * @author Marcel Baumann
 * @version $Revision$
 */
public class MdrLoader {
  /**
   * Logger for all instances of the class.
   */
  private static Log log = LogFactory.getLog(MdrLoader.class);

  /**
   * name of a MOF extent that will serve as a target extent for the UML2MOF
   * transformation.
   */
  private static final String MOF_INSTANCE = "MOFInstance";

  /**
   * name of a UML extent (instance of UML metamodel) that the UML models will
   * be loaded into.
   */
  private static final String UML_INSTANCE = "UMLInstance";

  /**
   * name of a MOF extent that will contain definition of UML metamodel.
   */
  private static final String UML_MM = "UML";

  private MDRepository repository;

  private ModelPackage mof;

  private org.omg.uml.UmlPackage packages;

  static {
    System.setProperty("org.netbeans.mdr.storagemodel.StorageFactoryClassName",
        "org.netbeans.mdr.persistence.memoryimpl.StorageFactoryImpl");
  }

  /**
   * Initializes the repository.
   * @return The UmlPackage extent.
   * @throws PersistenceException When an error during repository initialization
   *         occurs.
   */
  public org.omg.uml.UmlPackage init() throws PersistenceException {
    assert (packages == null || repository == null);
    // TODO: Ueberarbeiten. Funktioniert so. ist aber haesslich!
    try {

      MDRManager manager = MDRManager.getDefault();
      if (manager == null) {
        log.error("Could not retrieve the MDR manager");
      }

      repository = manager.getDefaultRepository();

      mof = (ModelPackage) repository.getExtent(MOF_INSTANCE);
      if (mof != null) {
        mof.refDelete();
        mof = null;
      }

      if (mof == null) {
        mof = (ModelPackage) repository.createExtent(MOF_INSTANCE);
      }

      packages = (org.omg.uml.UmlPackage) repository.getExtent(UML_INSTANCE);
      if (packages != null) {
        packages.refDelete();
        packages = null;
      }

      if (packages == null) {
        packages = (org.omg.uml.UmlPackage) repository.createExtent(
            UML_INSTANCE, getUmlPackage());
      }

    } catch (CreationFailedException e) {
      throw new PersistenceException(
          "The creation of the UmlPackage extent failed", e);
    } catch (Exception e) {
      throw new PersistenceException("An unexpected error occured", e);
    }
    return packages;
  }

  /**
   * Shuts the repository down.
   */
  public void dispose() {
    packages = null;
    if (repository != null) {
      repository.shutdown();
      repository = null;
    }
  }

  /**
   * Reads an XMI file into the Uml package extent.
   * @param file The file to read.
   * @return The UML package extent.
   * @throws PersistenceException When a problem during read occurs or the
   *         repository has not been initialized.
   */
  public org.omg.uml.UmlPackage read(InputStream file)
      throws PersistenceException {
    assert (packages != null) || (repository != null);
    try {
      final XMIReaderFactory factory = new ReaderFactory();

      factory.createXMIReader().read(file, "asdf", packages);
    } catch (IOException e) {
      throw new PersistenceException("The specified file could not be read", e);
    } catch (MalformedXMIException e) {
      throw new PersistenceException("The XMI file structure is malformed", e);
    }
    return packages;
  }

  
  /**
   * Writes the repository into a XMI file.
   * @param file The file to write the repository into.
   * @throws PersistenceException When either the repository has not been
   *         initialized or an error during file write occurs.
   */
  public void write(File file) throws PersistenceException {
    assert (packages != null || repository != null);
    FileOutputStream out = null;
    try {
      out = new FileOutputStream(file);
      final XMIWriterFactory factory = new WriterFactory();
      factory.createXMIWriter().write(out, packages, null);
    } catch (IOException e) {
      throw new PersistenceException("An error during writing occured", e);
    } finally {
      if (out != null) {
        try {
          out.close();
        } catch (IOException ex) {
          // Do nothing
        }
      }
    }
  }

  /**
   * Creates a UmlPackage.
   * @param pkgName The package name.
   * @return The UmlPackage instance.
   */
  public UmlPackage createUmlPackage(String pkgName) {
    assert (packages != null || repository != null);
    // packages are always VK_PUBLIC

    return packages.getModelManagement().getUmlPackage().createUmlPackage(
        pkgName, VisibilityKindEnum.VK_PUBLIC, false, false, false, false);

  }

  /**
   * Creates a UmlClass.
   * @param clzName The class name.
   * @param modifier The visibility of the class.
   * @return The UmlClass instance.
   */
  public UmlClass createUmlClass(String clzName, int modifier) {
    return createUmlClass(clzName, modifier, false);
  }

  /**
   * Creates a UmlClass.
   * @param clzName The class name.
   * @param modifier The visibility of the class.
   * @param abstr True when the class is abstract, false otherwise.
   * @return The UmlClass instance.
   */
  public UmlClass createUmlClass(String clzName, int modifier, boolean abstr) {
    assert (packages != null || repository != null);
    return packages.getCore().getUmlClass().createUmlClass(clzName,
        mapModifier(modifier), false, false, false, abstr, false);
  }

  /**
   * Creates an Interface.
   * @param itfName The interface name.
   * @param modifier The visibility of the interface.
   * @return The Interface instance.
   */
  public Interface createUmlInterface(String itfName, int modifier) {
    assert (packages != null || repository != null);
    return packages.getCore().getInterface().createInterface(itfName,
        mapModifier(modifier), false, false, false, false);
  }

  /**
   * Creates a UmlDataType.
   * @param typeName The datatype name.
   * @return The UmlClass instance.
   */
  public DataType createUmlDataType(String typeName) {
    assert (packages != null || repository != null);
    return packages.getCore().getDataType().createDataType(typeName,
        VisibilityKindEnum.VK_PUBLIC, false, false, false, false);
  }

  /**
   * Creates an Operation and associates it with a UmlClass.
   * @param opName The operation name.
   * @param modifier The visibility of the operation.
   * @param umlClz The UmlClass instance.
   * @return The Operation instance.
   */
  public Operation createUmlOperation(String opName, int modifier,
      Classifier umlClz) {
    assert (packages != null || repository != null);
    final Operation op = packages.getModelManagement().getCore().getOperation()
        .createOperation(opName, mapModifier(modifier), false, null, false,
            null, false, false, false, null);
    op.setOwner(umlClz);
    return op;
  }

  /**
   * Creates a return type Parameter and associates it with an Operation.
   * @param type The operation return type.
   * @param op The Operation instance.
   * @return The Parameter instance.
   * @throws PersistenceException when the repository has not been initialized.
   */
  public Parameter createUmlReturnType(Classifier type, Operation op)
      throws PersistenceException {
    assert (packages != null || repository != null);
    for(Object parameterObj:op.getParameter()){
      Parameter param = (Parameter)parameterObj;
      if (param.getKind() != null
          && param.getKind().equals(ParameterDirectionKindEnum.PDK_RETURN)) {
        throw new PersistenceException("Operation '" + op.getName()
            + "' already has a return type!", null);
      }
    }
    final Parameter parameter = createUmlParameter(null, type, op);
    parameter.setKind(ParameterDirectionKindEnum.PDK_RETURN);
    return parameter;
  }

  /**
   * Creates a Parameter and associates it with an Operation.
   * @param paraName The parameter name.
   * @param type The operation return type.
   * @param op The Operation instance.
   * @return The Parameter instance.
   */
  @SuppressWarnings("unchecked")
  public Parameter createUmlParameter(String paraName, Classifier type,
      Operation op) {
    assert (packages != null || repository != null);
    final List<Parameter> parameters = op.getParameter();
    final Parameter parameter = packages.getModelManagement().getCore()
        .getParameter().createParameter(paraName, VisibilityKindEnum.VK_PUBLIC,
            false, null, null);
    parameters.add(parameter);
    parameter.setType(type);
    return parameter;
  }

  /**
   * Creates an Attribute and associates it with a UmlClass.
   * @param attrName The attribute name.
   * @param modifier The visibility of the attribute.
   * @param type The attribute type.
   * @param umlClz The UmlClass instance.
   * @return The Attribute instance.
   */
  public Attribute createUmlAttribute(String attrName, int modifier,
      Classifier type, Classifier umlClz) {
    assert (packages != null || repository != null);
    final Attribute attr = packages.getModelManagement().getCore()
        .getAttribute().createAttribute(attrName, mapModifier(modifier), false,
            null, null, null, null, null, null);
    attr.setType(type);
    attr.setOwner(umlClz);
    return attr;
  }

  /**
   * Creates a Generalization between the class and its superclass.
   * @param clz The class.
   * @param superClz The super class.
   * @return The Generalization instance.
   */
  public Generalization createUmlGeneralization(Classifier clz,
      Classifier superClz) {
    assert (packages != null || repository != null);
    final Generalization gen = packages.getCore().getGeneralization()
        .createGeneralization("extends", VisibilityKindEnum.VK_PUBLIC, false,
            null /* discriminator */);
    gen.setChild(clz);
    gen.setParent(superClz);
    return gen;
  }

  /**
   * Creates a realization between the class and an interface.
   * @param clz The class.
   * @param itf The interface.
   * @return The Realization instance.
   */
  public Generalization createUmlrealization(Classifier clz, Classifier itf) {
    assert (packages != null || repository != null);
    final Generalization gen = packages.getCore().getGeneralization()
        .createGeneralization("implements", VisibilityKindEnum.VK_PUBLIC,
            false, null /* discriminator */);
    gen.setChild(clz);
    gen.setParent(itf);
    return gen;
  }

  /**
   * Creates a tagged Value
   * @param element The MdrUmlElement
   * @param name The name of the TaggedValue
   * @return The TaggedValue instance.
   */
  public TaggedValue createTaggedValue(MdrUmlElement element, String name) {
    assert (name != null || element != null);
    final TaggedValue taggedValue = packages.getCore().getTaggedValue()
        .createTaggedValue(name, VisibilityKindEnum.VK_PUBLIC, false, null);
    TagDefinition tagDefinition = packages.getCore().getTagDefinition()
        .createTagDefinition();
    tagDefinition.setName(name);
    taggedValue.setType(tagDefinition);
    return taggedValue;
  }

  /**
   * Creates a Stereotype
   * @param name The name of the Stereotype
   * @return The Stereotype instance.
   */
  public Stereotype createStereotype(String name) {
    assert (name != null);
    final Stereotype stereotype = packages.getCore().getStereotype()
        .createStereotype();
    stereotype.setName(name);

    return stereotype;
  }

  /**
   * Creates a Data Type
   * @param name The name of the datatype
   * @return The Datatype instance.
   */
  public MdrUmlDataType createDataType(String name) {
    final DataType dataType = packages.getCore().getDataType().createDataType();
    dataType.setName(name);
    MdrUmlDataType mdrDataType = new MdrUmlDataType(dataType);
    return mdrDataType;
  }

  /**
   * @param modifier
   * @return
   */
  private VisibilityKind mapModifier(int modifier) {
    VisibilityKind vis = null;

    if (modifier == 0) {
      vis = VisibilityKindEnum.VK_PACKAGE;
    } else if (Modifier.isPrivate(modifier)) {
      vis = VisibilityKindEnum.VK_PRIVATE;
    } else if (Modifier.isProtected(modifier)) {
      vis = VisibilityKindEnum.VK_PROTECTED;
    } else if (Modifier.isPublic(modifier)) {
      vis = VisibilityKindEnum.VK_PUBLIC;
    } else {
      log.warn("Unknown modifier: " + Modifier.toString(modifier));
    }
    return vis;
  }

  /**
   * Finds a "UML" package as the tompost package of UML metamodel. This is the
   * package that needs to be instantiated in order to create an UML extent.
   * @return the UML MOF top most package.
   * @throws Exception if the JMI MDR interface encounters a problem.
   */
  private MofPackage getUmlPackage() throws Exception {
    ModelPackage umlMM = (ModelPackage) repository.getExtent(UML_MM);
    if (umlMM == null) {
      umlMM = (ModelPackage) repository.createExtent(UML_MM);
    }
    MofPackage result = getUmlPackage(umlMM);
    if (result == null) {
      XMIReaderFactory readFact = new ReaderFactory();
      XmiReader reader = readFact.createXMIReader();
      reader.read(org.omg.uml.UmlPackage.class.getResource(
          "resources/01-02-15_Diff.xml").toString(), umlMM);
      result = getUmlPackage(umlMM);
    }
    return result;
  }

  /**
   * Finds the top most "UML" package in a given extend.
   * @param extent MOF extent that should be searched for "UML" package.
   * @return the requested package if found otherwise null.
   * @pre extent != null
   */
  private MofPackage getUmlPackage(ModelPackage extent) {
    for (Object mofPkg:extent.getMofPackage().refAllOfClass()){
      MofPackage pkg = (MofPackage) mofPkg;
      if (pkg.getContainer() == null && "UML".equals(pkg.getName())) {
        return pkg;
      }
    }
    return null;
  }

}
