/* copyright 2000-2004 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence");
 * you may not use this file except in compliance with the License.
 * $Id$
 */

package ch.bbv.explorer.nodeactions;

import java.util.ArrayList;
import java.util.List;

import ch.bbv.dog.AbstractVisitorFunctor;
import ch.bbv.dog.DataObject;
import ch.bbv.dog.DataObjectType;
import ch.bbv.dog.validation.Validator;
import ch.bbv.explorer.ExplorerNode;
import ch.bbv.explorer.UiNodeType;

/**
 * The validate action traverses the data object graph which root is in the
 * explorer node. All rules are executed and the results collected. If results
 * exist, they are displayed in the explorer window.
 * @author marcelbaumann
 * @version $Revision$
 */
public class ValidateNodeAction extends AbstractNodeAction {
  /**
   * The validator used to validate the data object graphs.
   */
  private transient Validator validator;

  /**
   * Constructor of the class.
   * @param type node type for which the action is defined.
   * @param name name of the action. If the action is an add, the name must be
   *        the name of the property.
   * @param validator validator used to validate the data object graph.
   */
  public ValidateNodeAction(UiNodeType type, String name, Validator validator) {
    super(type, name, false);
    this.validator = validator;
  }

  public void execute(ExplorerNode node) {
    DataObject object = (DataObject) node.getDataObject();
    if (object != null) {
      DataObjectType type = node.getNodeType().getDataObjectType();
      AbstractVisitorFunctor visitor = type.createVisitorInstance();
      List results = new ArrayList();
      validator.validate(object, visitor, results);
      if (results != null && results.size() > 0) {
        // TODO display results
      } else {
        node.getExplorer().getStatusBar().setInfo("Validation Completed");
      }
    }
  }
}