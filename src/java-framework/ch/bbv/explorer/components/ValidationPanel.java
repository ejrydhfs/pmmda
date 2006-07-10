/* 
 * copyright 2000-2006 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence");
 * you may not use this file except in compliance with the License.
 * $Id$
 */

package ch.bbv.explorer.components;

import java.awt.BorderLayout;

import javax.swing.JComponent;

import com.jgoodies.validation.ValidationResult;
import com.jgoodies.validation.ValidationResultModel;
import com.jgoodies.validation.util.DefaultValidationResultModel;
import com.jgoodies.validation.view.ValidationResultViewFactory;

/**
 * The validation panel displays a list of validation messages. The messages are
 * displayed in a table.
 * @author MarcelBaumann
 * @version $Revision$
 */
public class ValidationPanel extends JComponent {
  /**
   * Serial version identifier coded as back compatibility date.
   */
  private static final long serialVersionUID = 20041201;

  /**
   * List of displayed validation results.
   */
  private ValidationResultModel model;

  /**
   * Default constructor of the class.
   */
  public ValidationPanel() {
    setLayout(new BorderLayout());
    model = new DefaultValidationResultModel();
    add(ValidationResultViewFactory.createReportList(model),
        BorderLayout.CENTER);
  }

  /**
   * Sets the validation results in the panel. The previous values are
   * discarded.
   * @param results list of validation results to display
   */
  public void setResults(ValidationResult results) {
  	ValidationResult orderedResults = new ValidationResult();
  	orderedResults.addAll(results.getErrors());
  	orderedResults.addAll(results.getWarnings());
    model.setResult(orderedResults);
  }

  public ValidationResult getResults() {
    return model.getResult();
  }

  public void clear() {
    model.setResult(new ValidationResult());
  }
}