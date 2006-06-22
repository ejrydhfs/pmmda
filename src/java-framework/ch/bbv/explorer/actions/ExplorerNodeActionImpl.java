/* copyright 2000-2006 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence");
 * you may not use this file except in compliance with the License.
 * $Id$
 */
package ch.bbv.explorer.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import ch.bbv.explorer.ExplorerNode;
import ch.bbv.explorer.ExplorerTreeView;
import ch.bbv.explorer.UiNodeType;

/**
 * The explorer node action class implements the interface of the explorer node
 * action. All actions are empty.
 * @author Marcel Baumann
 * @version $Revision$
 */
public class ExplorerNodeActionImpl extends AbstractAction implements
    ExplorerNodeAction {
  /**
   * Serial version identifier coded as back compatibility date.
   */
  private static final long serialVersionUID = 20041201;

  /**
   * Name of the action.
   */
  private String name;

  /**
   * The node type on which the action is executed.
   */
  private UiNodeType nodeType;

  /**
   * Constructor of the class.
   * @param name name of the action.
   */
  public ExplorerNodeActionImpl(String name) {
    this(name, null);
  }

  /**
   * Constructor of the class.
   * @param name name of the action.
   * @param nodeType node type of the action.
   */
  public ExplorerNodeActionImpl(String name, UiNodeType nodeType) {
    super(name);
    this.name = name;
    this.nodeType = nodeType;
    setEnabled(true);
  }

  public void preExecute(ExplorerNode node) {
  }

  public void execute(ExplorerNode node) {
  }

  public void postExecute(ExplorerNode node, boolean shouldUndo) {
  }

  public String getName() {
    return name;
  }

  /**
   * Returns the node type for the action.
   * @return node type of the action.
   */
  public UiNodeType getNodeType() {
    return nodeType;
  }

  public void actionPerformed(ActionEvent actionEvent) {
    ExplorerNode node = ExplorerTreeView.getSelectedNodeForPopup();
    assert (node != null);
    preExecute(node);
    execute(node);
    ExplorerTreeView.setSelectedNodeForPopup(null);
  }
}