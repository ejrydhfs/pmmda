/* copyright 2000-2005 Marcel Baumann 
 * Licensed under the Apache License, Version 2.0 (the "Licence); 
 * you may not use this file except in compliance with the License. 
 * $Id$
 */
package ch.bbv.mda.ui;

import javax.swing.*;

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

  public void setViewEditable(boolean editable) {
    nameText.setEditable(editable);
    stereotypeText.setEditable(editable);
    descriptionText.setEditable(editable);
    tagsView.setViewEditable(editable);
    visibility.setEnabled(editable);
  }

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

    tagsView.commitChanges();
    getModel().nodeChanged();
  }

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
  }
}