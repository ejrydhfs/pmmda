/* copyright 2000-2006 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence");
 * you may not use this file except in compliance with the License.
 * $Id$
 */
package ch.bbv.explorer.actions;

import javax.swing.Action;

import ch.bbv.explorer.ExplorerNode;

/**
 * <p>
 * The explorer node action defines an action performed on an explorer node. The
 * explorer node is in general displayed in the right tree view of the explorer.
 * The action is triggered through a popup or a pull-down menu for example. The
 * explorer action calls the given method trough the reflection mechanisms.
 * Three modes are supported.
 * </p>
 * <ul>
 * <li>The action is applied on the lightweight representation displayed in the
 * node. The lightweight representation is always defined if a node exists.
 * </li>
 * <li>The action is applied on the data object displayed in the node. If the
 * node does not exist, nothing happens.</li>
 * <li>The action is applied on the explorer node itself.</li>
 * </ul>
 * <p>
 * Upon completion of the action a post processing step is sometimes required.
 * For example upon creation of a new data object it is necessary to insert it
 * into the model. The same is true before the action is executed. The provided
 * mechanism allows asynchronous call of the pre-execute and post-execute.
 * </p>
 * @author Marcel Baumann
 * @version $Revision$
 */
public interface ExplorerNodeAction extends Action {
  /**
   * Returns the name of the action as displayed on the user interface.
   * @return name of the action.
   */
  String getName();

  /**
   * Step executed before the action is called.
   * @param node node on which the action should be performed.
   */
  void preExecute(ExplorerNode node);

  /**
   * Executes the action on the given name.
   * @param node node on which the action should be performed.
   */
  void execute(ExplorerNode node);

  /**
   * Step executed after the action is called.
   * @param node node on which the action should be performed.
   * @param shouldUndo true if the action should be undo otherwise false.
   */
  void postExecute(ExplorerNode node, boolean shouldUndo);
}