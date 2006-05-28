/* copyright 2000-20056 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence");
 * you may not use this file except in compliance with the License.
 * $Id$
 */

package ch.bbv.explorer.nodeactions;

import javax.swing.JOptionPane;

import ch.bbv.dog.CachesHandler;
import ch.bbv.dog.DataObject;
import ch.bbv.explorer.ExplorerNode;
import ch.bbv.explorer.ExplorerNodeOwner;
import ch.bbv.explorer.UiNodeType;

/**
 * The revert node retrieves the local representation of the data object graph.
 * The behaviour is described in details in the referenced class.
 * @see ch.bbv.dog.CachesHandler
 * @author MarcelBaumann
 * @version $Revision$
 */
public class RevertNodeAction extends AbstractNodeAction {
  /**
   * Unique serial version identifier for the instances of the class.
   */
  private static final long serialVersionUID = 4645568096275127536L;

  /**
   * Constructor of the class.
   * @param type node type for which the action is defined
   * @param name name of the action. If the action is an add, the name must be
   *          the name of the property
   */
  public RevertNodeAction(UiNodeType type, String name) {
    super(type, name, false);
  }

  @Override
  public void execute(ExplorerNode node) {
    DataObject object = node.getDataObject();
    ExplorerNodeOwner owner = node.getNodeOwner();
    if (object != null) {
      // data object graph was loaded previously.
      if (object.isNew()) {
        JOptionPane.showMessageDialog(null, "Object is new.",
            "Revert Operation", JOptionPane.WARNING_MESSAGE);
      } else {
        CachesHandler handler = (CachesHandler) owner.getManager()
            .getDataObjectHandler();
        DataObject root = handler.revert(owner.getManager().getDatasource(),
            object);
        updateNavigator(node, root, root);
      }
    }
  }
}