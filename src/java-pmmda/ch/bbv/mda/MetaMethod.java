/* copyright 2000-2004 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence");
 * you may not use this file except in compliance with the License.
 * $Id$
 */

package ch.bbv.mda;

import java.util.ArrayList;
import java.util.List;

/**
 * The method class represent a class method with its parameters, return value
 * and exception.
 * @author MarcelBaumann
 * @version $Revision$
 */
public class MetaMethod extends MetaElement {
  /**
   * Parameters of the method.
   */
  private List parameters;

  /**
   * Return value of the method.
   */
  private MetaReturn returns;

  /**
   * Exceptions thrown in the method.
   */
  private List exceptions;

  /**
   * Flag indicating if the method is abstract or not.
   */
  private boolean abstracts;

  /**
   * Default constructor of the class.
   */
  public MetaMethod() {
    init();
  }

  /**
   * Initializes member variables. Only complex types have to be initialized
   * here. The init() method is called after deserializing and loading from xml
   * with XStream. Transient members have to be initialized with this init()
   * function.
   */
  public void init() {
    super.init();
    if (parameters == null)
      parameters = new ArrayList();
    if (exceptions == null)
      exceptions = new ArrayList();
  }

  /**
   * Returns true if the method is abstract otherwise not.
   * @return value of the flag.
   * @see #setAbstract
   */
  public boolean isAbstract() {
    return abstracts;
  }

  /**
   * Sets the abstract flag.
   * @param abstracts new value of the abstract flag.
   * @see #isAbstract()
   */
  public void setAbstract(boolean abstracts) {
    this.abstracts = abstracts;
  }

  /**
   * Sets the return type of the method if defined otherwise null.
   * @param returns new return value.
   * @see #getReturn()
   */
  public void setReturn(MetaReturn returns) {
    this.returns = returns;
  }

  /**
   * Returns the return type of the method.
   * @return return type of the method.
   * @see #setReturn(MetaReturn)
   */
  public MetaReturn getReturn() {
    return returns;
  }

  /**
   * Adds a parameter to the parameter list.
   * @param parameter parameter to add.
   */
  public void addParameter(MetaParameter parameter) {
    parameters.add(parameter);
  }

  /**
   * Adds an exception to the exception list.
   * @param exception exception to add.
   */
  public void addException(MetaException exception) {
    exceptions.add(exception);
  }

  /**
   * Indicates whether this model element is a leaf (no children)
   */
  public boolean isLeaf() {
    return true;
  }
}