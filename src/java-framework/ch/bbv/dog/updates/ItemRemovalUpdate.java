/* copyright 2000-2005 Marcel Baumann 
 * Licensed under the Apache License, Version 2.0 (the "Licence"); 
 * you may not use this file except in compliance with the License. 
 * $Id$
 */
package ch.bbv.dog.updates;

import ch.bbv.dog.DataObject;
import ch.bbv.utilities.ReflectUtilities;

/**
 * Represents the removal of an item from an indexed property of a
 * data object.
 * @version $Revision$
 */
public class ItemRemovalUpdate extends ItemUpdate {

	private static final long serialVersionUID = UpdatesSerialVersion.UID;

    /** 
     * Initializes a new instance of the {@code ItemRemovalUpdate} class.
     * @param item the data object update which represents the removed item
     * @param fieldName the field name of the indexed property from which the item was removed
     */
	public ItemRemovalUpdate(DataObjectUpdate item, String fieldName) {
		super(item,fieldName);
	}

	/**
	 *   Removes the item from the indexed property of the data object.
	 *   The item and indexed property are specified in the constructor.
	 *   @param dataObject the data object which is modified by this update   
	 */
	public void execute(DataObject dataObject) {		
		Object collection = ReflectUtilities.getProperty(dataObject, getFieldName());		
		ReflectUtilities.execute(collection, "remove", new Class[]{Object.class}, new Object[] {getItem().getObject()});
	}
}