/* copyright 2000-2006 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence");
 * you may not use this file except in compliance with the License.
 * $Id$
 */
package ch.bbv.mda.persistence.mdr;

import org.omg.uml.foundation.core.TaggedValue;

public class MdrTaggedValue {

  /**
   * the MDR element
   */
  private TaggedValue taggedValue;

  /**
   * constructor
   * @param taggedValue a MDR tagged value
   */
  public MdrTaggedValue(TaggedValue taggedValue) {
    this.taggedValue = taggedValue;
  }

  /**
   * returns the tagged value
   * @return the mdr tagged value
   */
  public TaggedValue getTaggedValue() {
    return taggedValue;
  }
  
  /**
   * Returns the MDA TaggedValue
   * @return
   */
  public ch.bbv.mda.TaggedValue getMdaTaggedValue(){
    return new ch.bbv.mda.TaggedValue(getName(), getValue());
  }

  /**
   * returns the name of the tagged value
   * @return
   */
  public String getName() {
    if (taggedValue.getType() == null)
      return null;
    return taggedValue.getType().getName();
  }

  /**
   * sets the name of the tagged value
   * @param name the name of the tagged value
   */
  public void setName(String name) {
    taggedValue.getType().setName(name);
  }

  /**
   * returns the value of the tagged value
   * @return the value of thte tagged value
   */
  public String getValue() {
    return taggedValue.getDataValue().iterator().next().toString();
  }

  /**
   * sets the value of the tagged value
   * @param value
   */
  @SuppressWarnings("unchecked")
  public void setValue(String value) {
    taggedValue.getDataValue().add(value);
  }
}
