/* 
 * copyright 2000-2006 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence");
 * you may not use this file except in compliance with the License.
 * $Id$
 */
package ch.bbv.application;

/**
 * The version identifier class defines the concept of versions for various
 * items. A version has four fields.
 * <dl>
 * <dt>Major version identifier</dt>
 * <dd>identifies the main version of the item</dd>
 * <dt>Minor version identifier</dt>
 * <dd>identifies the minor version of the item within a major version.</dd>
 * <dt>Patch version identifier</dt>
 * <dd>identifies the patch level of the item within a minor version.</dd>
 * <dt>Postfix</dt>
 * <dd>A optional text defining additional information</dd>
 * </dl>
 * Two versions of an item are fully compatible if their major and minor
 * versions are the same. These two items are at least partially compatible if
 * their major versions are the same. Othewise the two items are incompatible.
 * Patch versions are used to differenciate versions where only cosmetic changes
 * or error corrections was applied.
 * @author Marcel Baumann
 * @version $Revision$
 */
public class VersionId implements Comparable {
  /**
   * Illegal version identifier. This value can be used to initialize database
   * identifier variables.
   */
  public static final int ILLEGALVERSIONID = -1;

  /**
   * Major version identifier of the version.
   */
  private int majorVersionId;

  /**
   * Minor version identifier of the version.
   */
  private int minorVersionId;

  /**
   * Patch version identifier of the version.
   */
  private int patchVersionId;

  /**
   * Postfix information of the version. The postfix is not used for
   * compatibility tests.
   */
  private String postfix;

  /**
   * Default constructor of the class. All values are set to illegal version
   * identifiers.
   */
  public VersionId() {
    majorVersionId = ILLEGALVERSIONID;
    minorVersionId = ILLEGALVERSIONID;
    patchVersionId = ILLEGALVERSIONID;
  }

  /**
   * Constructor of the class. All variables are initialized.
   * @param majorVersionId major version identifier
   * @param minorVersionId minor version identifier
   * @param patchVersionId patch version identifier
   * @param postfix postfix information of the version, for example release
   *          candidate information
   */
  public VersionId(int majorVersionId, int minorVersionId, int patchVersionId,
      String postfix) {
    this.majorVersionId = majorVersionId;
    this.minorVersionId = minorVersionId;
    this.patchVersionId = patchVersionId;
    this.postfix = postfix;
  }

  /**
   * Constructor of the class. All variables are initialized.
   * @param majorVersionId major version identifier
   * @param minorVersionId minor version identifier
   * @param patchVersionId patch version identifier
   */
  public VersionId(int majorVersionId, int minorVersionId, int patchVersionId) {
    this(majorVersionId, minorVersionId, patchVersionId, null);
  }

  /**
   * Sets the new value of the requested field.
   * @param majorVersionId new value of the major version identifier
   * @see #getMajorVersionId()
   */
  public void setMajorVersionId(int majorVersionId) {
    this.majorVersionId = majorVersionId;
  }

  /**
   * Sets the new value of the requested field.
   * @param minorVersionId new value of the minor version identifier
   * @see #getMinorVersionId()
   */
  public void setMinorVersionId(int minorVersionId) {
    this.minorVersionId = minorVersionId;
  }

  /**
   * Sets the new value of the requested field.
   * @param patchVersionId new value of the patch version identifier
   * @see #getPatchVersionId()
   */
  public void setPatchVersionId(int patchVersionId) {
    this.patchVersionId = patchVersionId;
  }

  /**
   * Sets the new value of the requested field.
   * @param postfix new value of the postfix
   * @see #getPostfix()
   */
  public void setPostfix(String postfix) {
    this.postfix = postfix;
  }

  /**
   * Returns the value of the requested field.
   * @return requested field identifier value
   * @see #setMajorVersionId(int)
   */
  public int getMajorVersionId() {
    return majorVersionId;
  }

  /**
   * Returns the value of the requested field.
   * @return requested field identifier value
   * @see #setMinorVersionId(int)
   */
  public int getMinorVersionId() {
    return minorVersionId;
  }

  /**
   * Returns the value of the requested field.
   * @return requested field identifier value
   * @see #setPatchVersionId(int)
   */
  public int getPatchVersionId() {
    return patchVersionId;
  }

  /**
   * Returns the value of the requested field.
   * @return requested field identifier value
   * @see #setPostfix(String)
   */
  public String getPostfix() {
    return postfix;
  }

  /**
   * Returns true if the version identifier is illegal otherwise false.
   * @return result of the inquiry
   */
  public boolean isIllegal() {
    return (majorVersionId == ILLEGALVERSIONID)
        || (minorVersionId == ILLEGALVERSIONID)
        || (patchVersionId == ILLEGALVERSIONID);
  }

  /**
   * Returns true if both version identifiers are compatible otherwise false.
   * @param id version identifier to check
   * @return result of the inquiry
   */
  public boolean isCompatible(VersionId id) {
    return (majorVersionId == id.getMajorVersionId())
        && (minorVersionId == id.getMinorVersionId());
  }

  /**
   * Returns true if both version identifiers are equals otherwise false. If the
   * object is not a version identifier returns false.
   * @param object object to check
   * @return result of the inquiry
   */
  @Override
  public boolean equals(Object object) {
    boolean isEquals = false;
    if (object instanceof VersionId) {
      VersionId id = (VersionId) object;
      isEquals = (majorVersionId == id.getMajorVersionId())
          && (minorVersionId == id.getMinorVersionId())
          && (patchVersionId == id.getPatchVersionId());
    }
    return isEquals;
  }

  @Override
  public int hashCode() {
    int hashCode = majorVersionId ^ minorVersionId ^ patchVersionId;
    if (postfix != null) {
      hashCode ^= postfix.hashCode();
    }
    return hashCode;
  }

  /**
   * Compare two version identifiers.
   * @param right version identifier to compare with
   * @return <0 if smaller, 0 if equal, > 0 if higher
   * @pre (right != null) && (right.instanceof VersionId)
   * @see Comparable
   */
  public int compareTo(Object right) {
    assert (right != null) && (right instanceof VersionId);
    VersionId id = (VersionId) right;
    int result = compareTo(getMajorVersionId(), id.getMajorVersionId());
    if (result == 0) {
      result = compareTo(getMajorVersionId(), id.getMinorVersionId());
    }
    if (result == 0) {
      result = compareTo(getPatchVersionId(), id.getPatchVersionId());
    }
    return result;
  }

  /**
   * Returns the version identifier as string.
   * @return The version identifier as major.minor.patch[-postfix] value
   */
  @Override
  public String toString() {
    StringBuilder buffer = new StringBuilder();
    buffer.append(majorVersionId).append(".").append(minorVersionId)
        .append(".").append(patchVersionId);
    if (postfix != null) {
      buffer.append("-").append(postfix);
    }

    return buffer.toString();
  }

  private int compareTo(int left, int right) {
    int result;
    if (left < right) {
      result = -1;
    } else if (left > right) {
      result = 1;
    } else {
      result = 0;
    }
    return result;
  }
}