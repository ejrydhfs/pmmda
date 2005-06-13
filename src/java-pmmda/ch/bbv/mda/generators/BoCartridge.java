/* copyright 2000-2005 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence");
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
import ch.bbv.dog.validation.Validator;
import ch.bbv.mda.*;
import ch.bbv.mda.generators.validation.DogTagRule;
import ch.bbv.mda.generators.validation.TagRule;
import ch.bbv.mda.operators.FileOperators;
import ch.bbv.mda.operators.ModelOperators;

/**
 * The data object cartridge is responsible for the generation of all data
 * object types and the associated helper classes. The code generator is able to
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
 * <li>A reference service is generated for all the reference codes defined in
 * one package. Convenience methods to access each type of code are provided. If
 * no reference code exists in the package no service will be generated.</li>
 * <li>Helper classes are generated to provide a complete implementation of the
 * visitor pattern on all data object classes defined in the package. The
 * generated visitor can visit data objects defined in other packages but do not
 * generate specialized methods for their descendents.</li>
 * </ul>
 * @author Marcel Baumann
 * @version $Revision$
 */
public class BoCartridge extends CartridgeImpl implements Cartridge {
  /**
   * Key for all user defined blocks stored in the Velocity context.
   */
  public static final String CODE_BLOCKS = "blocks";

  /**
   * Key for user defined source code.
   */
  public static final String IMPORT_BLOCK = "imports";

  /**
   * Key for user defined source code.
   */
  public static final String DECLARATION_BLOCK = "declarations";

  /**
   * Key for user defined source code.
   */
  public static final String CONSTRUCTOR_BLOCK = "constructors";

  /**
   * Key for user defined source code.
   */
  public static final String DEFINITION_BLOCK = "definitions";

  /**
   * Key for user defined source code.
   */
  public static final String PRIVATE_DEFINITION_BLOCK = "private definitions";

  /**
   * Comment in the generated code to detect a border of a user defined source
   * code.
   */
  public static final String USER_IMPORT_BEGIN = "/* %% USER DEFINED IMPORT START %% */";

  /**
   * Comment in the generated code to detect a border of a user defined source
   * code.
   */
  public static final String USER_IMPORT_END = "/* %% USER DEFINED IMPORT END %% */";

  /**
   * Comment in the generated code to detect a border of a user defined source
   * code.
   */
  public static final String USER_DECLARATION_BEGIN = "/* %% USER DEFINED DECLARATION START %% */";

  /**
   * Comment in the generated code to detect a border of a user defined source
   * code.
   */
  public static final String USER_DECLARATION_END = "/* %% USER DEFINED DECLARATION END %% */";

  /**
   * Comment in the generated code to detect a border of a user defined source
   * code.
   */
  public static final String USER_CONSTRUCTORS_BEGIN = "/* %% USER DEFINED CONSTRUCTORS START %% */";

  /**
   * Comment in the generated code to detect a border of a user defined source
   * code.
   */
  public static final String USER_CONSTRUCTORS_END = "/* %% USER DEFINED CONSTRUCTORS END %% */";

  /**
   * Comment in the generated code to detect a border of a user defined source
   * code.
   */
  public static final String USER_CODE_BEGIN = "/* %% USER DEFINED CODE START %% */";

  /**
   * Comment in the generated code to detect a border of a user defined source
   * code.
   */
  public static final String USER_CODE_END = "/* %% USER DEFINED CODE END %% */";

  /**
   * Comment in the generated code to detect a border of a user defined source
   * code for protected or private methods.
   */
  public static final String USER_PRIVATE_CODE_BEGIN = "/* %% USER DEFINED PROTECTED/PRIVATE CODE START %% */";

  /**
   * Comment in the generated code to detect a border of a user defined source
   * code for protected or private methods.
   */
  public static final String USER_PRIVATE_CODE_END = "/* %% USER DEFINED PROTECTED/PRIVATE CODE END %% */";

  /**
   * The prefix of the cartridge.
   */
  public static final String PREFIX = "bo";

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
  public static final String LANGUAGE_KEY = "bo-cartridge.language";

  /**
   * Key for a property of the cartridge used to retrieve user values.
   */
  public static final String UID_KEY = "bo-cartridge.uid";

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
   * The UML standard persistence tag.
   */
  public static final String PERSISTENCE_TAG = "persistence";

  /**
   * The flag specifies if the package should be generated or not.
   */
  public static final String GENERATE_TAG = "bo-generate";

  /**
   * The value is the logical root directory, meaning a variable defined in the
   * properties where the Java code artifacts are created.
   */
  public static final String ROOT_TAG = "bo-root";

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
  public static final String NAVIGABLE_RELATION_TAG = "bo-navigable";

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
   * Log instance for all instatnces of the class.
   */
  private static Log log = LogFactory.getLog(BoCartridge.class);

  /**
   * List of stereotypes managed in the cartride.
   */
  private static List<StereotypeType> stereotypes;

  /**
   * Velocity template to generate data objects.
   */
  private Template javaClassTemplate;

  /**
   * Velocity template to generate data objects.
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

  /**
   * Flag indicating if the package contains reference code declarations.
   */
  private boolean hasCodes;

  /**
   * Flag indicating if the package contains data object declarations.
   */
  private boolean hasDataObjects;

  /**
   * Root directory of the generated source files.
   */
  private File rootDirectory;

  /**
   * Constructor of the class. All data object related code generator templates
   * are loaded.
   */
  public BoCartridge() {
    super("Data Object Graph Cartridge", PREFIX, new VersionId(0, 3, 0));
    stereotypes = new ArrayList<StereotypeType>();
    stereotypes.add(new StereotypeType(DATA_OBJECT_STEREOTYPE));
    stereotypes.add(new StereotypeType(REFERENCE_CODE_STEREOTYPE));
  }

  public void initialize(Properties properties) {
    super.initialize(properties);
    addUndefinedProperty(properties, REFERENCE_CODE_FOLDER_KEY, "");
    addUndefinedProperty(properties, ROOT_COMMON_FOLDER_KEY, "");
    addUndefinedProperty(properties, ROOT_SERVER_FOLDER_KEY, "");
    addUndefinedProperty(properties, ROOT_CLIENT_FOLDER_KEY, "");
    addUndefinedProperty(properties, LANGUAGE_KEY, "java-1.4");
    addUndefinedProperty(properties, UID_KEY, "99990101");
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
    try {
      String postfix = "-" + getContext().getProperty(LANGUAGE_KEY) + ".vm";
      javaLwTemplate = Velocity.getTemplate("bog-Lw" + postfix);
      javaBoTemplate = Velocity.getTemplate("bog-Bo" + postfix);
      javaClassTemplate = Velocity.getTemplate("bog-Class" + postfix);
      referenceServiceTemplate = Velocity.getTemplate("bog-CodeService"
          + postfix);
      visitorIfBoTemplate = Velocity.getTemplate("bog-visitorIfBo" + postfix);
      visitorFunctorBoTemplate = Velocity.getTemplate("bog-visitorFunctorBo"
          + postfix);
      visitorIfLwTemplate = Velocity.getTemplate("bog-visitorIfLw" + postfix);
      visitorFunctorLwTemplate = Velocity.getTemplate("bog-visitorFunctorLw"
          + postfix);
      transformerLwTemplate = Velocity.getTemplate("bog-transformerLw"
          + postfix);
      typeFactoryTemplate = Velocity.getTemplate("bog-typeFactory" + postfix);
      referenceCodeTemplate = Velocity.getTemplate("bog-Code" + postfix);
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

  public List<TagDefinition> getTaggedValueTypes(Class clazz) {
    List<TagDefinition> types = new ArrayList<TagDefinition>();
    if (clazz.equals(MetaPackage.class)) {
      types.add(new TagDefinition(GENERATE_TAG, "false", Boolean.TYPE));
      types.add(new TagDefinition(ROOT_TAG, String.class));
    } else if (clazz.equals(MetaClass.class)) {
      types.add(new TagDefinition(GENERATE_TAG, "false", Boolean.TYPE));
      types.add(new TagDefinition(PERSISTENCE_TAG, "false", Boolean.TYPE));
      types.add(new TagDefinition(IS_ROOT_TAG, "false", Boolean.TYPE));
    } else if (clazz.equals(MetaProperty.class)) {
      types.add(new TagDefinition(PERSISTENCE_TAG, "false", Boolean.TYPE));
      types.add(new TagDefinition(MODE_TAG, "READ_WRITE",
          PropertyModeCode.class));
      types.add(new TagDefinition(CHANGE_EVENT_TAG, "false", Boolean.TYPE));
      types.add(new TagDefinition(VETO_EVENT_TAG, "false", Boolean.TYPE));
      types.add(new TagDefinition(NAVIGABLE_RELATION_TAG, String.class));
      types.add(new TagDefinition(LIGHTWEIGHT_TAG, "false", Boolean.TYPE));
    } else if (clazz.equals(MetaIndexedProperty.class)) {
      types.add(new TagDefinition(PERSISTENCE_TAG, "false", Boolean.TYPE));
      types.add(new TagDefinition(MODE_TAG, "READ_WRITE",
          PropertyModeCode.class));
      types.add(new TagDefinition(CHANGE_EVENT_TAG, "false", Boolean.TYPE));
      types.add(new TagDefinition(VETO_EVENT_TAG, "false", Boolean.TYPE));
      types.add(new TagDefinition(NAVIGABLE_RELATION_TAG, String.class));
      types.add(new TagDefinition(CONTAINER_CLASS_TAG, "java.util.ArrayList",
          String.class));
      types.add(new TagDefinition(INTERFACE_CLASS_TAG, "java.util.List",
          String.class));
      types.add(new TagDefinition(KEY_CLASS_TAG, "String", String.class));
      types.add(new TagDefinition(LIGHTWEIGHT_TAG, "false", Boolean.TYPE));
    }
    return types;
  }

  public List<TagDefinition> getObsoleteTaggedValueTypes(Class clazz) {
    List<TagDefinition> types = null;
    if (MetaProperty.class.equals(clazz)) {
      types = new ArrayList<TagDefinition>();
      types.add(new TagDefinition("bo-constructor-parameter", Boolean.TYPE));
      types.add(new TagDefinition("bo-bidirectional", Boolean.TYPE));
      types.add(new TagDefinition("bo-key-class", String.class));
      types.add(new TagDefinition("bo-is-ordered", Boolean.TYPE));
      types.add(new TagDefinition("bo-item-class", String.class));
    } else if (MetaIndexedProperty.class.equals(clazz)) {
      types = new ArrayList<TagDefinition>();
      types.add(new TagDefinition("bo-constructor-parameter", Boolean.TYPE));
      types.add(new TagDefinition("bo-bidirectional", Boolean.TYPE));
      types.add(new TagDefinition("bo-item-class", String.class));
    }
    return types;
  }

  public void registerRules(Validator validator) {
    TagRule tagRule = new DogTagRule();
    tagRule.register(validator);
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
   * @param packages the package, which attributes should be computed.
   * @pre packages != null
   */
  public void preProcessPackage(MetaPackage packages) {
    assert (packages != null);
    Properties context = getContext();
    StringBuilder buffer = new StringBuilder();
    String qualifiedName = packages.qualifiedName();
    buffer.append(qualifiedName).append(
        context.getProperty(IF_PACKAGE_POSTFIX_KEY));
    packages.addAttribute(IF_PACKAGE, buffer.toString());
    buffer.setLength(0);
    buffer.append(qualifiedName).append(
        context.getProperty(BO_PACKAGE_POSTFIX_KEY));
    packages.addAttribute(BO_PACKAGE, buffer.toString());
    buffer.setLength(0);
    buffer.append(qualifiedName).append(
        context.getProperty(LW_PACKAGE_POSTFIX_KEY));
    packages.addAttribute(LW_PACKAGE, buffer.toString());
    packages.addAttribute(BO_LIGHTWEIGHT, Boolean
        .toString(hasLightweight(packages)));
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
    assert (clazz != null);
    Properties context = getContext();
    String name = clazz.getName();
    StringBuilder buffer = new StringBuilder();
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
    for (MetaProperty property : clazz.getProperties()) {
      isChangeable |= property.retrieveIsChangeable();
      isVetoable |= property.retrieveIsVetoable();
    }
    for (MetaIndexedProperty property : clazz.getIndexedProperties()) {
      isChangeable |= property.retrieveIsChangeable();
      isVetoable |= property.retrieveIsVetoable();
      property.addAttribute(INTERFACE_CLASS_TAG, ModelOperators
          .getComponentName(property.valueOfTag(INTERFACE_CLASS_TAG)));
      property.addAttribute(CONTAINER_CLASS_TAG, ModelOperators
          .getComponentName(property.valueOfTag(CONTAINER_CLASS_TAG)));
      property.addAttribute(KEY_CLASS_TAG, ModelOperators
          .getComponentName(property.valueOfTag(KEY_CLASS_TAG)));
    }
    clazz.addAttribute(BO_CHANGEABLE, Boolean.toString(isChangeable));
    clazz.addAttribute(BO_VETOABLE, Boolean.toString(isVetoable));
  }

  public void processPackage(MetaPackage metaPackage) {
    String root = metaPackage.valueOfTag(ROOT_TAG);
    if (root != null) {
      root = getContext().getProperty(root);
      if (root == null) {
        log.error(metaPackage.valueOfTag(ROOT_TAG)
            + " logical root directory undefined");
      }
    }
    if (root == null) {
      root = getContext().getProperty(ROOT_COMMON_FOLDER_KEY);
    }
    rootDirectory = new File(root);
    this.metaPackage = metaPackage;
    hasDataObjects = false;
    hasCodes = false;
    if (!metaPackage.booleanValueOfTag(GENERATE_TAG)) {
      return;
    }
    for (MetaClass clazz : metaPackage.getClasses()) {
      hasDataObjects |= DATA_OBJECT_STEREOTYPE.equalsIgnoreCase(clazz
          .getStereotype());
      hasCodes |= REFERENCE_CODE_STEREOTYPE.equalsIgnoreCase(clazz
          .getStereotype());
    }
  }

  public void processClass(MetaClass clazz) {
    String stereotype = clazz.getStereotype();
    // class is only emitted if the class is relevant and the package should be
    // generated
    if (!clazz.retrievePackage().booleanValueOfTag(GENERATE_TAG)) {
      return;
    }
    VelocityContext context = createContext(clazz);
    super.processClass(clazz);
    try {
      Map<String, List<String>> blocks = extractUserCode(metaPackage
          .getAttribute(BO_PACKAGE), clazz.getAttribute(BO_CLASS));
      context.put(CODE_BLOCKS, blocks);
      if (DATA_OBJECT_STEREOTYPE.equalsIgnoreCase(stereotype)) {
        generateJavaFile(metaPackage.getAttribute(BO_PACKAGE), clazz
            .getAttribute(BO_CLASS), javaBoTemplate, context);
        if (hasLightweight(clazz)) {
          blocks = extractUserCode(metaPackage.getAttribute(LW_PACKAGE), clazz
              .getAttribute(LW_CLASS));
          context.put(CODE_BLOCKS, blocks);
          generateJavaFile(metaPackage.getAttribute(LW_PACKAGE), clazz
              .getAttribute(LW_CLASS), javaLwTemplate, context);
        }
      } else if (REFERENCE_CODE_STEREOTYPE.equalsIgnoreCase(stereotype)) {
        generateJavaFile(metaPackage.getAttribute(BO_PACKAGE), clazz
            .getAttribute(BO_CLASS), referenceCodeTemplate, context);
      } else if (clazz.booleanValueOfTag(BO_GENERATE)) {
        generateJavaFile(metaPackage.getAttribute(BO_PACKAGE), clazz
            .getAttribute(BO_CLASS), javaClassTemplate, context);
      }
    } catch (IOException e) {
      log.error(e);
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
      } catch (IOException e) {
        log.error(e);
      }
    }
  }

  /**
   * Returns true if the property type is a reference code.
   * @param property property to evaluate.
   * @return true if the property type is a reference code otherwise false.
   */
  public boolean isReferenceCode(MetaProperty property) {
    MetaModel model = (MetaModel) property.getRoot();
    MetaClass clazz = model.findClass(property.getTypeName());
    if ((clazz != null) && (clazz.getStereotype() == REFERENCE_CODE_STEREOTYPE)) {
      return true;
    }
    return false;
  }

  /**
   * Returns true if the property is ordered. An indexed property is ordered if
   * the "bo-key-class" is not null.
   * @param property indexed property to evaluate.
   * @return true if the property is ordered otherwise false.
   */
  public boolean isOrdered(MetaIndexedProperty property) {
    String value = property.valueOfTag(KEY_CLASS_TAG);
    return (value != null) && (value.trim().length() > 0);
  }

  /**
   * Generates the import statements necessary for the class defining the data
   * object type.
   * @param clazz class for which the import statements should be retrieved.
   * @param packageName qualified name of the package containing the generated
   *          artifact.
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
   *          artifact.
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
   *          artifact.
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
   *          dependencies should be imported.
   * @param kind kind of type to be retrieved from the data object type of the
   *          property.
   * @return list of all import statements for the class.
   */
  private ImportStatements retrieveImportStatements(ImportStatements imports,
      MetaClass clazz, String key, String kind) {
    for (MetaClass item : clazz.retrieveAncestors()) {
      imports.addClass(item);
    }
    for (MetaClass item : clazz.retrieveInterfaces()) {
      imports.addClass(item);
    }
    for (MetaProperty property : clazz.getProperties()) {
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
    for (MetaIndexedProperty property : clazz.getIndexedProperties()) {
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
        addImportStatement(imports, clazz, property
            .valueOfTag(INTERFACE_CLASS_TAG));
        addImportStatement(imports, clazz, property
            .valueOfTag(CONTAINER_CLASS_TAG));
        if (isOrdered(property)) {
          type = clazz.findClass(property.valueOfTag(KEY_CLASS_TAG));
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
   * @param statements statements to add.
   * @param clazz clazz to which the statements should be added.
   * @param qualifiedName qualified name of the class to import.
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
    for (MetaClass clazz : metaPackage.getClasses()) {
      hasLightweight = hasLightweight(clazz);
      if (hasLightweight) {
        break;
      }
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
    assert clazz != null;
    boolean hasLightweight = false;
    for (MetaProperty property : clazz.getProperties()) {
      hasLightweight = property.booleanValueOfTag(LIGHTWEIGHT_TAG);
      if (hasLightweight) {
        break;
      }
    }
    if (!hasLightweight) {
      for (MetaIndexedProperty property : clazz.getIndexedProperties()) {
        hasLightweight = property.booleanValueOfTag(LIGHTWEIGHT_TAG);
        if (hasLightweight) {
          break;
        }
      }
    }
    if (!hasLightweight) {
      // TODO ancestor lightweight
    }
    return hasLightweight;
  }

  /**
   * Extracts all blocks of declarations defined by the developer. These blocks
   * are stored in the class. The file containing the source code is inferred
   * from the global properties.
   * @param packageName name of the package containing the Java class.
   * @param classname name of the Java class.
   * @return the map of retrieved user code blocks indexed with the block name.
   * @throws IOException if error was encountered when reading the file.
   */
  private Map<String, List<String>> extractUserCode(String packageName,
      String classname) throws IOException {
    Map<String, List<String>> blocks = new HashMap<String, List<String>>();
    File source = FileOperators.createFile(rootDirectory, packageName,
        classname + JAVA_EXTENSION);
    blocks.put(IMPORT_BLOCK, FileOperators.extractSourceCode(source,
        USER_IMPORT_BEGIN, USER_IMPORT_END));
    blocks.put(DECLARATION_BLOCK, FileOperators.extractSourceCode(source,
        USER_DECLARATION_BEGIN, USER_DECLARATION_END));
    blocks.put(CONSTRUCTOR_BLOCK, FileOperators.extractSourceCode(source,
        USER_CONSTRUCTORS_BEGIN, USER_CONSTRUCTORS_END));
    blocks.put(DEFINITION_BLOCK, FileOperators.extractSourceCode(source,
        USER_CODE_BEGIN, USER_CODE_END));
    blocks.put(PRIVATE_DEFINITION_BLOCK, FileOperators.extractSourceCode(
        source, USER_PRIVATE_CODE_BEGIN, USER_PRIVATE_CODE_END));
    return blocks;
  }

  /**
   * Generate a Java class in the associated source file.
   * @param packageName package name of the class to generate.
   * @param className class name of the class to generate.
   * @param template velocity template for the generation.
   * @param context velocity context for the generation.
   * @throws IOException an file IO exception occured when writing the class.
   */
  private void generateJavaFile(String packageName, String className,
      Template template, VelocityContext context) throws IOException {
    PrintWriter writer = createJavaWriter(rootDirectory, packageName, className);
    writer.print(processTemplate(template, context));
    writer.close();
  }
}