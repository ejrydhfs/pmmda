package ch.bbv.explorer.views;

import java.util.Date;

import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JTextField;

import ch.bbv.explorer.UiSwingUtilities;
import ch.bbv.utilities.TypeUtilities;

import com.toedter.calendar.JDateChooser;

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
      formattedText = UiSwingUtilities.getField(type);
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
    } else {
      String text = formattedText.getText();
      if (text != null && text.trim().length() == 0) {
        text = null;
      }
      if (Short.TYPE.equals(type)) {
        value = (text != null) ? Short.decode(text) : TypeUtilities.SHORT_0;
      } else if (Short.class.equals(type)) {
        value = (text != null) ? Short.decode(text) : null;
      } else if (Integer.TYPE.equals(type)) {
        value = (text != null) ? Integer.decode(text)
            : TypeUtilities.INTEGER_0;
      } else if (Integer.class.equals(type)) {
        value = (text != null) ? Integer.decode(text) : null;
      } else if (Long.TYPE.equals(type)) {
        value = (text != null) ? Long.decode(text) : TypeUtilities.LONG_0;
      } else if (Long.class.equals(type)) {
        value = (text != null) ? Long.decode(text) : null;
      } else if (Float.TYPE.equals(type)) {
        value = (text != null) ? Float.valueOf(text) : TypeUtilities.FLOAT_0;
      } else if (Float.class.equals(type)) {
        value = (text != null) ? Float.valueOf(text) : null;
      } else if (Double.TYPE.equals(type)) {
        value = (text != null) ? Double.valueOf(text)
            : TypeUtilities.DOUBLE_0;
      } else if (Double.class.equals(type)) {
        value = (text != null) ? Double.valueOf(text) : null;
      }
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
      if (TypeUtilities.isPrimitiveNumber(type)) {
        value = TypeUtilities.getPrimitiveZero(type);

      }
      formattedText.setValue(value);
    }
  }
}