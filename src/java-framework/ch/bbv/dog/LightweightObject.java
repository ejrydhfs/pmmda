/* copyright 2000-2004 Marcel Baumann 
 * Licensed under the Apache License, Version 2.0 (the "Licence); 
 * you may not use this file except in compliance with the License. 
 * $Id$ 
 */
package ch.bbv.dog;

import java.io.Serializable;

/**
 * The lightweight object interface defines the common signature of all
 * lightweight data objects.
 * @author MarcelBaumann
 * @version $Revision$
 */
public interface LightweightObject extends Serializable {
  /**
   * List of simple system properties which should not be handled through
   * introspection.
   */
  static final String[] SYSTEM_SIMPLE_PROPERTIES = { "class", "id" };

  /**
   * Returns the identifier of the object.
   * @return the identifier of the object. If the object is a new one, null is
   *         returned.
   */
  Integer getId();

  /**
   * Performs a deep copy of all referenced data objects and their properties.
   * @return the cloned data object.
   */
  LightweightObject copy();
}