/* copyright 2000-2005 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence");
 * you may not use this file except in compliance with the License.
 * $Id$ 
 */
package ch.bbv.explorer.actions;

import ch.bbv.application.ProjectInfo;
import ch.bbv.explorer.Explorer;
import ch.bbv.explorer.about.AboutFrame;

/**
 * The explorer about action displays the about dialog box when requesting. The
 * content of the about box is standardized.
 * @author MarcelBaumann
 * @version $Revision$
 */
public class ExplorerAboutAction extends ExplorerGlobalActionImpl {
  /**
   * Serial version identifier coded as back compatibility date.
   */
  private static final long serialVersionUID = 20041201;

  /**
   * The project description displayed in the about frame.
   */
  private transient ProjectInfo projectInfo;

  /**
   * Buffer containing the about frame once lazy created.
   */
  private AboutFrame aboutFrame;

  /**
   * Constructor of the instances of the class.
   * @param explorer explorer owning the global action.
   */
  public ExplorerAboutAction(Explorer explorer) {
    super("About", explorer);
  }

  /**
   * Returns the project information of the application.
   * @return the project information of the application.
   * @see #setProjectInfo(ProjectInfo)
   */
  public ProjectInfo getProjectInfo() {
    return projectInfo;
  }

  /**
   * Sets the project information of the application.
   * @param projectInfo new project information of the application.
   * @see #getProjectInfo()
   */
  public void setProjectInfo(ProjectInfo projectInfo) {
    this.projectInfo = projectInfo;
  }

  public boolean execute() {
    if (aboutFrame == null) {
      aboutFrame = new AboutFrame(getExplorer().getFrame(), "About",
          projectInfo);
    }
    aboutFrame.setVisible(true);
    return true;
  }
}