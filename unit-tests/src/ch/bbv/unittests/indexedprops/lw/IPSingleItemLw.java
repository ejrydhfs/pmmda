/* This code was generated automatically with the pmMDA generator. Please do 
   NOT add code outside the marked zones. All changes outside the marked areas 
   will be discarded during the next execution of the MDA generator. 
*/

package ch.bbv.unittests.indexedprops.lw;

import ch.bbv.dog.LightweightObject;
import java.util.*;

/* %% USER DEFINED IMPORT START %% */
/* %% USER DEFINED IMPORT END %% */

public class IPSingleItemLw  implements LightweightObject {

/* %% USER DEFINED DECLARATION START %% */
/* %% USER DEFINED DECLARATION END %% */

  /**
   * Serial version identifier coded as back compatibility date.
   */
  private static final long serialVersionUID = 99990101;

  private int ipSingleItemAttribute;

  private List<IPListItemLw> items;

  /** 
   * Unique identifier of the data object instance distinguishing it from 
   * all other instances of the same data object type.
   */
  private Integer id;

  /** 
   * Default constructor of the class used to create new lightweight data 
   * object instances.
   */
  public IPSingleItemLw() {
    this(null);
  }
     
  /** 
   * Constructor of the class.
   * @param id identifier of the object to which the proxy is associated.
   */
  public IPSingleItemLw(Integer id) {
    this.id = id;
    items = new ArrayList<IPListItemLw>();
  }

/* %% USER DEFINED CONSTRUCTORS START %% */
/* %% USER DEFINED CONSTRUCTORS END %% */

/* %% USER DEFINED CODE START %% */
/* %% USER DEFINED CODE END %% */


  /** 
   * Gets the current value of the attribute IpSingleItemAttribute.
   * @return the value of the attribute IpSingleItemAttribute.
   * @see #setIpSingleItemAttribute(int)
   */
  public int getIpSingleItemAttribute() {
    return ipSingleItemAttribute;
  }

  /** 
   * Sets the new value of the simple property IpSingleItemAttribute.
   * @param ipSingleItemAttribute new value of the property.
   * @see #getIpSingleItemAttribute()
   */    
  public void setIpSingleItemAttribute(int ipSingleItemAttribute) {
      this.ipSingleItemAttribute = ipSingleItemAttribute;
  }

  /** 
   * Returns the unique identifier of the data object instance.
   * @return the identifier of the instance.
   */
  public Integer getId() {
    return id;
  }

  /** 
   * Gets all elements of the relation items.
   * @return List of all children.
   */
  public List<IPListItemLw> getItems() {
    return items;
  }

  /** 
   * Returns the element at the specified position in the indexed property 
   * items.
   * @param index index of element to return. 
   * @return the element at the specified position in this list.
   * @see java.util.List#get(int)
   */
  public IPListItemLw getItem(int index) {
    return items.get(index);  
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
  public boolean addItems(Collection<IPListItemLw> c) {
    return items.addAll(c);
  }  
  
  /** 
   * Inserts the given child at the select position in the relation items.
   * @param index index at which the specified element is to be inserted.
   * @param element element to be inserted.
   * @see java.util.List#add(int, Object)
   */
  public void addItem(int index, IPListItemLw element) {
    items.add(index, element);
  }

  /** 
   * Appends the specified element to the end of this list in the relation 
   * Items.
   * @param element to be appended to this list.
   * @return true (as per the general contract of the Collection.add method)   
   * @see java.util.List#add(Object)
   */
  public boolean addItem(IPListItemLw element) {
    return items.add(element);
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
  public boolean removeItem(IPListItemLw element) {
    return items.remove(element);
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
  public IPSingleItemLw deepCopy() {
    IPSingleItemLw clone = null;
    try {
      clone = (IPSingleItemLw)clone();
    } catch (CloneNotSupportedException e) {
      // impossible exception because data object is cloneable.
      return null;
    }
    clone.items = new java.util.ArrayList<IPListItemLw>();
    for (int i = 0; i < items.size(); i++) {
      clone.items.add(items.get(i).deepCopy());
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
    for (IPListItemLw item : getItems()) {
      if ((item != null) && visitor.shouldVisit(item)) {
        item.accept(visitor);
	  }
    }
  }  
}
