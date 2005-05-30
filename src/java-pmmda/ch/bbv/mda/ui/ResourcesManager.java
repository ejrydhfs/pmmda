/* copyright 2000-2005 Marcel Baumann 
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

public class ResourcesManager extends ComponentImpl implements Component {
  public static final String RESOURCES_POSTFIX = "UiMessages";

  public static final String NAME = "Resources Manager";

  private Map<Cartridge, ResourceBundle> resources;

  /**
   * Constructor of the class.
   * @param name name of the instance as accessible through the locator.
   */
  public ResourcesManager(String name) {
    super(name);
    resources = new HashMap<Cartridge, ResourceBundle>();
  }

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

  public String getResource(String key) {
    String text = null;
    Cartridge cartridge = getCartridge(getAbbreviation(key));
    if (cartridge != null) {
      text = resources.get(cartridge).getString(key);
    }
    return text;
  }

  private String getAbbreviation(String text) {
    int index = text.indexOf('-');
    if (index > 0) {
      return text.substring(0, text.indexOf('-'));
    } 
    return null;
  }

  private Cartridge getCartridge(String abbreviation) {
    return CartridgeFactory.getInstance().getCartridge(abbreviation);
  }
}
