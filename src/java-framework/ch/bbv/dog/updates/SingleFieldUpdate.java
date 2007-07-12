/* copyright 2000-2005 Marcel Baumann 
 * Licensed under the Apache License, Version 2.0 (the "Licence"); 
 * you may not use this file except in compliance with the License. 
 * $Id$
 */
package ch.bbv.dog.updates;

import ch.bbv.dog.DataObject;
import ch.bbv.utilities.ReflectUtilities;

/**
 * Represents the update of a single property of type {@link DataObject}.
 * <p>
 * To single property updates that are not of type {@link DataObject}
 * use the {@link PrimitiveFieldUpdate} class.
 */
public class SingleFieldUpdate extends ItemUpdate {
	private static final long serialVersionUID = UpdatesSerialVersion.UID;
	
	/**
     * Initializes a new instance of the {@code SingleFieldUpdate} class.
     * @param item the data object update which represents the new property value
     * @param fieldName the field name of the single property
	 */
	public SingleFieldUpdate(DataObjectUpdate item, String fieldName) {
		super(item,fieldName);
	}
	
	/**
	 * Sets the value of the single property to the value of this update. The single property
	 * and the new value are specified in the constructor.
	 * @param dataObject the data object which is modified by this update.
	 */
	public void execute(DataObject dataObject) {
		ReflectUtilities.setProperty(dataObject, getFieldName(), getItem().getObject());
	}
}