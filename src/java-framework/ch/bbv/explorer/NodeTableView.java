/* copyright 2000-2004 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence);
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
 * view defines the table structure with a node table cell.
 * @author marcelbaumann
 * @version $Revision$
 */
public class NodeTableView extends TablePanel implements NodeView {
  /**
   * Constructor of the class.
   * @param fields fields of the view.
   */
  public NodeTableView(List fields) {
    super(Collections.EMPTY_LIST, new NodeTableCell(fields));
  }

  /**
   * Creates an instance of the class.
   * @param type user interface type of the nodes to display.
   * @param object parameters list for the constructor.
   * @return a new instance of the class.
   */
  public static NodeTableView create(UiNodeType type, Object object) {
    return new NodeTableView((List) object);
  }

  public void setModel(ExplorerNode node) {
    setModel(node.getChildren());
  }

  public void viewClosed() {
    System.out.println("viewClosed");
  }

  public void nodeInserted(ExplorerNode node, int index) {
    System.out.println("nodeInserted");
  }

  public void nodeRemoved(ExplorerNode parent, ExplorerNode node, int index) {
    System.out.println("nodeRemoved");
  }

  public void nodeChanged(ExplorerNode node) {
    System.out.println("nodeChanged");
  }

}