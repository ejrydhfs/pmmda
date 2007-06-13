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
import java.util.Date;

import javax.swing.JComponent;

import ch.bbv.dynamicproperties.core.PropertyValue;
import ch.bbv.dynamicproperties.core.SimpleValue;

import com.toedter.calendar.JDateChooser;

/**
 * @author UeliKurmann
 */
@SuppressWarnings("serial")
public class DateEditor extends DefaultEditor implements PropertyChangeListener {

    JDateChooser dateChooser;

    PropertyValue attributeValue;

    public DateEditor(PropertyValue value) {
        this((Date) ((SimpleValue) value).getValue());
        this.attributeValue = value;
    }

    public DateEditor(Date date) {
        dateChooser = new JDateChooser(date);
        dateChooser.setPreferredSize(new Dimension(120, 20));
        dateChooser.addPropertyChangeListener(this);
    }

    public JComponent getComponent() {
        return dateChooser;
    }

    public Object getValue() {
        return dateChooser.getDate();
    }

    public void setValue(Object value) {
        dateChooser.setDate((Date) value);
    }

    public void propertyChange(PropertyChangeEvent event) {
        ((SimpleValue) attributeValue).setValue(dateChooser.getDate());
    }
}
