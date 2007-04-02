/* copyright 2000-2006 Marcel Baumann 
 * Licensed under the Apache License, Version 2.0 (the "Licence); 
 * you may not use this file except in compliance with the License. 
 * $Id$
 */
package ch.bbv.mda.ui;

import java.io.File;

import javax.swing.JPanel;
import javax.swing.JTextField;

import ch.bbv.explorer.NodePropertiesView;
import ch.bbv.mda.MetaModel;

import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.validation.view.ValidationComponentUtils;

/**
 * The view has two areas. The first area displays the fields of a meta package.
 * The second area displays all tagged values associcated with the meta element.
 * @author marcelbaumann
 * @version $Revision$
 */
public class MetaModelView extends NodePropertiesView {
  /**
   * Serial version identifier for the instances of the class.
   */
  private static final long serialVersionUID = 20041201;

  /**
   * The size of a text field in the view.
   */
  private final static int TEXT_FIELD_SIZE = 50;

  /**
   * Name of the model.
   */
  private JTextField name;

  /**
   * Directory containing the model and associated information.
   */
  private JTextField root;

  /**
   * Name of the file containing the model.
   */
  private JTextField filename;

  /**
   * Default constructor of the class.
   */
  public MetaModelView() {
    name = new JTextField();
    root = new JTextField(TEXT_FIELD_SIZE);
    root.setEditable(false);
    filename = new JTextField(TEXT_FIELD_SIZE);
    filename.setEditable(false);
    add(buildPanel());
  }

  @Override
  public void setViewEditable(boolean editable) {
    name.setEditable(editable);
  }

  @Override
  public void populateView() {
    String emptyFileName = "....";
    MetaModel model = (MetaModel) getDataObject();
    name.setText(model.getName());
    if (model.getFilename() == null || model.getFilename().equals("")
        || model.getFilename().equals(emptyFileName)) {
      root.setText("\"Unnamed model\"");
      filename.setText(emptyFileName);
    } else {
      File file = new File(model.getFilename());
      root.setText(file.getParent());
      filename.setText(file.getAbsolutePath());
    }
  }

  @Override
  public void commitChanges() {
    MetaModel model = (MetaModel) getDataObject();
    model.setName(name.getText());
    model.setFilename(filename.getText());
    getModel().nodeChanged();
  }

  @Override
  public void cancelChanges() {
  }

  /**
   * Builds the view component with the user interface elements.
   * @return the panel containing all components
   */
  protected JPanel buildPanel() {
    FormLayout layout = new FormLayout("right:pref, 3dlu, fill:pref:grow", "");
    DefaultFormBuilder builder = new DefaultFormBuilder(layout);
    builder.setDefaultDialogBorder();
    builder.appendSeparator("General");
    builder.append("&Name", name);
    builder.append("&Filename", filename);
    builder.append("&Root Directory", root);
    JPanel panel = builder.getPanel();
    ValidationComponentUtils.setMandatory(name, true);
    ValidationComponentUtils.updateComponentTreeMandatoryBorder(panel);
    return panel;
  }
}