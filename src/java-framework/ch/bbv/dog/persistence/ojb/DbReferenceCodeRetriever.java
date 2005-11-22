/* copyright 2000-2005 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence");
 * you may not use this file except in compliance with the License.
 * $Id$
 */

package ch.bbv.dog.persistence.ojb;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ch.bbv.dog.reference.ReferenceCodeInfo;
import ch.bbv.dog.reference.ReferenceCodeRetriever;

/**
 * The database reference code retriever read reference code values from a
 * database based repository.
 * @author MarcelBaumann
 * @version $Revision$
 */
public class DbReferenceCodeRetriever implements ReferenceCodeRetriever {
  /**
   * Logger of the OJB policy handler class.
   */
  private static Log log = LogFactory.getLog(DbReferenceCodeRetriever.class);

  /**
   * The persistence handler used to access the repository containing the
   * reference code.
   */
  private PersistenceHandler handler;

  /**
   * Name of the datasource containing the reference code.
   */
  private String datasource;

  /**
   * Constructor of the class.
   * @param handler the persistence handler used to access the database.
   * @param datasource the name of the datasource containing the codes.
   */
  public DbReferenceCodeRetriever(PersistenceHandler handler, String datasource) {
    this.handler = handler;
    this.datasource = datasource;
  }

  public ReferenceCodeInfo retrieveReferenceCode(ReferenceCodeInfo info) {
    return null;
  }

  public List<ReferenceCodeInfo> retrieveReferenceCodes(
      List<ReferenceCodeInfo> infos) {
    Class clazz = null;
    for (ReferenceCodeInfo descriptor : infos) {
      String classname = descriptor.getClassName();
      if (classname == null) {
        descriptor.setCodes(null);
        descriptor.setHierarchical(false);
        descriptor.setVersion(null);
        continue;
      }
      try {
        clazz = Class.forName(classname);
      } catch (ClassNotFoundException e) {
        log.error(e);
        continue;
      }
      List codes = handler.retrieve(datasource, clazz, (String) null);
      descriptor.setCodes(codes);
    }
    return infos;
  }

  public void storeReferenceCodes(List infos) {
    throw new UnsupportedOperationException();
  }
}