/* copyright 2000-2006 Marcel Baumann 
 * Licensed under the Apache License, Version 2.0 (the "Licence"); 
 * you may not use this file except in compliance with the License. 
 * $Id$ 
 */
package ch.bbv.pim.ui.actions;

import ch.bbv.explorer.actions.FileTypes;
import ch.bbv.explorer.actions.SimpleFileFilter;

/**
 * The class defines all the file extensions and descriptive text for the file
 * types supported in the application.
 * @author MarcelBaumann
 * @version $Revision$
 */
class PimFileTypes extends FileTypes {
  /**
   * File extensions for models developed with ArgoUML.
   */
  public static final String[] LDIF_EXT = { "ldif" };

  /**
   * Text for the associated extensions.
   */
  public static final String LDIF_TEXT = "LDIF - Mozilla, Thunderbird - (*.ldif)";

  /**
   * File extensions for models developed with ArgoUML.
   */
  public static final String[] VCARD_EXT = { "vcf" };

  /**
   * Text for the associated extensions.
   */
  public static final String VCARD_TEXT = "VCARD format (*.vcf)";

  /**
   * The singleton instance.
   */
  private static PimFileTypes instance;

  static {
    instance = new PimFileTypes();
  }

  /**
   * Returns the singleton instance.
   * @return the singleton instance
   */
  public static PimFileTypes getInstance() {
    return instance;

  }

  /**
   * Private constructor for the singleton class.
   */
  private PimFileTypes() {
    setDefaultExtension(VCARD_EXT[0]);
    add(new SimpleFileFilter(LDIF_EXT, LDIF_TEXT));
    add(new SimpleFileFilter(VCARD_EXT, VCARD_TEXT));
  }
}