/* This code was generated automatically with the pmMDA generator. Please do 
   NOT add code outside the marked zones. All changes outside the marked areas 
   will be discarded during the next execution of the MDA generator. 
   $Id$
*/

package ch.bbv.dnm.users;

import ch.bbv.dog.DataObject;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.beans.VetoableChangeListener;
import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.util.*;

/* %% USER DEFINED IMPORT START %% */
/* %% USER DEFINED IMPORT END %% */

/**
 * A unit is a logical group of resources inside a domain. In Internet 
 * terminology it is a sub address modeling e.g. a subsudiary of a company. A 
 * unit can have sub units.
 * @version $Revision$
 * @author MarcelBaumann
 */
public class Unit implements DataObject {

/* %% USER DEFINED DECLARATION START %% */
/* %% USER DEFINED DECLARATION END %% */

  /**
   * Serial version identifier coded as back compatibility date.
   */
  private static final long serialVersionUID = 99990101;

  /**
   * The human readable name of the entity. The name should be unique in the 
   * context it is used to simplify navigation for users.
   */
  private String name;

  /**
   * Description of the item.
   */
  private String description;

  /**
   * List of users belonging to the unit.
   */
  private List<User> users;

  /**
   * List of units part of the unit.
   */
  private List<Unit> units;

  /**
   * Mapping for 1: N relationship units as reverse foreign key.
   */
  private Integer unitUnitsHandle;

  /**
   * Mapping for 1: N relationship units as reverse foreign key.
   */
  private Integer domainUnitsHandle;

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
   * Helper class for managing changeable properties and associated listeners.
   */
  protected transient PropertyChangeSupport changeSupport;

  /** 
   * Default constructor of the class necessary for Java bean convention. The 
   * object identifier is set to null to indicate it is a new object.
   */
  public Unit() {
  	users = new ArrayList<User>();
  	units = new ArrayList<Unit>();
    changeSupport = new PropertyChangeSupport(this);
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
	String oldName = this.name;
    if (((this.name != null) && !this.name.equals(name)) || 
   	    ((this.name == null) && (name != null)))  {    
      this.name = name;
      changeSupport.firePropertyChange("name", oldName, name);
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
   * Gets all elements of the relation users.
   * @return List of all children.
   */
  public List<User> getUsers() {
    return users;
  }

  /** 
   * Returns the element at the specified position in the indexed property 
   * users.
   * @param index index of element to return. 
   * @return the element at the specified position in this list.
   * @see #setUser(int, User)
   * @see java.util.List#get(int)
   */
  public User getUser(int index) {
    return users.get(index);  
   }

  /** 
   * Appends all of the elements in the specified collection to the end of 
   * this list, in the order that they are returned by the specified 
   * collection's iterator. The behavior of this operation is unspecified if 
   * the specified collection is modified while the operation is in progress. 
   * @param c collection whose elements are to be added to this list. 
   * @return true if this list changed as a result of the call. 
   * @see java.util.List#addAll(Collection)
   */
  public boolean addUsers(Collection<User> c) {
    setModified(true);
    for(User item : c) {
      setForeignKey(item, "unitUsersHandle", getId());
	}
    return users.addAll(c);
  }  
  
  /** 
   * Inserts the given child at the select position in the relation users.
   * @param index index at which the specified element is to be inserted.
   * @param element element to be inserted.
   * @see #getUser(int)
   * @see java.util.List#add(int, Object)
   */
  public void setUser(int index, User element) {
    setModified(true);
    setForeignKey(element, "unitUsersHandle", getId());
    users.add(index, element);
  }

  /** 
   * Appends the specified element to the end of this list in the relation 
   * users.
   * @param element to be appended to this list.
   * @return true (as per the general contract of the Collection.add method).   
   * @see java.util.List#add(Object)
   * @pre element != null
   */
  public boolean addUser(User element) {
    setModified(true);
    setForeignKey(element, "unitUsersHandle", getId());
    return users.add(element);
  }
  
  /** 
   * Removes the first occurrence in this list of the specified element 
   * If this list does not contain the element, it is unchanged. More formally, 
   * removes the element with the lowest index i such that 
   * (o==null ? get(i)==null : o.equals(get(i))) (if such an element exists). 
   * @param element element to be removed from this list, if present. 
   * @return true if this list contained the specified element. 
   * @see java.util.List#remove(Object)
   */
  public boolean removeUser(User element) {
    boolean removed = users.remove(element);
    if (removed) {
      setModified(true);
      setForeignKey(element, "unitUsersHandle", getId());
    }
    return removed;
  }

  /** 
   * Gets all elements of the relation units.
   * @return List of all children.
   */
  public List<Unit> getUnits() {
    return units;
  }

  /** 
   * Returns the element at the specified position in the indexed property 
   * units.
   * @param index index of element to return. 
   * @return the element at the specified position in this list.
   * @see #setUnit(int, Unit)
   * @see java.util.List#get(int)
   */
  public Unit getUnit(int index) {
    return units.get(index);  
   }

  /** 
   * Appends all of the elements in the specified collection to the end of 
   * this list, in the order that they are returned by the specified 
   * collection's iterator. The behavior of this operation is unspecified if 
   * the specified collection is modified while the operation is in progress. 
   * @param c collection whose elements are to be added to this list. 
   * @return true if this list changed as a result of the call. 
   * @see java.util.List#addAll(Collection)
   */
  public boolean addUnits(Collection<Unit> c) {
    setModified(true);
    for(Unit item : c) {
      setForeignKey(item, "unitUnitsHandle", getId());
	}
    return units.addAll(c);
  }  
  
  /** 
   * Inserts the given child at the select position in the relation units.
   * @param index index at which the specified element is to be inserted.
   * @param element element to be inserted.
   * @see #getUnit(int)
   * @see java.util.List#add(int, Object)
   */
  public void setUnit(int index, Unit element) {
    setModified(true);
    setForeignKey(element, "unitUnitsHandle", getId());
    units.add(index, element);
  }

  /** 
   * Appends the specified element to the end of this list in the relation 
   * units.
   * @param element to be appended to this list.
   * @return true (as per the general contract of the Collection.add method).   
   * @see java.util.List#add(Object)
   * @pre element != null
   */
  public boolean addUnit(Unit element) {
    setModified(true);
    setForeignKey(element, "unitUnitsHandle", getId());
    return units.add(element);
  }
  
  /** 
   * Removes the first occurrence in this list of the specified element 
   * If this list does not contain the element, it is unchanged. More formally, 
   * removes the element with the lowest index i such that 
   * (o==null ? get(i)==null : o.equals(get(i))) (if such an element exists). 
   * @param element element to be removed from this list, if present. 
   * @return true if this list contained the specified element. 
   * @see java.util.List#remove(Object)
   */
  public boolean removeUnit(Unit element) {
    boolean removed = units.remove(element);
    if (removed) {
      setModified(true);
      setForeignKey(element, "unitUnitsHandle", getId());
    }
    return removed;
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
  public Unit copy() {
    Unit clone = null;
    try {
      clone = (Unit)clone();
    } catch (CloneNotSupportedException e) {
      // impossible exception because data object is cloneable.
      return null;
    }
    copy(clone);
    clone.setModified(isModified());
    return clone;
  }

  public void addPropertyChangeListener(PropertyChangeListener listener) {
    changeSupport.addPropertyChangeListener(listener);
  }
  
  public void addPropertyChangeListener(String propertyName, 
      PropertyChangeListener listener) {
    changeSupport.addPropertyChangeListener(propertyName, listener);
  }
  
  public void removePropertyChangeListener(PropertyChangeListener listener) {
    changeSupport.removePropertyChangeListener(listener);
  }
  
  public void removePropertyChangeListener(String propertyName, 
      PropertyChangeListener listener) {
    changeSupport.removePropertyChangeListener(propertyName, listener);
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
  protected void copy(Unit clone) {
    clone.users = new ArrayList<User>();
    for (int i = 0; i < users.size(); i++) {
      clone.users.add(users.get(i).copy());
    }
    clone.units = new ArrayList<Unit>();
    for (int i = 0; i < units.size(); i++) {
      clone.units.add(units.get(i).copy());
    }
  }

  /** 
   * Implements the visitor pattern in the data object class. The accept
   * method dispatches the visitor to all relevant children of the ancestors
   * and its children. A relevant child is always another data object.
   * @param visitor visitor to act on the data object.
   */  
  protected void traverse(Visitor visitor) {
    for (User item : getUsers()) {
      item.accept(visitor);
    }
    for (Unit item : getUnits()) {
      item.accept(visitor);
    }
  }  

/* %% USER DEFINED PROTECTED/PRIVATE CODE START %% */
/* %% USER DEFINED PROTECTED/PRIVATE CODE END %% */
}
