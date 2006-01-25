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
 * A resource is a domain item which can be accessed
 * @version $Revision$
 * @author Marcel Baumann
 */
public class ResourceType extends NamedEntity implements DataObject {

/* %% USER DEFINED DECLARATION START %% */
/* %% USER DEFINED DECLARATION END %% */

  /**
   * Serial version identifier coded as back compatibility date.
   */
  private static final long serialVersionUID = 99990101;

  /**
   * The roles associated with a resource type.
   */
  private List<Role> roles;
  /**
   * Mapping for 1: N relationship resources as reverse foreign key.
   */
  private Integer domainResourcesHandle;

  /** 
   * Default constructor of the class necessary for Java bean convention. The 
   * object identifier is set to null to indicate it is a new object.
   */
  public ResourceType() {
    roles = new ArrayList<Role>();
  }

/* %% USER DEFINED CONSTRUCTORS START %% */
/* %% USER DEFINED CONSTRUCTORS END %% */

/* %% USER DEFINED CODE START %% */
/* %% USER DEFINED CODE END %% */


  /** 
   * Gets all elements of the relation roles.
   * @return List of all children.
   */
  public List<Role> getRoles() {
    return roles;
  }

  /** 
   * Returns the element at the specified position in the indexed property 
   * roles.
   * @param index index of element to return. 
   * @return the element at the specified position in this list.
   * @see #addRole(int, Role)
   * @see java.util.List#get(int)
   */
  public Role getRole(int index) {
    return roles.get(index);  
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
  public boolean addRoles(Collection<Role> c) {
    setModified(true);
    return roles.addAll(c);
  }  
  
  /** 
   * Inserts the given child at the select position in the relation roles.
   * @param index index at which the specified element is to be inserted.
   * @param element element to be inserted.
   * @see #getRole(int)
   * @see java.util.List#add(int, Object)
   */
  public void addRole(int index, Role element) {
    roles.add(index, element);
    setModified(true);
  }

  /** 
   * Appends the specified element to the end of this list in the relation 
   * roles.
   * @param element to be appended to this list.
   * @return true (as per the general contract of the Collection.add method).   
   * @see java.util.List#add(Object)
   * @pre element != null
   */
  public boolean addRole(Role element) {
    setModified(true);
    return roles.add(element);
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
  public boolean removeRole(Role element) {
    boolean removed = roles.remove(element);
    if (removed) {
      setModified(true);
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
  public ResourceType copy() {
    ResourceType clone = null;
    try {
      clone = (ResourceType)clone();
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
  protected void copy(ResourceType clone) {
    super.copy(clone);
    clone.roles = new ArrayList<Role>();
    for (int i = 0; i < roles.size(); i++) {
      clone.roles.add(roles.get(i).copy());
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
    for (Role item : getRoles()) {
      item.accept(visitor);
    }
  }  

/* %% USER DEFINED PROTECTED/PRIVATE CODE START %% */
/* %% USER DEFINED PROTECTED/PRIVATE CODE END %% */
}
