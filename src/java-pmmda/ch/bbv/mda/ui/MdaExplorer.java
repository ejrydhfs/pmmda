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
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import org.apache.commons.cli.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.flexdock.docking.Dockable;
import org.flexdock.docking.DockingConstants;
import org.flexdock.docking.DockingManager;

import ch.bbv.application.Library;
import ch.bbv.application.ProjectInfo;
import ch.bbv.application.VersionId;
import ch.bbv.cmdlineparser.CmdLineBuilder;
import ch.bbv.dog.DataObjectManager;
import ch.bbv.explorer.Explorer;
import ch.bbv.explorer.ExplorerDocument.State;
import ch.bbv.explorer.ExplorerMenuBar;
import ch.bbv.explorer.ExplorerUtilities;
import ch.bbv.explorer.UiNodeTypeFactory;
import ch.bbv.explorer.ExplorerMenuBar.Names;
import ch.bbv.explorer.about.Contributor;
import ch.bbv.explorer.about.Licences;
import ch.bbv.explorer.actions.*;
import ch.bbv.explorer.components.ValidationPanel;
import ch.bbv.mda.MetaModel;
import ch.bbv.mda.generators.Cartridge;
import ch.bbv.mda.generators.CartridgeFactory;
import ch.bbv.mda.ui.actions.*;
import ch.bbv.utilities.ResourceUtilities;

/**
 * The MDA explorer provides a user interface to view and manipulate model
 * driven architecture models. All the editing functions are provided through
 * the explorer user interface framework.
 * @author Marcel Baumann
 * @version $Revision$
 */
public class MdaExplorer extends Explorer {

  /**
   * key to infer the set of project lately opened.
   */
  public static final String PREFERENCES_OPENED_PROJECT = "mda.opened-project";

  /**
   * The name of the validation panel.
   */
  public static final String VALIDATION_PANEL_NAME = "Validator";

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

  /**
   * The document edited in the explorer component.
   */
  private MdaDocument document;

  /**
   * The command line arguments of the application.
   */
  private String[] arguments;

  private ExplorerGlobalAction generateAllAction;

  private ValidationPanel validationPanel;

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
    validationPanel.clear();
    getMenuBar().documentChanged(document.getState());
  }

  /**
   * Returns the validation panel of the explorer.
   * @return the requested validation panel.
   */
  public ValidationPanel getValidationPanel() {
    return validationPanel;
  }

  /**
   * Initializes the pmmda explorer.
   */
  @Override
  protected void initialize() {
    new ResourceUtilities("ch.bbv.mda.ui.Messages", Locale.US);
    ExplorerMenuBar menuBar = new ExplorerMenuBar(this);
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
    validationPanel = new ValidationPanel();
    validationPanel.setName(VALIDATION_PANEL_NAME);
    Dockable dockable = DockingManager.registerDockable(validationPanel,
        validationPanel.getName());
    DockingManager.dock(dockable, getUtilitiesPort(),
        DockingConstants.CENTER_REGION);

    JMenu menu = menuBar.getMenu(ExplorerMenuBar.HELP_MENU);
    ExplorerAboutAction aboutAction = new ExplorerAboutAction(this);
    aboutAction.setProjectInfo(projectInfo);
    menu.add(new JMenuItem(aboutAction));

    menu = menuBar.getMenu(ExplorerMenuBar.FILE_MENU);
    addAction(menuBar, menu, Names.NEW, new NewAction(this));
    menu.addSeparator();
    addAction(menuBar, menu, Names.OPEN, new OpenAction(this));
    menu.addSeparator();
    addAction(menuBar, menu, Names.SAVE, new SaveAction(this));
    addAction(menuBar, menu, Names.SAVE_AS, new SaveAsAction(this));
    addAction(menuBar, menu, Names.EXIT, new ExplorerExitAction(this));
    generateAllAction = new GenerateAllAction(this);
    createGenerateMenu();
    createToolsMenu();
    processCommandLine();
    getFrame().addWindowListener(new MyWindowAdapter(this));
  }

  /**
   * Adds an action to the given menu.
   * @param menuBar menu bar owning the menu.
   * @param menu menu to which the action should be added.
   * @param name name of the action as defined in the menu bar.
   * @param action action to add.
   */
  protected void addAction(ExplorerMenuBar menuBar, JMenu menu, Names name,
      ExplorerGlobalAction action) {
    menu.add(new JMenuItem(action));
    menuBar.putStandardAction(name, action);
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
    ExplorerMenuBar menuBar = getMenuBar();
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
      boolean success = false;
      if (line.hasOption("m")) {
        String filename = line.getOptionValue("m");
        if (filename != null) {
          ExplorerFileAction action = (ExplorerFileAction) menuBar
              .getStandardAction(Names.OPEN);
          action.setSelectedFile(new File(filename));
          success = action.execute();
        } else {
          success = menuBar.getStandardAction(Names.NEW).execute();
        }
      }
      if (!success) {
        menuBar.getStandardAction(Names.NEW).execute();
        this.getDocument().setState(State.CLEAN);
      }
    } catch (ParseException e) {
      log.error("parse arguments exception", e);
      menuBar.getStandardAction(Names.NEW).execute();
      this.getDocument().setState(State.CLEAN);
    }
  }

  /**
   * Creates the dynamic menu containing all configured cartridges.
   */
  private void createGenerateMenu() {
    JMenu menu = new JMenu("Generate");
    menu.setName("Generate");
    menu.add(new JMenuItem(generateAllAction));
    menu.addSeparator();

    for (Cartridge cartridge : CartridgeFactory.getInstance().getCartridges()) {
      menu.add(new JMenuItem(new GenerateCartridgeAction(cartridge, this)));
    }
    ExplorerMenuBar menuBar = getMenuBar();
    menuBar.add(menu, menuBar.getMenuCount() - 1);
  }

  private void createToolsMenu() {
    JMenu menu = new JMenu("Tools");
    menu.setName("Tools");
    menu.add(new ExplorerSimpleDialogAction("Preferences",
        PreferencesDialog.class, this));
    ExplorerMenuBar menuBar = getMenuBar();
    menuBar.add(menu, menuBar.getMenuCount() - 1);
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
    librairies.add(new Library("glazedlists", new VersionId(0, 9, 7), "LGPL",
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
  
  private class MyWindowAdapter extends WindowAdapter {
    
    private Explorer explorer = null;
    
    public MyWindowAdapter(Explorer explorer) {
      this.explorer = explorer;
    }
    
    public void windowClosing(WindowEvent e) {
      super.windowClosing(e);
      ExplorerGlobalAction action = explorer.getMenuBar().getStandardAction(Names.EXIT);
      action.execute();
    }
    
  }
}