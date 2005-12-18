/* copyright 2000-2005 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence");
 * you may not use this file except in compliance with the License.
 * $Id$
 */
package ch.bbv.explorer.components;

import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

import javax.swing.DefaultCellEditor;
import javax.swing.JTextField;

/**
 * Customized editor stopping editing in text field when focus is lost.
 * @author MarcelBaumann
 * @version $Revision$
 */
public class FocusCellEditor extends DefaultCellEditor {
  /**
   * Serial version identifier coded as back compatibility date.
   */
  private static final long serialVersionUID = 20041201;

  /**
   * Constructor of the class.
   * @param field text field which editor should be focus aware.
   */
  public FocusCellEditor(JTextField field) {
    super(field);
    // setting the click count to start with 0 solved a editor refresh problem
    // when an edited value is leaved with the dialog cancel button.
    setClickCountToStart(0);
    field.addFocusListener(new FocusListener(this));
  }

  /**
   * Implements a focus adapter ending cell editing in case of focus lost.
   */
  private final class FocusListener extends FocusAdapter {
    /**
     * The editor to managed through the focus listener.
     */
    private FocusCellEditor editor;

    /**
     * Constructor of the class.
     * @param editor editor to manage in the focus listener.
     */
    public FocusListener(FocusCellEditor editor) {
      this.editor = editor;
    }

    @Override
    public void focusLost(FocusEvent e) {
      editor.stopCellEditing();
    }
  }
}