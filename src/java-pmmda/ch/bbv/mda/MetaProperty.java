/* copyright 2000-2004 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence");
 * you may not use this file except in compliance with the License.
 * $Id$
 */
package ch.bbv.mda;

import java.util.Arrays;

/**
 * The property class is a conveniance class to simply access properties of a
 * meta property instance defined as an xml tree. The class uses services of the
 * JDOM package. A property has some central characteristics.
 * <ul>
 * <dt>mode</dt>
 * <dd>The mode specifies if the property can be read, written or both. The
 * mode determines which accessors will be available to access the property. The
 * mode is a reference code.
 * <dd>
 * <dt>visibility</dt>
 * <dd>The visibility of the property defines the visibility of the accesors
 * related to the property. The property data is always private. The visiblity
 * is a reference code.</dd>
 * <dt>class type kind</dt>
 * <dd>The type kind defines the kind of the type of the property. The property
 * can reference a primitive type, a Java class, a data object or a framework
 * class defined in the application or a reference code. The type kind is a
 * reference code.</dd>
 * </ul>
 * @author Marcel Baumann
 * @version $Revision$
 */
public class MetaProperty extends MetaMember {
  /** 
   * The list of primitive types in Java, C and C++.
   */
  public static final String[] PRIMITIVES_TYPES = { "boolean", "byte", "char",
      "double", "float", "int", "long", "short" };

  /**
   * Indicates if the property is transient or not.
   */
  protected boolean transients;

  /**
   * Indicates if the property is volatile or not.
   */
  protected boolean volatiles;

  /**
   * Multiplicity of the property.
   */
  protected Multiplicity multiplicity;

  /**
   * The optional initial value of the property.
   */
  protected String initialValue;
  
  /**
   * A binary association may be declared an aggregation. It is a whole part
   * relationship. In UML terminology the data object has the aggregate end and
   * the items of the property are on the part end.
   */
  protected boolean aggregation;

  /**
   * A composition is a form of aggregation association with strong ownership
   * and coincident lifetime of parts by the whole. A part may belong to only
   * one composite.
   */
  protected boolean composition;

  /**
   * Default constructor of the class.
   */
  public MetaProperty() {
    init();
    multiplicity = Multiplicity.MULTIPLICITY_0_1;
    transients = false;
    volatiles = false;
  }

  /**
   * Initializes member variables. Only complex types have to be initialized
   * here. The init() method is called after deserializing and loading from xml
   * with XStream. Transient members have to be initialized with this init()
   * function.
   */
  public void init() {
    super.init();
  }

  /**
   * Returns the multiplicity of the property.
   * @return multiplicity of the property.
   * @see #setMultiplicity
   */
  public Multiplicity getMultiplicity() {
    return multiplicity;
  }

  /**
   * Sets the multiplicity of the property.
   * @param multiplicity new multiplicity of the property.
   * @see #getMultiplicity
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
   * @see #setTransient
   */
  public boolean isTransient() {
    return transients;
  }

  /**
   * Sets the transient flag.
   * @param transients new value of the transient flag.
   * @see #isTransient
   */
  public void setTransient(boolean transients) {
    this.transients = transients;
  }

  /**
   * Returns true if the property is volatile otherwise false.
   * @return the value of the volatile flag.
   * @see #setVolatile
   */
  public boolean isVolatile() {
    return volatiles;
  }

  /**
   * Sets the volatile flag.
   * @param volatiles new value of the volatile flag.
   * @see #isVolatile
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

  /**
   * Returns true if the property is a reference code, meaning it implements the
   * reference code or hierarchical reference code interface.
   * @return true if the property is a reference code otherwise false.
   */
  public boolean retrieveIsReferenceCode() {
    return booleanValueOfTag("bo-reference-code");
  }

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

  /**
   * Returns true if the type of the property is a primitive build-in type.
   * @return true if the type is build-in otherwise false.
   */
  public boolean isTypePrimitive() {
    return Arrays.binarySearch(PRIMITIVES_TYPES, getTypeName()) >= 0;
  }

  public MetaClass retrieveReferenceClass() {
    MetaClass referenceClass = null;
    String reference = getTaggedValue("reference").getValue();
    if (reference != null) {
      referenceClass = findClass(reference);
    }
    return referenceClass;
  }

  /**
   * Indicates whether this model element is a leaf (no children)
   */
  public boolean isLeaf() {
    return true;
  }
}