/* copyright 2000-2005 Marcel Baumann 
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

import javax.swing.*;

import ch.bbv.dog.ui.ReferenceCodeComboBox;
import ch.bbv.explorer.NodePropertiesView;
import ch.bbv.mda.*;
import ch.bbv.mda.generators.CartridgeFactory;

import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.FormLayout;

/**
 * The meta property view defines the view of a MDA simple or indexed property.
 * @author marcelbaumann
 * @version $Revision$
 */
public class MetaPropertyView extends NodePropertiesView {
  /**
   * Serial version identifier coded as back compatibility date.
   */
  private static final long serialVersionUID = 20041201;

  /**
   * The size of a text field in the view.
   */
  private final static int TEXT_FIELD_SIZE = 20;

  /**
   * The size of a text area in the view.
   */
  private final static int TEXT_AREA_SIZE = 40;

  /**
   * Pattern for validating identifiers
   */
  private static Pattern validName;

  /**
   * Pattern for validating types
   */
  private static Pattern validType;

  private JTextField nameText;

  private JTextField typeText;

  private JTextField contextText;

  private ReferenceCodeComboBox visibility;

  private JTextField multiplicityLowerBoundText;

  private JTextField multiplicityUpperBoundText;

  private JTextField initialValueText;

  private JTextArea descriptionText;

  private JScrollPane descriptionPane;

  private JCheckBox volatiles;

  private JCheckBox transients;

  private JCheckBox finals;

  private JCheckBox statics;

  private TaggedValuesView tagsView;

  /**
   * Default constructor of the class.
   */
  public MetaPropertyView() {
    nameText = new JTextField(TEXT_FIELD_SIZE);
    nameText.addFocusListener(new VerificationActions());
    typeText = new JTextField(TEXT_FIELD_SIZE);
    typeText.addFocusListener(new VerificationActions());
    contextText = new JTextField(TEXT_FIELD_SIZE);
    contextText.setEditable(false);
    visibility = new ReferenceCodeComboBox(VisibilityCode.class, VisibilityCode
        .getCodes());
    multiplicityLowerBoundText = new JTextField();
    multiplicityUpperBoundText = new JTextField();
    initialValueText = new JTextField(TEXT_FIELD_SIZE);
    descriptionText = new JTextArea(3, TEXT_AREA_SIZE);
    descriptionText.setLineWrap(true);
    descriptionText.setWrapStyleWord(true);
    descriptionPane = new JScrollPane(descriptionText,
        JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
        JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    volatiles = new JCheckBox();
    transients = new JCheckBox();
    finals = new JCheckBox();
    statics = new JCheckBox();
    tagsView = new TaggedValuesView();
    addNodePropertiesSubView(tagsView);
    initialize();
  }

  @Override
  public void populateView() {
    MetaProperty model = (MetaProperty) getDataObject();
    if (this.isNewObject()) {
      CartridgeFactory.getInstance().getItemFactory().initialize(model,
          model.getClass());
    }
    nameText.setText(model.getName());
    typeText.setText(model.getTypeName());
    visibility.setSelectedCode(model.getVisibility());
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
    MetaElement context = model.getContext();
    if (context != null) {
      contextText.setText(context.qualifiedName());
    } else {
      contextText.setText(null);
    }
    initialValueText.setText(model.getInitialValue());
    descriptionText.setText(model.getDescription());
  }

  @Override
  public void commitChanges() {
    MetaProperty model = (MetaProperty) getDataObject();
    String text = nameText.getText();
    if (text != null) {
      model.setName(text.trim());
    }
    model.setInitialValue(initialValueText.getText());

    text = descriptionText.getText();
    if (text != null) {
      model.setDescription(text.trim());
    }
    text = typeText.getText();
    if (text != null) {
      model.setTypeName(text.trim());
    }
    model.setVisibility((VisibilityCode) visibility.getSelectedCode());
    model.setVolatile(volatiles.isSelected());
    model.setTransient(transients.isSelected());
    model.setFinal(finals.isSelected());
    model.setStatic(statics.isSelected());
    tagsView.commitChanges();
    getModel().nodeChanged();
  }

  @Override
  public void setViewEditable(boolean editable) {
    nameText.setEditable(editable);
    typeText.setEditable(editable);
    visibility.setEnabled(editable);
    multiplicityLowerBoundText.setEditable(editable);
    multiplicityUpperBoundText.setEditable(editable);
    initialValueText.setEditable(editable);
    descriptionText.setEditable(editable);
    volatiles.setEnabled(editable);
    transients.setEnabled(editable);
    finals.setEnabled(editable);
    statics.setEnabled(editable);
    tagsView.setEnabled(editable);
  }

  @Override
  public void cancelChanges() {
    tagsView.cancelChanges();
  }

  /**
   * Initialize the panel with the user interface elements.
   */
  protected void initialize() {
    FormLayout layout = new FormLayout(
        "right:pref, 3dlu, pref, 7dlu, right:pref, 3dlu, pref", "");
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

    validName = Pattern.compile("^[0-9]"            // no number at the beginning of the identifier
                                +"|[^\\w]");        // no special characters
    
    validType = Pattern.compile("^[0-9\\.]"         // no number and no dot at the beginning of the identifier
                                +"|[^\\w&&[^\\.]]"  // no special characters other than dot
                                +"|\\.[0-9]");      // no number after a dot
  }
  
  /**
   * Focus listeners for verification of input. It applies input rules.
   * @author AdrianBachofen
   *
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