/* This code was generated automatically with the pmMDA generator. Please do 
   NOT add code outside the marked zones. All changes outside the marked areas 
   will be discarded during the next execution of the MDA generator. 
   $Id$
*/

package ch.bbv.codes.lw;

import ch.bbv.dog.LightweightObject;
import java.util.*;

/* %% USER DEFINED IMPORT START %% */
/* %% USER DEFINED IMPORT END   %% */

/**
 * The group contains a set of logically related reference code types.
 * @version $Revision$
 */
public class ReferenceCodeGroupLw implements LightweightObject {
  /**
   * The name of the reference code group.
   */
  private String name;
  /**
   * The description of the reference code group.
   */
  private String description;
  private List codes;
  private List groups;

  /** 
   * Unique identifier of the data object instance distinguishing it from 
   * all other instances of the same data object type.
   */
  private Integer id;

  /** 
   * Default constructor of the class used to create new lightweight data 
   * object instances.
   */
  public ReferenceCodeGroupLw() {
    this(null);
  }
     
  /** 
   * Constructor of the class.
   * @param id identifier of the object to which the proxy is associated.
   */
  public ReferenceCodeGroupLw(Integer id) {
    this.id = id;
  	codes = new ArrayList();
  	groups = new ArrayList();
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
   * Gets the current value of the attribute Description.
   * @return the value of the attribute Description.
   * @see #setDescription
   */
  public String getDescription() {
    return description;
  }

  /** 
   * Sets the new value of the simple property Description.
   * @param description new value of the property.
   * @see #getDescription
   */    
  public void setDescription(String description) {
    this.description = description;
  }

  /** 
   * Returns the unique identifier of the data object instance.
   * @return the identifier of the instance.
   */
  public Integer getId() {
  	return id;
  }

  /** 
   * Gets all elements of the relation codes.
   * @return List of all children.
   */
  public List getCodes() {
    return codes;
  }

  /** 
   * Returns the element at the specified position in the indexed property 
   * codes.
   * @param index index of element to return. 
   * @return the element at the specified position in this list.
   * @see java.util.List#get(int)
   */
  public ReferenceCodeDescriptorLw getCode(int index) {
    return (ReferenceCodeDescriptorLw)codes.get(index);  
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
  public boolean addCodes(Collection c) {
    return codes.addAll(c);
  }  
  
  /** 
   * Inserts the given child at the select position in the relation codes.
   * @param index index at which the specified element is to be inserted.
   * @param element element to be inserted.
   * @see java.util.List#add(int, Object)
   */
  public void addCode(int index, ReferenceCodeDescriptorLw element) {
    codes.add(index, element);
  }

  /** 
   * Appends the specified element to the end of this list in the relation 
   * Codes.
   * @param element to be appended to this list.
   * @return true (as per the general contract of the Collection.add method)   
   * @see java.util.List#add(Object)
   */
  public boolean addCode(ReferenceCodeDescriptorLw element) {
    return codes.add(element);
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
  public boolean removeCode(ReferenceCodeDescriptorLw element) {
    return codes.remove(element);
  }

  /** 
   * Gets all elements of the relation groups.
   * @return List of all children.
   */
  public List getGroups() {
    return groups;
  }

  /** 
   * Returns the element at the specified position in the indexed property 
   * groups.
   * @param index index of element to return. 
   * @return the element at the specified position in this list.
   * @see java.util.List#get(int)
   */
  public ReferenceCodeGroupLw getGroup(int index) {
    return (ReferenceCodeGroupLw)groups.get(index);  
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
  public boolean addGroups(Collection c) {
    return groups.addAll(c);
  }  
  
  /** 
   * Inserts the given child at the select position in the relation groups.
   * @param index index at which the specified element is to be inserted.
   * @param element element to be inserted.
   * @see java.util.List#add(int, Object)
   */
  public void addGroup(int index, ReferenceCodeGroupLw element) {
    groups.add(index, element);
  }

  /** 
   * Appends the specified element to the end of this list in the relation 
   * Groups.
   * @param element to be appended to this list.
   * @return true (as per the general contract of the Collection.add method)   
   * @see java.util.List#add(Object)
   */
  public boolean addGroup(ReferenceCodeGroupLw element) {
    return groups.add(element);
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
  public boolean removeGroup(ReferenceCodeGroupLw element) {
    return groups.remove(element);
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
    ReferenceCodeGroupLw clone = null;
    try {
      clone = (ReferenceCodeGroupLw)clone();
    } catch (CloneNotSupportedException e) {
      // impossible exception because data object is cloneable.
      return null;
    }
    clone.codes = new java.util.ArrayList();
    for (int i = 0; i < codes.size(); i++) {
      clone.codes.add(((LightweightObject)codes.get(i)).copy());
    }
    clone.groups = new java.util.ArrayList();
    for (int i = 0; i < groups.size(); i++) {
      clone.groups.add(((LightweightObject)groups.get(i)).copy());
    }
	return clone;
  }
}
