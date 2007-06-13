/* This code was generated automatically with the pmMDA generator. Please do 
 NOT add code outside the marked zones. All changes outside the marked areas 
 will be discarded during the next execution of the MDA generator. 
 */

package ch.bbv;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.VetoableChangeListener;
import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ch.bbv.dog.DataObject;
import ch.bbv.dynamicproperties.PropertyInstanceManager;
import ch.bbv.dynamicproperties.core.DynamicProperty;
import ch.bbv.dynamicproperties.core.PropertyValue;

/* %% USER DEFINED IMPORT START %% */
/* %% USER DEFINED IMPORT END %% */

public class Contact implements DataObject, DynamicProperty {

	/* %% USER DEFINED DECLARATION START %% */
	
	/* %% USER DEFINED DECLARATION END %% */

	/**
	 * Serial version identifier coded as back compatibility date.
	 */
	private static final long serialVersionUID = 99990101;

	private String name;

	private String city;

	/**
	 * Mapping for 1: N relationship contacts as reverse foreign key.
	 */
	private Integer rootClassContactsHandle;

	/**
	 * List to store dynamic properties. Dynamic properties implements the
	 * DataObject interface to be compatible with the DataObject Graph.
	 */
	private List<PropertyValue> propertyValues;

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
	public Contact() {
		this.propertyValues = PropertyInstanceManager.instance().createProperties(
				this.getClass());
	}

	/* %% USER DEFINED CONSTRUCTORS START %% */
	/* %% USER DEFINED CONSTRUCTORS END %% */

	/* %% USER DEFINED CODE START %% */
	/* %% USER DEFINED CODE END %% */

	@Override
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}
		if (!(o instanceof Contact)) {
			return false;
		}
		Contact c = (Contact) o;
		return c.id.intValue() == this.id.intValue();
	}

	@Override
	public int hashCode() {
		if (id == null) {
			return super.hashCode();
		}
		return id.hashCode();
	}

	/**
	 * Returns the unique identifier of the data object instance. A new object
	 * has the value null.
	 * 
	 * @return the identifier of the instance.
	 * @see #setId(Integer)
	 */
	public final Integer getId() {
		return id;
	}

	/**
	 * Sets the identifier of the data object instance.
	 * 
	 * @param id
	 *            new identifier of the instance
	 * @see #getId()
	 */
	public final void setId(Integer id) {
		this.id = id;
	}

	/**
	 * Returns the flag indicating if the data object instance was modified.
	 * 
	 * @return true if the object was modified otherwise false.
	 * @see #setModified
	 */
	public final boolean isModified() {
		return modified;
	}

	/**
	 * Sets the flag indicating if the data object instance was modified.
	 * 
	 * @param modified
	 *            new value of the flag
	 * @see #isModified
	 */
	public final void setModified(boolean modified) {
		this.modified = modified;
	}

	/**
	 * Convenience method indicating if the instance is a new one or not.
	 * 
	 * @return true if the object is new otherwise false.
	 * @see #getId()
	 */
	public final boolean isNew() {
		return (getId() == null);
	}

	/**
	 * Returns the timestamp of the data object instance.
	 * 
	 * @return the timestamp of the last persistent modification.
	 * @see #setTimestamp(Timestamp)
	 */
	public final Timestamp getTimestamp() {
		return timestamp;
	}

	/**
	 * Sets the timestamp of the data object instance.
	 * 
	 * @param timestamp
	 *            timestamp of the instance
	 * @see #getTimestamp()
	 */
	public final void setTimestamp(Timestamp timestamp) {
		this.timestamp = timestamp;
	}

	/**
	 * Returns the current value of the simple property name.
	 * 
	 * @return the value of the simple property name.
	 * @see #setName(String)
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the new value of the simple property name.
	 * 
	 * @param name
	 *            new value of the property
	 * @see #getName()
	 */
	public void setName(String name) {
		if (((this.name != null) && !this.name.equals(name))
				|| ((this.name == null) && (name != null))) {
			this.name = name;
			setModified(true);
		}
	}

	/**
	 * Returns the current value of the simple property city.
	 * 
	 * @return the value of the simple property city.
	 * @see #setCity(String)
	 */
	public String getCity() {
		return city;
	}

	/**
	 * Sets the new value of the simple property city.
	 * 
	 * @param city
	 *            new value of the property
	 * @see #getCity()
	 */
	public void setCity(String city) {
		if (((this.city != null) && !this.city.equals(city))
				|| ((this.city == null) && (city != null))) {
			this.city = city;
			setModified(true);
		}
	}

	/**
	 * Implements the visitor pattern in the data object class. The accept
	 * method dispatches the visitor to all its relevant children. A relevant
	 * child is always another data object.
	 * 
	 * @param visitor
	 *            visitor to act on the data object
	 * @pre visitor != null
	 */
	public void accept(Visitor visitor) {
		assert (visitor != null);
		visitor.setDepth(visitor.getDepth() + 1);
		visitor.visit(this);
		traverse(visitor);
		visitor.setDepth(visitor.getDepth() - 1);
	}

	/**
	 * Creates and returns a deep copy of the object and its descendants. The
	 * properties containing keys are not cloned because they are immutable.
	 * 
	 * @return a deep copy of the instance
	 */
	public Contact copy() {
		Map<Object, Object> copies = new HashMap<Object, Object>();
		return copy(copies);
	}

	public void addPropertyChangeListener(PropertyChangeListener listener) {
		throw new UnsupportedOperationException();
	}

	public void addPropertyChangeListener(String propertyName,
			PropertyChangeListener listener) {
		throw new UnsupportedOperationException();
	}

	public void removePropertyChangeListener(PropertyChangeListener listener) {
		throw new UnsupportedOperationException();
	}

	public void removePropertyChangeListener(String propertyName,
			PropertyChangeListener listener) {
		throw new UnsupportedOperationException();
	}

	public void addVetoableChangeListener(VetoableChangeListener listener) {
		throw new UnsupportedOperationException();
	}

	public void addVetoableChangeListener(String propertyName,
			VetoableChangeListener listener) {
		throw new UnsupportedOperationException();
	}

	public void removeVetoableChangeListener(VetoableChangeListener listener) {
		throw new UnsupportedOperationException();
	}

	public void removeVetoableChangeListener(String propertyName,
			VetoableChangeListener listener) {
		throw new UnsupportedOperationException();
	}

	/**
	 * Convenience method to set the value of a foreign key.
	 * 
	 * @param bean
	 *            object which property should be set
	 * @param property
	 *            property to be set
	 * @param key
	 *            key identifier to set
	 * @pre (bean != null) && (property != null) && (key != null)
	 */
	protected final void setForeignKey(DataObject bean, String property,
			Integer key) {
		assert ((bean != null) && (property != null));
		try {
			Field field = bean.getClass().getDeclaredField(property);
			if (field != null) {
				field.setAccessible(true);
				field.set(bean, key);
			}
		} catch (NoSuchFieldException e) {
			assert (false);
		} catch (IllegalArgumentException e) {
			assert (false);
		} catch (IllegalAccessException e) {
			assert (false);
		}
	}

	/**
	 * Copies deeply all the attributes of the class and its ancestors. Multiple
	 * references to the same data object are handled correctly.
	 * 
	 * @param copies
	 *            set of copied objects used to detect circular references
	 * @return the deep copy of the object
	 */
	public Contact copy(Map<Object, Object> copies) {
		Contact clone = (Contact) copies.get(this);
		if (clone == null) {
			try {
				clone = (Contact) clone();
				copies.put(this, clone);
			} catch (CloneNotSupportedException e) {
				// impossible exception because data object is cloneable.
			}
		}
		return clone;
	}

	/**
	 * Implements the visitor pattern in the data object class. The accept
	 * method dispatches the visitor to all relevant children of the ancestors
	 * and its children. A relevant child is always another data object.
	 * 
	 * @param visitor
	 *            visitor to act on the data object
	 */
	protected void traverse(Visitor visitor) {
	}

	/**
	 * Returns the Property value with the given name
	 * 
	 * @param name
	 *            the name of the property
	 */
	public PropertyValue getProperty(String name) {
		for (PropertyValue value : propertyValues) {
			
			if (value.getProperty().getName().equals(name)) {
				value.removePropertyChangeListener(this);
				value.addPropertyChangeListener(this);
				return value;
			}
		}
		return null;
	}

	/**
	 * Returns the list of all properties
	 * 
	 * @return the list of all properties
	 */
	public List<PropertyValue> getProperties() {
		for(PropertyValue value:propertyValues){
			value.removePropertyChangeListener(this);
			value.addPropertyChangeListener(this);
		}
		return propertyValues;
	}
	/* %% USER DEFINED PROTECTED/PRIVATE CODE START %% */
	/* %% USER DEFINED PROTECTED/PRIVATE CODE END %% */

	
	public void propertyChange(PropertyChangeEvent event) {
		setModified(true);
	}
}