/* copyright 2000-2006 Marcel Baumann 
 * Licensed under the Apache License, Version 2.0 (the "Licence"); 
 * you may not use this file except in compliance with the License. 
 * $Id$ 
 */
package ch.bbv.pim.ui;

import java.io.File;

import ch.bbv.explorer.ExplorerDocument;
import ch.bbv.explorer.ExplorerDocumentImpl;
import ch.bbv.pim.AddressBook;

public class PimDocument extends ExplorerDocumentImpl implements
    ExplorerDocument {
  /**
   * File in which the model is stored.
   */
  private File file;

  private AddressBook addressBook;

  /**
   * Default constructor of the class.
   */
  public PimDocument() {
    addressBook = new AddressBook();
    addressBook.setName("contacts");
  }

  public AddressBook getAddressBook() {
    return addressBook;
  }

  public File getFile() {
    return file;
  }

  public void setFile(File file) {
    this.file = file;
  }
}
