/**
 * <copyright> 
 *
 * Copyright (c) 2002-2005 bbv Software Services AG
 * All rights reserved.   This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: 
 *   bbv - Adrian Bachofen
 *
 * </copyright>
 *
 * $Id$
 */
package ch.bbv.mda.persistence.rose;

import java.util.List;
import java.util.Map;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.resource.URIConverter;
import org.eclipse.emf.importer.rose.builder.*;
import org.eclipse.emf.importer.rose.parser.*;

import ch.bbv.mda.MetaModel;

import com.jgoodies.validation.ValidationResult;

/**
 * @author AdrianBachofen
 *
 * Adds support functions to the currently available RoseUtil functions.
 * These support functions are handier to me than those already available.
 */
public class MdaRoseUtil extends RoseUtil
{
  
  public MdaRoseUtil(URIConverter uriConverter)
  {
    super(uriConverter);
  }
  
  public Map getQuidTable() { return quidTable;}
  public Map getNameTable() { return nameTable;}
  public Map<Object, List<String>> getSuperTable() { return superTable;}
  public Map<EReference, String> getRefTable() { return refTable;}
  public Map<EObject, String> getTypeTable() { return typeTable;}

  private RoseNode versionTree = null;
  private RoseNode modelTree = null;
  private UnitTreeNode unitTreeRoot = null;
  
  public RoseNode getVersionTree(){
    return versionTree;
  }
  
  public RoseNode getModelTree(){
    return modelTree;
  }
  
  public UnitTreeNode getUnitTree(){
    return unitTreeRoot;
  }
  
  public Rose2MdaTransformer loadTree(String fileName, MetaModel target, ValidationResult vResults) throws Exception
  {
    Rose2MdaTransformer mdaRoseVisitor = new Rose2MdaTransformer(this, target);
    mdaRoseVisitor.setValidationResults(vResults);
    
    // Load the Rose .mdl or .cat file, and create mappings for the objects.
    //
    String roseFile = fileName;
    RoseLoader loader = new RoseLoader(roseFile, uriConverter);
    try
    {
      if (loader.isValid())
      {
        RoseLexer  lexer = new RoseLexer(loader);
        RoseParser parser = new RoseParser(lexer, false, false);
        parser.parse();
        versionTree = parser.getVersionTree();
        modelTree = parser.getModelTree();
        MdaUnitTreeBuilder unitTreeBuilder = new MdaUnitTreeBuilder(this);
        unitTreeRoot = createUnitTreeNode(fileName);
        target.setName(unitTreeRoot.getName());
        unitTreeBuilder.traverse(getQualifier(), modelTree, unitTreeRoot);
        createExtent4RoseUnitTree(unitTreeRoot);

        // Start second traverse to create mapping objects in memory.
        //
        RoseWalker roseWalker = new RoseWalker(modelTree);
        roseWalker.traverse(mdaRoseVisitor);
        
        mdaRoseVisitor.createEPackageForRootClasses(unitTreeRoot.getExtent(), unitTreeRoot.getRoseNode(), unitTreeRoot.getName());
        mdaRoseVisitor.setEEnums();
        mdaRoseVisitor.setEReferences();
        mdaRoseVisitor.setSuper();
        mdaRoseVisitor.setETypeClassifier();
        mdaRoseVisitor.setViews();
        setIDs(unitTreeRoot);
        validate(unitTreeRoot);
      }
    }
    finally 
    {
      loader.close();
    }
    return mdaRoseVisitor;
  }
  
  public String getModelName(String fileName) {
    int ind_1 = fileName.lastIndexOf(System.getProperty("file.separator"));
    int ind_2 = fileName.lastIndexOf(".");
    String modelName;
    if (ind_2 != -1)
      modelName = fileName.substring(ind_1 + 1, ind_2);
    else
      modelName = fileName.substring(ind_1 + 1, fileName.length());
    return modelName;
  }
  
  private UnitTreeNode createUnitTreeNode(String fileName) {
    String quid = modelTree.getRoseId();
    if (quid != null)
    {
      if(modelTree.getValue() != null && modelTree.getKey() != null && modelTree.getKey().equals("") && (Util.getType(modelTree.getValue())).equals(RoseStrings.CLASS_CATEGORY)) {
        String objName = Util.getName(modelTree.getValue());
        UnitTreeNode unitTree = new UnitTreeNode(Util.getName(modelTree.getValue()), quid, fileName);
        TableObject obj = new TableObject(objName, quid, unitTree);
        quidTable.put(quid, obj);
        nameTable.put(objName, obj);
        return unitTree;
      }
      quid = quid.substring(1, quid.length() - 1);
      return new UnitTreeNode(getModelName(fileName), quid, fileName);
    }
    return new UnitTreeNode(getModelName(fileName), "", fileName);
  }
  
  private String getQualifier() {
    String quid = modelTree.getRoseId();
    if (quid != null)
    {
      if(modelTree.getValue() != null) {
        return Util.getName(modelTree.getValue());
      }
    }
    return null;
  }
}
