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
 * The meta property view defines the view of a MDA simple or indexed property.
 * @author marcelbaumann
 * @version $Revision$
 */
public class MetaPropertyView extends NodePropertiesView {
  /**
   * The size of a text field in the view.
   */
  private final static int TEXT_FIELD_SIZE = 20;

  /**
   * The size of a text area in the view.
   */
  private final static int TEXT_AREA_SIZE = 40;

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
    typeText = new JTextField(TEXT_FIELD_SIZE);
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

  public void populateView() {
    MetaProperty model = (MetaProperty) getDataObject();
    if (this.isNewObject()) {
      MetaFactory.getInstance().initialize(model);
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

  public void commitChanges() {
    if (isNewObject()) {
      ExplorerNode parentNode = (ExplorerNode) getModel().getParent();
      if (parentNode != null) {
        Object dataObject = parentNode.getDataObject();
        MetaClass context = (MetaClass) dataObject;
        context.addProperty((MetaProperty) getDataObject());
      }
    }

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
    model.setVisibility((VisibilityCode) visibility.getSelectedCode());
    model.setVolatile(volatiles.isSelected());
    model.setTransient(transients.isSelected());
    model.setFinal(finals.isSelected());
    model.setStatic(statics.isSelected());
    tagsView.commitChanges();
    getModel().nodeChanged();
  }

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

  public void cancelChanges() {
    tagsView.cancelChanges();
  }

  /**
   * Initialize the panel with the user interface elements.
   */
  protected void initialize() {
    FormLayout layout = new FormLayout(
        "right:pref, 3dlu, pref, 7dlu, right:pref, 3dlu, pref",
        "p, 3dlu, p, 3dlu, p, 3dlu, p, 3dlu, p, 3dlu, p, 6dlu, p, 3dlu, p, 3dlu, p, 6dlu, p, 3dlu, max(32dlu;pref)");
    layout.setColumnGroups(new int[][] { { 1, 5 }, { 3, 7 } });

    PanelBuilder builder = new PanelBuilder(layout);
    builder.setDefaultDialogBorder();

    CellConstraints cc = new CellConstraints();

    builder.addSeparator("General", cc.xywh(1, 1, 7, 1));
    builder.addLabel("Name", cc.xy(1, 3));
    builder.add(nameText, cc.xy(3, 3));
    builder.addLabel("Type", cc.xy(5, 3));
    builder.add(typeText, cc.xy(7, 3));

    builder.addLabel("Context", cc.xy(1, 5));
    builder.add(contextText, cc.xy(3, 5));
    builder.addLabel("Visibility", cc.xy(5, 5));
    builder.add(visibility, cc.xy(7, 5));

    builder.addLabel("Lower Bound", cc.xy(1, 7));
    builder.add(multiplicityLowerBoundText, cc.xy(3, 7));
    builder.addLabel("Upper Bound", cc.xy(5, 7));
    builder.add(multiplicityUpperBoundText, cc.xy(7, 7));

    builder.addLabel("Initial Value", cc.xy(1, 9));
    builder.add(initialValueText, cc.xy(3, 9));

    builder.addLabel("Description", cc.xy(1, 11));
    builder.add(descriptionPane, cc.xywh(3, 11, 5, 1));

    builder.addSeparator("Modifiers", cc.xywh(1, 13, 7, 1));
    builder.addLabel("volatile", cc.xy(1, 15));
    builder.add(volatiles, cc.xy(3, 15));
    builder.addLabel("transient", cc.xy(5, 15));
    builder.add(transients, cc.xy(7, 15));

    builder.addLabel("final", cc.xy(1, 17));
    builder.add(finals, cc.xy(3, 17));
    builder.addLabel("static", cc.xy(5, 17));
    builder.add(statics, cc.xy(7, 17));

    builder.addSeparator("Tagged Values", cc.xywh(1, 19, 7, 1));
    builder.add(tagsView, cc.xywh(1, 21, 7, 1, "fill, fill"));

    JPanel panel = builder.getPanel();
    add(panel);
  }
}