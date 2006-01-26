/* copyright 2000-2006 Marcel Baumann 
 * Licensed under the Apache License, Version 2.0 (the "Licence"); 
 * you may not use this file except in compliance with the License. 
 * $Id$ 
 */
package ch.bbv.mda;

/**
 * The class provides access to standard tags and stereotype defined in the UML
 * standard.
 * @author MarcelBaumann
 * @version $Revision$
 */
public final class UmlConstants {
  /**
   * The UML standard volatile tag.
   */
  public static final String VOLATILE_TAG = "volatile";

  /**
   * The old UML standard transient tag is no more defined in the new UML
   * standard. The value is now handled in the persistence tag.
   */
  public static final String TRANSIENT_TAG = "transient";

  /**
   * The UML standard persistence tag is defined on classifier, associations and
   * attribute, and denotes whether an instance value should outlive the process
   * that created it. Values are persistent and transient. If used on
   * attributes, allows a finer descrimination about which attribute values
   * should be preserved within a classifier.
   */
  public static final String PERSISTENCE_TAG = "persistence";

  /**
   * The UML standard documentation tag is defined on element and denotes a
   * comment, description, or explanation of the element to which it is
   * attached.
   */
  public static final String DOCUMENTATION_TAG = "documentation";

}
