/* copyright 2000-2004 Marcel Baumann 
 * Licensed under the Apache License, Version 2.0 (the "Licence); 
 * you may not use this file except in compliance with the License.
 * $Id$ 
 */
package ch.bbv.cmdlineparser;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * <p>
 * The class describes a legal option supported on the command line. This
 * description contains all relevant attributes of a command option. A command
 * option has a mandatory option identifier and an optional value. Multiple
 * values for an option are supported. It is possible to define the set of legal
 * values an option can have.
 * </p>
 * @author Marcel Baumann
 * @version $Revision$
 */
public class CmdLineOptionDescr {
  /**
   * Type constant for the value of the option description.
   */
  public static final int TYPE_NONE = 1;

  /**
   * Type constant for the value of the option description.
   */
  public static final int TYPE_ENUM = 2;

  /**
   * Type constant for the value of the option description.
   */
  public static final int TYPE_BOOLEAN = 3;

  /**
   * Type constant for the value of the option description.
   */
  public static final int TYPE_INT = 4;

  /**
   * Type constant for the value of the option description.
   */
  public static final int TYPE_DOUBLE = 5;

  /**
   * Type constant for the value of the option description.
   */
  public static final int TYPE_CHAR = 6;

  /**
   * Type constant for the value of the option description.
   */
  public static final int TYPE_STRING = 7;

  /**
   * Type constant for the value of the option description.
   */
  public static final int TYPE_PATH = 8;

  /**
   * Name of the command used to identify the command when parsing the command
   * line.
   */
  private String cmdId;

  /**
   * Defines the minimum length of the command ID to uniquely identify it on the
   * command line.
   */
  private int mimimumLgth;

  /**
   * Defines if the command ID is case sensitive or not.
   */
  private boolean isCaseSensitive;

  /**
   * Defines if the command has a value or if the command itself is the unique
   * information needed on the command line.
   */
  private boolean hasValues;

  /**
   * Defines if the option can have multiple values or not.
   */
  private boolean areMultipleValuesAllowed;

  /**
   * Defines if the option is mandatory or not.
   */
  private boolean isMandatory;

  /**
   * Defines if the options follows the UNIX conventions or not.
   */
  private boolean isUnixLike;

  /**
   * type of the values defined for the option description.
   */
  private int type;

  /**
   * If not null contains the list of valid values this command option.
   */
  private List cmdValueList;

  /**
   * Contains the default value. A default value exists if the field is not
   * null.
   */
  private String defaultValue;

  /**
   * Constructs a new object and sets all members to the passed values.
   * @param cmdId command identifier as a string.
   * @param minimumLgth minimum of significant characters of the command ID to
   *        uniquely identify it.
   * @param isCaseSensitive indicates if the option identifier is case sensitive
   *        or not.
   * @param isMandatory indicates if the option is mandatory or not.
   * @param hasValues indicates if the option has values or not.
   * @param areMultipleValuesAllowed true if multiple values of the option are
   *        allowed.
   * @pre cmdId != null
   */
  public CmdLineOptionDescr(String cmdId, int minimumLgth,
      boolean isCaseSensitive, boolean isMandatory, boolean hasValues,
      boolean areMultipleValuesAllowed) {
    assert cmdId != null : "pre";
    setCmdId(cmdId);
    setMinimumLength(minimumLgth);
    setCaseSensitive(isCaseSensitive);
    setMandatory(isMandatory);
    setHasValues(hasValues);
    setAreMultipleValuesAllowed(areMultipleValuesAllowed);
    isUnixLike = false;
  }

  /**
   * Constructs a new object and sets all members to the passed values.
   * @param cmdId command identifier as a string.
   * @param minimumLgth minimum of significant characters of the command ID to
   *        uniquely identify it.
   * @param isCaseSensitive indicates if the option identifier is case sensitive
   *        or not.
   * @param isMandatory indicates if the option is mandatory or not.
   * @param hasValues indicates if the option has values or not.
   * @param areMultipleValuesAllowed true if multiple values of the option are
   *        allowed.
   * @param cmdValueList list of all values supported by this option.
   */
  public CmdLineOptionDescr(String cmdId, int minimumLgth,
      boolean isCaseSensitive, boolean isMandatory, boolean hasValues,
      boolean areMultipleValuesAllowed, Iterator cmdValueList) {
    this(cmdId, minimumLgth, isCaseSensitive, isMandatory, hasValues,
        areMultipleValuesAllowed);
    while (cmdValueList.hasNext()) {
      addValue((String) cmdValueList.next());
    }
  }

  /**
   * Constructs a new object and sets all members to the passed values.
   * @param cmdId command identifier as a string.
   * @param minimumLgth minimum of significant characters of the command ID to
   *        uniquely identify it.
   * @param isCaseSensitive indicates if the option identifier is case sensitive
   *        or not.
   * @param isMandatory indicates if the option is mandatory or not.
   * @param hasValues indicates if the option has values or not.
   * @param areMultipleValuesAllowed true if multiple values of the option are
   *        allowed.
   * @param isUnixLike indicates if the option follows the UNIX tools
   *        conventions or not.
   * @param cmdValueList list of all values supported by this option.
   */
  public CmdLineOptionDescr(String cmdId, int minimumLgth,
      boolean isCaseSensitive, boolean isMandatory, boolean hasValues,
      boolean areMultipleValuesAllowed, boolean isUnixLike,
      Iterator cmdValueList) {
    this(cmdId, minimumLgth, isCaseSensitive, isMandatory, hasValues,
        areMultipleValuesAllowed, cmdValueList);
    setUnixLike(isUnixLike);
  }

  /**
   * Sets the command identifier of the option.
   * @param cmdId new command identifier for the command line option.
   * @pre cmdId != null
   * @post getCmdId().equals(cmdId)
   * @see #getCmdId()
   */
  public void setCmdId(String cmdId) {
    assert cmdId != null : "pre";
    this.cmdId = cmdId;
    assert getCmdId().equals(cmdId) : "post";
  }

  /**
   * Returns the command identifier string.
   * @return Identifier of the command as string.
   * @see #setCmdId(String)
   */
  public String getCmdId() {
    return cmdId;
  }

  /**
   * Returns the minimum length fo the command identifier string of the option.
   * @return Minimum length of the command identifier string of the option.
   * @see #setMinimumLength(int)
   */
  public int getMinimumLength() {
    return mimimumLgth;
  }

  /**
   * Sets the minimum length necessary to uniquely identify the command
   * identifier of the option.
   * @param minimumLgth minimum length of the command identifier.
   * @pre minimumLgth > 0
   * @see #getMinimumLength()
   */
  public void setMinimumLength(int minimumLgth) {
    assert minimumLgth > 0 : "pre";
    mimimumLgth = minimumLgth;
  }

  /**
   * Returns the type of the value of the option description.
   * @return type type of the option description.
   * @see #setType(int)
   */
  public int getType() {
    return type;
  }

  /**
   * Sets the type of the value of the option description.
   * @param type type of the option description.
   * @see #getType()
   */
  public void setType(int type) {
    this.type = type;
  }

  /**
   * Sets the case sensitive flag.
   * @param isCaseSensitive new value of the flag.
   * @see #isCaseSensitive()
   */
  public void setCaseSensitive(boolean isCaseSensitive) {
    this.isCaseSensitive = isCaseSensitive;
  }

  /**
   * Returns the case sensitiveness flag of the description.
   * @return True if the option is case sensitive, otherwise false.
   * @see #setCaseSensitive(boolean)
   */
  public boolean isCaseSensitive() {
    return isCaseSensitive;
  }

  /**
   * Sets the UNIX like option.
   * @param isUnixLike new value of the flag.
   * @see #isUnixLike()
   */
  public void setUnixLike(boolean isUnixLike) {
    this.isUnixLike = isUnixLike;
  }

  /**
   * Returns the is the option UNIX like flag of the description.
   * @return True if teh option is UNIX like otherwise false.
   * @see #setUnixLike(boolean)
   */
  public boolean isUnixLike() {
    return isUnixLike;
  }

  /**
   * Sets the is mandatory flag.
   * @param isMandatory new value of the flag.
   * @pre !isMandatory implies !hasDefaultValue()
   * @post isMandatory() == isMandatory
   * @see #isMandatory()
   */
  public void setMandatory(boolean isMandatory) {
    assert (!isMandatory() ? !hasDefaultValue() : true) : "pre";
    this.isMandatory = isMandatory;
    assert isMandatory() == isMandatory : "post";
  }

  /**
   * Returns the mandatory flag of the description.
   * @return True if the option is mandatory otherwise false.
   * @see #setMandatory(boolean)
   */
  public boolean isMandatory() {
    return isMandatory;
  }

  /**
   * Sets the has values flag.
   * @param hasValues new value of the flag.
   * @post hasValues() == hasValues
   * @see #hasValues()
   */
  public void setHasValues(boolean hasValues) {
    this.hasValues = hasValues;
    assert hasValues() == hasValues : "post";
  }

  /**
   * Returns the has values flag of the description.
   * @return True if the option has values otherwise false.
   * @see #setHasValues(boolean)
   */
  public boolean hasValues() {
    return hasValues;
  }

  /**
   * Sets the are multiple values allowed flag.
   * @param areMultipleValuesAllowed new value of the flag.
   * @post areMultipleValuesAllowed() == areMultipleValuesAllowed
   * @see #areMultipleValuesAllowed()
   */
  public void setAreMultipleValuesAllowed(boolean areMultipleValuesAllowed) {
    this.areMultipleValuesAllowed = areMultipleValuesAllowed;
    assert areMultipleValuesAllowed() == areMultipleValuesAllowed : "post";
  }

  /**
   * Returns the multiple values allowed flag.
   * @return True if multiple values are allowed, otherwise false.
   * @see #setAreMultipleValuesAllowed(boolean)
   */
  public boolean areMultipleValuesAllowed() {
    return areMultipleValuesAllowed;
  }

  /**
   * Returns option default value flag value. The option has a value if the
   * field is not null.
   * @return Return true if the option has a default value otherwise false.
   * @see #setDefaultValue(String)
   */
  public boolean hasDefaultValue() {
    return (defaultValue != null);
  }

  /**
   * Sets the default value of the option. A null value clears the default
   * value.
   * @param defaultValue new default value.
   * @pre isMandatory()
   * @post getDefaultValue() == defaultValue
   * @see #getDefaultValue()
   */
  public void setDefaultValue(String defaultValue) {
    assert isMandatory() : "pre";
    this.defaultValue = defaultValue;
    assert getDefaultValue() == defaultValue : "post";
  }

  /**
   * Returns the default value of the option.
   * @return The default value of the option or null is none is specified.
   * @see #setDefaultValue(String)
   */
  public String getDefaultValue() {
    return defaultValue;
  }

  /**
   * Checks if the given string is a legal identifier of the option. The given
   * string must be a substring starting at position 0 of the option ID and with
   * a length of at least the minimum number of required characters.
   * @param optionId string to be checked.
   * @return True if the string identifies the option ID, false otherwise.
   * @pre optionId != null
   */
  public boolean isOptionId(String optionId) {
    assert optionId != null : "pre";
    boolean isOptionId = false;
    int matchLgth = optionId.length();
    if (matchLgth >= mimimumLgth) {
      isOptionId = cmdId.regionMatches(!isCaseSensitive, 0, optionId, 0,
          matchLgth);
    }
    return isOptionId;
  }

  /**
   * Checks if the given value is a legal one. A value is legal either if no
   * list is defined or if the value is member of the list. The case sensitive
   * field is used to select the correct comparaison operation.
   * @param value string to be checked.
   * @return True if the value is a legal one, false otherwise
   * @pre value != null
   */
public boolean isLegalOptionValue(String value) {
    assert value != null : "pre";
    if (cmdValueList == null) {
      boolean found = false;
      for (Iterator i = cmdValueList.iterator(); i.hasNext() && !found;) {
        found = ((String) i.next()).regionMatches(!isCaseSensitive, 0, value,
            0, value.length());
      }
      return found;
    }
    return true;
  }
  /**
   * Adds a possible option value to the list. The command value list is
   * initialized using lazy evaluation.
   * @param value new value to be added in the list of existing values.
   * @pre value != null
   */
  public void addValue(String value) {
    assert value != null : "pre";
    if (cmdValueList == null) {
      cmdValueList = new ArrayList();
    }
    cmdValueList.add(value);
  }

}