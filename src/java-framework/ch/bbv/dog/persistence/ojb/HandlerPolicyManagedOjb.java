/* copyright 2000-2006 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence");
 * you may not use this file except in compliance with the License.
 * $Id$
 */
package ch.bbv.dog.persistence.ojb;

import java.util.List;

import org.apache.ojb.odmg.NarrowTransaction;
import org.apache.ojb.odmg.TransactionExt;
import org.odmg.Database;
import org.odmg.OQLQuery;
import org.odmg.QueryException;
import org.odmg.QueryInvalidException;

import ch.bbv.dog.DataObject;

/**
 * The handler policy OJB implements the CRUD operations for the data object
 * persistence handler connected through an OJB layer.
 * @author MarcelBaumann
 * @version $Revision$
 */
class HandlerPolicyManagedOjb extends HandlerPolicyOjb {
  /**
   * Constructor of the class.
   * @param databaseMgr the database manager on which the policy is applied.
   */
  public HandlerPolicyManagedOjb(DatabaseMgr databaseMgr) {
    super(databaseMgr);
  }

  void update(Database database, List<DataObject> originals,
      List<DataObject> copies, List deletedObjects,
      List<DataObject> insertedObjects, List<DataObject> updatedObjects) {
    assert (originals != null) && (copies != null) && (deletedObjects != null)
        && (updatedObjects != null);
    TransactionExt tx = (TransactionExt) getDatabaseMgr().currentTransaction(
        database);
    flushCache(tx, originals);

    for (Object item : deletedObjects) {
      database.deletePersistent(item);
    }
    for (DataObject item : insertedObjects) {
      database.makePersistent(item);
    }
    for (DataObject item : updatedObjects) {
      tx.markDirty(item);
    }
    cache(tx, copies);
    // Insures that identifier and optimistic locking are synchronized
    ((NarrowTransaction) tx).flush();
  }

  /**
   * Retrieves the list of objects from the datbase fulfilling the given OQL
   * query.
   * @param database database containing the requested objects.
   * @param queryString the OQL query string used to retrieve the elements.
   * @return the list of objects respecting the search criteria.
   */
  protected List<Object> retrieve(Database database, String queryString) {
    assert (database != null) && (queryString != null);
    List<Object> items = null;
    try {
      OQLQuery query = getDatabaseMgr().createOQLQuery(database);
      query.create(queryString);
      items = (List<Object>) query.execute();
    } catch (QueryInvalidException e) {
      log.error(e);
    } catch (QueryException e) {
      log.error(e);
    }
    return items;
  }
}