/* copyright 2000-2006 Marcel Baumann 
 * Licensed under the Apache License, Version 2.0 (the "Licence"); 
 * you may not use this file except in compliance with the License. 
 * $Id$ 
 */
package ch.bbv.mda.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.Collections;
import java.util.Date;

import javax.swing.AbstractAction;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ch.bbv.dog.ui.ReferenceCodeComboBox;
import ch.bbv.explorer.ExplorerNode;
import ch.bbv.explorer.NodeView;
import ch.bbv.explorer.UiNodeType;
import ch.bbv.explorer.components.TableCell;
import ch.bbv.explorer.components.TablePanel;
import ch.bbv.mda.MetaClass;
import ch.bbv.mda.VisibilityCode;
import ch.bbv.mda.cartridges.BoConstants;
import ch.bbv.mda.pim.CodeType;
import ch.bbv.mda.pim.CodeTypeMgr;
import ch.bbv.mda.pim.CodeValue;
import ch.bbv.mda.pim.StorageKind;

import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.FormLayout;

/**
 * The reference code values view displays all code values of a reference code
 * type. The code values are displayed as a list of values. If the class is not
 * a reference code the fields of the view are cleared and the popup menu is
 * disabled. The view supports a popup menu with the following actions.
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
 * </dl>
 * The actions of the menu have a reference to the code view.
 * @author MarcelBaumann
 * @version $Revision$
 */
public class CodesView extends JPanel implements NodeView {
  /**
   * Serial version identifier coded as back compatibility date.
   */
  private static final long serialVersionUID = 20041201;

  /**
   * The size of a text field in the view.
   */
  private final static int TEXT_FIELD_SIZE = 20;

  /**
   * Cells of the columns of the code view tuples.
   */
  private static final TableCell.Cell[] CELLS;

  /**
   * Swing component to display the associated property.
   */
  private JTextField nameText;

  /**
   * Swing component to display the associated property.
   */
  private JCheckBox hierarchical;

  /**
   * Swing component to display the associated property.
   */
  private ReferenceCodeComboBox storageKind;

  private TablePanel tablePanel;

  /**
   * Actions available through the popup menu of the view.
   * @author marcelbaumann
   * @version $Revision$
   */
  private enum Actions {
    ADD, REMOVE, RELOAD, STORE
  };

  /**
   * Popup menu action of the view.
   * @author marcelbaumann
   * @version $Revision$
   */
  private static class CodesViewAction extends AbstractAction {
    /**
     * Serial version identifier coded as back compatibility date.
     */
    private static final long serialVersionUID = 20041201;

    private static final String ACTION_KEY = "ch.bbv.mda.ui.CodesView.Actions";

    /**
     * The view the action should process.
     */
    private CodesView codeView;

    /**
     * Constructor of the class.
     * @param action action kind of the action.
     * @param codeView the code view on which the action will work on.
     */
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

    @Override
    public void mousePressed(MouseEvent event) {
      if (event.isPopupTrigger()) {
        menu.show((Component) event.getSource(), event.getX(), event.getY());
      }
    }

    @Override
    public void mouseReleased(MouseEvent event) {
      if (event.isPopupTrigger()) {
        menu.show((Component) event.getSource(), event.getX(), event.getY());
      }
    }
  }

  /**
   * Handles table selections of the users for various operations.
   * @author MarcelBaumann
   * @version $Revision$
   */
  private static class CodesListener implements ListSelectionListener {
    /**
     * The currently selected row.
     */
    private int selectedRow;

    /**
     * Default constructor of the class.
     */
    public CodesListener() {
      selectedRow = -1;
    }

    /**
     * Returns the selected row in the table.
     * @return the selected row if defined otherwise -1.
     */
    public int getSelectedRow() {
      return selectedRow;
    }

    public void valueChanged(ListSelectionEvent event) {
      if (event.getValueIsAdjusting()) {
        return;
      }
      ListSelectionModel model = (ListSelectionModel) event.getSource();
      if (model.isSelectionEmpty()) {
        selectedRow = -1;
      } else {
        selectedRow = model.getMinSelectionIndex();
        // TODO selected row
      }
    }
  }

  static {
    CELLS = new TableCell.Cell[7];
    CELLS[0] = new TableCell.Cell("codeId", "id", Integer.TYPE, true);
    CELLS[1] = new TableCell.Cell("name", "name", String.class, true);
    CELLS[2] = new TableCell.Cell("sortOrder", "order", Integer.TYPE, true);
    CELLS[3] = new TableCell.Cell("shortDescription", "short description",
        String.class, true);
    CELLS[4] = new TableCell.Cell("description", "description", String.class,
        true);
    CELLS[5] = new TableCell.Cell("start", "start date", Date.class, true);
    CELLS[6] = new TableCell.Cell("end", "end date", Date.class, true);
  }

  /**
   * Logger for all instances of the class.
   */
  private static Log log = LogFactory.getLog(CodesView.class);

  /**
   * Node which reference code are displayed.
   */
  private ExplorerNode node;

  /**
   * The code type associated with the MDA class having a stereotype identifying
   * it as reference code.
   */
  private CodeType type;

  /**
   * Popup menu of the view. The menu contains the actions available in the
   * view.
   */
  private JPopupMenu menu;

  /**
   * Listener handling table row selections.
   */
  private CodesListener codesListener;

  /**
   * Name of the view.
   */
  private String name;

  /**
   * Creates an instance of the class.
   * @param type user interface type of the nodes to display
   * @param object parameters list for the constructor
   * @return a new instance of the class
   * @pre type != null
   */
  public static CodesView create(UiNodeType type, Object object) {
    assert type != null;
    return new CodesView();
  }

  /**
   * Constructor of the class. All listeners are hooked on the visual
   * components.
   */
  public CodesView() {
    super(new BorderLayout());
    nameText = new JTextField(TEXT_FIELD_SIZE);
    nameText.setEditable(false);
    hierarchical = new JCheckBox();
    storageKind = new ReferenceCodeComboBox(VisibilityCode.class, StorageKind
        .getCodes());
    tablePanel = new TablePanel(Collections.EMPTY_LIST, new TableCell(CELLS));
    initialize();
    tablePanel.getScrollPane().addMouseListener(new PopupListener(menu));
    tablePanel.getTable().addMouseListener(new PopupListener(menu));
    tablePanel.getTable().setSelectionMode(
        ListSelectionModel.SINGLE_INTERVAL_SELECTION);
    codesListener = new CodesListener();
    tablePanel.getTable().getSelectionModel().addListSelectionListener(
        codesListener);
  }

  /**
   * Returns the name of the view.
   * @return the name of the view
   * @see #setName(String)
   */
  public String getName() {
    return name;
  }

  /**
   * Sets the name of the view.
   * @param name name of the view
   * @pre #getName()
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Adds a new code value in the list of codes.
   */
  public void add() {
    CodeValue value = new CodeValue();
    type.addValue(value);
    tablePanel.getList().add(value);
  }

  /**
   * Removes the selected code values in the list of codes.
   */
  public void remove() {
    int selectedRow = codesListener.getSelectedRow();
    if (selectedRow != -1) {
      CodeValue value = type.getValue(selectedRow);
      type.removeValue(value);
      tablePanel.getList().remove(value);
    }
  }

  /**
   * Stores the code values in the persistent store.
   */
  public void store() {
    MetaClass clazz = (MetaClass) node.getDataObject();
    try {
      type.setHierarchical(hierarchical.isSelected());
      type.setStorageKind((StorageKind) storageKind.getSelectedCode());
      getCodeTypeMgr().save(clazz);
    } catch (IOException e) {
      log.error(e);
    }
  }

  /**
   * Loads the code values from the persistent store.
   */
  public void load() {
    MetaClass clazz = (MetaClass) node.getDataObject();
    CodeTypeMgr mgr = getCodeTypeMgr();
    try {
      type = mgr.retrieve(clazz);
      type.setQualifiedName(clazz.getQualifiedName());
      nameText.setText(type.getQualifiedName());
      hierarchical.setSelected(type.isHierarchical());
      storageKind.setSelectedCode(type.getStorageKind());
      tablePanel.setModel(type.getValues());
    } catch (IOException e) {
      log.error(e);
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
    tablePanel.setModel(type.getValues());
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
    if (clazz.hasStereotype(BoConstants.REFERENCE_CODE_STEREOTYPE)) {
      load();
      setMenuEnabled(true);
    } else {
      nameText.setText(null);
      hierarchical.setSelected(false);
      storageKind.setSelectedCode(null);
      tablePanel.setModel(Collections.emptyList());
      setMenuEnabled(false);
    }
  }

  public void viewClosed() {
  }

  /**
   * Returns the code type manager of the MDA model.
   * @return the code type manager of the MDA model
   */
  private CodeTypeMgr getCodeTypeMgr() {
    return ((MdaExplorer) node.getNodeOwner()).getDocument().getModel()
        .getCodeTypeMgr();
  }

  /**
   * Enables the popup menu of the view.
   * @param enabled flag indicating if the menu is enabled or not
   */
  private void setMenuEnabled(boolean enabled) {
    menu.setEnabled(enabled);
    for (Component component : menu.getComponents()) {
      component.setEnabled(enabled);
    }
  }

  /**
   * Initializes the fields of the class.
   */
  private void initialize() {
    menu = new JPopupMenu();
    menu.add(new CodesViewAction(Actions.ADD, this));
    menu.add(new CodesViewAction(Actions.REMOVE, this));
    menu.addSeparator();
    menu.add(new CodesViewAction(Actions.RELOAD, this));
    menu.add(new CodesViewAction(Actions.STORE, this));
    FormLayout layout = new FormLayout(
        "right:pref, 3dlu, pref, 7dlu, right:pref, 3dlu, pref", "");
    DefaultFormBuilder builder = new DefaultFormBuilder(layout);
    builder.setDefaultDialogBorder();
    builder.append("&Name", nameText);
    builder.nextLine();
    builder.append("&Hierarchical", hierarchical);
    builder.append("&Storage Kind", storageKind);
    builder.appendSeparator("Code Values");
    builder.append(tablePanel, 7);
    JPanel panel = builder.getPanel();
    add(panel, BorderLayout.CENTER);
  }
}
