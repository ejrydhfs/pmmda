/* This code was generated automatically with the pmMDA generator. Please do 
   NOT add code outside the marked zones. All changes outside the marked areas 
   will be discarded during the next execution of the MDA generator. 
*/

package ch.bbv.unittests.props;









import ch.bbv.dog.DataObject;
import ch.bbv.dog.updates.*;
import java.beans.PropertyChangeListener;
import java.beans.VetoableChangeListener;
import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

/* %% USER DEFINED IMPORT START %% */
/* %% USER DEFINED IMPORT END %% */







public class SimpleAttributes implements DataObject {

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
   * Used to keep track of all changes made to the data object.
   */
  protected transient DataObjectUpdate update;   

  /** 
   * Default constructor of the class necessary for Java bean convention. The 
   * object identifier is set to null to indicate it is a new object.
   */
  public SimpleAttributes() {
    update = new DataObjectUpdate(this);


  }

/* %% USER DEFINED CONSTRUCTORS START %% */
/* %% USER DEFINED CONSTRUCTORS END %% */

/* %% USER DEFINED CODE START %% */
/* %% USER DEFINED CODE END %% */

  @Override
  public boolean equals(Object o){
	  if(o == this){
		  return true;
	  }
	  if(! (o instanceof SimpleAttributes)){
		  return false;
	  }
	  SimpleAttributes c = (SimpleAttributes)o;
	  if ((id == null) || (c.id == null)) {
		  return false;
	  }
	  return c.id.intValue() == this.id.intValue();
  }
  
  @Override
  public int hashCode(){
  	if(id == null) {
  		return super.hashCode();
  	}
	  return id.hashCode();
  }
  
  /** 
   * Returns the unique identifier of the data object instance. A new object has
   * the value null.
   * @return the identifier of the instance.
   * @see #setId(Integer)
   */
  public final Integer getId() {
    return id;
  }
  
  /** 
   * Sets the identifier of the data object instance.
   * @param id new identifier of the instance
   * @see #getId()
   */
  public final void setId(Integer id) {
    this.id = id;
  }

  /** 
   * Returns the flag indicating if the data object instance was modified.
   * @return true if the object was modified otherwise false.
   * @see #setModified
   */
  public final boolean isModified() {
    return modified;
  }
  
  /** 
   * Sets the flag indicating if the data object instance was modified.
   * @param modified new value of the flag
   * @see #isModified
   */
  public final void setModified(boolean modified) {
    this.modified = modified;
  }
  
  /** 
   * Convenience method indicating if the instance is a new one or not.
   * @return true if the object is new otherwise false.
   * @see #getId()
   */
  public final boolean isNew()  {
    return (getId() == null);
  }
  
  /** 
   * Returns the timestamp of the data object instance.
   * @return the timestamp of the last persistent modification.
   * @see #setTimestamp(Timestamp)
   */
  public final Timestamp getTimestamp() {
    return timestamp;
  }
  
  /** 
   * Sets the timestamp of the data object instance.
   * @param timestamp timestamp of the instance
   * @see #getTimestamp()
   */
   public final void setTimestamp(Timestamp timestamp) {
     this.timestamp = timestamp;
   }
   
  /**
   * Returns the update object that stores the modifications made on
   * this data object since it was created or last loaded. This property is
   * used within the pmMDA framework to reduce network traffic. Developers
   * not modifying or inheriting from this class can ignore this property.
   * <p>
   * <b>Note to implementors:</b>
   * <p>
   * Changes to user-defined persistent properties must be registered via the 
   * {@link DataObjectUpdate.addUpdate} method. Single properties are modified
   * if their value has changed. Indexed property are modified if items were
   * added, inserted or removed.
   */
   public final DataObjectUpdate getUpdate() {
     return update;
   }

   /** 
    * Resets the update object. That is, a new update object is created for 
    * this data object.
    */
   public final void resetUpdate() {
     update = new DataObjectUpdate(this);
   }

  /** 
   * Returns the current value of the simple property booleanAttribute.
   * @return the value of the simple property booleanAttribute.
   * @see #setBooleanAttribute(boolean)
   */
  public boolean isBooleanAttribute() {
    return booleanAttribute;
  }

  /** 
   * Sets the new value of the simple property booleanAttribute.
   * @param booleanAttribute new value of the property
   * @see #isBooleanAttribute()
   */    
  public void setBooleanAttribute(boolean booleanAttribute) {
    if (this.booleanAttribute != booleanAttribute) {  
      this.booleanAttribute = booleanAttribute;
      update.addUpdate(new PrimitiveFieldUpdate("booleanAttribute", booleanAttribute));
      setModified(true);
    }
  }

  /** 
   * Returns the current value of the simple property intAttribute.
   * @return the value of the simple property intAttribute.
   * @see #setIntAttribute(int)
   */
  public int getIntAttribute() {
    return intAttribute;
  }

  /** 
   * Sets the new value of the simple property intAttribute.
   * @param intAttribute new value of the property
   * @see #getIntAttribute()
   */    
  public void setIntAttribute(int intAttribute) {
    if (this.intAttribute != intAttribute) {  
      this.intAttribute = intAttribute;
      update.addUpdate(new PrimitiveFieldUpdate("intAttribute", intAttribute));
      setModified(true);
    }
  }

  /** 
   * Returns the current value of the simple property floatAttribute.
   * @return the value of the simple property floatAttribute.
   * @see #setFloatAttribute(float)
   */
  public float getFloatAttribute() {
    return floatAttribute;
  }

  /** 
   * Sets the new value of the simple property floatAttribute.
   * @param floatAttribute new value of the property
   * @see #getFloatAttribute()
   */    
  public void setFloatAttribute(float floatAttribute) {
    if (this.floatAttribute != floatAttribute) {  
      this.floatAttribute = floatAttribute;
      update.addUpdate(new PrimitiveFieldUpdate("floatAttribute", floatAttribute));
      setModified(true);
    }
  }

  /** 
   * Returns the current value of the simple property doubleAttribute.
   * @return the value of the simple property doubleAttribute.
   * @see #setDoubleAttribute(double)
   */
  public double getDoubleAttribute() {
    return doubleAttribute;
  }

  /** 
   * Sets the new value of the simple property doubleAttribute.
   * @param doubleAttribute new value of the property
   * @see #getDoubleAttribute()
   */    
  public void setDoubleAttribute(double doubleAttribute) {
    if (this.doubleAttribute != doubleAttribute) {  
      this.doubleAttribute = doubleAttribute;
      update.addUpdate(new PrimitiveFieldUpdate("doubleAttribute", doubleAttribute));
      setModified(true);
    }
  }

  /** 
   * Returns the current value of the simple property stringAttribute.
   * @return the value of the simple property stringAttribute.
   * @see #setStringAttribute(String)
   */
  public String getStringAttribute() {
    return stringAttribute;
  }

  /** 
   * Sets the new value of the simple property stringAttribute.
   * @param stringAttribute new value of the property
   * @see #getStringAttribute()
   */    
  public void setStringAttribute(String stringAttribute) {
    if (((this.stringAttribute != null) && !this.stringAttribute.equals(stringAttribute)) || 
        ((this.stringAttribute == null) && (stringAttribute != null)))  {    
      this.stringAttribute = stringAttribute;
      update.addUpdate(new PrimitiveFieldUpdate("stringAttribute", stringAttribute));
      setModified(true);
    }
  }    

  /** 
   * Returns the current value of the simple property longAttribute.
   * @return the value of the simple property longAttribute.
   * @see #setLongAttribute(long)
   */
  public long getLongAttribute() {
    return longAttribute;
  }

  /** 
   * Sets the new value of the simple property longAttribute.
   * @param longAttribute new value of the property
   * @see #getLongAttribute()
   */    
  public void setLongAttribute(long longAttribute) {
    if (this.longAttribute != longAttribute) {  
      this.longAttribute = longAttribute;
      update.addUpdate(new PrimitiveFieldUpdate("longAttribute", longAttribute));
      setModified(true);
    }
  }

  /** 
   * Implements the visitor pattern in the data object class. The accept
   * method dispatches the visitor to all its relevant children. A relevant
   * child is always another data object.
   * @param visitor visitor to act on the data object
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
   * @return a deep copy of the instance
   */
  public SimpleAttributes copy() {
    Map<Object, Object> copies = new HashMap<Object, Object>();
    return copy(copies);
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
   * @param bean object which property should be set
   * @param property property to be set
   * @param key key identifier to set
   * @pre (bean != null) && (property != null) && (key != null)
   */
   protected final void setForeignKey(DataObject bean, String property,
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
   * Copies deeply all the attributes of the class and its ancestors. Multiple 
   * references to the same data object are handled correctly.
   * @param copies set of copied objects used to detect circular references
   * @return the deep copy of the object
   */
  public SimpleAttributes copy(Map<Object, Object> copies) {
    SimpleAttributes clone = (SimpleAttributes)copies.get(this);
    if (clone == null) {
      try {
	    clone = (SimpleAttributes)clone();
        copies.put(this, clone);
      } catch (CloneNotSupportedException e) {
	    // impossible exception because data object is cloneable.
      }
    }
    return clone;
  }

  /** 
   * Implements the visitor pattern in the data object class. The accept
   * method dispatches the visitor to all relevant children of the ancestors
   * and its children. A relevant child is always another data object.
   * @param visitor visitor to act on the data object
   */  
  protected void traverse(Visitor visitor) {
  }  
  
	
  
/* %% USER DEFINED PROTECTED/PRIVATE CODE START %% */
/* %% USER DEFINED PROTECTED/PRIVATE CODE END %% */
}