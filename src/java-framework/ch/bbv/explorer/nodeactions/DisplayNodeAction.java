/* copyright 2000-2005 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence");
 * you may not use this file except in compliance with the License.
 * $Id$
 */

package ch.bbv.explorer.nodeactions;

import ch.bbv.explorer.ExplorerNode;
import ch.bbv.explorer.UiNodeType;

/**
 * The node action displays the properties of the data object defined in the
 * node. The view is the current selected one.
 * @author MarcelBaumann
 * @version $Revision$
 */
public class DisplayNodeAction extends AbstractNodeAction {
  private static final long serialVersionUID = 20041201;
  
  /**
   * Constructor of the class.
   * @param type node type for which the action is defined.
   * @param name name of the view to display. 
   */
  public DisplayNodeAction(UiNodeType type, String name) {
    super(type, name, false);
  }

  public void execute(ExplorerNode node) {
    displayView(node, false, false);
  }
}