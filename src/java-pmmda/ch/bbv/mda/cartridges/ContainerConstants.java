/* copyright 2000-2005 Marcel Baumann, Adrian Bachofen 
 * Licensed under the Apache License, Version 2.0 (the "Licence"); 
 * you may not use this file except in compliance with the License. 
 * $Id:  
 */
package ch.bbv.mda.cartridges;

/**
 * The constant class defines the public constant declarations of the cartridge.
 * @author Adrian Bachofen
 * @version $Revision$
 */
public final class ContainerConstants {

  /**
   * Velocity context key to identify the object stored in the context. Other keys are defined in CartridgeImpl
   */
  public static final String VIEW_KEY = "view";

  /**
   * Key for a property of the cartridge used to retrieve user values.
   */
  public static final String LANGUAGE_KEY = "container-cartridge.language";

  /**
   * Key for a property of the cartridge used to retrieve user values.
   */
  public static final String CONTAINER_PACKAGE_POSTFIX_KEY = "container-cartridge.container-package-postfix";

  /**
   * Attribute containing the qualified package name containing the
   * implementation classes of the package.
   */
  public static final String CONTAINER_PACKAGE = "container-package";

  /**
   * Key for a property of the cartridge used to retrieve user values.
   */
  public static final String CONTAINER_CLASS_PREFIX_KEY = "container-cartridge.container-object-class-prefix";

  /**
   * Key for a property of the cartridge used to retrieve user values.
   */
  public static final String CONTAINER_CLASS_POSTFIX_KEY = "container-cartridge.container-object-class-postfix";

  /**
   * Attribute containing the name of the implemenation of the container class.
   */
  public static final String CONTAINER_CLASS = "container-class";

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

}
