/* copyright 2000-2005 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence");
 * you may not use this file except in compliance with the License.
 * $Id$
 */

package ch.bbv.mda;

/**
 * The parameter describes a parameter of a method or a constuctor.
 * @author MarcelBaumann
 * @version $Revision$
 */
public class MetaParameter extends MetaElement {
  /**
   * Serial version identifier coded as back compatibility date.
   */
  private static final long serialVersionUID = 20041201;

  /**
   * Type of the element.
   * @serial
   */
  private String type;

  /**
   * Sets the type of the parameter.
   * @return qualified name of the new type of the parameter.
   * @see #setType
   */
  public String getType() {
    return type;
  }

  /**
   * Returns the type of the parameter.
   * @param type qualified name of the type of the parameter.
   * @see #getType
   */
  public void setType(String type) {
    this.type = type;
  }
}