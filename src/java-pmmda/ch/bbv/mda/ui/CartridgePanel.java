/* copyright 2000-2004 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence);
 * you may not use this file except in compliance with the License.
 * $Id$
 */

package ch.bbv.mda.ui;

import java.util.Collections;

import javax.swing.JPanel;

import ch.bbv.explorer.components.TableCell;
import ch.bbv.explorer.components.TablePanel;
import ch.bbv.mda.generators.Cartridge;

/**
 * The cartridge panel displays configuration information for a specific
 * cartridge loaded in the application. The panel shows
 * <ul>
 * <li>The abbreviation of the cartridge.</li>
 * <li>The list of stereotypes defined in the cartridge and to which kind of
 * items - package, class, method, field - it can be applied to. The kind
 * of stereotype is chosen with a combo box.</li>
 * <li>The list of tagged values defined in the cartridge and their default
 * values, and the kind of items - package, class, method, field -it can be
 * applied to.</li>
 * <li>The list of cartridges it depends on.</li>
 * <li>The list of properties defined in the cartridge and their default and
 * actual values.</li>
 * </ul>
 * @author MarcelBaumann
 * @version $Revision$
 */
public class CartridgePanel extends JPanel {
  /**
   * Column names for the sterotypes of the cartridge.
   */
  public static final String[] STEREOTYPE_COLUMN_NAMES = { "name" };

  /**
   * Column names for the tagged value types of the cartridge.
   */
  public static final String[] TAGGED_VALUE_TYPE_COLUMN_NAMES = { "name",
      "default", "typename" };

  /**
   * Column names of the properties with their default and actual values defined
   * in the cartridge.
   */
  public static final String[] PROPERTY_COLUMN_NAMES = { "name", "default",
      "value" };

  /**
   * Column names of the dependent cartridges.
   */
  public static final String[] CARTRIDGE_COLUMN_NAMES = { "name", "prefix" };

  /**
   * The cartridge being displayed.
   */
  private Cartridge cartridge;

  /**
   * the table displaying the stereotypes of the cartridge.
   */
  private TablePanel stereotypes;

  /**
   * the table displaying the tagged value types of the cartridge.
   */
  private TablePanel taggedValueTypes;

  /**
   * the table displaying the properties of the cartridge.
   */
  private TablePanel properties;

  /**
   * the table displaying the cartridges of the cartridge depends on.
   */
  private TablePanel cartridges;

  /**
   * Default constructor of the class.
   */
public CartridgePanel() {
    stereotypes = new TablePanel(Collections.EMPTY_LIST, new TableCell(
        STEREOTYPE_COLUMN_NAMES));
    taggedValueTypes = new TablePanel(Collections.EMPTY_LIST, new TableCell(
        TAGGED_VALUE_TYPE_COLUMN_NAMES));
    properties = new TablePanel(Collections.EMPTY_LIST, new TableCell (
        PROPERTY_COLUMN_NAMES));
    cartridges = new TablePanel(Collections.EMPTY_LIST, new TableCell (
        CARTRIDGE_COLUMN_NAMES));
    

  }
  /**
   * Sets the new cartridge of the panel.
   * @param cartridge new cartridge of the panel.
   */
  public void setCatridge(Cartridge cartridge) {
    this.cartridge = cartridge;
    stereotypes.setModel(cartridge.getAllStereotypes());
    // taggedValueTypes.setModel(cartridge.getTaggedValueTypes());
    properties.setModel(cartridge.getAllProperties());
    cartridges.setModel(cartridge.getUsedCartridges());
  }
}