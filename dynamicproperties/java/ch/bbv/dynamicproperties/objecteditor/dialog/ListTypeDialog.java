/*********************************************************************** 
 * Copyright 2007 BBV Software Services AG (http://www.bbv.ch)
 *  
 * Licensed under the Apache License, Version 2.0 (the "Licence"); 
 * http://www.apache.org/licenses/LICENSE-2.0
 * you may not use this file except in compliance with the License.
 * 
 * $Id$
 * 
 * History:
 * 2007/06/06      Ueli Kurmann     Initial Release
 ***********************************************************************/

package ch.bbv.dynamicproperties.objecteditor.dialog;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JTextField;

import ch.bbv.dynamicproperties.PropertyInstanceManager;
import ch.bbv.dynamicproperties.core.ListType;
import ch.bbv.dynamicproperties.core.PropertyType;

import com.jgoodies.forms.builder.DefaultFormBuilder;

public class ListTypeDialog extends BasicDialog {

    private JTextField nameField;

    private JComboBox comboBox;

    private ListType listType;

    public ListTypeDialog(ListType listType) {
        super();
        this.listType = listType;
        setOkAction(new SaveAction());
        nameField.setText(listType.getName());
        comboBox.setSelectedItem(listType.getElementType());
    }

    @Override
    JComponent createFormArea() {
        DefaultFormBuilder builder = createFormBuilder();

        nameField = new JTextField();
        comboBox = createComboBox();

        builder.append("Name", nameField);
        builder.append("Type", comboBox);

        return builder.getPanel();
    }

    private JComboBox createComboBox() {
        JComboBox comboBox = new JComboBox(PropertyInstanceManager.instance().getTypes().toArray());
        return comboBox;
    }

    private class SaveAction extends AbstractAction {

        public SaveAction() {
            this.putValue(NAME, OK_BUTTON_LABEL);
        }

        public void actionPerformed(ActionEvent arg0) {
            listType.setName(nameField.getText());
            listType.setElementType((PropertyType) comboBox.getSelectedItem());
            ListTypeDialog.this.setVisible(false);
            ListTypeDialog.this.firePropertyChange("Property", null, listType);
        }
    }
}
