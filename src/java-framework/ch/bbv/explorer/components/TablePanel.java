/* 
 * copyright 2000-2005 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence");
 * you may not use this file except in compliance with the License.
 * $Id$
 */

package ch.bbv.explorer.components;

import java.awt.BorderLayout;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;

import ca.odell.glazedlists.BasicEventList;
import ca.odell.glazedlists.SortedList;
import ca.odell.glazedlists.gui.WritableTableFormat;
import ca.odell.glazedlists.swing.EventTableModel;
import ca.odell.glazedlists.swing.TableComparatorChooser;

/**
 * The table panel provides a panel with a sortable table.
 * @author Marcel Baumann
 * @version $Revision$
 */
public class TablePanel extends JPanel {
  /**
   * Serial version identifier coded as back compatibility date.
   */
  private static final long serialVersionUID = 20041201;

  private TableComparatorChooser chooser;

  /**
   * Sorted list of the collection of objects.
   */
  private SortedList sortedList;

  /**
   * Table model of the collection of objects.
   */
  private EventTableModel tableModel;

  /**
   * The table component used to display the table model.
   */
  private JTable table;

  /**
   * The scroll component containing the table component.
   */
  private JScrollPane scrollPane;

  /**
   * Description of the table structure.
   */
  private WritableTableFormat tableCell;

  /**
   * Constructor of the class. The order of column names and properties names
   * must be the same.
   * @param objects objects to display in the panel.
   * @param tableCell table cell of the table.
   */
  public TablePanel(Collection objects, WritableTableFormat tableCell) {
    this(objects, tableCell, null);
  }

  /**
   * Constructor of the class. The order of column names and properties names
   * must be the same.
   * @param objects objects to display in the panel.
   * @param tableCell table cell of the table.
   * @param comparator comparator used to sort the rows.
   */
  public TablePanel(Collection objects, WritableTableFormat tableCell,
      Comparator comparator) {
    super(new BorderLayout());
    this.tableCell = tableCell;

    BasicEventList list = new BasicEventList();
    list.addAll(objects);
    sortedList = new SortedList(list, comparator);
    tableModel = new EventTableModel(sortedList, tableCell);
    table = createTable(tableModel);
    chooser = new TableComparatorChooser(table, sortedList, false);
    scrollPane = new JScrollPane(table);
    add(scrollPane);
  }

  /**
   * Returns the object located at the given row.
   * @param row row of the object to retrieve.
   * @return the requested object.
   */
  public Object getRow(int row) {
    return sortedList.get(row);
  }

  /**
   * Returns the unmodifiable list of objects displayed in the table.
   * @return the unmodifiable list of objects.
   * @see #setModel(Collection)
   */
  public Collection getModel() {
    return Collections.unmodifiableCollection(sortedList);
  }

  /**
   * Sets the list of objects to display in the table.
   * @param objects new list of objects to display.
   * @see #getModel()
   */
  public void setModel(Collection objects) {
    sortedList.clear();
    sortedList.addAll(objects);
  }

  /**
   * Creates the table view for the table panel. The method can be overwritten
   * to create application specific table components.
   * @param tableModel the table model containing the data for the table view.
   * @return the new table view.
   */
  protected JTable createTable(TableModel tableModel) {
    JTable table = new JTable(tableModel);
    setBooleanMode(table);
    return table;
  }

  /**
   * Returns the table displayed in the pane. The method is available to hook
   * listeners for example.
   * @return the table displayed in the pane.
   */
  public JTable getTable() {
    return table;
  }

  /**
   * Retursn the scroll pane in which the table is displayed. The method is
   * available to hook listeners for example.
   * @return the scroll table displayed in the pane.
   */
  public JScrollPane getScrollPane() {
    return scrollPane;
  }

  /**
   * Returns the sorted list of the panel.
   * @return the sorted list of elements.
   */
  public SortedList getList() {
    return sortedList;
  }

  /**
   * Returns the comparator used to order the items of the table.
   * @return the default comparator to sort the table.
   */
  protected TableComparatorChooser getSelector() {
    return chooser;
  }

  /**
   * Sets the boolean mode for the cells with a boolean type. The boolean value
   * is displayed as a check box.
   * @param table table to update.
   */
  protected void setBooleanMode(JTable table) {
    if (tableCell instanceof TableCell) {
      TableCell.Cell[] cells = ((TableCell) tableCell).getCells();
      for (int i = 0; i < cells.length; i++) {
        if ((cells[i].type == Boolean.class) || (cells[i].type == Boolean.TYPE)) {
          TableColumn tableColumn = table.getColumnModel().getColumn(i);
          tableColumn.setCellRenderer(table.getDefaultRenderer(Boolean.class));
          tableColumn.setCellEditor(table.getDefaultEditor(Boolean.class));
        }
      }
    }
  }
}