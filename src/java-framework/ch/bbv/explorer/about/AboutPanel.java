/* copyright 2000-2005 Marcel Baumann 
 * Licensed under the Apache License, Version 2.0 (the "Licence"); 
 * you may not use this file except in compliance with the License. 
 * $Id$
 */
package ch.bbv.explorer.about;

import java.awt.*;

import javax.swing.*;

import ch.bbv.application.ProjectInfo;

/**
 * A standard panel for displaying information about an application.
 * @author Marcel Baumann
 * @version $Revision$
 */
public class AboutPanel extends JPanel {
  /**
   * Serial version identifier coded as back compatibility date.
   */
  private static final long serialVersionUID = 20041201;

  /**
   * Constructor of the class.
   * @param projectInfo the project information.
   */
  public AboutPanel(ProjectInfo projectInfo) {
    setLayout(new BorderLayout());

    JPanel textPanel = new JPanel(new GridLayout(4, 1, 0, 4));

    JPanel appPanel = new JPanel();
    Font f1 = new Font("Dialog", Font.BOLD, 14);
    JLabel appLabel = createLabel(projectInfo.getName(), f1, Color.black);
    appLabel.setHorizontalTextPosition(SwingConstants.CENTER);
    appPanel.add(appLabel);

    JPanel verPanel = new JPanel();
    Font f2 = new Font("Dialog", Font.PLAIN, 12);
    JLabel verLabel = createLabel(projectInfo.getVersion().toString(), f2,
        Color.black);
    verLabel.setHorizontalTextPosition(SwingConstants.CENTER);
    verPanel.add(verLabel);

    JPanel copyrightPanel = new JPanel();
    JLabel copyrightLabel = createLabel(projectInfo.getCopyright(), f2,
        Color.black);
    copyrightLabel.setHorizontalTextPosition(SwingConstants.CENTER);
    copyrightPanel.add(copyrightLabel);

    JPanel infoPanel = new JPanel();
    JLabel infoLabel = createLabel(projectInfo.getInfo(), f2, Color.black);
    infoLabel.setHorizontalTextPosition(SwingConstants.CENTER);
    infoPanel.add(infoLabel);

    textPanel.add(appPanel);
    textPanel.add(verPanel);
    textPanel.add(copyrightPanel);
    textPanel.add(infoPanel);
    add(textPanel);

    if (projectInfo.getLogo() != null) {
      JPanel imagePanel = new JPanel(new BorderLayout());
      imagePanel.add(new javax.swing.JLabel(new javax.swing.ImageIcon(
          projectInfo.getLogo())));
      imagePanel.setBorder(BorderFactory.createLineBorder(Color.black));

      JPanel imageContainer = new JPanel(new BorderLayout());
      imageContainer.add(imagePanel, BorderLayout.NORTH);
      add(imageContainer, BorderLayout.WEST);
    }
  }

  /**
   * Creates a label of the expected structure.
   * @param text text of the label.
   * @param font font of the label.
   * @param color color of the label.
   * @return the requested label.
   */
  public JLabel createLabel(String text, Font font, Color color) {
    JLabel result = new JLabel(text);
    result.setFont(font);
    result.setForeground(color);
    return result;
  }
}
