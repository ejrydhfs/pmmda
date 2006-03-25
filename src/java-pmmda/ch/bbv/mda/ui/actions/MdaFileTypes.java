/* copyright 2000-2006 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence");
 * you may not use this file except in compliance with the License.
 * $Id$
 */

package ch.bbv.mda.ui.actions;

import javax.swing.JFileChooser;

import ch.bbv.explorer.actions.FileTypes;
import ch.bbv.explorer.actions.SimpleFileFilter;
import ch.bbv.utilities.ReflectUtilities;

/**
 * The class defines all the file extensions and descriptive text for the file
 * types supported in the application.
 * @author MarcelBaumann
 * @version $Revision$
 */
public class MdaFileTypes extends FileTypes {
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
   * The singleton instance.
   */
  private static MdaFileTypes instance;

  static {
    instance = new MdaFileTypes();
  }

  /**
   * Returns the singleton instance.
   * @return the singleton instance
   */
  public static MdaFileTypes getInstance() {
    return instance;

  }

  /**
   * Private constructor for the singleton class.
   */
  private MdaFileTypes() {
    setDefaultExtension(ARGOUML_EXT[0]);
    add(new SimpleFileFilter(POSEIDON_EXT, POSEIDON_TEXT));
    add(new SimpleFileFilter(SERIALIZE_EXT, SERIALIZE_TEXT));
    add(new SimpleFileFilter(XMI_EXT, XMI_TEXT));
    add(new SimpleFileFilter(XML_EXT, XML_TEXT));
    add(new SimpleFileFilter(ARGOUML_EXT, ARGOUML_TEXT));
  }

  /**
   * Adds all file filters defined for the MDA application.
   * @param chooser file chooser to which the file filters must be added.
   */
  public void addFileFilters(JFileChooser chooser) {
    Class[] types = { JFileChooser.class };
    Object[] params = { chooser };
    if (ReflectUtilities.exists("ch.bbv.mda.ui.eclipse.EclipseUtilities")) {
      ReflectUtilities.execute("ch.bbv.mda.ui.eclipse.EclipseUtilities",
          "addEclipseFileFilters", types, params);
    }
    super.addFileFilters(chooser);
  }

  /**
   * Returns the first extension in the list of a filter description.
   * @param filterDescription to search for an extension.
   * @return extension (without '.' at the beginning) or null if none found.
   */
  public String getBestMatchingExtension(String filterDescription) {
    if (filterDescription == null)
      return null;
    if (filterDescription.equals(MdaFileTypes.POSEIDON_TEXT))
      return MdaFileTypes.POSEIDON_EXT[0];
    if (filterDescription.equals(MdaFileTypes.SERIALIZE_TEXT))
      return MdaFileTypes.SERIALIZE_EXT[0];
    if (filterDescription.equals(MdaFileTypes.XMI_TEXT))
      return MdaFileTypes.XMI_EXT[0];
    if (filterDescription.equals(MdaFileTypes.XML_TEXT))
      return MdaFileTypes.XML_EXT[0];
    if (filterDescription.equals(MdaFileTypes.ARGOUML_TEXT))
      return MdaFileTypes.ARGOUML_EXT[0];
    if (filterDescription.equals(MdaFileTypes.RATIONAL_ROSE_TEXT))
      return MdaFileTypes.RATIONAL_ROSE_EXT[0];
    if (filterDescription.equals(MdaFileTypes.RATIONAL_ROSE_CAT_TEXT))
      return MdaFileTypes.RATIONAL_ROSE_CAT_EXT[0];
    return null;
  }
}