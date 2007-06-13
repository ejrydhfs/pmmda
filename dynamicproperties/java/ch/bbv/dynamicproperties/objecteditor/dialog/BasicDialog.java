/*********************************************************************** 
 * Copyright 2007 BBV Software Services AG (http://www.bbv.ch)
 *  
 * Licensed under the Apache License, Version 2.0 (the "Licence"); 
 * http://www.apache.org/licenses/LICENSE-2.0
 * you may not use this file except in compliance with the License.
 * 
 * $Id$
 * 
 * History:
 * 2007/06/06      Ueli Kurmann     Initial Release
 ***********************************************************************/

package ch.bbv.dynamicproperties.objecteditor.dialog;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.FormLayout;

public abstract class BasicDialog extends JDialog {

    protected String OK_BUTTON_LABEL = "OK";

    protected String CANCEL_BUTTON_LABEL = "Cancel";

    private JPanel panel;

    private JButton buttonOk;

    private JButton buttonCancel;

    public BasicDialog() {
        super();
        initialize();
    }

    private void initialize() {
        panel = new JPanel();
        panel.setLayout(new BorderLayout());

        panel.add(createFormArea(), BorderLayout.NORTH);
        panel.add(createControlArea(), BorderLayout.SOUTH);

        this.add(panel);
        this.pack();
    }

    /**
     * Creates the control area
     * 
     * @return
     */
    protected JComponent createControlArea() {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());
        buttonOk = new JButton(OK_BUTTON_LABEL);
        buttonCancel = new JButton(CANCEL_BUTTON_LABEL);
        buttonCancel.setAction(new CancelAction());
        panel.add(buttonOk);
        panel.add(buttonCancel);
        return panel;
    }

    public void setOkAction(Action action) {
        buttonOk.setAction(action);
    }

    abstract JComponent createFormArea();

    public JLabel createLabel(String text) {
        return new JLabel(text);
    }

    protected DefaultFormBuilder createFormBuilder() {
        FormLayout layout = new FormLayout("right:pref, 3dlu, pref:grow", "");
        DefaultFormBuilder builder = new DefaultFormBuilder(layout);
        builder.setDefaultDialogBorder();

        return builder;
    }

    private class CancelAction extends AbstractAction {

        public CancelAction() {
            this.putValue(NAME, CANCEL_BUTTON_LABEL);
        }

        public void actionPerformed(ActionEvent arg0) {
            BasicDialog.this.setVisible(false);
            BasicDialog.this.dispose();
        }
    }

}
