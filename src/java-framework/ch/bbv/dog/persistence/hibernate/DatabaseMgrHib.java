/* copyright 2000-2005 Marcel Baumann 
 * Licensed under the Apache License, Version 2.0 (the "Licence"); 
 * you may not use this file except in compliance with the License. 
 * $Id$
 */
package ch.bbv.dog.persistence.hibernate;

import java.util.HashMap;
import java.util.Map;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/**
 * <p>
 * The database manager provides access to all defined logical and physical
 * databases defined in the application. The manager hides the limitation of the
 * persistency layer and provides a simple interface to retrieve a database. A
 * database is a logical connection to a physical database. The manager handles
 * multiple logical and physical databases simultaneously. Operations are always
 * performed on one database.
 * </p>
 * @author MarcelBaumann
 * @version $Revision$
 */
class DatabaseMgrHib {

  private static String CFG_FILE_POSTFIX = ".cfg.xml";

  /**
   * List of all opened databases.
   */
  private Map<String, SessionFactory> databases;

  /**
   * Default constructor of the class.
   */
  public DatabaseMgrHib() {
    databases = new HashMap<String, SessionFactory>();
  }

  /**
   * Opens a database with the given name
   * @param databaseName name of the database to open. The name has to
   *          correspond with the filename of the hibernate configuration file.
   *          E.g.: Filename myTestDB.cfg.xml -> Name = myTestDB
   * @pre databaseName != null
   */
  public void openDatabase(String databaseName) {
    assert databaseName != null;
    Configuration config = new Configuration().configure(databaseName
        + CFG_FILE_POSTFIX);
    SessionFactory sessionFactory = config.buildSessionFactory();
    databases.put(databaseName, sessionFactory);
  }

  /**
   * Returns the SessionFactory with the given name.
   * @param databaseName name of the database to look for.
   * @return the requested SessionFactory if found otherwise null.
   * @pre databaseName != null
   */
  public SessionFactory getDatabase(String databaseName) {
    assert databaseName != null;
    return databases.get(databaseName);
  }

  /**
   * Closes the given database.
   * @param sessionFactory the SessionFactory to close
   */
  public void closeDatabase(SessionFactory sessionFactory) {
    assert sessionFactory != null;
    sessionFactory.close();
    databases.remove(sessionFactory);
  }

  /**
   * Creates a session object for the given database.
   * @param sessionFactory
   * @return session the new session
   * @pre sessionFactory != null
   */
  public Session createSession(SessionFactory sessionFactory) {
    assert sessionFactory != null;
    Session session = sessionFactory.getCurrentSession();
    return session;
  }
}