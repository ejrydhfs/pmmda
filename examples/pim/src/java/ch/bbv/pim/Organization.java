/* This code was generated automatically with the pmMDA generator. Please do 
   NOT add code outside the marked zones. All changes outside the marked areas 
   will be discarded during the next execution of the MDA generator. 
*/

package ch.bbv.pim;

import ch.bbv.dog.DataObject;
import java.beans.*;
import java.lang.reflect.Field;
import java.net.URL;
import java.sql.Timestamp;
import java.util.*;

/* %% USER DEFINED IMPORT START %% */
/* %% USER DEFINED IMPORT END %% */

/**
 * The main URL of the organization defining the Internet site where 
 * information about it can be found.
 * @author Marcel Baumann
 */
public class Organization implements DataObject {

/* %% USER DEFINED DECLARATION START %% */
/* %% USER DEFINED DECLARATION END %% */

  /**
   * Serial version identifier coded as back compatibility date.
   */
  private static final long serialVersionUID = 99990101;

  /**
   * The name of the organization as defined in its legal statutes.
   */
  private String name;

  /**
   * The main email address of the organization. Often it is the information 
   * address.
   */
  private String mainEmail;

  /**
   * The main URL address of the organization where information about the 
   * organization can be found.
   */
  private URL mainUrl;

  /**
   * The main number of the organization.
   */
  private PhoneNumber mainNumber;

  /**
   * The legal main address of the organization.
   */
  private Address mainAddress;

  private List<Contact> contacts;

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
   * Helper class for managing vetoable properties and associated listeners.
   */
  protected transient VetoableChangeSupport vetoSupport;

  /** 
   * Default constructor of the class necessary for Java bean convention. The 
   * object identifier is set to null to indicate it is a new object.
   */
  public Organization() {
    contacts = new ArrayList<Contact>();
    vetoSupport = new VetoableChangeSupport(this);
  }

/* %% USER DEFINED CONSTRUCTORS START %% */
/* %% USER DEFINED CONSTRUCTORS END %% */

/* %% USER DEFINED CODE START %% */
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
   * Returns the current value of the simple property mainEmail.
   * @return the value of the simple property mainEmail.
   * @see #setMainEmail(String)
   */
  public String getMainEmail() {
    return mainEmail;
  }

  /** 
   * Sets the new value of the simple property mainEmail.
   * @param mainEmail new value of the property
   * @see #getMainEmail()
   */    
  public void setMainEmail(String mainEmail) {
    if (((this.mainEmail != null) && !this.mainEmail.equals(mainEmail)) || 
        ((this.mainEmail == null) && (mainEmail != null)))  {    
      this.mainEmail = mainEmail;
      setModified(true);
    }
  }    

  /** 
   * Returns the current value of the simple property mainUrl.
   * @return the value of the simple property mainUrl.
   * @see #setMainUrl(URL)
   */
  public URL getMainUrl() {
    return mainUrl;
  }

  /** 
   * Sets the new value of the simple property mainUrl.
   * @param mainUrl new value of the property
   * @see #getMainUrl()
   */    
  public void setMainUrl(URL mainUrl) {
    if (((this.mainUrl != null) && !this.mainUrl.equals(mainUrl)) || 
        ((this.mainUrl == null) && (mainUrl != null)))  {    
      this.mainUrl = mainUrl;
      setModified(true);
    }
  }    

  /** 
   * Returns the current value of the simple property mainNumber.
   * @return the value of the simple property mainNumber.
   * @see #setMainNumber(PhoneNumber)
   */
  public PhoneNumber getMainNumber() {
    return mainNumber;
  }

  /** 
   * Sets the new value of the simple property mainNumber.
   * @param mainNumber new value of the property
   * @see #getMainNumber()
   */    
  public void setMainNumber(PhoneNumber mainNumber) {
    if (((this.mainNumber != null) && !this.mainNumber.equals(mainNumber)) || 
        ((this.mainNumber == null) && (mainNumber != null)))  {    
      this.mainNumber = mainNumber;
      setModified(true);
    }
  }    

  /** 
   * Returns the current value of the simple property mainAddress.
   * @return the value of the simple property mainAddress.
   * @see #setMainAddress(Address)
   */
  public Address getMainAddress() {
    return mainAddress;
  }

  /** 
   * Sets the new value of the simple property mainAddress.
   * @param mainAddress new value of the property
   * @see #getMainAddress()
   */    
  public void setMainAddress(Address mainAddress) {
    if (((this.mainAddress != null) && !this.mainAddress.equals(mainAddress)) || 
        ((this.mainAddress == null) && (mainAddress != null)))  {    
      this.mainAddress = mainAddress;
      setModified(true);
    }
  }    

  /** 
   * Gets all elements of the relation contacts.
   * @return List of all children.
   */
  public List<Contact> getContacts() {
    return contacts;
  }

  /** 
   * Returns the element at the specified position in the indexed property 
   * contacts.
   * @param index index of element to return
   * @return the element at the specified position in this list
   * @see #addContact(int, Contact)
   * @see java.util.List#get(int)
   */
  public Contact getContact(int index) {
    return contacts.get(index);  
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
  public boolean addContacts(Collection<Contact> c) {
    setModified(true);
    for(Contact item : c) {
      item.setOrganization(this);
    }
    return contacts.addAll(c);
  }  
  
  /** 
   * Inserts the given child at the select position in the relation contacts.
   * @param index index at which the specified element is to be inserted
   * @param element element to be inserted
   * @throws PropertyVetoException if a listener refuses the published change.
   * @see #getContact(int)
   * @see java.util.List#add(int, Object)
   */
  public void addContact(int index, Contact element) throws PropertyVetoException {
    contacts.add(index, element);
    if (element != null) {
      element.setOrganization(this);
    }
    try {
      vetoSupport.fireVetoableChange("contacts", null, element);
    } catch (PropertyVetoException e) {
      contacts.remove(index);
      if (element != null) {
        element.setOrganization(null);
      }
      throw e;
    }
    setModified(true);
  }

  /** 
   * Appends the specified element to the end of this list in the relation 
   * contacts.
   * @param element to be appended to this list
   * @return true (as per the general contract of the Collection.add method)  
   * @throws PropertyVetoException if a listener refuses the published change
   * @see java.util.List#add(Object)
   * @pre element != null
   */
  public boolean addContact(Contact element) throws PropertyVetoException {
    boolean result = contacts.add(element);
    if (result) {	
      if (result) {
        element.setOrganization(this);
      }
      try {
        vetoSupport.fireVetoableChange("contacts", null, element);
      } catch (PropertyVetoException e) {
        contacts.remove(element);
        if (element != null) {
          element.setOrganization(null);
        }
        throw e;
      }
    }
    setModified(true);
    return result;
  }
  
  /** 
   * Removes the first occurrence in this list of the specified element. If 
   * the list does not contain the element, it is unchanged. More formally, 
   * removes the element with the lowest index i such that 
   * (o==null ? get(i)==null : o.equals(get(i))) (if such an element exists). 
   * @param element element to be removed from this list, if present
   * @return true if this list contained the specified element
   * @throws PropertyVetoException if a listener refuses the published change
   * @see java.util.List#remove(Object)
   */
  public boolean removeContact(Contact element) throws PropertyVetoException {
    boolean removed = contacts.remove(element);
    if (removed) {
      if (element != null) {
        element.setOrganization(null);
      }
      try {
        vetoSupport.fireVetoableChange("contacts", element, null);
      } catch (PropertyVetoException e) {
        contacts.add(element);
        if (element != null) {
          element.setOrganization(this);
        }
        throw e;
      }
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
   * @return a deep copy of the instance
   */
  public Organization copy() {
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
    vetoSupport.addVetoableChangeListener(listener);
  }
  
  public void addVetoableChangeListener(String propertyName, 
      VetoableChangeListener listener) {
    vetoSupport.addVetoableChangeListener(propertyName, listener);
  }
  
  public void removeVetoableChangeListener(VetoableChangeListener listener) {
    vetoSupport.removeVetoableChangeListener(listener);
  }
  
  public void removeVetoableChangeListener(String propertyName, 
      VetoableChangeListener listener) {
    vetoSupport.removeVetoableChangeListener(propertyName, listener);
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
  public Organization copy(Map<Object, Object> copies) {
    Organization clone = (Organization)copies.get(this);
    if (clone == null) {
      try {
	    clone = (Organization)clone();
        copies.put(this, clone);
      } catch (CloneNotSupportedException e) {
	    // impossible exception because data object is cloneable.
      }
      if (mainNumber != null) {
        clone.mainNumber = mainNumber.copy(copies);
      }
      if (mainAddress != null) {
        clone.mainAddress = mainAddress.copy(copies);
      }
      clone.contacts = new ArrayList<Contact>();
      for (int i = 0; i < contacts.size(); i++) {
        clone.contacts.add(contacts.get(i).copy(copies));
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
    if ((mainNumber != null) && visitor.shouldVisit(mainNumber)) {
      mainNumber.accept(visitor);
    }
    if ((mainAddress != null) && visitor.shouldVisit(mainAddress)) {
      mainAddress.accept(visitor);
    }
    for (Contact item : getContacts()) {
      if ((item != null) && visitor.shouldVisit(item)) {
        item.accept(visitor);
	  }
    }
  }  

/* %% USER DEFINED PROTECTED/PRIVATE CODE START %% */
/* %% USER DEFINED PROTECTED/PRIVATE CODE END %% */
}