/* This code was generated automatically with the pmMDA generator. Please do 
   NOT add code outside the marked zones. All changes outside the marked areas 
   will be discarded during the next execution of the MDA generator. 
   $Id$
*/

package ch.bbv.dnm.users.lw;

import ch.bbv.dog.LightweightObject;
import java.util.Date;

/* %% USER DEFINED IMPORT START %% */
/* %% USER DEFINED IMPORT END   %% */

/**
 * A user is an entity who can connect to the system. All operations he 
 * performs can be logged.
 * @version $Revision$
 */
public class UserLw implements LightweightObject {
  /**
   * The human readable name of the entity. The name should be unique in the 
   * context it is used to simplify navigation for users.
   */
  private String login;
  private boolean active;
  private String name;
  /**
   * The end date until when the user account is active. If the value is null 
   * the account has no termination date.
   */
  private Date validTo;
  /**
   * The start date from when the user account is active.
   */
  private Date validFrom;

  /** 
   * Unique identifier of the data object instance distinguishing it from 
   * all other instances of the same data object type.
   */
  private Integer id;

  /** 
   * Default constructor of the class used to create new lightweight data 
   * object instances.
   */
  public UserLw() {
    this(null);
  }
     
  /** 
   * Constructor of the class.
   * @param id identifier of the object to which the proxy is associated.
   */
  public UserLw(Integer id) {
    this.id = id;
  }

  /** 
   * Gets the current value of the attribute Login.
   * @return the value of the attribute Login.
   * @see #setLogin
   */
  public String getLogin() {
    return login;
  }

  /** 
   * Sets the new value of the simple property Login.
   * @param login new value of the property.
   * @see #getLogin
   */    
  public void setLogin(String login) {
    this.login = login;
  }

  /** 
   * Gets the current value of the attribute Active.
   * @return the value of the attribute Active.
   * @see #setActive
   */
  public boolean getActive() {
    return active;
  }

  /** 
   * Sets the new value of the simple property Active.
   * @param active new value of the property.
   * @see #getActive
   */    
  public void setActive(boolean active) {
    this.active = active;
  }

  /** 
   * Gets the current value of the attribute Name.
   * @return the value of the attribute Name.
   * @see #setName
   */
  public String getName() {
    return name;
  }

  /** 
   * Sets the new value of the simple property Name.
   * @param name new value of the property.
   * @see #getName
   */    
  public void setName(String name) {
    this.name = name;
  }

  /** 
   * Gets the current value of the attribute ValidTo.
   * @return the value of the attribute ValidTo.
   * @see #setValidTo
   */
  public Date getValidTo() {
    return validTo;
  }

  /** 
   * Sets the new value of the simple property ValidTo.
   * @param validTo new value of the property.
   * @see #getValidTo
   */    
  public void setValidTo(Date validTo) {
    this.validTo = validTo;
  }

  /** 
   * Gets the current value of the attribute ValidFrom.
   * @return the value of the attribute ValidFrom.
   * @see #setValidFrom
   */
  public Date getValidFrom() {
    return validFrom;
  }

  /** 
   * Sets the new value of the simple property ValidFrom.
   * @param validFrom new value of the property.
   * @see #getValidFrom
   */    
  public void setValidFrom(Date validFrom) {
    this.validFrom = validFrom;
  }

  /** 
   * Returns the unique identifier of the data object instance.
   * @return the identifier of the instance.
   */
  public Integer getId() {
  	return id;
  }

  /** 
   * Implements the visitor pattern in the data object class. The accept
   * method dispatches the visitor to all its relevant children. A relevant
   * child is always another data object.
   * @param visitor visitor to act on the data object.
   */  
  public void accept(LwVisitor visitor) {
  	visitor.setDepth(visitor.getDepth() + 1);
    visitor.visit(this);
    visitor.setDepth(visitor.getDepth() - 1);
  }

  /** 
   * Creates and returns a deep copy of the object and its descendants. The 
   * properties containing keys are not cloned because they are immutable.
   * @return a deep copy of the instance.
   */
  public LightweightObject copy() {
    UserLw clone = null;
    try {
      clone = (UserLw)clone();
    } catch (CloneNotSupportedException e) {
      // impossible exception because data object is cloneable.
      return null;
    }
	return clone;
  }
}
