/* copyright 2000-2005 Marcel Baumann 
 * Licensed under the Apache License, Version 2.0 (the "Licence); 
 * you may not use this file except in compliance with the License. 
 * $Id$
 */
package ch.bbv.mda.ui;

import javax.swing.*;

import ch.bbv.dog.ui.ReferenceCodeComboBox;
import ch.bbv.explorer.NodePropertiesView;
import ch.bbv.mda.MetaClass;
import ch.bbv.mda.MetaElement;
import ch.bbv.mda.VisibilityCode;
import ch.bbv.mda.generators.CartridgeFactory;

import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.FormLayout;

/**
 * The meta class view defines the view of a MDA class.
 * @author marcelbaumann
 * @version $Revision$
 */
public class MetaClassView extends NodePropertiesView {
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
   * Swing component to display the associated property.
   */
  private JTextField nameText;

  /**
   * Swing component to display the associated property.
   */
  private JTextField stereotypeText;

  /**
   * Swing component to display the associated property.
   */
  private JTextField contextText;

  /**
   * Swing component to display the associated property.
   */
  private ReferenceCodeComboBox visibility;

  /**
   * Swing component to display the associated property.
   */
  private JTextArea descriptionText;

  /**
   * Swing component to display the associated property.
   */
  private JScrollPane descriptionPane;

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

  private TaggedValuesView tagsView;

  /**
   * Default constructor of the class.
   */
  public MetaClassView() {
    nameText = new JTextField(TEXT_FIELD_SIZE);
    stereotypeText = new JTextField(TEXT_FIELD_SIZE);
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
    abstracts = new JCheckBox();
    finals = new JCheckBox();
    interfaces = new JCheckBox();
    statics = new JCheckBox();
    tagsView = new TaggedValuesView();
    addNodePropertiesSubView(tagsView);
    initialize();
  }

  public void populateView() {
    MetaClass model = (MetaClass) getDataObject();
    if (this.isNewObject()) {
      CartridgeFactory.getInstance().getItemFactory().initialize(model, MetaClass.class);
    }
    nameText.setText(model.getName());
    stereotypeText.setText(model.getStereotype());
    visibility.setSelectedCode(model.getVisibility());
    abstracts.setSelected(model.isAbstract());
    interfaces.setSelected(model.isInterface());
    statics.setSelected(model.isStatic());
    finals.setSelected(model.isFinal());

    MetaElement context = model.getContext();
    if (context != null) {
      contextText.setText(context.qualifiedName());
    } else {
      contextText.setText(null);
    }
    descriptionText.setText(model.getDescription());
  }

  public void commitChanges() {
    MetaClass model = (MetaClass) getDataObject();
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
    model.setAbstract(abstracts.isSelected());
    model.setFinal(finals.isSelected());
    model.setInterface(interfaces.isSelected());
    model.setStatic(statics.isSelected());
    tagsView.commitChanges();
    getModel().nodeChanged();
  }

  public void setViewEditable(boolean editable) {
    nameText.setEditable(editable);
    stereotypeText.setEditable(editable);
    descriptionText.setEditable(editable);
    visibility.setEnabled(editable);
    abstracts.setEnabled(editable);
    finals.setEnabled(editable);
    interfaces.setEnabled(editable);
    statics.setEnabled(editable);
    tagsView.setEnabled(editable);
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
    builder.append("&abstract", abstracts);
    builder.append("&interface", interfaces);
    builder.append("&final", finals);
    builder.append("&static", statics);
    builder.appendSeparator("Tagged Values");
    builder.append(tagsView, 7);
    JPanel panel = builder.getPanel();
    add(panel);
  }
}