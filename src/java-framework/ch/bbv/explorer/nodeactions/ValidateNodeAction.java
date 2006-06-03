/* 
 * copyright 2000-2006 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence");
 * you may not use this file except in compliance with the License.
 * $Id$
 */

package ch.bbv.explorer.nodeactions;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ch.bbv.dog.AbstractVisitorFunctor;
import ch.bbv.dog.DataObject;
import ch.bbv.dog.DataObjectType;
import ch.bbv.dog.validation.Validator;
import ch.bbv.explorer.Explorer;
import ch.bbv.explorer.ExplorerNode;
import ch.bbv.explorer.UiNodeType;
import ch.bbv.mda.ui.MdaExplorer;

import com.jgoodies.validation.ValidationResult;

/**
 * The validate action traverses the data object graph which root is in the
 * explorer node. All rules are executed and the results collected. If results
 * exist, they are displayed in the explorer window.
 * @author marcelbaumann
 * @version $Revision$
 */
public class ValidateNodeAction extends AbstractNodeAction {
  /**
   * Unique serial version identifier for the instances of the class.
   */
  private static final long serialVersionUID = 8665244713385388052L;

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
   * @param type node type for which the action is defined
   * @param name name of the action. If the action is an add, the name must be
   *          the name of the property
   * @param validator validator used to validate the data object graph
   */
  public ValidateNodeAction(UiNodeType type, String name, Validator validator) {
    super(type, name, false);
    this.validator = validator;
  }

  @Override
  public void execute(ExplorerNode node) {
    DataObject object = node.getDataObject();
    if (object != null) {
      MdaExplorer explorer = (MdaExplorer) node.getNodeOwner();
      validateTree(object, validator, explorer);
    }
  }

  public static void validateTree(DataObject object, Validator validator,
      Explorer explorer) {
    DataObjectType type = explorer.getManager().getType(object);
    AbstractVisitorFunctor visitor = type.createVisitorInstance();
    ValidationResult results = new ValidationResult();
    validator.validate(object, visitor, results);
    log.debug("Display validator messages " + results.size());
    explorer.getStatusBar().setInfo("Validation Completed");
    explorer.getDocument().setValidated(true);
    explorer.getValidationPanel().setResults(results);
  }
}