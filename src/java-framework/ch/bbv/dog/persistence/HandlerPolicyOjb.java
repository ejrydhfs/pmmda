/* copyright 2000-2005 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence");
 * you may not use this file except in compliance with the License.
 * $Id$
 */
package ch.bbv.dog.persistence;

import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ojb.broker.Identity;
import org.apache.ojb.broker.PersistenceBroker;
import org.apache.ojb.broker.cache.ObjectCache;
import org.apache.ojb.odmg.HasBroker;
import org.apache.ojb.odmg.TransactionExt;
import org.odmg.*;

import ch.bbv.dog.DataObject;

/**
 * The handler policy OJB implements the CRUD operations for the data object
 * persistence handler connected through an OJB layer.
 * @author MarcelBaumann
 * @version $Revision$
 */
class HandlerPolicyOjb {
  /**
   * Logger of the instances of the class.
   */
  protected static Log log = LogFactory.getLog(HandlerPolicyOjb.class);

  /**
   * The database manager on which the policy is applied.
   */
  private DatabaseMgr databaseMgr;

  /**
   * Constructor of the class.
   * @param databaseMgr the database manager on which the policy is applied.
   */
  public HandlerPolicyOjb(DatabaseMgr databaseMgr) {
    this.databaseMgr = databaseMgr;
  }

  /**
   * Retrieves the object with the given identifer.
   * @param database database containing the object.
   * @param clazz class of the requested object.
   * @param id identifier of the requested object.
   * @return the requested object if found otherwise false.
   */
  Object retrieve(Database database, Class clazz, Integer id) {
    Object object = null;
    String query = createRetrieveQuery(clazz, id, null);
    List objects = retrieve(database, query);
    if (objects.size() > 0) {
      object = objects.get(0);
    }
    return object;
  }

  /**
   * Retrieves the list of objects fulfilling the given constraints. The
   * constraints defines the where clause of the select statement.
   * @param database database containing the objects.
   * @param clazz class of the requested objects.
   * @param constraints constraints for the requested objects.
   * @return the list of objects fulfilling the constraints.
   */
  List retrieve(Database database, Class clazz, String constraints) {
    String query = createRetrieveQuery(clazz, null, constraints);
    return retrieve(database, query);
  }

  /**
   * Performs the set of updates to synchronize a data object graph with its
   * persistent representation. The following steps are performed.
   * <ul>
   * <li>Remove all original objects from the OJB cache.</li>
   * <li>Remove all objects identified as deleted in the database.</li>
   * <li>Insert all new objects in the database.</li>
   * <li>Updae all modified objects in the database.</li>
   * </ul>
   * @param originals the objects of the original graph available locally.
   * @param copies the objects of the copied graph received from the client.
   * @param database database containing the requested objects.
   * @param deletedObjects objects to delete in the database.
   * @param insertedObjects objects to insert in the database.
   * @param updatedObjects objects to update in the database.
   */
  void update(Database database, List originals, List copies,
      List deletedObjects, List insertedObjects, List updatedObjects) {
    TransactionExt tx = (TransactionExt) databaseMgr
        .createTransaction(database);
    try {
      tx.begin();
      flushCache(tx, originals);

      for (Iterator i = deletedObjects.iterator(); i.hasNext();) {
        database.deletePersistent(i.next());
      }
      for (Iterator i = insertedObjects.iterator(); i.hasNext();) {
        DataObject object = (DataObject) i.next();
        object.setModified(false);
        database.makePersistent(object);
      }
      for (Iterator i = updatedObjects.iterator(); i.hasNext();) {
        DataObject object = (DataObject) i.next();
        object.setModified(false);
        tx.markDirty(object);
      }
      cache(tx, copies);
      tx.commit();
    } catch (RuntimeException e) {
      log.error(e);
      tx.abort();
      throw e;
    }
  }

  boolean areCached(Transaction tx, List objects) {
    boolean inCache = false;
    PersistenceBroker broker = getBroker(tx);
    ObjectCache cache = broker.serviceObjectCache();
    for (Iterator i = objects.iterator(); i.hasNext();) {
      DataObject object = (DataObject) i.next();
      Identity identity = new Identity(object, broker);
      if (cache.lookup(identity) == null) {
        inCache = true;
        break;
      }
    }
    return inCache;
  }

  /**
   * Creates the retrieve statement for objects with the given identifier and
   * constraints. If both parameters are null return all persistent objects of
   * the class.
   * @param clazz class of the requested objects.
   * @param id identifier of the searched object. The identifier can be null.
   * @param constraints constraints for the searched object. The constraints can
   *        be null.
   * @return The OQL select statement.
   */
  protected final String createRetrieveQuery(Class clazz, Integer id,
      String constraints) {
    StringBuilder query = new StringBuilder();
    query.append("select item from ").append(clazz.getName());
    if (id != null) {
      query.append(" where id = ").append(id.intValue());
      if ((constraints != null) && (constraints.trim().length() > 0)) {
        query.append(" and ").append(constraints);
      }
    } else if ((constraints != null) && (constraints.trim().length() > 0)) {
      query.append(" where ").append(constraints);
    }
    return query.toString();
  }

  /**
   * Flushes the list of objects from the broker's cache associated with the
   * transaction.
   * @param tx transaction context which cache should be cleared.
   * @param objects objects to flush from the cache.
   */
  protected final void flushCache(Transaction tx, List objects) {
    PersistenceBroker broker = getBroker(tx);
    for (Iterator i = objects.iterator(); i.hasNext();) {
      DataObject object = (DataObject) i.next();
      broker.removeFromCache(object);
    }
  }

  /**
   * Caches the list of objects in the broker's cache associated with the
   * transaction. The cache operation is faster when retrieving the data from
   * the database.
   * @param tx transaction context which cache should be cleared.
   * @param objects objects to cache in the broker's cache.
   */
  protected final void cache(Transaction tx, List objects) {
    PersistenceBroker broker = getBroker(tx);
    ObjectCache cache = broker.serviceObjectCache();
    for (Iterator i = objects.iterator(); i.hasNext();) {
      DataObject object = (DataObject) i.next();
      Identity identity = new Identity(object, broker);
      cache.cache(identity, object);
    }
  }

  /**
   * Retrieves the list of objects from the datbase fulfilling the given OQL
   * query.
   * @param database database containing the requested objects.
   * @param queryString the OQL query string used to retrieve the elements.
   * @return the list of objects respecting the search criteria.
   */
  protected List retrieve(Database database, String queryString) {
    Transaction tx = databaseMgr.createTransaction(database);
    List items = null;
    try {
      tx.begin();

      OQLQuery query = databaseMgr.createOQLQuery(database);
      query.create(queryString);
      items = (DList) query.execute();
      tx.commit();
    } catch (QueryInvalidException qie) {
      log.error(qie);
      tx.abort();
    } catch (QueryException qe) {
      log.error(qe);
      tx.abort();
    }
    return items;
  }

  /**
   * Returns the broker associated with the transaction.
   * @param tx transaction which broker is requested.
   * @return the requested broker.
   */
  protected final PersistenceBroker getBroker(Transaction tx) {
    return ((HasBroker) tx).getBroker();
  }

  /**
   * Returns the database manager associated with the handler policy object.
   * @return the database manager of the handler policy.
   */
  protected final DatabaseMgr getDatabaseMgr() {
    return databaseMgr;
  }
}