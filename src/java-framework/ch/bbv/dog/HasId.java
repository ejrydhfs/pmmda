/* copyright 2000-2005 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence");
 * you may not use this file except in compliance with the License.
 * $Id$
 */

package ch.bbv.dog;

import java.io.Serializable;

/**
 * The interface defines the characteristic of any object possessing an
 * identifier.
 * @author MarcelBaumann
 * @version $Revision$
 */
public interface HasId extends Serializable, Cloneable {
  /**
   * Returns the identifier of the object.
   * @return the identifier of the object. If the object is a new one, null is
   *         returned.
   */
  Integer getId();
}