/* copyright 2000-2005 Marcel Baumann 
 * Licensed under the Apache License, Version 2.0 (the "Licence"); 
 * you may not use this file except in compliance with the License. 
 * $Id$
 */
package ch.bbv.dog.persistence.ojb;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ojb.broker.PersistenceBroker;
import org.apache.ojb.broker.query.Query;
import org.apache.ojb.odmg.HasBroker;
import org.apache.ojb.odmg.OJB;
import org.apache.ojb.odmg.oql.EnhancedOQLQuery;
import org.odmg.Database;
import org.odmg.Implementation;
import org.odmg.ODMGException;
import org.odmg.Transaction;

/**
 * <p>
 * The database manager provides access to all defined logical and physical
 * databases defined in the application. The manager hides the limitation of the
 * persistency layer and provides a simple interface to retrieve a database. A
 * database is a logical connection to a physical database. The manager handles
 * multiple logical and physical databases simultaneously. Operations are always
 * performed on one database.
 * </p>
 * <p>
 * The handling of implementation and database instances follow the
 * recommendations of the ODMG API guide of OJB.
 * </p>
 * @author MarcelBaumann
 * @version $Revision$
 */
class DatabaseMgrOjb {
  /**
   * Logger of the database manager class.
   */
  private static Log log = LogFactory.getLog(DatabaseMgrOjb.class);

  /**
   * List of all opened databases.
   */
  private Map<String, LogicalDatabase> databases;

  /**
   * Default constructor of the class.
   */
  public DatabaseMgrOjb() {
    databases = new HashMap<String, LogicalDatabase>();
  }

  /**
   * Opens a database with the given name and access rights.
   * @param jcdAlias name of the database to open. The name is the OJB JCD alias
   *          defined in the XML configuration file of OJB.
   * @param accessMode access mode of the database.
   * @see org.odmg.Database
   * @pre (jcdAlias != null) && ((accessMode == Database.OPEN_EXCLUSIVE) ||
   *      (accessMode == Database.OPEN_READ_ONLY) || (accessMode ==
   *      Database.OPEN_READ_WRITE))
   */
  public void openDatabase(String jcdAlias, int accessMode) {
    assert (jcdAlias != null);
    try {
      LogicalDatabase logicalDb = new LogicalDatabase();
      logicalDb.jcdAlias = jcdAlias;
      logicalDb.implementation = OJB.getInstance();
      logicalDb.database = logicalDb.implementation.newDatabase();
      logicalDb.database.open(jcdAlias, accessMode);
      databases.put(jcdAlias, logicalDb);
    } catch (ODMGException e) {
      log.error(e);
    }
  }

  /**
   * Returns the database with the given name.
   * @param jcdAlias name of the database to look for.
   * @return the requested database if found otherwise null.
   * @pre jcdAlias != null
   */
  public Database getDatabase(String jcdAlias) {
    assert (jcdAlias != null);
    Database database = null;
    LogicalDatabase logicalDb = databases.get(jcdAlias);
    if (logicalDb != null) {
      database = logicalDb.database;
    }
    return database;
  }

  /**
   * Closes the given database.
   * @param database database to close.
   */
  public void closeDatabase(Database database) {
    assert (database != null);
    LogicalDatabase logicalDb = findDb(database);
    if (logicalDb != null) {
      try {
        logicalDb.database.close();
        databases.remove(logicalDb);
      } catch (ODMGException odmge) {
        log.error(odmge);
      }
    }
  }

  /**
   * Creates a transaction object for the given database. This method should not
   * be called in a managed environment where the transactional context is
   * managed through the container.
   * @param database database on which the transaction will be executed.
   * @return the requested transaction object.
   */
  public Transaction createTransaction(Database database) {
    assert (database != null);
    Transaction transaction = null;
    LogicalDatabase logicalDb = findDb(database);
    if (logicalDb != null) {
      transaction = logicalDb.implementation.newTransaction();
    }
    return transaction;
  }

  /**
   * Returns the currenct transaction associated with the database.
   * @param database database on which the transaction will be executed.
   * @return the requested transaction object.
   */
  public Transaction currentTransaction(Database database) {
    assert (database != null);
    Transaction transaction = null;
    LogicalDatabase logicalDb = findDb(database);
    if (logicalDb != null) {
      transaction = logicalDb.implementation.currentTransaction();
    }
    return transaction;
  }

  /**
   * Creates a query object for the given database. A query object should be
   * used only once.
   * @param database database on which the query will be executed.
   * @return the requested query object.
   */
  public EnhancedOQLQuery createOQLQuery(Database database) {
    assert (database != null);
    EnhancedOQLQuery query = null;
    LogicalDatabase logicalDb = findDb(database);
    if (logicalDb != null) {
      query = logicalDb.implementation.newOQLQuery();
    }
    return query;
  }

  /**
   * Returns the result set of a criteria query request.
   * @param database database on which the query will be executed.
   * @param query query to execute.
   * @return the collection of results of the query.
   */
  public Collection executeQuery(Database database, Query query) {
    PersistenceBroker broker = ((HasBroker) currentTransaction(database))
        .getBroker();
    return broker.getCollectionByQuery(query);
  }

  /**
   * Returns the logical database holding the given database.
   * @param database database which logical database should be found.
   * @return the requested logical database if found otherwise null.
   */
  protected LogicalDatabase findDb(Database database) {
    assert (database != null);
    for (LogicalDatabase db : databases.values()) {
      if (db.database == database) {
        return db;
      }
    }
    return null;
  }

  /**
   * The logical database defines the information necessary to manipulates\ a
   * database in the ODMG OJB context.
   * @author MarcelBaumann
   * @version $Revision$
   */
  private static class LogicalDatabase {
    /**
     * Alias for the database.
     */
    String jcdAlias;

    /**
     * Implementation instance of OJB.
     */
    Implementation implementation;

    /**
     * Database opened on a logical and physical database.
     */
    Database database;
  }
}