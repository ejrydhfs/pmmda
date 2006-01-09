/* copyright 2005 Adrian Bachofen
 * Licensed under the Apache License, Version 2.0 (the "Licence");
 * you may not use this file except in compliance with the License.
 * $Id$
 */
package ch.bbv.mda.persistence.eclipse;


import java.io.*;

import javax.swing.JOptionPane;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.emf.importer.rose.builder.UnitTreeNode;
import org.eclipse.emf.importer.rose.parser.RoseWriter;
import org.eclipse.emf.importer.rose.parser.RoseNode;

import ch.bbv.mda.MetaModel;
import ch.bbv.mda.persistence.PersistenceException;
import ch.bbv.mda.persistence.PersistenceHandler;
import ch.bbv.mda.persistence.rose.RoseTransformer;
import ch.bbv.mda.persistence.rose.MdaTransformer;
import ch.bbv.utilities.ResourceUtilities;


/**
 * @author AdrianBachofen
 *
 * Reads *.mdl (Rational Rose) files and creates the MetaModel
 */
public class RosePersistenceHandler implements PersistenceHandler {
  public static String TaggedValueToolIdentifier = "pmMDA";
  private static Log log = LogFactory.getLog(RosePersistenceHandler.class);

  protected UnitTreeNode unitTree;
  protected RoseTransformer roseTransformer;
  protected MdaTransformer mdaTransformer;

  public RosePersistenceHandler(){
    roseTransformer = new RoseTransformer();
    mdaTransformer = new MdaTransformer();
  }
  
  public MetaModel readModel(File file) throws PersistenceException {
    MetaModel metaModel = new MetaModel();
    try {
      unitTree = roseTransformer.loadRoseModel(file.toString(), metaModel);
      if (unitTree == null) {
        log.error("RosePersistenceHandler#readModel: illegal file - " + file);
        JOptionPane.showMessageDialog(null, ResourceUtilities
            .getString("mda.ui.sorryMsg.INVALID_FORMAT"), ResourceUtilities
            .getString("mda.ui.sorryMsg.SORRY"), JOptionPane.ERROR_MESSAGE);
        return null;
      }
    }
    catch(Exception e){
      throw new PersistenceException(e);
    }
    return metaModel;
  }
  
  public void writeModel(MetaModel model, File file) throws PersistenceException {
    if (model.getFilename() != null && model.getFilename().equals(file.toString()) && file.exists()) {
      updateModel(model, file);
    } else {
      try {
        // TODO in case of a rose model within model I could read it and then use for synchronizing diagrams
        // I have to load the file again, remove everything from the root category except the logical_presentations node and use this for synchronization.
        RoseNode vT = mdaTransformer.createVersionTree();
        RoseNode mT = mdaTransformer.createModelTree();
        mdaTransformer.synchronize(model, mT);
        RoseWriter roseWriter = new RoseWriter(file.toString(), vT, mT);
        roseWriter.write();
        String name = file.getName();
        int idx = name.lastIndexOf(".");
        if(idx > 0) name = name.substring(0, idx);
        model.setName(name);
      } catch (Exception e){
        throw new PersistenceException(e);
      }
    }
  }

  public void updateModel(MetaModel model, File file) throws PersistenceException {
    try{
      MetaModel metaModel = new MetaModel();
      unitTree = roseTransformer.loadRoseModel(file.toString(), metaModel);
      if (unitTree == null) {
        log.error("RosePersistenceHandler#updateModel: illegal file - " + file);
        JOptionPane.showMessageDialog(null, ResourceUtilities
            .getString("mda.ui.sorryMsg.INVALID_FORMAT"), ResourceUtilities
            .getString("mda.ui.sorryMsg.SORRY"), JOptionPane.ERROR_MESSAGE);
        return;
      }
      mdaTransformer.synchronize(model, roseTransformer.getRoseUtil().getModelTree());
      RoseWriter roseWriter = new RoseWriter(file.toString(), roseTransformer.getRoseUtil().getVersionTree(), roseTransformer.getRoseUtil().getModelTree());
      roseWriter.write();
      model.setName(unitTree.getName());
    } catch (Exception e){
      throw new PersistenceException(e);
    }
  }
  
}

