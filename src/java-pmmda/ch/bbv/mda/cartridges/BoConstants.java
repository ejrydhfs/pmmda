/* copyright 2000-2005 Marcel Baumann 
 * Licensed under the Apache License, Version 2.0 (the "Licence"); 
 * you may not use this file except in compliance with the License. 
 * $Id$ 
 */
package ch.bbv.mda.cartridges;

/**
 * The constant class defines the public constant declarations of the cartridge.
 * The stereotypes, tags, attributes and artifact markers are made available.
 * The keys of the properties available to the cartridge are also made
 * available.
 * @author MarcelBaumann
 * @version $Revision$
 */
public final class BoConstants {
  /**
   * Stereotype name for the data object stereotype.
   */
  public static final String DATA_OBJECT_STEREOTYPE = "dataObject";

  /**
   * Stereotype name for the reference code stereotype.
   */
  public static final String REFERENCE_CODE_STEREOTYPE = "referenceCode";

  /**
   * Stereotype name for the service stereotype.
   */
  public static final String SERVICE_STEREOTYPE = "service";

  /**
   * Stereotype name for the interface stereotype.
   */
  public static final String INTERFACE_STEREOTYPE = "interface";

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
   * Key for a property of the cartridge used to retrieve user values.
   */
  public static final String CYCLIC_WRAPPER_KEY = "bo-cartridge.generate-cyclic-visitor";

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
   * The flag specifies if the package should be generated or not.
   */
  public static final String GENERATE = "bo-generate";

  /**
   * The value is the logical root directory, meaning a variable defined in the
   * properties where the Java code artifacts are created.
   */
  public static final String ROOT = "bo-root";

  /**
   * The mode of the property either read, write or read-write.
   */
  public static final String MODE = "bo-mode";

  /**
   * Indicates if the property supports property change events.
   */
  public static final String CHANGE_EVENT = "bo-change-event";

  /**
   * Indicates if the property supports vetoable change events.
   */
  public static final String VETO_EVENT = "bo-veto-event";

  /**
   * Indicates if owned data objects can navigate back to their owner.
   */
  public static final String NAVIGABLE_RELATION = "bo-navigable";

  /**
   * Indicates if the property is member of the lightweight representation.
   */
  public static final String LIGHTWEIGHT = "bo-lightweight";

  /**
   * Defines the class used to store the items of the indexed property. The type
   * of the indexed property defines the type of items in the container.
   */
  public static final String CONTAINER_CLASS = "bo-container-class";

  /**
   * Defines the interface of the class used to store the items of the indexed
   * property. This information supports interface for public signature coding
   * guideline.
   */
  public static final String INTERFACE_CLASS = "bo-interface-class";

  /**
   * Flag indicating if a class is a root class in data object graph or not.
   */
  public static final String IS_ROOT = "bo-is-root";

  /**
   * Defines the class of the key used to order the indexed property. This tag
   * is only relevant if the indexed property is ordered.
   */
  public static final String KEY_CLASS = "bo-key-class";

  /**
   * Attribute containing the mapping of the classifier to the primitive type or
   * class of the target language.
   */
  public static final String BO_DATATYPE = "bo-datatype";

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

}
