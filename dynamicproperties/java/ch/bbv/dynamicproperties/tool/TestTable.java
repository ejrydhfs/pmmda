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

package ch.bbv.dynamicproperties.tool;

import java.awt.Dimension;
import java.io.File;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTable;

import ch.bbv.dynamicproperties.PropertyInstanceManager;
import ch.bbv.dynamicproperties.core.ListValue;

public class TestTable {

    private JTable table;

    public TestTable() {
        init();
    }

    public void init() {
        PropertyInstanceManager manager = XMLTools.importAttributeManager(new File(System.getProperty("user.dir")
                + "/test.xml"));

        ListValue value4 = (ListValue) manager.getProperty("Namen").createInstance();
        // value4.addValue((AttributeSimpleValue)
        // manager.getAttribute("Bezahlt").createInstance(), new Boolean(true));
        // value4.addValue((AttributeSimpleValue)
        // manager.getAttribute("Geburtstag").createInstance(), new
        // Date(System.currentTimeMillis()));

        table = new JTable(new TableModel(value4));
        table.setPreferredScrollableViewportSize(new Dimension(500, 70));

        table.setDefaultEditor(Object.class, new CellEditor());

        table.setDefaultRenderer(Object.class, new CellViewer());

        JFrame frame = new JFrame();
        frame.setSize(300, 300);
        JPanel panel = new JPanel();
        panel.add(table.getTableHeader());
        panel.add(table);
        frame.add(panel);
        frame.setVisible(true);

    }

    public static void main(String[] args) {
        new TestTable();
    }
}
