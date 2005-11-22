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
public final class CtoConstants {
  /**
   * Extension for IDL source file.
   */
  public static final String IDL_EXTENSION = ".idl";

  /**
   * Property defining the root folder where all IDL files are generated.
   */
  public static final String ROOT_IDL_KEY = "cto-cartridge.idl-root-folder";

  /**
   * Property defining the root folder where all Java transformer files are
   * generated.
   */
  public static final String ROOT_JAVA_KEY = "cto-cartridge.java-root-folder";

  /**
   * Property defining the package containing all transformers of a data object
   * package.
   */
  public static final String TRANSFORM_PACKAGE_POSTFIX = "cto-cartridge.transform-package-postfix";

  /**
   * Property defining the prefix for all IDL structures.
   */
  public static final String IDL_PREFIX_KEY = "cto-cartridge.idl-prefix";

  /**
   * Property defining the postfix for all IDL structures.
   */
  public static final String IDL_POSTFIX_KEY = "cto-cartridge.idl-postfix";

  /**
   * Tag defining the name of the package containing the IDL sources.
   */
  public static final String CTO_PACKAGE_IDL = "cto-package-idl";

  /**
   * Attribute defining the name of the IDL structure of the data object.
   */
  public static final String CTO_MO_CLASS = "cto-mo-class";

  /**
   * Attribute defining the name of the IDL structure of the lightweight object.
   */
  public static final String CTO_LW_CLASS = "cto-lw-class";

  /**
   * Attribute defining the IDL type of a property.
   */
  public static final String CTO_IDL_TYPE = "cto-idl-type";

  /**
   * Attribute defining the folder containing the data objects to CORBA
   * transformer Java classes.
   */
  public static final String CTO_BO2DTO_PACKAGE = "cto-bo2dto-package";

  /**
   * Attribute defining the folder containing the ligthweight data objects to
   * CORBA transformer Java classes.
   */
  public static final String CTO_LW2DTO_PACKAGE = "cto-lw2dto-package";

  /**
   * Attribute defining the name of the transformer class for the data object
   * transformers.
   */
  public static final String CTO_BO2DTO_CLASS = "cto-bo2dto-class";

  /**
   * Attribute defining the name of the transformer class for the lightweight
   * data object transformers.
   */
  public static final String CTO_LW2DTO_CLASS = "cto-lw2dto-class";

}
