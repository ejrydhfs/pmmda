/*
 * copyright 2000-2006 Marcel Baumann 
 * Licensed under the Apache License, Version 2.0 (the "Licence); 
 * you may not use this file except in compliance with the
 * License. $Id$
 */
package ch.bbv.pim.ui.actions;

import java.io.File;
import java.io.IOException;

import javax.swing.JOptionPane;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ch.bbv.dog.AbstractVisitorFunctor;
import ch.bbv.dog.DataObjectType;
import ch.bbv.dog.functors.ModifiedFlagFunctor;
import ch.bbv.explorer.Explorer;
import ch.bbv.explorer.actions.ExplorerFileAction;
import ch.bbv.pim.AddressBook;
import ch.bbv.pim.formats.ContactParser;
import ch.bbv.pim.ui.PimApplication;
import ch.bbv.pim.ui.PimDocument;
import ch.bbv.pim.ui.PimExplorer;

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
   * Serial version identifier.
   */
  private static final long serialVersionUID = -1266462566763460770L;

  /**
   * Name of the action as displayed in the menu.
   */
  private static final String ACTION = "Open...";

  /**
   * Logger instance of the class.
   */
  private static Log log = LogFactory.getLog(OpenAction.class);

  /**
   * Constructor of the class.
   * @param explorer explorer owning the global action
   */
  public OpenAction(Explorer explorer) {
    super(ACTION, explorer);
    PimFileTypes.getInstance().addFileFilters(getFileChooser());
    shouldSaveAndProceed = true;
  }

  public boolean execute() {
    File file = getSelectedFile();
    PimExplorer explorer = (PimExplorer) getExplorer();
    try {
      if (!file.exists()) {
        JOptionPane.showMessageDialog(getExplorer().getFrame(),
            "Selected file does not exists: " + file.getCanonicalPath(),
            "File name error", JOptionPane.ERROR_MESSAGE);
        return true;

      }
      switchWaitCursor(true);
      ContactParser parser = PimApplication.getApplication().getFormatManager()
          .getParser(file.getCanonicalPath());
      if (parser != null) {
        PimDocument document = new PimDocument();
        AddressBook book = document.getAddressBook();
        parser.synchronize(book, file);
        // sets the modified flag back to false
        DataObjectType type = explorer.getManager().getType(book.getClass());
        ModifiedFlagFunctor functor = new ModifiedFlagFunctor();
        functor.setModified(false);
        AbstractVisitorFunctor visitor = type.createVisitorInstance();
        visitor.setFunctor(functor);
        DataObjectType.invokeVisitor(book.getClass(), book, visitor);
        document.setFile(file);
        explorer.setDocument(document);
      } else {
        JOptionPane.showMessageDialog(getExplorer().getFrame(),
            "File extension not supported in the application",
            "File extension error", JOptionPane.ERROR_MESSAGE);

      }
    } catch (IOException e) {
      log.error("Error when reading file");
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