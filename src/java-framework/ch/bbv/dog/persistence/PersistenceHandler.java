/* copyright 2000-2006 Marcel Baumann 
 * Licensed under the Apache License, Version 2.0 (the "Licence"); 
 * you may not use this file except in compliance with the License. 
 * $Id$ 
 */
package ch.bbv.dog.persistence;

import ch.bbv.dog.DataObjectHandler;

/**
 * The handler for persistence of data objects.
 * @author MarcelBaumann
 * @version $Revision$
 */
public interface PersistenceHandler extends DataObjectHandler {
  /**
   * The modes of the persistence handler.
   */
  public enum Mode {
    REPLACE, SYNCHRONIZE, DETACH
  }
}
