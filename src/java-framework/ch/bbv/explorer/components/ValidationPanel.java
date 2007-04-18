/* 
 * copyright 2000-2006 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence");
 * you may not use this file except in compliance with the License.
 * $Id$
 */

package ch.bbv.explorer.components;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

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
    JComponent reportList = ValidationResultViewFactory.createReportList(model); 
    add(reportList, BorderLayout.CENTER);
   	setPopupMenu(reportList.getComponents(), ValidationPanel.this.createPopupMenu());
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

  /**
   * Creates a new JPopupMenu with one single entry - Clear
   * @return JPopupMenu having a Clear entry.
   */
  private JPopupMenu createPopupMenu() {
  	JPopupMenu popupMenu = new JPopupMenu();
  	JMenuItem menuItemClear = new JMenuItem("Clear");
  	menuItemClear.addActionListener(new ActionListener() {
  		public void actionPerformed(ActionEvent event) {
  			ValidationPanel.this.clear();
  		}
  	});
  	popupMenu.add(menuItemClear);
  	return popupMenu;
  }

  /**
   * Sets the popupMenu on all children of type JList
   * @param children list of Components to set popupMenu onto or on its descendants
   * @param popupMenu JPopupMenu to set on JList
   */
  private void setPopupMenu(Component[] children, JPopupMenu popupMenu) {
  	if (children == null || children.length == 0) return;
  	for(int i=0; i<children.length; i++) {
  		if(children[i] instanceof JList) {
  			((JList)children[i]).setComponentPopupMenu(popupMenu);
  		}
  		if(children[i] instanceof JComponent) {
  		  setPopupMenu(((JComponent)children[i]).getComponents(), popupMenu);
  		}
  	}
  }

}