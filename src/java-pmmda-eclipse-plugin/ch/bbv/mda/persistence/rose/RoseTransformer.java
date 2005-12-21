/*
 * Created on Aug 1, 2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package ch.bbv.mda.persistence.rose;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.plugin.EcorePlugin;
import org.eclipse.emf.importer.rose.builder.UnitTreeNode;

import ch.bbv.mda.MetaModel;

/**
 * @author AdrianBachofen
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class RoseTransformer {
	private static Log log = LogFactory.getLog(RoseTransformer.class);
	protected MdaRoseUtil roseUtil;
	
    public RoseTransformer(){ 
    }
	
	public UnitTreeNode loadRoseModel(String file){
    	UnitTreeNode unitTree = null;
    	try{
            ResourceSet result = new ResourceSetImpl();
            result.getURIConverter().getURIMap().putAll(EcorePlugin.computePlatformURIMap());
            roseUtil = new MdaRoseUtil(result.getURIConverter());
            roseUtil.getRoseEcoreBuilder().noQualify = false;
            roseUtil.getRoseEcoreBuilder().unsettablePrimitive = false;
            unitTree = roseUtil.createRoseUnitTreeAndTable(file.toString(), null);
            roseUtil.createExtent4RoseUnitTree(unitTree);
            // TODO Comment next line out for release version 
            //roseUtil.showRoseUnitTree(unitTree);
    	}
    	catch(Exception e){
        	log.error(e);
    	}
        return unitTree;
	}

	public void synchronize(UnitTreeNode source, MetaModel target) {
		try{
			roseUtil.processUnitTree(source, target);
		}
		catch(Exception e){
	    	log.error(e);
		}
	}

}
