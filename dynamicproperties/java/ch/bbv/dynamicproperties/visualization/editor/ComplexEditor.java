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

import java.awt.GridBagLayout;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;

import ch.bbv.dynamicproperties.core.ComplexValue;
import ch.bbv.dynamicproperties.core.PropertyValue;
import ch.bbv.dynamicproperties.visualization.tool.SwingTools;

/**
 * @author UeliKurmann
 */
public class ComplexEditor extends DefaultEditor {

    private JPanel panel;

    private ComplexValue attributeValue;

    public ComplexEditor(PropertyValue attributeValue) {
        this.attributeValue = (ComplexValue) attributeValue;
        panel = new JPanel();
        init();
    }

    private void init() {
        panel.setLayout(new GridBagLayout());

        panel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder(attributeValue.getName()),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));

        panel.add(SwingTools.createEditorPanel(attributeValue.getProperties()));
        panel.repaint();
    }

    public JComponent getComponent() {
        return panel;
    }

    public Object getValue() {
        // TODO Auto-generated method stub
        return null;
    }

    public void setValue(Object value) {
        // TODO Auto-generated method stub

    }
}