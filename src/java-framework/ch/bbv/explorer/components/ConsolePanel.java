/* copyright 2000-2005 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence");
 * you may not use this file except in compliance with the License.
 * $Id$
 */

package ch.bbv.explorer.components;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 * The console panel implments a simple console where line of text are written.
 * @author MarcelBaumann
 * @version $Revision$
 */
public class ConsolePanel extends JPanel {
  /**
   * Serial version identifier coded as back compatibility date.
   */
  private static final long serialVersionUID = 20041201;

  /**
   * The text area containing the console text.
   */
  private JTextArea area;

  /**
   * Default constructor of the class.
   */
  public ConsolePanel() {
    super(new BorderLayout());
    area = new JTextArea();
    area.setWrapStyleWord(false);
    JScrollPane scrollPane = new JScrollPane(area);
    add(scrollPane, BorderLayout.CENTER);
  }

  /**
   * Clear the content of the console.
   */
  public void clear() {
    area.setText(null);
  }

  /**
   * Add a line to the console.
   * @param line next line displayed in the console.
   */
  public void addLine(String line) {
    area.setText(area.getText() + line + "\n");
  }
}
