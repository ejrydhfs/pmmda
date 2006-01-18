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
 * A unit is a logical group of resources inside a domain. In Internet 
 * terminology it is a sub address modeling e.g. a subsudiary of a company. A 
 * unit can have sub units.
 * @version $Revision$
 * @author Marcel Baumann
 */
public class Unit extends NamedEntity implements DataObject {

/* %% USER DEFINED DECLARATION START %% */
/* %% USER DEFINED DECLARATION END %% */

  /**
   * Serial version identifier coded as back compatibility date.
   */
  private static final long serialVersionUID = 99990101;

  /**
   * List of users belonging to the unit.
   */
  private List<User> users;

  /**
   * List of units part of the unit.
   */
  private List<Unit> units;
  /**
   * Mapping for 1: N relationship units as reverse foreign key.
   */
  private Integer unitUnitsHandle;
  /**
   * Mapping for 1: N relationship units as reverse foreign key.
   */
  private Integer domainUnitsHandle;

  /** 
   * Default constructor of the class necessary for Java bean convention. The 
   * object identifier is set to null to indicate it is a new object.
   */
  public Unit() {
    users = new ArrayList<User>();
    units = new ArrayList<Unit>();
  }

/* %% USER DEFINED CONSTRUCTORS START %% */
/* %% USER DEFINED CONSTRUCTORS END %% */

/* %% USER DEFINED CODE START %% */
/* %% USER DEFINED CODE END %% */


  /** 
   * Gets all elements of the relation users.
   * @return List of all children.
   */
  public List<User> getUsers() {
    return users;
  }

  /** 
   * Returns the element at the specified position in the indexed property 
   * users.
   * @param index index of element to return. 
   * @return the element at the specified position in this list.
   * @see #addUser(int, User)
   * @see java.util.List#get(int)
   */
  public User getUser(int index) {
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
  public boolean addUsers(Collection<User> c) {
    setModified(true);
    for(User item : c) {
      setForeignKey(item, "unitUsersHandle", getId());
    }
    return users.addAll(c);
  }  
  
  /** 
   * Inserts the given child at the select position in the relation users.
   * @param index index at which the specified element is to be inserted.
   * @param element element to be inserted.
   * @see #getUser(int)
   * @see java.util.List#add(int, Object)
   */
  public void addUser(int index, User element) {
    users.add(index, element);
    setModified(true);
    setForeignKey(element, "unitUsersHandle", getId());
  }

  /** 
   * Appends the specified element to the end of this list in the relation 
   * users.
   * @param element to be appended to this list.
   * @return true (as per the general contract of the Collection.add method).   
   * @see java.util.List#add(Object)
   * @pre element != null
   */
  public boolean addUser(User element) {
    return users.add(element);
  }
  
  /** 
   * Removes the first occurrence in this list of the specified element. If 
   * the list does not contain the element, it is unchanged. More formally, 
   * removes the element with the lowest index i such that 
   * (o==null ? get(i)==null : o.equals(get(i))) (if such an element exists). 
   * @param element element to be removed from this list, if present. 
   * @return true if this list contained the specified element. 
   * @see java.util.List#remove(Object)
   */
  public boolean removeUser(User element) {
    boolean removed = users.remove(element);
    if (removed) {
      setModified(true);
      setForeignKey(element, "unitUsersHandle", null);
    }
    return removed;
  }

  /** 
   * Gets all elements of the relation units.
   * @return List of all children.
   */
  public List<Unit> getUnits() {
    return units;
  }

  /** 
   * Returns the element at the specified position in the indexed property 
   * units.
   * @param index index of element to return. 
   * @return the element at the specified position in this list.
   * @see #addUnit(int, Unit)
   * @see java.util.List#get(int)
   */
  public Unit getUnit(int index) {
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
  public boolean addUnits(Collection<Unit> c) {
    setModified(true);
    for(Unit item : c) {
      setForeignKey(item, "unitUnitsHandle", getId());
    }
    return units.addAll(c);
  }  
  
  /** 
   * Inserts the given child at the select position in the relation units.
   * @param index index at which the specified element is to be inserted.
   * @param element element to be inserted.
   * @see #getUnit(int)
   * @see java.util.List#add(int, Object)
   */
  public void addUnit(int index, Unit element) {
    units.add(index, element);
    setModified(true);
    setForeignKey(element, "unitUnitsHandle", getId());
  }

  /** 
   * Appends the specified element to the end of this list in the relation 
   * units.
   * @param element to be appended to this list.
   * @return true (as per the general contract of the Collection.add method).   
   * @see java.util.List#add(Object)
   * @pre element != null
   */
  public boolean addUnit(Unit element) {
    return units.add(element);
  }
  
  /** 
   * Removes the first occurrence in this list of the specified element. If 
   * the list does not contain the element, it is unchanged. More formally, 
   * removes the element with the lowest index i such that 
   * (o==null ? get(i)==null : o.equals(get(i))) (if such an element exists). 
   * @param element element to be removed from this list, if present. 
   * @return true if this list contained the specified element. 
   * @see java.util.List#remove(Object)
   */
  public boolean removeUnit(Unit element) {
    boolean removed = units.remove(element);
    if (removed) {
      setModified(true);
      setForeignKey(element, "unitUnitsHandle", null);
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
  public Unit copy() {
    Unit clone = null;
    try {
      clone = (Unit)clone();
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
  protected void copy(Unit clone) {
    super.copy(clone);
    clone.users = new ArrayList<User>();
    for (int i = 0; i < users.size(); i++) {
      clone.users.add(users.get(i).copy());
    }
    clone.units = new ArrayList<Unit>();
    for (int i = 0; i < units.size(); i++) {
      clone.units.add(units.get(i).copy());
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
    for (User item : getUsers()) {
      item.accept(visitor);
    }
    for (Unit item : getUnits()) {
      item.accept(visitor);
    }
  }  

/* %% USER DEFINED PROTECTED/PRIVATE CODE START %% */
/* %% USER DEFINED PROTECTED/PRIVATE CODE END %% */
}
