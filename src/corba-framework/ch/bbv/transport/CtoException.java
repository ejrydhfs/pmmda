/* copyright 2000-2005 Marcel Baumann 
 * Licensed under the Apache License, Version 2.0 (the "Licence"); 
 * you may not use this file except in compliance with the License. 
 * $Id$ 
 */
package ch.bbv.transport;

/**
 * The remote exception transfered over the CORBA middleware.
 * @author MarcelBaumann
 * @version $Revision$
 */
public class CtoException extends Exception {
  /**
   * Serial version identifier coded as back compatibility date.
   */
  private static final long serialVersionUID = 20041201;

  /**
   * The reason of the exception.
   */
  private String reason;
  
  /** 
   * Constructor of the class.
   * @param text text of the remote exception.
   */
  public CtoException(String reason, String text) {
    super(text);
    this.reason = reason;
  }
  
  /**
   * Returns the reason of the exception.
   * @return the reason of the exception.
   */
  public String getReason() {
    return reason;
  }
}
