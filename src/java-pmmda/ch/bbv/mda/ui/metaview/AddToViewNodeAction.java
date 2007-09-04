/* 
 * copyright 2000-2006 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence");
 * you may not use this file except in compliance with the License.
 * $Id: 
 */
package ch.bbv.mda.ui.metaview;

import ch.bbv.explorer.ExplorerNode;
import ch.bbv.explorer.UiNodeType;
import ch.bbv.explorer.nodeactions.AbstractNodeAction;

public class AddToViewNodeAction extends AbstractNodeAction{

  /**
   * Unique serial version identifier for the instances of the class.
   */
  private static final long serialVersionUID = 20070830L;

  /**
   * Contains the metaViewView to be manipulated by this node action
   */
  private MetaViewView metaViewView;

  public AddToViewNodeAction(UiNodeType type, String name, MetaViewView metaViewView) {
    super(type, name, true);
    this.metaViewView = metaViewView;
  }

  @Override
  public void execute(ExplorerNode node) {
    metaViewView.nodeToTreeAsRoot(metaViewView.getModelTree().getTree(), metaViewView.getViewTree().getTree(), node);
  }

  @Override
  public void postExecute(ExplorerNode node, boolean shouldUndo) {
  }

}
