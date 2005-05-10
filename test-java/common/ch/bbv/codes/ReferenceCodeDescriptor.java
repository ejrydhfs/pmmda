	
/* This code was generated automatically with the pmMDA generator. Please do 
   NOT add code outside the marked zones. All changes outside the marked areas 
   will be discarded during the next execution of the MDA generator. 
   $Id$
*/
   
package ch.bbv.codes;

import ch.bbv.dog.DataObject;
import java.beans.PropertyChangeListener;
import java.beans.VetoableChangeListener;
import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.util.*;

/* %% USER DEFINED IMPORT START %% */
/* %% USER DEFINED IMPORT END %% */

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
   * Serial version identifier coded as back compatibility date.
   */
  private static final long serialVersionUID = 99990101;

  private String name;

  private String description;

  private String codeClassName;

  private Class codeClass;

  private boolean hierarchical;

  private CodeKind kind;

  private List<ValueDescriptor> values;

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
  	values = new ArrayList<ValueDescriptor>();
  }

/* %% USER DEFINED CONSTRUCTORS START %% */
/* %% USER DEFINED CONSTRUCTORS END %% */

/* %% USER DEFINED CODE START %% */
/* %% USER DEFINED CODE END %% */

  /** 
   * Returns the unique identifier of the data object instance.
   * @return the identifier of the instance.
   * @see #setId(Integer)
   */
  public Integer getId() {
  	return id;
  }
  
  /** 
   * Sets the identifier of the data object instance.
   * @param id new identifier of the instance.
   * @see #getId()
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
   * @see #setTimestamp(Timestamp)
   */
  public Timestamp getTimestamp() {
    return timestamp;
  }
  
  /** 
   * Sets the timestamp of the data object instance.
   * @param timestamp timestamp of the instance.
   * @see #getTimestamp()
   */
   public void setTimestamp(Timestamp timestamp) {
     this.timestamp = timestamp;
   }
  /** 
   * Returns the current value of the simple property name.
   * @return the value of the simple property name.
   * @see #setName(String)
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
   * Returns the current value of the simple property description.
   * @return the value of the simple property description.
   * @see #setDescription(String)
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
   * Returns the current value of the simple property codeClassName.
   * @return the value of the simple property codeClassName.
   * @see #setCodeClassName(String)
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
   * Returns the current value of the simple property codeClass.
   * @return the value of the simple property codeClass.
   * @see #setCodeClass(Class)
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
   * Returns the current value of the simple property hierarchical.
   * @return the value of the simple property hierarchical.
   * @see #setHierarchical(boolean)
   */
  public boolean isHierarchical() {
    return hierarchical;
  }

  /** 
   * Sets the new value of the simple property hierarchical.
   * @param hierarchical new value of the property.
   * @see #isHierarchical
   */    
  public void setHierarchical(boolean hierarchical) {
    if (this.hierarchical != hierarchical) {  
      this.hierarchical = hierarchical;
      setModified(true);
    }
  }
  /** 
   * Returns the current value of the simple property kind.
   * @return the value of the simple property kind.
   * @see #setKind(CodeKind)
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
  public List<ValueDescriptor> getValues() {
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
  public boolean addValues(Collection<ValueDescriptor> c) {
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
   * @return true (as per the general contract of the Collection.add method).   
   * @see java.util.List#add(Object)
   * @pre element != null
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
    traverse(visitor);
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
    copy(clone);
    clone.setModified(isModified());
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

  /**
   * Convenience method to set the value of a foreign key.
   * @param bean object which property should be set.
   * @param property property to be set.
   * @param key key identifier to set.
   */
  protected void setForeignKey(DataObject bean, String property, Integer key) {
    try {
      Field field = bean.getClass().getDeclaredField(property);
      if (field != null) {
        field.setAccessible(true);
        field.set(bean, key);
      }
    } catch (NoSuchFieldException e) {
      assert(false);
    } catch (IllegalArgumentException e) {
      assert(false);
    } catch (IllegalAccessException e) {
      assert(false);
    }
  }

  /** 
   * Copies deeply all the attributes of the class and its ancestors.
   * @param clone the copy which deep copy should be generated.
   */
  protected void copy(ReferenceCodeDescriptor clone) {
    clone.values = new ArrayList<ValueDescriptor>();
    for (int i = 0; i < values.size(); i++) {
      clone.values.add(((ValueDescriptor)(values.get(i)).copy()));
    }
  }

  /** 
   * Implements the visitor pattern in the data object class. The accept
   * method dispatches the visitor to all relevant children of the ancestors
   * and its children. A relevant child is always another data object.
   * @param visitor visitor to act on the data object.
   */  
  protected void traverse(Visitor visitor) {
    for (ValueDescriptor item : getValues()) {
      item.accept(visitor);
    }
  }  
}
