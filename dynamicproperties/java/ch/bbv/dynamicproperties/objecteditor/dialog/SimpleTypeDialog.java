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
import javax.swing.JComponent;
import javax.swing.JTextField;

import ch.bbv.dynamicproperties.core.SimpleType;

import com.jgoodies.forms.builder.DefaultFormBuilder;

public class SimpleTypeDialog extends BasicDialog {

    private JTextField nameField;

    private SimpleType simpleType;

    public SimpleTypeDialog(SimpleType simpleType) {
        super();
        this.simpleType = simpleType;
        setOkAction(new SaveAction());
        nameField.setText(simpleType.getName());
    }

    @Override
    JComponent createFormArea() {
        DefaultFormBuilder builder = createFormBuilder();

        nameField = new JTextField();

        builder.append("Name", nameField);

        return builder.getPanel();
    }

    private class SaveAction extends AbstractAction {

        public SaveAction() {
            this.putValue(NAME, OK_BUTTON_LABEL);
        }

        public void actionPerformed(ActionEvent arg0) {
            simpleType.setName(nameField.getText());
            SimpleTypeDialog.this.setVisible(false);
            SimpleTypeDialog.this.firePropertyChange("Property", null, simpleType);
        }
    }
}
