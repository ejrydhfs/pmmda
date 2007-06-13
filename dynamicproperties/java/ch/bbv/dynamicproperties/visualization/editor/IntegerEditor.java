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

import java.awt.Dimension;
import java.io.Serializable;

import javax.swing.JComponent;
import javax.swing.JSpinner;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import ch.bbv.dynamicproperties.core.PropertyValue;

/**
 * @author UeliKurmann
 */
@SuppressWarnings("serial")
public class IntegerEditor extends DefaultEditor implements ChangeListener {

    private JSpinner spinner;

    private PropertyValue attributeValue;

    public IntegerEditor(PropertyValue value) {
        this((Integer) value.getValue());
        this.attributeValue = value;
    }

    public IntegerEditor(Integer integer) {
        spinner = new JSpinner();

        if (integer == null) {
            spinner.setValue(0);
        } else {
            spinner.setValue(integer);
        }

        spinner.setPreferredSize(new Dimension(120, 20));
        spinner.addChangeListener(this);
    }

    public JComponent getComponent() {
        return spinner;
    }

    public Object getValue() {
        return spinner.getValue();
    }

    public void setValue(Object value) {
        if (value == null) {
            value = 0;
        }
        spinner.setValue(value);
    }

    public void stateChanged(ChangeEvent event) {
        attributeValue.setValue((Serializable) spinner.getValue());
    }
}
