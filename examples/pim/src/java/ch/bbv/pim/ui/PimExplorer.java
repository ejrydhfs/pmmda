/* 
 * copyright 2000-2006 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence");
 * you may not use this file except in compliance with the License.
 * $Id$
 */
package ch.bbv.pim.ui;

import java.util.ArrayList;
import java.util.List;

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
import ch.bbv.explorer.ExplorerNode;
import ch.bbv.explorer.UiNodeTypeFactory;
import ch.bbv.explorer.ExplorerMenuBar.Names;
import ch.bbv.explorer.about.Contributor;
import ch.bbv.explorer.about.Licences;
import ch.bbv.explorer.actions.ExplorerAboutAction;
import ch.bbv.explorer.actions.ExplorerExitAction;
import ch.bbv.pim.AddressBook;
import ch.bbv.pim.client.TypeFactory;
import ch.bbv.pim.lw.AddressBookLw;
import ch.bbv.pim.lw.LwTransformer;
import ch.bbv.pim.ui.actions.NewAction;
import ch.bbv.pim.ui.actions.OpenAction;
import ch.bbv.pim.ui.actions.SaveAction;
import ch.bbv.pim.ui.actions.SaveAsAction;

/**
 * The PIM explorer provides an explorer user interface to manipulate the
 * organizations and contacts. The explorer provides standard actions such as
 * <dl>
 * <dt>Open</dt>
 * <dd>Opens a file containing an address book and loads the address book and
 * the contacts in the explorer.</dd>
 * <dt>Save</dt>
 * <dd>Saves the address book in the file.</dd>
 * <dt>Save As</dt>
 * <dd>Saves the address book in the file. The file name and format is inquired
 * from the user.</dd>
 * <dt>Exit</dt>
 * <dd>Exists the application. If some address books are not saved the user is
 * asked if he wants to save his modifications.</dd>
 * </dl>
 * @author Marcel Baumann
 * @version $Revision$
 */
public class PimExplorer extends Explorer {
  /**
   * Logger instance of the class.
   */
  private static Log log = LogFactory.getLog(PimExplorer.class);

  /*
   * User interface node type factory of the explorer instances.
   */
  private static UiNodeTypeFactory factory;

  /**
   * Lightweight object handler of the explorer instances.
   */
  private static DataObjectManager manager;

  /**
   * The document displayed in the explorer.
   */
  private PimDocument document;

  /**
   * Creates a new MDA explorer.
   * @param title title of the explorer frame.
   * @return the newly created user explorer.
   */
  public static PimExplorer create(String title) {
    Class[] types = { String.class };
    Object[] arguments = { title };
    PimExplorer explorer = (PimExplorer) Explorer.createAndShow(
        PimExplorer.class, types, arguments);
    return explorer;
  }

  /**
   * Registers all the node types used the explorer with their attributes and
   * relationships.
   */
  public static void registerNodeTypes() {
    manager = PimApplication.getApplication().getDataObjectManager();
    factory = new UiNodeTypeFactory();
    TypeFactory typeFactory = new TypeFactory(factory, manager);
    typeFactory.registerAllTypes();
  }

  /**
   * Constructor of the class.
   * @param title title of the explorer frame.
   */
  public PimExplorer(String title) {
    super(factory, manager);
    this.setTitle(title);
    if (log.isInfoEnabled()) {
      log.info("Start initializing PIM explorer");
    }
  }

  /**
   * Returns the document of the explorer.
   * @return the document of the explorer.
   * @see #setDocument(PimDocument)
   */
  public PimDocument getDocument() {
    return document;
  }

  /**
   * Sets the document displayed in the explorer.
   * @param document the new document displayed in the explorer.
   * @see #getDocument()
   */
  public void setDocument(PimDocument document) {
    this.document = document;
    if (document != null) {
      AddressBook book = document.getAddressBook();
      AddressBookLw bookLw = LwTransformer.transform(book);
      ExplorerNode root = createTree(bookLw, book);
      setRoot(root);
      getMenuBar().documentChanged(document);
    }
  }

  /**
   * Initializes the explorer. The initial tree of lightweight objects is
   * created.
   */
  @Override
  protected void initialize() {
    ExplorerMenuBar menuBar = new ExplorerMenuBar(this);
    setMenuBar(menuBar);

    JMenu menu = menuBar.getMenu(ExplorerMenuBar.HELP_MENU);
    ExplorerAboutAction aboutAction = new ExplorerAboutAction(this);
    aboutAction.setProjectInfo(getProjectInfo());
    menu.add(new JMenuItem(aboutAction));

    menu = menuBar.getMenu(ExplorerMenuBar.FILE_MENU);
    menuBar.addStandardAction(menu, Names.NEW, new NewAction(this));
    menuBar.addStandardAction(menu, Names.OPEN, new OpenAction(this));
    menu.addSeparator();
    menuBar.addStandardAction(menu, Names.SAVE, new SaveAction(this));
    menuBar.addStandardAction(menu, Names.SAVE_AS, new SaveAsAction(this));
    menuBar.addStandardAction(menu, Names.EXIT, new ExplorerExitAction(this));
    getMenuBar().documentChanged(null);
  }

  /**
   * Returns the project information displayed in the about box.
   * @return the project information with contributors and libraries.
   */
  private ProjectInfo getProjectInfo() {
    ProjectInfo info = new ProjectInfo();
    info.setCopyright("copyright Marcel Baumann (Apache Sofware License 2.0)");
    info.setInfo("pmmda.sourceforge.net");
    info.setLicenceName("Apache Software License 2.0");
    info.setLicenceText(Licences.LICENCE_APACHE_2_0);
    info.setName("pmMDA - poor man Model Driven Architecture -");
    info.setVersion(new VersionId(0, 1, 0, "dev"));
    info.setContributors(getContributors());
    info.setLibraries(getLibraries());
    return info;
  }

  /**
   * Returns a list of all contributors to the project.
   * @return list of all contributors.
   */
  private List<Contributor> getContributors() {
    List<Contributor> contributors = new ArrayList<Contributor>();
    contributors.add(new Contributor("Marcel Baumann",
        "marcel.baumann@computer.org", "System Architect"));
    return contributors;
  }

  @Override
  protected List<Library> getLibraries() {
    List<Library> libraries = super.getLibraries();
    libraries.add(new Library("velocity", new VersionId(1, 4, 0),
        "Apache License", "http://jakarta.apache.org/velocity"));
    libraries.add(new Library("velocity-dep", new VersionId(1, 4, 0),
        "Apache License", "http://jakarta.apache.org/velocity"));
    return libraries;
  }
}