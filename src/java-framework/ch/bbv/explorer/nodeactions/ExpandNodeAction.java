/*
 * copyright 2000-2004 Marcel Baumann Licensed under the Apache License, Version
 * 2.0 (the "Licence"); you may not use this file except in compliance with the
 * License. $Id: DisplayNodeAction.java,v 1.1 2004/09/14 19:16:25 marcelbaumann
 * Exp $
 */

package ch.bbv.explorer.nodeactions;

import javax.swing.tree.TreePath;

import ch.bbv.explorer.ExplorerNode;
import ch.bbv.explorer.UiNodeType;

/**
 * Expands all the nodes below the node to process.
 * @author MarcelBaumann
 * @version $Revision$
 */
public class ExpandNodeAction extends AbstractNodeAction {
  /**
   * Constructor of the class.
   * @param type node type for which the action is defined.
   * @param name name of the action. If the action is an add, the name must be
   *        the name of the property.
   */
  public ExpandNodeAction(UiNodeType type, String name) {
    super(type, name, false);
  }

  public void execute(ExplorerNode node) {
    node.getExplorer().getTree().expandPath(new TreePath(node.getPath()));
  }
}