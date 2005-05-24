/* copyright 2000-2005 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence");
 * you may not use this file except in compliance with the License.
 * $Id$
 */
package ch.bbv.explorer;

import java.util.List;

import ch.bbv.explorer.components.TableCell;

/**
 * The explorer table model models a table displaying a table representation of
 * the children of the selected node. The list of attributes are defined in the
 * table view. If a child has not one of the selected attribute, null is
 * returned.
 * @author Marcel Baumann
 * @version $Revision$
 */
public class NodeTableCell extends TableCell {
  /**
   * List of fields to display for each object member of the table model.
   */
  private List<UiField> fields;

  /**
   * Creates the cell representation of a list of user interface fields.
   * @param fields user interface list to transform. if field list is null or
   *          empty an empty table is created.
   * @return the cell based representation of the list.
   */
  public static TableCell.Cell[] create(List<UiField> fields) {
    Cell[] cells;
    if ((fields != null) && (fields.size() > 0)) {
      cells = new Cell[fields.size()];
      for (int i = 0; i < cells.length; i++) {
        UiField field = fields.get(i);
        cells[i] = new Cell(field.getPropertyName(), field.getPropertyName(),
            field.getType(), field.isEditable());
      }
    } else {
      cells = new Cell[0];
    }
    return cells;

  }

  /**
   * Constructor of the class. All instance fields are initialized.
   * @param fields fields to display of type UiField.
   * @pre (fields != null) && (fields.size() > 0)
   */
  public NodeTableCell(List<UiField> fields) {
    super(create(fields));
    this.fields = fields;
  }

  /**
   * Indicates if the column is editable or not.
   * @param column column to check.
   * @return true if the column is editable otherwise false.
   */
  public boolean isColumnEditable(int column) {
    return fields.get(column).isEditable();
  }

  /**
   * Returns the type of the items displayed in the column.
   * @param column index of the requested column.
   * @return class of the items in the column.
   */
  public Class getColumnClass(int column) {
    return fields.get(column).getType();
  }

  /**
   * Indicates if the object in the column is editable or not.
   * @param object object to test as editable or not.
   * @param column column to check.
   * @return true if the column is editable otherwise false.
   */
  public boolean isEditable(Object object, int column) {
    return fields.get(column).isEditable();
  }

  /**
   * Returns the value at the specified row and column.
   * @param bean the row index.
   * @param column the column index.
   * @return the requested value.
   * @see #setColumnValue(Object, Object, int)
   * @pre (0 <= column) && (column <= fields.size())
   */
  public Object getColumnValue(Object bean, int column) {
    assert ((0 <= column) && (column <= fields.size()));
    if (!(bean instanceof ExplorerNode)) {
      return fields.get(column).getProperty(bean);
    }
    ExplorerNode node = (ExplorerNode) bean;
    return fields.get(column).getProperty(node.getLightweightObject());
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
      fields.get(column).setProperty(bean, value);
      return bean;
    }
    ExplorerNode node = (ExplorerNode) bean;
    node.getExplorer().nodeChanged(node);
    Object object = node.getDataObject();
    fields.get(column).setProperty(object, value);
    return object;
  }

}