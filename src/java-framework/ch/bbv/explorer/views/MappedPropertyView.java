/* copyright 2000-2006 Marcel Baumann 
 * Licensed under the Apache License, Version 2.0 (the "Licence"); 
 * you may not use this file except in compliance with the License. 
 * $Id$ 
 */
package ch.bbv.explorer.views;

import javax.swing.JButton;
import javax.swing.JComponent;

import ch.bbv.explorer.properties.Messages;

class MappedPropertyView extends PropertyView {
  JButton button;

  public MappedPropertyView(String property, Class type, String label) {
    super(property, type, label);
    this.button = new JButton(Messages.actionEdit);
  }

  @Override
 public JComponent getComponent() {
    return null;
  }

  @Override
  public void setEditable(boolean editable) {

  }

  @Override
 protected Object getValue() {
    return null;
  }

  @Override
 protected void setValue(Object value) {

  }
}