/* copyright 2000-2005 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence");
 * you may not use this file except in compliance with the License.
 * $Id$
 */
package ch.bbv.dog;

import java.util.Collections;
import java.util.List;

import ch.bbv.dog.updates.DataObjectUpdate;

/**
 * The empty handler implements the data object handler with a set of methods
 * performing no operations.
 * @author MarcelBaumann
 * @version $Revision$
 */
public class EmptyHandler implements DataObjectHandler {
  public void openDatasource(String source, boolean readonly) {
    throw new UnsupportedOperationException();
  }

  public void closeDatasource(String source) {
    throw new UnsupportedOperationException();
  }

  public DataObject retrieve(String datasource, Class clazz, Integer id) {
    return null;
  }

  public List<DataObject> retrieve(String datasource, Class clazz,
      String constraints) {
    return Collections.emptyList();
  }

  public LightweightObject retrieveLw(String datasource, Class clazz, Integer id) {
    return null;
  }

  public List<LightweightObject> retrieveLw(String datasource, Class clazz,
      String constraints) {
    return Collections.emptyList();
  }

  public List<DataObjectUpdate> store(String datasource, Class clazz,
      DataObject root) {
    throw new UnsupportedOperationException();
  }

  public void remove(String datasource, Class clazz, DataObject root) {
    throw new UnsupportedOperationException();
  }

  public void remove(String datasource, Class clazz, Integer id) {
    throw new UnsupportedOperationException();
  }
}