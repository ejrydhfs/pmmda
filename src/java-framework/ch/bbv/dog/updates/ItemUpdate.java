/* copyright 2000-2005 Marcel Baumann 
 * Licensed under the Apache License, Version 2.0 (the "Licence"); 
 * you may not use this file except in compliance with the License. 
 * $Id$
 */
package ch.bbv.dog.updates;

import java.io.Serializable;

import ch.bbv.dog.DataObject;

/**
 * Base class for all updates to data objects where the value to be set
 * represents a {@link DataObject data object}.
 * @version $Revision$
 */
public abstract class ItemUpdate implements Update, Serializable {
	/**
	 * The update which represents the value for this update.
	 */
	private DataObjectUpdate item;
	
	/**
	 * The field name of the single or indexed property to which this update belongs.
	 */
	private String fieldName;

    /**
     * Initializes a new instance of the {@code ItemUpdate} class.
     * @param item the value of this update
     * @param fieldName the field name of the single or indexed property to which this update belongs
	 */
	public ItemUpdate(DataObjectUpdate item, String fieldName) {
		if ( fieldName == null ) {
			throw new IllegalArgumentException("fieldName == null");
		}
		this.item = item;
		this.fieldName = fieldName;
	}

	/**
	 * Executes this update on a specified data object.
	 * @param dataObject the data object which should be modified by this update
	 */
	public abstract void execute(DataObject dataObject);

	/**
	 * Get the item that represents the new value.
	 * @return the data object update which represents the item
	 */
	public DataObjectUpdate getItem() {
		return item;
	}

	/**
	 * Gets the field name of the single or indexed property to which this update belongs.
	 * @return the name of the field that needs to be updated
	 */
	public String getFieldName() {
		return fieldName;
	}
}