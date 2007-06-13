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

import javax.swing.table.AbstractTableModel;

import ch.bbv.dynamicproperties.core.ListValue;

/**
 * @author UeliKurmann
 */
public class TableModel extends AbstractTableModel {

    private ListValue listValues;

    public TableModel(ListValue listValues) {
        this.listValues = listValues;
    }

    public Class<?> getColumnClass(int arg0) {
        return String.class;
    }

    public int getColumnCount() {
        return 1;
    }

    public String getColumnName(int arg0) {
        return listValues.getName();
    }

    public int getRowCount() {
        return listValues.size();
    }

    public Object getValueAt(int arg0, int arg1) {
        return listValues.get(arg0);
    }

    public boolean isCellEditable(int arg0, int arg1) {
        return true;
    }

    public void addElement() {
        listValues.add(listValues.createListElement());
        this.fireTableRowsInserted(listValues.size() - 1, listValues.size() - 1);
    }
}
