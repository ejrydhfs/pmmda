/* copyright 2000-2006 Marcel Baumann 
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
   * Flag indicating if the document was modified in the application.
   */
  private boolean modified;

  /**
   * Flag indicating if the document was loaded from a persistent store or is a
   * new one.
   */
  private boolean news;

  /**
   * Default constructor of the class.
   */
  public ExplorerDocumentImpl() {
    modified = false;
    news = true;
  }

  public boolean isModified() {
    return modified;
  }

  public boolean isNew() {
    return news;
  }

  public void setModified(boolean modified) {
    this.modified = modified;
  }

  public void setNew(boolean news) {
    this.news = news;
  }

  public File getFile() {
    return null;
  }
}
