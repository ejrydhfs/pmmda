/* copyright 2000-2004 Marcel Baumann 
 * Licensed under the Apache License, Version 2.0 (the "Licence); 
 * you may not use this file except in compliance with the License. 
 * $Id$
 */
package ch.bbv.mda.ui;

import javax.swing.*;

import ch.bbv.dog.ui.ReferenceCodeComboBox;
import ch.bbv.explorer.ExplorerNode;
import ch.bbv.explorer.NodePropertiesView;
import ch.bbv.mda.*;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

/**
 * The meta class view defines the view of a MDA class.
 * @author marcelbaumann
 * @version $Revision$
 */
public class MetaClassView extends NodePropertiesView {
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

  private ReferenceCodeComboBox visibility;

  private JTextArea descriptionText;

  private JScrollPane descriptionPane;

  private JCheckBox abstracts;

  private JCheckBox finals;

  private JCheckBox interfaces;

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
      MetaFactory.getInstance().initialize(model);
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
    ExplorerNode parentNode = (ExplorerNode) getModel().getParent();
    if (parentNode != null) {
      MetaPackage context = (MetaPackage) parentNode.getDataObject();
      context.addClass((MetaClass) getDataObject());
    }
    MetaClass model = (MetaClass) getDataObject();
    String text = nameText.getText();
    if (text != null) {
      model.setName(text.trim());
    }
    text = descriptionText.getText();
    if (text != null) {
      model.setDescription(text.trim());
    }
    model.setVisibility((VisibilityCode) visibility.getSelectedCode());
    model.setAbstract(abstracts.isEnabled());
    model.setFinal(finals.isEnabled());
    model.setInterface(interfaces.isEnabled());
    model.setStatic(statics.isEnabled());
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
        "right:pref, 3dlu, pref, 7dlu, right:pref, 3dlu, pref",
        "p, 3dlu, p, 3dlu, p, 3dlu, p, 6dlu, p, 3dlu, p, 3dlu, p, 6dlu, p, 3dlu, max(32dlu;pref)");
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
    builder.add(visibility, cc.xy(7, 5));
    builder.addLabel("Description", cc.xy(1, 7));
    builder.add(descriptionPane, cc.xywh(3, 7, 5, 1));

    builder.addSeparator("Modifiers", cc.xywh(1, 9, 7, 1));
    builder.addLabel("abstract", cc.xy(1, 11));
    builder.add(abstracts, cc.xy(3, 11));
    builder.addLabel("interface", cc.xy(5, 11));
    builder.add(interfaces, cc.xy(7, 11));
    builder.addLabel("final", cc.xy(1, 13));
    builder.add(finals, cc.xy(3, 13));
    builder.addLabel("static", cc.xy(5, 13));
    builder.add(statics, cc.xy(7, 13));

    builder.addSeparator("Tagged Values", cc.xywh(1, 15, 7, 1));
    builder.add(tagsView, cc.xywh(1, 17, 7, 1, "fill, fill"));

    JPanel panel = builder.getPanel();
    add(panel);
  }
}