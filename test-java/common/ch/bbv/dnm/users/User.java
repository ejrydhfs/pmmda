/* This code was generated automatically with the pmMDA generator. Please do 
   NOT add code outside the marked zones. All changes outside the marked areas 
   will be discarded during the next execution of the MDA generator. 
   $Id$
*/

package ch.bbv.dnm.users;

import ch.bbv.dog.DataObject;
import java.util.*;

/* %% USER DEFINED IMPORT START %% */
/* %% USER DEFINED IMPORT END %% */

/**
 * A user is an entity who can connect to the system. All operations he 
 * performs can be logged.
 * @version $Revision$
 * @author Marcel Baumann
 */
public class User extends NamedEntity implements DataObject {

/* %% USER DEFINED DECLARATION START %% */
/* %% USER DEFINED DECLARATION END %% */

  /**
   * Serial version identifier coded as back compatibility date.
   */
  private static final long serialVersionUID = 99990101;

  /**
   * The human readable name of the entity. The name should be unique in the 
   * context it is used to simplify navigation for users. The context of the 
   * user is the domain therefore the login is unique in the domain.
   */
  private String login;

  /**
   * Flag indicating if the user is currently active or not.
   */
  private boolean active;

  /**
   * The primary email address of the user.
   */
  private String email;

  /**
   * The password of the user.
   */
  private String password;

  /**
   * Upper range limit of the user account validity. If the value is null no 
   * upper limit is defined.
   */
  private Date validTo;

  /**
   * Lower range limit of the user account validity. If the value is null no 
   * lower limit is defined.
   */
  private Date validFrom;

  /**
   * The roles the user belongs to.
   */
  private List<UserRole> roles;
  /**
   * Mapping for 1: N relationship users as reverse foreign key.
   */
  private Integer unitUsersHandle;

  /** 
   * Default constructor of the class necessary for Java bean convention. The 
   * object identifier is set to null to indicate it is a new object.
   */
  public User() {
    roles = new ArrayList<UserRole>();
  }

/* %% USER DEFINED CONSTRUCTORS START %% */
/* %% USER DEFINED CONSTRUCTORS END %% */

/* %% USER DEFINED CODE START %% */
/* %% USER DEFINED CODE END %% */


  /** 
   * Returns the current value of the simple property login.
   * @return the value of the simple property login.
   * @see #setLogin(String)
   */
  public String getLogin() {
    return login;
  }

  /** 
   * Sets the new value of the simple property login.
   * @param login new value of the property.
   * @see #getLogin()
   */    
  public void setLogin(String login) {
    String oldLogin = this.login;
    if (((this.login != null) && !this.login.equals(login)) || 
        ((this.login == null) && (login != null)))  {    
      this.login = login;
      changeSupport.firePropertyChange("login", oldLogin, login);
      setModified(true);
    }
  }    

  /** 
   * Returns the current value of the simple property active.
   * @return the value of the simple property active.
   * @see #setActive(boolean)
   */
  public boolean isActive() {
    return active;
  }

  /** 
   * Sets the new value of the simple property active.
   * @param active new value of the property.
   * @see #isActive()
   */    
  public void setActive(boolean active) {
    if (this.active != active) {  
      this.active = active;
      setModified(true);
    }
  }

  /** 
   * Returns the current value of the simple property email.
   * @return the value of the simple property email.
   * @see #setEmail(String)
   */
  public String getEmail() {
    return email;
  }

  /** 
   * Sets the new value of the simple property email.
   * @param email new value of the property.
   * @see #getEmail()
   */    
  public void setEmail(String email) {
    String oldEmail = this.email;
    if (((this.email != null) && !this.email.equals(email)) || 
        ((this.email == null) && (email != null)))  {    
      this.email = email;
      changeSupport.firePropertyChange("email", oldEmail, email);
      setModified(true);
    }
  }    

  /** 
   * Returns the current value of the simple property password.
   * @return the value of the simple property password.
   * @see #setPassword(String)
   */
  public String getPassword() {
    return password;
  }

  /** 
   * Sets the new value of the simple property password.
   * @param password new value of the property.
   * @see #getPassword()
   */    
  public void setPassword(String password) {
    if (((this.password != null) && !this.password.equals(password)) || 
        ((this.password == null) && (password != null)))  {    
      this.password = password;
      setModified(true);
    }
  }    

  /** 
   * Returns the current value of the simple property validTo.
   * @return the value of the simple property validTo.
   * @see #setValidTo(Date)
   */
  public Date getValidTo() {
    return validTo;
  }

  /** 
   * Sets the new value of the simple property validTo.
   * @param validTo new value of the property.
   * @see #getValidTo()
   */    
  public void setValidTo(Date validTo) {
    if (((this.validTo != null) && !this.validTo.equals(validTo)) || 
        ((this.validTo == null) && (validTo != null)))  {    
      this.validTo = validTo;
      setModified(true);
    }
  }    

  /** 
   * Returns the current value of the simple property validFrom.
   * @return the value of the simple property validFrom.
   * @see #setValidFrom(Date)
   */
  public Date getValidFrom() {
    return validFrom;
  }

  /** 
   * Sets the new value of the simple property validFrom.
   * @param validFrom new value of the property.
   * @see #getValidFrom()
   */    
  public void setValidFrom(Date validFrom) {
    if (((this.validFrom != null) && !this.validFrom.equals(validFrom)) || 
        ((this.validFrom == null) && (validFrom != null)))  {    
      this.validFrom = validFrom;
      setModified(true);
    }
  }    

  /** 
   * Gets all elements of the relation roles.
   * @return List of all children.
   */
  public List<UserRole> getRoles() {
    return roles;
  }

  /** 
   * Returns the element at the specified position in the indexed property 
   * roles.
   * @param index index of element to return. 
   * @return the element at the specified position in this list.
   * @see #setRole(int, UserRole)
   * @see java.util.List#get(int)
   */
  public UserRole getRole(int index) {
    return roles.get(index);  
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
  public boolean addRoles(Collection<UserRole> c) {
    setModified(true);
    for(UserRole item : c) {
      setForeignKey(item, "userRolesHandle", getId());
    }
    return roles.addAll(c);
  }  
  
  /** 
   * Inserts the given child at the select position in the relation roles.
   * @param index index at which the specified element is to be inserted.
   * @param element element to be inserted.
   * @see #getRole(int)
   * @see java.util.List#add(int, Object)
   */
  public void setRole(int index, UserRole element) {
    setModified(true);
    setForeignKey(element, "userRolesHandle", getId());
    roles.add(index, element);
  }

  /** 
   * Appends the specified element to the end of this list in the relation 
   * roles.
   * @param element to be appended to this list.
   * @return true (as per the general contract of the Collection.add method).   
   * @see java.util.List#add(Object)
   * @pre element != null
   */
  public boolean addRole(UserRole element) {
    setModified(true);
    setForeignKey(element, "userRolesHandle", getId());
    return roles.add(element);
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
  public boolean removeRole(UserRole element) {
    boolean removed = roles.remove(element);
    if (removed) {
      setModified(true);
      setForeignKey(element, "userRolesHandle", getId());
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
  public User copy() {
    User clone = null;
    try {
      clone = (User)clone();
    } catch (CloneNotSupportedException e) {
      // impossible exception because data object is cloneable.
      return null;
    }
    copy(clone);
    clone.setModified(isModified());
    return clone;
  }

  /** 
   * Copies deeply all the attributes of the class and its ancestors.
   * @param clone the copy which deep copy should be generated.
   */
  protected void copy(User clone) {
    super.copy(clone);
    clone.roles = new ArrayList<UserRole>();
    for (int i = 0; i < roles.size(); i++) {
      clone.roles.add(roles.get(i).copy());
    }
  }

  /** 
   * Implements the visitor pattern in the data object class. The accept
   * method dispatches the visitor to all relevant children of the ancestors
   * and its children. A relevant child is always another data object.
   * @param visitor visitor to act on the data object.
   */  
  protected void traverse(Visitor visitor) {
    super.traverse(visitor);
    for (UserRole item : getRoles()) {
      item.accept(visitor);
    }
  }  

/* %% USER DEFINED PROTECTED/PRIVATE CODE START %% */
/* %% USER DEFINED PROTECTED/PRIVATE CODE END %% */
}
