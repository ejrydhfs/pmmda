/* copyright 2000-2004 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence);
 * you may not use this file except in compliance with the License.
 * $Id$
 */
package ch.bbv.dog;

import java.util.Collections;
import java.util.List;

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

  public List retrieve(String datasource, Class clazz, String constraints) {
    return Collections.EMPTY_LIST;
  }

  public LightweightObject retrieveLw(String datasource, Class clazz, Integer id) {
    return null;
  }

  public List retrieveLw(String datasource, Class clazz, String constraints) {
    return Collections.EMPTY_LIST;
  }

  public List store(String datasource, Class clazz, DataObject root) {
    throw new UnsupportedOperationException();
  }

  public void remove(String datasource, Class clazz, DataObject root) {
    throw new UnsupportedOperationException();
  }

  public void remove(String datasource, Class clazz, Integer id) {
    throw new UnsupportedOperationException();
  }

  public DataObject deepCopy(Class clazz, DataObject object) {
    return null;
  }

  public void register(DataObjectType type) {
    // empty method
  }

  public DataObjectType getType(DataObject object) {
    return null;
  }
}