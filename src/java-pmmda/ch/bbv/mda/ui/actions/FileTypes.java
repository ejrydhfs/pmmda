/* copyright 2000-2004 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence");
 * you may not use this file except in compliance with the License.
 * $Id$
 */

package ch.bbv.mda.ui.actions;

/**
 * The interface defines all the file extensions and descriptive text for the
 * file types supported in the application.
 * @author MarcelBaumann
 * @version $Revision$
 */
public interface FileTypes {
  /**
   * File extensions for models developed with ArgoUML.
   */
  String[] ARGOUML_EXT = { "zargo" };

  /**
   * Text for the associated extensions.
   */
  String ARGOUML_TEXT = "ArgoUML, Poseidon 1.x (*.zargo)";

  /**
   * File extensions for models developed with ArgoUML.
   */
  String[] POSEIDON_EXT = { "zuml" };

  /**
   * Text for the associated extensions.
   */
  String POSEIDON_TEXT = "Poseidon 2.x (*.zuml)";

  /**
   * File extensions for models developed with Rational Rose.
   */
  String[] RATIONAL_ROSE_EXT = { "mdl" };

  /**
   * Text for the associated extensions.
   */
  String RATIONAL_ROSE_TEXT = "Rational Rose (*.mdl)";

  /**
   * File extensions for models serialized to XML using the open source library
   * from CodeHaus.
   */
  String[] XML_EXT = { "xml" };

  /**
   * Text for the associated extensions.
   */
  String XML_TEXT = "XML models (*.xml)";

  /**
   * File extensions for models serialized with the Java serialization features.
   */
  String[] SERIALIZE_EXT = { "ser" };

  /**
   * Text for the associated extensions.
   */
  String SERIALIZE_TEXT = "Java XML models (*.ser)";

  /**
   * File extensions for models according to xmi standard.
   */
  String[] XMI_EXT = { "xmi" };

  /**
   * Text for the associated extensions.
   */
  String XMI_TEXT = "XMI models (*.xmi)";
}