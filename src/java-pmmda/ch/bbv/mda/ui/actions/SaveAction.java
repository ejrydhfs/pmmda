/* 
 * copyright 2000-2005 Marcel Baumann 
 * Licensed under the Apache License, Version 2.0 (the "Licence); 
 * you may not use this file except in compliance with the License. 
 * $Id$
 */
package ch.bbv.mda.ui.actions;

import java.awt.event.ActionEvent;
import java.io.IOException;

import javax.swing.JOptionPane;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ch.bbv.explorer.actions.ExplorerSaveAction;
import ch.bbv.mda.persistency.PersistencyException;
import ch.bbv.mda.ui.MdaDocument;
import ch.bbv.mda.ui.MdaExplorer;
import ch.bbv.utilities.ResourceUtility;

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

  public void execute() {
    try {
      MdaExplorer explorer = (MdaExplorer) getExplorer();
      MdaDocument document = explorer.getDocument();
      String filename = document.getModel().getFilename();
      if (filename != null) {
        filename = filename.trim();
        if (filename.length() > 0) {
          explorer.getDocument().save();
          getExplorer().getMenuBar().documentSaved();
        } else {
          JOptionPane.showMessageDialog(getExplorer().getFrame(),
              ResourceUtility
                  .getString("mda.ui.errorMsg.NO_FILE_DEFINED_ERROR"),
              ResourceUtility.getString("mda.ui.errorMsg.ERROR"),
              JOptionPane.ERROR_MESSAGE);
        }
      }
    } catch (PersistencyException e) {
      log.error(e);
      JOptionPane.showMessageDialog(getExplorer().getFrame(), ResourceUtility
          .getString("mda.ui.sorryMsg.UNSUPPORTED_FILE_TYPE_FOR_SAVE"),
          ResourceUtility.getString("mda.ui.sorryMsg.SORRY"),
          JOptionPane.ERROR_MESSAGE);
    } catch (NullPointerException e) {
      log.error(e);
      JOptionPane.showMessageDialog(getExplorer().getFrame(), ResourceUtility
          .getString("mda.ui.sorryMsg.UNSUPPORTED_FILE_TYPE_FOR_SAVE"),
          ResourceUtility.getString("mda.ui.sorryMsg.SORRY"),
          JOptionPane.ERROR_MESSAGE);
    } catch (IOException e) {
      log.error(e);
    }
  }

  public void actionPerformed(ActionEvent action) {
    execute();
  }
}