/* copyright 2000-2005 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence");
 * you may not use this file except in compliance with the License.
 * $Id$
 */
package ch.bbv.dog.reference;

import java.util.Iterator;
import java.util.List;

import ch.bbv.dog.persistence.PersistenceHandler;

/**
 * The reference code factory creates all the codes available in an application.
 * The codes can be stored in the application itself, in a serialized file, or
 * in a database. 
 * The reference code manager handles the code locally and is the first item
 * in the chain. The persistence handler has a persistent version of codes on
 * the database.
 * @author MarcelBaumann
 * @version $Revision$
 */
public class ReferenceCodeFactory {
  /**
   * The reference code manager which is responsible for the codes created in
   * the factory.
   */
  private ReferenceCodeMgr manager;

  /**
   * The persistence handler used to access the codes stored in the database.
   */
  private PersistenceHandler persistenceHandler;

  /**
   * The remote handler used to access the codes stored in the remote server.
   */
  private Object remoteHandler;

  private List<ReferenceCodeInfo> infos;

  /**
   * Constructor of the class.
   * @param manager reference code manager responsible for the codes.
   * @param persistenceHandler the persistence handler of the factory.
   * @param remoteHandler the remote handler of the factory.
   */
  public ReferenceCodeFactory(ReferenceCodeMgr manager,
      PersistenceHandler persistenceHandler, Object remoteHandler) {
    this.manager = manager;
    this.persistenceHandler = persistenceHandler;
    this.remoteHandler = remoteHandler;
  }

  void addReferenceCodeInfos(List<ReferenceCodeInfo> infos) {
    this.infos.addAll((infos));
  }

  void addReferenceCodeInfo(ReferenceCodeInfo info) {
    infos.add(info);
  }

  void addReferenceCodeInfos(ReferenceCodeMgr manager) {
    for (Iterator i = manager.getReferenceCodeClasses().iterator(); i.hasNext();) {
      Class clazz = (Class) i.next();
      ReferenceCodeInfo info = new ReferenceCodeInfo();
      info.setClassname(clazz.getName());
      info.setVersion(manager.getVersion(clazz));
      info.setCodes(manager.getAllReferenceCodes(clazz));
      info.setHierarchical(false);
      infos.add(info);
    }
  }
}