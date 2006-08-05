/* This code was generated automatically with the pmMDA generator. Please do 
   NOT add code outside the marked zones. All changes outside the marked areas 
   will be discarded during the next execution of the MDA generator. 
*/

package ch.bbv.pim.lw;

import ch.bbv.dog.LightweightObject;
import java.util.*;

/* %% USER DEFINED IMPORT START %% */
/* %% USER DEFINED IMPORT END %% */

/**
 * The main URL of the organization defining the Internet site where 
 * information about it can be found.
 * @author Marcel Baumann
 */
public class OrganizationLw  implements LightweightObject {

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
   * The main number of the organization.
   */
  private PhoneNumberLw mainNumber;

  private List<ContactLw> contacts;

  /** 
   * Unique identifier of the data object instance distinguishing it from 
   * all other instances of the same data object type.
   */
  private Integer id;

  /** 
   * Default constructor of the class used to create new lightweight data 
   * object instances.
   */
  public OrganizationLw() {
    this(null);
  }
     
  /** 
   * Constructor of the class.
   * @param id identifier of the object to which the proxy is associated.
   */
  public OrganizationLw(Integer id) {
    this.id = id;
    contacts = new ArrayList<ContactLw>();
  }

/* %% USER DEFINED CONSTRUCTORS START %% */
/* %% USER DEFINED CONSTRUCTORS END %% */

/* %% USER DEFINED CODE START %% */
/* %% USER DEFINED CODE END %% */


  /** 
   * Gets the current value of the attribute Name.
   * @return the value of the attribute Name.
   * @see #setName(String)
   */
  public String getName() {
    return name;
  }

  /** 
   * Sets the new value of the simple property Name.
   * @param name new value of the property.
   * @see #getName()
   */    
  public void setName(String name) {
      this.name = name;
  }

  /** 
   * Gets the current value of the attribute MainNumber.
   * @return the value of the attribute MainNumber.
   * @see #setMainNumber(PhoneNumberLw)
   */
  public PhoneNumberLw getMainNumber() {
    return mainNumber;
  }

  /** 
   * Sets the new value of the simple property MainNumber.
   * @param mainNumber new value of the property.
   * @see #getMainNumber()
   */    
  public void setMainNumber(PhoneNumberLw mainNumber) {
      this.mainNumber = mainNumber;
  }

  /** 
   * Returns the unique identifier of the data object instance.
   * @return the identifier of the instance.
   */
  public Integer getId() {
    return id;
  }

  /** 
   * Gets all elements of the relation contacts.
   * @return List of all children.
   */
  public List<ContactLw> getContacts() {
    return contacts;
  }

  /** 
   * Returns the element at the specified position in the indexed property 
   * contacts.
   * @param index index of element to return. 
   * @return the element at the specified position in this list.
   * @see java.util.List#get(int)
   */
  public ContactLw getContact(int index) {
    return contacts.get(index);  
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
  public boolean addContacts(Collection<ContactLw> c) {
    return contacts.addAll(c);
  }  
  
  /** 
   * Inserts the given child at the select position in the relation contacts.
   * @param index index at which the specified element is to be inserted.
   * @param element element to be inserted.
   * @see java.util.List#add(int, Object)
   */
  public void addContact(int index, ContactLw element) {
    contacts.add(index, element);
  }

  /** 
   * Appends the specified element to the end of this list in the relation 
   * Contacts.
   * @param element to be appended to this list.
   * @return true (as per the general contract of the Collection.add method)   
   * @see java.util.List#add(Object)
   */
  public boolean addContact(ContactLw element) {
    return contacts.add(element);
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
  public boolean removeContact(ContactLw element) {
    return contacts.remove(element);
  }

  /** 
   * Implements the visitor pattern in the lightweight data object class. The 
   * accept method dispatches the visitor to all its relevant children. A 
   * relevant child is always another data object.
   * @param visitor visitor to act on the data object.
   * @pre visitor != null
   */  
  public void accept(LwVisitor visitor) {
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
  public OrganizationLw deepCopy() {
    OrganizationLw clone = null;
    try {
      clone = (OrganizationLw)clone();
    } catch (CloneNotSupportedException e) {
      // impossible exception because data object is cloneable.
      return null;
    }
    clone.contacts = new java.util.ArrayList<ContactLw>();
    for (int i = 0; i < contacts.size(); i++) {
      clone.contacts.add(contacts.get(i).deepCopy());
    }
    return clone;
  }

  /** 
   * Implements the visitor pattern in the ligthweight data object class. The 
   * accept method dispatches the visitor to all relevant children of the 
   * ancestors and its children. A relevant child is always another lightweigth 
   * data object.
   * @param visitor visitor to act on the data object.
   */  
  protected void traverse(LwVisitor visitor) {
    if ((mainNumber != null) && visitor.shouldVisit(mainNumber)) {
      mainNumber.accept(visitor);
    }
    for (ContactLw item : getContacts()) {
      if ((item != null) && visitor.shouldVisit(item)) {
        item.accept(visitor);
	  }
    }
  }  
}
