/* copyright 2000-2006 Marcel Baumann 
 * Licensed under the Apache License, Version 2.0 (the "Licence"); 
 * you may not use this file except in compliance with the License. 
 * $Id$ 
 */
package ch.bbv.explorer.views;

import javax.swing.JButton;
import javax.swing.JComponent;

class IndexedPropertyView extends PropertyView {
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