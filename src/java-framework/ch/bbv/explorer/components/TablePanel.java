/* copyright 2000-2004 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence);
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
import javax.swing.table.TableModel;

import ca.odell.glazedlists.BasicEventList;
import ca.odell.glazedlists.SortedList;
import ca.odell.glazedlists.swing.EventTableModel;
import ca.odell.glazedlists.swing.TableComparatorChooser;
import ca.odell.glazedlists.swing.WritableTableFormat;

/**
 * The table panel provides a panel with a sortable table. The columns of the
 * table are specified as an array of strings.
 * @author MarcelBaumann
 * @version $Revision$
 */
public class TablePanel extends JPanel {
  private TableComparatorChooser chooser;

  /**
   * Sorted list of the collection of objects.
   */
  private SortedList sortedList;

  /**
   * table model of the collection of objects.
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
    setLayout(new BorderLayout());

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
    return new JTable(tableModel);
  }

  /**
   * Returns the comparator used to order the items of the table.
   * @return the default comparator to sort the table.
   */
  protected TableComparatorChooser getSelector() {
    return chooser;
  }
}