/* copyright 2000-2005 Marcel Baumann 
 * Licensed under the Apache License, Version 2.0 (the "Licence"); 
 * you may not use this file except in compliance with the License. 
 * $Id$
 */
package ch.bbv.dog.updates;

import ch.bbv.dog.DataObject;
import ch.bbv.utilities.ReflectUtilities;

/**
 * Represents the insertion of an item into an indexed property of a data object.
 * @version $Revision$ 
 */
public class ItemInsertionUpdate extends ItemUpdate {
	
	private static final String ADD = "add";

  private static final long serialVersionUID = UpdatesSerialVersion.UID;
	
	/**
	 * The index at which the item was inserted.
	 */
	private int index;

	/**
     * Initializes a new instance of the {@code ItemInsertionUpdate} class.
     * @param item the data object update which represents the inserted item
     * @param fieldName the field name of the indexed property
     * @param index the index at which the item was inserted
	 */
	public ItemInsertionUpdate(DataObjectUpdate item, String fieldName, int index) {
		super(item,fieldName);
		this.index = index;
	}

	/** 
	 * Inserts the value of this update to an indexed property of the data object.
	 * The value, indexed property and index are specified in the constructor.
	 * @param dataObject the data object which is modified by this update
	 */
	public void execute(DataObject dataObject) {
		Object collection = ReflectUtilities.getProperty(dataObject, getFieldName());		
		ReflectUtilities.execute(collection, ADD, new Class[]{int.class, Object.class}, new Object[] {index, getItem().getObject()});	
	}
}