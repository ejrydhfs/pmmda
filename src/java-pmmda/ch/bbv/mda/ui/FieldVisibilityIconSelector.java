/* copyright 2000-2005 Marcel Baumann 
 * Licensed under the Apache License, Version 2.0 (the "Licence); 
 * you may not use this file except in compliance with the License. 
 * $Id$
 */
package ch.bbv.mda.ui;

import javax.swing.ImageIcon;

import ch.bbv.explorer.ExplorerNode;
import ch.bbv.explorer.IconFactory;
import ch.bbv.explorer.IconSelector;
import ch.bbv.mda.MetaProperty;
import ch.bbv.mda.operators.MdaUtilities;

/**
 * The field visibility icon selector implements an icon selector to define the
 * visibility icons for fields of a class.
 * @author marcelbaumann
 * @version $Revision$
 */
public class FieldVisibilityIconSelector implements IconSelector {
	/** The name of the node type.
	 */
  private String nodeTypeName;
  
  /** factory managing the icons.
   */
  private IconFactory factory;

	/** Constructor of the class. 
	 * @param factory factory responsible for the icons.
	 * @param nodeTypeName the name of the node type.
	 */
  public FieldVisibilityIconSelector(IconFactory factory, String nodeTypeName) {
  	this.factory = factory;
    this.nodeTypeName = nodeTypeName;
  }

  public ImageIcon select(ExplorerNode node, boolean expanded) {
    MetaProperty property = (MetaProperty) node.getLightweightObject();
		StringBuilder buffer = new StringBuilder();
		buffer.append(nodeTypeName);
		buffer.append("-");
		buffer.append(MdaUtilities.visibilityToString(property.getVisibility().getCode()));
		return factory.getSmallIconFor(buffer.toString());
  }
}
