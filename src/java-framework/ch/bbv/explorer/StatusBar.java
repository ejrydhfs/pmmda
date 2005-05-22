/* copyright 2000-2005 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence");
 * you may not use this file except in compliance with the License.
 * $Id$
 */

package ch.bbv.explorer;

import java.awt.BorderLayout;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

/**
 * The status bar provides a status bar for the explorer framework. Status and
 * progress information can be displayed in it.
 * @author MarcelBaumann
 * @version $Revision$
 */
public class StatusBar extends JPanel {
  /**
   * Serial version identifier coded as back compatibility date.
   */
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

  /**
   * Sets an information message on the status bar.
   * @param text text to set on the status bar.
   */
  public void setInfo(String text) {
    textLabel.setText(text);
  }

  /**
   * Sets a warning message on the status bar.
   * @param text text to set on the status bar.
   */
  public void setWarning(String text) {
    textLabel.setText(format(WARNING_FORMATS, text));
  }

  /**
   * Sets an error message on the status bar.
   * @param text text to set on the status bar.
   */
  public void setError(String text) {
    textLabel.setText(format(ERROR_FORMATS, text));
  }

  /**
   * Sets the progress bar of the status bar.
   * @param progress relative progress value between 0 and 100.
   * @pre (0 <= progress) && (progress <= 100)
   */
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