/* copyright 2000-2006 Marcel Baumann 
 * Licensed under the Apache License, Version 2.0 (the "Licence"); 
 * you may not use this file except in compliance with the License. 
 * $Id$
 */
package ch.bbv.dog.persistence.hibernate;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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
class HibDatabaseMgr {
  /**
   * Logger of the database manager class.
   */
  private static Log log = LogFactory.getLog(HibDatabaseMgr.class);

  /**
   * List of all opened databases.
   */
  private Map<String, SessionFactory> databases;

  /**
   * Default constructor of the class.
   */
  public HibDatabaseMgr() {
    databases = new HashMap<String, SessionFactory>();
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

    Configuration config = new Configuration().configure(jcdAlias + ".cfg.xml");
    // TODO use access mode
    SessionFactory sessionFactory = config.buildSessionFactory();
    databases.put(jcdAlias, sessionFactory);
  }

  /**
   * Returns the database with the given name.
   * @param jcdAlias name of the database to look for.
   * @return the requested database if found otherwise null.
   * @pre jcdAlias != null
   */
  public SessionFactory getDatabase(String jcdAlias) {
    assert (jcdAlias != null);
    return databases.get(jcdAlias);
  }

  /**
   * Closes the given database
   * @param sessionFactory the sessionfactory to close
   */
  public void closeDatabase(SessionFactory sessionFactory) {
    assert (sessionFactory != null);
    sessionFactory.close();
    databases.remove(sessionFactory);
  }

  /**
   * creates a session object for the given database
   * @param sessionFactory
   * @return session
   */
  public Session createSession(SessionFactory sessionFactory) {
    assert (sessionFactory != null);
    Session session = sessionFactory.getCurrentSession();
    return session;

  }
}