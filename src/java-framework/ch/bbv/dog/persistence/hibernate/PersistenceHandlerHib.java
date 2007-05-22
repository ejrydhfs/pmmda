package ch.bbv.dog.persistence.hibernate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.ObjectNotFoundException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import ch.bbv.dog.AbstractVisitorFunctor;
import ch.bbv.dog.DataObject;
import ch.bbv.dog.DataObjectMgr;
import ch.bbv.dog.DataObjectType;
import ch.bbv.dog.DataObjectUpdate;
import ch.bbv.dog.LightweightObject;
import ch.bbv.dog.persistence.CollectFunctor;
import ch.bbv.dog.persistence.PersistenceHandler;

/**
 * The data object persistence handler retrieves and stores data object graphs
 * in the database. The handler uses the services defined in the package and
 * provided in Hibernate layer to communicate with the underlying database. The
 * handler always returns a copy of the graph to be able later to infer which
 * objects were removed.
 * <ul>
 * <li>The retrieval of a graph of data objects is the creation of the correct
 * OQL statement and its execution.</li>
 * <li>The update of a graph of data objects is to organize the objects into
 * one set of objects to delete, to insert and to update and to perform these
 * operations. Upon completion the update information should be sent back to the
 * client.</li>
 * </ul>
 * 
 * @author MarcelBaumann
 * @author UeliKurmann
 * @version $Revision$
 */

public class PersistenceHandlerHib implements PersistenceHandler {

	/**
	 * The Hibernate Database Manager
	 */
	DatabaseMgrHib databaseMgr;

	/**
	 * Logger of the instances of the class.
	 */
	protected static Log log = LogFactory.getLog(PersistenceHandlerHib.class);

	/**
	 * Hack due to compiler differences between JDK 1.5 and eclipse. This
	 * constant is used instead of Collections.emptyList().
	 */
	private static final List<DataObject> EMPTY_LIST = new ArrayList<DataObject>();

	/**
	 * Default constructor of the class instantiates the Hibernate Database
	 * Manager
	 */
	public PersistenceHandlerHib() {
		databaseMgr = new DatabaseMgrHib();
	}

	public void openDatasource(String source, boolean readonly) {
		databaseMgr.openDatabase(source);
	}

	public void closeDatasource(String datasource) {
		databaseMgr.closeDatabase(databaseMgr.getDatabase(datasource));
	}

	public DataObject retrieve(String datasource, Class clazz, Integer id) {
		assert (datasource != null) && (clazz != null) && (id != null);
		Session session = databaseMgr.getDatabase(datasource)
				.getCurrentSession();
		DataObject dataObject = null;
		session.beginTransaction();
		try {
			dataObject = (DataObject) session.load(clazz, id);
		} catch (ObjectNotFoundException exception) {
			log.info("Object not found...");
		}
		session.getTransaction().commit();

		return dataObject;
	}

	public List<DataObject> retrieve(String datasource, Class clazz,
			String constraints) {
		assert (datasource != null) && (clazz != null);
	
		Session session = databaseMgr.getDatabase(datasource)
				.getCurrentSession();
		session.beginTransaction();
		Criteria criteria = session.createCriteria(clazz);
		
		List<DataObject> result = (List<DataObject>) (criteria.list());
		
		session.getTransaction().commit();
		
		return result;
	}

	public LightweightObject retrieveLw(String datasource, Class clazz,
			Integer id) {
		Session session = databaseMgr.getDatabase(datasource)
				.getCurrentSession();
		session.beginTransaction();
		LightweightObject LightweightObject = (LightweightObject) session.load(
				clazz, id);
		session.getTransaction().commit();

		return LightweightObject;
	}

	public List<LightweightObject> retrieveLw(String datasource, Class clazz,
			String constraints) {
		assert (datasource != null) && (clazz != null);
		
		Session session = databaseMgr.getDatabase(datasource)
				.getCurrentSession();
		session.beginTransaction();
		Criteria criteria = session.createCriteria(clazz);
		List<LightweightObject> result = (List<LightweightObject>) (criteria.list());
		session.getTransaction().commit();
		return result;
	}

	public List<DataObjectUpdate> store(String datasource, Class clazz,
			DataObject root) {
		CollectFunctor collect = new CollectFunctor();
		AbstractVisitorFunctor visitor = DataObjectMgr.getVisitorFor(clazz);
		visitor.setFunctor(collect);
		List<DataObject> originals = Collections.emptyList();
		if (!root.isNew()) {
			DataObject originalRoot = retrieve(datasource, clazz, root.getId());
			if (originalRoot != null) {
				DataObjectType.invokeVisitor(clazz, originalRoot, visitor);
				originals = new ArrayList<DataObject>(collect.getObjects());
			}
		}
		collect.reset();
		DataObjectType.invokeVisitor(clazz, root, visitor);
		List<DataObject> objects = collect.getObjects();
		List<DataObject> deleted = collect.getMissingObjectsFrom(originals);
		List<DataObject> inserted = collect.getNewObjects();
		List<DataObject> updated = collect.getUdpatedObjects();

		update(databaseMgr.getDatabase(datasource), originals, objects,
				deleted, inserted, updated);
		if (log.isTraceEnabled()) {
			StringBuilder text = new StringBuilder();
			text.append("end store operation ").append(objects.size()).append(
					" inserted: ").append(inserted.size()).append(" updated: ")
					.append(updated.size()).append("deleted ").append(
							deleted.size());
			log.trace(text.toString());
		}
		return collect.getUpdates();
	}

	public void remove(String datasource, Class clazz, DataObject root) {
		DataObject originalRoot = retrieve(datasource, clazz, root.getId());
		remove(datasource, clazz, root, originalRoot);
	}

	public void remove(String datasource, Class clazz, Integer id) {
		assert (datasource != null) && (clazz != null) && (id != null);
		DataObject object = retrieve(datasource, clazz, id);
		if (object != null) {
			remove(datasource, clazz, null, object);
		}
	}

	/**
	 * Remvoes the data object graph.
	 * 
	 * @param datasource
	 *            datasouce containing the data objects to remove.
	 * @param clazz
	 *            class of the root object.
	 * @param root
	 *            root of the data object graph to remove.
	 * @param originalRoot
	 *            root of the orginal data object graph to remove.
	 */
	private void remove(String datasource, Class clazz, DataObject root,
			DataObject originalRoot) {

		if (log.isTraceEnabled()) {
			StringBuilder text = new StringBuilder();
			text.append("start remove operation in ").append(datasource)
					.append(" class:").append(clazz.getName()).append(" id: ")
					.append(originalRoot.getId());
			log.trace(text.toString());
		}

		SessionFactory sessionFactory = databaseMgr.getDatabase(datasource);

		CollectFunctor collect = new CollectFunctor();
		AbstractVisitorFunctor visitor = DataObjectMgr.getVisitorFor(clazz);
		visitor.setFunctor(collect);
		DataObjectType.invokeVisitor(clazz, originalRoot, visitor);
		List<DataObject> deleted = new ArrayList<DataObject>(collect
				.getObjects());
		update(sessionFactory, deleted, deleted, deleted, EMPTY_LIST,
				EMPTY_LIST);
		if (log.isTraceEnabled()) {
			StringBuilder text = new StringBuilder();
			text.append("end remove operation ").append(deleted.size());
			log.trace(text.toString());
		}
	}

	/**
	 * Performs the set of updates to synchronize a data object graph with its
	 * persistent representation. The following steps are performed.
	 * <ul>
	 * <li>Remove all original objects from the hibernate cache.</li>
	 * <li>Remove all objects identified as deleted in the database.</li>
	 * <li>Insert all new objects in the database.</li>
	 * <li>Update all modified objects in the database.</li>
	 * </ul>
	 * 
	 * @param originals
	 *            the objects of the original graph available locally.
	 * @param copies
	 *            the objects of the copied graph received from the client.
	 * @param sessionFactory
	 *            database containing the requested objects.
	 * @param deletedObjects
	 *            objects to delete in the database.
	 * @param insertedObjects
	 *            objects to insert in the database.
	 * @param updatedObjects
	 *            objects to update in the database.
	 * @pre (originals != null) && (copies != null) && (deletedObjects != null) &&
	 *      (insertedObjects != null) && (updatedObjects != null)
	 */
	void update(SessionFactory sessionFactory, List<DataObject> originals,
			List<DataObject> copies, List deletedObjects,
			List<DataObject> insertedObjects, List<DataObject> updatedObjects) {
		assert (originals != null) && (copies != null)
				&& (deletedObjects != null) && (insertedObjects != null)
				&& (updatedObjects != null);

		Session session = sessionFactory.getCurrentSession();

		try {
			session.beginTransaction();
			
			for (DataObject item : updatedObjects) {
				item.setModified(false);
				session.saveOrUpdate(item);
			}
			
			session.flush();
			//session.clear();
			
			for (Object item : deletedObjects) {	
				session.delete(item);
			}
			
			session.flush();
			//session.clear();
			
			for (DataObject item : insertedObjects) {
				item.setModified(false);
				session.save(item);
			}
			
			session.flush();
			//session.clear();
		
			session.getTransaction().commit();
		} catch (RuntimeException e) {
			log.error(e);
			session.getTransaction().rollback();
			throw e;
		}
	}
}
