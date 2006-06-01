/* copyright 2000-2006 Marcel Baumann 
 * Licensed under the Apache License, Version 2.0 (the "Licence"); 
 * you may not use this file except in compliance with the License. 
 * $Id$ 
 */
package ch.bbv.explorer;

import java.io.File;

/**
 * The interface defines the contract of any document loaded in an explorer. The
 * state property is used to determine the status of general menu action such as
 * exit, open, open as, and save. A document has two attributes describing its
 * states. The first attribute describes if the document was at least once saved
 * in a persistence store. The second attribute describes if the document
 * contains data which is not persisted. These two attributes define a space of
 * four legal states for any document.
 * @author MarcelBaumann
 * @version $Revision$
 */
public interface ExplorerDocument {

  /**
   * Returns the modified flag of the document.
   * @return true if the document was modified otherwise false
   * @see #setModified(boolean)
   */
  boolean isModified();

  /**
   * Sets the state of the document.
   * @param modified flag indicating if the document was modified since it was
   *          loaded
   * @see #isModified()
   */
  void setModified(boolean modified);

  /**
   * Returns the new flag of the document.
   * @return true if the document is new othewise false
   * @see #setNew(boolean)
   */
  boolean isNew();

  /**
   * Sets the state of the document.
   * @param news flag indicating if the document is a new one
   * @see #isNew()
   */
  void setNew(boolean news);

  /**
   * Returns the name of the document.
   * @return the name of the document
   * @see #setName(String)
   */
  String getName();

  /**
   * Sets the name of the document.
   * @param name name of the document
   * @see #getName()
   */
  void setName(String name);

  /**
   * Returns the file or directory where the persistent representation of the
   * document is located.
   * @return the file or directory containing the persistent representation
   */
  File getFile();
}
