/* copyright 2000-2005 Marcel Baumann 
 * Licensed under the Apache License, Version 2.0 (the "Licence"); 
 * you may not use this file except in compliance with the License. 
 * $Id$ 
 */
package ch.bbv.mda.persistency;

/**
 * The exception class for all exceptions thrown when reading or writing a UML
 * model.
 * @author marcelbaumann
 * @version $Revision$
 */
public class PersistencyException extends Exception {
  /**
   * Serial version identifier coded as back compatibility date.
   */
  private static final long serialVersionUID = 20041201;

  public PersistencyException() {
  }

  public PersistencyException(Exception exception) {
    super(exception);
  }
}
