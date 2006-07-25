/*
 * copyright 2000-2006 Marcel Baumann 
 * Licensed under the Apache License, Version 2.0 (the "Licence); 
 * you may not use this file except in compliance with the
 * License. $Id$
 */
package ch.bbv.mda.ui.actions;

import java.io.File;
import java.text.MessageFormat;

import javax.swing.JOptionPane;

import ch.bbv.explorer.Explorer;
import ch.bbv.explorer.actions.ExplorerFileAction;
import ch.bbv.explorer.resources.Messages;
import ch.bbv.mda.persistence.PersistenceException;
import ch.bbv.mda.resources.MessagesMda;
import ch.bbv.mda.ui.MdaDocument;
import ch.bbv.mda.ui.MdaExplorer;

import com.jgoodies.validation.ValidationResult;

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
   * Default constructor.
   * @param explorer explorer owning the global action
   */
  public OpenAction(Explorer explorer) {
    super(Messages.actionOpen, explorer);
    shouldSaveAndProceed = true;
    MdaFileTypes.getInstance().addFileFilters(getFileChooser());
  }

  public boolean execute() {
    File file = getSelectedFile();
    MdaExplorer explorer = (MdaExplorer) getExplorer();
    switchWaitCursor(true);
    try {
      MdaDocument document = new MdaDocument();
      String name = file.getName();
      if (name != null && (name.lastIndexOf('.') == -1 || name.endsWith("."))) {
        name = MdaFileTypes.getInstance().getBestMatchingExtension(
            getFileChooser().getFileFilter().getDescription());
        if (name != null && !file.getName().endsWith(".")) {
          name = "." + name;
        }
        if (name != null)
          file = new File(file.getPath() + name);
      }
      if (name == null) {
        JOptionPane.showMessageDialog(explorer.getFrame(),
            MessagesMda.msgIllegalFileNameOrType, Messages.titleError,
            JOptionPane.ERROR_MESSAGE);
        lastResult = false;
        return lastResult;
      }
      ValidationResult vResults = new ValidationResult();
      document.load(file, vResults);
      explorer.setDocument(document);
      explorer.getValidationPanel().setResults(vResults);
    } catch (PersistenceException e) {
      JOptionPane.showMessageDialog(getExplorer().getFrame(), MessageFormat
          .format(MessagesMda.msgIllegalFilename, file.getName()),
          Messages.titleError, JOptionPane.ERROR_MESSAGE);
      lastResult = false;
      return lastResult;
    } finally {
      switchWaitCursor(false);
    }
    lastResult = true;
    return lastResult;
  }

  @Override
  protected int display() {
    return getFileChooser().showOpenDialog(getExplorer().getFrame());
  }
}