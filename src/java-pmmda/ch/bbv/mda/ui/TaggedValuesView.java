/* 
 * copyright 2000-2006 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence);
 * you may not use this file except in compliance with the License.
 * $Id$
 */
package ch.bbv.mda.ui;

import java.awt.BorderLayout;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.util.*;

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
import ch.bbv.mda.*;

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

  /**
   * Map of specialized cell editors with the classes they edit.
   */
  private static Map<Class, TableCellEditor> editors;

  /**
   * Cells defining the columns displayed in the tagged value list.
   */
  private static final TableCell.Cell[] CELLS;

  static {
    editors = new HashMap<Class, TableCellEditor>();
    ReferenceCodeComboBox comboBox = new ReferenceCodeComboBox(
        BooleanCode.class, BooleanCode.getCodes());
    comboBox.setCaseSensitive(false);
    editors.put(Boolean.TYPE, new DefaultCellEditor(comboBox));
    editors.put(Boolean.class, new DefaultCellEditor(comboBox));
    editors.put(String.class, new FocusCellEditor(new JTextField()));
    // used to resolve issue of stop cell editing in case of focus lost.
    registerReferenceCode(VisibilityCode.class, VisibilityCode.getCodes(),
        false, false);
    registerReferenceCode(PropertyModeCode.class, PropertyModeCode.getCodes(),
        false, false);
    CELLS = new TableCell.Cell[2];
    CELLS[0] = new TableCell.Cell("tag", "tag", String.class, false);
    CELLS[1] = new TableCell.Cell("value", "value", String.class, true);
  }

  /**
   * Table cell defining the characteristics of the table.
   */
  private TableCell tableCell;

  /**
   * The panel containing the table.
   */
  private TablePanel tablePanel;

  /**
   * Default constructor of the class. The content of the view is constructed.
   */
  public TaggedValuesView() {
    setLayout(new BorderLayout());
    tableCell = new TableCell(CELLS);
    tablePanel = new TaggedValuePanel(Collections.EMPTY_LIST, tableCell);
    add(tablePanel);
  }

  /**
   * Returns the editor associated with the class of items.
   * @param clazz class which editor should be retrieved.
   * @return the requested table cell editor.
   * @pre clazz != null
   */
  static TableCellEditor getEditor(Class clazz) {
    assert clazz != null;
    TableCellEditor editor = editors.get(clazz);
    return editor;
  }

  /**
   * Registers the reference code with its values so that a combo box is used.
   * @param referenceCode reference code type to display.
   * @param codes the code values of the reference code type.
   * @param caseSensitive flag indicating if the combo box is case sensitive or
   *          not.
   * @param hasEmptyValue flag defining if null value is supported or not.
   * @pre (referenceCode != null) && (codes != null)
   */
  public static void registerReferenceCode(
      Class< ? extends ReferenceCode> referenceCode,
      List< ? extends ReferenceCode> codes, boolean caseSensitive,
      boolean hasEmptyValue) {
    assert (referenceCode != null) && (codes != null);
    ReferenceCodeComboBox comboBox = new ReferenceCodeComboBox(referenceCode,
        codes, hasEmptyValue);
    comboBox.setCaseSensitive(caseSensitive);
    editors.put(referenceCode, new DefaultCellEditor(comboBox));
  }

  @Override
  public void cancelChanges() {
  }

  @Override
  public void commitChanges() {
    MetaElement model = (MetaElement) getDataObject();
    for (TaggedValue newTag : (Collection<TaggedValue>) tablePanel.getModel()) {
      TaggedValue originalTag = model.getTaggedValue(newTag.getTag());
      TagDefinition definition = originalTag.getType();
      String oldValue = originalTag.getValue();
      String newValue = newTag.getValue();
      if (newValue != null) {
        // trims the inputed value. If the value is an empty string and the
        // default value of the tag is null transform the input value to null.
        // Tagged values from over applications do not have a tag definition.
        newValue = newValue.trim();
        if ((definition != null) && (definition.getDefaultValue() == null)
            && (newValue.length() == 0)) {
          newValue = null;
        }
      }
      if (oldValue != null) {
        if (!oldValue.equals(newValue)) {
          originalTag.setValue(newValue);
        }
      } else if (newValue != null) {
        originalTag.setValue(newValue);
      }
    }
  }

  @Override
  public void populateView() {
    MetaElement model = (MetaElement) getDataObject();
    List tags = cloneTaggedValues(model.getTags());
    tablePanel.setModel(tags);
  }

  @Override
  public void setViewEditable(boolean editable) {
    boolean[] editableColumns = { false, editable };
    tableCell.setEditableColumns(editableColumns);
  }

  /**
   * Clones the list of tagged values.
   * @param values tagged values to clones.
   * @return the list of cloned tagged values.
   */
  private List<TaggedValue> cloneTaggedValues(List<TaggedValue> values) {
    List<TaggedValue> copies = new ArrayList<TaggedValue>();
    for (TaggedValue value : values) {
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

  /**
   * The table containing the tagged values to display in the view.
   * @author Marcel Baumann
   * @version $Revision$
   */
  static class TaggedValuesTable extends JTable {
    /**
     * Serial version identifier coded as back compatibility date.
     */
    private static final long serialVersionUID = 20041201;

    /**
     * Table panel used to sort and filter the elements of the table.
     */
    private TablePanel tablePanel;

    /**
     * Constructor of the class.
     * @param model model to display in the table.
     * @param tablePanel the table panel providing sorting and filtering of
     *          rows.
     */
    public TaggedValuesTable(TableModel model, TablePanel tablePanel) {
      super(model);
      this.tablePanel = tablePanel;
    }

    /**
     * Returns the cell editor of the cell located at the given position.
     * @param row row of the cell to edit.
     * @param column column of the cell to edit.
     * @return the requested table cell editor.
     */
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
      editor.cancelCellEditing();
      return editor;
    }

    /**
     * Returns the tool tip assocatied with a tagged value handled in a
     * cartridge.
     * @param event mouse event triggering the tool tip request.
     * @return the requested tool tip text.
     */
    public String getToolTipText(MouseEvent event) {
      String tip = null;
      Point point = event.getPoint();
      int row = rowAtPoint(point);
      int column = convertColumnIndexToModel(columnAtPoint(point));
      if (column == 0) {
        TagDefinition type = ((TaggedValue) tablePanel.getRow(row)).getType();
        if (type != null) {
          ResourcesManager manager = MdaApplication.getApplication()
              .getResourcesManager();
          tip = manager.getString(type.getTag());
        }
      } else {
        tip = super.getToolTipText(event);
      }
      return tip;
    }
  }
}