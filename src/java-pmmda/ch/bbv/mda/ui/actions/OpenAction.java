/* copyright 2000-2004 Marcel Baumann 
 * Licensed under the Apache License, Version 2.0 (the "Licence); 
 * you may not use this file except in compliance with the License. 
 * $Id$
 */
package ch.bbv.mda.ui.actions;

import java.io.File;

import javax.swing.JFileChooser;

import ch.bbv.explorer.Explorer;
import ch.bbv.explorer.actions.ExplorerOpenAction;
import ch.bbv.explorer.actions.SimpleFileFilter;
import ch.bbv.mda.MetaModel;
import ch.bbv.mda.TreeChecker;
import ch.bbv.mda.persistency.PersistencyException;
import ch.bbv.mda.persistency.PersistencyHandler;
import ch.bbv.mda.persistency.PersistencyHandlerFactory;
import ch.bbv.mda.ui.TreeConstructor;

/**
 * The open action implements the loading of a complete meta model. The model 
 * is first loaded from the persistency storage and a graph of meta objects are 
 * instantiated. Second the elements are completed with missing requested tagged 
 * values - for example if new cartridges were defined -. Last the explorer tree
 * is constructed.
 * @author MarcelBaumann
 * @version $Revision$
 */
public class OpenAction extends ExplorerOpenAction {
  public void initialize() {
    JFileChooser fileChooser = getFileChooser();
    String[] mof1_3s = { "zargo" };
    String[] mof1_4s = { "zuml", "ecore" };
    String[] xmls = { "xml", "ser" };
		String[] serials = { "ser" };
		fileChooser.addChoosableFileFilter(new SimpleFileFilter(mof1_3s,
				"MOF 1.3 models (*.zargo)"));
    fileChooser.addChoosableFileFilter(new SimpleFileFilter(serials,
        "Java serialized models (*.ser)"));
    fileChooser.addChoosableFileFilter(new SimpleFileFilter(mof1_4s,
        "MOF 1.4 models (*.zuml, *.ecore)"));
    fileChooser.addChoosableFileFilter(new SimpleFileFilter(xmls,
        "XML models (*.xml, *.ser)"));
  }

  public void execute() {
    try {
    	openModel(getSelectedFile());
		} catch (PersistencyException pe) {
			System.out.println("File Not Found");
		}
  }
  
  /** Opens the model in the given file programatically.
   * @param file file containing the model to open.
   * @throws PersistencyException an exception was encountered when loading the 
   * model.
   */ 
  public void openModel(File file) throws PersistencyException {
		PersistencyHandlerFactory factory = new PersistencyHandlerFactory();
		PersistencyHandler handler = factory.getHandler(file);
		MetaModel model = handler.readModel(file);
		model.accept(new TreeChecker());
		TreeConstructor constructor = new TreeConstructor(getExplorer().getNodeTypeFactory());
		model.accept(constructor);
		model.setFilename(file.toString());
		Explorer explorer = getExplorer();
		explorer.setRoot(constructor.getRoot());
		explorer.getMenuBar().documentSaved();
  }
}
