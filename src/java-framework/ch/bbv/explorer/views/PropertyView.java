package ch.bbv.explorer.views;

import javax.swing.JComponent;

import ch.bbv.dog.DataObject;
import ch.bbv.utilities.ReflectUtilities;

/**
 * The abstract property view defines the interface for all property views. A
 * property view displays a property of a bean on the user interface.
 * @author MarcelBaumann
 * @version $Revision$
 */
abstract class PropertyView {
  /**
   * Standard size of the text field.
   */
  protected static final int TEXT_FIELD_SIZE = 32;

  /**
   * The bean which property is displayed.
   */
  private DataObject bean;

  /**
   * Name of the property displayed in the view.
   */
  private String property;

  /**
   * Label of the property in the view.
   */
  private String label;

  /**
   * Type of the property.
   */
  private Class type;

  /**
   * Constructor of the property view.
   * @param property property name to be displayed in the view
   * @param type type of the simple property
   * @param label label of the simple property
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
   * @param editable new value of the editable flag
   */
  public abstract void setEditable(boolean editable);

  /**
   * Returns the type of the property.
   * @return the type of the property
   */
  public Class getType() {
    return type;
  }

  /**
   * Returns the label of the property.
   * @return the label of the property
   */
  public String getLabel() {
    return label;
  }

  /**
   * Sets the model of the view to the new data object. The view is
   * automatically updated.
   * @param bean bean which property should be displayed
   */
  public void setModel(DataObject bean) {
    this.bean = bean;
    Object value;
    if ((bean != null) && (property != null)) {
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
   * @param value value of the property
   */
  protected abstract void setValue(Object value);
}