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
   * Name of the document as displayed on the user interface.
   */
  private String name;

  /**
   * File or directory in which the document is stored.
   */
  private File file;

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

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  /**
   * @inheritDoc
   * @see #setFile(File)
   */
  public File getFile() {
    return file;
  }

  /**
   * Set the file in which the document is stored.
   * @param file file containing the document
   * @see #getFile()
   */
  public void setFile(File file) {
    this.file = file;
  }
}
