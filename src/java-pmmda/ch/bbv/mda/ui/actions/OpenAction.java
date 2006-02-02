/*
 * copyright 2000-2005 Marcel Baumann 
 * Licensed under the Apache License, Version 2.0 (the "Licence); 
 * you may not use this file except in compliance with the
 * License. $Id$
 */
package ch.bbv.mda.ui.actions;

import java.awt.Cursor;
import java.io.File;
import java.text.MessageFormat;

import javax.swing.JOptionPane;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ch.bbv.explorer.Explorer;
import ch.bbv.explorer.actions.ExplorerFileAction;
import ch.bbv.mda.persistence.PersistenceException;
import ch.bbv.mda.ui.MdaDocument;
import ch.bbv.mda.ui.MdaExplorer;
import ch.bbv.utilities.ResourceUtilities;

/**
 * The open action implements the loading of a complete meta model. The model is
 * first loaded from the persistency storage and a graph of meta objects are
 * instantiated. Second the elements are completed with missing requested tagged
 * values - for example if new cartridges were defined -. Last the explorer tree
 * is constructed.
 * @author MarcelBaumann
 * @version $Revision$
 */
public class OpenAction extends ExplorerFileAction {
  /**
   * Serial version identifier coded as back compatibility date.
   */
  private static final long serialVersionUID = 20041201;

  /**
   * Logger for all instances of the class.
   */
  private static Log log = LogFactory.getLog(OpenAction.class);

  /**
   * Name of the action as displayed in the menu.
   */
  private static final String ACTION = "Open...";

  /**
   * Default constructor.
   * @param explorer explorer owning the global action.
   */
  public OpenAction(Explorer explorer) {
    super(ACTION, explorer);
    shouldSaveAndProceed = true;
    FileTypes.addFileFilters(getFileChooser());
  }

  public boolean execute() {
    File file = getSelectedFile();
    MdaExplorer explorer = (MdaExplorer) getExplorer();
    explorer.getFrame().setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
    try {
      MdaDocument document = new MdaDocument();
      String name = file.getName();
      if(name != null && (name.lastIndexOf('.') == -1 || name.endsWith("."))) { // in case where user thinks that the extension comes from the file filter we get the first match from the filter.
        name = FileTypes.getBestMatchingExtension(getFileChooser().getFileFilter().getDescription());
        if(name != null && !file.getName().endsWith(".")) { name = "." + name; }
        if(name != null) file = new File(file.getPath()+name);
      }
      if(name == null) {
        JOptionPane.showMessageDialog(explorer.getFrame(), ResourceUtilities
            .getString("mda.ui.sorryMsg.ILLEGAL_FILE_NAME_OR_TYPE"),
            ResourceUtilities.getString("mda.ui.errorMsg.ERROR"),
            JOptionPane.ERROR_MESSAGE);
        lastResult = false;
        return lastResult;
      }
      document.load(file);
      explorer.setDocument(document);
    } catch (PersistenceException e) {
      JOptionPane.showMessageDialog(getExplorer().getFrame(), MessageFormat.format(ResourceUtilities
          .getString("mda.ui.errorMsg.ILLEGAL_FILENAME_ERROR"), file.getName()),
          ResourceUtilities.getString("mda.ui.errorMsg.ERROR"),
          JOptionPane.ERROR_MESSAGE);
      lastResult = false;
      return lastResult;
    } catch (NullPointerException e) {
      JOptionPane.showMessageDialog(getExplorer().getFrame(), MessageFormat.format(ResourceUtilities
          .getString("mda.ui.errorMsg.ILLEGAL_FILENAME_ERROR"), file.getName()),
          ResourceUtilities.getString("mda.ui.errorMsg.ERROR"),
          JOptionPane.ERROR_MESSAGE);
      lastResult = false;
      return lastResult;
    } finally {
      explorer.getFrame().setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    }
    lastResult = true;
    return lastResult;
  }
  
  protected int display() {
    return getFileChooser().showOpenDialog(getExplorer().getFrame());
  }
}