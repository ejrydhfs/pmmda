/* copyright 2000-2004 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence");
 * you may not use this file except in compliance with the License.
 * $Id$
 */
package ch.bbv.explorer;

import java.util.List;

import ca.odell.glazedlists.swing.WritableTableFormat;

/**
 * The explorer table model models a table displaying a table representation of
 * the children of the selected node. The list of attributes are defined in the
 * table view. If a child has not one of the selected attribute, null is
 * returned.
 * @author Marcel Baumann
 * @version $Revision$
 */
public class NodeTableCell implements WritableTableFormat {
  /**
   * List of fields to display for each object member of the table model.
   */
  private List fields;

  /**
   * Constructor of the class. All instance fields are initialized.
   * @param fields fields to display of type UiField.
   */
  public NodeTableCell(List fields) {
    setNodeType(fields);
  }

  /**
   * Sets the node type of the node table cell.
   * @param fields fields to display.
   */
  public void setNodeType(List fields) {
    this.fields = fields;
  }

  /**
   * Returns the name of the column with the given index.
   * @param column index of the requested column.
   * @return name of the column as displayed in the header of the table.
   */
  public String getColumnName(int column) {
    return ((UiField) fields.get(column)).getProperty();
  }

  /**
   * Returns the number of columns in the table model.
   * @return the column count of the table.
   */
  public int getColumnCount() {
    return fields.size();
  }

  /**
   * Indicates if the column is editable or not.
   * @param column column to check.
   * @return true if the column is editable otherwise false.
   */
  public boolean isColumnEditable(int column) {
    return getField(column).isEditable();
  }

  /**
   * Returns the type of the items displayed in the column.
   * @param column index of the requested column.
   * @return class of the items in the column.
   */
  public Class getColumnClass(int column) {
    return ((UiField) fields.get(column)).getType();
  }

  /**
   * Indicates if the object in the column is editable or not.
   * @param object object to test as editable or not.
   * @param column column to check.
   * @return true if the column is editable otherwise false.
   */
  public boolean isEditable(Object object, int column) {
    return ((UiField) fields.get(column)).isEditable();
  }

  /**
   * Returns the value at the specified row and column.
   * @param bean the row index.
   * @param column the column index.
   * @return the requested value.
   * @see #setColumnValue(Object, Object, int)
   */
  public Object getColumnValue(Object bean, int column) {
    assert ((0 <= column) && (column <= fields.size()));
    if (!(bean instanceof ExplorerNode)) {
      return getField(column).getProperty(bean);
    }
    ExplorerNode node = (ExplorerNode) bean;
    return getField(column).getProperty(node.getLightweightObject());
  }

  /**
   * Sets the property in the bean associated with the column displayed in the
   * table.
   * @param bean object to update.
   * @param value new value of the field displayed at the given column.
   * @param column column of the field to update.
   * @return the updated object if sucessful otherwise null.
   * @see #getColumnValue(Object, int)
   */
  public Object setColumnValue(Object bean, Object value, int column) {
    assert ((0 <= column) && (column <= fields.size()));
    if (!(bean instanceof ExplorerNode)) {
      getField(column).setProperty(bean, value);
      return bean;
    }
    ExplorerNode node = (ExplorerNode) bean;
    node.getExplorer().nodeChanged(node);
    Object object = node.getDataObject();
    getField(column).setProperty(object, value);
    return object;
  }

  /**
   * Returns the user interface field at the given column.
   * @param column column of the field.
   * @return the requested user interface field.
   */
  private UiField getField(int column) {
    return (UiField) fields.get(column);
  }
}