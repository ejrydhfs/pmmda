/* copyright 2000-2004 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence);
 * you may not use this file except in compliance with the License.
 * $Id$
 */
package ch.bbv.mda.ui;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.swing.DefaultCellEditor;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableModel;

import ch.bbv.dog.reference.BooleanCode;
import ch.bbv.dog.ui.ReferenceCodeComboBox;
import ch.bbv.explorer.NodePropertiesView;
import ch.bbv.explorer.components.TableCell;
import ch.bbv.explorer.components.TablePanel;
import ch.bbv.mda.MetaElement;
import ch.bbv.mda.PropertyModeCode;
import ch.bbv.mda.TaggedValue;
import ch.bbv.mda.TaggedValueType;
import ch.bbv.mda.VisibilityCode;

/**
 * The tagged value view displays a set of tagged values in a table. The first
 * column contains the name of the tag and the second column contains the value
 * of the tag. If no value exists for a tag, the column is empty. The user can
 * edit the value of each displayed tag, but the tag itself is read-only.
 * @author Marcel Baumann
 * @version $Revision$
 */
public class TaggedValuesView extends NodePropertiesView {
  private static Map editors;

  static {
    editors = new HashMap();

    ReferenceCodeComboBox comboBox = new ReferenceCodeComboBox(
        BooleanCode.class, BooleanCode.getCodes());
    comboBox.setCaseSensitive(false);
    editors.put(Boolean.TYPE, new DefaultCellEditor(comboBox));
    editors.put(Boolean.class, new DefaultCellEditor(comboBox));
    editors.put(String.class, new MyEditor(new JTextField())); 
    // used to resolve issue of stop cell editing in case of focus lost.
    registerReferenceCode(VisibilityCode.class, VisibilityCode.getCodes(),
        false);
    registerReferenceCode(PropertyModeCode.class, PropertyModeCode.getCodes(),
        false);
  }

  private TableCell tableCell;

  private TablePanel tablePanel;

  private static final String[] COLUMN_NAMES = { "tag", "value" };

  private static final String[] PROPERTY_NAMES = { "tag", "value" };

  /**
   * Default constructor of the class. The content of the view is constructed.
   */
  public TaggedValuesView() {
    setLayout(new BorderLayout());
    tableCell = new TableCell(COLUMN_NAMES, PROPERTY_NAMES);
    tablePanel = new TaggedValuePanel(Collections.EMPTY_LIST, tableCell);
    add(tablePanel);
  }

  static TableCellEditor getEditor(Class clazz) {
    return (TableCellEditor) editors.get(clazz);
  }

  public static void registerReferenceCode(Class referenceCode, List codes,
      boolean caseSensitive) {
    ReferenceCodeComboBox comboBox = new ReferenceCodeComboBox(referenceCode,
        codes);
    comboBox.setCaseSensitive(caseSensitive);
    editors.put(referenceCode, new DefaultCellEditor(comboBox));
  }

  public void cancelChanges() {
  }

  public void commitChanges() {
    MetaElement model = (MetaElement) getDataObject();
    for (Iterator i = tablePanel.getModel().iterator(); i.hasNext();) {
      TaggedValue newTag = (TaggedValue) i.next();
      TaggedValue originalTag = model.getTaggedValue(newTag.getTag());
      String value = originalTag.getValue();
      if (value != null) {
        if (!value.equals(newTag.getValue())) {
          originalTag.setValue(newTag.getValue());
        }
      } else {
        value = newTag.getValue();
        if (value != null) {
          originalTag.setValue(newTag.getValue());
        }
      }
    }
  }

  public void populateView() {
    MetaElement model = (MetaElement) getDataObject();
    List tags = cloneTaggedValues(model.getTags());
    tablePanel.setModel(tags);
  }

  public void setViewEditable(boolean editable) {
    boolean[] editableColumns = { false, editable };
    tableCell.setEditableColumns(editableColumns);
  }

  private List cloneTaggedValues(List taggedValues) {
    List copies = new ArrayList();
    for (Iterator i = taggedValues.iterator(); i.hasNext();) {
      copies.add(new TaggedValue((TaggedValue) i.next()));
    }
    return copies;
  }
  
  static class TaggedValuePanel extends TablePanel {
    public TaggedValuePanel(Collection objects, TableCell tableCell) {
      super(objects, tableCell, new TaggedValue.TaggedValueComparator());
    }

    protected JTable createTable(TableModel tableModel) {
      return new TaggedValuesTable(tableModel, this);
    }
  }

  static class TaggedValuesTable extends JTable {
    private TablePanel tablePanel;

    public TaggedValuesTable(TableModel model, TablePanel tablePanel) {
      super(model);
      this.tablePanel = tablePanel;
      TableCellEditor tce = new MyEditor(new JTextField());
      for(Enumeration i = this.defaultEditorsByColumnClass.keys(); i.hasMoreElements();){
        this.setDefaultEditor(i.nextElement().getClass(), tce);
      }
    }

    public TableCellEditor getCellEditor(int row, int column) {
      TaggedValue taggedValue = (TaggedValue) tablePanel.getRow(row);
      TableCellEditor editor = null;
      if (taggedValue != null) {
        TaggedValueType type = taggedValue.getType();
        if (type != null) {
          editor = getEditor(type.getType());
        }
      }
      if (editor == null) {
        editor = super.getCellEditor(row, column);
      }
      return editor;
    }
  }
}