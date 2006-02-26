/* copyright 2000-2005 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence");
 * you may not use this file except in compliance with the License.
 * $Id$
 */
package org.sf.pmmda.development.taglets;

import java.util.Map;

import com.sun.javadoc.Tag;
import com.sun.tools.doclets.Taglet;

/**
 * The generic taglet provides generic services to all custom taglets defined
 * in the package.
 * @author MarcelBaumann
 * @version $Revision$
 */
public abstract class GenericTaglet implements Taglet {
  /** Name of the taglet.
   */
  private String name;

	/** Constructor of the class.
	 * @param name name of the taglet.
	 */
  public GenericTaglet(String name) {
    this.name = name;
  }

  /** Registers the taglet in the taglet map.
   * @param taglet taglet to register.
   * @param tagletMap map of all registered taglets.
   */
  public static void register(Taglet taglet, Map<String, Taglet> tagletMap) {
    String name = taglet.getName();
    if (tagletMap.get(name) != null) {
      tagletMap.remove(name);
    }
    tagletMap.put(name, taglet);
  }

  public String getName() {
    return name;
  }

  public boolean inField() {
    return false;
  }

  public boolean inConstructor() {
    return false;
  }

  public boolean inMethod() {
    return false;
  }

  public boolean inOverview() {
    return false;
  }

  public boolean inPackage() {
    return false;
  }

  public boolean inType() {
    return false;
  }

  public boolean isInlineTag() {
    return false;
  }

  public String toString(Tag[] tags) {
    if (tags.length == 0) {
      return null;
    }

    StringBuilder buffer = new StringBuilder();
    for (int i = 0; i < tags.length; i++) {
      buffer.append(toString(tags[i]));
    }
    return buffer.toString();
  }

  public abstract String toString(Tag tag);

	/** Converts a tag to its HTML representation.
	 * @param tag tag to convert.
	 * @param text text to used in the conversion process.
	 * @return the converted tag in HTML.
	 */
  protected String convert(Tag tag, String text) {
    StringBuilder buffer = new StringBuilder();
    buffer.append("<dt>").append("<strong>").append(text).append("</strong></dt>")
          .append("<dd>").append(tag.text()).append("</code>").append("</dd>\n");
    return buffer.toString();
  }
}
