/* copyright 2000-2005 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence");
 * you may not use this file except in compliance with the License.
 * $Id$
 */

package ch.bbv.explorer.components;

import javax.swing.JComponent;
import javax.swing.JTabbedPane;

/**
 * The class provides a simple docking area for additional components. The
 * components are added to the docking area as tabs. At most one tab is visible.
 * @author MarcelBaumann
 * @version $Revision$
 */
public class DockingArea extends JTabbedPane {
  /**
   * Serial version identifier coded as back compatibility date.
   */
  private static final long serialVersionUID = 20041201;

  /**
   * Default constructor of the class.
   */
  public DockingArea() {
  }

  /**
   * Returns the tab component witht the given tile.
   * @param title title of the tab component to return.
   * @return the requested tab component if found otherwise null.
   */
  public JComponent getTab(String title) {
    JComponent tab = null;
    int index = indexOfTab(title);
    if (index != -1) {
      tab = (JComponent)getComponentAt(index);
    }
    return tab;
  }
}