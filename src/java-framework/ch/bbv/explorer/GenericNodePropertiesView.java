/* copyright 2000-2004 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence");
 * you may not use this file except in compliance with the License.
 * $Id$
 */
package ch.bbv.explorer;

import java.beans.IndexedPropertyDescriptor;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.*;

import javax.swing.*;

import org.apache.commons.beanutils.MappedPropertyDescriptor;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ch.bbv.dog.DataObject;

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
 * The formatted text field features are used to check if the inputted data have
 * the expected format.
 * @author MarcelBaumann
 * @version $Revision$
 */
public class GenericNodePropertiesView extends NodePropertiesView {
  /**
   * Logger for all instances of the class.
   */
  private static Log log = LogFactory.getLog(GenericNodePropertiesView.class);

  /**
   * The date format used to convert string representation of dates.
   */
  private DateFormat dateFormat;

  /**
   * List of all views for simple properties.
   */
  private List propertyViews;

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
    dateFormat = DateFormat.getDateInstance();
    propertyViews = new ArrayList();
    indexedPropertyViews = new ArrayList();
    mappedPropertyViews = new ArrayList();
  }

  public void populateView() {
    Object object = getModel().getDataObject();
    if (type == null) {
      type = object.getClass();
      retrieveProperties(object);
      initialize();
    }

    for (Iterator i = propertyViews.iterator(); i.hasNext();) {
      getTextValue(object, (PropertyView) i.next());
    }
  }

  public void commitChanges() {
    Object object = getModel().getDataObject();
    Iterator views = propertyViews.iterator();
    while (views.hasNext()) {
      setTextValue(object, (PropertyView) views.next());
    }
  }

  public void cancelChanges() {
    // no operations
  }

  public void setViewEditable(boolean editable) {
    for (Iterator i = propertyViews.iterator(); i.hasNext();) {
      ((PropertyView) i.next()).setEditable(editable);
    }
  }

  protected void initialize() {
    StringBuffer buffer = new StringBuffer();
    generateRowsLayout(buffer, propertyViews);
    generateRowsLayout(buffer, indexedPropertyViews);
    generateRowsLayout(buffer, mappedPropertyViews);

    FormLayout layout = new FormLayout("right:pref, 3dlu, pref, 12dlu, pref",
        buffer.toString());

    PanelBuilder builder = new PanelBuilder(layout);
    builder.setDefaultDialogBorder();

    int row = generateRows(builder, "Properties", propertyViews, 1);
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
        AbstractPropertyView view = (AbstractPropertyView) properties.get(i);
        builder.addLabel(view.label, cc.xy(1, currentRow));
        if (view instanceof PropertyView) {
          builder.add(((PropertyView) view).getComponent(), cc
              .xy(3, currentRow));
        } else if (view instanceof IndexedPropertyView) {
          builder
              .add(((IndexedPropertyView) view).button, cc.xy(5, currentRow));
        } else if (view instanceof MappedPropertyView) {
          builder.add(((MappedPropertyView) view).button, cc.xy(5, currentRow));
        }
        currentRow += 2;
      }
    }
    return currentRow;
  }

  protected void retrieveProperties(Object object) {
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
            // plural name of the indexed property
            IndexedPropertyView propertyView = new IndexedPropertyView(
                descriptor.getName(), descriptor.getPropertyType(), descriptor
                    .getDisplayName());
            indexedPropertyViews.add(propertyView);
          } else {
            if (!containsInterface(clazz.getInterfaces(), clazz)) {
              PropertyView propertyView = new PropertyView(
                  descriptor.getName(), clazz, descriptor.getDisplayName());
              propertyViews.add(propertyView);
            }
          }
        }
      }
    }
  }

  protected void getTextValue(Object object, PropertyView view) {
    try {
      Object value = PropertyUtils.getProperty(object, view.property);
      view.setValue(value);
    } catch (IllegalAccessException e) {
      log.error(e);
    } catch (InvocationTargetException e) {
      log.error(e);
    } catch (NoSuchMethodException e) {
      log.error(e);
    }
  }

  protected void setTextValue(Object object, PropertyView view) {
    Object value = view.getValue();
    if (value instanceof String) {
      String textValue = (String) value;
      if (String.class.equals(view.type)) {
        value = textValue;
      } else if (Boolean.TYPE.equals(view.type)
          || Boolean.class.equals(view.type)) {
        value = Boolean.valueOf(textValue);
      } else if (Short.TYPE.equals(view.type) || Short.class.equals(view.type)) {
        value = Short.decode(textValue);
      } else if (Integer.TYPE.equals(view.type)
          || Integer.class.equals(view.type)) {
        value = Integer.decode(textValue);
      } else if (Long.TYPE.equals(view.type) || Long.class.equals(view.type)) {
        value = Long.decode(textValue);
      } else if (Float.TYPE.equals(view.type) || Float.class.equals(view.type)) {
        value = Float.valueOf(textValue);
      } else if (Double.TYPE.equals(view.type)
          || Double.class.equals(view.type)) {
        value = Double.valueOf(textValue);
      } else if (Date.class.equals(view.type)) {
        try {
          value = dateFormat.parseObject(textValue);
        } catch (ParseException e) {
          log.error(e);
        }
      }
    }
    try {
      PropertyUtils.setProperty(object, view.property, value);
    } catch (IllegalAccessException e) {
      log.error(e);
    } catch (InvocationTargetException e) {
      log.error(e);
    } catch (NoSuchMethodException e) {
      log.error(e);
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

  static class AbstractPropertyView {
    protected static final int TEXT_FIELD_SIZE = 32;

    String property;

    String label;

    Class type;

    public AbstractPropertyView(String property, Class type, String label) {
      this.property = property;
      this.label = label;
      this.type = type;
    }
  }

  static class PropertyView extends AbstractPropertyView {
    private JTextField text;

    private JFormattedTextField formattedText;

    private JDateChooser dateChooser;

    private JCheckBox checkBox;

    public PropertyView(String property, Class type, String label) {
      super(property, type, label);
      if (type.equals(String.class)) {
        text = new JTextField(TEXT_FIELD_SIZE);
      } else if (Boolean.class.equals(type) || Boolean.TYPE.equals(type)) {
        checkBox = new JCheckBox();
      } else if (Date.class.equals(type)) {
        dateChooser = new JDateChooser();
      } else {
        formattedText = new JFormattedTextField();
      }
    }

    public Object getValue() {
      Object value = null;
      if (type.equals(String.class)) {
        value = text.getText();
      } else if (Boolean.class.equals(type) || Boolean.TYPE.equals(type)) {
        value = new Boolean(checkBox.isSelected());
      } else if (Date.class.equals(type)) {
        value = dateChooser.getDate();
      } else {
        formattedText.getText();
      }
      return value;
    }

    public void setValue(Object value) {
      if (type.equals(String.class)) {
        text.setText((String) value);
      } else if (Boolean.class.equals(type) || Boolean.TYPE.equals(type)) {
        checkBox.setSelected(((Boolean) value).booleanValue());
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
  }

  static class IndexedPropertyView extends AbstractPropertyView {
    JButton button;

    public IndexedPropertyView(String property, Class type, String label) {
      super(property, type, label);
      this.button = new JButton("Edit");
    }
  }

  static class MappedPropertyView extends AbstractPropertyView {
    JButton button;

    public MappedPropertyView(String property, Class type, String label) {
      super(property, type, label);
      this.button = new JButton("Edit");
    }
  }
}