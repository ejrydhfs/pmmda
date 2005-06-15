/* copyright 2000-2005 Marcel Baumann 
 * Licensed under the Apache License, Version 2.0 (the "Licence"); 
 * you may not use this file except in compliance with the License. 
 * $Id$ 
 */
package ch.bbv.explorer;

import java.text.NumberFormat;

import javax.swing.JFormattedTextField;

import ch.bbv.utilities.TypeUtilities;

/**
 * The Swing utilities class provides higher level functions to provide user
 * interface views of standard Java types.
 * @author Marcel Baumann
 * @version $Revision$
 */
public class UiSwingUtilities {
  /**
   * Returns a formatted text field for elements of the given class.
   * @param clazz class to which a view must be provided.
   * @return the requested view for instances of the class.
   */
  public static JFormattedTextField getField(Class clazz) {
    JFormattedTextField field;
    if (TypeUtilities.isNaturalNumber(clazz)) {
      field = new JFormattedTextField(NumberFormat.getIntegerInstance());
    } else if (TypeUtilities.isRealNumber(clazz)) {
      field = new JFormattedTextField(NumberFormat.getIntegerInstance());
    } else {
      field = new JFormattedTextField();
    }
    return field;
  }
}
