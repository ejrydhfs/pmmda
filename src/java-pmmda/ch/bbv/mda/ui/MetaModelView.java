/* copyright 2000-2004 Marcel Baumann 
 * Licensed under the Apache License, Version 2.0 (the "Licence); 
 * you may not use this file except in compliance with the License. 
 * $Id$
 */
package ch.bbv.mda.ui;

import javax.swing.JPanel;
import javax.swing.JTextField;

import ch.bbv.explorer.NodePropertiesView;
import ch.bbv.mda.MetaModel;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

/**
 * The view has two areas. The first area displays the fields of a meta package.
 * The second area displays all tagged values associcated with the meta element.
 * @author marcelbaumann
 * @version $Revision$
 */
public class MetaModelView extends NodePropertiesView {
  /** 
   * The size of a text field in the view.
   */
  private final static int TEXT_FIELD_SIZE = 20;

  private JTextField nameText;

  private JTextField filenameText;

  /**
   * Default constructor of the class.
   */
  public MetaModelView() {
    nameText = new JTextField();
    filenameText = new JTextField(TEXT_FIELD_SIZE);
    filenameText.setEditable(false);
    initialize();
  }

  public void setViewEditable(boolean editable) {
    nameText.setEditable(editable);
  }

  public void populateView() {
    MetaModel model = (MetaModel) getDataObject();
    nameText.setText(model.getName());
    filenameText.setText(model.getFilename());
  }

  public void commitChanges() {
    MetaModel model = (MetaModel) getDataObject();
    model.setName(nameText.getText());
    model.setFilename(filenameText.getText());
    getModel().nodeChanged();
  }

  public void cancelChanges() {
  }

  /**
   * Initialize the view component with the user interface elements.
   */
  protected void initialize() {
    FormLayout layout = new FormLayout("right:pref, 3dlu, pref:grow",
        "p, 3dlu, p, 3dlu, p, 3dlu");
    PanelBuilder builder = new PanelBuilder(layout);
    builder.setDefaultDialogBorder();

    CellConstraints cc = new CellConstraints();

    builder.addSeparator("General", cc.xywh(1, 1, 3, 1));
    builder.addLabel("Name", cc.xy(1, 3));
    builder.add(nameText, cc.xy(3, 3));
    builder.addLabel("Filename", cc.xy(1, 5));
    builder.add(filenameText, cc.xy(3, 5));

    JPanel panel = builder.getPanel();
    add(panel);
  }
}