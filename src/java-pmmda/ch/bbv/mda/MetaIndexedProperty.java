/* copyright 2000-2004 Marcel Baumann 
 * Licensed under the Apache License, Version 2.0 (the "Licence); 
 * you may not use this file except in compliance with the License. 
 * $Id$
 */
package ch.bbv.mda;

/**
 * The indexed property implements the conception of indexed property defined in
 * the Java bean standard. It maps the concept of aggregation or composition as
 * defined in UML to the mechanisms provided in regular programming languages.
 * @author Marcel Baumann
 * @version $Revision$
 */
public class MetaIndexedProperty extends MetaProperty {
  public static final String DEFAULT_META_INDEX_TYPE = "ARRAY_LIST";

  public MetaIndexedProperty(){
    super();
    multiplicity = Multiplicity.MULTIPLICITY_0_N;
  }
  
  public boolean isOrdered() {
    return booleanValueOfTag("bo-is-ordered");
  }

  public void setOrdered(boolean ordered) {
    setTaggedValue("bo-container-class", ordered);
  }

  public void accept(Visitor visitor) {
    visitor.visit(this);
  }

  public String getStorageType() {
    return getTaggedValue("bo-container-class").getValue();
  }

  public String getStorageSize() {
    return "N";
  }

  public String getStorageInterface() {
    return getTaggedValue("bo-interface-class").getValue();
  }

  public String getKeyType() {
    return getTaggedValue("bo-key-class").getValue();
  }

  public boolean canBeNull() {
    return getMultiplicity().getLowerBound() == 0;
  }

  /**
   * Indicates whether this model element is a leaf (no children)
   */
  public boolean isLeaf() {
    return true;
  }
}