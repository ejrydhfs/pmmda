/* This code was generated automatically with the pmMDA generator. Please do 
   NOT add code outside the marked zones. All changes outside the marked areas 
   will be discarded during the next execution of the MDA generator. 
   $Id$
*/

package ch.bbv.dnm.users.lw;

import ch.bbv.dog.LightweightObject;
import java.util.*;

/* %% USER DEFINED IMPORT START %% */
/* %% USER DEFINED IMPORT END   %% */

/**
 * A domain is a set of resources independant from all other domains. In 
 * Internet terminology a domain is a Internet domain identified through its 
 * name.
 * @version $Revision$
 */
public class DomainLw implements LightweightObject {
  /**
   * The human readable name of the entity. The name should be unique in the 
   * context it is used to simplify navigation for users.
   */
  private String name;
  private List units;

  /** 
   * Unique identifier of the data object instance distinguishing it from 
   * all other instances of the same data object type.
   */
  private Integer id;

  /** 
   * Default constructor of the class used to create new lightweight data 
   * object instances.
   */
  public DomainLw() {
    this(null);
  }
     
  /** 
   * Constructor of the class.
   * @param id identifier of the object to which the proxy is associated.
   */
  public DomainLw(Integer id) {
    this.id = id;
  	units = new ArrayList();
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
   * Returns the unique identifier of the data object instance.
   * @return the identifier of the instance.
   */
  public Integer getId() {
  	return id;
  }

  /** 
   * Gets all elements of the relation units.
   * @return List of all children.
   */
  public List getUnits() {
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
    return (UnitLw)units.get(index);  
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
  public boolean addUnits(Collection c) {
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
    DomainLw clone = null;
    try {
      clone = (DomainLw)clone();
    } catch (CloneNotSupportedException e) {
      // impossible exception because data object is cloneable.
      return null;
    }
    clone.units = new java.util.ArrayList();
    for (int i = 0; i < units.size(); i++) {
      clone.units.add(((LightweightObject)units.get(i)).copy());
    }
	return clone;
  }
}
