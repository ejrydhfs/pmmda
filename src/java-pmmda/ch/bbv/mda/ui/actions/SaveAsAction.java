/*
 * copyright 2000-2004 Marcel Baumann Licensed under the Apache License, Version
 * 2.0 (the "Licence); you may not use this file except in compliance with the
 * License. $Id$
 */
package ch.bbv.mda.ui.actions;

import java.io.*;
import java.text.MessageFormat;
import java.util.Arrays;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ch.bbv.explorer.actions.ExplorerSaveAsAction;
import ch.bbv.explorer.actions.SimpleFileFilter;
import ch.bbv.mda.MetaModel;
import ch.bbv.mda.persistency.PersistencyException;
import ch.bbv.mda.persistency.PersistencyHandler;
import ch.bbv.mda.persistency.PersistencyHandlerFactory;
import ch.bbv.utilities.SystemUtilities;
import ch.bbv.utilities.ResourceUtility;

/**
 * The save as action for the user interface of <em>pmMDA</em> application.
 * The action semantic requests handling of various cases.
 * <ul>
 * <li>Saving a model to the XML format is implemented with XML serialization
 * open source library.</li>
 * <li>Saving a model to the zargo format requires the creation of a file
 * structure compatible to ArgoUML zargo format. The action first copies an
 * empty template to the destination directory and rename it with the user
 * specified name. When the model data is written into this file.</li>
 * </ul>
 * @author MarcelBaumann
 * @version $Revision$
 */
public class SaveAsAction extends ExplorerSaveAsAction {
  /**
   * Logger for all instance of the class.
   */
  private static Log log = LogFactory.getLog(SaveAsAction.class);

  /**
   * File extensions for models developed with ArgoUML.
   */
  private static final String[] ARGOUML_EXT = { "zargo" };

  /**
   * File extensions for models developed with ArgoUML.
   */
  private static final String[] POSEIDON_EXT = { "zuml" };

  /**
   * File extensions for models serialized to XML using the open source library
   * from CodeHaus.
   */
  private static final String[] XML_EXT = { "xml" };

  /**
   * File extensions for models serialized with the Java serialization features.
   */
  private static final String[] SERIALIZE_EXT = { "ser" };

  /**
   * File extensions for models according to xmi standard.
   */
  private static final String[] XMI_EXT = { "xmi" };

  /**
   * Resource containing the ArgoUML project template.
   */
  private static final String ZARGO_TEMPLATE = "/templates/template.zargo";

  public void initialize() {
    JFileChooser fileChooser = getFileChooser();
    fileChooser.addChoosableFileFilter(new SimpleFileFilter(ARGOUML_EXT,
        "ArgoUML, Poseidon 1.x (*.zargo)"));
    fileChooser.addChoosableFileFilter(new SimpleFileFilter(POSEIDON_EXT,
        "Poseidon 2.x (*.zuml)"));
    fileChooser.addChoosableFileFilter(new SimpleFileFilter(XML_EXT,
        "XML models (*.xml)"));
    fileChooser.addChoosableFileFilter(new SimpleFileFilter(XMI_EXT,
        "XMI models (*.xmi)"));
    fileChooser.addChoosableFileFilter(new SimpleFileFilter(SERIALIZE_EXT,
        "XML models (*.ser)"));
  }

  public void execute() {
    if (getSelectedFile().exists()) {
      String message = MessageFormat.format(ResourceUtility
          .getString("mda.ui.confirmationMsg.FILE_OVERWRITE_CONFIRMATION"),
          new Object[] { getSelectedFile().getName() });
      int no = JOptionPane.showConfirmDialog(getExplorer().getFrame(), message,
          ResourceUtility.getString("mda.ui.confirmationMsg.CONFIRMATION"),
          JOptionPane.YES_NO_OPTION);
      if (no == 1)
        return;
    }
    MetaModel model = (MetaModel) getExplorer().getRoot().getDataObject();
    File file = getSelectedFile();
    String extension = SystemUtilities.getFileExtension(file).toLowerCase();
    if (Arrays.binarySearch(ARGOUML_EXT, extension) >= 0) {
      writeArgoUmlFile(model, file);
    } else if (Arrays.binarySearch(POSEIDON_EXT, extension) >= 0) {
      writePoseidonFile(model, file);
    } else if (Arrays.binarySearch(XML_EXT, extension) >= 0) {
      writeXmlFile(model, file);
    } else if (Arrays.binarySearch(SERIALIZE_EXT, extension) >= 0) {
      writeSerializedFile(model, file);
    } else if (Arrays.binarySearch(XMI_EXT, extension) >= 0) {
      writeArgoUmlFile(model, file);
    } else {
      JOptionPane.showMessageDialog(getExplorer().getFrame(), ResourceUtility
          .getString("mda.ui.sorryMsg.ILLEGAL_FILE_NAME_OR_TYPE"),
          ResourceUtility.getString("mda.ui.sorryMsg.SORRY"),
          JOptionPane.ERROR_MESSAGE);
    }
  }

  /**
   * Writes the model to a new ArgoUML file. The file is copied from a template
   * before writing the model into it.
   * @param model model to write to the persistence file.
   * @param file file in which the model is written.
   */
  private void writeArgoUmlFile(MetaModel model, File file) {
    try {
      PersistencyHandlerFactory factory = new PersistencyHandlerFactory();
      PersistencyHandler handler = factory.getHandler(getSelectedFile());
      handler.writeModel(model, getSelectedFile());
      model.setFilename(getSelectedFile().toString());
      getExplorer().getMenuBar().documentSaved();
    } catch (PersistencyException e) {
      log.error(e);
    }
  }

  /**
   * Writes the model to a new Poseidon file. The file is copied from a template
   * before writing the model into it.
   * @param model model to write to the persistence file.
   * @param file file in which the model is written.
   */
  private void writePoseidonFile(MetaModel model, File file) {
    JOptionPane.showMessageDialog(null, "Poseidon Save Option Not Supported",
        "Not Yet Implemented", JOptionPane.ERROR_MESSAGE);
  }

  /**
   * Writes the model to a new XML file. The file is copied from a template
   * before writing the model into it.
   * @param model model to write to the persistence file.
   * @param file file in which the model is written.
   */
  private void writeXmlFile(MetaModel model, File file) {
    try {
      PersistencyHandlerFactory factory = new PersistencyHandlerFactory();
      PersistencyHandler handler = factory.getHandler(getSelectedFile());
      handler.writeModel(model, getSelectedFile());
      model.setFilename(getSelectedFile().toString());
      getExplorer().getMenuBar().documentSaved();
    } catch (PersistencyException e) {
      log.error(e);
    }
  }

  /**
   * Writes the model to a new Poseidon file. The file is copied from a template
   * before writing the model into it.
   * @param model model to write to the persistence file.
   * @param file file in which the model is written.
   */
  private void writeSerializedFile(MetaModel model, File file) {
    try {
      PersistencyHandlerFactory factory = new PersistencyHandlerFactory();
      PersistencyHandler handler = factory.getHandler(getSelectedFile());
      handler.writeModel(model, getSelectedFile());
      model.setFilename(getSelectedFile().toString());
      getExplorer().getMenuBar().documentSaved();
    } catch (PersistencyException e) {
      log.error(e);
    }
  }
}