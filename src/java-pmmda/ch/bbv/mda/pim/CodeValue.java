/* This code was generated automatically with the pmMDA generator. Please do 
   NOT add code outside the marked zones. All changes outside the marked areas 
   will be discarded during the next execution of the MDA generator. 
   $Id$
*/

package ch.bbv.mda.pim;

import ch.bbv.dog.DataObject;
import java.beans.PropertyChangeListener;
import java.beans.VetoableChangeListener;
import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.util.Date;

/* %% USER DEFINED IMPORT START %% */
/* %% USER DEFINED IMPORT END %% */

/**
 * The code value defines one value of a reference code type.
 * @version $Revision$
 * @author Marcel Baumann
 */
public class CodeValue implements DataObject {

/* %% USER DEFINED DECLARATION START %% */
/* %% USER DEFINED DECLARATION END %% */

  /**
   * Serial version identifier coded as back compatibility date.
   */
  private static final long serialVersionUID = 99990101;

  /**
   * Name of the code value. The name is used to generate identifiers for the 
   * value or to define enumeration constant.
   */
  private String name;

  /**
   * The order of the value in the set of values for a code type. The sort 
   * order defines an ordering relation in the set of values.
   */
  private int sortOrder;

  /**
   * The short description of the code value. This description is often used in 
   * lists.
   */
  private String shortDescription;

  /**
   * The description of the code value.
   */
  private String description;

  /**
   * The identifier uniquely identifying this code values in the set of values 
   * of a code type.
   */
  private int codeId;

  /**
   * The start date of the validity of the code value. If null the code is 
   * valid from the beginning.
   */
  private Date start;

  /**
   * The end date of the validity of the code value. If null the value is no 
   * end date.
   */
  private Date end;

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
  public CodeValue() {
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
   * Returns the current value of the simple property name.
   * @return the value of the simple property name.
   * @see #setName(String)
   */
  public String getName() {
    return name;
  }

  /** 
   * Sets the new value of the simple property name.
   * @param name new value of the property.
   * @see #getName()
   */    
  public void setName(String name) {
    if (((this.name != null) && !this.name.equals(name)) || 
        ((this.name == null) && (name != null)))  {    
      this.name = name;
      setModified(true);
    }
  }    

  /** 
   * Returns the current value of the simple property sortOrder.
   * @return the value of the simple property sortOrder.
   * @see #setSortOrder(int)
   */
  public int getSortOrder() {
    return sortOrder;
  }

  /** 
   * Sets the new value of the simple property sortOrder.
   * @param sortOrder new value of the property.
   * @see #getSortOrder()
   */    
  public void setSortOrder(int sortOrder) {
    if (this.sortOrder != sortOrder) {  
      this.sortOrder = sortOrder;
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
   * @see #getShortDescription()
   */    
  public void setShortDescription(String shortDescription) {
    if (((this.shortDescription != null) && !this.shortDescription.equals(shortDescription)) || 
        ((this.shortDescription == null) && (shortDescription != null)))  {    
      this.shortDescription = shortDescription;
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
   * @see #getDescription()
   */    
  public void setDescription(String description) {
    if (((this.description != null) && !this.description.equals(description)) || 
        ((this.description == null) && (description != null)))  {    
      this.description = description;
      setModified(true);
    }
  }    

  /** 
   * Returns the current value of the simple property codeId.
   * @return the value of the simple property codeId.
   * @see #setCodeId(int)
   */
  public int getCodeId() {
    return codeId;
  }

  /** 
   * Sets the new value of the simple property codeId.
   * @param codeId new value of the property.
   * @see #getCodeId()
   */    
  public void setCodeId(int codeId) {
    if (this.codeId != codeId) {  
      this.codeId = codeId;
      setModified(true);
    }
  }

  /** 
   * Returns the current value of the simple property start.
   * @return the value of the simple property start.
   * @see #setStart(Date)
   */
  public Date getStart() {
    return start;
  }

  /** 
   * Sets the new value of the simple property start.
   * @param start new value of the property.
   * @see #getStart()
   */    
  public void setStart(Date start) {
    if (((this.start != null) && !this.start.equals(start)) || 
        ((this.start == null) && (start != null)))  {    
      this.start = start;
      setModified(true);
    }
  }    

  /** 
   * Returns the current value of the simple property end.
   * @return the value of the simple property end.
   * @see #setEnd(Date)
   */
  public Date getEnd() {
    return end;
  }

  /** 
   * Sets the new value of the simple property end.
   * @param end new value of the property.
   * @see #getEnd()
   */    
  public void setEnd(Date end) {
    if (((this.end != null) && !this.end.equals(end)) || 
        ((this.end == null) && (end != null)))  {    
      this.end = end;
      setModified(true);
    }
  }    

  /** 
   * Implements the visitor pattern in the data object class. The accept
   * method dispatches the visitor to all its relevant children. A relevant
   * child is always another data object.
   * @param visitor visitor to act on the data object.
   * @pre visitor != null
   */  
  public void accept(Visitor visitor) {
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
  public CodeValue copy() {
    CodeValue clone = null;
    try {
      clone = (CodeValue)clone();
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
  
  public void addPropertyChangeListener(String propertyName, 
      PropertyChangeListener listener) {
    throw new UnsupportedOperationException();
  }
  
  public void removePropertyChangeListener(PropertyChangeListener listener) {
    throw new UnsupportedOperationException();
  }
  
  public void removePropertyChangeListener(String propertyName, 
      PropertyChangeListener listener) {
    throw new UnsupportedOperationException();
  }
  
  public void addVetoableChangeListener(VetoableChangeListener listener) {
    throw new UnsupportedOperationException();
  }
  
  public void addVetoableChangeListener(String propertyName,
      VetoableChangeListener listener) {
    throw new UnsupportedOperationException();
  }
  
  public void removeVetoableChangeListener(VetoableChangeListener listener) {
    throw new UnsupportedOperationException();
  }
  
  public void removeVetoableChangeListener(String propertyName,
      VetoableChangeListener listener) {
    throw new UnsupportedOperationException();
  }

  /**
   * Convenience method to set the value of a foreign key.
   * @param bean object which property should be set.
   * @param property property to be set.
   * @param key key identifier to set.
   * @pre (bean != null) && (property != null) && (key != null)
   */
  final protected void setForeignKey(DataObject bean, String property,
      Integer key) {
    assert((bean != null) && (property != null));
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
  protected void copy(CodeValue clone) {
  }

  /** 
   * Implements the visitor pattern in the data object class. The accept
   * method dispatches the visitor to all relevant children of the ancestors
   * and its children. A relevant child is always another data object.
   * @param visitor visitor to act on the data object.
   */  
  protected void traverse(Visitor visitor) {
  }  

/* %% USER DEFINED PROTECTED/PRIVATE CODE START %% */
/* %% USER DEFINED PROTECTED/PRIVATE CODE END %% */
}
