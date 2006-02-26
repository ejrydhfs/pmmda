/* This code was generated automatically with the pmMDA generator. Please do 
   NOT add code outside the marked zones. All changes outside the marked areas 
   will be discarded during the next execution of the MDA generator. 
*/

package ch.bbv.dnm.users.lw;

import ch.bbv.dog.LightweightObject;
import java.util.*;

/* %% USER DEFINED IMPORT START %% */
/* %% USER DEFINED IMPORT END   %% */

/**
 * A unit is a logical group of resources inside a domain. In Internet 
 * terminology it is a sub address modeling e.g. a subsudiary of a company. A 
 * unit can have sub units.
 * @author Marcel Baumann
 */
public class UnitLw  extends NamedEntityLw implements LightweightObject {

/* %% USER DEFINED DECLARATION START %% */
/* %% USER DEFINED DECLARATION END %% */

  /**
   * Serial version identifier coded as back compatibility date.
   */
  private static final long serialVersionUID = 99990101;

  /**
   * List of users belonging to the unit.
   */
  private List<UserLw> users;

  /**
   * List of units part of the unit.
   */
  private List<UnitLw> units;

  /** 
   * Default constructor of the class used to create new lightweight data 
   * object instances.
   */
  public UnitLw() {
    this(null);
  }
     
  /** 
   * Constructor of the class.
   * @param id identifier of the object to which the proxy is associated.
   */
  public UnitLw(Integer id) {
    super(id);
    users = new ArrayList<UserLw>();
    units = new ArrayList<UnitLw>();
  }

  /** 
   * Gets all elements of the relation users.
   * @return List of all children.
   */
  public List<UserLw> getUsers() {
    return users;
  }

  /** 
   * Returns the element at the specified position in the indexed property 
   * users.
   * @param index index of element to return. 
   * @return the element at the specified position in this list.
   * @see java.util.List#get(int)
   */
  public UserLw getUser(int index) {
    return users.get(index);  
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
  public boolean addUsers(Collection<UserLw> c) {
    return users.addAll(c);
  }  
  
  /** 
   * Inserts the given child at the select position in the relation users.
   * @param index index at which the specified element is to be inserted.
   * @param element element to be inserted.
   * @see java.util.List#add(int, Object)
   */
  public void addUser(int index, UserLw element) {
    users.add(index, element);
  }

  /** 
   * Appends the specified element to the end of this list in the relation 
   * Users.
   * @param element to be appended to this list.
   * @return true (as per the general contract of the Collection.add method)   
   * @see java.util.List#add(Object)
   */
  public boolean addUser(UserLw element) {
    return users.add(element);
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
  public boolean removeUser(UserLw element) {
    return users.remove(element);
  }

  /** 
   * Gets all elements of the relation units.
   * @return List of all children.
   */
  public List<UnitLw> getUnits() {
    return units;
  }

  /** 
   * Returns the element at the specified position in the indexed property 
   * units.
   * @param index index of element to return. 
   * @return the element at the specified position in this list.
   * @see java.util.List#get(int)
   */
  public UnitLw getUnit(int index) {
    return units.get(index);  
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
  public boolean addUnits(Collection<UnitLw> c) {
    return units.addAll(c);
  }  
  
  /** 
   * Inserts the given child at the select position in the relation units.
   * @param index index at which the specified element is to be inserted.
   * @param element element to be inserted.
   * @see java.util.List#add(int, Object)
   */
  public void addUnit(int index, UnitLw element) {
    units.add(index, element);
  }

  /** 
   * Appends the specified element to the end of this list in the relation 
   * Units.
   * @param element to be appended to this list.
   * @return true (as per the general contract of the Collection.add method)   
   * @see java.util.List#add(Object)
   */
  public boolean addUnit(UnitLw element) {
    return units.add(element);
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
  public boolean removeUnit(UnitLw element) {
    return units.remove(element);
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
  public UnitLw deepCopy() {
    UnitLw clone = null;
    try {
      clone = (UnitLw)clone();
    } catch (CloneNotSupportedException e) {
      // impossible exception because data object is cloneable.
      return null;
    }
    clone.users = new java.util.ArrayList<UserLw>();
    for (int i = 0; i < users.size(); i++) {
      clone.users.add(users.get(i).deepCopy());
    }
    clone.units = new java.util.ArrayList<UnitLw>();
    for (int i = 0; i < units.size(); i++) {
      clone.units.add(units.get(i).deepCopy());
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
    for (UserLw item : getUsers()) {
      item.accept(visitor);
    }
    for (UnitLw item : getUnits()) {
      item.accept(visitor);
    }
  }  
}
