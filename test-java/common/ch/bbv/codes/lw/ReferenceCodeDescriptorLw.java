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
 * The reference code descriptor defines a set of reference code values. 
 * Structural information such as the target class, the  persistency are 
 * defined in it.
 * @version $Revision$
 */
public class ReferenceCodeDescriptorLw implements LightweightObject {

/* %% USER DEFINED DECLARATION START %% */
/* %% USER DEFINED DECLARATION END %% */

  /**
   * Serial version identifier coded as back compatibility date.
   */
  private static final long serialVersionUID = 99990101;

  /**
   * The reference code descriptor defines a set of reference code values. 
   * Structural information such as the target class, the  persistency are 
   * defined in it.
   */
  private String name;

  private boolean hierarchical;

  private List<ValueDescriptorLw> values;

  /** 
   * Unique identifier of the data object instance distinguishing it from 
   * all other instances of the same data object type.
   */
  private Integer id;

  /** 
   * Default constructor of the class used to create new lightweight data 
   * object instances.
   */
  public ReferenceCodeDescriptorLw() {
    this(null);
  }
     
  /** 
   * Constructor of the class.
   * @param id identifier of the object to which the proxy is associated.
   */
  public ReferenceCodeDescriptorLw(Integer id) {
    this.id = id;
  	values = new ArrayList<ValueDescriptorLw>();
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
   * Gets the current value of the attribute Hierarchical.
   * @return the value of the attribute Hierarchical.
   * @see #setHierarchical
   */
  public boolean getHierarchical() {
    return hierarchical;
  }

  /** 
   * Sets the new value of the simple property Hierarchical.
   * @param hierarchical new value of the property.
   * @see #getHierarchical
   */    
  public void setHierarchical(boolean hierarchical) {
    this.hierarchical = hierarchical;
  }

  /** 
   * Returns the unique identifier of the data object instance.
   * @return the identifier of the instance.
   */
  public Integer getId() {
  	return id;
  }

  /** 
   * Gets all elements of the relation values.
   * @return List of all children.
   */
  public List<ValueDescriptorLw> getValues() {
    return values;
  }

  /** 
   * Returns the element at the specified position in the indexed property 
   * values.
   * @param index index of element to return. 
   * @return the element at the specified position in this list.
   * @see java.util.List#get(int)
   */
  public ValueDescriptorLw getValue(int index) {
    return values.get(index);  
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
  public boolean addValues(Collection<ValueDescriptorLw> c) {
    return values.addAll(c);
  }  
  
  /** 
   * Inserts the given child at the select position in the relation values.
   * @param index index at which the specified element is to be inserted.
   * @param element element to be inserted.
   * @see java.util.List#add(int, Object)
   */
  public void addValue(int index, ValueDescriptorLw element) {
    values.add(index, element);
  }

  /** 
   * Appends the specified element to the end of this list in the relation 
   * Values.
   * @param element to be appended to this list.
   * @return true (as per the general contract of the Collection.add method)   
   * @see java.util.List#add(Object)
   */
  public boolean addValue(ValueDescriptorLw element) {
    return values.add(element);
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
  public boolean removeValue(ValueDescriptorLw element) {
    return values.remove(element);
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
    for (ValueDescriptorLw item : values) {
      item.accept(visitor);
    }
    visitor.setDepth(visitor.getDepth() - 1);
  }

  /** 
   * Creates and returns a deep copy of the object and its descendants. The 
   * properties containing keys are not cloned because they are immutable.
   * @return a deep copy of the instance.
   */
  public LightweightObject deepCopy() {
    ReferenceCodeDescriptorLw clone = null;
    try {
      clone = (ReferenceCodeDescriptorLw)clone();
    } catch (CloneNotSupportedException e) {
      // impossible exception because data object is cloneable.
      return null;
    }
    clone.values = new java.util.ArrayList<ValueDescriptorLw>();
    for (int i = 0; i < values.size(); i++) {
      clone.values.add(((ValueDescriptorLw)(values.get(i)).deepCopy()));
    }
	return clone;
  }
}
