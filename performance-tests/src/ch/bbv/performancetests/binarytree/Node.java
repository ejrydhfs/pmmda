/* This code was generated automatically with the pmMDA generator. Please do 
   NOT add code outside the marked zones. All changes outside the marked areas 
   will be discarded during the next execution of the MDA generator. 
*/

package ch.bbv.performancetests.binarytree;









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







public class Node implements DataObject {

/* %% USER DEFINED DECLARATION START %% */
/* %% USER DEFINED DECLARATION END %% */

  /**
   * Serial version identifier coded as back compatibility date.
   */
  private static final long serialVersionUID = 99990101;

  private int value;

  private Node right;
  /**
   * Mapping for 1: 1 relationship right as foreign key.
   */
  private Integer rightHandle;  

  private Node left;
  /**
   * Mapping for 1: 1 relationship left as foreign key.
   */
  private Integer leftHandle;  
  /**
   * Mapping for 1: N relationship trees as reverse foreign key.
   */
  private Integer forestTreesHandle;




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
  public Node() {
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
	  if(! (o instanceof Node)){
		  return false;
	  }
	  Node c = (Node)o;
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
   * Returns the current value of the simple property value.
   * @return the value of the simple property value.
   * @see #setValue(int)
   */
  public int getValue() {
    return value;
  }

  /** 
   * Sets the new value of the simple property value.
   * @param value new value of the property
   * @see #getValue()
   */    
  public void setValue(int value) {
    if (this.value != value) {  
      this.value = value;
      update.addUpdate(new PrimitiveFieldUpdate("value", value));
      setModified(true);
    }
  }

  /** 
   * Returns the current value of the simple property right.
   * @return the value of the simple property right.
   * @see #setRight(Node)
   */
  public Node getRight() {
    return right;
  }

  /** 
   * Sets the new value of the simple property right.
   * @param right new value of the property
   * @see #getRight()
   */    
  public void setRight(Node right) {
    if (((this.right != null) && !this.right.equals(right)) || 
        ((this.right == null) && (right != null)))  {    
      this.right = right;
      rightHandle = (right != null ? right.getId() : null);
      if (right == null) {
        update.addUpdate(new PrimitiveFieldUpdate("right", null));
      } else {
        update.addUpdate(new SingleFieldUpdate(right.getUpdate(), "right"));
      }
      setModified(true);
    }
  }    

  /** 
   * Returns the current value of the simple property left.
   * @return the value of the simple property left.
   * @see #setLeft(Node)
   */
  public Node getLeft() {
    return left;
  }

  /** 
   * Sets the new value of the simple property left.
   * @param left new value of the property
   * @see #getLeft()
   */    
  public void setLeft(Node left) {
    if (((this.left != null) && !this.left.equals(left)) || 
        ((this.left == null) && (left != null)))  {    
      this.left = left;
      leftHandle = (left != null ? left.getId() : null);
      if (left == null) {
        update.addUpdate(new PrimitiveFieldUpdate("left", null));
      } else {
        update.addUpdate(new SingleFieldUpdate(left.getUpdate(), "left"));
      }
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
  public Node copy() {
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
  public Node copy(Map<Object, Object> copies) {
    Node clone = (Node)copies.get(this);
    if (clone == null) {
      try {
	    clone = (Node)clone();
        copies.put(this, clone);
      } catch (CloneNotSupportedException e) {
	    // impossible exception because data object is cloneable.
      }
      if (right != null) {
        clone.right = right.copy(copies);
      }
      if (left != null) {
        clone.left = left.copy(copies);
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
    if ((right != null) && visitor.shouldVisit(right)) {
      right.accept(visitor);
    }
    if ((left != null) && visitor.shouldVisit(left)) {
      left.accept(visitor);
    }
  }  
  

/* %% USER DEFINED PROTECTED/PRIVATE CODE START %% */
/* %% USER DEFINED PROTECTED/PRIVATE CODE END %% */
}