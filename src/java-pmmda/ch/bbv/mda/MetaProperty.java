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
   * Indicates whether this MetaProperty is a pure property or a reference to a classifier.
   * True indicates a relation, false indicates a member.
   */
  private boolean isRelation;
  
  /**
   * True indicates that one can navigate from the property towards my context (MetaClass).
   */
  private boolean isNavigableToContext;
  
  /**
   * True indicates that one can navigate towards the property.
   */
  private boolean isNavigableToClassifier;
  

  @Override
  public MetaProperty copy(){
	  MetaProperty clone = new MetaProperty();
	  clone.aggregation = this.aggregation;
	  clone.setClassifier(this.getClassifier());
	  clone.composition = this.composition;
	  clone.setContext(this.getContext());
	  clone.setDescription(this.getDescription());
	  clone.setFinal(this.isFinal());
	  clone.initialValue = new String(this.initialValue == null ? "" : this.initialValue);
	  clone.multiplicity = new Multiplicity(this.getMultiplicity().getLowerBound(), this.getMultiplicity().getUpperBound());
	  clone.setName(this.getName());
	  clone.setStereotypes(this.getStereotypes());
	  clone.setTags(this.getTags());
	  clone.transients = this.transients;
	  clone.setVisibility(this.getVisibility());
	  clone.volatiles = this.volatiles;
	  clone.setStatic(this.isStatic());
	  clone.setSystem(this.isSystem());
	  clone.setIsRelation(this.isRelation);
	  clone.setIsNavigableToClassifier(this.isNavigableToClassifier);
	  clone.setIsNavigableToContext(this.isNavigableToContext);
	  return clone;
  }

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
   * Returns true if this MetaProperty is referenced by a relation, false if it is a member.
   * @return true if relation, false if a real property.
   */
  public boolean isRelation() {
  	return isRelation;
  }
  
  /**
   * True if it is a true relation or reference, false if it is a true member.
   * @param isRelation set to true if relation, otherwise false.
   */
  public void setIsRelation(boolean isRelation) {
  	this.isRelation = isRelation;
  }
  
  /**
   * True if one can navigate from related property to my context. In case of a true member this has to be false.
   * @return true if navigable to my context, false if not navigable or if true member.
   */
  public boolean isNavigableToContext() {
  	return isNavigableToContext;
  }
  
  /**
   * True if one can navigate from property to the containing context (class). False if not or if this property is not part of a relation.
   * @param isNavigableToContext true if navigable.
   */
  public void setIsNavigableToContext(boolean isNavigableToContext) {
  	this.isNavigableToContext = isNavigableToContext;
  }
  
  /**
   * True if one can navigate from the containing context (class) to the property (classifier). False if not or if this property is not part of a relation.
   * @return true if navigable.
   */
  public boolean isNavigableToClassifier() {
  	return isNavigableToClassifier;
  }

  /**
   * True if one can navigate from the containing context (class) to the property (classifier). False if not or if this property is not part of a relation.
   * @param isNavigableToClassifier true if navigable.
   */
  public void setIsNavigableToClassifier(boolean isNavigableToClassifier) {
  	this.isNavigableToClassifier = isNavigableToClassifier;
  }
  
  /**
   * Implements the visitor pattern.
   * @param visitor visitor to process
   * @pre visitor != null
   */
  public void accept(Visitor visitor) {
    visitor.visit(this);
  }

  /* %% USER DEFINED CODE START %% */
  /**
   * Returns the value of the tagged value.
   * @return value of the requested tagged value
   */
  public boolean retrieveIsChangeable() {
    return booleanValueOfTag("bo-change-event");
  }

  /**
   * Returns the value of the tagged value.
   * @return value of the requested tagged value
   */
  public boolean retrieveIsVetoable() {
    return booleanValueOfTag("bo-veto-event");
  }

  /**
   * Returns the value of the tagged value.
   * @return value of the requested tagged value
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