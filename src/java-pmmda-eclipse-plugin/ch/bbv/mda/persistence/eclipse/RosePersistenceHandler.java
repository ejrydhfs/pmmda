package ch.bbv.mda.persistence.eclipse;


import java.io.*;

import javax.swing.JOptionPane;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.emf.importer.rose.builder.UnitTreeNode;
import org.eclipse.emf.importer.rose.parser.RoseWriter;
import org.xml.sax.SAXException;

import ru.novosoft.uml.model_management.MModel;

import ch.bbv.explorer.ExplorerDocument.State;
import ch.bbv.mda.generators.ItemFactory;
import ch.bbv.mda.generators.TreeChecker;
import ch.bbv.mda.MetaModel;
import ch.bbv.mda.persistence.PersistenceException;
import ch.bbv.mda.persistence.PersistenceHandler;
import ch.bbv.mda.persistence.PersistenceHandlerFactory;
import ch.bbv.mda.persistence.rose.MdaRoseUtil;
import ch.bbv.mda.persistence.rose.RoseTransformer;
import ch.bbv.utilities.ResourceUtility;


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
    roseTransformer = new RoseTransformer();
  }
  
  public MetaModel readModel(File file) throws PersistenceException {
    MetaModel metaModel = new MetaModel();
    //MetaFactory.getInstance().initialize(metaModel); TODO verify whether it works correctly
    unitTree = roseTransformer.loadRoseModel(file.toString());
    if (unitTree == null) {
      log.error("NsUmlPersistenceHandler#readModel: illegal file - " + file);
      JOptionPane.showMessageDialog(null, ResourceUtility
          .getString("mda.ui.sorryMsg.INVALID_FORMAT"), ResourceUtility
          .getString("mda.ui.sorryMsg.SORRY"), JOptionPane.ERROR_MESSAGE);
      return null;
    }
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
    roseTransformer = new RoseTransformer();
    UnitTreeNode uTree = roseTransformer.loadRoseModel(file.toString());
    roseTransformer.synchronize(uTree, model);
    // TODO write synchronized model
    try{
      new RoseWriter(file.toString(), null, uTree.getRoseNode());
    } catch (Exception e){
    }
  }
  
}

