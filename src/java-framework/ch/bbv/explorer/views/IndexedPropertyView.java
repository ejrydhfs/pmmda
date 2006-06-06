/* copyright 2000-2006 Marcel Baumann 
 * Licensed under the Apache License, Version 2.0 (the "Licence"); 
 * you may not use this file except in compliance with the License. 
 * $Id$ 
 */
package ch.bbv.explorer.views;

import javax.swing.JButton;
import javax.swing.JComponent;

import ch.bbv.explorer.resources.Messages;

class IndexedPropertyView extends PropertyView {
  JButton button;

  public IndexedPropertyView(String property, Class type, String label) {
    super(property, type, label);
    this.button = new JButton(Messages.actionEdit);
  }

  @Override
  public JComponent getComponent() {
    return button;
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