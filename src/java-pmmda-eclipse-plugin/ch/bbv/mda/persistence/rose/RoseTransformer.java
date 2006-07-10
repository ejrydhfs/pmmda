/* copyright 2005 Adrian Bachofen
 * Licensed under the Apache License, Version 2.0 (the "Licence");
 * you may not use this file except in compliance with the License.
 * $Id$
 */
package ch.bbv.mda.persistence.rose;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.emf.ecore.plugin.EcorePlugin;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.importer.rose.builder.UnitTreeNode;

import ch.bbv.mda.MetaModel;

import com.jgoodies.validation.ValidationResult;

/**
 * @author AdrianBachofen
 *
 * Loads and transforms a rose model into a MDA model
 */
public class RoseTransformer {
	private static Log log = LogFactory.getLog(RoseTransformer.class);
	protected MdaRoseUtil roseUtil;
    
    public RoseTransformer(){ 
      ResourceSet result = new ResourceSetImpl();
      result.getURIConverter().getURIMap().putAll(EcorePlugin.computePlatformURIMap());
      roseUtil = new MdaRoseUtil(result.getURIConverter());
      roseUtil.getRoseEcoreBuilder().noQualify = false;
      roseUtil.getRoseEcoreBuilder().unsettablePrimitive = false;
    }
	
    public UnitTreeNode loadRoseModel(String fileName, MetaModel model, ValidationResult vResults) throws Exception{
      roseUtil.loadTree(fileName, model, vResults);
      return roseUtil.getUnitTree();
    }
    
	public UnitTreeNode loadRoseModel(String file){
    	UnitTreeNode unitTree = null;
    	try{
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

    public MdaRoseUtil getRoseUtil() {
      return roseUtil;
    }
}
