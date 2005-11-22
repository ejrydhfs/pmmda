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
public final class OexConstants {
  /**
   * Key for a property of the cartridge used to retrieve user values.
   */
  public static final String RICH_CLIENT_PACKAGE_POSTFIX_KEY = "oex-cartridge.rich-client-package-extension";

  /**
   * Key for a property of the cartridge used to retrieve user values.
   */
  public static final String ROOT_RICH_CLIENT_KEY = "oex-cartridge.rich-client-folder";

  /**
   * Tag name defining the class of the property view used to display the class.
   */
  public static final String VIEW_CLASS = "oex-view-class";

  /**
   * Tag name indicating if the item is visible in the explorer or not.
   */
  public static final String IS_VISIBLE = "oex-visible";

  /**
   * Tag name of the package containing the rich client source code artifacts.
   */
  public static final String OEX_RICH_CLIENT_PACKAGE = "oex-rich-client-package";

}
