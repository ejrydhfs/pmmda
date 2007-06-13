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
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JComponent;
import javax.swing.JTextField;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;

import ch.bbv.dynamicproperties.core.PropertyValue;
import ch.bbv.dynamicproperties.core.SimpleValue;

/**
 * @author UeliKurmann
 */
@SuppressWarnings("serial")
public class StringEditor extends DefaultEditor implements PropertyChangeListener, CaretListener {

    private JTextField textField;

    private SimpleValue attributeValue;

    public StringEditor(PropertyValue value) {
        this.attributeValue = (SimpleValue) value;
        textField = new JTextField();

        if (attributeValue.getValue() != null) {
            textField.setText((String) this.attributeValue.getValue().toString());
        }

        textField.setPreferredSize(new Dimension(180, 20));
        textField.setEditable(true);
        textField.addPropertyChangeListener(this);
        textField.addCaretListener(this);
    }

    public JComponent getComponent() {
        return textField;
    }

    public Object getValue() {
        return textField.getText();
    }

    public void setValue(Object value) {
        textField.setText((String) value);
    }

    public void propertyChange(PropertyChangeEvent evt) {
        attributeValue.setValue(textField.getText());
    }

    public void caretUpdate(CaretEvent arg0) {
        attributeValue.setValue(textField.getText());
    }
}
