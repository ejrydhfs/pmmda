/* copyright 2000-2005 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence");
 * you may not use this file except in compliance with the License.
 * $Id$
 */

package ch.bbv.explorer;

import java.awt.BorderLayout;

import javax.swing.*;

/**
 * The status bar provides a status bar for the explorer framework. Status and
 * progress information can be displayed in it.
 * @author MarcelBaumann
 * @version $Revision$
 */
public class StatusBar extends JPanel {
  private static final long serialVersionUID = 20041201;

  private JLabel textLabel;

  private JProgressBar progressBar;

  private StringBuilder buffer;

  private static String[] WARNING_FORMATS = { "<html><font color=yellow>",
      "</font></html>" };

  private static String[] ERROR_FORMATS = { "<html><font color=red>",
      "</font></html>" };

  /**
   * Default constructor of the class.
   */
  public StatusBar() {
    setBorder(BorderFactory.createLoweredBevelBorder());
    setLayout(new BorderLayout());
    textLabel = new JLabel("");
    progressBar = new JProgressBar(0, 100);
    progressBar.setValue(0);
    buffer = new StringBuilder();
    add(BorderLayout.WEST, textLabel);
    add(BorderLayout.EAST, progressBar);
  }

  public void setInfo(String info) {
    textLabel.setText(info);
  }

  public void setWarning(String text) {
    textLabel.setText(format(WARNING_FORMATS, text));
  }

  public void setError(String text) {
    textLabel.setText(format(ERROR_FORMATS, text));
  }

  public void setProgress(int progress) {
    assert ((0 <= progress) && (progress <= 100));
    progressBar.setValue(progress);
  }

  private String format(String[] formats, String text) {
    buffer.setLength(0);
    buffer.append(formats[0]).append(text).append(formats[1]);
    return buffer.toString();
  }
}