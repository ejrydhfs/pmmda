/* copyright 2000-2004 Marcel Baumann 
 * Licensed under the Apache License, Version 2.0 (the "Licence); 
 * you may not use this file except in compliance with the License. 
 * $Id$
 */
package ch.bbv.mda;

import java.io.Serializable;

/**
 * The tagged value implements the UML concept of a tag. A tag is an extension
 * mechanism for UML models. It is composed of a name and a value. Additionally
 * A type definition can define the set of legal values.
 * @author Marcel Baumann
 * @version $Revision$
 */
public class TaggedValue implements Serializable {
  // %% BEGIN USER DECLARATION
  // %% END USER DECLARATION
  
  /** Name of the tag describing the tagged value.
   */
  private String tag;

  /** Value of the tagged value.
   */
  private String value;

  /** Type of the values of the tagged value.
   */
  private TaggedValueType type;

	/** Default constructor of the class.
	 */
  public TaggedValue() {
    // default constructor
  }

	/** Constructor of the class.
	 * @param value value of the tagged value.
	 * @param type type of the tagged value.
	 */
  public TaggedValue(String value, TaggedValueType type) {
    this.tag = type.getTag();
		this.type = type;
    this.value = value;
  }

  /** Constructor of the class.
   * @param tag tag name of the tagged value.
   * @param value value of the tagged value.
   */
	public TaggedValue(String tag, String value) {
		this.tag = tag;
		this.value = value;
	}

	/** Creates a new tagged value of the given tagged value type.
	 * @param type type of the tagged value to create.
	 */
	public TaggedValue(TaggedValueType type) {
		this(type.getDefaultValue(), type);
	}

	/** Copy constructor.
	 * @param taggedValue tagged value used as prototype.
	 */
  public TaggedValue(TaggedValue taggedValue) {
    this.tag = taggedValue.tag;
    this.value = taggedValue.value;
    this.type = taggedValue.type;
  }

	/** Returns the tag of the tagged value.
	 * @return the tag name of the tagged value.
	 */
  public String getTag() {
    return tag;
  }

	/** Returns the value of the tagged value.
	 * @return the value of the tagged value.
	 * @see #setValue
	 */
  public String getValue() {
    return value;
  }

	/** Sets the value of the tagged value.
	 * @param value new value of the tagged value.
	 * @see #getValue
	 */
  public void setValue(String value) {
    this.value = value;
  }

	/** Returns the type of the tagged value.
	 * @return the type of the tagged value.
	 * @see #setType
	 */
  public TaggedValueType getType() {
    return type;
  }

	/** Sets the type of the tagged value.
	 * @param type new type of the tagged value.
	 * @see #getType
	 */
  public void setType(TaggedValueType type) {
    this.type = type;
  }

  // %% BEGIN USER CODE 
  
  /** Returns the boolean value of the tagged value if the type is boolean and
   * the value defined.
   * @return the boolean value of the tag if defined otherwise false.
   */
  public boolean booleanValue() {
    return ((value != null) && value.equals("true")); 
  }

	/** Sets a boolean value to the tagged value.
	 * @param value new boolean value of the tag.
	 */
  public void booleanValue(boolean value) {
    this.value = Boolean.toString(value);
  }
  // %% END USER CODE 
}
