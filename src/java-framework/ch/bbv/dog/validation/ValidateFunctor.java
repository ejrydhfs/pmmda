/* copyright 2000-2004 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence");
 * you may not use this file except in compliance with the License.
 * $Id$
 */

package ch.bbv.dog.validation;

import java.util.ArrayList;
import java.util.List;

import ch.bbv.dog.DataObject;
import ch.bbv.dog.DoFunctor;

/**
 * The validator functor applies the validation rules defined in a validator to
 * all data objects part of a graph. The results can be retrieved upon
 * completion of the traversal.
 * @author MarcelBaumann
 * @version $Revision$
 */
public class ValidateFunctor implements DoFunctor {
  /**
   * The validator used to validate the graph of data objects.
   */
  private Validator validator;

  /**
   * List of validation rule results of the last traversal.
   */
  private List results;

  /**
   * Constructor of the class.
   * @param validator validator which rules should be evaluated.
   */
  public ValidateFunctor(Validator validator) {
    this.validator = validator;
    results = new ArrayList();
  }

  /**
   * Returns the validation results of the last graph traversal.
   * @return list of validation rule results.
   */
  public List getResults() {
    return results;
  }
  /**
   * Resets the results list. Once reseted the functor can be used for a new
   * traversal.
   */
  public void reset() {
    results.clear();
  }

  public void execute(DataObject object) {
    validator.validate(object, results);
  }
}