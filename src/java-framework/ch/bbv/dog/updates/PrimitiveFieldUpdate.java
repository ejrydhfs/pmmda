/* copyright 2000-2005 Marcel Baumann 
 * Licensed under the Apache License, Version 2.0 (the "Licence"); 
 * you may not use this file except in compliance with the License. 
 * $Id$
 */
package ch.bbv.dog.updates;

import java.io.Serializable;

import ch.bbv.dog.DataObject;
import ch.bbv.utilities.ReflectUtilities;

/**
 * Represents the update of a single property which is not of type {@link DataObject}.
 * For updates on single properties of type {@code DataObject}, the {@code SingleFieldUpdate}
 * class is used.
 * @see SingleFieldUpdate
 * @version $Revision$
 */
public class PrimitiveFieldUpdate implements Update, Serializable {
	private static final long serialVersionUID = UpdatesSerialVersion.UID;

	/**
	 * The name of the single property.
	 */
	private String fieldName;

	/**
	 * The new value of the single property.
	 */
	private Serializable value;
	
	/**
     * Initializes a new instance of the {@code PrimitiveFieldUpdate} class.
     * @param fieldName the field name of the single property to which this update belongs
     * @param value the new value of the single property
	 */
	public PrimitiveFieldUpdate(String fieldName, Serializable value) {
		this.fieldName = fieldName;
		this.value = value;
	}

	/**
	 * Sets the value of the single property to the value of this update. The single property and
	 * the new value are specified in the constructor.
	 * @param dataObject the data object which is modified by this update.
	 */
	public void execute(DataObject dataObject) {		
		ReflectUtilities.setProperty(dataObject, fieldName, value);
	}	
}