/* copyright 2000-2004 Marcel Baumann 
 * Licensed under the Apache License, Version 2.0 (the "Licence); 
 * you may not use this file except in compliance with the License. 
 * $Id$
 */
package ch.bbv.mda;

import java.io.Serializable;

import ch.bbv.mda.generators.Cartridge;

/**
 * The tagged value type defines the type of a tagged value. The type specifies
 * the name of the tag, the default value and the type of the value.
 * @author marcelbaumann
 * @version $Revision$
 */
public class TaggedValueType extends AdornmentType implements Serializable{
  /**
   * Default value of the tag.
   */
  private String defaultValue;

  /**
   * Type of the values for this type.
   */
  private transient Class type;
  
  /**
   * Back link to the cartridge having defined this tagged value type
   */
  private Cartridge cartridge;

  /**
   * Constructor of the class.
   * @param tag tag name for the tagged values of this type.
   * @param defaultValue default value for the tagged values of this type.
   * @param type type of the values for the tagged values of this type.
   */
  public TaggedValueType(String tag, String defaultValue, Class type, Cartridge cartridge) {
    setName(tag);
    this.defaultValue = defaultValue;
    this.type = type;
    this.cartridge = cartridge;
  }

  /**
   * Returns the tag name of this type.
   * @return the tag name represented in this type.
   */
  public String getTag() {
    return getName();
  }

  /**
   * Returns the default value of this type.
   * @return the default value represented in this type.
   */
  public String getDefaultValue() {
    return defaultValue;
  }

  /**
   * Returns the value type of this type.
   * @return the type of values represented in this type.
   */
  public Class getType() {
    return type;
  }

  /**
   * Returns the qualified class name of the type if defined.
   * @return class name of the type if defined otherwise null.
   */
  public String getTypename() {
    return (type != null) ? type.getName() : null;
  }
 
  /**
   * Returns the cartridge having defined this tagged value type
   * @return Cartridge cartridge having defined this tagged value type
   */
  public Cartridge getCartridge(){
    return cartridge;
  }
}