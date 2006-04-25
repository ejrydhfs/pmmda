/* copyright 2000-2006 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence");
 * you may not use this file except in compliance with the License.
 * $Id$
 */
package ch.bbv.mda;

/**
 * The property class models a simple property of a class.
 * @author Marcel Baumann
 * @version $Revision$
 */
public class MetaProperty extends MetaMember {
  /**
   * Serial version identifier coded as back compatibility date.
   */
  private static final long serialVersionUID = 20041201;

  /**
   * Indicates if the property is transient or not.
   * @serial
   */
  private boolean transients;

  /**
   * Indicates if the property is volatile or not.
   * @serial
   */
  private boolean volatiles;

  /**
   * The optional initial value of the property.
   * @serial
   */
  private String initialValue;

  /**
   * Multiplicity of the property.
   * @serial
   */
  private Multiplicity multiplicity;

  /**
   * A binary association may be declared an aggregation. It is a whole part
   * relationship. In UML terminology the data object has the aggregate end and
   * the items of the property are on the part end.
   * @serial
   */
  private boolean aggregation;

  /**
   * A composition is a form of aggregation association with strong ownership
   * and coincident lifetime of parts by the whole. A part may belong to only
   * one composite.
   * @serial
   */
  private boolean composition;

  /**
   * Default constructor of the class.
   */
  public MetaProperty() {
    multiplicity = Multiplicity.MULTIPLICITY_0_1;
  }

  /**
   * Returns the multiplicity of the property.
   * @return multiplicity of the property.
   * @see #setMultiplicity(Multiplicity)
   */
  public Multiplicity getMultiplicity() {
    return multiplicity;
  }

  /**
   * Sets the multiplicity of the property.
   * @param multiplicity new multiplicity of the property.
   * @see #getMultiplicity()
   */
  public void setMultiplicity(Multiplicity multiplicity) {
    this.multiplicity = multiplicity;
  }

  /**
   * Returns the initial value of the property.
   * @return initial value of the property if defined otherwise null.
   * @see #setInitialValue(String)
   */
  public String getInitialValue() {
    return initialValue;
  }

  /**
   * Sets the initial value of the property.
   * @param initialValue new initial value.
   * @see #getInitialValue()
   */
  public void setInitialValue(String initialValue) {
    this.initialValue = initialValue;
  }

  /**
   * Returns true if the property is transient otherwise false.
   * @return the value of the transient flag.
   * @see #setTransient(boolean)
   */
  public boolean isTransient() {
    return transients;
  }

  /**
   * Sets the transient flag.
   * @param transients new value of the transient flag.
   * @see #isTransient()
   */
  public void setTransient(boolean transients) {
    this.transients = transients;
  }

  /**
   * Returns true if the property is volatile otherwise false.
   * @return the value of the volatile flag.
   * @see #setVolatile(boolean)
   */
  public boolean isVolatile() {
    return volatiles;
  }

  /**
   * Sets the volatile flag.
   * @param volatiles new value of the volatile flag.
   * @see #isVolatile()
   */
  public void setVolatile(boolean volatiles) {
    this.volatiles = volatiles;
  }

  /**
   * Returns true if the property is an aggregation otherwise false.
   * @return the value of the aggregation flag.
   * @see #setAggregation
   */
  public boolean isAggregation() {
    return aggregation;
  }

  /**
   * Sets the aggregation flag.
   * @param aggregation new value of the aggregation flag.
   * @see #isAggregation()
   */
  public void setAggregation(boolean aggregation) {
    this.aggregation = aggregation;
  }

  /**
   * Returns true if the property is a composition otherwise false.
   * @return the value of the composition flag.
   * @see #setComposition(boolean)
   */
  public boolean isComposition() {
    return composition;
  }

  /**
   * Sets the composition flag.
   * @param composition new value of the composition flag.
   * @see #isComposition()
   */
  public void setComposition(boolean composition) {
    this.composition = composition;
  }

  /**
   * Implements the visitor pattern.
   * @param visitor visitor to process.
   * @pre visitor != null
   */
  public void accept(Visitor visitor) {
    visitor.visit(this);
  }

  /* %% USER DEFINED CODE START %% */
  /**
   * Returns the value of the tagged value.
   * @return value of the requested tagged value.
   */
  public boolean retrieveIsChangeable() {
    return booleanValueOfTag("bo-change-event");
  }

  /**
   * Returns the value of the tagged value.
   * @return value of the requested tagged value.
   */
  public boolean retrieveIsVetoable() {
    return booleanValueOfTag("bo-veto-event");
  }

  /**
   * Returns the value of the tagged value.
   * @return value of the requested tagged value.
   */
  public boolean canBeRead() {
    String mode = getTaggedValue("bo-mode").getValue();
    return (mode.equals("READ_WRITE") || mode.equals("READ"));
  }

  /**
   * Returns the value of the tagged value.
   * @return value of the requested tagged value.
   */
  public boolean canBeWritten() {
    String mode = getTaggedValue("bo-mode").getValue();
    return (mode.equals("READ_WRITE") || mode.equals("WRITE"));
  }
  /* %% USER DEFINED CODE END %% */
}