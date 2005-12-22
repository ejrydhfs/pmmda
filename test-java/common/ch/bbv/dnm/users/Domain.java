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
 * A domain is a set of resources independant from all other domains. In 
 * Internet terminology a domain is a Internet domain identified through its 
 * name.
 * @version $Revision$
 * @author Marcel Baumann
 */
public class Domain extends NamedEntity implements DataObject {

/* %% USER DEFINED DECLARATION START %% */
/* %% USER DEFINED DECLARATION END %% */

  /**
   * Serial version identifier coded as back compatibility date.
   */
  private static final long serialVersionUID = 99990101;

  /**
   * Long name of the domain used for correspondance and human interactions.
   */
  private String longname;

  /**
   * The administrator of a domain is the user responsible for it and 
   * optionally some of its subdomains.
   */
  private User administrator;
  /**
   * Mapping for 1: 1 relationship ${relation.getName()} as foreign key.
   */
  private Integer administratorHandle;  

  /**
   * List of units member of the domain.
   */
  private List<Unit> units;

  /**
   * The resources available in the domain.
   */
  private List<ResourceType> resources;
  /**
   * Mapping for 1: N relationship domains as reverse foreign key.
   */
  private Integer providerDomainsHandle;

  /** 
   * Default constructor of the class necessary for Java bean convention. The 
   * object identifier is set to null to indicate it is a new object.
   */
  public Domain() {
    units = new ArrayList<Unit>();
    resources = new ArrayList<ResourceType>();
  }

/* %% USER DEFINED CONSTRUCTORS START %% */
/* %% USER DEFINED CONSTRUCTORS END %% */

/* %% USER DEFINED CODE START %% */
/* %% USER DEFINED CODE END %% */


  /** 
   * Returns the current value of the simple property longname.
   * @return the value of the simple property longname.
   * @see #setLongname(String)
   */
  public String getLongname() {
    return longname;
  }

  /** 
   * Sets the new value of the simple property longname.
   * @param longname new value of the property.
   * @see #getLongname()
   */    
  public void setLongname(String longname) {
    if (((this.longname != null) && !this.longname.equals(longname)) || 
        ((this.longname == null) && (longname != null)))  {    
      this.longname = longname;
      setModified(true);
    }
  }    

  /** 
   * Returns the current value of the simple property administrator.
   * @return the value of the simple property administrator.
   * @see #setAdministrator(User)
   */
  public User getAdministrator() {
    return administrator;
  }

  /** 
   * Sets the new value of the simple property administrator.
   * @param administrator new value of the property.
   * @see #getAdministrator()
   */    
  public void setAdministrator(User administrator) {
    if (((this.administrator != null) && !this.administrator.equals(administrator)) || 
        ((this.administrator == null) && (administrator != null)))  {    
      this.administrator = administrator;
      administratorHandle = (administrator != null ? administrator.getId() : null);
      setModified(true);
    }
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
      setForeignKey(item, "domainUnitsHandle", getId());
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
    setForeignKey(element, "domainUnitsHandle", getId());
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
      setForeignKey(element, "domainUnitsHandle", null);
    }
    return removed;
  }

  /** 
   * Gets all elements of the relation resources.
   * @return List of all children.
   */
  public List<ResourceType> getResources() {
    return resources;
  }

  /** 
   * Returns the element at the specified position in the indexed property 
   * resources.
   * @param index index of element to return. 
   * @return the element at the specified position in this list.
   * @see #addResource(int, ResourceType)
   * @see java.util.List#get(int)
   */
  public ResourceType getResource(int index) {
    return resources.get(index);  
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
  public boolean addResources(Collection<ResourceType> c) {
    setModified(true);
    return resources.addAll(c);
  }  
  
  /** 
   * Inserts the given child at the select position in the relation resources.
   * @param index index at which the specified element is to be inserted.
   * @param element element to be inserted.
   * @see #getResource(int)
   * @see java.util.List#add(int, Object)
   */
  public void addResource(int index, ResourceType element) {
    resources.add(index, element);
    setModified(true);
  }

  /** 
   * Appends the specified element to the end of this list in the relation 
   * resources.
   * @param element to be appended to this list.
   * @return true (as per the general contract of the Collection.add method).   
   * @see java.util.List#add(Object)
   * @pre element != null
   */
  public boolean addResource(ResourceType element) {
    return resources.add(element);
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
  public boolean removeResource(ResourceType element) {
    boolean removed = resources.remove(element);
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
  public Domain copy() {
    Domain clone = null;
    try {
      clone = (Domain)clone();
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
  protected void copy(Domain clone) {
    super.copy(clone);
    if (administrator != null) {
      clone.administrator = administrator.copy();
    }
    clone.units = new ArrayList<Unit>();
    for (int i = 0; i < units.size(); i++) {
      clone.units.add(units.get(i).copy());
    }
    clone.resources = new ArrayList<ResourceType>();
    for (int i = 0; i < resources.size(); i++) {
      clone.resources.add(resources.get(i).copy());
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
    if (administrator != null) {
      administrator.accept(visitor);
    }
    for (Unit item : getUnits()) {
      item.accept(visitor);
    }
    for (ResourceType item : getResources()) {
      item.accept(visitor);
    }
  }  

/* %% USER DEFINED PROTECTED/PRIVATE CODE START %% */
/* %% USER DEFINED PROTECTED/PRIVATE CODE END %% */
}
