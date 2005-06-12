/* copyright 2000-2005 Marcel Baumann 
 * Licensed under the Apache License, Version 2.0 (the "Licence"); 
 * you may not use this file except in compliance with the License. 
 * $Id$ 
 */
package ch.bbv.explorer;

import java.io.File;

public interface ExplorerDocument {
  /**
   * Defines the states a document can have.
   */
  enum State {
    NONE, CREATED, CLEAN, MODIFIED
  };

  /**
   * Returns the state of the document.
   * @return state of the document.
   * @see #setState(State)
   */
  State getState();

  /**
   * Sets the state of the document.
   * @param state new state of the document.
   * @see #getState()
   */
  void setState(State state);

  /**
   * Returns the file containing the persistent representation of the file.
   * @return the file of the document if defined otherwise null.
   */
  File getFile();
}
