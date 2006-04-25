/* copyright 2000-2006 Marcel Baumann 
 * Licensed under the Apache License, Version 2.0 (the "Licence"); 
 * you may not use this file except in compliance with the License. 
 * $Id$ 
 */
package ch.bbv.mda.ui;

import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import ch.bbv.application.Component;
import ch.bbv.application.ComponentImpl;
import ch.bbv.mda.generators.Cartridge;
import ch.bbv.mda.generators.CartridgeFactory;
import ch.bbv.mda.operators.ModelOperators;

/**
 * The resouce manager provides a facade to access the resources defined for
 * each registered cartridge. The resource files are identified through a naming
 * conventions <code> cartridge-prefix "UiMessages"</code> and have the file
 * extension properties.
 * @author MarcelBaumann
 * @version $Revision$
 */
public class ResourcesManager extends ComponentImpl implements Component {
  /**
   * Name of the component to find it through the locator service.
   */
  public static final String NAME = "Resources Manager";

  /**
   * Postfix for cartridges user interface related messages.
   */
  private static final String RESOURCES_POSTFIX = "UiMessages";

  /**
   * Resource bundle associated with their cartridge.
   */
  private Map<Cartridge, ResourceBundle> resources;

  /**
   * Constructor of the class.
   * @param name name of the instance as accessible through the locator
   */
  public ResourcesManager(String name) {
    super(name);
    resources = new HashMap<Cartridge, ResourceBundle>();
  }

  /**
   * Registers the cartridge and its resources in the manager.
   * @param cartridge cartridge to register
   */
  public void registerResources(Cartridge cartridge) {
    StringBuilder buffer = new StringBuilder();
    buffer
        .append(ModelOperators.getContextName(cartridge.getClass().getName()))
        .append(".resources.").append(
            ModelOperators.identifierFirstUpper(cartridge.getPrefix())).append(
            RESOURCES_POSTFIX);
    ResourceBundle resource = ResourceBundle.getBundle(buffer.toString());
    resources.put(cartridge, resource);
  }

  /**
   * Returns the resource associated to the provided key.
   * @param key the key of the requested resource. The key prefix must be the
   *          prefix of the cartridge defining the resource
   * @return the requested resource if found otherwise null
   * @pre key != null
   */
  public String getString(String key) {
    assert key != null;
    String text = null;
    String prefix = getPrefix(key);
    if (prefix != null) {
      Cartridge cartridge = getCartridge(prefix);
      if (cartridge != null) {
        text = resources.get(cartridge).getString(key);
      }
    }
    return text;
  }

  /**
   * Extracts the cartridge prefix of the key.
   * @param key key which prefix should be extracted. The separator between the
   *          prefix and the end of the key is the character '-'
   * @return the abbreviation if found otherwise null
   */
  private String getPrefix(String key) {
    int index = key.indexOf('-');
    if (index > 0) {
      return key.substring(0, key.indexOf('-'));
    }
    return null;
  }

  /**
   * Returns the cartridge with the given prefix.
   * @param prefix prefix of the cartridge
   * @return requested cartridge if found otherwise null
   */
  private Cartridge getCartridge(String prefix) {
    return CartridgeFactory.getInstance().getCartridge(prefix);
  }
}
