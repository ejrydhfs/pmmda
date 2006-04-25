/* 
 * copyright 2000-2006 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence");
 * you may not use this file except in compliance with the License.
 * $Id$
 */

package ch.bbv.mda;

/**
 * The stereotype models an UML stereotype definition.
 * @author MarcelBaumann
 * @version $Revision$
 */
public class Stereotype extends AdornmentType {
  /**
   * Constructor of the class.
   * @param name name of the stereotype type
   */
  public Stereotype(String name) {
    setName(name);
  }
  
  @Override
  public String toString() {
    return getName();
  }
}