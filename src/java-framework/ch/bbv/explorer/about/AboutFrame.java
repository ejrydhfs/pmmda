/* copyright 2000-2004 Marcel Baumann 
 * Licensed under the Apache License, Version 2.0 (the "Licence"); 
 * you may not use this file except in compliance with the License. 
 * $Id$
 */
package ch.bbv.explorer.about;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.List;

import javax.swing.*;
import javax.swing.border.Border;

import ch.bbv.application.ProjectInfo;
import ch.bbv.explorer.components.PropertiesPanel;
import ch.bbv.explorer.components.TableCell;
import ch.bbv.explorer.components.TablePanel;

/**
 * A dialog that displays information about the application.
 * @author Marcel Baumann
 * @version $Revision$
 */
public class AboutFrame extends JDialog {
  /**
   * The name of the columns of the contributors table list.
   */
  private static final String[] CONTRIBUTORS_COLUMN_NAMES = { "name", "email",
      "role" };

  /** The preferred size for the frame. */
  private static final Dimension PREFERRED_SIZE = new Dimension(600, 400);

  /**
   * The default border for the panels in the tabbed pane.
   */
  private static final Border STANDARD_BORDER = BorderFactory
      .createEmptyBorder(5, 5, 5, 5);

  /**
   * The description of the project.
   */
  private ProjectInfo projectInfo;

  /**
   * Constructs an about frame.
   * @param owner the owner of the dialog.
   * @param title the frame title.
   * @param projectInfo information about the project.
   */
  public AboutFrame(JFrame owner, String title, ProjectInfo projectInfo) {
    super(owner, title);

    this.projectInfo = projectInfo;

    JPanel content = new JPanel(new BorderLayout());
    content.setBorder(STANDARD_BORDER);

    JTabbedPane tabs = createTabs();
    content.add(tabs);
    setContentPane(content);
    pack();
  }

  /**
   * Returns the preferred size for the about frame.
   * @return the preferred size.
   */
  public Dimension getPreferredSize() {
    return PREFERRED_SIZE;
  }

  /**
   * Creates a tabbed pane containing an about panel and a system properties
   * panel.
   * @return a tabbed pane.
   */
  private JTabbedPane createTabs() {
    JTabbedPane tabs = new JTabbedPane();

    JPanel aboutPanel = createAboutPanel();
    aboutPanel.setBorder(AboutFrame.STANDARD_BORDER);

    String aboutTab = "About";
    tabs.add(aboutTab, aboutPanel);

    JPanel systemPanel = PropertiesPanel.createSystemPropertiesPanel();
    systemPanel.setBorder(AboutFrame.STANDARD_BORDER);

    String systemTab = "System";
    tabs.add(systemTab, systemPanel);

    return tabs;
  }

  /**
   * Creates a panel showing information about the application, including the
   * name, version, copyright notice, URL for further information, and a list of
   * contributors.
   * @return a panel.
   */
  private JPanel createAboutPanel() {
    JPanel about = new JPanel(new BorderLayout());

    JPanel details = new AboutPanel(projectInfo);

    boolean includetabs = false;
    JTabbedPane tabs = new JTabbedPane();

    List contributors = projectInfo.getContributors();
    if (contributors != null) {
      JPanel contributorsPane = new TablePanel(contributors, new TableCell(
          CONTRIBUTORS_COLUMN_NAMES));
      contributorsPane.setBorder(AboutFrame.STANDARD_BORDER);

      String contributorsTab = "Contributors";
      tabs.add(contributorsTab, contributorsPane);
      includetabs = true;
    }

    if (projectInfo.getLicenceName() != null) {
      JPanel licencePanel = createLicencePanel();
      licencePanel.setBorder(STANDARD_BORDER);

      String licenceTab = "Licence";
      tabs.add(licenceTab, licencePanel);
      includetabs = true;
    }

    List libraries = projectInfo.getLibraries();
    if (libraries != null) {
      JPanel librariesPanel = new LibraryPanel(libraries);
      librariesPanel.setBorder(AboutFrame.STANDARD_BORDER);

      String librariesTab = "Libraries";
      tabs.add(librariesTab, librariesPanel);
      includetabs = true;
    }

    about.add(details, BorderLayout.NORTH);
    if (includetabs) {
      about.add(tabs);
    }

    return about;
  }

  /*
   * Creates a panel showing the licence. @return a panel.
   */
  private JPanel createLicencePanel() {
    JPanel licence = new JPanel(new BorderLayout());
    JTextArea area = new JTextArea(projectInfo.getLicenceText());
    area.setLineWrap(true);
    area.setWrapStyleWord(true);
    area.setCaretPosition(0);
    area.setEditable(false);
    licence.add(new JScrollPane(area));
    return licence;
  }
}