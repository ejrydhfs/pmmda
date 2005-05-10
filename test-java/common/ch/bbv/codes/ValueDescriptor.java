	
/* This code was generated automatically with the pmMDA generator. Please do 
   NOT add code outside the marked zones. All changes outside the marked areas 
   will be discarded during the next execution of the MDA generator. 
   $Id$
*/
   
package ch.bbv.codes;

import ch.bbv.dog.DataObject;
import java.beans.PropertyChangeListener;
import java.beans.VetoableChangeListener;
import java.lang.reflect.Field;
import java.sql.Timestamp;

/* %% USER DEFINED IMPORT START %% */
/* %% USER DEFINED IMPORT END %% */

/**
 * A value descriptor describes the values of a reference code instance.
 * @version $Revision$
 */
public class ValueDescriptor implements DataObject {

/* %% USER DEFINED DECLARATION START %% */
/* %% USER DEFINED DECLARATION END %% */

  /**
   * Serial version identifier coded as back compatibility date.
   */
  private static final long serialVersionUID = 99990101;

  private int code;

  private int order;

  private boolean active;

  private String description;

  private String shortDescription;

  private int parentCode;

  private String keyName;

  /** 
   * Unique identifier of the data object instance distinguishing it from all
   * other instances of the same data object type.
   */
  private Integer id;
   
  /** 
   * Flag indicated if the data object is dirty or not.
   */
  private boolean modified;
  
  /** 
   * Timestamp used to implement optimistic locking. 
   */
  private Timestamp timestamp;   

  /** 
   * Default constructor of the class necessary for Java bean convention. The 
   * object identifier is set to null to indicate it is a new object.
   */
  public ValueDescriptor() {
  }

/* %% USER DEFINED CONSTRUCTORS START %% */
/* %% USER DEFINED CONSTRUCTORS END %% */

/* %% USER DEFINED CODE START %% */
/* %% USER DEFINED CODE END %% */

  /** 
   * Returns the unique identifier of the data object instance.
   * @return the identifier of the instance.
   * @see #setId(Integer)
   */
  public Integer getId() {
  	return id;
  }
  
  /** 
   * Sets the identifier of the data object instance.
   * @param id new identifier of the instance.
   * @see #getId()
   */
  public void setId(Integer id) {
    this.id = id;
  }

  /** 
   * Returns the flag indicating if the data object instance was modified.
   * @return true if the object was modified otherwise false.
   * @see #setModified
   */
  public boolean isModified() {
    return modified;
  }
  
  /** 
   * Sets the flag indicating if the data object instance was modified.
   * @param modified new value of the flag.
   * @see #isModified
   */
  public void setModified(boolean modified) {
    this.modified = modified;
  }
  
  /** 
   * Convenience method indicating if the instance is a new one or not.
   * @return true if the object is new otherwise false.
   */
  public boolean isNew()  {
    return (getId() == null);
  }
  
  /** 
   * Returns the timestamp of the data object instance.
   * @return the timestamp of the last persistent modification.
   * @see #setTimestamp(Timestamp)
   */
  public Timestamp getTimestamp() {
    return timestamp;
  }
  
  /** 
   * Sets the timestamp of the data object instance.
   * @param timestamp timestamp of the instance.
   * @see #getTimestamp()
   */
   public void setTimestamp(Timestamp timestamp) {
     this.timestamp = timestamp;
   }
  /** 
   * Returns the current value of the simple property code.
   * @return the value of the simple property code.
   * @see #setCode(int)
   */
  public int getCode() {
    return code;
  }

  /** 
   * Sets the new value of the simple property code.
   * @param code new value of the property.
   * @see #getCode
   */    
  public void setCode(int code) {
    if (this.code != code) {  
      this.code = code;
      setModified(true);
    }
  }
  /** 
   * Returns the current value of the simple property order.
   * @return the value of the simple property order.
   * @see #setOrder(int)
   */
  public int getOrder() {
    return order;
  }

  /** 
   * Sets the new value of the simple property order.
   * @param order new value of the property.
   * @see #getOrder
   */    
  public void setOrder(int order) {
    if (this.order != order) {  
      this.order = order;
      setModified(true);
    }
  }
  /** 
   * Returns the current value of the simple property active.
   * @return the value of the simple property active.
   * @see #setActive(boolean)
   */
  public boolean isActive() {
    return active;
  }

  /** 
   * Sets the new value of the simple property active.
   * @param active new value of the property.
   * @see #isActive
   */    
  public void setActive(boolean active) {
    if (this.active != active) {  
      this.active = active;
      setModified(true);
    }
  }
  /** 
   * Returns the current value of the simple property description.
   * @return the value of the simple property description.
   * @see #setDescription(String)
   */
  public String getDescription() {
    return description;
  }

  /** 
   * Sets the new value of the simple property description.
   * @param description new value of the property.
   * @see #getDescription
   */    
  public void setDescription(String description) {
    if (((this.description != null) && !this.description.equals(description)) || 
   	    ((this.description == null) && (description != null)))  {    
	  this.description = description;
      setModified(true);
    }
  }    
  /** 
   * Returns the current value of the simple property shortDescription.
   * @return the value of the simple property shortDescription.
   * @see #setShortDescription(String)
   */
  public String getShortDescription() {
    return shortDescription;
  }

  /** 
   * Sets the new value of the simple property shortDescription.
   * @param shortDescription new value of the property.
   * @see #getShortDescription
   */    
  public void setShortDescription(String shortDescription) {
    if (((this.shortDescription != null) && !this.shortDescription.equals(shortDescription)) || 
   	    ((this.shortDescription == null) && (shortDescription != null)))  {    
	  this.shortDescription = shortDescription;
      setModified(true);
    }
  }    
  /** 
   * Returns the current value of the simple property parentCode.
   * @return the value of the simple property parentCode.
   * @see #setParentCode(int)
   */
  public int getParentCode() {
    return parentCode;
  }

  /** 
   * Sets the new value of the simple property parentCode.
   * @param parentCode new value of the property.
   * @see #getParentCode
   */    
  public void setParentCode(int parentCode) {
    if (this.parentCode != parentCode) {  
      this.parentCode = parentCode;
      setModified(true);
    }
  }
  /** 
   * Returns the current value of the simple property keyName.
   * @return the value of the simple property keyName.
   * @see #setKeyName(String)
   */
  public String getKeyName() {
    return keyName;
  }

  /** 
   * Sets the new value of the simple property keyName.
   * @param keyName new value of the property.
   * @see #getKeyName
   */    
  public void setKeyName(String keyName) {
    if (((this.keyName != null) && !this.keyName.equals(keyName)) || 
   	    ((this.keyName == null) && (keyName != null)))  {    
	  this.keyName = keyName;
      setModified(true);
    }
  }    

  /** 
   * Implements the visitor pattern in the data object class. The accept
   * method dispatches the visitor to all its relevant children. A relevant
   * child is always another data object.
   * @param visitor visitor to act on the data object.
   */  
  public void accept(Visitor visitor) {
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
  public DataObject copy() {
    ValueDescriptor clone = null;
    try {
      clone = (ValueDescriptor)clone();
    } catch (CloneNotSupportedException e) {
      // impossible exception because data object is cloneable.
      return null;
    }
    copy(clone);
    clone.setModified(isModified());
    return clone;
  }

  public void addPropertyChangeListener(PropertyChangeListener listener) {
    throw new UnsupportedOperationException();
  }
  
  public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener) {
    throw new UnsupportedOperationException();
  }
  
  public void removePropertyChangeListener(PropertyChangeListener listener) {
    throw new UnsupportedOperationException();
  }
  
  public void removePropertyChangeListener(String propertyName, PropertyChangeListener listener) {
    throw new UnsupportedOperationException();
  }
  
  public void addVetoableChangeListener(VetoableChangeListener listener) {
    throw new UnsupportedOperationException();
  }
  
  public void addVetoableChangeListener(String propertyName, VetoableChangeListener listener) {
    throw new UnsupportedOperationException();
  }
  
  public void removeVetoableChangeListener(VetoableChangeListener listener) {
    throw new UnsupportedOperationException();
  }
  
  public void removeVetoableChangeListener(String propertyName, VetoableChangeListener listener) {
    throw new UnsupportedOperationException();
  }

  /**
   * Convenience method to set the value of a foreign key.
   * @param bean object which property should be set.
   * @param property property to be set.
   * @param key key identifier to set.
   */
  protected void setForeignKey(DataObject bean, String property, Integer key) {
    try {
      Field field = bean.getClass().getDeclaredField(property);
      if (field != null) {
        field.setAccessible(true);
        field.set(bean, key);
      }
    } catch (NoSuchFieldException e) {
      assert(false);
    } catch (IllegalArgumentException e) {
      assert(false);
    } catch (IllegalAccessException e) {
      assert(false);
    }
  }

  /** 
   * Copies deeply all the attributes of the class and its ancestors.
   * @param clone the copy which deep copy should be generated.
   */
  protected void copy(ValueDescriptor clone) {
  }

  /** 
   * Implements the visitor pattern in the data object class. The accept
   * method dispatches the visitor to all relevant children of the ancestors
   * and its children. A relevant child is always another data object.
   * @param visitor visitor to act on the data object.
   */  
  protected void traverse(Visitor visitor) {
  }  
}
