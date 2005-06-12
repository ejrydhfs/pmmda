/* copyright 2000-2005 Marcel Baumann 
 * Licensed under the Apache License, Version 2.0 (the "Licence"); 
 * you may not use this file except in compliance with the License. 
 * $Id$ 
 */
package ch.bbv.explorer;

import java.io.File;

/**
 * The default implementation of an explorer document.
 * @author MarcelBaumann
 * @version $Revision$
 */
public class ExplorerDocumentImpl implements ExplorerDocument {

  /**
   * State of the document.
   */
  private State state;

  public State getState() {
    return state;
  }

  public void setState(State state) {
    this.state = state;
  }

  public File getFile() {
    return null;
  }
}
