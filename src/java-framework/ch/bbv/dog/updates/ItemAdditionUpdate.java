/* copyright 2000-2005 Marcel Baumann 
 * Licensed under the Apache License, Version 2.0 (the "Licence"); 
 * you may not use this file except in compliance with the License. 
 * $Id$
 */
package ch.bbv.dog.updates;

import ch.bbv.dog.DataObject;
import ch.bbv.utilities.ReflectUtilities;


/**
 * Represents the addition of an item to an indexed property.
 * @version $Revision$
 */
public class ItemAdditionUpdate extends ItemUpdate {

	private static final long serialVersionUID = UpdatesSerialVersion.UID;

    /**
     * Initializes a new instance of the {@code ItemAdditionUpdate} class.
     * @param item the data object update which represents the added item
     * @param fieldName the field name of the indexed property
     */
	public ItemAdditionUpdate(DataObjectUpdate item, String fieldName) {
		super(item,fieldName);
	}

	/**
	 * Adds the value of this update to an indexed property of a data object.
	 * The value and indexed property are specified in the constructor.
	 * @param dataObject the data object which is modified by this update
	 */
	public void execute(DataObject dataObject) {
		Object collection = ReflectUtilities.getProperty(dataObject, getFieldName());
		ReflectUtilities.execute(collection, "add", new Class[]{Object.class}, new Object[] {getItem().getObject()});
	}
}