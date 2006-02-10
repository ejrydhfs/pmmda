/* 
 * copyright 2000-2006 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence");
 * you may not use this file except in compliance with the License.
 * $Id$
 */
package ch.bbv.pim.ui;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ch.bbv.explorer.*;

/**
 * The PIM explorer provides an explorer user interface to manipulate the
 * organizations and contacts. The explorer provides standard actions such as
 * <dl>
 * <dt>Open</dt>
 * <dd>Opens a file containing an address book and loads the address book and
 * the contacts in the explorer.</dd>
 * <dt>Save</dt>
 * <dd>Saves the address book in the file.</dd>
 * <dt>Save As</dt>
 * <dd>Saves the address book in the file. The file name and format is inquired
 * from the user.</dd>
 * <dt>Exit</dt>
 * <dd>Exists the application. If some address books are not saved the user is
 * asked if he wants to save his modifications.</dd>
 * </dl>
 * @author Marcel Baumann
 * @version $Revision$
 */
public class PimExplorer extends Explorer {
  /**
   * Logger instance of the class.
   */
  private static Log log = LogFactory.getLog(PimExplorer.class);

  /*
   * User interface node type factory of the explorer instances.
   */
  private static UiNodeTypeFactory factory;

  /**
   * The document displayed in the explorer.
   */
  private ExplorerDocument document;

  /**
   * Creates a new MDA explorer.
   * @param title title of the explorer frame.
   * @return the newly created user explorer.
   */
  public static PimExplorer create(String title) {
    Class[] types = { String.class };
    Object[] arguments = { title };
    PimExplorer explorer = (PimExplorer) Explorer.createAndShow(
        PimExplorer.class, types, arguments);
    return explorer;
  }

  /**
   * Constructor of the class.
   * @param title title of the explorer frame.
   */
  public PimExplorer(String title) {
    super(factory, null, null);
    this.setTitle(title);
    document = new ExplorerDocumentImpl();
    if (log.isInfoEnabled()) {
      log.info("Start initializing PIM explorer");
    }
  }

  /**
   * Returns the document of the explorer.
   * @return the document of the explorer.
   */
  public ExplorerDocument getDocument() {
    return document;
  }

  /**
   * Initializes the explorer. The initial tree of lightweight objects is
   * created.
   */
  protected void initialize() {
    ExplorerMenuBar menuBar = new ExplorerMenuBar(this);
    setMenuBar(menuBar);
  }
}