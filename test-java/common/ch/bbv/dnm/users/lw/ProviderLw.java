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
 * The provider is the owner of a set of domains. It defines the policies and 
 * capabilities available in his domains. In our systems the provider is the 
 * commercial entities selling domains to various customers. In the Internet 
 * terminology a provider is often called ISP.
 * @version $Revision$
 * @author MarcelBaumann
 */
public class ProviderLw implements LightweightObject {

/* %% USER DEFINED DECLARATION START %% */
/* %% USER DEFINED DECLARATION END %% */

  /**
   * Serial version identifier coded as back compatibility date.
   */
  private static final long serialVersionUID = 99990101;

  /**
   * The human readable name of the entity. The name should be unique in the 
   * context it is used to simplify navigation for users.
   */
  private String name;

  /**
   * List of the domains the provider is in charge.
   */
  private List<DomainLw> domains;

  /** 
   * Unique identifier of the data object instance distinguishing it from 
   * all other instances of the same data object type.
   */
  private Integer id;

  /** 
   * Default constructor of the class used to create new lightweight data 
   * object instances.
   */
  public ProviderLw() {
    this(null);
  }
     
  /** 
   * Constructor of the class.
   * @param id identifier of the object to which the proxy is associated.
   */
  public ProviderLw(Integer id) {
    this.id = id;
  	domains = new ArrayList<DomainLw>();
  }

  /** 
   * Gets the current value of the attribute Name.
   * @return the value of the attribute Name.
   * @see #setName(String)
   */
  public String getName() {
    return name;
  }

  /** 
   * Sets the new value of the simple property Name.
   * @param name new value of the property.
   * @see #getName()
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
   * Gets all elements of the relation domains.
   * @return List of all children.
   */
  public List<DomainLw> getDomains() {
    return domains;
  }

  /** 
   * Returns the element at the specified position in the indexed property 
   * domains.
   * @param index index of element to return. 
   * @return the element at the specified position in this list.
   * @see java.util.List#get(int)
   */
  public DomainLw getDomain(int index) {
    return domains.get(index);  
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
  public boolean addDomains(Collection<DomainLw> c) {
    return domains.addAll(c);
  }  
  
  /** 
   * Inserts the given child at the select position in the relation domains.
   * @param index index at which the specified element is to be inserted.
   * @param element element to be inserted.
   * @see java.util.List#add(int, Object)
   */
  public void addDomain(int index, DomainLw element) {
    domains.add(index, element);
  }

  /** 
   * Appends the specified element to the end of this list in the relation 
   * Domains.
   * @param element to be appended to this list.
   * @return true (as per the general contract of the Collection.add method)   
   * @see java.util.List#add(Object)
   */
  public boolean addDomain(DomainLw element) {
    return domains.add(element);
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
  public boolean removeDomain(DomainLw element) {
    return domains.remove(element);
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
    for (DomainLw item : domains) {
      item.accept(visitor);
    }
    visitor.setDepth(visitor.getDepth() - 1);
  }

  /** 
   * Creates and returns a deep copy of the object and its descendants. The 
   * properties containing keys are not cloned because they are immutable.
   * @return a deep copy of the instance.
   */
  public ProviderLw deepCopy() {
    ProviderLw clone = null;
    try {
      clone = (ProviderLw)clone();
    } catch (CloneNotSupportedException e) {
      // impossible exception because data object is cloneable.
      return null;
    }
    clone.domains = new java.util.ArrayList<DomainLw>();
    for (int i = 0; i < domains.size(); i++) {
      clone.domains.add(domains.get(i).deepCopy());
    }
	return clone;
  }
}
