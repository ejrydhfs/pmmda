/* copyright 2000-2006 Marcel Baumann 
 * Licensed under the Apache License, Version 2.0 (the "Licence"); 
 * you may not use this file except in compliance with the License. 
 * $Id$ 
 */
package ch.bbv.mda.ui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;

import ch.bbv.dog.ui.ReferenceCodeComboBox;
import ch.bbv.explorer.Explorer;
import ch.bbv.explorer.NodePropertiesView;
import ch.bbv.mda.MetaElement;
import ch.bbv.mda.Stereotype;
import ch.bbv.mda.VisibilityCode;
import ch.bbv.mda.generators.CartridgeFactory;

import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.FormLayout;

class MetaItemView extends NodePropertiesView implements ActionListener {
  /**
   * The size of a text field in the view.
   */
  protected final static int TEXT_FIELD_SIZE = 20;

  /**
   * The size of a text area in the view.
   */
  protected final static int TEXT_AREA_SIZE = 40;

  /**
   * Layout for the item dialog forms.
   */
  final static String LAYOUT = "right:pref, 3dlu, pref, 7dlu, right:pref, 3dlu, pref";

  /**
   * Swing component to display the associated property.
   */
  JTextField nameText;

  /**
   * Swing component to display the associated property.
   */
  protected JTextField stereotypeText;

  /**
   * Read-only swing component to display the associated property.
   */
  protected JTextField contextText;

  /**
   * Swing component to display the associated property.
   */
  protected ReferenceCodeComboBox visibility;

  /**
   * Swing component to display the associated property.
   */
  protected JTextArea descriptionText;

  /**
   * Swing component to display the associated property.
   */
  protected JScrollPane descriptionPane;

  /**
   * The tags view of the item.
   */
  protected TaggedValuesView tagsView;

  /**
   * The stereotypes of the item.
   */
  protected List<Stereotype> stereotypes;

  /**
   * Button opening the stereotype edition dialog.
   */
  private JButton editStereotypes;

  /**
   * Default constructor of the class.
   */
  MetaItemView() {
    nameText = new JTextField(TEXT_FIELD_SIZE);
    stereotypes = new ArrayList<Stereotype>();
    stereotypeText = new JTextField(TEXT_FIELD_SIZE);
    stereotypeText.setEditable(false);
    contextText = new JTextField(TEXT_FIELD_SIZE);
    contextText.setEditable(false);
    visibility = new ReferenceCodeComboBox(VisibilityCode.class, VisibilityCode
        .getCodes());
    descriptionText = new JTextArea(3, TEXT_AREA_SIZE);
    descriptionText.setLineWrap(true);
    descriptionText.setWrapStyleWord(true);
    descriptionPane = new JScrollPane(descriptionText,
        JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
        JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    tagsView = new TaggedValuesView();
    addNodePropertiesSubView(tagsView);
  }

  @Override
  public void setViewEditable(boolean editable) {
    nameText.setEditable(editable);
    descriptionText.setEditable(editable);
    visibility.setEnabled(editable);
    tagsView.setEnabled(editable);
    editStereotypes.setEnabled(editable);
  }

  @Override
  public void cancelChanges() {
    tagsView.cancelChanges();
  }

  @Override
  public void populateView() {
    MetaElement model = (MetaElement) getDataObject();
    if (isNewObject()) {
      CartridgeFactory.getInstance().getItemFactory().initialize(model,
          model.getClass());
    }
    nameText.setText(model.getName());
    nameText.setBackground(Color.WHITE);
    MetaElement context = model.getContext();
    contextText.setText(context != null ? context.qualifiedName() : null);
    updateStereotypes(model.getStereotypes());
    visibility.setSelectedCode(model.getVisibility());
    descriptionText.setText(model.getDescription());
  }

  @Override
  public void commitChanges() {
    MetaElement model = (MetaElement) getDataObject();
    String text = nameText.getText();
    if (text != null) {
      model.setName(text.trim());
    }
    text = descriptionText.getText();
    model.setDescription(text != null ? text.trim() : null);
    model.setStereotypes(stereotypes);
    model.setVisibility((VisibilityCode) visibility.getSelectedCode());
    tagsView.commitChanges();
  }

  /**
   * @inheritDoc
   */
  public void actionPerformed(ActionEvent event) {
    if (editStereotypes == event.getSource()) {
      MetaElement model = (MetaElement) getDataObject();
      StereotypesDialog dialog = new StereotypesDialog(((Explorer) this
          .getNode().getNodeOwner()).getFrame());
      dialog.populate(this, stereotypes, model, false);
      dialog.setVisible(true);
    }
  }

  /**
   * Initialize the panel with the user interface elements.
   * @return form builder to use
   */
  protected DefaultFormBuilder initializeGeneral() {
    FormLayout layout = new FormLayout(LAYOUT, "");
    DefaultFormBuilder builder = new DefaultFormBuilder(layout);
    builder.setDefaultDialogBorder();
    builder.appendSeparator("General");
    builder.append("&Name", nameText);
    builder.append("&Context", contextText);
    builder.append("&Visibility", visibility);
    editStereotypes = new JButton("Stereotypes ...");
    editStereotypes.setHorizontalTextPosition(SwingConstants.RIGHT);
    editStereotypes.addActionListener(this);
    builder.append(editStereotypes, stereotypeText);
    builder.nextLine();
    builder.append("&Description", descriptionPane, 5);
    return builder;
  }

  void updateStereotypes(List<Stereotype> stereotypes) {
    this.stereotypes.clear();
    this.stereotypes.addAll(stereotypes);
    StringBuilder buffer = new StringBuilder();
    for (Stereotype stereotype : this.stereotypes) {
      buffer.append("<<").append(stereotype.getName()).append(">>").append(" ");
    }
    stereotypeText.setText(buffer.toString());

  }
}
