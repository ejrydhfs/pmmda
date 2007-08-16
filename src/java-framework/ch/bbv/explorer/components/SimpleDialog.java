/* 
 * copyright 2000-2006 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence");
 * you may not use this file except in compliance with the License.
 * $Id$
 */

package ch.bbv.explorer.components;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

import ch.bbv.explorer.ExplorerUtilities;

/**
 * The simple dialog defines a simple dialog with a view and two buttons. One to
 * accept the changes and one to cancel the changes.
 * @author MarcelBaumann
 * @version $Revision$
 */
public class SimpleDialog extends JDialog implements ActionListener {
  /**
   * Serial version identifier for the class.
   */
  private static final long serialVersionUID = 7868386323347272561L;

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
   * @param owner owner of the dialog
   */
  public SimpleDialog(JFrame owner) {
    this(owner, true, true);
  }

  /**
   * Constructor of the class.
   * @param owner owner of the dialog
   * @param displayCancel flag indicating if the cancel button should be
   *          displayed
   * @param displayApply flag indicating if the apply button should be displayed
   */
  public SimpleDialog(JFrame owner, boolean displayCancel, boolean displayApply) {
    super(owner);
    addWindowListener(new PropertiesWindowListener());
    okButton = new JButton(OK_BUTTON_LABEL);
    okButton.addActionListener(this);
    cancelButton = new JButton(CANCEL_BUTTON_LABEL);
    cancelButton.addActionListener(this);
    applyButton = new JButton(APPLY_BUTTON_LABEL);
    applyButton.addActionListener(this);
    JPanel panel = new JPanel(new FlowLayout());
    
    if (displayCancel) {
      panel.add(cancelButton);
    }
    
    if (displayApply) {
      panel.add(applyButton);
    }
    
    panel.add(okButton);
    getContentPane().add(panel, BorderLayout.SOUTH);
    ExplorerUtilities.centerOnParent(this);
    KeyStroke stroke = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);
    panel.registerKeyboardAction(new EscapeListener(), stroke,
        JComponent.WHEN_IN_FOCUSED_WINDOW);
    stroke = KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0);
    panel.registerKeyboardAction(new EnterListener(), stroke,
        JComponent.WHEN_IN_FOCUSED_WINDOW);
  }

  /**
   * Called each time one of the buttons of the dialog is activated. The
   * associated abstract processing methods are called.
   * @param event action event detected in Swing
   */
  public void actionPerformed(ActionEvent event) {
    Object source = event.getSource();
    if (source == okButton) {
      if (processOk()) {
        setVisible(false);
      }
    } else if (source == cancelButton) {
      processCancel();
      setVisible(false);
    } else if (source == applyButton) {
      processApply();
    }
  }

  /**
   * Sets the view of the dialog.
   * @param view the new view of the dialog
   */
  public void setView(JPanel view) {
    getContentPane().add(view, BorderLayout.CENTER);
    pack();
    ExplorerUtilities.centerOnParent(this);
  }

  /**
   * Returns the requested button.
   * @return the requested button if defined otherwise null
   */
  public JButton getOkButton() {
    return okButton;
  }

  /**
   * Returns the requested button.
   * @return the requested button if defined otherwise null
   */
  public JButton getCancelButton() {
    return cancelButton;
  }

  /**
   * Returns the requested button.
   * @return the requested button if defined otherwise null
   */
  public JButton getApplyButton() {
    return applyButton;
  }

  /**
   * The process method is called either when the cancel button is pressed or
   * the window is closed. If action is requested it should be overwritten in
   * the subclass.
   * @return true if the processing was successful otherwise false. The dialog
   *         is only closed if the processing is sucessful
   */
  protected boolean processCancel() {
    return true;
  }

  /**
   * The process method is called when the ok button is pressed. If action is
   * requested it should be overwritten in the subclass.
   * @return true if the processing was successful otherwise false. The dialog
   *         is only closed if the processing is sucessful
   */
  protected boolean processOk() {
    return true;
  }

  /**
   * The process method is called when the apply button is pressed. If action is
   * requested it should be overwritten in the subclass.
   * @return true if the processing was successful otherwise false
   */
  protected boolean processApply() {
    return true;
  }

  /**
   * Implements the window listener of the property dialog.
   */
  private class PropertiesWindowListener extends WindowAdapter {
    /**
     * Handles the window closing event.
     * @param event windows event to handle
     */
    public void windowClosing(WindowEvent event) {
      processCancel();
      setVisible(false);
    }
  }

  /**
   * The escape key listener associated with the cancel action.
   */
  private class EscapeListener implements ActionListener {
    /**
     * @inheritDoc
     */
    public void actionPerformed(ActionEvent actionEvent) {
      processCancel();
      setVisible(false);
    }
  }

  /**
   * The enter key listener associated with the enter action.
   */
  private class EnterListener implements ActionListener {
    /**
     * @inheritDoc
     */
    public void actionPerformed(ActionEvent actionEvent) {
      if(processOk()) {
      	setVisible(false);
      }
    }
  }
}