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

import java.awt.Component;

import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

import ch.bbv.dynamicproperties.core.PropertyValue;
import ch.bbv.dynamicproperties.visualization.IViewer;

@SuppressWarnings("serial")
public class CellViewer implements TableCellRenderer {

    public Component getTableCellRendererComponent(JTable table, Object value, boolean arg2, boolean arg3, int arg4,
            int arg5) {
        PropertyValue attribute = (PropertyValue) value;
        IViewer viewer = attribute.getViewer();

        JComponent component = viewer.getComponent();
        // compute the height of the table cell
        // TODO: more robuste implementation
        int height = (int) (component.getPreferredSize().getHeight() * 1.3);
        if (height <= 8) {
            height = 15;
        }

        table.setRowHeight(height);
        return component;
    }
}
