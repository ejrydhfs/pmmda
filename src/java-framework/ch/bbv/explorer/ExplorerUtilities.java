/* copyright 2000-2005 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence");
 * you may not use this file except in compliance with the License.
 * $Id$
 */

package ch.bbv.explorer;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JDialog;
import javax.swing.JFrame;

/**
 * @author MarcelBaumann
 * @version $Revision$
 */
public class ExplorerUtilities {
  public static void centerOnScreen(JFrame frame) {
    positionOnScreen(frame, 0.5, 0.5);
  }

  public static void positionOnScreen(JFrame frame, double horizontal,
      double vertical) {
    Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
    Dimension window = frame.getSize();
    int width = Math.max(screen.width - window.width, 0);
    int height = Math.max(screen.height - window.height, 0);
    frame.setBounds((int) (horizontal * width), (int) (vertical * height),
        window.width, window.height);
  }

  public static void centerOnParent(JDialog dialog) {
    positionOnParent(dialog, 0.5, 0.5);
  }

  public static void positionOnParent(JDialog dialog, double horizontal,
      double vertical) {
    Dimension window = dialog.getSize();
    Container owner = dialog.getParent();
    Dimension parent = owner.getSize();
    int width = parent.width - window.width;
    int height = parent.height - window.height;
    int x = owner.getX() + (int) (horizontal * width);
    int y = owner.getY() + (int) (vertical * height);
    Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
    x = Math.max(Math.min(x, screen.width - window.width), 0);
    y = Math.max(Math.min(y, screen.height - window.height), 0);
    dialog.setBounds(x, y, window.width, window.height);
  }

}