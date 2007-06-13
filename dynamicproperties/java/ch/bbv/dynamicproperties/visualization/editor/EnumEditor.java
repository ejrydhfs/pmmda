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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;
import javax.swing.JComponent;

import ch.bbv.dynamicproperties.core.EnumerationElement;
import ch.bbv.dynamicproperties.core.EnumerationType;
import ch.bbv.dynamicproperties.core.EnumerationValue;
import ch.bbv.dynamicproperties.core.PropertyValue;

/**
 * @author Ueli Kurmann
 */
public class EnumEditor extends DefaultEditor implements ActionListener {

    private JComboBox dropDownList;

    private EnumerationValue enumValue;

    public EnumEditor(PropertyValue enumerationValue) {
        enumValue = (EnumerationValue) enumerationValue;
        EnumerationType enumType = (EnumerationType) enumValue.getProperty().getType();
        dropDownList = new JComboBox(enumType.getEnumerations().toArray(new EnumerationElement[0]));
        dropDownList.addActionListener(this);
    }

    public JComponent getComponent() {
        return dropDownList;
    }

    public Object getValue() {
        return null;
    }

    public void setValue(Object value) {

    }

    public void actionPerformed(ActionEvent event) {
        EnumerationElement element = (EnumerationElement) dropDownList.getSelectedItem();
        enumValue.setValue(element);
    }

}
