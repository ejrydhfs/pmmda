/* copyright 2000-2004 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence");
 * you may not use this file except in compliance with the License.
 * $Id$
 */
package ch.bbv.explorer.components;

import java.awt.event.*;

import javax.swing.ComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.UIManager;
import javax.swing.text.*;

/**
 * The completion adapter provides auto completion features for
 * Swing combo boxes. Once you wrapped an adapter on the combo
 * box, auto completion is available.
 * @author MarcelBaumann
 * @version $Revision$
 */
public class CompletionAdapter extends PlainDocument {
  /**
   * The combo box to extend with auto completion features.
   */
  private JComboBox comboBox;

  /**
   * The editor of the combo box.
   */
  private JTextComponent editor;

  /**
   * Flag indicating if backspace was hit.
   */
  private boolean hitBackspace;

  /**
   * Flag indicating if backspace was hit on a selection.
   */
  private boolean hitBackspaceOnSelection;

  /**
   * Creates a completion adapter for the given combo box.
   * @param comboBox combo box to extend.
   */
  public CompletionAdapter(JComboBox comboBox) {
    this.comboBox = comboBox;
    comboBox.setEditable(true);
    comboBox.addActionListener(new CompletionActionListener());
    editor = (JTextComponent) comboBox.getEditor().getEditorComponent();
    editor.setDocument(this);
    editor.addFocusListener(new CompletionFocusAdapter());
    editor.addKeyListener(new CompletionKeyAdapter());
    Object selected = comboBox.getSelectedItem();
    if (selected != null)
      editor.setText(selected.toString());
  }

  /**
   * Convenience method to enable auto completion for a combo box.
   * @param comboBox combo box which should support auto completion.
   */
  public static void enable(JComboBox comboBox) {
    new CompletionAdapter(comboBox);
  }

  public void remove(int offs, int length) throws BadLocationException {
    if (hitBackspace) {
      // user hit backspace => move the selection backwards
      // old item keeps being selected
      if (offs > 0 && hitBackspaceOnSelection)
        offs--;
      highlightCompletedText(offs);
      comboBox.setPopupVisible(true);
    } else {
      super.remove(offs, length);
    }
  }

  public void insertString(int offs, String str, AttributeSet attributes)
      throws BadLocationException {
    String currentText = getText(0, getLength());
    String beforeOffset = currentText.substring(0, offs);
    String afterOffset = currentText.substring(offs, currentText.length());
    String futureText = beforeOffset + str + afterOffset;
    Object item = lookupItem(futureText);
    if (item != null) {
      comboBox.setSelectedItem(item);
    } else {
      item = comboBox.getSelectedItem();
      // imitate insert at previous position (selection won't move forward)
      offs--;
      // provide feedback to the user that his input has been received but can
      // not be accepted
      UIManager.getLookAndFeel().provideErrorFeedback(comboBox);
    }

    String itemString = item.toString();
    setText(itemString);

    // if the user selects an item via mouse the the whole string will be
    // inserted.
    // highlight the entire text if this happens.
    if (itemString.equals(str) && offs == 0) {
      highlightCompletedText(0);
    } else {
      highlightCompletedText(offs + 1);
      comboBox.setPopupVisible(true);
    }
  }

  void highlightCompletedText(int start) {
    editor.setCaretPosition(getLength());
    editor.moveCaretPosition(start);
  }

  Object lookupItem(String pattern) {
    ComboBoxModel model = comboBox.getModel();
    Object selectedItem = model.getSelectedItem();
    // only search for a different item if the currently selected does not match
    if (selectedItem != null
        && startsWithIgnoreCase(selectedItem.toString(), pattern)) {
      return selectedItem;
    }
    for (int i = 0, n = model.getSize(); i < n; i++) {
      Object currentItem = model.getElementAt(i);
      if (startsWithIgnoreCase(currentItem.toString(), pattern)) {
        return currentItem;
      }
    }
    return null;
  }

  private void setText(String text) throws BadLocationException {
    // remove all text and insert the new text
    super.remove(0, getLength());
    super.insertString(0, text, null);
  }

  private boolean startsWithIgnoreCase(String str1, String str2) {
    return str1.toUpperCase().startsWith(str2.toUpperCase());
  }

  private class CompletionActionListener implements ActionListener {

    public void actionPerformed(ActionEvent event) {
      if (event.getActionCommand().equals("comboBoxEdited")) {
        // select the entire text when the user hits enter
        highlightCompletedText(0);
      }
    }
  }

  private class CompletionFocusAdapter extends FocusAdapter {
    public void focusLost(FocusEvent e) {
      highlightCompletedText(0);
      comboBox.setPopupVisible(false);
    }
  }

  private class CompletionKeyAdapter extends KeyAdapter {
    /**
     * handles the event and determines if the pressed key is backspace (needed
     * by the remove method).
     * @param event event to process.
     */
    public void keyPressed(java.awt.event.KeyEvent event) {
      if (event.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
        hitBackspace = true;
        hitBackspaceOnSelection = editor.getSelectionStart() != editor
            .getSelectionEnd();
      } else {
        hitBackspace = false;
      }
    }
  }
}