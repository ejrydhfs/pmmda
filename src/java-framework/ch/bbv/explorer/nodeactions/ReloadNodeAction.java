/* copyright 2000-2006 Marcel Baumann
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
 * The reload node retrieves the the data object graph from the underlying layer
 * and refreshes all caches. The behaviour is described in details in the
 * referenced class.
 * @see ch.bbv.dog.CachesHandler
 * @author MarcelBaumann
 * @version $Revision$
 */
public class ReloadNodeAction extends AbstractNodeAction {
  /**
   * Unique serial version identifier for the instances of the class.
   */
  private static final long serialVersionUID = 7020308297035887768L;

  /**
   * Constructor of the class.
   * @param type node type for which the action is defined
   * @param name name of the action. If the action is an add, the name must be
   *          the name of the property
   */
  public ReloadNodeAction(UiNodeType type, String name) {
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
            "Reload Operation", JOptionPane.WARNING_MESSAGE);
      } else {
        CachesHandler handler = (CachesHandler) owner.getManager()
            .getDataObjectHandler();
        DataObject root = handler.reload(owner.getManager().getDatasource(),
            object);
        updateNavigator(node, root, root);
      }
    }
  }
}