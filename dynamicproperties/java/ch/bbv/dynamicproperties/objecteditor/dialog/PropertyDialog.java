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
import ch.bbv.dynamicproperties.core.Property;
import ch.bbv.dynamicproperties.core.PropertyType;

import com.jgoodies.forms.builder.DefaultFormBuilder;

public class PropertyDialog extends BasicDialog {

    private JTextField nameField;

    private JComboBox comboBox;

    private Property property;

    public PropertyDialog(Property property) {
        super();
        this.property = property;
        setOkAction(new SaveAction());
        nameField.setText(property.getName());
        comboBox.setSelectedItem(property.getType());
    }

    @Override
    JComponent createFormArea() {
        DefaultFormBuilder builder = createFormBuilder();

        nameField = new JTextField();
        comboBox = createComboBox();

        builder.append("Name", nameField);
        builder.append("Types", comboBox);

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
            property.setName(nameField.getText());
            property.setType((PropertyType) comboBox.getSelectedItem());
            PropertyDialog.this.setVisible(false);
            PropertyDialog.this.firePropertyChange("Property", null, property);
        }
    }
}
