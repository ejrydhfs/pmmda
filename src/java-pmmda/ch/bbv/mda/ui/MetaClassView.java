/* copyright 2000-2006 Marcel Baumann 
 * Licensed under the Apache License, Version 2.0 (the "Licence); 
 * you may not use this file except in compliance with the License. 
 * $Id$
 */
package ch.bbv.mda.ui;

import javax.swing.JCheckBox;
import javax.swing.JComponent;

import ch.bbv.mda.MetaClass;
import ch.bbv.mda.VisibilityCode;
import ch.bbv.mda.validation.IdentifierValidator;

import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.validation.Severity;
import com.jgoodies.validation.ValidationResult;
import com.jgoodies.validation.message.SimpleValidationMessage;
import com.jgoodies.validation.util.ValidationUtils;

/**
 * The meta class view defines the view of a MDA class.
 * @author Marcel Baumann
 * @version $Revision$
 */
public class MetaClassView extends MetaItemView {
  /**
   * Serial version identifier coded as back compatibility date.
   */
  private static final long serialVersionUID = 20041201;

  /**
   * Swing component to display the associated property.
   */
  private JCheckBox abstracts;

  /**
   * Swing component to display the associated property.
   */
  private JCheckBox finals;

  /**
   * Swing component to display the associated property.
   */
  private JCheckBox interfaces;

  /**
   * Swing component to display the associated property.
   */
  private JCheckBox statics;

  /**
   * Default constructor of the class.
   */
  public MetaClassView() {
    abstracts = new JCheckBox();
    finals = new JCheckBox();
    interfaces = new JCheckBox();
    statics = new JCheckBox();
    add(buildPanel());
  }

  @Override
  public void populateView() {
    super.populateView();
    MetaClass model = (MetaClass) getDataObject();
    visibility.setSelectedCode(model.getVisibility());
    abstracts.setSelected(model.isAbstract());
    interfaces.setSelected(model.isInterface());
    statics.setSelected(model.isStatic());
    finals.setSelected(model.isFinal());
  }

  @Override
  public void commitChanges() {
    super.commitChanges();
    MetaClass model = (MetaClass) getDataObject();
    model.setVisibility((VisibilityCode) visibility.getSelectedCode());
    model.setAbstract(abstracts.isSelected());
    model.setFinal(finals.isSelected());
    model.setInterface(interfaces.isSelected());
    model.setStatic(statics.isSelected());
    getModel().nodeChanged();
  }

  @Override
  public void validateView(ValidationResult result) {
    String identifier = name.getText();
    if (!ValidationUtils.isBlank(identifier)) {
      if (!IdentifierValidator.checkJavaIdentifier(identifier)) {
        result.add(new SimpleValidationMessage("name is not a Java identifier",
            Severity.ERROR, name));
      } else if (!Character.isUpperCase(identifier.charAt(0))) {
        result.add(new SimpleValidationMessage(
            "name does not start with upper case", Severity.WARNING, name));
      }
    }
    super.validateView(result);
  }

  @Override
  public void setViewEditable(boolean editable) {
    super.setViewEditable(editable);
    abstracts.setEnabled(editable);
    finals.setEnabled(editable);
    interfaces.setEnabled(editable);
    statics.setEnabled(editable);
  }

  @Override
  protected JComponent buildGeneral() {
    DefaultFormBuilder builder = initializeGeneral();
    return builder.getPanel();
  }

  @Override
  protected JComponent buildFlags() {
    DefaultFormBuilder builder = getBuilder(FLAGS_LAYOUT);
    builder.append("&abstract", abstracts);
    builder.append("&interface", interfaces);
    builder.append("&final", finals);
    builder.append("&static", statics);
    return builder.getPanel();
  }
}