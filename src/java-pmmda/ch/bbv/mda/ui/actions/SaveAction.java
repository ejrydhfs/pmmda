/* copyright 2000-2004 Marcel Baumann 
 * Licensed under the Apache License, Version 2.0 (the "Licence); 
 * you may not use this file except in compliance with the License. 
 * $Id$
 */
package ch.bbv.mda.ui.actions;

import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.JOptionPane;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ch.bbv.explorer.actions.ExplorerGlobalActionImpl;
import ch.bbv.utilities.ResourceUtility;
import ch.bbv.mda.MetaModel;
import ch.bbv.mda.persistency.PersistencyException;
import ch.bbv.mda.persistency.PersistencyHandler;
import ch.bbv.mda.persistency.PersistencyHandlerFactory;

/**
 * The save action for the user interface of <em>pmMDA</em> application.
 * @author MarcelBaumann
 * @version $Revision$
 */
public class SaveAction extends ExplorerGlobalActionImpl {
  /**
   * Logger of the instances of the class.
   */
  private static Log log = LogFactory.getLog(SaveAction.class);

  /**
   * Default constructor.
   */
  public SaveAction() {
    super("Save");
    initialize();
  }

  public void execute() {
    try {
      MetaModel project = (MetaModel) getExplorer().getRoot().getDataObject();
      PersistencyHandlerFactory factory = new PersistencyHandlerFactory();
      String filename = project.getFilename();
      if (filename != null) {
        filename = filename.trim();
        if (filename.length() > 0) {
          File file = new File(project.getFilename());
          PersistencyHandler handler = factory.getHandler(file);
          handler.writeModel(project, file);
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
    }
  }

  public void actionPerformed(ActionEvent action) {
    execute();
  }
}