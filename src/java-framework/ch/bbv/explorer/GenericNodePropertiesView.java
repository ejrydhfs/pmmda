/* copyright 2000-2004 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence");
 * you may not use this file except in compliance with the License.
 * $Id$
 */
package ch.bbv.explorer;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.IndexedPropertyDescriptor;
import java.beans.PropertyDescriptor;
import java.util.*;

import javax.swing.*;

import org.apache.commons.beanutils.MappedPropertyDescriptor;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ch.bbv.dog.DataObject;
import ch.bbv.dog.DataObjectType;
import ch.bbv.utilities.ReflectUtilities;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.toedter.calendar.JDateChooser;

/**
 * The generic node properties view displays all the properties of a data object
 * type. Their values can be edited. The properties are inferred through
 * reflection. The view handles simple, indexed and mapped properties. The view
 * has knowledge how to handle strings, natural numbers, decimal numbers,
 * booleans and dates. The following conventions exist.
 * <ul>
 * <li>String properties are displayed as text lines.</li>
 * <li>Natural numbers and decimal numbers are displayed as a text input field
 * with a format filter to allow only legal values. Overflows are only detected
 * during the internal conversion and not during user input.</li>
 * <li>Boolean properties are displayed as check boxes.</li>
 * <li>Date are displayed with a custom calendar components.</li>
 * </ul>
 * The formatted text field features are used to check if the inputed data have
 * the expected format. The class is well behaved in case the bean is null.
 * @author MarcelBaumann
 * @version $Revision$
 */
public class GenericNodePropertiesView extends NodePropertiesView {
  /**
   * Logger for all instances of the class.
   */
  private static Log log = LogFactory.getLog(GenericNodePropertiesView.class);

  /**
   * List of all views for simple properties.
   */
  private List primitivePropertyViews;

  /**
   * List of all views for simple data object properties.
   */
  private List dataPropertyViews;

  /**
   * List of all views for indexed properties.
   */
  private List indexedPropertyViews;

  /**
   * List of all views for mapped properties.
   */
  private List mappedPropertyViews;

  /**
   * Class of the instance displayed in the generic properties view.
   */
  private Class type;

  /**
   * Default constructor of the class.
   */
  public GenericNodePropertiesView() {
    primitivePropertyViews = new ArrayList();
    dataPropertyViews = new ArrayList();
    indexedPropertyViews = new ArrayList();
    mappedPropertyViews = new ArrayList();
  }

  public void populateView() {
    DataObject bean = getModel().getDataObject();
    if (type == null) {
      if (log.isDebugEnabled()) {
        log.debug("Creating generic node properties view " + type.getName());
      }
      type = bean.getClass();
      retrieveProperties(bean);
      initialize();
    }

    for (Iterator i = primitivePropertyViews.iterator(); i.hasNext();) {
      ((PropertyView) i.next()).setModel(bean);
    }
    for (Iterator i = indexedPropertyViews.iterator(); i.hasNext();) {
      ((PropertyView) i.next()).setModel(bean);
    }
    for (Iterator i = mappedPropertyViews.iterator(); i.hasNext();) {
      ((PropertyView) i.next()).setModel(bean);
    }
  }

  public void commitChanges() {
    for (Iterator i = primitivePropertyViews.iterator(); i.hasNext();) {
      ((PropertyView) i.next()).update();
    }
  }

  public void cancelChanges() {
    // no operations
  }

  public void setViewEditable(boolean editable) {
    for (Iterator i = primitivePropertyViews.iterator(); i.hasNext();) {
      ((PropertyView) i.next()).setEditable(editable);
    }
  }

  protected void initialize() {
    StringBuffer buffer = new StringBuffer();
    generateRowsLayout(buffer, primitivePropertyViews);
    generateRowsLayout(buffer, dataPropertyViews);
    generateRowsLayout(buffer, indexedPropertyViews);
    generateRowsLayout(buffer, mappedPropertyViews);

    FormLayout layout = new FormLayout("right:pref, 3dlu, pref, 12dlu, pref",
        buffer.toString());

    PanelBuilder builder = new PanelBuilder(layout);
    builder.setDefaultDialogBorder();

    int row = generateRows(builder, "Simple Properties",
        primitivePropertyViews, 1);
    row = generateRows(builder, "Data Objects", dataPropertyViews, row);
    row = generateRows(builder, "Indexed Properties", indexedPropertyViews, row);
    row = generateRows(builder, "Mapped Properties", mappedPropertyViews, row);

    JPanel panel = builder.getPanel();
    add(panel);
  }

  /**
   * Generates the row layout for the list of properties.
   * @param buffer buffer in which the layout is written.
   * @param properties the properties which layout should be generated.
   */
  protected void generateRowsLayout(StringBuffer buffer, List properties) {
    int size = properties.size();
    if (size > 0) {
      if (buffer.length() > 0) {
        buffer.append(", 6dlu, p, 3dlu");
      } else {
        buffer.append("p, 3dlu");
      }
      for (int i = 0; i < (size - 1); i++) {
        buffer.append(", p, 3dlu");
      }
      buffer.append(", p");
    }
  }

  protected int generateRows(PanelBuilder builder, String seperator,
      List properties, int row) {
    int size = properties.size();
    int currentRow = row;
    if (size > 0) {
      CellConstraints cc = new CellConstraints();
      builder.addSeparator(seperator, cc.xywh(1, currentRow, 5, 1));
      currentRow += 2;
      for (int i = 0; i < size; i++) {
        PropertyView view = (PropertyView) properties.get(i);
        builder.addLabel(view.label, cc.xy(1, currentRow));
        builder.add(view.getComponent(), cc.xy(3, currentRow));
        currentRow += 2;
      }
    }
    return currentRow;
  }

  /**
   * Retrieves the properties of the data object. The following rules are
   * defined.
   * <ul>
   * <li>Indexed property descriptors are only added if the type of their
   * elements is not a data object type. Indexed properties with data objects
   * are always displayed in the navigator.</li>
   * <li>Mapped property descriptors are only added if the type of their
   * elements is not a data object type. Mapped properties with data objects are
   * always displayed in the navigator.</li>
   * <li>Simple properties are displayed if they contains at most one item or
   * if their elements are not data objects.</li>
   * </ul>
   * @param object data object which properties should be infered.
   */
  protected void retrieveProperties(DataObject object) {
    type = object.getClass();

    PropertyDescriptor[] descriptors = PropertyUtils
        .getPropertyDescriptors(object);
    for (int i = 0; i < descriptors.length; i++) {
      PropertyDescriptor descriptor = descriptors[i];
      if (descriptor instanceof IndexedPropertyDescriptor) {
        continue;
      } else if (descriptor instanceof MappedPropertyDescriptor) {
        MappedPropertyView propertyView = new MappedPropertyView(descriptor
            .getName(), descriptor.getPropertyType(), descriptor
            .getDisplayName());
        mappedPropertyViews.add(propertyView);
      } else {
        if (Arrays.binarySearch(DataObject.SYSTEM_SIMPLE_PROPERTIES, descriptor
            .getName()) < 0) {
          Class clazz = descriptor.getPropertyType();
          if (containsInterface(clazz.getInterfaces(),
              java.util.Collection.class)
              || containsInterface(clazz.getInterfaces(), java.util.Map.class)) {
            // handle indexed or mapped properties with multiple items.
            if (getNode().getNodeType().getRelation(descriptor.getName()) == null) {
              IndexedPropertyView propertyView = new IndexedPropertyView(
                  descriptor.getName(), descriptor.getPropertyType(),
                  descriptor.getDisplayName());
              indexedPropertyViews.add(propertyView);
            }
          } else {
            // handle simple properties with at most one item.
            PropertyView view;
            if (containsInterface(clazz.getInterfaces(), DataObject.class)) {
              DataObjectType type = getModel().getNodeType().getFactory()
                  .getNodeTypeForBo(clazz).getDataObjectType();
              view = new DataObjectPropertyView(descriptor.getName(), clazz,
                  descriptor.getDisplayName(), type);
              dataPropertyViews.add(view);
            } else {
              view = new PrimitivePropertyView(descriptor.getName(), clazz,
                  descriptor.getDisplayName());
              primitivePropertyViews.add(view);
            }
          }
        }
      }
    }
  }

  protected boolean containsInterface(Class[] interfaces, Class clazz) {
    boolean containsInterface = false;
    for (int i = 0; i < interfaces.length; i++) {
      if (clazz == interfaces[i]) {
        containsInterface = true;
        break;
      }
    }
    return containsInterface;
  }

  /**
   * The abstract property view defines the interface for all property views. A
   * property view displays a property of a bean on the user interface.
   * @author MarcelBaumann
   * @version $Revision$
   */
  static abstract class PropertyView {
    protected static final int TEXT_FIELD_SIZE = 32;

    /**
     * The bean which property is displayed.
     */
    DataObject bean;

    /**
     * Name of the property displayed in the view.
     */
    String property;

    /**
     * Label of the property in the view.
     */
    String label;

    /**
     * Type of the property.
     */
    Class type;

    /**
     * Constructor of the property view.
     * @param property property name to be displayed in the view.
     * @param type type of the simple property.
     * @param label label of the simple property.
     */
    public PropertyView(String property, Class type, String label) {
      this.property = property;
      this.label = label;
      this.type = type;
    }

    /**
     * Returns the component used to manipulate the property.
     * @return the visual component displaying the property.
     */
    public abstract JComponent getComponent();

    /**
     * Sets the visual component to the given editable mode.
     * @param editable new value of the editable flag.
     */
    public abstract void setEditable(boolean editable);

    /**
     * Sets the model of the view to the new data object. The view is
     * automatically updated.
     * @param bean bean which property should be displayed.
     */
    public void setModel(DataObject bean) {
      this.bean = bean;
      Object value;
      if (bean != null) {
        value = ReflectUtilities.getProperty(bean, property);
      } else {
        value = null;
      }
      setValue(value);
    }

    /**
     * Updates the bean with the value edited in the view. The update is only
     * performed if the value is different from the one stored in the bean.
     */
    public void update() {
      Object oldValue = ReflectUtilities.getProperty(bean, property);
      Object value = getValue();
      if (((oldValue != null) && !oldValue.equals(value))
          || ((oldValue == null) && (value != null))) {
        ReflectUtilities.setProperty(bean, property, value);
      }
    }

    /**
     * Returns the value of the displayed property. The type is the one of the
     * property.
     * @return the value of the displayed property.
     */
    protected abstract Object getValue();

    /**
     * Sets the value of the displayed property.
     * @param value value of the property.
     */
    protected abstract void setValue(Object value);
  }

  /**
   * The simple property view displays a simple property with a language
   * build-in type, meaning not a data object type.
   * @author MarcelBaumann
   * @version $Revision$
   */
  class PrimitivePropertyView extends PropertyView {
    private JTextField text;

    private JFormattedTextField formattedText;

    private JDateChooser dateChooser;

    private JCheckBox checkBox;

    public PrimitivePropertyView(String property, Class type, String label) {
      super(property, type, label);
      if (type.equals(String.class)) {
        text = new JTextField(TEXT_FIELD_SIZE);
      } else if (Boolean.class.equals(type) || Boolean.TYPE.equals(type)) {
        checkBox = new JCheckBox();
      } else if (Date.class.equals(type)) {
        dateChooser = new JDateChooser(true);
      } else {
        formattedText = new JFormattedTextField();
      }
    }

    public JComponent getComponent() {
      JComponent component = null;
      if (text != null) {
        component = text;
      } else if (dateChooser != null) {
        component = dateChooser;
      } else if (checkBox != null) {
        component = checkBox;
      } else if (formattedText != null) {
        component = formattedText;
      }
      return component;
    }

    public void setEditable(boolean editable) {
      if (text != null) {
        text.setEditable(editable);
      } else if (dateChooser != null) {
        dateChooser.setEnabled(editable);
      } else if (checkBox != null) {
        checkBox.setEnabled(editable);
      } else if (formattedText != null) {
        formattedText.setEditable(editable);
      }
    }

    protected Object getValue() {
      Object value = null;
      if (type.equals(String.class)) {
        value = text.getText();
      } else if (Boolean.class.equals(type) || Boolean.TYPE.equals(type)) {
        value = new Boolean(checkBox.isSelected());
      } else if (Date.class.equals(type)) {
        value = dateChooser.getDate();
      } else if (Short.TYPE.equals(type) || Short.class.equals(type)) {
        value = Short.decode(text.getText());
      } else if (Integer.TYPE.equals(type) || Integer.class.equals(type)) {
        value = Integer.decode(text.getText());
      } else if (Long.TYPE.equals(type) || Long.class.equals(type)) {
        value = Long.decode(text.getText());
      } else if (Float.TYPE.equals(type) || Float.class.equals(type)) {
        value = Float.valueOf(text.getText());
      } else if (Double.TYPE.equals(type) || Double.class.equals(type)) {
        value = Double.valueOf(text.getText());
      }
      return value;
    }

    protected void setValue(Object value) {
      if (type.equals(String.class)) {
        text.setText((String) value);
      } else if (Boolean.class.equals(type) || Boolean.TYPE.equals(type)) {
        boolean booleanValue = value != null ? ((Boolean) value).booleanValue()
            : false;
        checkBox.setSelected(booleanValue);
      } else if (Date.class.equals(type)) {
        if (value != null) {
          dateChooser.setDate((Date) value);
        } else {
          dateChooser = new JDateChooser(true);
        }
      } else {
        formattedText.setValue(value);
      }
    }
  }

  /**
   * The data object property view displays a data object property . The object
   * is cloned to allow the user to cancel the operation once it was edited in a
   * child dialog.
   * @author MarcelBaumann
   * @version $Revision$
   */
  class DataObjectPropertyView extends PropertyView {
    private JComponent propertyArea;

    private DataObjectType type;

    private DataObject dataObject;

    private DataObject copy;

    private ButtonActions actions;

    JButton deleteButton;

    JButton editButton;

    public DataObjectPropertyView(String property, Class clazz, String label,
        DataObjectType type) {
      super(property, clazz, label);
      this.type = type;
      propertyArea = new JPanel();
      deleteButton = new JButton("Delete");
      editButton = new JButton("Edit");
      propertyArea.add(deleteButton);
      propertyArea.add(editButton);
      actions = new ButtonActions();
      deleteButton.addActionListener(actions);
      editButton.addActionListener(actions);
    }

    public JComponent getComponent() {
      return propertyArea;
    }

    public void setEditable(boolean editable) {
      deleteButton.setEnabled(editable);
      if (editable) {
        editButton.setText("Edit");
      } else {
        editButton.setText("View");
      }
    }

    protected Object getValue() {
      return (((copy != null) && copy.isModified()) ? copy : dataObject);
    }

    protected void setValue(Object value) {
      dataObject = (DataObject) value;
      copy = null;
      deleteButton.setEnabled(dataObject != null);
    }

    /**
     * Deletes the data object. The original object and all changes are lost.
     */
    void deleteObject() {
      setValue(null);
    }

    /**
     * Edits the data object. If the object does not exists it is created. The
     * view associated with the data object type is used to display a dialog to
     * edit the object.
     */
    void editObject() {
      if (dataObject != null) {
        if (copy == null) {
          copy = dataObject.copy();
        }
      } else {
        copy = type.createBoInstance();
        deleteButton.setEnabled(true);
      }
      // TODO open the frame with the view of the data object type.
    }

    private class ButtonActions implements ActionListener {
      public void actionPerformed(ActionEvent event) {
        if (event.getSource() == editButton) {
          editObject();
        } else if (event.getSource() == deleteButton) {
          deleteObject();
        }
      }
    }
  }

  static class IndexedPropertyView extends PropertyView {
    JButton button;

    public IndexedPropertyView(String property, Class type, String label) {
      super(property, type, label);
      this.button = new JButton("Edit");
    }

    public JComponent getComponent() {
      return button;
    }

    public void setEditable(boolean editable) {

    }

    protected Object getValue() {
      return null;
    }

    protected void setValue(Object value) {

    }
  }

  static class MappedPropertyView extends PropertyView {
    JButton button;

    public MappedPropertyView(String property, Class type, String label) {
      super(property, type, label);
      this.button = new JButton("Edit");
    }

    public JComponent getComponent() {
      return null;
    }

    public void setEditable(boolean editable) {

    }

    protected Object getValue() {
      return null;
    }

    protected void setValue(Object value) {

    }
  }
}