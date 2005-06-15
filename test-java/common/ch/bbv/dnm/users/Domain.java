/* This code was generated automatically with the pmMDA generator. Please do 
   NOT add code outside the marked zones. All changes outside the marked areas 
   will be discarded during the next execution of the MDA generator. 
   $Id$
*/

package ch.bbv.dnm.users;

import ch.bbv.dog.DataObject;
import java.beans.PropertyChangeListener;
import java.beans.VetoableChangeListener;
import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.util.*;

/* %% USER DEFINED IMPORT START %% */
/* %% USER DEFINED IMPORT END %% */

/**
 * A domain is a set of resources independant from all other domains. In 
 * Internet terminology a domain is a Internet domain identified through its 
 * name.
 * @version $Revision$
 * @author MarcelBaumann
 */
public class Domain implements DataObject {

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
   * Long name of the domain used for correspondance and human interactions.
   */
  private String longname;

  /**
   * The administrator of a domain is the user responsible for it and 
   * optionally some of its subdomains.
   */
  private User administrator;

  /**
   * Mapping for 1: 1 relationship ${relation.getName()} as foreign key.
   */
  private Integer administratorHandle;  

  /**
   * List of units member of the domain.
   */
  private List<Unit> units;

  /**
   * Mapping for 1: N relationship domains as reverse foreign key.
   */
  private Integer providerDomainsHandle;

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
  public Domain() {
  	units = new ArrayList<Unit>();
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
   * Returns the current value of the simple property longname.
   * @return the value of the simple property longname.
   * @see #setLongname(String)
   */
  public String getLongname() {
    return longname;
  }

  /** 
   * Sets the new value of the simple property longname.
   * @param longname new value of the property.
   * @see #getLongname()
   */    
  public void setLongname(String longname) {
    if (((this.longname != null) && !this.longname.equals(longname)) || 
   	    ((this.longname == null) && (longname != null)))  {    
      this.longname = longname;
      setModified(true);
    }
  }    

  /** 
   * Returns the current value of the simple property administrator.
   * @return the value of the simple property administrator.
   * @see #setAdministrator(User)
   */
  public User getAdministrator() {
    return administrator;
  }

  /** 
   * Sets the new value of the simple property administrator.
   * @param administrator new value of the property.
   * @see #getAdministrator()
   */    
  public void setAdministrator(User administrator) {
    if (((this.administrator != null) && !this.administrator.equals(administrator)) || 
   	    ((this.administrator == null) && (administrator != null)))  {    
      this.administrator = administrator;
      administratorHandle = (administrator != null ? administrator.getId() : null);
      setModified(true);
    }
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
      setForeignKey(item, "domainUnitsHandle", getId());
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
    setForeignKey(element, "domainUnitsHandle", getId());
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
    setForeignKey(element, "domainUnitsHandle", getId());
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
      setForeignKey(element, "domainUnitsHandle", getId());
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
  public Domain copy() {
    Domain clone = null;
    try {
      clone = (Domain)clone();
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
  protected void copy(Domain clone) {
    if (administrator != null) {
      clone.administrator = administrator.copy();
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
    if (administrator != null) {
      administrator.accept(visitor);
    }
    for (Unit item : getUnits()) {
      item.accept(visitor);
    }
  }  

/* %% USER DEFINED PROTECTED/PRIVATE CODE START %% */
/* %% USER DEFINED PROTECTED/PRIVATE CODE END %% */
}
