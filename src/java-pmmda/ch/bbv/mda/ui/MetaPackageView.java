/* copyright 2000-2004 Marcel Baumann 
 * Licensed under the Apache License, Version 2.0 (the "Licence); 
 * you may not use this file except in compliance with the License. 
 * $Id$
 */
package ch.bbv.mda.ui;

import javax.swing.*;

import ch.bbv.explorer.ExplorerNode;
import ch.bbv.explorer.NodePropertiesView;
import ch.bbv.mda.*;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

/**
 * The meta package view defines the view of a MDA package.
 * @author MarcelBaumann
 * @version $Revision$
 */
public class MetaPackageView extends NodePropertiesView {
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
    initialize();
  }

  public void setViewEditable(boolean editable) {
    nameText.setEditable(editable);
    stereotypeText.setEditable(editable);
    descriptionText.setEditable(editable);
    tagsView.setViewEditable(editable);
  }

  public void populateView() {
    MetaPackage model = (MetaPackage) getDataObject();
    if (this.isNewObject()) {
      MetaFactory.getInstance().initialize(model);
    }
    nameText.setText(model.getName());
    MetaElement context = model.getContext();
    if (context != null) {
      contextText.setText(context.qualifiedName());
    } else {
      contextText.setText(null);
    }
    descriptionText.setText(model.getDescription());
    system.setSelected(model.isSystem());
  }

  public void commitChanges() {
    ExplorerNode parentNode = (ExplorerNode) getModel().getParent();
    if (parentNode != null) {
      Object dataObject = parentNode.getDataObject();
      if (dataObject instanceof MetaModel) {
        MetaModel context = (MetaModel) dataObject;
        context.addPackage((MetaPackage) getDataObject());
      }
    }

    MetaPackage model = (MetaPackage) getDataObject();
    String text = nameText.getText();
    if (text != null) {
      model.setName(text.trim());
    }
    text = descriptionText.getText();
    if (text != null) {
      model.setDescription(text.trim());
    }
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
        "right:pref, 3dlu, pref, 7dlu, right:pref, 3dlu, pref",
        "p, 3dlu, p, 3dlu, p, 3dlu, p, 6dlu, p, 3dlu, p, 6dlu, p, 3dlu, max(32dlu;pref)");
    layout.setColumnGroups(new int[][] { { 1, 5 }, { 3, 7 } });

    PanelBuilder builder = new PanelBuilder(layout);
    builder.setDefaultDialogBorder();

    CellConstraints cc = new CellConstraints();

    builder.addSeparator("General", cc.xywh(1, 1, 7, 1));
    builder.addLabel("Name", cc.xy(1, 3));
    builder.add(nameText, cc.xy(3, 3));
    builder.addLabel("Stereotype", cc.xy(5, 3));
    builder.add(stereotypeText, cc.xy(7, 3));
    builder.addLabel("Context", cc.xy(1, 5));
    builder.add(contextText, cc.xy(3, 5));
    builder.addLabel("Visibility", cc.xy(5, 5));
    builder.addLabel("visibility Text", cc.xy(7, 5));
    builder.addLabel("Description", cc.xy(1, 7));
    builder.add(descriptionPane, cc.xywh(3, 7, 5, 1));
    builder.addSeparator("Modifiers", cc.xywh(1, 9, 7, 1));
    builder.addLabel("Is system", cc.xy(1, 11));
    builder.add(system, cc.xy(3, 11));
    builder.addSeparator("Tagged Values", cc.xywh(1, 13, 7, 1));
    builder.add(tagsView, cc.xywh(1, 15, 7, 1, "fill, fill"));

    JPanel panel = builder.getPanel();
    add(panel);
  }
}