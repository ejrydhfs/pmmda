/* This code was generated automatically with the pmMDA generator. Please do 
   NOT add code outside the marked zones. All changes outside the marked areas 
   will be discarded during the next execution of the MDA generator. 
   $Id$
*/
   
package ch.bbv.codes;

import ch.bbv.dog.DataObject;
import java.beans.PropertyChangeListener;
import java.beans.VetoableChangeListener;
import java.sql.Timestamp;
import java.util.*;

/* %% USER DEFINED IMPORT START %% */
/* %% USER DEFINED IMPORT END   %% */

/**
 * The reference code descriptor defines a set of reference code values. 
 * Structural information such as the target class, the  persistency are 
 * defined in it.
 * @version $Revision$
 */
public class ReferenceCodeDescriptor implements DataObject {

/* %% USER DEFINED DECLARATION START %% */
/* %% USER DEFINED DECLARATION END %% */


  /**
   * The name of reference code descriptor has displayed in the user interface.
   */
  private String name;

  /**
   * The description of the reference code descriptor.
   */
  private String description;

  /**
   * The name of the Java class defining the reference code type.
   */
  private String codeClassName;

  /**
   * The java class defining the reference code type.
   */
  private Class codeClass;

  private Boolean hierarchical;

  private CodeKind kind;

  private List values;

  /** 
   * Unique identifier of the data object instance distinguishing it from all
   * other instances of the same data object type.
   */
  private Integer id;
   
  /** 
   * Flag indicated if the data object is dirty or not.
   */
  private boolean modified;
  
  /** 
   * Timestamp used to implement optimistic locking. 
   */
  private Timestamp timestamp;   

  /** 
   * Default constructor of the class necessary for Java bean convention. The 
   * object identifier is set to null to indicate it is a new object.
   */
  public ReferenceCodeDescriptor() {
  	values = new ArrayList();
  }

/* %% USER DEFINED CONSTRUCTORS START %% */
/* %% USER DEFINED CONSTRUCTORS END %% */

/* %% USER DEFINED CODE START %% */
/* %% USER DEFINED CODE END %% */

  /** 
   * Returns the unique identifier of the data object instance.
   * @return the identifier of the instance.
   */
  public Integer getId() {
  	return id;
  }
  
  /** 
   * Sets the identifier of the data object instance.
   * @param id new identifier of the instance.
   */
  public void setId(Integer id) {
    this.id = id;
  }

  /** 
   * Returns the flag indicating if the data object instance was modified.
   * @return true if the object was modified otherwise false.
   * @see #setModified
   */
  public boolean isModified() {
    return modified;
  }
  
  /** 
   * Sets the flag indicating if the data object instance was modified.
   * @param modified new value of the flag.
   * @see #isModified
   */
  public void setModified(boolean modified) {
    this.modified = modified;
  }
  
  /** 
   * Convenience method indicating if the instance is a new one or not.
   * @return true if the object is new otherwise false.
   */
  public boolean isNew()  {
    return (getId() == null);
  }
  
  /** 
   * Returns the timestamp of the data object instance.
   * @return the timestamp of the last persistent modification.
   */
  public Timestamp getTimestamp() {
    return timestamp;
  }
  
  /** 
   * Sets the timestamp of the data object instance.
   * @param timestamp timestamp of the instance.
   */
   public void setTimestamp(Timestamp timestamp) {
     this.timestamp = timestamp;
   }

  /** 
   * Gets the current value of the simple property Name.
   * @return the value of the simple property Name.
   * @see #setName
   */
  public String getName() {
    return name;
  }

  /** 
   * Sets the new value of the simple property name.
   * @param name new value of the property.
   * @see #getName
   */    
  public void setName(String name) {
    if (((this.name != null) && !this.name.equals(name)) || 
    	((this.name == null) && (name != null)))  {    
      this.name = name;
      setModified(true);
    }
  }

  /** 
   * Gets the current value of the simple property Description.
   * @return the value of the simple property Description.
   * @see #setDescription
   */
  public String getDescription() {
    return description;
  }

  /** 
   * Sets the new value of the simple property description.
   * @param description new value of the property.
   * @see #getDescription
   */    
  public void setDescription(String description) {
    if (((this.description != null) && !this.description.equals(description)) || 
    	((this.description == null) && (description != null)))  {    
      this.description = description;
      setModified(true);
    }
  }

  /** 
   * Gets the current value of the simple property CodeClassName.
   * @return the value of the simple property CodeClassName.
   * @see #setCodeClassName
   */
  public String getCodeClassName() {
    return codeClassName;
  }

  /** 
   * Sets the new value of the simple property codeClassName.
   * @param codeClassName new value of the property.
   * @see #getCodeClassName
   */    
  public void setCodeClassName(String codeClassName) {
    if (((this.codeClassName != null) && !this.codeClassName.equals(codeClassName)) || 
    	((this.codeClassName == null) && (codeClassName != null)))  {    
      this.codeClassName = codeClassName;
      setModified(true);
    }
  }

  /** 
   * Gets the current value of the simple property CodeClass.
   * @return the value of the simple property CodeClass.
   * @see #setCodeClass
   */
  public Class getCodeClass() {
    return codeClass;
  }

  /** 
   * Sets the new value of the simple property codeClass.
   * @param codeClass new value of the property.
   * @see #getCodeClass
   */    
  public void setCodeClass(Class codeClass) {
    if (((this.codeClass != null) && !this.codeClass.equals(codeClass)) || 
    	((this.codeClass == null) && (codeClass != null)))  {    
      this.codeClass = codeClass;
      setModified(true);
    }
  }

  /** 
   * Gets the current value of the simple property Hierarchical.
   * @return the value of the simple property Hierarchical.
   * @see #setHierarchical
   */
  public Boolean getHierarchical() {
    return hierarchical;
  }

  /** 
   * Sets the new value of the simple property hierarchical.
   * @param hierarchical new value of the property.
   * @see #getHierarchical
   */    
  public void setHierarchical(Boolean hierarchical) {
    if (((this.hierarchical != null) && !this.hierarchical.equals(hierarchical)) || 
    	((this.hierarchical == null) && (hierarchical != null)))  {    
      this.hierarchical = hierarchical;
      setModified(true);
    }
  }

  /** 
   * Gets the current value of the simple property Kind.
   * @return the value of the simple property Kind.
   * @see #setKind
   */
  public CodeKind getKind() {
    return kind;
  }

  /** 
   * Sets the new value of the simple property kind.
   * @param kind new value of the property.
   * @see #getKind
   */    
  public void setKind(CodeKind kind) {
    if (((this.kind != null) && !this.kind.equals(kind)) || 
    	((this.kind == null) && (kind != null)))  {    
      this.kind = kind;
      setModified(true);
    }
  }

  /** 
   * Gets all elements of the relation values.
   * @return List of all children.
   */
  public List getValues() {
    return values;
  }

  /** 
   * Returns the element at the specified position in the indexed property 
   * values.
   * @param index index of element to return. 
   * @return the element at the specified position in this list.
   * @see java.util.List#get(int)
   */
  public ValueDescriptor getValue(int index) {
    return (ValueDescriptor)values.get(index);  
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
  public boolean addValues(Collection c) {
    setModified(true);
    return values.addAll(c);
  }  
  
  /** 
   * Inserts the given child at the select position in the relation values.
   * @param index index at which the specified element is to be inserted.
   * @param element element to be inserted.
   * @see java.util.List#add(int, Object)
   */
  public void setValue(int index, ValueDescriptor element) {
    setModified(true);
    values.add(index, element);
  }

  /** 
   * Appends the specified element to the end of this list in the relation 
   * values.
   * @param element to be appended to this list.
   * @return true (as per the general contract of the Collection.add method)   
   * @see java.util.List#add(Object)
   */
  public boolean addValue(ValueDescriptor element) {
    setModified(true);
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
  public boolean removeValue(ValueDescriptor element) {
    setModified(true);
    return values.remove(element);
  }

  /** 
   * Implements the visitor pattern in the data object class. The accept
   * method dispatches the visitor to all its relevant children. A relevant
   * child is always another data object.
   * @param visitor visitor to act on the data object.
   */  
  public void accept(Visitor visitor) {
  	visitor.setDepth(visitor.getDepth() + 1);
    visitor.visit(this);
    for (Iterator i = values.iterator(); i.hasNext();) {
      ((ValueDescriptor)i.next()).accept(visitor);
    }
  	visitor.setDepth(visitor.getDepth() - 1);
  }
  
  /** 
   * Creates and returns a deep copy of the object and its descendants. The 
   * properties containing keys are not cloned because they are immutable.
   * @return a deep copy of the instance.
   */
  public DataObject copy() {
    ReferenceCodeDescriptor clone = null;
    try {
      clone = (ReferenceCodeDescriptor)clone();
    } catch (CloneNotSupportedException e) {
      // impossible exception because data object is cloneable.
      return null;
    }
    clone.values = new java.util.ArrayList();
    for (int i = 0; i < values.size(); i++) {
      clone.values.add(((DataObject)values.get(i)).copy());
    }
    return clone;
  }

  public void addPropertyChangeListener(PropertyChangeListener listener) {
    throw new UnsupportedOperationException();
  }
  
  public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener) {
    throw new UnsupportedOperationException();
  }
  
  public void removePropertyChangeListener(PropertyChangeListener listener) {
    throw new UnsupportedOperationException();
  }
  
  public void removePropertyChangeListener(String propertyName, PropertyChangeListener listener) {
    throw new UnsupportedOperationException();
  }
  
  public void addVetoableChangeListener(VetoableChangeListener listener) {
    throw new UnsupportedOperationException();
  }
  
  public void addVetoableChangeListener(String propertyName, VetoableChangeListener listener) {
    throw new UnsupportedOperationException();
  }
  
  public void removeVetoableChangeListener(VetoableChangeListener listener) {
    throw new UnsupportedOperationException();
  }
  
  public void removeVetoableChangeListener(String propertyName, VetoableChangeListener listener) {
    throw new UnsupportedOperationException();
  }
}
