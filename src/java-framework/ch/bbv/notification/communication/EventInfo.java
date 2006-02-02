/* 
 * copyright 2000-2005 BBV Software Services AG
 * Licensed under the Apache License, Version 2.0 (the "Licence");
 * you may not use this file except in compliance with the License.
 */
package ch.bbv.notification.communication;

import java.io.Serializable;


/**
 * This class comprised all about the requested Event. 
 * @author Bruno Persi
 */
public class EventInfo implements Serializable{

	private Object[] _changedProperties;
	private java.lang.String _eventAction;
	private Object[] _newValues;
	private Object[] _originalValues;
	private int _subjectId;
	private String _qualifiedSubjectName;

	/**
	 * create a new instance
	 * @param subjectId the DataObject ID as <code>int</code>
	 * @param action the desired action as <code>String</code>
	 * @pre action != null && subjectName != null
	 */
	public EventInfo(int subjectId, String subjectName, String action){
		assert action != null && subjectName != null;
		_subjectId = subjectId;
		_eventAction = action;
		_qualifiedSubjectName = subjectName;
	}

	/**
	 * get the changed properties
	 * @return all changed properties as <code>Object[]</code>
	 */
	public Object[] getChangedProperties(){
		return _changedProperties;
	}

	/**
	 * get the event action
	 * @return the event action as <code>String</code>
	 */
	public String getEventAction(){
		return _eventAction;
	}

	/**
	 * get the original values
	 * @return the original values as <code>Object[]</code>
	 */
	public Object[] getOriginalValues(){
		return _originalValues;
	}

	/**
	 * get the DataObject ID
	 * @return the DataObject ID as <code>int</code>
	 */
	public int getSubjectId(){
		return _subjectId;
	}
	
	/**
	 * get the qualified DataObject name
	 * @return the DataObject name as <code>String</code>
	 */
	public String getSubjectName(){
		return _qualifiedSubjectName;
	}

	/**
	 * get the new values
	 * @return the new values as <code>Object[]</code>
	 */
	public Object[] getValues(){
		return _newValues;
	}
	
	@Override public boolean equals(Object obj){
		if (obj == null) return false;
		if (this == obj) return true;
		if (!(obj instanceof EventInfo)) return false;
		EventInfo info = (EventInfo)obj;
		if (info._subjectId == this._subjectId && info._eventAction == this._eventAction){
			return true;
		}
		return false;
	}
	
	@Override public int hashCode(){
		return _subjectId * 17 + _eventAction.hashCode();
	}

	protected void setChangedProperties(Object... properties) {
		assert properties != null;
		_changedProperties = properties;
	}

	protected void setNewValues(Object... values) {
		assert values != null;
		_newValues = values;
	}

	protected void setOriginalValues(Object... values) {
		assert values != null;
		_originalValues = values;
	}

}