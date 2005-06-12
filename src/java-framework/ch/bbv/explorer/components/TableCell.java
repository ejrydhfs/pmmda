/* 
 * copyright 2000-2005 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence");
 * you may not use this file except in compliance with the License.
 * $Id$
 */
package ch.bbv.explorer.components;

import java.lang.reflect.InvocationTargetException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ca.odell.glazedlists.GlazedLists;
import ca.odell.glazedlists.gui.AdvancedTableFormat;
import ca.odell.glazedlists.gui.TableFormat;
import ca.odell.glazedlists.gui.WritableTableFormat;

/**
 * A sortable table model containing a set of properties of Java beans. All
 * beans in the table should contain the defined properties. The order of the
 * column names, property names and editable column flags is relevant.
 * @author Marcel Baumann
 * @version $Revision$
 */
public class TableCell implements TableFormat, WritableTableFormat,
    AdvancedTableFormat {
  /**
   * The cell class defines the characteristic of a table column.
   * @author MarcelBaumann
   * @version $Revision$
   */
  public static class Cell {
    /**
     * Primitive class to object class conversion map, not specified type
     * defaults to string type.
     */
    private static Map<Class, Class> primitiveToType;

    static {
      primitiveToType = new HashMap<Class, Class>();
      primitiveToType.put(boolean.class, Boolean.class);
      primitiveToType.put(char.class, Character.class);
      primitiveToType.put(byte.class, Byte.class);
      primitiveToType.put(short.class, Short.class);
      primitiveToType.put(int.class, Integer.class);
      primitiveToType.put(long.class, Long.class);
      primitiveToType.put(float.class, Float.class);
      primitiveToType.put(double.class, Double.class);
      primitiveToType.put(null, String.class);
    }

    /**
     * The name of the property displayed in the column.
     */
    String property;

    /**
     * The name of the column.
     */
    String column;

    /**
     * The type of the column.
     */
    Class type;

    /**
     * Flag indicating if the column is editable or not.
     */
    boolean editable;

    /**
     * Comparator for the items in the column.
     */
    Comparator comparator;

    /**
     * Constructor of the class.
     * @param property name of the property displayed in the column.
     * @param column name of the column as displayed in the header.
     * @param type type of the values displayed in the column.
     * @param editable flag indicating if the values are editable.
     */
    public Cell(String property, String column, Class type, boolean editable) {
      this.property = property;
      this.column = column;
      this.type = primitiveToType.containsKey(type) ? primitiveToType.get(type)
          : type;
      this.editable = editable;
      if (Comparable.class.isAssignableFrom(this.type)) {
        comparator = GlazedLists.comparableComparator();
      }
    }

    /**
     * Returns the property name.
     * @return the name of the property.
     */
    public String getProperty() {
      return property;
    }

    /**
     * Returns the column header name.
     * @return the name of the column header.
     */
    public String getColumn() {
      return column;
    }

    /**
     * Returns the type of the column. The type is the Java class of the items
     * displayed in a column. If the type is primitive one the class of the
     * associated wrapper is returned.
     * @return the type of the column.
     */
    public Class getType() {
      return type;
    }

    /**
     * Returns the flag defining in the column values are editable.
     * @return the editable flag.
     */
    public boolean isEditable() {
      return editable;
    }
  }

  /**
   * Creates a cell array describing a table with the passed parameters.
   * @param properties list of properties displayed in the table.
   * @param columns list of column header names in the table.
   * @param types list of types for each column in the table.
   * @param editables list of flag indicating if the column is editable.
   * @return the cell array describing the table to construct.
   */
  public static Cell[] create(String[] properties, String[] columns,
      Class[] types, boolean[] editables) {
    assert ((properties != null) && (columns != null) && (properties.length == columns.length));
    Cell[] cells = new Cell[properties.length];
    for (int i = 0; i < cells.length; i++) {
      cells[i] = new Cell(properties[i], columns[i], types[i], editables[i]);
    }
    return cells;
  }

  /**
   * Logger for all instances of the class.
   */
  private static Log log = LogFactory.getLog(TableCell.class);

  /**
   * The description of the displayed columns.
   */
  private Cell[] cells;

  /**
   * Constructor of the class. The name of a column is also the name of the
   * property in the Java bean.
   * @param cells the list of column cells of the tables.
   * @pre columnNames != null
   */
  public TableCell(Cell[] cells) {
    this.cells = cells;
  }

  /**
   * Constructor of the class. The name of a column is also the name of the
   * property in the Java bean.
   * @param propertyNames the list of property names of the table.
   * @pre propertyNames != null
   */
  public TableCell(String[] propertyNames) {
    this(create(propertyNames, propertyNames, new Class[propertyNames.length],
        new boolean[propertyNames.length]));
  }

  /**
   * Sets the editable columns of table panel.
   * @param editables the list of editable columns of the table.
   * @pre (editables == null) || (editables.length == cells.length)
   */
  public void setEditableColumns(boolean[] editables) {
    assert ((editables == null) || (editables.length == cells.length));
    for (int i = 0; i < cells.length; i++) {
      cells[i].editable = editables[i];
    }
  }

  /**
   * Returns the number of columns in the table model.
   * @return the column count of the table.
   */
  public int getColumnCount() {
    return cells.length;
  }

  /**
   * Returns the name of the specified column.
   * @param column the column index.
   * @return the column name.
   * @pre (0 <= column) && (column <= cells.length)
   */
  public String getColumnName(int column) {
    assert ((0 <= column) && (column <= cells.length));
    return cells[column].column;
  }

  /**
   * Indicates if the object in the column is editable or not.
   * @param object object to test as editable or not.
   * @param column column to check.
   * @return true if the column is editable otherwise false.
   * @pre (0 <= column) && (column <= cells.length)
   */
  public boolean isEditable(Object object, int column) {
    assert ((0 <= column) && (column <= cells.length));
    return cells[column].editable;
  }

  /**
   * Returns the value at the specified row and column.
   * @param bean the row index.
   * @param column the column index.
   * @return the requested value.
   * @pre (0 <= column) && (column <= cells.length) && (bean != null)
   * @see #setColumnValue(Object, Object, int)
   */
  public Object getColumnValue(Object bean, int column) {
    assert ((0 <= column) && (column <= cells.length) && (bean != null));
    Object value = null;
    try {
      return PropertyUtils.getProperty(bean, cells[column].property);
    } catch (IllegalAccessException e) {
      log.error(e);
    } catch (InvocationTargetException e) {
      log.error(e);
    } catch (NoSuchMethodException e) {
      log.error(e);
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
   * @pre (0 <= column) && (column <= cells.length) && (bean != null)
   * @see #getColumnValue(Object, int)
   */
  public Object setColumnValue(Object bean, Object value, int column) {
    assert ((0 <= column) && (column <= cells.length) && (bean != null));

    try {
      PropertyUtils.setProperty(bean, cells[column].property, value);
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

  /**
   * Returns the class of the requested column.
   * @param column index of the requested column.
   * @return the class of the items displayed in the column.
   */
  public Class getColumnClass(int column) {
    return cells[column].type;
  }

  /**
   * Returns the comparator of the requested column.
   * @param column index of the requested column.
   * @return the comparator of the items displayed in the column.
   */
  public Comparator getColumnComparator(int column) {
    return cells[column].comparator;
  }

  /**
   * Returns the column description of the table.
   * @return the column descriptions.
   */
  public Cell[] getCells() {
    return cells;
  }
}