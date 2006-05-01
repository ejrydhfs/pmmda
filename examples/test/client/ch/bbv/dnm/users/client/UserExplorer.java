/* 
 * copyright 2000-2006 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence");
 * you may not use this file except in compliance with the License.
 * $Id$
 */
package ch.bbv.dnm.users.client;

import java.util.Iterator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ch.bbv.application.Application;
import ch.bbv.dnm.users.Realm;
import ch.bbv.dnm.users.lw.ProviderLw;
import ch.bbv.dnm.users.lw.RealmLw;
import ch.bbv.dog.DataObjectId;
import ch.bbv.dog.DataObjectMgr;
import ch.bbv.explorer.*;

/**
 * The user explorer provides an explorer user interface to manipulate the users
 * roles for Internet providers. The defined information is used to configure
 * the rights of a Tomcat server. The user explorer uses the explorer framework
 * and the artifacts generated with the pmMDA tool to implement a complete user
 * interface to visualize and manipulate user roles with only one hand written
 * class.
 * @author Marcel Baumann
 * @version $Revision$
 */
public class UserExplorer extends Explorer {
  /**
   * Logger instance of the class.
   */
  private static Log log = LogFactory.getLog(UserExplorer.class);

  /*
   * User interface node type factory of the explorer instances.
   */
  private static UiNodeTypeFactory factory;

  /**
   * Lightweight object handler of the explorer instances.
   */
  private static DataObjectMgr manager;

  /**
   * The document displayed in the explorer.
   */
  private ExplorerDocument document;

  /**
   * Creates a new MDA explorer.
   * @param title title of the explorer frame.
   * @return the newly created user explorer.
   */
  public static UserExplorer create(String title) {
    Class[] types = { String.class };
    Object[] arguments = { title };
    UserExplorer explorer = (UserExplorer) Explorer.createAndShow(
        UserExplorer.class, types, arguments);
    return explorer;
  }

  /**
   * Constructor of the class.
   * @param title title of the explorer frame.
   */
  public UserExplorer(String title) {
    super(factory, manager);
    this.setTitle(title);
    document = new ExplorerDocumentImpl();
    if (log.isInfoEnabled()) {
      log.info("Start initializing user explorer");
    }
  }

  /**
   * Registers all the node types used the explorer.
   */
  public static void registerNodeTypes() {
    manager = Application.getApplication().getDataObjectMgr();
    factory = new UiNodeTypeFactory();
    TypeFactory typeFactory = new TypeFactory(factory, manager);
    typeFactory.registerAllTypes();
  }

  /**
   * Returns the document of the explorer.
   * @return the document of the explorer.
   */
  public ExplorerDocument getDocument() {
    return document;
  }

  /**
   * Initializes the explorer. The initial tree of lightweight objects is
   * created.
   */
  protected void initialize() {
    ExplorerMenuBar menuBar = new ExplorerMenuBar(this);
    setMenuBar(menuBar);
    Realm realm = new Realm();
    realm.setId(DataObjectId.ILLEGAL_ID);
    realm.setName("Realm");
    RealmLw realmLw = new RealmLw(DataObjectId.ILLEGAL_ID);
    realmLw.setName("Realm");
    for (Iterator i = manager.getDataObjectHandler().retrieveLw(
        getManager().getDatasource(), ProviderLw.class, (String) null)
        .iterator(); i.hasNext();) {
      realmLw.addProvider((ProviderLw) i.next());
    }
    ExplorerNode root = createTree(realmLw);
    root.setDataObject(realm);
    setRoot(root);
  }
}