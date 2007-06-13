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

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JPanel;

import ch.bbv.dynamicproperties.core.ListValue;
import ch.bbv.dynamicproperties.core.PropertyValue;
import ch.bbv.dynamicproperties.visualization.tool.AttributeListModel;
import ch.bbv.dynamicproperties.visualization.tool.CellRenderer;

/**
 * @author UeliKurmann
 */
public class ListEditor extends DefaultEditor {

    private JList list;

    private JPanel panel;

    private PropertyValue attributeValue;

    public ListEditor(PropertyValue attributeValue) {
        list = new JList();
        list.setCellRenderer(new CellRenderer());
        AttributeListModel model = new AttributeListModel((ListValue) attributeValue);
        this.attributeValue = attributeValue;
        list.setModel(model);

        panel = new JPanel();
        panel.add(list);

        JButton button = new JButton("Add");
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent event) {
                super.mouseClicked(event);

                ((AttributeListModel) list.getModel()).addElement();

            }
        });
        panel.add(button);

    }

    public JComponent getComponent() {
        return panel;
    }

    public Object getValue() {
        return attributeValue;
    }

    public void setValue(Object value) {
        this.attributeValue = (PropertyValue) value;
    }
}