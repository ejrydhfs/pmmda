/* copyright 2000-2005 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence");
 * you may not use this file except in compliance with the License.
 * $Id$
 */
package ch.bbv.mda.ui;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.prefs.Preferences;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

import org.apache.commons.cli.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ch.bbv.application.Library;
import ch.bbv.application.ProjectInfo;
import ch.bbv.application.VersionId;
import ch.bbv.cmdlineparser.CmdLineBuilder;
import ch.bbv.dog.DataObjectManager;
import ch.bbv.explorer.Explorer;
import ch.bbv.explorer.ExplorerMenuBar;
import ch.bbv.explorer.ExplorerUtilities;
import ch.bbv.explorer.UiNodeTypeFactory;
import ch.bbv.explorer.about.Contributor;
import ch.bbv.explorer.about.Licences;
import ch.bbv.explorer.actions.ExplorerAboutAction;
import ch.bbv.explorer.actions.ExplorerExitAction;
import ch.bbv.explorer.actions.ExplorerGlobalAction;
import ch.bbv.explorer.actions.ExplorerSimpleDialogAction;
import ch.bbv.explorer.components.DockingArea;
import ch.bbv.explorer.components.ValidationPanel;
import ch.bbv.mda.MetaModel;
import ch.bbv.mda.generators.Cartridge;
import ch.bbv.mda.generators.CartridgeFactory;
import ch.bbv.mda.persistence.PersistenceException;
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

  private MdaDocument document;

  private String[] arguments;

  private ExplorerGlobalAction newAction;

  private ExplorerGlobalAction saveAction;

  private ExplorerGlobalAction saveAsAction;

  private ExplorerGlobalAction exitAction;

  private OpenAction openAction;

  private ExplorerGlobalAction generateAllAction;

  /**
   * Creates a new MDA explorer.
   * @param args command line arguments.
   * @return the newly created MDA explorer.
   */
  public static MdaExplorer create(String[] args) {
    Class[] types = { String[].class };
    Object[] arguments = { args };
    MdaExplorer explorer = (MdaExplorer) Explorer.createAndShow(
        MdaExplorer.class, types, arguments);
    ExplorerUtilities.centerOnScreen(explorer.getFrame());
    return explorer;
  }

  /**
   * Constructor of the class.
   * @param args arguments of the command line.
   */
  public MdaExplorer(String[] args) {
    super(factory, manager, null);
    arguments = args;
    if (log.isInfoEnabled()) {
      log.info("Start initializing MDA explorer");
    }
    setTitle("pmMDA Explorer");
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
   * Returns the document displayed in the explorer.
   * @return the document displayed in the explorer.
   * @see #setDocument(MdaDocument)
   */
  public MdaDocument getDocument() {
    return document;
  }

  /**
   * Sets the document displayed in the explorer.
   * @param document the new document displayed in the explorer.
   * @see #getDocument()
   */
  public void setDocument(MdaDocument document) {
    assert (document != null);
    this.document = document;
    MetaModel model = document.getModel();
    TreeConstructor constructor = new TreeConstructor(getNodeTypeFactory());
    model.accept(constructor);
    setRoot(constructor.getRoot());
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
    projectInfo.setVersion(new VersionId(0, 3, 0, "dev"));

    projectInfo.setContributors(getContributors());
    projectInfo.setLibraries(getLibrairies());

    DockingArea dockingArea = getDockingArea();
    dockingArea.addTab("Validator", new ValidationPanel());

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
    processCommandLine();
  }

  /**
   * Returns the user preferences for the MDA explorer application. The values
   * are stored under the package name and are updated each time a project is
   * loaded.
   */
  protected void getUserPreferences() {
    Preferences preferences = Preferences.userNodeForPackage(this.getClass());
    String project = preferences.get(PREFERENCES_OPENED_PROJECT + ".1", null);
  }

  /**
   * Processes the command line and executes the defined actions.
   */
  private void processCommandLine() {
    Options options = new Options();
    OptionGroup group = new OptionGroup();
    options.addOption(CmdLineBuilder.createNoArgOption("h", "help",
        "display the list of available commands", false));
    group.addOption(CmdLineBuilder.createNoArgOption("v", "verbose",
        "set the logging level to verbose", false));
    group.addOption(CmdLineBuilder.createNoArgOption("q", "quiet",
        "set the logging level to quiet", false));
    options.addOptionGroup(group);
    options.addOption(CmdLineBuilder
        .createArgOption("m", "model",
            "loads the UML model from the given file", "model", false,
            String.class));
    options.addOption(CmdLineBuilder.createJavaProperty());
    Parser parser = new PosixParser();
    try {
      CommandLine line = parser.parse(options, arguments);
      if (line.hasOption("h")) {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("pmMDA", options);
      }
      if (line.hasOption("v")) {
        log.info("logging level set to verbose");
      }
      if (line.hasOption("q")) {
        log.info("logging level set to quiet");
      }
      if (line.hasOption("m")) {
        String filename = line.getOptionValue("m");
        if (filename != null) {
          File file = new File(filename);
          openAction.openModel(file);
        } else {
          newAction.execute();
        }
      } else {
        newAction.execute();
      }
    } catch (PersistenceException e) {
      log.error("parse arguments exception", e);
      newAction.execute();
    } catch (ParseException e) {
      log.error("parse arguments exception", e);
      newAction.execute();
    }
  }

  private void createGenerateMenu() {
    JMenu menu = new JMenu("Generate");
    menu.setName("Generate");
    menu.add(new JMenuItem(generateAllAction));
    menu.addSeparator();

    for (Cartridge cartridge : CartridgeFactory.getInstance().getCartridges()) {
      menu.add(new JMenuItem(new GenerateCartridgeAction(cartridge)));
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
  private List<Contributor> getContributors() {
    List<Contributor> contributors = new ArrayList<Contributor>();
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
  private List<Library> getLibrairies() {
    List<Library> librairies = new ArrayList<Library>();
    librairies.add(new Library("commons-beanutils", new VersionId(1, 6, 1),
        "Apache License", "http://jakarta.apache.org/commons"));
    librairies.add(new Library("commons-collections", new VersionId(2, 1, 1),
        "Apache License", "http://jakarta.apache.org/commons"));
    librairies.add(new Library("commons-logging", new VersionId(1, 0, 4),
        "Apache License", "http://jakarta.apache.org/commons"));
    librairies.add(new Library("commons-logging-api", new VersionId(1, 0, 4),
        "Apache License", "http://jakarta.apache.org/commons"));
    librairies.add(new Library("forms", new VersionId(1, 0, 5), "Open Source",
        "www.jgoodies.com"));
    librairies.add(new Library("glazedlists", new VersionId(0, 9, 6), "LGPL",
        "www.publicobject.com/glazedlists"));
    librairies.add(new Library("jgraph", new VersionId(5, 4, 5), "LGPL/BSD",
        "www.jgraph.com"));
    librairies.add(new Library("jgraphaddons", new VersionId(1, 0, 7),
        "LGPL/BSD", "www.jgraph.com"));
    librairies.add(new Library("jcalendar", new VersionId(1, 2, 1), "LGPL",
        "www.toedter.com/jcalendar"));
    librairies.add(new Library("velocity", new VersionId(1, 4, 0),
        "Apache License", "http://jakarta.apache.org/velocity"));
    librairies.add(new Library("velocity-dep", new VersionId(1, 4, 0),
        "Apache License", "http://jakarta.apache.org/velocity"));
    librairies.add(new Library("xpp3", new VersionId(1, 1, 3), "BSD License",
        "http://www.extreme.indiana.edu"));
    librairies.add(new Library("xstream", new VersionId(1, 1, 2),
        "BSD License", "http://xtream.codehaus.org"));
    return librairies;
  }
}