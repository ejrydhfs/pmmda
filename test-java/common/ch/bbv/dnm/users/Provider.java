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
 * The provider is the owner of a set of domains. It defines the policies and 
 * capabilities available in his domains. In our systems the provider is the 
 * commercial entities selling domains to various customers. In the Internet 
 * terminology a provider is often called ISP.
 * @version $Revision$
 * @author Marcel Baumann
 */
public class Provider extends NamedEntity implements DataObject {

/* %% USER DEFINED DECLARATION START %% */
/* %% USER DEFINED DECLARATION END %% */

  /**
   * Serial version identifier coded as back compatibility date.
   */
  private static final long serialVersionUID = 99990101;

  /**
   * Primary email address of the provider.
   */
  private java.lang.String email;

  /**
   * Long name of the provider used for correspondance and human interactions.
   */
  private java.lang.String longname;

  /**
   * Defines the level of trust the application has to the provider instance. 
   * Values range between 0 and 1.
   */
  private double trusted;

  /**
   * List of the domains the provider is in charge.
   */
  private List<Domain> domains;

  /** 
   * Default constructor of the class necessary for Java bean convention. The 
   * object identifier is set to null to indicate it is a new object.
   */
  public Provider() {
    domains = new ArrayList<Domain>();
  }

/* %% USER DEFINED CONSTRUCTORS START %% */
/* %% USER DEFINED CONSTRUCTORS END %% */

/* %% USER DEFINED CODE START %% */
/* %% USER DEFINED CODE END %% */


  /** 
   * Returns the current value of the simple property email.
   * @return the value of the simple property email.
   * @see #setEmail(java.lang.String)
   */
  public java.lang.String getEmail() {
    return email;
  }

  /** 
   * Sets the new value of the simple property email.
   * @param email new value of the property.
   * @see #getEmail()
   */    
  public void setEmail(java.lang.String email) {
    if (((this.email != null) && !this.email.equals(email)) || 
        ((this.email == null) && (email != null)))  {    
      this.email = email;
      setModified(true);
    }
  }    

  /** 
   * Returns the current value of the simple property longname.
   * @return the value of the simple property longname.
   * @see #setLongname(java.lang.String)
   */
  public java.lang.String getLongname() {
    return longname;
  }

  /** 
   * Sets the new value of the simple property longname.
   * @param longname new value of the property.
   * @see #getLongname()
   */    
  public void setLongname(java.lang.String longname) {
    if (((this.longname != null) && !this.longname.equals(longname)) || 
        ((this.longname == null) && (longname != null)))  {    
      this.longname = longname;
      setModified(true);
    }
  }    

  /** 
   * Returns the current value of the simple property trusted.
   * @return the value of the simple property trusted.
   * @see #setTrusted(double)
   */
  public double getTrusted() {
    return trusted;
  }

  /** 
   * Sets the new value of the simple property trusted.
   * @param trusted new value of the property.
   * @see #getTrusted()
   */    
  public void setTrusted(double trusted) {
    if (this.trusted != trusted) {  
      this.trusted = trusted;
      setModified(true);
    }
  }

  /** 
   * Gets all elements of the relation domains.
   * @return List of all children.
   */
  public List<Domain> getDomains() {
    return domains;
  }

  /** 
   * Returns the element at the specified position in the indexed property 
   * domains.
   * @param index index of element to return. 
   * @return the element at the specified position in this list.
   * @see #setDomain(int, Domain)
   * @see java.util.List#get(int)
   */
  public Domain getDomain(int index) {
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
  public boolean addDomains(Collection<Domain> c) {
    setModified(true);
    for(Domain item : c) {
      setForeignKey(item, "providerDomainsHandle", getId());
    }
    return domains.addAll(c);
  }  
  
  /** 
   * Inserts the given child at the select position in the relation domains.
   * @param index index at which the specified element is to be inserted.
   * @param element element to be inserted.
   * @see #getDomain(int)
   * @see java.util.List#add(int, Object)
   */
  public void setDomain(int index, Domain element) {
    setModified(true);
    setForeignKey(element, "providerDomainsHandle", getId());
    domains.add(index, element);
  }

  /** 
   * Appends the specified element to the end of this list in the relation 
   * domains.
   * @param element to be appended to this list.
   * @return true (as per the general contract of the Collection.add method).   
   * @see java.util.List#add(Object)
   * @pre element != null
   */
  public boolean addDomain(Domain element) {
    setModified(true);
    setForeignKey(element, "providerDomainsHandle", getId());
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
  public boolean removeDomain(Domain element) {
    boolean removed = domains.remove(element);
    if (removed) {
      setModified(true);
      setForeignKey(element, "providerDomainsHandle", getId());
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
  public Provider copy() {
    Provider clone = null;
    try {
      clone = (Provider)clone();
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
  protected void copy(Provider clone) {
    super.copy(clone);
    clone.domains = new ArrayList<Domain>();
    for (int i = 0; i < domains.size(); i++) {
      clone.domains.add(domains.get(i).copy());
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
    for (Domain item : getDomains()) {
      item.accept(visitor);
    }
  }  

/* %% USER DEFINED PROTECTED/PRIVATE CODE START %% */
/* %% USER DEFINED PROTECTED/PRIVATE CODE END %% */
}
