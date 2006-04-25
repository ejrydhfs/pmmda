/* copyright 2000-2006 Marcel Baumann 
 * Licensed under the Apache License, Version 2.0 (the "Licence); 
 * you may not use this file except in compliance with the License. 
 * $Id$
 */
package ch.bbv.mda.ui;

import java.awt.Color;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JTextField;

import ch.bbv.mda.MetaIndexedProperty;
import ch.bbv.mda.MetaProperty;
import ch.bbv.mda.Multiplicity;
import ch.bbv.mda.VisibilityCode;

import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.FormLayout;

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
  static Pattern validType;

  JTextField typeText;

  private JTextField multiplicityLowerBoundText;

  private JTextField multiplicityUpperBoundText;

  private JTextField initialValueText;

  private JCheckBox volatiles;

  private JCheckBox transients;

  private JCheckBox finals;

  private JCheckBox statics;

  /**
   * Default constructor of the class.
   */
  public MetaPropertyView() {
    typeText = new JTextField(TEXT_FIELD_SIZE);
    typeText.addFocusListener(new VerificationActions());
    multiplicityLowerBoundText = new JTextField();
    multiplicityUpperBoundText = new JTextField();
    initialValueText = new JTextField(TEXT_FIELD_SIZE);
    volatiles = new JCheckBox();
    transients = new JCheckBox();
    finals = new JCheckBox();
    statics = new JCheckBox();
    initialize();
  }

  @Override
  public void populateView() {
    super.populateView();
    MetaProperty model = (MetaProperty) getDataObject();
    typeText.setText(model.getTypeName());
    typeText.setBackground(Color.WHITE);
    volatiles.setSelected(model.isVolatile());
    transients.setSelected(model.isTransient());
    statics.setSelected(model.isStatic());
    finals.setSelected(model.isFinal());
    Multiplicity multiplicity = model.getMultiplicity();
    multiplicityLowerBoundText.setText(Multiplicity.stringValue(multiplicity
        .getLowerBound()));
    multiplicityUpperBoundText.setText(Multiplicity.stringValue(multiplicity
        .getUpperBound()));
    if (model instanceof MetaIndexedProperty) {
      multiplicityLowerBoundText.setEditable(true);
      multiplicityUpperBoundText.setEditable(true);
    } else {
      multiplicityLowerBoundText.setEditable(false);
      multiplicityUpperBoundText.setEditable(false);
    }
    initialValueText.setText(model.getInitialValue());
  }

  @Override
  public void commitChanges() {
    super.commitChanges();
    MetaProperty model = (MetaProperty) getDataObject();
    model.setInitialValue(initialValueText.getText());
    String text = typeText.getText();
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
  public void setViewEditable(boolean editable) {
    super.setViewEditable(editable);
    typeText.setEditable(editable);
    multiplicityLowerBoundText.setEditable(editable);
    multiplicityUpperBoundText.setEditable(editable);
    initialValueText.setEditable(editable);
    volatiles.setEnabled(editable);
    transients.setEnabled(editable);
    finals.setEnabled(editable);
    statics.setEnabled(editable);
  }

  /**
   * Initialize the panel with the user interface elements.
   */
  protected void initialize() {
    FormLayout layout = new FormLayout(LAYOUT, "");
    DefaultFormBuilder builder = new DefaultFormBuilder(layout);
    builder.setDefaultDialogBorder();
    builder.appendSeparator("General");
    builder.append("&Name", nameText);
    builder.append("&Type", typeText);
    builder.append("&Context", contextText);
    builder.append("&Visibility", visibility);
    builder.append("&Lower Bound", multiplicityLowerBoundText);
    builder.append("&Upper Bound", multiplicityUpperBoundText);
    builder.append("&Initial Value", initialValueText);
    builder.nextLine();
    builder.append("&Description", descriptionPane, 5);
    builder.appendSeparator("Modifiers");
    builder.append("&volatile", volatiles);
    builder.append("&transient", transients);
    builder.append("&final", finals);
    builder.append("&static", statics);
    builder.appendSeparator("Tagged Values");
    builder.append(tagsView, 7);
    JPanel panel = builder.getPanel();
    add(panel);

    validName = Pattern.compile("^[0-9]" // no number at the beginning of the
        // identifier
        + "|[^\\w]"); // no special characters

    validType = Pattern.compile("^[0-9\\.]" // no number and no dot at the
        // beginning of the identifier
        + "|[^\\w&&[^\\.]]" // no special characters other than dot
        + "|\\.[0-9]"); // no number after a dot
  }

  /**
   * Focus listeners for verification of input. It applies input rules.
   * @author AdrianBachofen
   * @version $Revision$
   */
  private class VerificationActions implements FocusListener {
    public void focusLost(FocusEvent e) {
      if (e.getSource() == nameText) {
        Matcher matcher = validName.matcher(nameText.getText());
        if (matcher.find()) {
          nameText.setBackground(ALERT_COLOR);
        } else {
          nameText.setBackground(Color.WHITE);
        }
      }
      if (e.getSource() == typeText) {
        Matcher matcher = validType.matcher(typeText.getText());
        if (matcher.find()) {
          typeText.setBackground(ALERT_COLOR);
        } else {
          typeText.setBackground(Color.WHITE);
        }
      }
    }

    public void focusGained(FocusEvent e) {
      // don't do anything
    }
  }
}