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

import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import ch.bbv.dynamicproperties.core.PropertyValue;
import ch.bbv.dynamicproperties.core.SimpleValue;

/**
 * @author UeliKurmann
 */
public class BooleanEditor extends DefaultEditor implements ChangeListener {

    private JCheckBox checkBox;

    private SimpleValue attributeValue;

    public BooleanEditor(PropertyValue attributeValue) {
        this.attributeValue = (SimpleValue) attributeValue;
        checkBox = new JCheckBox();
        checkBox.addChangeListener(this);
        setValue(((SimpleValue) attributeValue).getValue());
    }

    public JComponent getComponent() {
        return checkBox;
    }

    public Object getValue() {
        return new Boolean(checkBox.isSelected());
    }

    public void setValue(Object value) {
        if (value != null) {
            checkBox.setSelected((Boolean) value);
        }
    }

    public void stateChanged(ChangeEvent arg0) {
        attributeValue.setValue(checkBox.isSelected());
    }
}
