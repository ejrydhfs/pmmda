/* copyright 2000-2005 Marcel Baumann 
 * Licensed under the Apache License, Version 2.0 (the "Licence"); 
 * you may not use this file except in compliance with the License. 
 * $Id$
 */
package ch.bbv.dog.updates;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import ch.bbv.application.Application;
import ch.bbv.dog.DataObject;
import ch.bbv.dog.DataObjectHandler;
import ch.bbv.dog.DataObjectId;
import ch.bbv.dog.DataObjectMgr;
import ch.bbv.dog.persistence.CollectFunctor;
import ch.bbv.utilities.ReflectUtilities;

/**
 * The DOG update represents the root {@link DataObject data object} of a DOG
 * which should be stored.
 * <p>
 * The updates can be iterated over by obtaining an iterator via the {@link #iterator()}
 * method or by getting an array of all updates via {@link #getUpdates()}.
 * @see DataObject
 * @see DataObjectUpdate
 * @see Update
 * @version $Revision$
 */
public class DogUpdate implements Iterable<DataObjectUpdate>, Serializable {
	
	private static final long serialVersionUID = UpdatesSerialVersion.UID;

	/**
	 * A list of all data object updates.
	 */
	private List<DataObjectUpdate> updates;

	/**
	 * The root data object update which represents the root of the DOG associated with
	 * this DOG update.
	 */
	private DataObjectUpdate rootUpdate;
	
	/**
	 * Initializes a new instance of the {@code DogUpdate} class using a
	 * {@code DataObject} instance. The DOG update stores the
	 * update object of each data object within this DOG update.
     * @param rootObject the root object of the DOG
	 */
	public DogUpdate(DataObject rootObject) {
		CollectFunctor functor = CollectFunctor.collect(rootObject);
		updates = functor.getUpdates();
		rootUpdate = rootObject.getUpdate();
		if (!updates.contains(rootUpdate)) {
			updates.add(rootUpdate);
		}
	}

	/**
     * Gets a {@code Map} which maps the data object updates
     * within this dog update to the corresponding data objects.
     * @return a {@code Map} containing the update-to-data-object mappings
     */
	public Map<DataObjectUpdate,DataObject> mapUpdatesToDataobjects() {
      HashMap<DataObjectUpdate,DataObject> updateIdToDo = new HashMap<DataObjectUpdate, DataObject>();
      for(DataObjectUpdate update : updates) {
        updateIdToDo.put(update, update.getObject());
      }
      return updateIdToDo;
    }

	/**
	 * Executes this dog update.
	 * @return the root data object which is represented by this update
	 */
	public DataObject execute() throws Exception {
		DataObjectMgr doMgr = Application.getApplication().getDataObjectMgr();
		DataObjectHandler doHandler = Application.getApplication().getDataObjectMgr().getDataObjectHandler(); 
		String datasource = doMgr.getDatasource();

		// NOTE: We need to load the root object, even if it is not updated, because that's what gets returned
		//       by this method. We load all objects (due to the functor, which traverses the whole data structure).
		//       But load each data object just once!
		Map<DataObjectId, DataObject> idToDataObject = new HashMap<DataObjectId, DataObject>();
		if (rootUpdate.getId() != null) {
			DataObject loadedObject = doHandler.retrieve(datasource, (Class<? extends DataObject>)Class.forName(rootUpdate.getClassname()), rootUpdate.getId());					
			CollectFunctor functor = CollectFunctor.collect(loadedObject);
			for (DataObject dataObject : functor.getObjects()) {
				DataObjectId loadedId = new DataObjectId(dataObject);
				idToDataObject.put(loadedId, dataObject);
			}
		}

		for (DataObjectUpdate update : updates) {
			DataObjectId doID = new DataObjectId(Class.forName(update.getClassname()), update.getId());			
			
			if(!idToDataObject.containsKey(doID)) {
				if (doID.getId() == null) {
					// update represents a new data object --> recreate it
					DataObject dataObject = (DataObject) ReflectUtilities.newInstance(doID.getBoClass());
					idToDataObject.put(doID, dataObject);
				}
				 else {					 
					DataObject loadedObject = doHandler.retrieve(datasource, doID.getBoClass(), doID.getId()); 
					CollectFunctor functor = CollectFunctor.collect(loadedObject);
					for (DataObject dataObject : functor.getObjects()) {
						DataObjectId loadedDoId = new DataObjectId(dataObject);
						idToDataObject.put(loadedDoId, dataObject);
					}
				}
			}		

			update.setObject(idToDataObject.get(doID));
			if (!timestampsAreEqual(update.getTimestamp(), update.getObject().getTimestamp())) {
				// TODO: Optimistic locking failed -> take appropriate action
			}
		}
		for (DataObjectUpdate update : updates) {			
			update.execute();
		}
		return rootUpdate.getObject();
	}

	/**
	 * Clears all updates. This method is called before this DOG update is
	 * transferred back from the server to the client to update the id and time stamp of the altered
	 * data objects.
	 */
	public void clearUpdates() {
		for (DataObjectUpdate update : updates) {
			update.clearUpdates();
		}
	}

	/**
	 * Gets the updates for each new, modified and deleted data objects in the underlying DOG.
	 * @return an array containing the update objects 
	 */
	public DataObjectUpdate[] getUpdates() {
		return updates.toArray(new DataObjectUpdate[updates.size()]);
	}

	@Override
	public Iterator<DataObjectUpdate> iterator() {
		return updates.iterator();
	}

	/**
	 * Compares two time stamps.
	 * @param timestamp1 the first time stamp
	 * @param timestamp2 the second time stamp
	 * @return true if the time stamps are equal
	 */
	private boolean timestampsAreEqual(Timestamp timestamp1, Timestamp timestamp2) {
		if ((timestamp1 == null) && (timestamp2 == null)) {
			return true;
		} else if (timestamp1 != null) {
			return timestamp1.equals(timestamp2);
		} else {
			return false;
		}
	}
}