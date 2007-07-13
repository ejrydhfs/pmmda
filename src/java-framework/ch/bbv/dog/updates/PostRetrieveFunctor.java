/* copyright 2000-2005 Marcel Baumann 
 * Licensed under the Apache License, Version 2.0 (the "Licence"); 
 * you may not use this file except in compliance with the License. 
 * $Id$
 */
package ch.bbv.dog.updates;

import java.util.List;

import ch.bbv.dog.AbstractVisitorFunctor;
import ch.bbv.dog.DataObject;
import ch.bbv.dog.DataObjectMgr;
import ch.bbv.dog.DataObjectType;
import ch.bbv.dog.DoFunctor;

/**
 * This functor is called after a retrieve on the client side in order to set
 * the transient properties of a data object.
 * @author ZlatkoFranjcic
 * @version $Revision$
 */
public class PostRetrieveFunctor implements DoFunctor {
	/**
	 * Creates a {@code PostRetrieveFunctor} and executes it on a given data object. 
	 * @param dataObject data object instance to process
	 */
	public static void createUpdates(DataObject dataObject) {
		PostRetrieveFunctor functor = new PostRetrieveFunctor();
		Class<? extends DataObject> clazz = dataObject.getClass();
		AbstractVisitorFunctor visitor = DataObjectMgr.getVisitorFor(clazz);
		visitor.setFunctor(functor);
		DataObjectType.invokeVisitor(clazz, dataObject, visitor);
	}

	/**
	 * Creates a {@code PostRetrieveFunctor} and executes it on a given list of data objects. 
	 * @param dataObjectList list of data object instances to process
	 */
	public static void createUpdatesForList(List<DataObject> dataObjectList) {
		for (DataObject dataObject : dataObjectList) {
			createUpdates(dataObject);
		}
	}

	public void execute(DataObject dataObject) {
		// Set the transient update field of the data object
		dataObject.resetUpdate();
	}
}
