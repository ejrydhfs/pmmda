/* 
 * copyright 2000-20056 Marcel Baumann 
 * Licensed under the Apache License, Version 2.0 (the "Licence); 
 * you may not use this file except in compliance with the License. 
 * $Id$
 */
package ch.bbv.pim.ui.actions;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.JOptionPane;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ch.bbv.explorer.Explorer;
import ch.bbv.explorer.actions.ExplorerSaveAction;
import ch.bbv.pim.formats.ContactEmitter;
import ch.bbv.pim.ui.PimApplication;
import ch.bbv.pim.ui.PimDocument;
import ch.bbv.pim.ui.PimExplorer;

/**
 * The save action for the user interface of <em>pmMDA</em> application.
 * @author MarcelBaumann
 * @version $Revision$
 */
public class SaveAction extends ExplorerSaveAction {
  /**
   * Serial version identifier coded as back compatibility date.
   */
  private static final long serialVersionUID = 20041201;

  /**
   * Logger of the instances of the class.
   */
  private static Log log = LogFactory.getLog(SaveAction.class);

  /**
   * Default constructor.
   * @param explorer explorer owning the global action.
   */
  public SaveAction(Explorer explorer) {
    super(explorer);
  }

  public boolean execute() {
    PimExplorer explorer = (PimExplorer) getExplorer();
    switchWaitCursor(true);
    try {
      PimDocument document = explorer.getDocument();
      File file = document.getFile();
      if (file != null) {
        ContactEmitter emitter = PimApplication.getApplication()
            .getFormatManager().getEmitter(file.getCanonicalPath());
        if (emitter != null) {
          emitter.emit(document.getAddressBook(), file);
          document.setFile(file);
          explorer.setDocument(document);
        } else {
          JOptionPane.showMessageDialog(explorer.getFrame(),
              "File extension not supported in the application",
              "File extension error", JOptionPane.ERROR_MESSAGE);

        }
      }
    } catch (IOException e) {
      log.error(e);
      lastResult = false;
      return lastResult;
    } finally {
      switchWaitCursor(false);
    }
    return lastResult;
  }

  public void actionPerformed(ActionEvent action) {
    execute();
  }
}