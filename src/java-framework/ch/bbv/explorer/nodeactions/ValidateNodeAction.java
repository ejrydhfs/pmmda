/* 
 * copyright 2000-2005 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence");
 * you may not use this file except in compliance with the License.
 * $Id$
 */

package ch.bbv.explorer.nodeactions;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ch.bbv.dog.AbstractVisitorFunctor;
import ch.bbv.dog.DataObject;
import ch.bbv.dog.DataObjectType;
import ch.bbv.dog.validation.ValidationRuleResult;
import ch.bbv.dog.validation.Validator;
import ch.bbv.explorer.ExplorerNode;
import ch.bbv.explorer.UiNodeType;
import ch.bbv.explorer.components.ValidationPanel;

/**
 * The validate action traverses the data object graph which root is in the
 * explorer node. All rules are executed and the results collected. If results
 * exist, they are displayed in the explorer window.
 * @author marcelbaumann
 * @version $Revision$
 */
public class ValidateNodeAction extends AbstractNodeAction {
  /**
   * Serial version identifier coded as back compatibility date.
   */
  private static final long serialVersionUID = 20041201;

  /**
   * Logger of the instances of the class.
   */
  private static Log log = LogFactory.getLog(ValidateNodeAction.class);

  /**
   * The validator used to validate the data object graphs.
   */
  private transient Validator validator;

  /**
   * Constructor of the class.
   * @param type node type for which the action is defined.
   * @param name name of the action. If the action is an add, the name must be
   *          the name of the property.
   * @param validator validator used to validate the data object graph.
   */
  public ValidateNodeAction(UiNodeType type, String name, Validator validator) {
    super(type, name, false);
    this.validator = validator;
  }

  public void execute(ExplorerNode node) {
    DataObject object = node.getDataObject();
    if (object != null) {
      DataObjectType type = node.getNodeType().getDataObjectType();
      AbstractVisitorFunctor visitor = type.createVisitorInstance();
      List<ValidationRuleResult> results = new ArrayList<ValidationRuleResult>();
      validator.validate(object, visitor, results);
      log.debug("Display validator messages " + results.size());
      node.getExplorer().getStatusBar().setInfo("Validation Completed");
      ValidationPanel panel = (ValidationPanel) node.getExplorer()
          .getDockingArea().getTab("Validator");
      panel.setResults(results);
    }
  }
}