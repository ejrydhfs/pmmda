/* This code was generated automatically with the pmMDA generator. Please do 
   NOT add code outside the marked zones. All changes outside the marked areas 
   will be discarded during the next execution of the MDA generator. 
   $Id$
*/

package ch.bbv.codes.lw;

import ch.bbv.dog.LightweightObject;

/* %% USER DEFINED IMPORT START %% */
/* %% USER DEFINED IMPORT END   %% */

/**
 * A value descriptor describes the values of a reference code instance.
 * @version $Revision$
 */
public class ValueDescriptorLw implements LightweightObject {

/* %% USER DEFINED DECLARATION START %% */
/* %% USER DEFINED DECLARATION END %% */

  /**
   * Serial version identifier coded as back compatibility date.
   */
  private static final long serialVersionUID = 99990101;

  /**
   * A value descriptor describes the values of a reference code instance.
   */
  private int code;

  /**
   * A value descriptor describes the values of a reference code instance.
   */
  private int order;

  /**
   * A value descriptor describes the values of a reference code instance.
   */
  private boolean active;

  /**
   * A value descriptor describes the values of a reference code instance.
   */
  private int parentCode;

  /**
   * A value descriptor describes the values of a reference code instance.
   */
  private String keyName;

  /** 
   * Unique identifier of the data object instance distinguishing it from 
   * all other instances of the same data object type.
   */
  private Integer id;

  /** 
   * Default constructor of the class used to create new lightweight data 
   * object instances.
   */
  public ValueDescriptorLw() {
    this(null);
  }
     
  /** 
   * Constructor of the class.
   * @param id identifier of the object to which the proxy is associated.
   */
  public ValueDescriptorLw(Integer id) {
    this.id = id;
  }

  /** 
   * Gets the current value of the attribute Code.
   * @return the value of the attribute Code.
   * @see #setCode
   */
  public int getCode() {
    return code;
  }

  /** 
   * Sets the new value of the simple property Code.
   * @param code new value of the property.
   * @see #getCode
   */    
  public void setCode(int code) {
    this.code = code;
  }

  /** 
   * Gets the current value of the attribute Order.
   * @return the value of the attribute Order.
   * @see #setOrder
   */
  public int getOrder() {
    return order;
  }

  /** 
   * Sets the new value of the simple property Order.
   * @param order new value of the property.
   * @see #getOrder
   */    
  public void setOrder(int order) {
    this.order = order;
  }

  /** 
   * Gets the current value of the attribute Active.
   * @return the value of the attribute Active.
   * @see #setActive
   */
  public boolean getActive() {
    return active;
  }

  /** 
   * Sets the new value of the simple property Active.
   * @param active new value of the property.
   * @see #getActive
   */    
  public void setActive(boolean active) {
    this.active = active;
  }

  /** 
   * Gets the current value of the attribute ParentCode.
   * @return the value of the attribute ParentCode.
   * @see #setParentCode
   */
  public int getParentCode() {
    return parentCode;
  }

  /** 
   * Sets the new value of the simple property ParentCode.
   * @param parentCode new value of the property.
   * @see #getParentCode
   */    
  public void setParentCode(int parentCode) {
    this.parentCode = parentCode;
  }

  /** 
   * Gets the current value of the attribute KeyName.
   * @return the value of the attribute KeyName.
   * @see #setKeyName
   */
  public String getKeyName() {
    return keyName;
  }

  /** 
   * Sets the new value of the simple property KeyName.
   * @param keyName new value of the property.
   * @see #getKeyName
   */    
  public void setKeyName(String keyName) {
    this.keyName = keyName;
  }

  /** 
   * Returns the unique identifier of the data object instance.
   * @return the identifier of the instance.
   */
  public Integer getId() {
  	return id;
  }

  /** 
   * Implements the visitor pattern in the data object class. The accept
   * method dispatches the visitor to all its relevant children. A relevant
   * child is always another data object.
   * @param visitor visitor to act on the data object.
   */  
  public void accept(LwVisitor visitor) {
  	visitor.setDepth(visitor.getDepth() + 1);
    visitor.visit(this);
    visitor.setDepth(visitor.getDepth() - 1);
  }

  /** 
   * Creates and returns a deep copy of the object and its descendants. The 
   * properties containing keys are not cloned because they are immutable.
   * @return a deep copy of the instance.
   */
  public LightweightObject deepCopy() {
    ValueDescriptorLw clone = null;
    try {
      clone = (ValueDescriptorLw)clone();
    } catch (CloneNotSupportedException e) {
      // impossible exception because data object is cloneable.
      return null;
    }
	return clone;
  }
}
