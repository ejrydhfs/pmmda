/* copyright 2000-2004 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence);
 * you may not use this file except in compliance with the License.
 * $Id$ 
 */
package ch.bbv.explorer;

import java.awt.Image;
import java.awt.Toolkit;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.swing.ImageIcon;

/**
 * The icon factory defines a conceptual framework to service lifecycle icons
 * for all lightweight data object types. The concept defines the following
 * rules.
 * <ul>
 * <li>The node type implementation has a get icon method. This method calls
 * the get icon method of the factory with the node type name and the state of
 * the lightweight object. Specialized functors are provided to simplify the
 * implementation of common situations.</li>
 * <li>The factory build a icon name composed on the node type name, a dash,
 * and the state name.</li>
 * <li>If the icon is not in the cache, it is loaded from the resources, and
 * added to the cache.</li>
 * <li>The icon is returned.</li>
 * </ul>
 * @author Marcel Baumann
 * @version $Revision$
 */
public class IconFactoryImpl implements IconFactory {
  /**
   * Extension for GIF image files.
   */
  private static final String GIF_EXTENSION = ".gif";

  /**
   * Local cache of all loaded icons.
   */
  private Map icons;

  private Map iconSelectors;

  /**
   * Default constructor of the class.
   */
  public IconFactoryImpl() {
    icons = new HashMap();
    iconSelectors = new HashMap();
  }

  public void registerIconSelector(UiNodeType nodeType,
      IconSelector iconSelector) {
    iconSelectors.put(nodeType, iconSelector);
  }

  public void flushIconsCache() {
    icons.clear();
  }

  public ImageIcon getSmallIconFor(ExplorerNode node, boolean isExpanded) {
    ImageIcon icon = null;
    IconSelector selector = (IconSelector) iconSelectors
        .get(node.getNodeType());
    if (selector != null) {
      icon = selector.select(node, isExpanded);
    }
    return icon;
  }

  public ImageIcon getSmallIconFor(String identifier) {
    ImageIcon icon = (ImageIcon) icons.get(identifier);
    if (icon == null) {
      StringBuffer buffer = new StringBuffer();
      buffer.append("/images/").append(identifier).append(GIF_EXTENSION);
      URL url = getClass().getResource(buffer.toString());
      Image image = Toolkit.getDefaultToolkit().createImage(url);
      icon = new ImageIcon(image);
      icons.put(identifier, icon);
    }
    return icon;
  }
}