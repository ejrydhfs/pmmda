/* copyright 2000-2004 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence);
 * you may not use this file except in compliance with the License.
 * $Id$
 */
package ch.bbv.dog.persistence;

import java.util.Iterator;
import java.util.List;

import org.apache.ojb.odmg.NarrowTransaction;
import org.apache.ojb.odmg.TransactionExt;
import org.odmg.*;

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

  void update(Database database, List originals, List copies,
      List deletedObjects, List insertedObjects, List updatedObjects) {
    Transaction tx = getDatabaseMgr().currentTransaction(database);
    flushCache(tx, originals);

    for (Iterator i = deletedObjects.iterator(); i.hasNext();) {
      database.deletePersistent(i.next());
    }
    for (Iterator i = insertedObjects.iterator(); i.hasNext();) {
      database.makePersistent(i.next());
    }
    for (Iterator i = updatedObjects.iterator(); i.hasNext();) {
      ((TransactionExt) tx).markDirty(i.next());
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
  protected List retrieve(Database database, String queryString) {
    List items = null;
    try {
      OQLQuery query = getDatabaseMgr().createOQLQuery(database);
      query.create(queryString);
      items = (DList) query.execute();
    } catch (QueryInvalidException qie) {
      log.error(qie);
    } catch (QueryException qe) {
      log.error(qe);
    }
    return items;
  }
}