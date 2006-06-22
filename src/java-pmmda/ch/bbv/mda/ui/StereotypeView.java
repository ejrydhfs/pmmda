/* copyright 2000-2006 Marcel Baumann 
 * Licensed under the Apache License, Version 2.0 (the "Licence); 
 * you may not use this file except in compliance with the License. 
 * $Id$
 */
package ch.bbv.mda.ui;

import javax.swing.JCheckBox;
import javax.swing.JComponent;

import ch.bbv.mda.MetaDatatype;
import ch.bbv.mda.Stereotype;
import ch.bbv.mda.generators.CartridgeFactory;
import ch.bbv.mda.validation.IdentifierValidator;

import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.validation.Severity;
import com.jgoodies.validation.ValidationResult;
import com.jgoodies.validation.message.SimpleValidationMessage;
import com.jgoodies.validation.util.ValidationUtils;

/**
 * The stereotype view defines the view of UML stereotype.
 * @author marcelbaumann
 * @version $Revision$
 */
public class StereotypeView extends MetaItemView {
  /**
   * Serial version identifier coded as back compatibility date.
   */
  private static final long serialVersionUID = 20060103;

  /**
   * Flag indicating if the package is a system package.
   */
  private JCheckBox system;

  /**
   * Default constructor of the class.
   */
  public StereotypeView() {
    system = new JCheckBox();
    system.setEnabled(false);
    add(buildPanel());
  }

  @Override
  public void populateView() {
    Stereotype model = (Stereotype) getDataObject();
    if (this.isNewObject()) {
      CartridgeFactory.getInstance().getItemFactory().initialize(model,
          Stereotype.class);
    }
    super.populateView();
  }

  @Override
  public void commitChanges() {
    super.commitChanges();
    getModel().nodeChanged();
  }

  @Override
  public void validateView(ValidationResult result) {
    String identifier = name.getText();
    if (!ValidationUtils.isBlank(identifier)) {
      if (!IdentifierValidator.checkJavaIdentifier(identifier)) {
        result.add(new SimpleValidationMessage("name is not a Java identifier",
            Severity.ERROR, name));
      }
    }
    super.validateView(result);
  }

  @Override
  protected JComponent buildGeneral() {
    DefaultFormBuilder builder = initializeGeneral();
    return builder.getPanel();
  }

  @Override
  protected JComponent buildFlags() {
    DefaultFormBuilder builder = getBuilder(FLAGS_LAYOUT);
    builder.append("&Is System", system);
    return builder.getPanel();
  }
}