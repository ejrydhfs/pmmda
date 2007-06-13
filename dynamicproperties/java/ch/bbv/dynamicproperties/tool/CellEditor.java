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

import javax.swing.AbstractCellEditor;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;

import ch.bbv.dynamicproperties.core.PropertyValue;
import ch.bbv.dynamicproperties.visualization.IEditor;

/**
 * @author UeliKurmann
 */
@SuppressWarnings("serial")
public class CellEditor extends AbstractCellEditor implements TableCellEditor {

    private IEditor editor;

    public JComponent getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int rowIndex,
            int colIndex) {

        PropertyValue attribute = (PropertyValue) value;
        editor = attribute.getEditor();

        return editor.getComponent();
    }

    public Object getCellEditorValue() {
        return editor.getValue();
    }
}
