/* copyright 2000-2004 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence);
 * you may not use this file except in compliance with the License.
 * $Id$
 */

package ch.bbv.dog;

/**
 * The data object graph exception is the exception thrown when the BOG
 * framework encounters a problem.
 * @author MarcelBaumann
 * @version $Revision$
 */
public class DogException extends Exception {
  /**
   * Error code for a database or O/R layer error.
   */
  public static final int DATABASE_ERROR = 100000;

  /**
   * Error code for a concurrent access to the same data.
   */
  public static final int CONCURRENT_ACCESS_ERROR = 100001;

  /**
   * Error code for a unknown data type.
   */
  public static final int DATA_TYPE_UNDEFINED_ERROR = 100002;

  /**
   * Error code for internal error indicating a programming error.
   */
  public static final int INTERNAL_ERROR = 200000;

  /**
   * Code describing the kind of exception occuring.
   */
  private int code;

  /**
   * The subsystem where the exception was thrown.
   */
  private String origin;

  /**
   * The text describing the exception reason in human readable form. The text
   * can also be a key to a localized bundle of messages.
   */
  private String text;

  /**
   * Paremeters of the text describing the exception.
   */
  private Object[] params;

  /**
   * Constructor of the class.
   * @param code code identifying the error.
   * @param origin subsystem origin of the error.
   * @param text text of the error.
   * @param cause cause of the exception.
   */
  public DogException(int code, String origin, String text, Throwable cause) {
    super(text, cause);
    this.code = code;
    this.origin = origin;
    this.text = text;
  }

  /**
   * Constructor of the class.
   * @param code code identifying the error.
   * @param origin subsystem origin of the error.
   * @param text text of the error.
   */
  public DogException(int code, String origin, String text) {
    super(text);
    this.code = code;
    this.origin = origin;
    this.text = text;
  }

  /**
   * Returns the parameters of the exception.
   * @return the parameters of the error text.
   * @see #setParams(Object[])
   */
  public Object[] getParams() {
    return params;
  }

  /**
   * Sets the parameters of the error text.
   * @param params parameters of the error text.
   * @see #getParams()
   */
  public void setParams(Object[] params) {
    this.params = params;
  }

  /**
   * Returns the error code of the exception.
   * @return error code of the exception.
   */
  public int getCode() {
    return code;
  }

  /**
   * Returns the origin of the exception. The origin is often the name of teh
   * subsystem where the error was detected.
   * @return origin of the exception.
   */
  public String getOrigin() {
    return origin;
  }

  /**
   * Returns the text of the exception.
   * @return text of the exception.
   */
  public String getText() {
    return text;
  }
}