/* copyright 2000-2006 Marcel Baumann 
 * Licensed under the Apache License, Version 2.0 (the "Licence"); 
 * you may not use this file except in compliance with the License. 
 * $Id$
 */
package ch.bbv.explorer.about;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

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
  private static final long serialVersionUID = -4640733906720449033L;

  /**
   * Constructor of the class.
   * @param projectInfo the project information
   */
  public AboutPanel(ProjectInfo projectInfo) {
    setLayout(new BorderLayout());
    JPanel textPanel = new JPanel(new GridLayout(4, 1, 0, 4));
    textPanel.add(create(projectInfo.getName()));
    textPanel.add(create(projectInfo.getVersion().toString()));
    textPanel.add(create(projectInfo.getCopyright()));
    textPanel.add(create(projectInfo.getInfo()));
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
  
  /** Creates a new panel with the given name
   * 
   * @param name of the panel as displayed in the label
   * @return the created panel
   */
  private JPanel create(String name) {
    JPanel panel = new JPanel();
    JLabel label = new JLabel(name);
    label.setHorizontalTextPosition(SwingConstants.CENTER);
    panel.add(label);
    return panel;

  }
}
