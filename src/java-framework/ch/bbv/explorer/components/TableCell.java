/* copyright 2000-2004 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence");
 * you may not use this file except in compliance with the License.
 * $Id$
 */
package ch.bbv.explorer.components;

import java.lang.reflect.InvocationTargetException;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ca.odell.glazedlists.swing.WritableTableFormat;

/**
 * A sortable table model containing a set of properties of Java beans. All
 * beans in the table should contain the defined properties. The order of the
 * column names, property names and editable column flags is relevant.
 * @author Marcel Baumann
 * @version $Revision$
 */
public class TableCell implements WritableTableFormat {
  /**
   * Logger for all instances of the class.
   */
  private static Log log = LogFactory.getLog(TableCell.class);

  /**
   * The name of the displayed columns. The order is the same as the order of
   * the properties.
   */
  private String[] columnNames;

  /**
   * The name of the properties displayed in the columns.
   */
  private String[] propertyNames;

  /**
   * Flag list defining which columns are editable. If null no column is
   * editable. The order is the same as the order of the properties.
   */
  private boolean[] editableColumns;

  /**
   * Constructor of the class. The name of a column is also the name of the
   * property in the Java bean.
   * @param columnNames the list of column names of the tables.
   * @pre columnNames != null
   */
  public TableCell(String[] columnNames) {
    this(columnNames, columnNames, null);
  }

  /**
   * Constructor of the class. The name of a column is also the name of the
   * property in the Java bean.
   * @param columnNames the list of column names of the table.
   * @param propertyNames the list of property names of the table.
   * @pre (columnNames == null) || (propertyNames.length == columnNames.length)
   */
  public TableCell(String[] columnNames, String[] propertyNames) {
    this(columnNames, propertyNames, null);
  }

  /**
   * Constructor of the class. The name of a column is also the name of the
   * property in the Java bean.
   * @param columnNames the list of column names of the table.
   * @param propertyNames the list of property names of the table.
   * @param editableColumns the list of editable columns of the table.
   * @pre (columnNames == null) || (columnNames.length == propertyNames.length)
   * @pre (editableColumns == null) || (editableColumns.length ==
   *      propertyNames.length)
   */
  public TableCell(String[] columnNames, String[] propertyNames,
      boolean[] editableColumns) {
    assert ((columnNames == null) || (columnNames.length == propertyNames.length));
    assert ((editableColumns == null) || (editableColumns.length == propertyNames.length));
    this.columnNames = columnNames;
    this.propertyNames = propertyNames;
    this.editableColumns = editableColumns;
  }

  /**
   * Sets the editable columns of table panel.
   * @param editableColumns the list of editable columns of the table.
   * @pre (editableColumns == null) || (editableColumns.length ==
   *      propertyNames.length)
   */
  public void setEditableColumns(boolean[] editableColumns) {
    assert ((editableColumns == null) || (editableColumns.length == propertyNames.length));
    this.editableColumns = editableColumns;
  }

  /**
   * Returns the number of columns in the table model.
   * @return the column count of the table.
   */
  public int getColumnCount() {
    return columnNames.length;
  }

  /**
   * Returns the name of the specified column.
   * @param column the column index.
   * @return the column name.
   * @pre (0 <= column) && (column <= columnNames.length)
   */
  public String getColumnName(int column) {
    assert ((0 <= column) && (column <= columnNames.length));
    return columnNames[column];
  }

  /**
   * Indicates if the object in the column is editable or not.
   * @param object object to test as editable or not.
   * @param column column to check.
   * @return true if the column is editable otherwise false.
   * @pre (0 <= column) && (column <= columnNames.length)
   */
  public boolean isEditable(Object object, int column) {
    assert ((0 <= column) && (column <= columnNames.length));
    return editableColumns != null && editableColumns[column];
  }

  /**
   * Returns the value at the specified row and column.
   * @param bean the row index.
   * @param column the column index.
   * @return the requested value.
   * @pre (0 <= column) && (column <= columnNames.length) && (bean != null)
   * @see #setColumnValue(Object, Object, int)
   */
  public Object getColumnValue(Object bean, int column) {
    assert ((0 <= column) && (column <= propertyNames.length) && (bean != null));

    Object value = null;
    try {
      return PropertyUtils.getProperty(bean, propertyNames[column]);
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    } catch (InvocationTargetException e) {
      e.printStackTrace();
    } catch (NoSuchMethodException e) {
      e.printStackTrace();
    }
    return value;
  }

  /**
   * Sets the property in the bean associated with the column displayed in the
   * table.
   * @param bean object to update.
   * @param value new value of the field displayed at the given column.
   * @param column column of the field to update.
   * @return the updated object if sucessful otherwise null.
   * @pre (0 <= column) && (column <= columnNames.length) && (bean != null)
   * @see #getColumnValue(Object, int)
   */
  public Object setColumnValue(Object bean, Object value, int column) {
    assert ((0 <= column) && (column <= propertyNames.length) && (bean != null));

    try {
      PropertyUtils.setProperty(bean, propertyNames[column], value);
      return bean;
    } catch (IllegalAccessException e) {
      log.error(e);
    } catch (InvocationTargetException e) {
      log.error(e);
    } catch (NoSuchMethodException e) {
      log.error(e);
    }
    return null;
  }  
}
