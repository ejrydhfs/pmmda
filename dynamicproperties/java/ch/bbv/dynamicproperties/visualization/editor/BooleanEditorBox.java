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

package ch.bbv.dynamicproperties.visualization.editor;

import javax.swing.ButtonGroup;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import ch.bbv.dynamicproperties.core.PropertyValue;
import ch.bbv.dynamicproperties.core.SimpleValue;

public class BooleanEditorBox extends DefaultEditor {

    private ButtonGroup group = new ButtonGroup();

    private JPanel panel;

    private JRadioButton b1;

    private JRadioButton b2;

    public BooleanEditorBox(PropertyValue attributeValue) {
        panel = new JPanel();
        b1 = new JRadioButton("True");
        b2 = new JRadioButton("False");
        group.add(b1);
        group.add(b2);

        panel.add(b1);
        panel.add(b2);

        setValue(((SimpleValue) attributeValue).getValue());
    }

    public JComponent getComponent() {
        return panel;
    }

    public Object getValue() {
        return new Boolean(group.getSelection().equals("True"));
    }

    public void setValue(Object value) {
        if ((Boolean) value) {
            b1.setSelected(true);
            b2.setSelected(false);
        } else {
            b1.setSelected(false);
            b2.setSelected(true);
        }
    }
}
