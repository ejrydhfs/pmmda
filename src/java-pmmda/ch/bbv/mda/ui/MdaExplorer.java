/* copyright 2000-2004 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence");
 * you may not use this file except in compliance with the License.
 * $Id$
 */
package ch.bbv.mda.ui;

import java.io.File;
import java.util.*;
import java.util.prefs.Preferences;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ch.bbv.application.Library;
import ch.bbv.application.ProjectInfo;
import ch.bbv.application.VersionId;
import ch.bbv.dog.DataObjectManager;
import ch.bbv.explorer.Explorer;
import ch.bbv.explorer.ExplorerMenuBar;
import ch.bbv.explorer.UiNodeTypeFactory;
import ch.bbv.explorer.about.Contributor;
import ch.bbv.explorer.about.Licences;
import ch.bbv.explorer.actions.*;
import ch.bbv.mda.generators.Cartridge;
import ch.bbv.mda.generators.CartridgeFactory;
import ch.bbv.mda.persistency.PersistencyException;
import ch.bbv.mda.ui.actions.*;
import ch.bbv.utilities.ResourceUtility;

/**
 * The MDA explorer provides a user interface to view and manipulate model
 * driven architecture models. All the editing functions are provided through
 * the explorer user interface framework.
 * @author Marcel Baumann
 * @version $Revision$
 */
public class MdaExplorer extends Explorer {

  public static final String PREFERENCES_OPENED_PROJECT = "mda.opened-project";

  /**
   * Logger instance of the class.
   */
  private static Log log = LogFactory.getLog(MdaExplorer.class);

  /**
   * User interface node type factory of the explorer instances.
   */
  private static UiNodeTypeFactory factory;

  /**
   * Lightweight object handler of the explorer instances.
   */
  private static DataObjectManager manager;

  private ExplorerGlobalAction newAction;

  private ExplorerGlobalAction saveAction;

  private ExplorerGlobalAction saveAsAction;

  private ExplorerGlobalAction exitAction;

  private OpenAction openAction;

  private ExplorerGlobalAction generateAllAction;

  /**
   * Constructor of the class.
   */
  public MdaExplorer() {
    super(factory, manager, null);
    if (log.isInfoEnabled()) {
      log.info("Start initializing MDA explorer");
    }
    initialize();
    display();
  }

  /**
   * Creates a new MDA explorer.
   * @param args arguments of the command line.
   * @return the newly created MDA explorer.
   */
  public static MdaExplorer createExplorer(String[] args) {
    MdaExplorer explorer = new MdaExplorer();
    explorer.setTitle("pmMDA Explorer");
    log.info("pmMDA explorer created");
    explorer.parseArguments(args);
    return explorer;
  }

  /**
   * register all the node types used the explorer.
   */
  public static void registerNodeTypes() {
    manager = MdaApplication.getApplication().getDataObjectManager();
    factory = new UiNodeTypeFactory();

    TypeFactory typeFactory = new TypeFactory(factory, manager);
    typeFactory.registerAllTypes();
  }

  /**
   * Parses the command line arguments.
   * @param args command line arguments.
   */
  public void parseArguments(String[] args) {
    try {
      if (args.length >= 2) {
        // TODO clean up assume -m filename
        File file = new File(args[1]);
        openAction.openModel(file);
      }
    } catch (PersistencyException pe) {
      log.error("parse arguments exception", pe);
      pe.printStackTrace();
    }
  }

  /**
   * Initializes the pmmda explorer.
   */
  protected void initialize() {
    new ResourceUtility("ch.bbv.mda.ui.Messages", Locale.US);
    ExplorerMenuBar menuBar = new ExplorerMenuBar();
    setMenuBar(menuBar);

    ProjectInfo projectInfo = new ProjectInfo();
    projectInfo
        .setCopyright("copyright Marcel Baumann (Apache Sofware License 2.0)");
    projectInfo.setInfo("pmmda.sourceforge.net");
    projectInfo.setLicenceName("Apache Software License 2.0");
    projectInfo.setLicenceText(Licences.LICENCE_APACHE_2_0);
    projectInfo.setName("pmMDA - poor man Model Driven Architecture -");
    projectInfo.setVersion(new VersionId(0, 2, 9, "RC1"));

    projectInfo.setContributors(getContributors());

    projectInfo.setLibraries(getLibrairies());

    JMenu menu = menuBar.getMenu(ExplorerMenuBar.HELP_MENU);
    ExplorerAboutAction aboutAction = new ExplorerAboutAction();
    aboutAction.setProjectInfo(projectInfo);
    menu.add(new JMenuItem(aboutAction));

    menu = menuBar.getMenu(ExplorerMenuBar.FILE_MENU);
    newAction = new NewAction();
    saveAction = new SaveAction();
    saveAsAction = new SaveAsAction();
    openAction = new OpenAction();
    exitAction = new ExplorerExitAction();
    generateAllAction = new GenerateAllAction();

    menu.add(new JMenuItem(newAction));
    menu.addSeparator();
    menu.add(new JMenuItem(openAction));
    menu.addSeparator();
    menu.add(new JMenuItem(saveAction));
    menu.add(new JMenuItem(saveAsAction));
    menu.add(new JMenuItem(exitAction));
    menuBar.registerDocumentFunctions(newAction, openAction, null, saveAction,
        saveAsAction, exitAction);

    createGenerateMenu();
    createToolsMenu();

    menuBar.setExplorer(this);
    createNewDocument();
  }

  public void createNewDocument() {
    if (newAction != null) {
      newAction.execute();
    }
  }

  /**
   * Returns the user preferences for the MDA explorer application.
   */
  protected void getUserPreferences() {
    Preferences preferences = Preferences.userNodeForPackage(this.getClass());
    String project = preferences.get(PREFERENCES_OPENED_PROJECT + ".1", null);
  }

  private void createGenerateMenu() {
    JMenu menu = new JMenu("Generate");
    menu.setName("Generate");
    menu.add(new JMenuItem(generateAllAction));
    menu.addSeparator();

    for (Iterator i = CartridgeFactory.getInstance().getCartridges().iterator(); i
        .hasNext();) {
      menu
          .add(new JMenuItem(new GenerateCartridgeAction((Cartridge) i.next())));
    }
    ExplorerMenuBar menuBar = getMenuBar();
    menuBar.add(menu, menuBar.getMenuCount() - 1);
    menuBar.setExplorer(this);
  }

  private void createToolsMenu() {
    JMenu menu = new JMenu("Tools");
    menu.setName("Tools");
    menu.add(new ExplorerSimpleDialogAction("Preferences",
        PreferencesDialog.class));
    ExplorerMenuBar menuBar = getMenuBar();
    menuBar.add(menu, menuBar.getMenuCount() - 1);
    menuBar.setExplorer(this);
  }

  /**
   * Returns a list of all contributors to the project.
   * @return list of all contributors.
   */
  private List getContributors() {
    List contributors = new ArrayList();
    contributors.add(new Contributor("Marcel Baumann",
        "marcel.baumann@computer.org", "System Architect"));
    contributors.add(new Contributor("Alexandra Junghans",
        "junghana@student.ethz.ch", "Reviewer"));
    return contributors;
  }

  /**
   * Returns a list of all librairies used in the project.
   * @return list of used librairies.
   */
  private List getLibrairies() {
    List librairies = new ArrayList();
    librairies.add(new Library("commons-beanutils", new VersionId(1, 6, 1),
        "Apache License", "http://jakarta.apache.org/commons"));
    librairies.add(new Library("commons-collections", new VersionId(2, 1, 1),
        "Apache License", "http://jakarta.apache.org/commons"));
    librairies.add(new Library("commons-logging", new VersionId(1, 0, 4),
        "Apache License", "http://jakarta.apache.org/commons"));
    librairies.add(new Library("commons-logging-api", new VersionId(1, 0, 4),
        "Apache License", "http://jakarta.apache.org/commons"));
    librairies.add(new Library("velocity", new VersionId(1, 4, 0),
        "Apache License", "http://jakarta.apache.org/velocity"));
    librairies.add(new Library("velocity-dep", new VersionId(1, 4, 0),
        "Apache License", "http://jakarta.apache.org/velocity"));
    librairies.add(new Library("xpp3", new VersionId(1, 1, 3), "BSD License",
        "http://www.extreme.indiana.edu"));
    librairies.add(new Library("xstream", new VersionId(1, 0, 2),
        "BSD License", "http://xtream.codehaus.org"));
    librairies.add(new Library("forms", new VersionId(1, 0, 4), "Open Source",
        "www.jgoodies.com"));
    librairies.add(new Library("glazedlists", new VersionId(0, 9, 2), "LGPL",
        "www.publicobject.com/glazedlists"));
    librairies.add(new Library("jcalendar", new VersionId(1, 2, 1), "LGPL",
        "www.toedter.com/jcalendar"));
    return librairies;
  }
}