/* 
 * copyright 2000-2005 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence");
 * you may not use this file except in compliance with the License.
 * $Id$
 */

package ch.bbv.explorer.components;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.*;

import javax.swing.*;

import ch.bbv.explorer.ExplorerUtilities;

/**
 * The simple dialog defines a simple dialog with a view and two buttons. One to
 * accept the changes and one to cancel the changes.
 * @author MarcelBaumann
 * @version $Revision$
 */
public abstract class SimpleDialog extends JDialog implements ActionListener {
  /**
   * Text for the OK button.
   */
  static final String OK_BUTTON_LABEL = "OK";

  /**
   * Text for the cancel button.
   */
  static final String CANCEL_BUTTON_LABEL = "Cancel";

  /**
   * Text for the cancel button.
   */
  static final String APPLY_BUTTON_LABEL = "Apply";
  
  /**
   * Swing component of the property dialog.
   */
  private JButton okButton;

  /**
   * Swing component of the property dialog.
   */
  private JButton cancelButton;

  /**
   * Swing component of the property dialog.
   */
  private JButton applyButton;

  /**
   * Constructor of the class.
   * @param owner owner of the dialog. 
   */
  public SimpleDialog(JFrame owner) {
    super(owner);
    addWindowListener(new PropertiesWindowListener());
    okButton = new JButton(OK_BUTTON_LABEL);
    okButton.addActionListener(this);
    cancelButton = new JButton(CANCEL_BUTTON_LABEL);
    cancelButton.addActionListener(this);
    applyButton = new JButton(APPLY_BUTTON_LABEL);
    applyButton.addActionListener(this);
    JPanel panel = new JPanel(new FlowLayout());
    panel.add(cancelButton);
    panel.add(applyButton);
    panel.add(okButton);
    getContentPane().add(panel, BorderLayout.SOUTH);
    ExplorerUtilities.centerOnParent(this);
    KeyStroke stroke = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);
    panel.registerKeyboardAction(new EscListener(), stroke, JComponent.WHEN_IN_FOCUSED_WINDOW);
    stroke = KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0);
    panel.registerKeyboardAction(new EnterListener(), stroke, JComponent.WHEN_IN_FOCUSED_WINDOW);
  }

  /**
   * Called each time one of the buttons of the dialog is activated. The
   * associated abstract processing methods are called.
   * @param event event detected in Swing.
   */
  public void actionPerformed(ActionEvent event) {
    Object source = event.getSource();
    if (source == okButton) {
      processOk();
      setVisible(false);
    } else if (source == cancelButton) {
      processCancel();
      setVisible(false);
    } else if (source == applyButton) {
      processApply();
    }
  }

  /**
   * Sets the view of the dialog.
   * @param view the new view of the dialog.
   */
  public void setView(JPanel view) {
    getContentPane().add(view, BorderLayout.CENTER);
    pack();
    ExplorerUtilities.centerOnParent(this);
  }

  /**
   * Sets the enabled status of the button.
   * @param enabled new status of the button.
   */
  public void setOkButtonEnabled(boolean enabled) {
    okButton.setEnabled(enabled);
  }

  /**
   * Sets the enabled status of the button.
   * @param enabled new status of the button.
   */
  public void setApplyButtonEnabled(boolean enabled) {
    applyButton.setEnabled(enabled);
  }

  /**
   * Sets the enabled status of the button.
   * @param enabled new status of the button.
   */
  public void setCancelButtonEnabled(boolean enabled) {
    cancelButton.setEnabled(enabled);
  }

  /**
   * The process method is called either when the cancel button is pressed or
   * the window is closed.
   */
  protected abstract void processCancel();

  /**
   * The process method is called when the ok button is pressed.
   */
  protected abstract void processOk();

  /**
   * The process method is called when the apply button is pressed.
   */
  protected abstract void processApply();
  
  /**
   * Implements the window listener of the property dialog.
   */
  private class PropertiesWindowListener extends WindowAdapter {
    /**
     * Handles the window closing event.
     * @param event windows event to handle.
     */
    public void windowClosing(WindowEvent event) {
      processCancel();
      setVisible(false);
    }
  }
  
  /**
   * Listener for escape keys -> cancel action
   */
  private class EscListener implements ActionListener {
    public void actionPerformed(ActionEvent actionEvent) {
      processCancel();
      setVisible(false);
    }
  }

  /**
   * Listener for enter key -> ok action 
   */
  private class EnterListener implements ActionListener {
    public void actionPerformed(ActionEvent actionEvent) {
      processOk();
      setVisible(false);
    }
  }
}