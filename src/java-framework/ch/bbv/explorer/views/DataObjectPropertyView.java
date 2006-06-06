/* copyright 2000-2006 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence");
 * you may not use this file except in compliance with the License.
 * $Id$
 */
package ch.bbv.explorer.views;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTextField;

import ch.bbv.dog.DataObject;
import ch.bbv.dog.DataObjectType;
import ch.bbv.explorer.resources.Messages;

/**
 * The data object property view displays a data object property . The object is
 * cloned to allow the user to cancel the operation once it was edited in a
 * child dialog.
 * @author MarcelBaumann
 * @version $Revision$
 */
class DataObjectPropertyView extends PropertyView {
  /**
   * The area containing the visible components of the view.
   */
  private JComponent propertyArea;

  /**
   * The text field display the string representation of the data object if
   * defined otherwise the field is disabled.
   */
  private JTextField text;

  private DataObjectType type;

  private DataObject dataObject;

  private DataObject copy;

  private ButtonActions actions;

  JButton deleteButton;

  JButton editButton;

  public DataObjectPropertyView(String property, Class clazz, String label,
      DataObjectType type) {
    super(property, clazz, label);
    this.type = type;
    propertyArea = new JPanel();
    text = new JTextField(TEXT_FIELD_SIZE);
    text.setEditable(false);
    deleteButton = new JButton(Messages.actionDelete);
    editButton = new JButton(Messages.actionEdit);
    propertyArea.add(text);
    propertyArea.add(deleteButton);
    propertyArea.add(editButton);
    actions = new ButtonActions();
    deleteButton.addActionListener(actions);
    editButton.addActionListener(actions);
  }

  public JComponent getComponent() {
    return propertyArea;
  }

  public void setEditable(boolean editable) {
    deleteButton.setEnabled(editable);
    editButton.setText(editable ? Messages.actionEdit : Messages.actionView);
  }

  protected Object getValue() {
    return (((copy != null) && copy.isModified()) ? copy : dataObject);
  }

  protected void setValue(Object value) {
    dataObject = (DataObject) value;
    copy = null;
    deleteButton.setEnabled(dataObject != null);
    text.setEnabled(dataObject != null);
    text.setText((dataObject != null) ? dataObject.toString() : null);
  }

  /**
   * Deletes the data object. The original object and all changes are lost.
   */
  void deleteObject() {
    setValue(null);
  }

  /**
   * Edits the data object. If the object does not exists it is created. The
   * view associated with the data object type is used to display a dialog to
   * edit the object.
   */
  void editObject() {
    if (dataObject != null) {
      if (copy == null) {
        copy = dataObject.copy();
      }
    } else {
      copy = type.createBoInstance();
      deleteButton.setEnabled(true);
    }
    // TODO open the frame with the view of the data object type.
  }

  class ButtonActions implements ActionListener {
    public void actionPerformed(ActionEvent event) {
      if (event.getSource() == editButton) {
        editObject();
      } else if (event.getSource() == deleteButton) {
        deleteObject();
      }
    }
  }
}