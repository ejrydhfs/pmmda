/* copyright 2000-2005 Marcel Baumann 
 * Licensed under the Apache License, Version 2.0 (the "Licence"); 
 * you may not use this file except in compliance with the License. 
 * $Id$ 
 */
package ch.bbv.dog;


/**
 * The lightweight object interface defines the common signature of all
 * lightweight data objects.
 * @author MarcelBaumann
 * @version $Revision$
 */
public interface LightweightObject extends HasId {
  /**
   * List of simple system properties which should not be handled through
   * introspection.
   */
  static final String[] SYSTEM_SIMPLE_PROPERTIES = { "class", "id" };

  /**
   * Performs a deep copy of all referenced data objects and their properties.
   * The name is not copy to avoid collision with the copy of the data object
   * type for classes implementing both interfaces.
   * @return the cloned data object.
   */
  LightweightObject deepCopy();
}