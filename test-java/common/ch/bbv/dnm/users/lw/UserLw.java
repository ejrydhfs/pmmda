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
 * @author Marcel Baumann
 */
public class UserLw  extends NamedEntityLw implements LightweightObject {

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
    super(id);
  }

  /** 
   * Gets the current value of the attribute Login.
   * @return the value of the attribute Login.
   * @see #setLogin(String)
   */
  public String getLogin() {
    return login;
  }

  /** 
   * Sets the new value of the simple property Login.
   * @param login new value of the property.
   * @see #getLogin()
   */    
  public void setLogin(String login) {
    this.login = login;
  }

  /** 
   * Gets the current value of the attribute Active.
   * @return the value of the attribute Active.
   * @see #setActive(boolean)
   */
  public boolean isActive() {
    return active;
  }

  /** 
   * Sets the new value of the simple property Active.
   * @param active new value of the property.
   * @see #isActive()
   */    
  public void setActive(boolean active) {
    this.active = active;
  }

  /** 
   * Gets the current value of the attribute ValidTo.
   * @return the value of the attribute ValidTo.
   * @see #setValidTo(Date)
   */
  public Date getValidTo() {
    return validTo;
  }

  /** 
   * Sets the new value of the simple property ValidTo.
   * @param validTo new value of the property.
   * @see #getValidTo()
   */    
  public void setValidTo(Date validTo) {
    this.validTo = validTo;
  }

  /** 
   * Gets the current value of the attribute ValidFrom.
   * @return the value of the attribute ValidFrom.
   * @see #setValidFrom(Date)
   */
  public Date getValidFrom() {
    return validFrom;
  }

  /** 
   * Sets the new value of the simple property ValidFrom.
   * @param validFrom new value of the property.
   * @see #getValidFrom()
   */    
  public void setValidFrom(Date validFrom) {
    this.validFrom = validFrom;
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
  public UserLw deepCopy() {
    UserLw clone = null;
    try {
      clone = (UserLw)clone();
    } catch (CloneNotSupportedException e) {
      // impossible exception because data object is cloneable.
      return null;
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
    super.traverse(visitor);
  }  
}
