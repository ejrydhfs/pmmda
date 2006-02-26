/* copyright 2000-2006 Marcel Baumann 
 * Licensed under the Apache License, Version 2.0 (the "Licence"); 
 * you may not use this file except in compliance with the License. 
 * $Id$ 
 */
package ch.bbv.pim.ui.actions;

import javax.swing.JFileChooser;

import ch.bbv.explorer.actions.SimpleFileFilter;

/**
 * The interface defines all the file extensions and descriptive text for the
 * file types supported in the application.
 * @author MarcelBaumann
 * @version $Revision$
 */
public class FileTypes {
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
   * Adds all file filters defined for the MDA application.
   * @param chooser file chooser to which the file filters must be added.
   */
  public static void addFileFilters(JFileChooser chooser) {
    chooser.addChoosableFileFilter(new SimpleFileFilter(LDIF_EXT, LDIF_TEXT));
    chooser.addChoosableFileFilter(new SimpleFileFilter(VCARD_EXT, VCARD_TEXT));
  }
}