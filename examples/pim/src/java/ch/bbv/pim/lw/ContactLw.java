/* This code was generated automatically with the pmMDA generator. Please do 
   NOT add code outside the marked zones. All changes outside the marked areas 
   will be discarded during the next execution of the MDA generator. 
*/

package ch.bbv.pim.lw;

import ch.bbv.dog.LightweightObject;

/* %% USER DEFINED IMPORT START %% */
/* %% USER DEFINED IMPORT END %% */

public class ContactLw  implements LightweightObject {

/* %% USER DEFINED DECLARATION START %% */
/* %% USER DEFINED DECLARATION END %% */

  /**
   * Serial version identifier coded as back compatibility date.
   */
  private static final long serialVersionUID = 99990101;

  private String firstname;

  private String lastname;

  private String middlename;

  private String workEmail;

  private PhoneNumberLw workNumber;

  /** 
   * Unique identifier of the data object instance distinguishing it from 
   * all other instances of the same data object type.
   */
  private Integer id;

  /** 
   * Default constructor of the class used to create new lightweight data 
   * object instances.
   */
  public ContactLw() {
    this(null);
  }
     
  /** 
   * Constructor of the class.
   * @param id identifier of the object to which the proxy is associated.
   */
  public ContactLw(Integer id) {
    this.id = id;
  }

/* %% USER DEFINED CONSTRUCTORS START %% */
/* %% USER DEFINED CONSTRUCTORS END %% */

/* %% USER DEFINED CODE START %% */
  /**
   * Returns the legible name of a contact. This name is the concatenation
   * of the lastname, firstname and middle nanme.
   * @return the full name of the contact 
   */
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

/* %% USER DEFINED CODE END %% */


  /** 
   * Gets the current value of the attribute Firstname.
   * @return the value of the attribute Firstname.
   * @see #setFirstname(String)
   */
  public String getFirstname() {
    return firstname;
  }

  /** 
   * Sets the new value of the simple property Firstname.
   * @param firstname new value of the property.
   * @see #getFirstname()
   */    
  public void setFirstname(String firstname) {
      this.firstname = firstname;
  }

  /** 
   * Gets the current value of the attribute Lastname.
   * @return the value of the attribute Lastname.
   * @see #setLastname(String)
   */
  public String getLastname() {
    return lastname;
  }

  /** 
   * Sets the new value of the simple property Lastname.
   * @param lastname new value of the property.
   * @see #getLastname()
   */    
  public void setLastname(String lastname) {
      this.lastname = lastname;
  }

  /** 
   * Gets the current value of the attribute Middlename.
   * @return the value of the attribute Middlename.
   * @see #setMiddlename(String)
   */
  public String getMiddlename() {
    return middlename;
  }

  /** 
   * Sets the new value of the simple property Middlename.
   * @param middlename new value of the property.
   * @see #getMiddlename()
   */    
  public void setMiddlename(String middlename) {
      this.middlename = middlename;
  }

  /** 
   * Gets the current value of the attribute WorkEmail.
   * @return the value of the attribute WorkEmail.
   * @see #setWorkEmail(String)
   */
  public String getWorkEmail() {
    return workEmail;
  }

  /** 
   * Sets the new value of the simple property WorkEmail.
   * @param workEmail new value of the property.
   * @see #getWorkEmail()
   */    
  public void setWorkEmail(String workEmail) {
      this.workEmail = workEmail;
  }

  /** 
   * Gets the current value of the attribute WorkNumber.
   * @return the value of the attribute WorkNumber.
   * @see #setWorkNumber(PhoneNumberLw)
   */
  public PhoneNumberLw getWorkNumber() {
    return workNumber;
  }

  /** 
   * Sets the new value of the simple property WorkNumber.
   * @param workNumber new value of the property.
   * @see #getWorkNumber()
   */    
  public void setWorkNumber(PhoneNumberLw workNumber) {
      this.workNumber = workNumber;
  }

  /** 
   * Returns the unique identifier of the data object instance.
   * @return the identifier of the instance.
   */
  public Integer getId() {
    return id;
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
  public ContactLw deepCopy() {
    ContactLw clone = null;
    try {
      clone = (ContactLw)clone();
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
    if (workNumber != null) {
      workNumber.accept(visitor);
    }
  }  
}
