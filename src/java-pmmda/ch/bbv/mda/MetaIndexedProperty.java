/* copyright 2000-2005 Marcel Baumann 
 * Licensed under the Apache License, Version 2.0 (the "Licence"); 
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
  /**
   * Serial version identifier coded as back compatibility date.
   */
  private static final long serialVersionUID = 20041201;

  /**
   * Default constructor of the class.
   */
  public MetaIndexedProperty() {
    setMultiplicity(Multiplicity.MULTIPLICITY_0_N);
  }

  public void accept(Visitor visitor) {
    visitor.visit(this);
  }

  public String getStorageSize() {
    return "N";
  }

  /**
   * Returns true if the lower bound if the multiplicity is zero.
   * @return true if elements are optional otherwise false.
   */
  public boolean canBeNull() {
    return getMultiplicity().getLowerBound() == 0;
  }
}