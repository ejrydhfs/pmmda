/* copyright 2000-2005 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence");
 * you may not use this file except in compliance with the License.
 * $Id$
 */

package ch.bbv.mda.reports;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import ch.bbv.dog.DataObject;
import ch.bbv.dog.DoFunctor;
import ch.bbv.mda.MetaClass;
import ch.bbv.mda.MetaElement;
import ch.bbv.mda.MetaIndexedProperty;
import ch.bbv.mda.MetaModel;
import ch.bbv.mda.MetaPackage;
import ch.bbv.mda.MetaProperty;
import ch.bbv.utilities.ReflectUtilities;

/**
 * The model table model provides a table model of a meta model. All attributes
 * of the tree of elements are accessible through the table model interface.
 * @author MarcelBaumann
 * @version $Revision$
 */
public class ModelTableModel extends AbstractTableModel {
  /**
   * Serial version identifier coded as back compatibility date.
   */
  private static final long serialVersionUID = 20041201;

  private static final List<PropertyType> ROWS;

  static {
    ROWS = new ArrayList<PropertyType>();
    ROWS.add(new PropertyType("model.name", "name", MetaModel.class));
    ROWS.add(new PropertyType("model.description", "description",
        MetaModel.class));

    ROWS.add(new PropertyType("package.name", "name", MetaPackage.class));
    ROWS.add(new PropertyType("package.description", "description",
        MetaPackage.class));

    ROWS.add(new PropertyType("class.name", "name", MetaClass.class));
    ROWS.add(new PropertyType("class.description", "description",
        MetaClass.class));

    ROWS.add(new PropertyType("property.name", "name", MetaProperty.class));
    ROWS.add(new PropertyType("property.description", "description",
        MetaProperty.class));

    ROWS.add(new PropertyType("indexedPropery.name", "name",
        MetaIndexedProperty.class));
    ROWS.add(new PropertyType("indexedPropery.description", "description",
        MetaIndexedProperty.class));
  }

  static class ListFunctor implements DoFunctor {
    private List<DataObject> items;

    public ListFunctor() {
      items = new ArrayList<DataObject>();
    }

    public void execute(DataObject object) {
      items.add(object);
    }

    public List getItems() {
      return items;
    }
  }

  static abstract class AttributeType {
    String columnName;

    String property;

    Class clazz;

    public AttributeType(String columnName, String property, Class clazz) {
      this.columnName = columnName;
      this.property = property;
      this.clazz = clazz;
    }

    public String getColumnName() {
      return columnName;
    }

    public String getProperty() {
      return property;
    }

    public Object getValue(MetaElement item) {
      MetaElement bean = item;
      while ((bean != null) && (bean.getClass() != clazz)) {
        bean = bean.getContext();
      }
      Object value = null;
      if (bean != null) {
        value = retrieveValue(bean);
      }
      return value;
    }

    public abstract Object retrieveValue(MetaElement item);
  }

  static class PropertyType extends AttributeType {
    public PropertyType(String columnName, String property, Class clazz) {
      super(columnName, property, clazz);
    }

    public Object retrieveValue(MetaElement item) {
      return ReflectUtilities.getProperty(item, property);
    }
  }

  static class TagType extends AttributeType {
    public TagType(String columnName, String property, Class clazz) {
      super(columnName, property, clazz);
    }

    public Object retrieveValue(MetaElement item) {
      return item.getTaggedValue(property);
    }
  }

  private MetaModel model;

  private List rows;

  private List columns;

  public int getColumnCount() {
    return columns.size();
  }

  public int getRowCount() {
    return rows.size();
  }

  public String getColumnName(int column) {
    return ((AttributeType) columns.get(column)).getColumnName();
  }

  public Object getValueAt(int row, int column) {
    return ((AttributeType) columns.get(column)).getValue((MetaElement) rows
        .get(row));
  }
}