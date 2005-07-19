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

import java.io.File;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.emf.importer.rose.builder.RoseUtil;
import org.eclipse.emf.importer.rose.builder.RoseWalker;
import org.eclipse.emf.importer.rose.builder.UnitTreeNode;
import org.eclipse.emf.importer.rose.parser.RoseLexer;
import org.eclipse.emf.importer.rose.parser.RoseLoader;
import org.eclipse.emf.importer.rose.parser.RoseNode;
import org.eclipse.emf.importer.rose.parser.RoseParser;
import org.eclipse.emf.ecore.resource.URIConverter;

import ch.bbv.mda.MetaModel;

/**
 * Provide functions to process a rose file.
 */
public class MdaRoseUtil extends RoseUtil
{
  
  public MdaRoseUtil(URIConverter uriConverter)
  {
    super(uriConverter);
  }
  
  public Map getQuidTable() { return quidTable;}
  public Map getNameTable() { return nameTable;}
  public Map getSuperTable() { return superTable;}
  public Map getRefTable() { return refTable;}
  public Map getTypeTable() { return typeTable;}

  public void processUnitTree(UnitTreeNode unitTree, MetaModel target) throws Exception
  {
    if (unitTree != null) 
    {
      MdaRoseVisitor mdaRoseVisitor = loadTree(null, unitTree, target);
      String packageName = unitTree.getEcoreFileName();
      int fileSeparatorIndex = packageName.lastIndexOf(File.separator);
      if (fileSeparatorIndex != -1)
      {
        packageName = packageName.substring(fileSeparatorIndex + 1);
      }
      int dotIndex = packageName.lastIndexOf(".");
      if (dotIndex != -1)
      {
        packageName = packageName.substring(0, dotIndex);
      }
      mdaRoseVisitor.getMetaModel().setName(packageName);
      mdaRoseVisitor.createEPackageForRootClasses(unitTree.getExtent(), unitTree.getRoseNode(), packageName);
      mdaRoseVisitor.setEEnums();
      mdaRoseVisitor.setEReferences();
      mdaRoseVisitor.setSuper();
      mdaRoseVisitor.setETypeClassifier();
      setIDs(unitTree);
      validate(unitTree);
    }
  }

  protected MdaRoseVisitor loadTree(RoseNode containingNode, UnitTreeNode node, MetaModel target) throws Exception
  {
    MdaRoseVisitor mdaRoseVisitor = new MdaRoseVisitor(this, target);
    
    // Load the Rose .mdl or .cat file, and create mappings for the objects.
    //
    String name = node.getName();
    String roseFile = node.getRoseFileName();
    RoseLoader loader = new RoseLoader(roseFile, null);
    try
    {
      if (loader.isValid())
      {
        RoseLexer  lexer = new RoseLexer(loader);
        RoseParser parser = new RoseParser(lexer, true, true);
        parser.parse();
        RoseNode modelTree = parser.getModelTree();
        modelTree.setNode(node.getExtent());
    
        // This sets the parent so that the nodes can traverse to the root to find default eCore settings.
        //
        if (containingNode != null)
        {
          modelTree.setParent(containingNode);
        }
    
        containingNode = modelTree;
    
        // Start second traverse to create mapping objects in memory.
        //
        RoseWalker roseWalker = new RoseWalker(modelTree);
        roseWalker.traverse(mdaRoseVisitor);
      }
    }
    finally 
    {
      loader.close();
    }

    // Process the children of the UnitTreeNode recursively.
    //
    for (Iterator i = node.getNodes().iterator(); i.hasNext();)
    {
      UnitTreeNode subNode = (UnitTreeNode)i.next();
      loadTree(containingNode, subNode, target);
    }
    return mdaRoseVisitor;
  }
}
