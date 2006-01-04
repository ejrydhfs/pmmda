/* copyright 2000-2005 Marcel Baumann 
 * Licensed under the Apache License, Version 2.0 (the "Licence); 
 * you may not use this file except in compliance with the License. 
 * $Id$
 */
package ch.bbv.mda.ui;

import javax.swing.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.Color;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ch.bbv.dog.ui.ReferenceCodeComboBox;
import ch.bbv.explorer.NodePropertiesView;
import ch.bbv.mda.MetaElement;
import ch.bbv.mda.MetaPackage;
import ch.bbv.mda.VisibilityCode;
import ch.bbv.mda.generators.CartridgeFactory;

import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.FormLayout;

/**
 * The meta package view defines the view of a MDA package.
 * @author MarcelBaumann
 * @version $Revision$
 */
public class MetaPackageView extends NodePropertiesView {
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

  private JTextField nameText;

  private JTextField stereotypeText;

  private JTextField contextText;

  private JTextArea descriptionText;

  private JScrollPane descriptionPane;

  private JCheckBox system;

  private TaggedValuesView tagsView;

  private ReferenceCodeComboBox visibility;

  /**
   * Default constructor of the class.
   */
  public MetaPackageView() {
    nameText = new JTextField(TEXT_FIELD_SIZE);
    nameText.addFocusListener(new VerificationActions());
    stereotypeText = new JTextField(TEXT_FIELD_SIZE);
    contextText = new JTextField(TEXT_FIELD_SIZE);
    contextText.setEditable(false);
    descriptionText = new JTextArea(3, TEXT_AREA_SIZE);
    descriptionText.setLineWrap(true);
    descriptionText.setWrapStyleWord(true);
    descriptionPane = new JScrollPane(descriptionText,
        JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
        JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    system = new JCheckBox();
    system.setEnabled(false);
    tagsView = new TaggedValuesView();
    addNodePropertiesSubView(tagsView);
    visibility = new ReferenceCodeComboBox(VisibilityCode.class, VisibilityCode
        .getCodes());
    initialize();
  }

  @Override
  public void setViewEditable(boolean editable) {
    nameText.setEditable(editable);
    stereotypeText.setEditable(editable);
    descriptionText.setEditable(editable);
    tagsView.setViewEditable(editable);
    visibility.setEnabled(editable);
    system.setEnabled(editable);
  }

  @Override
  public void populateView() {
    MetaPackage model = (MetaPackage) getDataObject();
    if (this.isNewObject()) {
      CartridgeFactory.getInstance().getItemFactory().initialize(model,
          MetaPackage.class);
    }
    nameText.setText(model.getName());
    stereotypeText.setText(model.getStereotype());
    descriptionText.setText(model.getDescription());
    system.setSelected(model.isSystem());
    visibility.setSelectedCode(model.getVisibility());
    MetaElement context = model.getContext();
    if (context != null) {
      contextText.setText(context.qualifiedName());
    } else {
      contextText.setText(null);
    }
  }

  @Override
  public void commitChanges() {
    MetaPackage model = (MetaPackage) getDataObject();
    String text = nameText.getText();
    if (text != null) {
      model.setName(text.trim());
    }
    text = descriptionText.getText();
    if (text != null) {
      model.setDescription(text.trim());
    }
    text = stereotypeText.getText();
    if (text != null) {
      model.setStereotype(text.trim());
    }
    model.setVisibility((VisibilityCode) visibility.getSelectedCode());
    model.setSystem(system.isSelected());

    tagsView.commitChanges();
    getModel().nodeChanged();
  }

  @Override
  public void cancelChanges() {
    tagsView.cancelChanges();
  }

  /**
   * Initialize the view component with the user interface elements.
   */
  protected void initialize() {
    FormLayout layout = new FormLayout(
        "right:pref, 3dlu, pref, 7dlu, right:pref, 3dlu, pref", "");
    DefaultFormBuilder builder = new DefaultFormBuilder(layout);
    builder.setDefaultDialogBorder();
    builder.appendSeparator("General");
    builder.append("&Name", nameText);
    builder.append("&Stereotype", stereotypeText);
    builder.append("&Context", contextText);
    builder.append("&Visibility", visibility);
    builder.append("&Description", descriptionPane, 5);
    builder.appendSeparator("Modifiers");
    builder.append("&Is System", system);
    builder.nextLine();
    builder.appendSeparator("Tagged Values");
    builder.append(tagsView, 7);
    JPanel panel = builder.getPanel();
    add(panel);
    
    validName = Pattern.compile("^[0-9\\.]"         // no number and no dot at the beginning of the identifier
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
    }
    public void focusGained(FocusEvent e) {
      // don't do anything
    }
  }
}