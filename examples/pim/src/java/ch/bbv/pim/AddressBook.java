/* This code was generated automatically with the pmMDA generator. Please do 
   NOT add code outside the marked zones. All changes outside the marked areas 
   will be discarded during the next execution of the MDA generator. 
*/

package ch.bbv.pim;

import ch.bbv.dog.DataObject;
import java.beans.PropertyChangeListener;
import java.beans.VetoableChangeListener;
import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.util.*;

/* %% USER DEFINED IMPORT START %% */
/* %% USER DEFINED IMPORT END %% */

public class AddressBook implements DataObject {

/* %% USER DEFINED DECLARATION START %% */
/* %% USER DEFINED DECLARATION END %% */

  /**
   * Serial version identifier coded as back compatibility date.
   */
  private static final long serialVersionUID = 99990101;

  private String name;

  private List<Organization> organizations;

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
  public AddressBook() {
    organizations = new ArrayList<Organization>();
  }

/* %% USER DEFINED CONSTRUCTORS START %% */
/* %% USER DEFINED CONSTRUCTORS END %% */

/* %% USER DEFINED CODE START %% */
  /**
   * Finds the contact with the distinguished name and email address. At least
   * one of the parameter must not be null.
   * @param distinguishedName distinguished name of the contact is build with
   *          firstname middlename surname. The name can have a prefix such as
   *          mister.
   * @param email email address of the contact - either the work or private
   *          email address can be specified.
   * @return the requested contact if found otherwise null.
   */
  public Contact findContact(String distinguishedName, String email) {
    for (Organization organization : getOrganizations()) {
      for (Contact contact : organization.getContacts()) {
        if (email != null) {
          if (email.equalsIgnoreCase(contact.getWorkEmail())
              || email.equalsIgnoreCase(contact.getHomeEmail())) {
            if ((distinguishedName == null)
                || (distinguishedName.indexOf(contact.getDistinguishedName()) != -1)) {
              return contact;
            }
          }
        } else {
          if (distinguishedName.indexOf(contact.getDistinguishedName()) != -1) {
            return contact;
          }
        }
      }
    }
    return null;
  }

  public Organization findOrganization(String name) {
    for (Organization organization : getOrganizations()) {
      if (name.equalsIgnoreCase(organization.getName())) {
        return organization;
      }
    }
    return null;
  }

/* %% USER DEFINED CODE END %% */

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
   * Returns the current value of the simple property name.
   * @return the value of the simple property name.
   * @see #setName(String)
   */
  public String getName() {
    return name;
  }

  /** 
   * Sets the new value of the simple property name.
   * @param name new value of the property
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
   * Gets all elements of the relation organizations.
   * @return List of all children.
   */
  public List<Organization> getOrganizations() {
    return organizations;
  }

  /** 
   * Returns the element at the specified position in the indexed property 
   * organizations.
   * @param index index of element to return
   * @return the element at the specified position in this list
   * @see #addOrganization(int, Organization)
   * @see java.util.List#get(int)
   */
  public Organization getOrganization(int index) {
    return organizations.get(index);  
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
  public boolean addOrganizations(Collection<Organization> c) {
    setModified(true);
    return organizations.addAll(c);
  }  
  
  /** 
   * Inserts the given child at the select position in the relation organizations.
   * @param index index at which the specified element is to be inserted
   * @param element element to be inserted
   * @see #getOrganization(int)
   * @see java.util.List#add(int, Object)
   */
  public void addOrganization(int index, Organization element) {
    organizations.add(index, element);
    setModified(true);
  }

  /** 
   * Appends the specified element to the end of this list in the relation 
   * organizations.
   * @param element to be appended to this list
   * @return true (as per the general contract of the Collection.add method)  
   * @see java.util.List#add(Object)
   * @pre element != null
   */
  public boolean addOrganization(Organization element) {
    setModified(true);
    return organizations.add(element);
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
  public boolean removeOrganization(Organization element) {
    boolean removed = organizations.remove(element);
    if (removed) {
      setModified(true);
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
   * @return a deep copy of the instance.
   */
  public AddressBook copy() {
    AddressBook clone = null;
    try {
      clone = (AddressBook)clone();
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
   * Copies deeply all the attributes of the class and its ancestors.
   * @param clone the copy which deep copy should be generated
   */
  protected void copy(AddressBook clone) {
    clone.organizations = new ArrayList<Organization>();
    for (int i = 0; i < organizations.size(); i++) {
      clone.organizations.add(organizations.get(i).copy());
    }
  }

  /** 
   * Implements the visitor pattern in the data object class. The accept
   * method dispatches the visitor to all relevant children of the ancestors
   * and its children. A relevant child is always another data object.
   * @param visitor visitor to act on the data object
   */  
  protected void traverse(Visitor visitor) {
    for (Organization item : getOrganizations()) {
      item.accept(visitor);
    }
  }  

/* %% USER DEFINED PROTECTED/PRIVATE CODE START %% */
/* %% USER DEFINED PROTECTED/PRIVATE CODE END %% */
}
