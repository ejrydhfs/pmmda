package ch.bbv.mda.persistence.eclipse;


import java.io.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.emf.importer.rose.builder.UnitTreeNode;

import ch.bbv.mda.MetaFactory;
import ch.bbv.mda.MetaModel;
import ch.bbv.mda.persistence.PersistenceException;
import ch.bbv.mda.persistence.PersistenceHandler;
import ch.bbv.mda.persistence.rose.MdaRoseUtil;
import ch.bbv.mda.persistence.rose.RoseTransformer;


/**
 * @author AdrianBachofen
 *
 * Reads *.mdl (Rational Rose) files and creates the MetaModel
 */
public class RosePersistenceHandler implements PersistenceHandler {
  private static Log log = LogFactory.getLog(RosePersistenceHandler.class);
  protected MdaRoseUtil roseUtil;
  protected UnitTreeNode unitTree;
  protected RoseTransformer roseTransformer;

  public RosePersistenceHandler(){
  }
  
  public MetaModel readModel(File file) throws PersistenceException {
  	roseTransformer = new RoseTransformer();
    MetaModel metaModel = new MetaModel();
    MetaFactory.getInstance().initialize(metaModel);
    unitTree = roseTransformer.loadRoseModel(file.toString());
    roseTransformer.synchronize(unitTree, metaModel);
  	return metaModel;
  }
  
  public void writeModel(MetaModel model, File file) throws PersistenceException {
	  if (file.exists()) {
	    updateModel(model, file);
	  }
  }

  public void importPackage(MetaModel targetProject, File file, String packageName) {
  }

  public void updateModel(MetaModel model, File file) throws PersistenceException {
  	throw new PersistenceException();
  }

}

