/* copyright 2000-2004 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence);
 * you may not use this file except in compliance with the License.
 * $Id$
 */
package ch.bbv.dog;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * The local handler seperates the data object model instances used in two
 * layers running in the same virtual machine. This topology exists for example
 * when data logic is implemented on the server side.
 * @author MarcelBaumann
 * @version $Revision$
 */
public class LocalHandler implements DataObjectHandler {
  private DataObjectHandler handler;

  /**
   * Constructor of the class.
   * @param handler handler delegate implementing the services.
   */
  public LocalHandler(DataObjectHandler handler) {
    this.handler = handler;
  }

  public void openDatasource(String datasource, boolean readonly) {
    handler.openDatasource(datasource, readonly);
  }

  public void closeDatasource(String datasource) {
    handler.closeDatasource(datasource);
  }

  public DataObject retrieve(String datasource, Class clazz, Integer id) {
    DataObject object = handler.retrieve(datasource, clazz, id);
    if (object != null) {
      object = deepCopy(object.getClass(), object);
    }
    return object;
  }

  public List retrieve(String datasource, Class clazz, String constraints) {
    List copies = new ArrayList();
    for (Iterator i = handler.retrieve(datasource, clazz, constraints)
        .iterator(); i.hasNext();) {
      DataObject object = (DataObject) i.next();
      copies.add(deepCopy(object.getClass(), object));
    }
    return copies;
  }

  public LightweightObject retrieveLw(String datasource, Class clazz, Integer id) {
    return handler.retrieveLw(datasource, clazz, id);
  }

  public List retrieveLw(String datasource, Class clazz, String constraints) {
    return handler.retrieveLw(datasource, clazz, constraints);
  }

  public List store(String datasource, Class clazz, DataObject root) {
    return handler.store(datasource, clazz, root);
  }

  public void remove(String datasource, Class clazz, DataObject root) {
    handler.remove(datasource, clazz, root);
  }

  public void remove(String datasource, Class clazz, Integer id) {
    handler.remove(datasource, clazz, id);
  }

  public DataObject deepCopy(Class clazz, DataObject object) {
    return handler.deepCopy(clazz, object);
  }

  public void register(DataObjectType type) {
    handler.register(type);
  }

  public DataObjectType getType(DataObject object) {
    return handler.getType(object);
  }
}