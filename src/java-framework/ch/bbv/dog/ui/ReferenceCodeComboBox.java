/* copyright 2000-2004 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence");
 * you may not use this file except in compliance with the License.
 * $Id$
 */
package ch.bbv.dog.ui;

import ch.bbv.dog.reference.ReferenceCode;

import java.util.Iterator;
import java.util.List;

import javax.swing.JComboBox;

/**
 * The reference code combo box provides a combo box for reference codes.
 * @author MarcelBaumann
 * @version $Revision$
 */
public class ReferenceCodeComboBox extends JComboBox {
  /**
   * Reference code class displayed in the combobox.
   */
  private Class clazz;

  /**
   * List of all codes for the reference code type.
   */
  private List codes;

  /**
   * Flag indicating if it is possible to have a null value.
   */
  private boolean hasEmptyValue;

  /**
   * Flag indicating if the transformation between the string representation of
   * the code and the associated code is case sensitive or not.
   */
  private boolean caseSensitive;

  /**
   * Flag indicating if inactive code are displayed or not.
   */
  private boolean inactiveShowed;

  /**
   * Constructor of the class.
   * @param clazz reference code class.
   * @param codes list of codes of the reference code type.
   */
  public ReferenceCodeComboBox(Class clazz, List codes) {
    this(clazz, codes, false);
  }

  /**
   * Constructor of the class.
   * @param clazz reference code class.
   * @param codes list of codes of the reference code type.
   * @param hasEmptyValue flag defining if null value is supported or not.
   */
  public ReferenceCodeComboBox(Class clazz, List codes, boolean hasEmptyValue) {
    assert (codes != null);
    caseSensitive = true;
    this.clazz = clazz;
    this.codes = codes;
    this.hasEmptyValue = hasEmptyValue;
    initialize();
  }

  /**
   * Returns the class of the reference code type displayed in the combobox.
   * @return class of the reference code type.
   */
  public Class getReferenceCodeType() {
    return clazz;
  }

  /**
   * Returns the inactive show flag value.
   * @return value of the flag.
   * @see #setInactiveShowed
   */
  public boolean isInactiveShowed() {
    return inactiveShowed;
  }

  /**
   * Sets the inactive show flag to its new value.
   * @param inactiveShowed new value of the flag.
   * @see #isInactiveShowed
   */
  public void setInactiveShowed(boolean inactiveShowed) {
    this.inactiveShowed = inactiveShowed;
  }

  /**
   * Returns the case sensitive flag value.
   * @return value of the flag.
   * @see #setCaseSensitive
   */
  public boolean isCaseSensitive() {
    return caseSensitive;
  }

  /**
   * Sets the case sensitive flag to its new value.
   * @param caseSensitive new value of the flag.
   * @see #isCaseSensitive
   */
  public void setCaseSensitive(boolean caseSensitive) {
    this.caseSensitive = caseSensitive;
  }

  /**
   * Sets the selected code in the combo box.
   * @param code new selected reference code.
   * @see #getSelectedCode
   */
  public void setSelectedCode(ReferenceCode code) {
    int index = codes.indexOf(code);
    if (hasEmptyValue) {
      index++;
    }
    setSelectedIndex(index);
  }

  /**
   * Returns the selected code in the combo box.
   * @return the selected code in the combo box if defined otherwise null.
   * @see #setSelectedCode
   */
  public ReferenceCode getSelectedCode() {
    int index = getSelectedIndex();
    ReferenceCode code = null;
    if (hasEmptyValue) {
      if (index > 1) {
        code = (ReferenceCode) codes.get(index + 1);
      }
    } else {
      code = (ReferenceCode) codes.get(index);
    }
    return code;
  }

  /**
   * Sets the selected item as defined in the super class.
   * @param object object should be a string and is the name of the code.
   */
  public void setSelectedItem(Object object) {
    if (!caseSensitive && object instanceof String) {
      String value = (String) object;
      int count = getItemCount();
      for (int i = 0; i < count; i++) {
        String item = (String) getItemAt(i);
        if (value.equalsIgnoreCase(item)) {
          super.setSelectedItem(item);
          break;
        }
      }
    } else {
      super.setSelectedItem(object);
    }
  }

  /**
   * Initializes the reference code combo box.
   */
  protected void initialize() {
    if (hasEmptyValue) {
      addItem("");
    }
    for (Iterator i = codes.iterator(); i.hasNext();) {
      ReferenceCode code = (ReferenceCode) i.next();
      addItem(code.getShortDescription());
    }
  }
}