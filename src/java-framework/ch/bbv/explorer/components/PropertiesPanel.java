/* copyright 2000-2004 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence");
 * you may not use this file except in compliance with the License.
 * $Id$
 */
package ch.bbv.explorer.components;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * A panel containing a table of system or application properties. Convenience
 * methods are provided to retrieve and display the system properties of a Java
 * virtual machine.
 * @author Marcel Baumann
 * @version $Revision$
 */
public class PropertiesPanel extends TablePanel {
  /**
   * Name of the columns of the properties panel.
   */
  public static final String[] COLUMN_NAMES = { "name", "value" };

  /**
   * Default constructor of the class.
   * @param properties property list to display.
   */
  public PropertiesPanel(List properties) {
    super(properties, new TableCell(COLUMN_NAMES));
  }

  /**
   * Creates a properties panel containing all system properties.
   * @return the system properties panel.
   */
  public static PropertiesPanel createSystemPropertiesPanel() {
    return new PropertiesPanel(getSystemPropertyList());
  }

  /**
   * Returns a list of all system properties defined in the virtual machine.
   * @return the list of system properties.
   */
  public static List getSystemPropertyList() {
    List properties = new ArrayList();
    for (Iterator i = System.getProperties().keySet().iterator(); i.hasNext();) {
      String name = (String) i.next();
      String value = System.getProperty(name);
      Property property = new Property(name, value);
      properties.add(property);
    }
    return properties;
  }
}