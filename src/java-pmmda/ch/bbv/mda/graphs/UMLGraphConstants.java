/* 
 * copyright 2000-2005 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence");
 * you may not use this file except in compliance with the License.
 * $Id$
 */

package ch.bbv.mda.graphs;

import java.awt.Color;

import org.jgraph.graph.AttributeMap;
import org.jgraph.graph.GraphConstants;

/**
 * A collection of attribute keys and methods to get/set the properties of UML
 * elements.
 * @author Marcel Baumann
 * @version $Revision$
 */
public class UMLGraphConstants extends GraphConstants {
  /**
   * Key for the <code>fillColor</code> attribute. Use instances of
   * <code>Color</code> as values for this key.
   */
  public static final String FILLCOLOR = "fillColor";

  /**
   * Key for the <code>stereoTypeLabelPosition</code> attribute. Use instances
   * of <code>Point</code> as values for this key.
   */
  public static final String STEREO_TYPE_LABEL_POSITION = "stereoTypeLabelPosition";

  /**
   * Key for the <code>stereoTypeLabelPosition</code> attribute. Use instances
   * of <code>Point</code> as values for this key.
   */
  public static final String BEGIN_MULTIPLICITY_LABEL_POSITION = "beginMultiplicityLabelPosition";

  /**
   * Key for the <code>showVisibility</code> attribute. Use instances of
   * <code>boolean</code> as values for this key.
   */
  public static final String SHOW_VISIBILITY = "showVisibility";

  /**
   * Key for the <code>showStereoType</code> attribute. Use instances of
   * <code>boolean</code> as values for this key.
   */
  public static final String SHOW_STEREO_TYPE = "showStereoType";

  /**
   * Key for the <code>includeAttribute</code> attribute. Use instances of
   * <code>boolean</code> as values for this key.
   */
  public static final String INCLUDE_ATTRIBUTE = "includeAttribute";

  /**
   * Key for the <code>includeOperation</code> attribute. Use instances of
   * <code>boolean</code> as values for this key.
   */
  public static final String INCLUDE_OPERATION = "includeOperation";

  /**
   * Key for the <code>suppressAttribute</code> attribute. Use instances of
   * <code>boolean</code> as values for this key.
   */
  public static final String SUPPRESS_ATTRIBUTE = "suppressAttribute";

  /**
   * Key for the <code>suppressOperation</code> attribute. Use instances of
   * <code>boolean</code> as values for this key.
   */
  public static final String SUPPRESS_OPERATION = "suppressOperation";

  /**
   * Returns the fillColor attribute from the specified map.
   * @param map attributes map to look for the requested attribute.
   */
  public static final Color getFillColor(AttributeMap map) {
    return (Color) map.get(FILLCOLOR);
  }

  /**
   * Sets the fillColor attribute in the specified map to the specified value.
   * @param map attributes map to update.
   */
  public static final void setFillColor(AttributeMap map, Color fillColor) {
    map.put(FILLCOLOR, fillColor);
  }

  /**
   * Returns the showVisibility attribute from the specified map.
   * @param map attributes map to look for the requested attribute.
   */
  public static final boolean getShowVisibility(AttributeMap map) {
    Boolean value = (Boolean) map.get(SHOW_VISIBILITY);
    return value == null ? false : value.booleanValue();
  }

  /**
   * Sets the showVisibility attribute in the specified map to the specified
   * value.
   * @param map attributes map to update.
   * @param value new boolean value of the attribute.
   */
  public static final void setShowVisibility(AttributeMap map, boolean value) {
    map.put(SHOW_VISIBILITY, new Boolean(value));
  }

  /**
   * Returns the showStereoType attribute from the specified map.
   * @param map attributes map to look for the requested attribute.
   */
  public static final boolean getShowStereotype(AttributeMap map) {
    Boolean value = (Boolean) map.get(SHOW_STEREO_TYPE);
    return value == null ? false : value.booleanValue();
  }

  /**
   * Sets the showStereoType attribute in the specified map to the specified
   * value.
   * @param map attributes map to update.
   * @param value new boolean value of the attribute.
   */
  public static final void setShowStereotype(AttributeMap map, boolean value) {
    map.put(SHOW_STEREO_TYPE, new Boolean(value));
  }

  /**
   * Returns the includeAttribute attribute from the specified map.
   * @param map attributes map to look for the requested attribute.
   */
  public static final boolean getIncludeAttribute(AttributeMap map) {
    Boolean value = (Boolean) map.get(INCLUDE_ATTRIBUTE);
    return value == null ? false : value.booleanValue();
  }

  /**
   * Sets the includeAttribute attribute in the specified map to the specified
   * value.
   * @param map attributes map to update.
   * @param value new boolean value of the attribute.
   */
  public static final void setIncludeAttribute(AttributeMap map, boolean value) {
    map.put(INCLUDE_ATTRIBUTE, new Boolean(value));
  }

  /**
   * Returns the includeOperation attribute from the specified map.
   * @param map attributes map to look for the requested attribute.
   */
  public static final boolean getIncludeOperation(AttributeMap map) {
    Boolean value = (Boolean) map.get(INCLUDE_OPERATION);
    return value == null ? false : value.booleanValue();
  }

  /**
   * Sets the includeOperation attribute in the specified map to the specified
   * value.
   * @param map attributes map to update.
   * @param value new boolean value of the attribute.
   */
  public static final void setIncludeOperation(AttributeMap map, boolean value) {
    map.put(INCLUDE_OPERATION, new Boolean(value));
  }

  /**
   * Returns the suppressAttribute attribute from the specified map.
   * @param map attributes map to look for the requested attribute.
   */
  public static final boolean getSuppressAttribute(AttributeMap map) {
    Boolean value = (Boolean) map.get(SUPPRESS_ATTRIBUTE);
    return value == null ? false : value.booleanValue();
  }

  /**
   * Sets the suppressAttribute attribute in the specified map to the specified
   * value.
   * @param map attributes map to update.
   * @param value new boolean value of the attribute.
   */
  public static final void setSuppressAttribute(AttributeMap map, boolean value) {
    map.put(SUPPRESS_ATTRIBUTE, new Boolean(value));
  }

  /**
   * Returns the suppressOperation attribute from the specified map.
   * @param map attributes map to look for the requested attribute.
   */
  public static final boolean getSuppressOperation(AttributeMap map) {
    Boolean value = (Boolean) map.get(SUPPRESS_OPERATION);
    return value == null ? false : value.booleanValue();
  }

  /**
   * Sets the suppressOperation attribute in the specified map to the specified
   * value.
   * @param map attributes map to update.
   * @param value new boolean value of the attribute.
   */
  public static final void setSuppressOperation(AttributeMap map, boolean value) {
    map.put(SUPPRESS_OPERATION, new Boolean(value));
  }

  /**
   * Converts a string to HTML text.
   * @param str the original text.
   * @return HTML formatted text.
   */
  public static String convertToHTML(String str) {
    StringBuilder buf = new StringBuilder();
    buf.append("<html><body>");
    buf.append(str);
    buf.append("</body></html>");
    return buf.toString();
  }
}
