package ch.bbv.notification.communication;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ch.bbv.dog.DataObject;

/**
 * PropertyChangeEvent implementation for changed on DataObjects
 * @author Bruno Persi
 */
public class DataObjectPropertyChangeEvent extends PropertyChangeEvent {

	private List<String> _newValues;
	private List<String> _originalValues;
	private List<String> _propertyNames;
	private String _action;
	private DataObject _bog;
	
	
	/**
	 * Create a new DataObjectPropertyChanceEvent with given parameters
	 * @param source the source of the event as <code>DataObject</code>
	 * @param action the action of the event as <code>String</code>
	 * @pre source != null && action != null
	 */
	public DataObjectPropertyChangeEvent(DataObject source, String action){
		super(source,null,null,null);
		assert source != null && action != null;
		_newValues = new ArrayList<String>(20);
		_originalValues = new ArrayList<String>(20);
		_propertyNames = new ArrayList<String>(20);
		_action = action;
		_bog = source;
	}
	
	/**
	 * add an change event to this object
	 * @param propertyName the attribute who has changed as <code>String</code>
	 * @param newValue the new value of the property as <code>String</code>
	 * @param originalValue the value of the changed property befor has changed as <code>String</code>
	 * @pre propertyName != null && newValue != null && originalValue != null
	 */
	public void addChange(String propertyName, String newValue, String originalValue){
		assert propertyName != null && newValue != null && originalValue != null;
		_propertyNames.add(propertyName);
		_newValues.add(newValue);
		_originalValues.add(originalValue);
	}
	
	/**
	 * add a set of changes to this object
	 * @param propertyName the attribute who has changed as <code>String[]</code>
	 * @param newValue the new value of the property as <code>String[]</code>
	 * @param originalValue the value of the changed property befor has changed as <code>String[]</code>
	 * @pre propertyName != null && newValue != null && originalValue != null
	 * @pre propertyNames.length == newValues.length && propertyNames.length == originalValues.length
	 */
	public void addChange(String[] propertyNames, String[] newValues, String...originalValues){
		assert propertyNames != null && newValues != null && originalValues != null;
		assert propertyNames.length == newValues.length && propertyNames.length == originalValues.length;
		_propertyNames.addAll(Arrays.asList(propertyNames));
		_newValues.addAll(Arrays.asList(newValues));
		_originalValues.addAll(Arrays.asList(originalValues));
	}

	/**
	 * returns the new values
	 * @return new values in a <code>List</code> of <code>Strings</code>
	 */
	public List<String> getNewValues() {
		return _newValues;
	}

	/**
	 * returns the original values
	 * @return original values in a <code>List</code> of <code>Strings</code>
	 */
	public List<String> getOriginalValues() {
		return _originalValues;
	}

	/**
	 * returns the property names
	 * @return property names in a <code>List</code> of <code>Strings</code>
	 */
	public List<String> getPropertyNames() {
		return _propertyNames;
	}
	
	/**
	 * getting the action of this event
	 * @return the action of this event as <code>String</code>
	 */
	public String getAction(){
		return _action;
	}
	
	/**
	 * getting the source of this event
	 * @return the source of this event as <code>DataObject</Code>
	 */
	public DataObject getSource(){
		return _bog;
	}

	/**
	 * interface method, has no implementation at the moment.
	 * @exception java.lang.IllegalStateException thrown everytime, while method is not implemented
	 */
	@Override
	public Object getNewValue() {
		throw new IllegalStateException("Method: getNewValue() not supported to time, use getNewValues()");
	}

	/**
	 * interface method, has no implementation at the moment.
	 * @exception java.lang.IllegalStateException thrown everytime, while method is not implemented
	 */
	@Override
	public Object getOldValue() {
		throw new IllegalStateException("Method: getOldValue() not supported to time, use getOldValues()");
	}

	/**
	 * interface method, has no implementation at the moment.
	 * @exception java.lang.IllegalStateException thrown everytime, while method is not implemented
	 */
	@Override
	public String getPropertyName() {
		throw new IllegalStateException("Method: getPropertyName() not supported to time, use getPropertyNames()");
	}
	
	
	

}
