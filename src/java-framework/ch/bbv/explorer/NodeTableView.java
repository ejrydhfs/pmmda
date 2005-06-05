/* copyright 2000-2005 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence");
 * you may not use this file except in compliance with the License.
 * $Id$ 
 */
package ch.bbv.explorer;

import java.util.Collections;
import java.util.List;

import ch.bbv.explorer.components.TablePanel;

/**
 * The table view for explorer nodes. The class is generic and uses type
 * information from the framework to define the table content. The node table
 * view defines the table structure with a node table cell. A table view without
 * columns can be created to handle degenerated cases.
 * @author marcelbaumann
 * @version $Revision$
 */
public class NodeTableView extends TablePanel implements NodeView {
  /**
   * Serial version identifier coded as back compatibility date.
   */
  private static final long serialVersionUID = 20041201;

  /**
   * Node displayed by this view
   */
  private ExplorerNode node;

  /**
   * Constructor of the class.
   * @param fields fields of the view.
   */
  public NodeTableView(List<UiField> fields) {
    super(Collections.EMPTY_LIST, new NodeTableCell(fields));
  }

  /**
   * Creates an instance of the class.
   * @param type user interface type of the nodes to display.
   * @param object parameters list for the constructor.
   * @return a new instance of the class.
   */
  public static NodeTableView create(UiNodeType type, Object object) {
    return new NodeTableView((List<UiField>) object);
  }

  public void setModel(ExplorerNode node) {
    this.node = node;
    setModel(node.getChildren());
  }

  public void viewClosed() {
  }

  public void nodeInserted(ExplorerNode node, int index) {
  }

  public void nodeRemoved(ExplorerNode parent, ExplorerNode node, int index) {
    setModel(this.node);
  }

  public void nodeChanged(ExplorerNode node) {
    setModel(this.node);
  }
}