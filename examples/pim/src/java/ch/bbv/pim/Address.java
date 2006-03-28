/* This code was generated automatically with the pmMDA generator. Please do 
   NOT add code outside the marked zones. All changes outside the marked areas 
   will be discarded during the next execution of the MDA generator. 
*/

package ch.bbv.pim;

import java.beans.PropertyChangeListener;
import java.beans.VetoableChangeListener;
import java.lang.reflect.Field;
import java.sql.Timestamp;

import ch.bbv.dog.DataObject;

/* %% USER DEFINED IMPORT START %% */
/* %% USER DEFINED IMPORT END %% */

public class Address implements DataObject {

/* %% USER DEFINED DECLARATION START %% */
/* %% USER DEFINED DECLARATION END %% */

  /**
   * Serial version identifier coded as back compatibility date.
   */
  private static final long serialVersionUID = 99990101;

  private String street;

  private String poBox;

  private String city;

  private String state;

  private String postalCode;

  private Country country;

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
  public Address() {
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
   * Returns the current value of the simple property street.
   * @return the value of the simple property street.
   * @see #setStreet(String)
   */
  public String getStreet() {
    return street;
  }

  /** 
   * Sets the new value of the simple property street.
   * @param street new value of the property
   * @see #getStreet()
   */    
  public void setStreet(String street) {
    if (((this.street != null) && !this.street.equals(street)) || 
        ((this.street == null) && (street != null)))  {    
      this.street = street;
      setModified(true);
    }
  }    

  /** 
   * Returns the current value of the simple property poBox.
   * @return the value of the simple property poBox.
   * @see #setPoBox(String)
   */
  public String getPoBox() {
    return poBox;
  }

  /** 
   * Sets the new value of the simple property poBox.
   * @param poBox new value of the property
   * @see #getPoBox()
   */    
  public void setPoBox(String poBox) {
    if (((this.poBox != null) && !this.poBox.equals(poBox)) || 
        ((this.poBox == null) && (poBox != null)))  {    
      this.poBox = poBox;
      setModified(true);
    }
  }    

  /** 
   * Returns the current value of the simple property city.
   * @return the value of the simple property city.
   * @see #setCity(String)
   */
  public String getCity() {
    return city;
  }

  /** 
   * Sets the new value of the simple property city.
   * @param city new value of the property
   * @see #getCity()
   */    
  public void setCity(String city) {
    if (((this.city != null) && !this.city.equals(city)) || 
        ((this.city == null) && (city != null)))  {    
      this.city = city;
      setModified(true);
    }
  }    

  /** 
   * Returns the current value of the simple property state.
   * @return the value of the simple property state.
   * @see #setState(String)
   */
  public String getState() {
    return state;
  }

  /** 
   * Sets the new value of the simple property state.
   * @param state new value of the property
   * @see #getState()
   */    
  public void setState(String state) {
    if (((this.state != null) && !this.state.equals(state)) || 
        ((this.state == null) && (state != null)))  {    
      this.state = state;
      setModified(true);
    }
  }    

  /** 
   * Returns the current value of the simple property postalCode.
   * @return the value of the simple property postalCode.
   * @see #setPostalCode(String)
   */
  public String getPostalCode() {
    return postalCode;
  }

  /** 
   * Sets the new value of the simple property postalCode.
   * @param postalCode new value of the property
   * @see #getPostalCode()
   */    
  public void setPostalCode(String postalCode) {
    if (((this.postalCode != null) && !this.postalCode.equals(postalCode)) || 
        ((this.postalCode == null) && (postalCode != null)))  {    
      this.postalCode = postalCode;
      setModified(true);
    }
  }    

  /** 
   * Returns the current value of the simple property country.
   * @return the value of the simple property country.
   * @see #setCountry(Country)
   */
  public Country getCountry() {
    return country;
  }

  /** 
   * Sets the new value of the simple property country.
   * @param country new value of the property
   * @see #getCountry()
   */    
  public void setCountry(Country country) {
    if (((this.country != null) && !this.country.equals(country)) || 
        ((this.country == null) && (country != null)))  {    
      this.country = country;
      setModified(true);
    }
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
  public Address copy() {
    Address clone = null;
    try {
      clone = (Address)clone();
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
  protected void copy(Address clone) {
  }

  /** 
   * Implements the visitor pattern in the data object class. The accept
   * method dispatches the visitor to all relevant children of the ancestors
   * and its children. A relevant child is always another data object.
   * @param visitor visitor to act on the data object
   */  
  protected void traverse(Visitor visitor) {
  }  

/* %% USER DEFINED PROTECTED/PRIVATE CODE START %% */
/* %% USER DEFINED PROTECTED/PRIVATE CODE END %% */
}
