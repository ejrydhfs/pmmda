/* copyright 2000-2006 Marcel Baumann 
 * Licensed under the Apache License, Version 2.0 (the "Licence); 
 * you may not use this file except in compliance with the License. 
 * $Id$
 */
package ch.bbv.mda.ui;

import java.awt.Color;
import java.util.regex.Pattern;

import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JTextField;
import javax.swing.tree.TreeNode;

import ch.bbv.explorer.ExplorerNode;
import ch.bbv.mda.*;
import ch.bbv.mda.validation.IdentifierValidator;

import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.validation.Severity;
import com.jgoodies.validation.ValidationResult;
import com.jgoodies.validation.message.SimpleValidationMessage;
import com.jgoodies.validation.util.ValidationUtils;
import com.jgoodies.validation.view.ValidationComponentUtils;

/**
 * The meta property view defines the view of a MDA simple or indexed property.
 * @author marcelbaumann
 * @version $Revision$
 */
public class MetaPropertyView extends MetaItemView {
  /**
   * Serial version identifier coded as back compatibility date.
   */
  private static final long serialVersionUID = 20041201;

  /**
   * Pattern for validating identifiers
   */
  static Pattern validName;

  /**
   * Pattern for validating types
   */
  JTextField type;

  private JTextField multiplicityLowerBound;

  private JTextField multiplicityUpperBound;

  private JTextField initialValue;

  private JCheckBox volatiles;

  private JCheckBox transients;

  private JCheckBox finals;

  private JCheckBox statics;

  /**
   * Default constructor of the class.
   */
  public MetaPropertyView() {
    type = new JTextField(TEXT_FIELD_SIZE);
    multiplicityLowerBound = new JTextField();
    multiplicityUpperBound = new JTextField();
    initialValue = new JTextField(TEXT_FIELD_SIZE);
    volatiles = new JCheckBox();
    transients = new JCheckBox();
    finals = new JCheckBox();
    statics = new JCheckBox();
    ValidationComponentUtils.setMandatory(type, true);
    ValidationComponentUtils.setMandatory(multiplicityLowerBound, true);
    ValidationComponentUtils.setMandatory(multiplicityUpperBound, true);
    add(buildPanel());
  }

  @Override
  public void populateView() {
    super.populateView();
    MetaProperty model = (MetaProperty) getDataObject();
    if(model.getContext() == null) {
    	TreeNode treeNode = getNode().getParent();
    	if(treeNode instanceof ExplorerNode) {
      	model.setContext(((MetaElement)((ExplorerNode)treeNode).getDataObject()).getContext());
    	}
    }
    type.setText(model.getTypeName());
    type.setBackground(Color.WHITE);
    volatiles.setSelected(model.isVolatile());
    transients.setSelected(model.isTransient());
    statics.setSelected(model.isStatic());
    finals.setSelected(model.isFinal());
    Multiplicity multiplicity = model.getMultiplicity();
    multiplicityLowerBound.setText(Multiplicity.stringValue(multiplicity
        .getLowerBound()));
    multiplicityUpperBound.setText(Multiplicity.stringValue(multiplicity
        .getUpperBound()));
    if (model instanceof MetaIndexedProperty) {
      multiplicityLowerBound.setEditable(true);
      multiplicityUpperBound.setEditable(true);
    } else {
      multiplicityLowerBound.setEditable(false);
      multiplicityUpperBound.setEditable(false);
    }
    initialValue.setText(model.getInitialValue());
  }

  @Override
  public void commitChanges() {
    super.commitChanges();
    MetaProperty model = (MetaProperty) getDataObject();
    model.setInitialValue(initialValue.getText());
    String text = type.getText();
    if (text != null) {
      model.setTypeName(text.trim());
    }
    model.setVisibility((VisibilityCode) visibility.getSelectedCode());
    model.setVolatile(volatiles.isSelected());
    model.setTransient(transients.isSelected());
    model.setFinal(finals.isSelected());
    model.setStatic(statics.isSelected());
    getModel().nodeChanged();
  }

  @Override
  public void validateView(ValidationResult result) {
    String identifier = name.getText();
    if (!ValidationUtils.isBlank(identifier)) {
      if (!IdentifierValidator.checkIdentifier(identifier)) {
        result.add(new SimpleValidationMessage(
            " name is not a legal Java identifier", Severity.ERROR, name));
      } else if (!Character.isLowerCase(identifier.charAt(0))) {
        result.add(new SimpleValidationMessage(
            "name does not start with lower case", Severity.WARNING, name));
      }

    }
    if (ValidationUtils.isBlank(type.getText())) {
      result.add(new SimpleValidationMessage("type is mandatory",
          Severity.ERROR, type));
    }
    if (ValidationUtils.isBlank(multiplicityLowerBound.getText())) {
      result.add(new SimpleValidationMessage("lower bound must be provided",
          Severity.ERROR, multiplicityLowerBound));
    }
    if (ValidationUtils.isBlank(multiplicityUpperBound.getText())) {
      result.add(new SimpleValidationMessage("upper bound must be provided",
          Severity.ERROR, multiplicityUpperBound));
    }
    super.validateView(result);
  }

  @Override
  public void setViewEditable(boolean editable) {
    super.setViewEditable(editable);
    type.setEditable(editable);
    multiplicityLowerBound.setEditable(editable);
    multiplicityUpperBound.setEditable(editable);
    initialValue.setEditable(editable);
    volatiles.setEnabled(editable);
    transients.setEnabled(editable);
    finals.setEnabled(editable);
    statics.setEnabled(editable);
  }

  @Override
  protected JComponent buildGeneral() {
    DefaultFormBuilder builder = initializeGeneral();
    builder.append("&Type", type);
    builder.append("&Initial Value", initialValue);
    builder.append("&Lower Bound", multiplicityLowerBound);
    builder.append("&Upper Bound", multiplicityUpperBound);
    return builder.getPanel();
  }

  @Override
  protected JComponent buildFlags() {
    DefaultFormBuilder builder = getBuilder(FLAGS_LAYOUT);
    builder.append("&volatile", volatiles);
    builder.append("&transient", transients);
    builder.append("&final", finals);
    builder.append("&static", statics);
    return builder.getPanel();
  }
}