/* copyright 2000-2006 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence");
 * you may not use this file except in compliance with the License.
 * $Id$
 */

package ch.bbv.explorer.nodeactions;

import ch.bbv.explorer.ExplorerNode;
import ch.bbv.explorer.UiNodeType;

/**
 * The edit action opens the default view in the edit mode.
 * @author MarcelBaumann
 * @version $Revision$
 */
public class EditNodeAction extends AbstractNodeAction {
  /**
   * Unique serial version identifier for the instances of the class.
   */
  private static final long serialVersionUID = 602567193494956638L;

  /**
   * Constructor of the class.
   * @param type node type for which the action is defined
   * @param name name of the action. If the action is an add, the name must be
   *          the name of the property
   */
  public EditNodeAction(UiNodeType type, String name) {
    super(type, name, true);
  }

  @Override
  public void execute(ExplorerNode node) {
    displayView(node, true, false, true);
  }

  @Override
  public void postExecute(ExplorerNode node, boolean shouldUndo) {
    if (!shouldUndo) {
      node.nodeChanged();
    }
  }
}