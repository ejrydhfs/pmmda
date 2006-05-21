/* copyright 2000-2006 Marcel Baumann 
 * Licensed under the Apache License, Version 2.0 (the "Licence"); 
 * you may not use this file except in compliance with the License. 
 * $Id$
 */
package ch.bbv.dog.persistence.hibernate;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import ch.bbv.dog.DataObject;

public class HibHandlerPolicy {

  /**
   * Logger of the instances of the class.
   */
  protected static Log log = LogFactory.getLog(HibHandlerPolicy.class);

  void update(SessionFactory sessionFactory, List<DataObject> originals,
      List<DataObject> copies, List deletedObjects,
      List<DataObject> insertedObjects, List<DataObject> updatedObjects) {
    assert (originals != null) && (copies != null) && (deletedObjects != null)
        && (insertedObjects != null) && (updatedObjects != null);

    Session session = sessionFactory.getCurrentSession();

    try {
      session.beginTransaction();

      for (Object item : deletedObjects) {
        session.delete(item);
      }

      for (DataObject item : insertedObjects) {
        item.setModified(false);
        session.save(item);
      }

      for (DataObject item : updatedObjects) {
        item.setModified(false);
        session.saveOrUpdate(item);
      }

      session.getTransaction().commit();
    } catch (RuntimeException e) {
      log.error(e);
      session.getTransaction().rollback();
      throw e;
    }
  }

}
