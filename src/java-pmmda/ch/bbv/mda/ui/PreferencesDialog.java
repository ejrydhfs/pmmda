/* copyright 2000-2004 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence);
 * you may not use this file except in compliance with the License.
 * $Id$
 */

package ch.bbv.mda.ui;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import ch.bbv.explorer.components.PropertiesPanel;
import ch.bbv.explorer.components.SimpleDialog;
import ch.bbv.mda.generators.CartridgeFactory;

/**
 * The preferences dialog shows the preferences of the application and allows
 * the user to modify them. Configuration information is also displayed in the
 * dialog.
 * @author marcelbaumann
 * @version $Revision$
 */
public class PreferencesDialog extends SimpleDialog {

  private JTabbedPane tabbedPane;

  public PreferencesDialog() {
    JPanel content = new JPanel(new BorderLayout());
    tabbedPane = new JTabbedPane();
    tabbedPane.addTab("Cartridges", new CartridgesPanel(CartridgeFactory
        .getInstance()));
    tabbedPane.addTab("Cartridge", new CartridgePanel());
    tabbedPane.addTab("Properties", PropertiesPanel.createSystemPropertiesPanel());
    content.add(tabbedPane);
    setView(content);
  }
    
  protected void processCancel() {
  }

  protected void processOk() {
  }
}