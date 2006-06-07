/* 
 * copyright 2000-20056 Marcel Baumann 
 * Licensed under the Apache License, Version 2.0 (the "Licence); 
 * you may not use this file except in compliance with the License. 
 * $Id$
 */
package ch.bbv.mda.ui.actions;

import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.JOptionPane;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ch.bbv.explorer.Explorer;
import ch.bbv.explorer.ExplorerDocument;
import ch.bbv.explorer.actions.ExplorerSaveAction;
import ch.bbv.explorer.resources.Messages;
import ch.bbv.mda.persistence.PersistenceException;
import ch.bbv.mda.resources.MessagesMda;
import ch.bbv.mda.ui.MdaDocument;
import ch.bbv.mda.ui.MdaExplorer;

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
   * @param explorer explorer owning the global action
   */
  public SaveAction(Explorer explorer) {
    super(explorer);
  }

  public boolean execute() {
    MdaExplorer explorer = (MdaExplorer) getExplorer();
    explorer.getFrame().setCursor(
        Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
    try {
      MdaDocument document = explorer.getDocument();
      String filename = document.getModel().getFilename();
      if (filename != null) {
        filename = filename.trim();
        if (filename.length() > 0) {
          explorer.getDocument().save();
        } else {
          JOptionPane.showMessageDialog(explorer.getFrame(),
              MessagesMda.msgIllegalFileNameOrType, Messages.titleError,
              JOptionPane.ERROR_MESSAGE);
        }
      }
    } catch (PersistenceException e) {
      log.error(e);
      if (e.getCause() instanceof FileNotFoundException) {
        JOptionPane.showMessageDialog(null, e.getLocalizedMessage(),
            Messages.titleWarning, JOptionPane.ERROR_MESSAGE);
        lastResult = false; // This cause indicates that writter attempt to
        // write a locked file.
        return lastResult;
      }
      JOptionPane.showMessageDialog(explorer.getFrame(),
          MessagesMda.msgUnsupportedFileType, Messages.titleWarning,
          JOptionPane.ERROR_MESSAGE);
      lastResult = false;
      return lastResult;
    } catch (NullPointerException e) {
      log.error(e);
      JOptionPane.showMessageDialog(explorer.getFrame(),
          MessagesMda.msgUnsupportedFileType, Messages.titleWarning,
          JOptionPane.ERROR_MESSAGE);
      lastResult = false;
      return lastResult;
    } catch (IOException e) {
      log.error(e);
      lastResult = false;
      return lastResult;
    } finally {
      explorer.getFrame().setCursor(
          Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    }
    ExplorerDocument document = explorer.getDocument();
    document.setModified(false);
    explorer.getMenuBar().documentChanged(document);
    lastResult = true;
    return lastResult;
  }

  @Override
  public void actionPerformed(ActionEvent action) {
    execute();
  }
}