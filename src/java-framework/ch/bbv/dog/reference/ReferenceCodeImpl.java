/* copyright 2000-2006 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence");
 * you may not use this file except in compliance with the License.
 * $Id$
 */
package ch.bbv.dog.reference;

import java.util.Date;

/**
 * The canonical implementation of data reference code.
 * @author Marcel Baumann
 * @version $Revision$
 */
public class ReferenceCodeImpl implements ReferenceCode {
  /**
   * Serial version identifier coded as back compatibility date.
   */
  private static final long serialVersionUID = 20041201;

  /**
   * Code of the reference code instance.
   * @serial
   */
  private int code;

  /**
   * Sort order of the reference code instance in the set of related codes.
   * @serial
   */
  private int sortOrder;

  /**
   * Flag indicating if the code is active or not.
   * @serial
   */
  private boolean active;

  /**
   * Description of the reference code instance.
   * @serial
   */
  private String description;

  /**
   * Short description reference code instance.
   * @serial
   */
  private String shortDescription;

  /**
   * Start of the validity of the code instance.
   */
  private Date fromDate;

  /**
   * End of the validity of the code instance.
   */
  private Date toDate;

  /**
   * Default constructor necessary for Java bean convention.
   */
  public ReferenceCodeImpl() {
    // default constructor
  }

  /**
   * Constructor initializes the immutable instance with all property values.
   * @param code unique identifier for the code of this type.
   * @param sortOrder sort order of the code instance.
   * @param shortDescription short description of the code.
   * @param description description of the code.
   * @param active flag indicating if the code is active or not.
   */
  public ReferenceCodeImpl(int code, int sortOrder, String shortDescription,
      String description, boolean active) {
    this(code, sortOrder, shortDescription, description, active, null, null);
  }

  /**
   * Constructor initializes the immutable instance with all property values.
   * @param code unique identifier for the code of this type.
   * @param sortOrder sort order of the code instance.
   * @param shortDescription short description of the code.
   * @param description description of the code.
   * @param active flag indicating if the code is active or not.
   * @param fromDate the start of the interval when the code is active.
   * @param toDate the end of the interval when the code is active.
   */
  public ReferenceCodeImpl(int code, int sortOrder, String shortDescription,
      String description, boolean active, Date fromDate, Date toDate) {
    this.code = code;
    this.sortOrder = sortOrder;
    this.shortDescription = shortDescription;
    this.description = description;
    this.active = active;
    this.fromDate = fromDate;
    this.toDate = toDate;
  }

  public int getCode() {
    return code;
  }

  public int getSortOrder() {
    return sortOrder;
  }

  public boolean getActive() {
    return active;
  }

  public String getDescription() {
    return description;
  }

  public String getShortDescription() {
    return shortDescription;
  }

  public Date getFromDate() {
    return fromDate;
  }

  public Date getToDate() {
    return toDate;
  }

  public int compareTo(Object object) {
    if (object.getClass() == getClass()) {
      int compare;
      int codeValue = ((ReferenceCode) object).getCode();
      if (code > codeValue) {
        compare = 1;
      } else if (code < codeValue) {
        compare = -1;
      } else {
        compare = 0;
      }
      return compare;
    }
    throw new ClassCastException();
  }

  @Override
  public boolean equals(Object object) {
    if (object == null || !(object.getClass() == getClass())) {
      return false;
    }
    ReferenceCode referenceCode = (ReferenceCode) object;
    return (code == referenceCode.getCode());
  }

  @Override
  public int hashCode() {
    return getClass().hashCode() ^ code;
  }

  @Override
  public String toString() {
    return shortDescription;
  }
}