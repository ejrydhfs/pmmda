/* This code was generated automatically with the pmMDA generator. Please do 
   NOT add code outside the marked zones. All changes outside the marked areas 
   will be discarded during the next execution of the MDA generator. 
*/

package ch.bbv.unittests.props.lw;

import ch.bbv.dog.LightweightObject;

/* %% USER DEFINED IMPORT START %% */
/* %% USER DEFINED IMPORT END %% */

public class SimpleAttributesLw  implements LightweightObject {

/* %% USER DEFINED DECLARATION START %% */
/* %% USER DEFINED DECLARATION END %% */

  /**
   * Serial version identifier coded as back compatibility date.
   */
  private static final long serialVersionUID = 99990101;

  private boolean booleanAttribute;

  private int intAttribute;

  private float floatAttribute;

  private double doubleAttribute;

  private String stringAttribute;

  private long longAttribute;

  /** 
   * Unique identifier of the data object instance distinguishing it from 
   * all other instances of the same data object type.
   */
  private Integer id;

  /** 
   * Default constructor of the class used to create new lightweight data 
   * object instances.
   */
  public SimpleAttributesLw() {
    this(null);
  }
     
  /** 
   * Constructor of the class.
   * @param id identifier of the object to which the proxy is associated.
   */
  public SimpleAttributesLw(Integer id) {
    this.id = id;
  }

/* %% USER DEFINED CONSTRUCTORS START %% */
/* %% USER DEFINED CONSTRUCTORS END %% */

/* %% USER DEFINED CODE START %% */
/* %% USER DEFINED CODE END %% */


  /** 
   * Gets the current value of the attribute BooleanAttribute.
   * @return the value of the attribute BooleanAttribute.
   * @see #setBooleanAttribute(boolean)
   */
  public boolean isBooleanAttribute() {
    return booleanAttribute;
  }

  /** 
   * Sets the new value of the simple property BooleanAttribute.
   * @param booleanAttribute new value of the property.
   * @see #isBooleanAttribute()
   */    
  public void setBooleanAttribute(boolean booleanAttribute) {
      this.booleanAttribute = booleanAttribute;
  }

  /** 
   * Gets the current value of the attribute IntAttribute.
   * @return the value of the attribute IntAttribute.
   * @see #setIntAttribute(int)
   */
  public int getIntAttribute() {
    return intAttribute;
  }

  /** 
   * Sets the new value of the simple property IntAttribute.
   * @param intAttribute new value of the property.
   * @see #getIntAttribute()
   */    
  public void setIntAttribute(int intAttribute) {
      this.intAttribute = intAttribute;
  }

  /** 
   * Gets the current value of the attribute FloatAttribute.
   * @return the value of the attribute FloatAttribute.
   * @see #setFloatAttribute(float)
   */
  public float getFloatAttribute() {
    return floatAttribute;
  }

  /** 
   * Sets the new value of the simple property FloatAttribute.
   * @param floatAttribute new value of the property.
   * @see #getFloatAttribute()
   */    
  public void setFloatAttribute(float floatAttribute) {
      this.floatAttribute = floatAttribute;
  }

  /** 
   * Gets the current value of the attribute DoubleAttribute.
   * @return the value of the attribute DoubleAttribute.
   * @see #setDoubleAttribute(double)
   */
  public double getDoubleAttribute() {
    return doubleAttribute;
  }

  /** 
   * Sets the new value of the simple property DoubleAttribute.
   * @param doubleAttribute new value of the property.
   * @see #getDoubleAttribute()
   */    
  public void setDoubleAttribute(double doubleAttribute) {
      this.doubleAttribute = doubleAttribute;
  }

  /** 
   * Gets the current value of the attribute StringAttribute.
   * @return the value of the attribute StringAttribute.
   * @see #setStringAttribute(String)
   */
  public String getStringAttribute() {
    return stringAttribute;
  }

  /** 
   * Sets the new value of the simple property StringAttribute.
   * @param stringAttribute new value of the property.
   * @see #getStringAttribute()
   */    
  public void setStringAttribute(String stringAttribute) {
      this.stringAttribute = stringAttribute;
  }

  /** 
   * Gets the current value of the attribute LongAttribute.
   * @return the value of the attribute LongAttribute.
   * @see #setLongAttribute(long)
   */
  public long getLongAttribute() {
    return longAttribute;
  }

  /** 
   * Sets the new value of the simple property LongAttribute.
   * @param longAttribute new value of the property.
   * @see #getLongAttribute()
   */    
  public void setLongAttribute(long longAttribute) {
      this.longAttribute = longAttribute;
  }

  /** 
   * Returns the unique identifier of the data object instance.
   * @return the identifier of the instance.
   */
  public Integer getId() {
    return id;
  }

  /** 
   * Implements the visitor pattern in the lightweight data object class. The 
   * accept method dispatches the visitor to all its relevant children. A 
   * relevant child is always another data object.
   * @param visitor visitor to act on the data object.
   * @pre visitor != null
   */  
  public void accept(LwVisitor visitor) {
    assert(visitor != null);
    visitor.setDepth(visitor.getDepth() + 1);
    visitor.visit(this);
    traverse(visitor);
    visitor.setDepth(visitor.getDepth() - 1);
  }

  /** 
   * Creates and returns a deep copy of the object and its descendants. The 
   * properties containing keys are not cloned because they are immutable.
   * @return a deep copy of the instance.
   */
  public SimpleAttributesLw deepCopy() {
    SimpleAttributesLw clone = null;
    try {
      clone = (SimpleAttributesLw)clone();
    } catch (CloneNotSupportedException e) {
      // impossible exception because data object is cloneable.
      return null;
    }
    return clone;
  }

  /** 
   * Implements the visitor pattern in the ligthweight data object class. The 
   * accept method dispatches the visitor to all relevant children of the 
   * ancestors and its children. A relevant child is always another lightweigth 
   * data object.
   * @param visitor visitor to act on the data object.
   */  
  protected void traverse(LwVisitor visitor) {
  }  
}
