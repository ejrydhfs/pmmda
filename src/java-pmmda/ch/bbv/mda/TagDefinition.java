/* 
 * copyright 2000-2005 Marcel Baumann 
 * Licensed under the Apache License, Version 2.0 (the "Licence"); 
 * you may not use this file except in compliance with the License. 
 * $Id$
 */
package ch.bbv.mda;

import java.io.Serializable;

/**
 * The tag definition specifies the tagged values that can be attached to a kind
 * of model element. Among other things, tag definitions can be sued to define
 * the virtual meta attributes of the stereotype to which they are attached. The
 * tag definition has the following attributes.
 * <dl>
 * <dt>multiplicty</dt>
 * <dd>Specifies the number of data values that tagged values based on this tag
 * must have, or the number of model elements that can be associated to the
 * related tagged values.</dd>
 * <dt>tag type</dt>
 * <dd>In the general case, where the tag type is a data type, this specifies
 * the range of values of the tagged values associated with the tag definition.
 * In the special case, where the tag type refers to a metaclass that is not a
 * datatype, the tag value references model elements that are instances of the
 * metaclass.</dd>
 * </dl>
 * @author marcelbaumann
 * @version $Revision$
 */
public class TagDefinition extends AdornmentType implements Serializable {
  /**
   * Serial version identifier coded as back compatibility date.
   */
  private static final long serialVersionUID = 20041201;

  /**
   * Default value of the tag.
   */
  private String defaultValue;

  /**
   * Type of the values for this type.
   */
  private transient Class type;

  /**
   * Constructor of the class.
   * @param tag tag name for the tagged values of this type
   * @param defaultValue default value for the tagged values of this type
   * @param type type of the values for the tagged values of this type
   * @pre (tag != null) && (type != null)
   */
  public TagDefinition(String tag, String defaultValue, Class type) {
    assert (tag != null) && (type != null);
    setName(tag);
    this.defaultValue = defaultValue;
    this.type = type;
  }

  /**
   * Constructor of the class.
   * @param tag tag name for the tagged values of this type
   * @param type type of the values for the tagged values of this type
   * @pre (tag != null) && (type != null)
   */
  public TagDefinition(String tag, Class type) {
    this(tag, null, type);
  }
  
  /**
   * Returns the tag name of this type.
   * @return the tag name represented in this type
   */
  public String getTag() {
    return getName();
  }

  /**
   * Returns the default value of this type.
   * @return the default value represented in this type
   */
  public String getDefaultValue() {
    return defaultValue;
  }

  /**
   * Returns the value type of this type.
   * @return the type of values represented in this type
   */
  public Class getType() {
    return type;
  }

  /**
   * Returns the qualified class name of the type if defined.
   * @return class name of the type if defined otherwise null
   */
  public String getTypename() {
    return (type != null) ? type.getName() : null;
  }
}