/* copyright 2000-2004 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence);
 * you may not use this file except in compliance with the License.
 * $Id$
 */
package ch.bbv.mda.generators;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.MessageFormat;
import java.util.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;

import ch.bbv.application.VersionId;
import ch.bbv.mda.*;

/**
 * The data object handler is responsible for the generation of all data object
 * types and the associated helper classes. The code generator is able to
 * distinguish between simple properties, indexed properties and reference
 * codes. The generated code for each type of fields can be modified in the
 * velocity script without any source code changes. The following constructs are
 * supported.
 * <ul>
 * <li>The data object with its constructors, getters and setters for all
 * properties is generated. The code is well indented and fully documented
 * following the JavaDoc conventions. Each data object has a default constructor
 * and an optional specialized one. The getter and setter naming conventions are
 * defined in the package documentation of MDA models -.</li>
 * <li>A property is identified as a reference code if it implements the
 * reference code or hierarchical reference code interface.</li>
 * <li>A lightweight representation of each data object having the lightweight
 * flag set is generated. Lighweight objects are used for navigation and display
 * purposes when it would be too expensive to send the complete graph of data
 * object instances. Typical usage is displaying a navigation tree in the user
 * interface. The getter and setter naming conventions are the same as the ones
 * of the data objects.</li>
 * <li>A reference service is generated for all the reference code defined in
 * one package. Convenience methods to access each type of code are provided.
 * Codes marked as preloaded are cached in the service. They are loaded at
 * startup time and are always locally available. The framework assumes that
 * preloaded codes are read-only. No restrictions are set on other kinds of
 * codes. If no reference code exists in the package no service will be
 * generated.</li>
 * <li>Helper classes are generated to provide a complete implementation of the
 * visitor pattern on all data object classes defined in the package. The
 * generated visitor can visit data objects defined in other packages but do not
 * generate specialized methods for their descendents.</li>
 * </ul>
 * @author Marcel Baumann
 * @version $Revision$
 */
public class BogCartridge extends CartridgeImpl implements Cartridge {
  /**
   * Name of the cartridge as identified in the factory.
   */
  public static final String NAME = "dog";

  /**
   * Stereotype name for the data object stereotype.
   */
  public static final String DATA_OBJECT_STEREOTYPE = "dataObject";

  /**
   * Stereotype name for the reference code stereotype.
   */
  public static final String REFERENCE_CODE_STEREOTYPE = "referenceCode";

  /**
   * Key for a property of the cartridge used to retrieve user values.
   */
  public static final String REFERENCE_CODE_FOLDER_KEY = "bo-cartridge.reference-code-folder";

  /**
   * Key for a property of the cartridge used to retrieve user values.
   */
  public static final String BO_GENERATE_INTERFACE_KEY = "bo-cartridge.should-generate-interface";

  /**
   * Key for a property of the cartridge used to retrieve user values.
   */
  public static final String ROOT_COMMON_FOLDER_KEY = "bo-cartridge.common-source-root-folder";

  /**
   * Key for a property of the cartridge used to retrieve user values.
   */
  public static final String ROOT_SERVER_FOLDER_KEY = "bo-cartridge.server-source-root-folder";

  /**
   * Key for a property of the cartridge used to retrieve user values.
   */
  public static final String ROOT_CLIENT_FOLDER_KEY = "bo-cartridge.client-source-root-folder";

  /**
   * Key for a property of the cartridge used to retrieve user values.
   */
  public static final String IF_PACKAGE_POSTFIX_KEY = "bo-cartridge.interface-package-postfix";

  /**
   * Key for a property of the cartridge used to retrieve user values.
   */
  public static final String BO_PACKAGE_POSTFIX_KEY = "bo-cartridge.business-package-postfix";

  /**
   * Key for a property of the cartridge used to retrieve user values.
   */
  public static final String LW_PACKAGE_POSTFIX_KEY = "bo-cartridge.lightweight-package-postfix";

  /**
   * Key for a property of the cartridge used to retrieve user values.
   */
  public static final String IF_CLASS_PREFIX_KEY = "bo-cartridge.interface-object-class-prefix";

  /**
   * Key for a property of the cartridge used to retrieve user values.
   */
  public static final String IF_CLASS_POSTFIX_KEY = "bo-cartridge.interface-object-class-postfix";

  /**
   * Key for a property of the cartridge used to retrieve user values.
   */
  public static final String BO_CLASS_PREFIX_KEY = "bo-cartridge.business-object-class-prefix";

  /**
   * Key for a property of the cartridge used to retrieve user values.
   */
  public static final String BO_CLASS_POSTFIX_KEY = "bo-cartridge.business-object-class-postfix";

  /**
   * Key for a property of the cartridge used to retrieve user values.
   */
  public static final String LW_CLASS_PREFIX_KEY = "bo-cartridge.lightweight-object-class-prefix";

  /**
   * Key for a property of the cartridge used to retrieve user values.
   */
  public static final String LW_CLASS_POSTFIX_KEY = "bo-cartridge.lightweight-object-class-postfix";

  /**
   * Key for a property of the cartridge used to retrieve user values.
   */
  public static final String FIELD_PREFIX_KEY = "bo-cartridge.class-field-prefix";

  /**
   * Key for a property of the cartridge used to retrieve user values.
   */
  public static final String FIELD_POSTFIX_KEY = "bo-cartridge.class-field-postfix";

  /**
   * The flag specifies if the package should be generated or not.
   */
  public static final String GENERATE_TAG = "bo-generate";

  /**
   * The mode of the property either read, write or read-write.
   */
  public static final String MODE_TAG = "bo-mode";

  /**
   * Indicates if the property supports property change events.
   */
  public static final String CHANGE_EVENT_TAG = "bo-change-event";

  /**
   * Indicates if the property supports vetoable change events.
   */
  public static final String VETO_EVENT_TAG = "bo-veto-event";

  /**
   * Indicates if owned data objects can navigate back to their owner.
   */
  public static final String BIDIRECTIONAL_RELATION_TAG = "bo-bidirectional";

  /**
   * True if the simple property should be a parameter of the specialized
   * constructor of the class.
   */
  public static final String CONSTRUCTOR_PARAM_TAG = "bo-constructor-parameter";

  /**
   * Indicates if the property is member of the lightweight representation.
   */
  public static final String LIGHTWEIGHT_TAG = "bo-lightweight";

  /**
   * Defines the class used to store the items of the indexed property. The type
   * of the indexed property defines the type of items in the container.
   */
  public static final String CONTAINER_CLASS_TAG = "bo-container-class";

  /**
   * Defines the interface of the class used to store the items of the indexed
   * property. This information supports interface for public signature coding
   * guideline.
   */
  public static final String INTERFACE_CLASS_TAG = "bo-interface-class";

  /**
   * Flag indicating if an indexed property is ordered or not. If it is ordered
   * the key class tag defines the type of the key used to order the items.
   */
  public static final String IS_ORDERED_TAG = "bo-is-ordered";

  /**
   * Flag indicating if a class is a root class in data object graph or not.
   */
  public static final String IS_ROOT_TAG = "bo-is-root";

  /**
   * Defines the class of the key used to order the indexed property. This tag
   * is only relevant if the indexed property is ordered.
   */
  public static final String KEY_CLASS_TAG = "bo-key-class";

  /**
   * Attribute containing the qualified package name containing the interfaces
   * of the package.
   */
  public static final String IF_PACKAGE = "bo-if-package";

  /**
   * Attribute containing the qualified package name containing the
   * implementation classes of the package.
   */
  public static final String BO_PACKAGE = "bo-mo-package";

  /**
   * Attribute containing the qualified package name containing the lightweight
   * implementation classes of the package.
   */
  public static final String LW_PACKAGE = "bo-lw-package";

  /**
   * Attribute containing the name of the interface of the data class.
   */
  public static final String IF_CLASS = "bo-if-class";

  /**
   * Attribute containing the name of the implemenation of the data class.
   */
  public static final String BO_CLASS = "bo-mo-class";

  /**
   * Attribute containing the name of the lightweight implementation of the data
   * class.
   */
  public static final String LW_CLASS = "bo-lw-class";

  /**
   * Attribute indicating if the class should be generated or not.
   */
  public static final String BO_GENERATE = "bo-generate";

  /**
   * Attribute indicating if the class has changeable properties.
   */
  public static final String BO_CHANGEABLE = "bo-changeable";

  /**
   * Attribute indicating if the class has vetoable properties.
   */
  public static final String BO_VETOABLE = "bo-vetoable";

  /**
   * Attribute indicating if the class or the package has lightweight
   * properties.
   */
  public static final String BO_LIGHTWEIGHT = "bo-lightweight";

  /**
   * Attribute indicating if the class has an interface.
   */
  public static final String BO_HAS_INTERFACE = "bo-has-interface";

  /**
   * Attribute defining the unqualified class name for the storage interface of
   * an indexed property.
   */
  public static final String BO_STORAGE_INTERFACE = "bo-storage-interface";

  /**
   * Attribute defining the unqualified class name for the storage type of an
   * indexed property.
   */
  public static final String BO_STORAGE_TYPE = "bo-storage-type";

  /**
   * Log instance for the class.
   */
  private static Log log = LogFactory.getLog(BogCartridge.class);

  private static List propertyTypes;

  private static List stereotypes;

  /**
   * velocity template to generate data objects.
   */
  private Template javaBoTemplate;

  /**
   * Velocity template to generate lightweight data objects.
   */
  private Template javaLwTemplate;

  /**
   * Velocity template to generate reference service manager for the package.
   */
  private Template referenceServiceTemplate;

  /**
   * Velocity template to generate visitor interface pattern for the package.
   */
  private Template visitorIfBoTemplate;

  /**
   * Velocity template to generate abstract class of the visitor interface with
   * a functor call.
   */
  private Template visitorFunctorBoTemplate;

  /**
   * Velocity template to generate visitor interface pattern for the package.
   */
  private Template visitorIfLwTemplate;

  /**
   * Velocity template to generate abstract class of the visitor interface with
   * a functor call.
   */
  private Template visitorFunctorLwTemplate;

  /**
   * Velocity template to generate the transformer between data object graph and
   * the corresponding lightweight graph.
   */
  private Template transformerLwTemplate;

  /**
   * Velocity template to generate the type factory for all data object types of
   * the package.
   */
  private Template typeFactoryTemplate;

  /**
   * Velocity template to generate the type factory for all data object types of
   * the package.
   */
  private Template referenceCodeTemplate;

  /**
   * The actual package being processed in the cartridge.
   */
  private MetaPackage metaPackage;

  private boolean hasCodes;

  private boolean hasDataObjects;

  /**
   * Root directory of the generated source files.
   */
  private File sourceRootDirectory;

  /**
   * Constructor of the class. All data object related code generator templates
   * are loaded.
   */
  public BogCartridge() {
    super("Data Object Graph Cartridge", NAME, new VersionId(0, 3, 0));
    stereotypes = new ArrayList();
    stereotypes.add(new StereotypeType(DATA_OBJECT_STEREOTYPE));
    stereotypes.add(new StereotypeType(REFERENCE_CODE_STEREOTYPE));
    try {
      javaLwTemplate = Velocity.getTemplate("bog-javaLw.vm");
      javaBoTemplate = Velocity.getTemplate("bog-javaBo.vm");
      referenceServiceTemplate = Velocity
          .getTemplate("bog-referenceService.vm");
      visitorIfBoTemplate = Velocity.getTemplate("bog-visitorIfBo.vm");
      visitorFunctorBoTemplate = Velocity
          .getTemplate("bog-visitorFunctorBo.vm");
      visitorIfLwTemplate = Velocity.getTemplate("bog-visitorIfLw.vm");
      visitorFunctorLwTemplate = Velocity
          .getTemplate("bog-visitorFunctorLw.vm");
      transformerLwTemplate = Velocity.getTemplate("bog-transformerLw.vm");
      typeFactoryTemplate = Velocity.getTemplate("bog-typeFactory.vm");
      referenceCodeTemplate = Velocity.getTemplate("bog-referenceCode.vm");
    } catch (ParseErrorException pee) {
      log.error(pee);
      pee.printStackTrace();
    } catch (ResourceNotFoundException rnfe) {
      log.error(rnfe);
      rnfe.printStackTrace();
    } catch (Exception exception) {
      log.error(exception);
      exception.printStackTrace();
    }
  }

  public void initialize(Properties properties) {
    super.initialize(properties);
    addUndefinedProperty(properties, REFERENCE_CODE_FOLDER_KEY, "");
    addUndefinedProperty(properties, ROOT_COMMON_FOLDER_KEY, "");
    addUndefinedProperty(properties, ROOT_SERVER_FOLDER_KEY, "");
    addUndefinedProperty(properties, ROOT_CLIENT_FOLDER_KEY, "");
    addUndefinedProperty(properties, BO_GENERATE_INTERFACE_KEY, "false");
    addUndefinedProperty(properties, IF_PACKAGE_POSTFIX_KEY, "");
    addUndefinedProperty(properties, BO_PACKAGE_POSTFIX_KEY, "");
    addUndefinedProperty(properties, LW_PACKAGE_POSTFIX_KEY, "");
    addUndefinedProperty(properties, IF_CLASS_PREFIX_KEY, "");
    addUndefinedProperty(properties, IF_CLASS_POSTFIX_KEY, "");
    addUndefinedProperty(properties, BO_CLASS_PREFIX_KEY, "");
    addUndefinedProperty(properties, BO_CLASS_POSTFIX_KEY, "");
    addUndefinedProperty(properties, LW_CLASS_PREFIX_KEY, "");
    addUndefinedProperty(properties, LW_CLASS_POSTFIX_KEY, "");
    addUndefinedProperty(properties, FIELD_PREFIX_KEY, "");
    addUndefinedProperty(properties, FIELD_POSTFIX_KEY, "");
  }

  public List getTaggedValueTypes(Class clazz) {
    List types = new ArrayList();
    if (clazz.equals(MetaModel.class)) {
      types.add(new TaggedValueType(GENERATE_TAG, "true", Boolean.TYPE, this));
    } else if (clazz.equals(MetaPackage.class)) {
      types.add(new TaggedValueType(GENERATE_TAG, "false", Boolean.TYPE, this));
    } else if (clazz.equals(MetaClass.class)) {
      types.add(new TaggedValueType(IS_ROOT_TAG, "false", Boolean.TYPE, this));
    } else if (clazz.equals(MetaProperty.class)) {
      types.add(new TaggedValueType(MODE_TAG, "READ_WRITE",
          PropertyModeCode.class, this));
      types.add(new TaggedValueType(CONSTRUCTOR_PARAM_TAG, "false",
          Boolean.TYPE, this));
      types.add(new TaggedValueType(CHANGE_EVENT_TAG, "false", Boolean.TYPE, this));
      types.add(new TaggedValueType(VETO_EVENT_TAG, "false", Boolean.TYPE, this));
      types.add(new TaggedValueType(BIDIRECTIONAL_RELATION_TAG, "false",
          Boolean.TYPE, this));
      types.add(new TaggedValueType(LIGHTWEIGHT_TAG, "false", Boolean.TYPE, this));
    } else if (clazz.equals(MetaIndexedProperty.class)) {
      types.add(new TaggedValueType(MODE_TAG, "READ_WRITE", String.class, this));
      types.add(new TaggedValueType(MODE_TAG, "READ_WRITE", String.class, this));
      types.add(new TaggedValueType(CONSTRUCTOR_PARAM_TAG, "false",
          Boolean.TYPE, this));
      types.add(new TaggedValueType(CHANGE_EVENT_TAG, "false", Boolean.TYPE, this));
      types.add(new TaggedValueType(VETO_EVENT_TAG, "false", Boolean.TYPE, this));
      types.add(new TaggedValueType(BIDIRECTIONAL_RELATION_TAG, "false",
          Boolean.TYPE, this));
      types.add(new TaggedValueType(CONTAINER_CLASS_TAG, "java.util.ArrayList",
          String.class, this));
      types.add(new TaggedValueType(INTERFACE_CLASS_TAG, "java.util.List",
          String.class, this));
      types.add(new TaggedValueType(IS_ORDERED_TAG, "false", String.class, this));
      types.add(new TaggedValueType(KEY_CLASS_TAG, "String", String.class, this));
      types.add(new TaggedValueType(LIGHTWEIGHT_TAG, "false", String.class, this));
    }
    return types;
  }

  public TaggedValue getTaggedValueWithElementDependentDefault(TaggedValueType taggedValueType, MetaElement element){
    return new TaggedValue(taggedValueType);
  }
  
  /**
   * Computes the data object cartridge relevant attributes for the meta
   * package. The following attributes are defined.
   * <ul>
   * <dt>IF_PACKAGE</dt>
   * <dd>Name of the package containing the interfaces of data objects. The
   * name is the aggregation of the base package name, the interface extension
   * and the prefix and postfix for the interface package.</dd>
   * <dt>BO_PACKAGE</dt>
   * <dd>Name of the package containing the implementation of the data objects.
   * The name is the aggregation of the base package name, the interface
   * extension and the prefix and postfix for the data object package.</dd>
   * <dt>LW_PACKAGE</dt>
   * <dd>Name of the package containing the lightweight representation of data
   * objects. The name is the aggregation of the base package name, the
   * interface extension and the prefix and postfix for the lightweight
   * representation package.</dd>
   * </ul>
   * @param metaPackage the package, which attributes should be computed.
   * @pre metaPackage != null
   */
  public void preProcessPackage(MetaPackage metaPackage) {
    Properties context = getContext();
    StringBuffer buffer = new StringBuffer();
    String qualifiedName = metaPackage.qualifiedName();
    buffer.append(qualifiedName).append(
        context.getProperty(IF_PACKAGE_POSTFIX_KEY));
    metaPackage.addAttribute(IF_PACKAGE, buffer.toString());
    buffer.setLength(0);
    buffer.append(qualifiedName).append(
        context.getProperty(BO_PACKAGE_POSTFIX_KEY));
    metaPackage.addAttribute(BO_PACKAGE, buffer.toString());
    buffer.setLength(0);
    buffer.append(qualifiedName).append(
        context.getProperty(LW_PACKAGE_POSTFIX_KEY));
    metaPackage.addAttribute(LW_PACKAGE, buffer.toString());
    metaPackage.addAttribute(BO_LIGHTWEIGHT, Boolean
        .toString(hasLightweight(metaPackage)));
    super.preProcessPackage(metaPackage);
  }

  /**
   * Computes the data cartridge relevant attributes for the meta class. The
   * following attributes are defined.
   * <ul>
   * <dt>IF_CLASS</dt>
   * <dd>Name of the class representing the interface of the data object. The
   * name is the aggregation of the class name and the class prefix and postfix.
   * </dd>
   * <dt>BO_CLASS</dt>
   * <dd>Name of the class representing the data object implementation. The
   * name is the IF_CLASS and the data object class postfix.</dd>
   * <dt>LW_CLASS</dt>
   * <dd>Name of the class representing the data object lightweight
   * implementation. The name is the IF_CLASS and the data object lightweight
   * class postfix.</dd>
   * </ul>
   * @param clazz the class, which attributes should be computed.
   * @pre clazz != null
   */
  public void preProcessClass(MetaClass clazz) {
    Properties context = getContext();
    String name = clazz.getName();
    StringBuffer buffer = new StringBuffer();
    buffer.append(context.getProperty(IF_CLASS_PREFIX_KEY)).append(name)
        .append(context.getProperty(IF_CLASS_POSTFIX_KEY));
    clazz.addAttribute(IF_CLASS, buffer.toString());
    buffer.setLength(0);
    buffer.append(context.getProperty(BO_CLASS_PREFIX_KEY)).append(name)
        .append(context.getProperty(BO_CLASS_POSTFIX_KEY));
    clazz.addAttribute(BO_CLASS, buffer.toString());
    buffer.setLength(0);
    buffer.append(context.getProperty(LW_CLASS_PREFIX_KEY)).append(name)
        .append(context.getProperty(LW_CLASS_POSTFIX_KEY));
    clazz.addAttribute(LW_CLASS, buffer.toString());
    clazz.addAttribute(BO_HAS_INTERFACE, context
        .getProperty(BO_GENERATE_INTERFACE_KEY));
    clazz.addAttribute(BO_LIGHTWEIGHT, Boolean.toString(hasLightweight(clazz)));

    boolean isChangeable = false;
    boolean isVetoable = false;
    for (Iterator i = clazz.getProperties().iterator(); i.hasNext();) {
      MetaProperty property = (MetaProperty) i.next();
      isChangeable |= property.retrieveIsChangeable();
      isVetoable |= property.retrieveIsVetoable();
    }
    for (Iterator i = clazz.getIndexedProperties().iterator(); i.hasNext();) {
      MetaIndexedProperty property = (MetaIndexedProperty) i.next();
      isChangeable |= property.retrieveIsChangeable();
      isVetoable |= property.retrieveIsVetoable();
      property.addAttribute(BO_STORAGE_INTERFACE, MetaClass
          .getClassName(property.getStorageInterface()));
      property.addAttribute(BO_STORAGE_TYPE, MetaClass.getClassName(property
          .getStorageType()));
    }
    clazz.addAttribute(BO_CHANGEABLE, Boolean.toString(isChangeable));
    clazz.addAttribute(BO_VETOABLE, Boolean.toString(isVetoable));
    super.preProcessClass(clazz);
  }

  public void processModel(MetaModel model) {
    sourceRootDirectory = new File(getContext().getProperty(
        ROOT_COMMON_FOLDER_KEY));
  }

  public void processPackage(MetaPackage metaPackage) {
    this.metaPackage = metaPackage;
    hasDataObjects = false;
    if (!metaPackage.booleanValueOfTag(GENERATE_TAG)) {
      return;
    }
    hasCodes = false;

    for (Iterator i = metaPackage.getClasses().iterator(); i.hasNext()
        && !hasDataObjects;) {
      MetaClass clazz = (MetaClass) i.next();
      hasDataObjects = DATA_OBJECT_STEREOTYPE.equalsIgnoreCase(clazz
          .getStereotype());
    }
  }

  public void processClass(MetaClass clazz) {
    if (clazz.isNestedClass())
      return;
    String stereotype = clazz.getStereotype();
    if (!clazz.retrievePackage().booleanValueOfTag(GENERATE_TAG)
        || (!DATA_OBJECT_STEREOTYPE.equalsIgnoreCase(stereotype) && !REFERENCE_CODE_STEREOTYPE
            .equalsIgnoreCase(stereotype))) {
      return;
    }

    VelocityContext context = createContext(clazz);
    super.processClass(clazz);
    try {
      if (stereotype.equalsIgnoreCase(DATA_OBJECT_STEREOTYPE)) {
        generateJavaFile(metaPackage.getAttribute(BO_PACKAGE), clazz
            .getAttribute(BO_CLASS), javaBoTemplate, context);
        if (hasLightweight(clazz)) {
          generateJavaFile(metaPackage.getAttribute(LW_PACKAGE), clazz
              .getAttribute(LW_CLASS), javaLwTemplate, context);
        }
      } else if (stereotype.equalsIgnoreCase(REFERENCE_CODE_STEREOTYPE)) {
        generateJavaFile(metaPackage.getAttribute(BO_PACKAGE), clazz
            .getAttribute(BO_CLASS), referenceCodeTemplate, context);
      }
    } catch (IOException ioe) {
      log.equals(ioe);
      ioe.printStackTrace();
    }
  }

  public void postProcessPackage(MetaPackage metaPackage) {
    if (metaPackage.booleanValueOfTag(GENERATE_TAG)) {
      VelocityContext context = createContext(metaPackage);
      try {
        if (hasCodes) {
          generateJavaFile(metaPackage.getAttribute(BO_PACKAGE),
              "ReferenceService" + metaPackage.nameFirstUpper(),
              referenceServiceTemplate, context);
        }
        if (hasDataObjects) {
          generateJavaFile(metaPackage.getAttribute(IF_PACKAGE), "Visitor",
              visitorIfBoTemplate, context);
          generateJavaFile(metaPackage.getAttribute(IF_PACKAGE),
              "VisitorFunctor", visitorFunctorBoTemplate, context);
          generateJavaFile(metaPackage.getAttribute(BO_PACKAGE), "TypeFactory",
              typeFactoryTemplate, context);
        }
        if (metaPackage.booleanValueOfAttribute(BO_LIGHTWEIGHT)) {
          generateJavaFile(metaPackage.getAttribute(LW_PACKAGE), "LwVisitor",
              visitorIfLwTemplate, context);
          generateJavaFile(metaPackage.getAttribute(LW_PACKAGE),
              "LwVisitorFunctor", visitorFunctorLwTemplate, context);
          generateJavaFile(metaPackage.getAttribute(LW_PACKAGE),
              "LwTransformer", transformerLwTemplate, context);
        }
      } catch (IOException ioe) {
        log.equals(ioe);
        ioe.printStackTrace();
      }
    }
  }

  /**
   * Generates the import statements necessary for the class defining the data
   * object type.
   * @param clazz class for which the import statements should be retrieved.
   * @param packageName qualified name of the package containing the generated
   *        artifact.
   * @return list of all import statements for the class.
   */
  public ImportStatements retrieveImportStatementsForBo(MetaClass clazz,
      String packageName) {
    ImportStatements imports = new ImportStatements(packageName);
    return retrieveImportStatements(imports, clazz, null, BO_CLASS);
  }

  /**
   * Generates the import statements necessary for the class definint the data
   * object type.
   * @param clazz class for which the import statements should be retrieved.
   * @param packageName qualified name of the package containing the generated
   *        artifact.
   * @return list of all import statements for the class.
   */
  public ImportStatements retrieveImportStatementsForIf(MetaClass clazz,
      String packageName) {
    ImportStatements imports = new ImportStatements(packageName);
    return retrieveImportStatements(imports, clazz, null, IF_CLASS);
  }

  /**
   * Generates the import statements necessary for the class definint the data
   * object type.
   * @param clazz class for which the import statements should be retrieved.
   * @param packageName qualified name of the package containing the generated
   *        artifact.
   * @return list of all import statements for the class.
   */
  public ImportStatements retrieveImportStatementsForLw(MetaClass clazz,
      String packageName) {
    ImportStatements imports = new ImportStatements(packageName);
    return retrieveImportStatements(imports, clazz, BO_LIGHTWEIGHT, LW_CLASS);
  }

  /**
   * Generates the import statements necessary for the class. The import
   * statement should be only used if the classes belong to another package.
   * @param imports imports statements where to add the new identified ones.
   * @param clazz class for which the import statements should be retrieved.
   * @param key key of the tag or attribute constraining the properties, which
   *        dependencies should be imported.
   * @param kind kind of type to be retrieved from the data object type of the
   *        property.
   * @return list of all import statements for the class.
   */
  private ImportStatements retrieveImportStatements(ImportStatements imports,
      MetaClass clazz, String key, String kind) {
    for (Iterator i = clazz.retrieveAncestors().iterator(); i.hasNext();) {
      imports.addClass((MetaClass) i.next());
    }

    for (Iterator i = clazz.retrieveInterfaces().iterator(); i.hasNext();) {
      imports.addClass((MetaClass) i.next());
    }

    Iterator iterator = clazz.getProperties().iterator();
    while (iterator.hasNext()) {
      MetaProperty property = (MetaProperty) iterator.next();
      if ((key == null)
          || (property.booleanValueOfAttribute(key) || property
              .booleanValueOfTag(key))) {
        MetaClass type = clazz.findClass(property.getTypeName());
        if (type != null) {
          addImportStatement(imports, clazz, type, kind);
        } else {
          addImportStatement(imports, clazz, property.getTypeName());
        }
      }
    }
    for (Iterator i = clazz.getIndexedProperties().iterator(); i.hasNext();) {
      MetaIndexedProperty property = (MetaIndexedProperty) i.next();
      if ((key == null)
          || (property.booleanValueOfAttribute(key) || property
              .booleanValueOfTag(key))) {
        MetaClass type = clazz.findClass(property.getTypeName());
        if (type != null) {
          addImportStatement(imports, clazz, type, kind);
        } else {
          addImportStatement(imports, clazz, property.getTypeName());
        }
        imports.addClassName("java.util.Collection");
        imports.addClassName("java.util.Iterator");
        addImportStatement(imports, clazz, property.getStorageInterface());
        addImportStatement(imports, clazz, property.getStorageType());
        if (property.isOrdered()) {
          type = clazz.findClass(property.getKeyType());
          if (type != null) {
            imports.addClass(type);
          }
        }
      }
    }
    return imports;
  }

  /**
   * Adds the import statement for the type entity. If the attribute is defined
   * it is used otherwise the qualified type name is used.
   * @param statements
   * @param clazz
   * @param type
   * @param attribute
   */
  private void addImportStatement(ImportStatements statements, MetaClass clazz,
      MetaClass type, String attribute) {
    String typeName = type.getAttribute(attribute);
    if (typeName == null) {
      addImportStatement(statements, clazz, type.qualifiedName());
    } else {
      statements.addClassName(typeName);
    }
  }

  /**
   * Adds the qualified type name to the import statements.
   * @param statements
   * @param clazz
   * @param qualifiedName
   */
  private void addImportStatement(ImportStatements statements, MetaClass clazz,
      String qualifiedName) {
    if (MdaUtilities.isStandardItem(qualifiedName)) {
      statements.addClassName(qualifiedName);
    } else {
      MetaClass type = clazz.findClass(qualifiedName);
      if (type != null) {
        statements.addClass(type);
      } else {
        if (log.isErrorEnabled()) {
          Object[] parameters = { qualifiedName, clazz.qualifiedName() };
          log
              .error(MessageFormat
                  .format(
                      "Non standard class {0} used in class {1} is not defined in model",
                      parameters));
        }
      }
    }
  }

  private boolean hasLightweight(MetaPackage metaPackage) {
    boolean hasLightweight = false;
    for (Iterator i = metaPackage.getClasses().iterator(); i.hasNext()
        && !hasLightweight;) {
      hasLightweight = hasLightweight((MetaClass) i.next());
    }
    return hasLightweight;
  }

  /**
   * Returns true if the meta class has lightweight properties.
   * @param clazz class to check if it has lightweight properties.
   * @return true if the class has lightweights items otherwise false.
   * @pre clazz != null
   */
  private boolean hasLightweight(MetaClass clazz) {
    assert (clazz != null);

    boolean hasLightweight = false;
    for (Iterator i = clazz.getProperties().iterator(); i.hasNext()
        && !hasLightweight;) {
      hasLightweight = ((MetaProperty) i.next())
          .booleanValueOfTag(LIGHTWEIGHT_TAG);
    }
    if (!hasLightweight) {
      for (Iterator i = clazz.getIndexedProperties().iterator(); i.hasNext()
          && !hasLightweight;) {
        hasLightweight = ((MetaIndexedProperty) i.next())
            .booleanValueOfTag(LIGHTWEIGHT_TAG);
      }
    }
    return hasLightweight;
  }

  private void generateJavaFile(String packageName, String className,
      Template template, VelocityContext context) throws IOException {
    PrintWriter writer = createJavaWriter(sourceRootDirectory, packageName,
        className);
    writer.print(processTemplate(template, context));
    writer.close();
  }
}