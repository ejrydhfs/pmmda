/* copyright 2000-2006 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence");
 * you may not use this file except in compliance with the License.
 * $Id$
 */
package ch.bbv.explorer.components;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import ch.bbv.explorer.resources.Messages;

/**
 * A panel containing a table of system or application properties. Convenience
 * methods are provided to retrieve and display the system properties of a Java
 * virtual machine.
 * @author Marcel Baumann
 * @version $Revision$
 */
public class PropertiesPanel extends TablePanel {
  /**
   * Serial version identifier coded as back compatibility date.
   */
  private static final long serialVersionUID = 20041201;

  /**
   * Name of the properties for fields of the table.
   */
  public static String[] propertiesPropertyNames = { "name", "value" };

  /**
   * Default constructor of the class.
   * @param properties property list to display
   */
  public PropertiesPanel(List<Property> properties) {
    super(properties, new TableCell(propertiesPropertyNames,
        Messages.propertiesColumnNames));
  }

  /**
   * Creates a properties panel containing all system properties.
   * @return the system properties panel
   */
  public static PropertiesPanel createSystemPropertiesPanel() {
    return new PropertiesPanel(getSystemPropertyList());
  }

  /**
   * Returns a list of all system properties defined in the virtual machine.
   * @return the list of system properties
   */
  public static List<Property> getSystemPropertyList() {
    List<Property> properties = new ArrayList<Property>();
    for (Iterator i = System.getProperties().keySet().iterator(); i.hasNext();) {
      String name = (String) i.next();
      properties.add(new Property(name, System.getProperty(name)));
    }
    return properties;
  }
}