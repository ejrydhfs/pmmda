/* This code was generated automatically with the pmMDA generator. Please do 
   NOT add code outside the marked zones. All changes outside the marked areas 
   will be discarded during the next execution of the MDA generator. 
   $Id$
*/
   
package ch.bbv.dnm.users;

import ch.bbv.dog.DataObject;
import java.beans.PropertyChangeListener;
import java.beans.VetoableChangeListener;
import java.sql.Timestamp;

/* %% USER DEFINED IMPORT START %% */
/* %% USER DEFINED IMPORT END   %% */

/**
 * Ther user role implements the correlation table of N..M relation between 
 * user and role. The user name and role name are copied as correlation 
 * attributes because the MySQL does not support view. The above attributes 
 * are required for the Realm component of the Tomcat application.
 * @version $Revision$
 */
public class UserRole implements DataObject {

/* %% USER DEFINED DECLARATION START %% */
/* %% USER DEFINED DECLARATION END %% */


  private String username;

  private String rolename;

  private Role role;

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
  public UserRole() {
  }

/* %% USER DEFINED CONSTRUCTORS START %% */
/* %% USER DEFINED CONSTRUCTORS END %% */

/* %% USER DEFINED CODE START %% */
/* %% USER DEFINED CODE END %% */

  /** 
   * Returns the unique identifier of the data object instance.
   * @return the identifier of the instance.
   */
  public Integer getId() {
  	return id;
  }
  
  /** 
   * Sets the identifier of the data object instance.
   * @param id new identifier of the instance.
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
   */
  public Timestamp getTimestamp() {
    return timestamp;
  }
  
  /** 
   * Sets the timestamp of the data object instance.
   * @param timestamp timestamp of the instance.
   */
   public void setTimestamp(Timestamp timestamp) {
     this.timestamp = timestamp;
   }

  /** 
   * Gets the current value of the simple property Username.
   * @return the value of the simple property Username.
   * @see #setUsername
   */
  public String getUsername() {
    return username;
  }

  /** 
   * Sets the new value of the simple property username.
   * @param username new value of the property.
   * @see #getUsername
   */    
  public void setUsername(String username) {
    if (((this.username != null) && !this.username.equals(username)) || 
    	((this.username == null) && (username != null)))  {    
      this.username = username;
      setModified(true);
    }
  }

  /** 
   * Gets the current value of the simple property Rolename.
   * @return the value of the simple property Rolename.
   * @see #setRolename
   */
  public String getRolename() {
    return rolename;
  }

  /** 
   * Sets the new value of the simple property rolename.
   * @param rolename new value of the property.
   * @see #getRolename
   */    
  public void setRolename(String rolename) {
    if (((this.rolename != null) && !this.rolename.equals(rolename)) || 
    	((this.rolename == null) && (rolename != null)))  {    
      this.rolename = rolename;
      setModified(true);
    }
  }

  /** 
   * Gets the current value of the simple property Role.
   * @return the value of the simple property Role.
   * @see #setRole
   */
  public Role getRole() {
    return role;
  }

  /** 
   * Sets the new value of the simple property role.
   * @param role new value of the property.
   * @see #getRole
   */    
  public void setRole(Role role) {
    if (((this.role != null) && !this.role.equals(role)) || 
    	((this.role == null) && (role != null)))  {    
      this.role = role;
      setModified(true);
    }
  }

  /** 
   * Implements the visitor pattern in the data object class. The accept
   * method dispatches the visitor to all its relevant children. A relevant
   * child is always another data object.
   * @param visitor visitor to act on the data object.
   */  
  public void accept(Visitor visitor) {
  	visitor.setDepth(visitor.getDepth() + 1);
    visitor.visit(this);
    if (role != null) {
      visitor.visit(role); 
    }
  	visitor.setDepth(visitor.getDepth() - 1);
  }
  
  /** 
   * Creates and returns a deep copy of the object and its descendants. The 
   * properties containing keys are not cloned because they are immutable.
   * @return a deep copy of the instance.
   */
  public DataObject copy() {
    UserRole clone = null;
    try {
      clone = (UserRole)clone();
    } catch (CloneNotSupportedException e) {
      // impossible exception because data object is cloneable.
      return null;
    }
    return clone;
  }

  public void addPropertyChangeListener(PropertyChangeListener listener) {
    throw new UnsupportedOperationException();
  }
  
  public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener) {
    throw new UnsupportedOperationException();
  }
  
  public void removePropertyChangeListener(PropertyChangeListener listener) {
    throw new UnsupportedOperationException();
  }
  
  public void removePropertyChangeListener(String propertyName, PropertyChangeListener listener) {
    throw new UnsupportedOperationException();
  }
  
  public void addVetoableChangeListener(VetoableChangeListener listener) {
    throw new UnsupportedOperationException();
  }
  
  public void addVetoableChangeListener(String propertyName, VetoableChangeListener listener) {
    throw new UnsupportedOperationException();
  }
  
  public void removeVetoableChangeListener(VetoableChangeListener listener) {
    throw new UnsupportedOperationException();
  }
  
  public void removeVetoableChangeListener(String propertyName, VetoableChangeListener listener) {
    throw new UnsupportedOperationException();
  }
}
