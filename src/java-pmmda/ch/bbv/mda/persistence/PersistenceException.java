/* copyright 2000-2006 Marcel Baumann 
 * Licensed under the Apache License, Version 2.0 (the "Licence"); 
 * you may not use this file except in compliance with the License. 
 * $Id$ 
 */
package ch.bbv.mda.persistence;

/**
 * The exception class for all exceptions thrown when reading or writing a UML
 * model.
 * @author Marcel Baumann
 * @version $Revision$
 */
public class PersistenceException extends Exception {
  /**
   * Serial version identifier coded as back compatibility date.
   */
  private static final long serialVersionUID = 20041201;

  /**
   * Default constructor of the classs.
   */
  public PersistenceException() {
  }

  /**
   * Constructor of the class.
   * @param exception the exception cause of the persistence exception.
   */
  public PersistenceException(Exception exception) {
    super(exception);
  }

  /**
   * Constructor of the class.
   * @param message message of the exception.
   * @param exception the exception cause of the persistence exception.
   */
  public PersistenceException(String message, Exception exception) {
    super(message, exception);
  }

  /**
   * Constructor of the class.
   * @param message message of the exception.
   */
  public PersistenceException(String message) {
    super(message);
  }
}
