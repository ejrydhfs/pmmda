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

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import ch.bbv.dynamicproperties.core.ListValue;
import ch.bbv.dynamicproperties.core.PropertyValue;
import ch.bbv.dynamicproperties.tool.CellEditor;
import ch.bbv.dynamicproperties.tool.CellViewer;
import ch.bbv.dynamicproperties.tool.TableModel;

/**
 * @author UeliKurmann
 */
public class TableEditor extends DefaultEditor {

    private JTable table;

    private JPanel panel;

    JScrollPane scrollPane;

    private ListValue attributeValue;

    private TableModel tableModel;

    public TableEditor(PropertyValue attributeValue) {
        this.attributeValue = (ListValue) attributeValue;

        panel = new JPanel();

        tableModel = new TableModel(this.attributeValue);

        table = new JTable();
        table.setModel(tableModel);
        table.setDefaultEditor(Object.class, new CellEditor());
        table.setDefaultRenderer(Object.class, new CellViewer());
        table.add(createPopupMenu());

        scrollPane = new JScrollPane(table);
        scrollPane.setAutoscrolls(true);
        scrollPane.setPreferredSize(new Dimension(100, 100));

        panel.setLayout(new BorderLayout());
        panel.add(scrollPane, BorderLayout.NORTH);

        JButton button = new JButton("Add");
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent event) {
                tableModel.addElement();
            }
        });
        panel.add(button, BorderLayout.SOUTH);
    }

    public JComponent getComponent() {
        return panel;
    }

    public Object getValue() {
        return null;
    }

    public void setValue(Object value) {
        // TODO
    }

    private JPopupMenu createPopupMenu() {
        JPopupMenu popUpMenu = new JPopupMenu();
        JMenu menu = new JMenu("delete");
        popUpMenu.add(menu);
        return popUpMenu;
    }
}
