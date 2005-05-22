/* copyright 2000-2005 Marcel Baumann 
 * Licensed under the Apache License, Version 2.0 (the "Licence"); 
 * you may not use this file except in compliance with the License. 
 * $Id$ 
 */
package ch.bbv.mda.ui;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.Collections;
import java.util.Date;

import javax.swing.AbstractAction;
import javax.swing.JPopupMenu;

import ch.bbv.explorer.ExplorerNode;
import ch.bbv.explorer.NodeView;
import ch.bbv.explorer.UiNodeType;
import ch.bbv.explorer.components.TableCell;
import ch.bbv.explorer.components.TablePanel;
import ch.bbv.mda.MetaClass;
import ch.bbv.mda.generators.DogCartridge;
import ch.bbv.mda.pim.CodeType;
import ch.bbv.mda.pim.CodeTypeMgr;
import ch.bbv.mda.pim.CodeValue;

/**
 * The reference code values view displays all code values of a reference code
 * type. The code values are displayed as a list of values. The view supports a
 * popup menu with the following actions.
 * <dl>
 * <dt>Insert Row</dt>
 * <dd>Inserts a new row at the end of the table. The items can be sorted using
 * the provided ordering functions. The modification is in memory.</dd>
 * <dt>Delete Rows</dt>
 * <dd>Deletes the selected rows. The modification is in memory.</dd>
 * <dt>Save</dt>
 * <dd>Saves all code values of the code types to the persistent store.</dd>
 * <dt>Load</dt>
 * <dd>Loads the code values from the persistent store. All in memory changes
 * are lost.</dd>
 * </ul>
 * The actions of the menu have a reference to the code view.
 * @author MarcelBaumann
 * @version $Revision$
 */
public class CodesView extends TablePanel implements NodeView {
  /**
   * Serial version identifier coded as back compatibility date.
   */
  private static final long serialVersionUID = 20041201;

  /**
   * Cells of the columns of the code view tuples.
   */
  private static final TableCell.Cell[] CELLS;

  private enum Actions {
    ADD, REMOVE, RELOAD, STORE
  };

  private static class CodesViewAction extends AbstractAction {
    /**
     * Serial version identifier coded as back compatibility date.
     */
    private static final long serialVersionUID = 20041201;

    private static final String ACTION_KEY = "ch.bbv.mda.ui.CodesView.Actions";

    private CodesView codeView;

    public CodesViewAction(Actions action, CodesView codeView) {
      super(action.toString());
      putValue(ACTION_KEY, action);
      this.codeView = codeView;
    }

    public void actionPerformed(ActionEvent event) {
      Actions action = (Actions) getValue(ACTION_KEY);
      switch (action) {
      case ADD:
        codeView.add();
        break;
      case REMOVE:
        codeView.remove();
        break;
      case RELOAD:
        codeView.reload();
        break;
      case STORE:
        codeView.store();
        break;
      }
    }
  }

  /**
   * Handles the mouse events for the component.
   * @author Marcel Baumann
   * @version $Revision$
   */
  private static class PopupListener extends MouseAdapter {
    /**
     * Popup menu to display.
     */
    private JPopupMenu menu;

    /**
     * Constructor of the class.
     * @param menu popup menu to display.
     */
    public PopupListener(JPopupMenu menu) {
      this.menu = menu;
    }

    /**
     * Checks if the mouse event should activate the display of the popup menu
     * upon a mouse clicked. If yes displays the popup menu.
     * @param event mouse event to handle.
     */
    @Override
    public void mouseReleased(MouseEvent event) {
      if (event.isPopupTrigger()) {
        menu.show((Component) event.getSource(), event.getX(), event.getY());
      }
    }
  }

  static {
    CELLS = new TableCell.Cell[7];
    CELLS[0] = new TableCell.Cell("name", "name", String.class, true);
    CELLS[1] = new TableCell.Cell("id", "id", String.class, true);
    CELLS[2] = new TableCell.Cell("order", "order", String.class, true);
    CELLS[3] = new TableCell.Cell("short description", "shortDescription",
        String.class, true);
    CELLS[4] = new TableCell.Cell("description", "description", String.class,
        true);
    CELLS[5] = new TableCell.Cell("start date", "startDate", Date.class, true);
    CELLS[6] = new TableCell.Cell("end date", "endDate", Date.class, true);
  }

  /**
   * Node which reference code are displayed.
   */
  private ExplorerNode node;

  private CodeType type;

  /**
   * Creates an instance of the class.
   * @param type user interface type of the nodes to display.
   * @param object parameters list for the constructor.
   * @return a new instance of the class.
   * @pre type != null
   */
  public static CodesView create(UiNodeType type, Object object) {
    return new CodesView();
  }

  /**
   * Constructor of the class.
   */
  public CodesView() {
    super(Collections.EMPTY_LIST, new TableCell(CELLS));
    getScrollPane().addMouseListener(new PopupListener(create()));
  }

  /**
   * Adds a new code value in the list of codes.
   */
  public void add() {
    type.addValue(new CodeValue());
  }

  /**
   * Removes the selected code values in the list of codes.
   */
  public void remove() {
  }

  /**
   * Stores the code values in the persistent store.
   */
  public void store() {
    MetaClass clazz = (MetaClass) node.getDataObject();
    try {
      getCodeTypeMgr().save(clazz);
    } catch (IOException e) {
      // TODO
    }
  }

  /**
   * Loads the code values from the persistent store.
   */
  public void load() {
    MetaClass clazz = (MetaClass) node.getDataObject();
    CodeTypeMgr mgr = getCodeTypeMgr();
    mgr.removeType(clazz);
    try {
      type = mgr.retrieve(clazz);
    } catch (IOException e) {
      // TODO
    }
  }

  /**
   * Loads the code values from the persistent store.
   */
  public void reload() {
    MetaClass clazz = (MetaClass) node.getDataObject();
    CodeTypeMgr mgr = getCodeTypeMgr();
    mgr.removeType(clazz);
    load();
  }

  public void nodeChanged(ExplorerNode node) {
  }

  public void nodeInserted(ExplorerNode node, int index) {
  }

  public void nodeRemoved(ExplorerNode parent, ExplorerNode node, int index) {
  }

  public void setModel(ExplorerNode node) {
    this.node = node;
    MetaClass clazz = (MetaClass) node.getDataObject();
    if (DogCartridge.REFERENCE_CODE_STEREOTYPE.equals(clazz.getStereotype())) {
       load();
       // TODO synchronize tagged values with code values
       setModel(type.getValues());
    }
  }

  public void viewClosed() {
  }

  private CodeTypeMgr getCodeTypeMgr() {
    return ((MdaExplorer) node.getExplorer()).getDocument().getCodeTypeMgr();
  }

  private JPopupMenu create() {
    JPopupMenu menu = new JPopupMenu();
    menu.add(new CodesViewAction(Actions.ADD, this));
    menu.add(new CodesViewAction(Actions.REMOVE, this));
    menu.addSeparator();
    menu.add(new CodesViewAction(Actions.RELOAD, this));
    menu.add(new CodesViewAction(Actions.STORE, this));
    return menu;
  }
}
