/* This code was generated automatically with the pmMDA generator. Please do 
   NOT add code outside the marked zones. All changes outside the marked areas 
   will be discarded during the next execution of the MDA generator. 
*/

package ch.bbv.performancetests.list;









import ch.bbv.dog.DataObject;
import ch.bbv.dog.updates.*;
import java.beans.PropertyChangeListener;
import java.beans.VetoableChangeListener;
import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.util.*;

/* %% USER DEFINED IMPORT START %% */
/* %% USER DEFINED IMPORT END %% */







public class Master implements DataObject {

/* %% USER DEFINED DECLARATION START %% */
/* %% USER DEFINED DECLARATION END %% */

  /**
   * Serial version identifier coded as back compatibility date.
   */
  private static final long serialVersionUID = 99990101;

  private List<Slave> has;




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
  public Master() {
    update = new DataObjectUpdate(this);
    has = new ArrayList<Slave>();


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
	  if(! (o instanceof Master)){
		  return false;
	  }
	  Master c = (Master)o;
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
   * Gets all elements of the relation has.
   * @return List of all children.
   */
  public List<Slave> getHas() {
    return has;
  }

  /** 
   * Returns the element at the specified position in the indexed property 
   * has.
   * @param index index of element to return
   * @return the element at the specified position in this list
   * @see #addHa(int, Slave)
   * @see java.util.List#get(int)
   */
  public Slave getHa(int index) {
    return has.get(index);  
   }

  /** 
   * Appends all of the elements in the specified collection to the end of 
   * this list, in the order that they are returned by the specified 
   * collection's iterator. The behavior of this operation is unspecified if 
   * the specified collection is modified while the operation is in progress. 
   * @param c collection whose elements are to be added to this list
   * @return true if this list changed as a result of the call
   * @see java.util.List#addAll(Collection)
   */
  public boolean addHas(Collection<Slave> c) {
    setModified(true);
    for(Slave item : c) {
      setForeignKey(item, "masterHasHandle", getId());
      update.addUpdate(new ItemAdditionUpdate(item.getUpdate(), "has"));
    }
    return has.addAll(c);
  }  
  
  /** 
   * Inserts the given child at the select position in the relation has.
   * @param index index at which the specified element is to be inserted
   * @param element element to be inserted
   * @see #getHa(int)
   * @see java.util.List#add(int, Object)
   */
  public void addHa(int index, Slave element) {
    has.add(index, element);
    update.addUpdate(new ItemInsertionUpdate(element.getUpdate(), "has", index));
    setModified(true);
    setForeignKey(element, "masterHasHandle", getId());
  }

  /** 
   * Appends the specified element to the end of this list in the relation 
   * has.
   * @param element to be appended to this list
   * @return true (as per the general contract of the Collection.add method)  
   * @see java.util.List#add(Object)
   * @pre element != null
   */
  public boolean addHa(Slave element) {
    setForeignKey(element, "masterHasHandle", getId());
    update.addUpdate(new ItemAdditionUpdate(element.getUpdate(), "has"));
    setModified(true);
    return has.add(element);
  }
  
  /** 
   * Removes the first occurrence in this list of the specified element. If 
   * the list does not contain the element, it is unchanged. More formally, 
   * removes the element with the lowest index i such that 
   * (o==null ? get(i)==null : o.equals(get(i))) (if such an element exists). 
   * @param element element to be removed from this list, if present
   * @return true if this list contained the specified element
   * @see java.util.List#remove(Object)
   */
  public boolean removeHa(Slave element) {
    boolean removed = has.remove(element);
    if (removed) {
      update.addUpdate(new ItemRemovalUpdate(element.getUpdate(), "has"));
      setModified(true);
      setForeignKey(element, "masterHasHandle", null);
    }
    return removed;
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
  public Master copy() {
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
  public Master copy(Map<Object, Object> copies) {
    Master clone = (Master)copies.get(this);
    if (clone == null) {
      try {
	    clone = (Master)clone();
        copies.put(this, clone);
      } catch (CloneNotSupportedException e) {
	    // impossible exception because data object is cloneable.
      }
      clone.has = new ArrayList<Slave>();
      for (int i = 0; i < has.size(); i++) {
        clone.has.add(has.get(i).copy(copies));
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
    for (Slave item : getHas()) {
      if ((item != null) && visitor.shouldVisit(item)) {
        item.accept(visitor);
	  }
    }
  }  
  
	
  
/* %% USER DEFINED PROTECTED/PRIVATE CODE START %% */
/* %% USER DEFINED PROTECTED/PRIVATE CODE END %% */
}