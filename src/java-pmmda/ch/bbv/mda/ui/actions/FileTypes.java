/* copyright 2000-2005 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence");
 * you may not use this file except in compliance with the License.
 * $Id$
 */

package ch.bbv.mda.ui.actions;

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
  public static final String[] ARGOUML_EXT = { "zargo" };

  /**
   * Text for the associated extensions.
   */
  public static final String ARGOUML_TEXT = "ArgoUML, Poseidon 1.x (*.zargo)";

  /**
   * File extensions for models developed with ArgoUML.
   */
  public static final String[] POSEIDON_EXT = { "zuml" };

  /**
   * Text for the associated extensions.
   */
  public static final String POSEIDON_TEXT = "Poseidon 2.x (*.zuml)";

  /**
   * File extensions for models developed with Rational Rose.
   */
  public static final String[] RATIONAL_ROSE_EXT = { "mdl" };

  /**
   * Text for the associated extensions.
   */
  public static final String RATIONAL_ROSE_TEXT = "Rational Rose (*.mdl)";

  /**
   * Category File extensions for models developed with Rational Rose.
   */
  public static final String[] RATIONAL_ROSE_CAT_EXT = { "cat" };

  /**
   * Text for the associated extensions.
   */
  public static final String RATIONAL_ROSE_CAT_TEXT = "Rational Rose Category (*.cat)";

  /**
   * File extensions for models serialized to XML using the open source library
   * from CodeHaus.
   */
  public static final String[] XML_EXT = { "xml" };

  /**
   * Text for the associated extensions.
   */
  public static final String XML_TEXT = "XML models (*.xml)";

  /**
   * File extensions for models serialized with the Java serialization features.
   */
  public static final String[] SERIALIZE_EXT = { "ser" };

  /**
   * Text for the associated extensions.
   */
  public static final String SERIALIZE_TEXT = "Java serialized models (*.ser)";

  /**
   * File extensions for models according to xmi standard.
   */
  public static final String[] XMI_EXT = { "xmi" };

  /**
   * Text for the associated extensions.
   */
  public static final String XMI_TEXT = "XMI models (*.xmi)";

  /**
   * Adds all file filters defined for the MDA application.
   * @param chooser file chooser to which the file filters must be added.
   */
  public static void addFileFilters(JFileChooser chooser) {
    Class[] types = { JFileChooser.class };
    Object[] params = { chooser };
    ch.bbv.utilities.ReflectUtilities.execute("ch.bbv.mda.ui.eclipse.EclipseUtilities", "addEclipseFileFilters", types, params);
    chooser.addChoosableFileFilter(new SimpleFileFilter(FileTypes.POSEIDON_EXT,
        FileTypes.POSEIDON_TEXT));
    chooser.addChoosableFileFilter(new SimpleFileFilter(
        FileTypes.SERIALIZE_EXT, FileTypes.SERIALIZE_TEXT));
    chooser.addChoosableFileFilter(new SimpleFileFilter(FileTypes.XMI_EXT,
        FileTypes.XMI_TEXT));
    chooser.addChoosableFileFilter(new SimpleFileFilter(FileTypes.XML_EXT,
        FileTypes.XML_TEXT));
    chooser.addChoosableFileFilter(new SimpleFileFilter(FileTypes.ARGOUML_EXT,
        FileTypes.ARGOUML_TEXT));

  }
}