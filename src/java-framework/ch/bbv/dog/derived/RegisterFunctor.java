/* copyright 2000-2004 Marcel Baumann 
 * Licensed under the Apache License, Version 2.0 (the "Licence); 
 * you may not use this file except in compliance with the License. 
 * $Id$ 
 */
package ch.bbv.dog.derived;

import java.beans.PropertyChangeListener;

import ch.bbv.dog.DataObject;
import ch.bbv.dog.DoFunctor;

/**
 * The register functor registers its listeners to all the data objects
 * visited during a graph traversal. The functor should be called through
 * a visitor with functor instance.
 * @author MarcelBaumann
 * @version $Revision$
 */
class RegisterFunctor implements DoFunctor {
	/** Flag indicating if the listener is registered to or unregistered from the
	 * data object graph being traversed.
	 */
	private boolean register;
	
	/** Listener being registered or unregistered.
	 */
	private PropertyChangeListener listener;
	
	private Class dataObjectType;
	
	/** Name of the property the listener is interested in. If the value is null,
	 * the listener is interested in all properties defined in a data object
	 * type. 
	 */
	private String propertyName;
	
	public RegisterFunctor(PropertyChangeListener listener) {
		this.listener = listener;
	}
	
	public void setRegister(boolean register) {
		this.register = register;
	}
	
	public boolean getRegister() {
		return register;
	}

	public void setDataObjectType(Class dataObjectType) {
		this.dataObjectType = dataObjectType;	
	}
	
	public Class getDataObjectType() {
		return dataObjectType;
	}
	
	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}
	
	public String getPropertyName() {
		return propertyName;
	}
	
	public void execute(DataObject object) {
		assert(object != null && dataObjectType != null);
		if (object.getClass() != dataObjectType) {
			return;
		}
		if (register) {
			if (propertyName != null) {
				object.addPropertyChangeListener(propertyName, listener);	
			} else {
				object.addPropertyChangeListener(listener);
			}
		} else {
			if (propertyName != null) {
				object.removePropertyChangeListener(propertyName, listener);
			} else {
				object.removePropertyChangeListener(listener);
			}
		}
	}

}
