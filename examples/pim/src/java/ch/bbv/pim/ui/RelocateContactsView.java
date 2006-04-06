/* copyright 2000-2006 Marcel Baumann 
 * Licensed under the Apache License, Version 2.0 (the "Licence"); 
 * you may not use this file except in compliance with the License. 
 * $Id$ 
 */
package ch.bbv.pim.ui;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTree;

import ch.bbv.dog.DataObjectManager;
import ch.bbv.explorer.*;

/**
 * The view allows the relocation of contact to a selected organization. The
 * logic removes the contact from the current organization, adds the contact to
 * the new organization and adds a note to the history to provide an audit trail
 * to the operation. The view provides a left panel with a tree of all loaded
 * contacts and a right panel with a tree of the selected organization and its
 * contacts. Buttons are provided to select the target organizationa and to move
 * contact to it.
 * @author MarcelBaumann
 * @version $Revision$
 */
public class RelocateContactsView extends JPanel implements ExplorerNodeOwner {

  /**
   * Serial version identifier of the class.
   */
  private static final long serialVersionUID = -8153917542904210072L;

  private ExplorerTree tree;

  /**
   * Data object manager of this owner.
   */
  private DataObjectManager manager;

  /**
   * User interface node type factory of this owner.
   */
  private UiNodeTypeFactory factory;

  /**
   * Scroll pane containing the complete tree.
   */
  private JScrollPane treePane;

  /**
   * The name of the selected organization.
   */
  private JTextField organization;

  /**
   * Scroll pane containing the list of contacts.
   */
  private JScrollPane contactsPane;

  /**
   * Constructor of the class.
   * @param explorer explorer in which the view is created
   */
  public RelocateContactsView(Explorer explorer) {
    this(explorer.getRoot().clone(), explorer.getManager(), explorer
        .getNodeTypeFactory());
  }

  /**
   * Constructor of the class.
   * @param root root of the tree
   * @param manager data object manager of this owner
   * @param factory user interface node type factory of this owner
   */
  public RelocateContactsView(ExplorerNode root, DataObjectManager manager,
      UiNodeTypeFactory factory) {
    assert (root != null) && (manager != null) && (factory != null);
    this.manager = manager;
    this.factory = factory;
    tree = new ExplorerTree();
    tree.setRoot(root, this);
    treePane = new JScrollPane(tree.getTree());
    add(treePane);
    contactsPane = new JScrollPane();
    add(contactsPane);
  }

  public DataObjectManager getManager() {
    return manager;
  }

  public UiNodeTypeFactory getNodeTypeFactory() {
    return factory;
  }

  public JTree getTree() {
    return tree.getTree();
  }

  public void nodeChanged(ExplorerNode node) {
  }

  public void nodeInserted(ExplorerNode node, int index) {
  }

  public void nodeRemoved(ExplorerNode parent, ExplorerNode node, int index) {
  }
}
