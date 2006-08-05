/* This code was generated automatically with the pmMDA generator. Please do 
   NOT add code outside the marked zones. All changes outside the marked areas 
   will be discarded during the next execution of the MDA generator. 
*/

package ch.bbv.pim;

import ch.bbv.dog.DataObject;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.beans.VetoableChangeListener;
import java.lang.reflect.Field;
import java.net.URL;
import java.sql.Timestamp;
import java.util.*;

/* %% USER DEFINED IMPORT START %% */
/* %% USER DEFINED IMPORT END %% */

/**
 * A contact is a person or a company with its communication information.
 * @author Marcel Baumann
 */
public class Contact implements DataObject {

/* %% USER DEFINED DECLARATION START %% */
/* %% USER DEFINED DECLARATION END %% */

  /**
   * Serial version identifier coded as back compatibility date.
   */
  private static final long serialVersionUID = 99990101;

  private String firstname;

  private String lastname;

  private String middlename;

  private boolean male;

  private String timezone;

  private String title;

  private URL workUrl;

  private URL homeURL;

  private String workEmail;

  private String homeEmail;

  private String department;

  private Date birthday;

  /**
   * The work address of the contact.
   */
  private Address workAddress;

  private Address homeAddress;

  /**
   * The work number of the contact.
   */
  private PhoneNumber workNumber;

  /**
   * The home number of the contact.
   */
  private PhoneNumber homeNumber;

  private PhoneNumber workMobileNumber;

  private PhoneNumber workFaxNumber;

  private Organization organization;

  /**
   * The language used to communicate with the contact.
   */
  private Language language;

  private List<JournalEntry> entries;

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
  public Contact() {
    entries = new ArrayList<JournalEntry>();
    changeSupport = new PropertyChangeSupport(this);
  }

/* %% USER DEFINED CONSTRUCTORS START %% */
/* %% USER DEFINED CONSTRUCTORS END %% */

/* %% USER DEFINED CODE START %% */
  public String getDistinguishedName() {
    return AddressUtilities.createDistinguishedName(this);
  }

  public String getName() {
    StringBuilder buffer = new StringBuilder();
    buffer.append(lastname);
    if ((firstname != null) || (middlename != null)) {
      buffer.append(",");
      if (firstname != null) {
        buffer.append(" ").append(firstname);
      }
      if (middlename != null) {
        buffer.append(" ").append(middlename);
      }
    }
    return buffer.toString();
  }

  public String getPrefix() {
    return AddressUtilities.createPrefix(this);
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
   * Returns the current value of the simple property firstname.
   * @return the value of the simple property firstname.
   * @see #setFirstname(String)
   */
  public String getFirstname() {
    return firstname;
  }

  /** 
   * Sets the new value of the simple property firstname.
   * @param firstname new value of the property
   * @see #getFirstname()
   */    
  public void setFirstname(String firstname) {
    if (((this.firstname != null) && !this.firstname.equals(firstname)) || 
        ((this.firstname == null) && (firstname != null)))  {    
      this.firstname = firstname;
      setModified(true);
    }
  }    

  /** 
   * Returns the current value of the simple property lastname.
   * @return the value of the simple property lastname.
   * @see #setLastname(String)
   */
  public String getLastname() {
    return lastname;
  }

  /** 
   * Sets the new value of the simple property lastname.
   * @param lastname new value of the property
   * @see #getLastname()
   */    
  public void setLastname(String lastname) {
    if (((this.lastname != null) && !this.lastname.equals(lastname)) || 
        ((this.lastname == null) && (lastname != null)))  {    
      this.lastname = lastname;
      setModified(true);
    }
  }    

  /** 
   * Returns the current value of the simple property middlename.
   * @return the value of the simple property middlename.
   * @see #setMiddlename(String)
   */
  public String getMiddlename() {
    return middlename;
  }

  /** 
   * Sets the new value of the simple property middlename.
   * @param middlename new value of the property
   * @see #getMiddlename()
   */    
  public void setMiddlename(String middlename) {
    if (((this.middlename != null) && !this.middlename.equals(middlename)) || 
        ((this.middlename == null) && (middlename != null)))  {    
      this.middlename = middlename;
      setModified(true);
    }
  }    

  /** 
   * Returns the current value of the simple property male.
   * @return the value of the simple property male.
   * @see #setMale(boolean)
   */
  public boolean isMale() {
    return male;
  }

  /** 
   * Sets the new value of the simple property male.
   * @param male new value of the property
   * @see #isMale()
   */    
  public void setMale(boolean male) {
    if (this.male != male) {  
      this.male = male;
      setModified(true);
    }
  }

  /** 
   * Returns the current value of the simple property timezone.
   * @return the value of the simple property timezone.
   * @see #setTimezone(String)
   */
  public String getTimezone() {
    return timezone;
  }

  /** 
   * Sets the new value of the simple property timezone.
   * @param timezone new value of the property
   * @see #getTimezone()
   */    
  public void setTimezone(String timezone) {
    if (((this.timezone != null) && !this.timezone.equals(timezone)) || 
        ((this.timezone == null) && (timezone != null)))  {    
      this.timezone = timezone;
      setModified(true);
    }
  }    

  /** 
   * Returns the current value of the simple property title.
   * @return the value of the simple property title.
   * @see #setTitle(String)
   */
  public String getTitle() {
    return title;
  }

  /** 
   * Sets the new value of the simple property title.
   * @param title new value of the property
   * @see #getTitle()
   */    
  public void setTitle(String title) {
    if (((this.title != null) && !this.title.equals(title)) || 
        ((this.title == null) && (title != null)))  {    
      this.title = title;
      setModified(true);
    }
  }    

  /** 
   * Returns the current value of the simple property workUrl.
   * @return the value of the simple property workUrl.
   * @see #setWorkUrl(URL)
   */
  public URL getWorkUrl() {
    return workUrl;
  }

  /** 
   * Sets the new value of the simple property workUrl.
   * @param workUrl new value of the property
   * @see #getWorkUrl()
   */    
  public void setWorkUrl(URL workUrl) {
    if (((this.workUrl != null) && !this.workUrl.equals(workUrl)) || 
        ((this.workUrl == null) && (workUrl != null)))  {    
      this.workUrl = workUrl;
      setModified(true);
    }
  }    

  /** 
   * Returns the current value of the simple property homeURL.
   * @return the value of the simple property homeURL.
   * @see #setHomeURL(URL)
   */
  public URL getHomeURL() {
    return homeURL;
  }

  /** 
   * Sets the new value of the simple property homeURL.
   * @param homeURL new value of the property
   * @see #getHomeURL()
   */    
  public void setHomeURL(URL homeURL) {
    if (((this.homeURL != null) && !this.homeURL.equals(homeURL)) || 
        ((this.homeURL == null) && (homeURL != null)))  {    
      this.homeURL = homeURL;
      setModified(true);
    }
  }    

  /** 
   * Returns the current value of the simple property workEmail.
   * @return the value of the simple property workEmail.
   * @see #setWorkEmail(String)
   */
  public String getWorkEmail() {
    return workEmail;
  }

  /** 
   * Sets the new value of the simple property workEmail.
   * @param workEmail new value of the property
   * @see #getWorkEmail()
   */    
  public void setWorkEmail(String workEmail) {
    if (((this.workEmail != null) && !this.workEmail.equals(workEmail)) || 
        ((this.workEmail == null) && (workEmail != null)))  {    
      this.workEmail = workEmail;
      setModified(true);
    }
  }    

  /** 
   * Returns the current value of the simple property homeEmail.
   * @return the value of the simple property homeEmail.
   * @see #setHomeEmail(String)
   */
  public String getHomeEmail() {
    return homeEmail;
  }

  /** 
   * Sets the new value of the simple property homeEmail.
   * @param homeEmail new value of the property
   * @see #getHomeEmail()
   */    
  public void setHomeEmail(String homeEmail) {
    if (((this.homeEmail != null) && !this.homeEmail.equals(homeEmail)) || 
        ((this.homeEmail == null) && (homeEmail != null)))  {    
      this.homeEmail = homeEmail;
      setModified(true);
    }
  }    

  /** 
   * Returns the current value of the simple property department.
   * @return the value of the simple property department.
   * @see #setDepartment(String)
   */
  public String getDepartment() {
    return department;
  }

  /** 
   * Sets the new value of the simple property department.
   * @param department new value of the property
   * @see #getDepartment()
   */    
  public void setDepartment(String department) {
    if (((this.department != null) && !this.department.equals(department)) || 
        ((this.department == null) && (department != null)))  {    
      this.department = department;
      setModified(true);
    }
  }    

  /** 
   * Returns the current value of the simple property birthday.
   * @return the value of the simple property birthday.
   * @see #setBirthday(Date)
   */
  public Date getBirthday() {
    return birthday;
  }

  /** 
   * Sets the new value of the simple property birthday.
   * @param birthday new value of the property
   * @see #getBirthday()
   */    
  public void setBirthday(Date birthday) {
    if (((this.birthday != null) && !this.birthday.equals(birthday)) || 
        ((this.birthday == null) && (birthday != null)))  {    
      this.birthday = birthday;
      setModified(true);
    }
  }    

  /** 
   * Returns the current value of the simple property workAddress.
   * @return the value of the simple property workAddress.
   * @see #setWorkAddress(Address)
   */
  public Address getWorkAddress() {
    return workAddress;
  }

  /** 
   * Sets the new value of the simple property workAddress.
   * @param workAddress new value of the property
   * @see #getWorkAddress()
   */    
  public void setWorkAddress(Address workAddress) {
    if (((this.workAddress != null) && !this.workAddress.equals(workAddress)) || 
        ((this.workAddress == null) && (workAddress != null)))  {    
      this.workAddress = workAddress;
      setModified(true);
    }
  }    

  /** 
   * Returns the current value of the simple property homeAddress.
   * @return the value of the simple property homeAddress.
   * @see #setHomeAddress(Address)
   */
  public Address getHomeAddress() {
    return homeAddress;
  }

  /** 
   * Sets the new value of the simple property homeAddress.
   * @param homeAddress new value of the property
   * @see #getHomeAddress()
   */    
  public void setHomeAddress(Address homeAddress) {
    if (((this.homeAddress != null) && !this.homeAddress.equals(homeAddress)) || 
        ((this.homeAddress == null) && (homeAddress != null)))  {    
      this.homeAddress = homeAddress;
      setModified(true);
    }
  }    

  /** 
   * Returns the current value of the simple property workNumber.
   * @return the value of the simple property workNumber.
   * @see #setWorkNumber(PhoneNumber)
   */
  public PhoneNumber getWorkNumber() {
    return workNumber;
  }

  /** 
   * Sets the new value of the simple property workNumber.
   * @param workNumber new value of the property
   * @see #getWorkNumber()
   */    
  public void setWorkNumber(PhoneNumber workNumber) {
    if (((this.workNumber != null) && !this.workNumber.equals(workNumber)) || 
        ((this.workNumber == null) && (workNumber != null)))  {    
      this.workNumber = workNumber;
      setModified(true);
    }
  }    

  /** 
   * Returns the current value of the simple property homeNumber.
   * @return the value of the simple property homeNumber.
   * @see #setHomeNumber(PhoneNumber)
   */
  public PhoneNumber getHomeNumber() {
    return homeNumber;
  }

  /** 
   * Sets the new value of the simple property homeNumber.
   * @param homeNumber new value of the property
   * @see #getHomeNumber()
   */    
  public void setHomeNumber(PhoneNumber homeNumber) {
    if (((this.homeNumber != null) && !this.homeNumber.equals(homeNumber)) || 
        ((this.homeNumber == null) && (homeNumber != null)))  {    
      this.homeNumber = homeNumber;
      setModified(true);
    }
  }    

  /** 
   * Returns the current value of the simple property workMobileNumber.
   * @return the value of the simple property workMobileNumber.
   * @see #setWorkMobileNumber(PhoneNumber)
   */
  public PhoneNumber getWorkMobileNumber() {
    return workMobileNumber;
  }

  /** 
   * Sets the new value of the simple property workMobileNumber.
   * @param workMobileNumber new value of the property
   * @see #getWorkMobileNumber()
   */    
  public void setWorkMobileNumber(PhoneNumber workMobileNumber) {
    if (((this.workMobileNumber != null) && !this.workMobileNumber.equals(workMobileNumber)) || 
        ((this.workMobileNumber == null) && (workMobileNumber != null)))  {    
      this.workMobileNumber = workMobileNumber;
      setModified(true);
    }
  }    

  /** 
   * Returns the current value of the simple property workFaxNumber.
   * @return the value of the simple property workFaxNumber.
   * @see #setWorkFaxNumber(PhoneNumber)
   */
  public PhoneNumber getWorkFaxNumber() {
    return workFaxNumber;
  }

  /** 
   * Sets the new value of the simple property workFaxNumber.
   * @param workFaxNumber new value of the property
   * @see #getWorkFaxNumber()
   */    
  public void setWorkFaxNumber(PhoneNumber workFaxNumber) {
    if (((this.workFaxNumber != null) && !this.workFaxNumber.equals(workFaxNumber)) || 
        ((this.workFaxNumber == null) && (workFaxNumber != null)))  {    
      this.workFaxNumber = workFaxNumber;
      setModified(true);
    }
  }    

  /** 
   * Returns the current value of the simple property organization.
   * @return the value of the simple property organization.
   * @see #setOrganization(Organization)
   */
  public Organization getOrganization() {
    return organization;
  }

  /** 
   * Sets the new value of the simple property organization.
   * @param organization new value of the property
   * @see #getOrganization()
   */    
  public void setOrganization(Organization organization) {
    if (((this.organization != null) && !this.organization.equals(organization)) || 
        ((this.organization == null) && (organization != null)))  {    
      this.organization = organization;
      setModified(true);
    }
  }    

  /** 
   * Returns the current value of the simple property language.
   * @return the value of the simple property language.
   * @see #setLanguage(Language)
   */
  public Language getLanguage() {
    return language;
  }

  /** 
   * Sets the new value of the simple property language.
   * @param language new value of the property
   * @see #getLanguage()
   */    
  public void setLanguage(Language language) {
    if (((this.language != null) && !this.language.equals(language)) || 
        ((this.language == null) && (language != null)))  {    
      this.language = language;
      setModified(true);
    }
  }    

  /** 
   * Gets all elements of the relation entries.
   * @return List of all children.
   */
  public List<JournalEntry> getEntries() {
    return entries;
  }

  /** 
   * Returns the element at the specified position in the indexed property 
   * entries.
   * @param index index of element to return
   * @return the element at the specified position in this list
   * @see #addEntry(int, JournalEntry)
   * @see java.util.List#get(int)
   */
  public JournalEntry getEntry(int index) {
    return entries.get(index);  
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
  public boolean addEntries(Collection<JournalEntry> c) {
    setModified(true);
    return entries.addAll(c);
  }  
  
  /** 
   * Inserts the given child at the select position in the relation entries.
   * @param index index at which the specified element is to be inserted
   * @param element element to be inserted
   * @see #getEntry(int)
   * @see java.util.List#add(int, Object)
   */
  public void addEntry(int index, JournalEntry element) {
    entries.add(index, element);
    changeSupport.fireIndexedPropertyChange("entries", index, null, element);
    setModified(true);
  }

  /** 
   * Appends the specified element to the end of this list in the relation 
   * entries.
   * @param element to be appended to this list
   * @return true (as per the general contract of the Collection.add method)  
   * @see java.util.List#add(Object)
   * @pre element != null
   */
  public boolean addEntry(JournalEntry element) {
    boolean result = entries.add(element);
    if (result) {	
      changeSupport.firePropertyChange("entries", null, element);
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
   * @see java.util.List#remove(Object)
   */
  public boolean removeEntry(JournalEntry element) {
    boolean removed = entries.remove(element);
    if (removed) {
      changeSupport.fireIndexedPropertyChange("entries", -1, element, null);
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
  public Contact copy() {
    Map<Object, Object> copies = new HashMap<Object, Object>();
    return copy(copies);
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
  public Contact copy(Map<Object, Object> copies) {
    Contact clone = (Contact)copies.get(this);
    if (clone == null) {
      try {
	    clone = (Contact)clone();
        copies.put(this, clone);
      } catch (CloneNotSupportedException e) {
	    // impossible exception because data object is cloneable.
      }
      if (workAddress != null) {
        clone.workAddress = workAddress.copy(copies);
      }
      if (homeAddress != null) {
        clone.homeAddress = homeAddress.copy(copies);
      }
      if (workNumber != null) {
        clone.workNumber = workNumber.copy(copies);
      }
      if (homeNumber != null) {
        clone.homeNumber = homeNumber.copy(copies);
      }
      if (workMobileNumber != null) {
        clone.workMobileNumber = workMobileNumber.copy(copies);
      }
      if (workFaxNumber != null) {
        clone.workFaxNumber = workFaxNumber.copy(copies);
      }
      if (organization != null) {
        clone.organization = organization.copy(copies);
      }
      clone.entries = new ArrayList<JournalEntry>();
      for (int i = 0; i < entries.size(); i++) {
        clone.entries.add(entries.get(i).copy(copies));
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
    if ((workAddress != null) && visitor.shouldVisit(workAddress)) {
      workAddress.accept(visitor);
    }
    if ((homeAddress != null) && visitor.shouldVisit(homeAddress)) {
      homeAddress.accept(visitor);
    }
    if ((workNumber != null) && visitor.shouldVisit(workNumber)) {
      workNumber.accept(visitor);
    }
    if ((homeNumber != null) && visitor.shouldVisit(homeNumber)) {
      homeNumber.accept(visitor);
    }
    if ((workMobileNumber != null) && visitor.shouldVisit(workMobileNumber)) {
      workMobileNumber.accept(visitor);
    }
    if ((workFaxNumber != null) && visitor.shouldVisit(workFaxNumber)) {
      workFaxNumber.accept(visitor);
    }
    for (JournalEntry item : getEntries()) {
      if ((item != null) && visitor.shouldVisit(item)) {
        item.accept(visitor);
	  }
    }
  }  

/* %% USER DEFINED PROTECTED/PRIVATE CODE START %% */
/* %% USER DEFINED PROTECTED/PRIVATE CODE END %% */
}