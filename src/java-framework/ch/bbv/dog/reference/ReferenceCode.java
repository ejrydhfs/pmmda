/* copyright 2000-2004 Marcel Baumann 
 * Licensed under the Apache License, Version 2.0 (the "Licence"); 
 * you may not use this file except in compliance with the License. 
 * $Id$
 */
package ch.bbv.dog.reference;

import java.io.Serializable;
import java.util.Date;

/**
 * The reference code defines the interface for all regular key code of the
 * application. All codes of the application should implement this interface.
 * @author Marcel Baumann
 * @version $Revision$
 */
public interface ReferenceCode extends Serializable, Comparable {
  /**
   * Returns the code value of the reference code. The code is the unique
   * identifier of a reference code in the set of codes belonging to the same
   * type.
   * @return code value of the code.
   */
  public int getCode();

  /**
   * Returns the sort order of the reference code.
   * @return sort order of the reference code.
   */
  public int getSortOrder();

  /**
   * Returns true if the reference code is active otherwise false.
   * @return flag indicating if the reference code is active.
   */
  public boolean getActive();

  /**
   * Returns the description of the reference code.
   * @return description of the reference code.
   */
  public String getDescription();

  /**
   * Returns the short description of the reference code.
   * @return description of the reference code.
   */
  public String getShortDescription();

  /**
   * Returns the date from when the code was active.
   * @return the date from when the code was active or null.
   */
  public Date getFromDate();

  /**
   * Returns the date until when the code was active.
   * @return the date until when the code was active or null.
   */
  public Date getToDate();
}