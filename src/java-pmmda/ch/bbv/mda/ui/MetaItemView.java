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
import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.validation.Severity;
import com.jgoodies.validation.ValidationResult;
import com.jgoodies.validation.message.SimpleValidationMessage;
import com.jgoodies.validation.util.DefaultValidationResultModel;
import com.jgoodies.validation.util.ValidationUtils;
import com.jgoodies.validation.view.ValidationComponentUtils;
import com.jgoodies.validation.view.ValidationResultViewFactory;

class MetaItemView extends NodePropertiesView implements ActionListener {
  /**
   * Serial version identifier for the instances of the class.
   */
  private static final long serialVersionUID = -8559504085949561644L;

  /**
   * The size of a text field in the view.
   */
  protected final static int TEXT_FIELD_SIZE = 20;

  /**
   * The size of a text area in the view.
   */
  protected final static int TEXT_AREA_SIZE = 40;

  /**
   * Layout for the general section of the item dialog forms.
   */
  final static String GENERAL_LAYOUT = "right:pref, 3dlu, pref, 7dlu, right:pref, 3dlu, pref";

  /**
   * Layout for the general section of the item dialog forms.
   */
  final static String FLAGS_LAYOUT = "right:pref, 3dlu, pref, 17dlu, pref, 3dlu, pref, 17dlu, pref, 3dlu, pref, 17dlu, pref, 3dlu, pref";

  /**
   * Swing component to display the associated property.
   */
  protected JTextField name;

  /**
   * Swing component to display the associated property.
   */
  protected JTextField stereotypesText;

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
  protected JTextArea description;

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
    name = new JTextField(TEXT_FIELD_SIZE);
    stereotypes = new ArrayList<Stereotype>();
    stereotypesText = new JTextField(TEXT_FIELD_SIZE);
    stereotypesText.setEditable(false);
    contextText = new JTextField(TEXT_FIELD_SIZE);
    contextText.setEditable(false);
    visibility = new ReferenceCodeComboBox(VisibilityCode.class, VisibilityCode
        .getCodes());
    description = new JTextArea(4, TEXT_AREA_SIZE);
    description.setLineWrap(true);
    description.setWrapStyleWord(true);
    descriptionPane = new JScrollPane(description,
        JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
        JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    tagsView = new TaggedValuesView();
    addNodePropertiesSubView(tagsView);
    ValidationComponentUtils.setMandatory(name, true);
    ValidationComponentUtils.setMandatory(visibility, true);
    validationResultModel = new DefaultValidationResultModel();
  }

  @Override
  public void setViewEditable(boolean editable) {
    name.setEditable(editable);
    description.setEditable(editable);
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
    name.setText(model.getName());
    name.setBackground(Color.WHITE);
    MetaElement context = model.getContext();
    contextText.setText(context != null ? context.getQualifiedName() : null);
    updateStereotypes(model.getStereotypes());
    visibility.setSelectedCode(model.getVisibility());
    description.setText(model.getDescription());
  }

  @Override
  public void commitChanges() {
    MetaElement model = (MetaElement) getDataObject();
    String text = name.getText();
    if (text != null) {
      model.setName(text.trim());
    }
    text = description.getText();
    model.setDescription(text != null ? text.trim() : null);
    model.setStereotypes(stereotypes);
    model.setVisibility((VisibilityCode) visibility.getSelectedCode());
    tagsView.commitChanges();
  }

  @Override
  public void validateView(ValidationResult result) {
    if (ValidationUtils.isBlank(name.getText())) {
      result.add(new SimpleValidationMessage("name is mandatory",
          Severity.ERROR, name));
    }
    MetaElement item = (MetaElement) getDataObject();
    if (!item.isSystem() && ValidationUtils.isBlank(description.getText())) {
      result.add(new SimpleValidationMessage("description should be provided",
          Severity.WARNING, description));
    }
    super.validateView(result);
    validationResultModel.setResult(result);
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
   * Builds the view component with the user interface elements.
   * @return the panel containing all components
   */
  protected JPanel buildPanel() {
    FormLayout layout = new FormLayout("fill:pref:grow",
        "p, 3dlu, p, 9dlu, p, 3dlu, p, 9dlu, p, 3dlu, p, 12dlu, p, 9dlu");
    PanelBuilder builder = new PanelBuilder(layout);
    builder.setDefaultDialogBorder();
    CellConstraints cc = new CellConstraints();
    builder.addSeparator("General", cc.xy(1, 1));
    builder.add(buildGeneral(), cc.xy(1, 3));
    builder.addSeparator("Modifiers", cc.xy(1, 5));
    builder.add(buildFlags(), cc.xy(1, 7));
    builder.addSeparator("Tagged Values", cc.xy(1, 9));
    builder.add(tagsView, cc.xy(1, 11));
    builder.add(ValidationResultViewFactory
        .createReportList(validationResultModel), cc.xy(1, 13));
    ValidationComponentUtils.updateComponentTreeMandatoryBorder(builder
        .getPanel());
    ValidationComponentUtils.updateComponentTreeMandatoryBackground(builder
        .getPanel());
    return builder.getPanel();
  }

  /**
   * Initializes the panel with the user interface elements.
   * @return form builder to use
   */
  protected DefaultFormBuilder initializeGeneral() {
    DefaultFormBuilder builder = getBuilder(GENERAL_LAYOUT);
    builder.append("&Name", name);
    builder.append("&Context", contextText);
    builder.append("&Visibility", visibility);
    editStereotypes = new JButton("Stereotypes ...");
    editStereotypes.setHorizontalTextPosition(SwingConstants.RIGHT);
    editStereotypes.addActionListener(this);
    builder.append(editStereotypes, stereotypesText);
    builder.nextLine();
    builder.append("&Description", descriptionPane, 5);
    return builder;
  }

  /**
   * Returns a default builder with the given horizontal layout.
   * @param layout layout to use in the builder
   * @return a new builder instance
   * @pre layout != null
   */
  protected DefaultFormBuilder getBuilder(String layout) {
    assert layout != null;
    DefaultFormBuilder builder = new DefaultFormBuilder(new FormLayout(layout,
        ""));
    builder.setDefaultDialogBorder();
    return builder;
  }

  protected JComponent buildGeneral() {
    return null;
  }

  protected JComponent buildFlags() {
    return null;
  }

  void updateStereotypes(List<Stereotype> stereotypes) {
    this.stereotypes.clear();
    this.stereotypes.addAll(stereotypes);
    StringBuilder buffer = new StringBuilder();
    for (Stereotype stereotype : this.stereotypes) {
      buffer.append("<<").append(stereotype.getName()).append(">>").append(" ");
    }
    stereotypesText.setText(buffer.toString());
  }
}
