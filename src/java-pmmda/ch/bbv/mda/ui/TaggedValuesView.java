/* 
 * copyright 2000-2005 Marcel Baumann
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
import java.util.List;
import java.util.Map;

import javax.swing.CellEditor;
import javax.swing.DefaultCellEditor;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableModel;

import ch.bbv.dog.reference.BooleanCode;
import ch.bbv.dog.reference.ReferenceCode;
import ch.bbv.dog.ui.ReferenceCodeComboBox;
import ch.bbv.explorer.NodePropertiesView;
import ch.bbv.explorer.components.FocusCellEditor;
import ch.bbv.explorer.components.TableCell;
import ch.bbv.explorer.components.TablePanel;
import ch.bbv.mda.MetaElement;
import ch.bbv.mda.PropertyModeCode;
import ch.bbv.mda.TagDefinition;
import ch.bbv.mda.TaggedValue;
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
  /**
   * Serial version identifier coded as back compatibility date.
   */
  private static final long serialVersionUID = 20041201;

  private static Map<Class, CellEditor> editors;

  static {
    editors = new HashMap<Class, CellEditor>();

    ReferenceCodeComboBox comboBox = new ReferenceCodeComboBox(
        BooleanCode.class, BooleanCode.getCodes());
    comboBox.setCaseSensitive(false);
    editors.put(Boolean.TYPE, new DefaultCellEditor(comboBox));
    editors.put(Boolean.class, new DefaultCellEditor(comboBox));
    editors.put(String.class, new FocusCellEditor(new JTextField()));
    // used to resolve issue of stop cell editing in case of focus lost.
    registerReferenceCode(VisibilityCode.class, VisibilityCode.getCodes(),
        false);
    registerReferenceCode(PropertyModeCode.class, PropertyModeCode.getCodes(),
        false);
  }

  private TableCell tableCell;

  private TablePanel tablePanel;

  private static final TableCell.Cell[] CELLS;

  static {
    CELLS = new TableCell.Cell[2];
    CELLS[0] = new TableCell.Cell("tag", "tag", String.class, false);
    CELLS[1] = new TableCell.Cell("value", "value", String.class, true);
  }

  /**
   * Default constructor of the class. The content of the view is constructed.
   */
  public TaggedValuesView() {
    setLayout(new BorderLayout());
    tableCell = new TableCell(CELLS);
    tablePanel = new TaggedValuePanel(Collections.EMPTY_LIST, tableCell);
    add(tablePanel);
  }

  static TableCellEditor getEditor(Class clazz) {
    return (TableCellEditor) editors.get(clazz);
  }

  public static void registerReferenceCode(
      Class< ? extends ReferenceCode> referenceCode,
      List< ? extends ReferenceCode> codes, boolean caseSensitive) {
    ReferenceCodeComboBox comboBox = new ReferenceCodeComboBox(referenceCode,
        codes);
    comboBox.setCaseSensitive(caseSensitive);
    editors.put(referenceCode, new DefaultCellEditor(comboBox));
  }

  public void cancelChanges() {
  }

  public void commitChanges() {
    MetaElement model = (MetaElement) getDataObject();
    for (TaggedValue newTag : (Collection<TaggedValue>) tablePanel.getModel()) {
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

  private List<TaggedValue> cloneTaggedValues(List<TaggedValue> taggedValues) {
    List<TaggedValue> copies = new ArrayList<TaggedValue>();
    for (TaggedValue value : taggedValues) {
      copies.add(new TaggedValue(value));
    }
    return copies;
  }

  static class TaggedValuePanel extends TablePanel {
    /**
     * Serial version identifier coded as back compatibility date.
     */
    private static final long serialVersionUID = 20041201;

    public TaggedValuePanel(Collection objects, TableCell tableCell) {
      super(objects, tableCell, new TaggedValue.TaggedValueComparator());
    }

    protected JTable createTable(TableModel tableModel) {
      return new TaggedValuesTable(tableModel, this);
    }
  }

  static class TaggedValuesTable extends JTable {
    /**
     * Serial version identifier coded as back compatibility date.
     */
    private static final long serialVersionUID = 20041201;

    private TablePanel tablePanel;

    public TaggedValuesTable(TableModel model, TablePanel tablePanel) {
      super(model);
      this.tablePanel = tablePanel;
      TableCellEditor tce = new FocusCellEditor(new JTextField());
      for (Enumeration i = this.defaultEditorsByColumnClass.keys(); i
          .hasMoreElements();) {
        this.setDefaultEditor(i.nextElement().getClass(), tce);
      }
    }

    public TableCellEditor getCellEditor(int row, int column) {
      TaggedValue taggedValue = (TaggedValue) tablePanel.getRow(row);
      TableCellEditor editor = null;
      if (taggedValue != null) {
        TagDefinition type = taggedValue.getType();
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