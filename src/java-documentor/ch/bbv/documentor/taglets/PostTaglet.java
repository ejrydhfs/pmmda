/* copyright 2000-2005 Marcel Baumann 
 * Licensed under the Apache License, Version 2.0 (the "Licence"); 
 * you may not use this file except in compliance with the License. 
 * $Id$
 */
package ch.bbv.documentor.taglets;

import java.util.Map;

import com.sun.javadoc.Tag;
import com.sun.tools.doclets.Taglet;

/** The taglet implements the postcondition tag introduced to support 
 * programming by contract. The writer should use the OCL syntax.
 * Consult the documentation of the Doclet interface provided with JavaDoc for
 * a description of the methods of this class.
 * @author Marcel Baumann
 * @version $Revision$
 */
public class PostTaglet extends GenericTaglet implements Taglet {
	/** Name of the taglet.
	 */
  private static final String NAME = "post";

  /** Registers the taglet in the taglet map.
   * @param tagletMap map of all registered taglets.
   */
  public static void register(Map<String, Taglet> tagletMap) {
		GenericTaglet.register(new PostTaglet(), tagletMap);
  }

	public PostTaglet() {
		super(NAME);
	}
	
  public boolean inConstructor() {
    return true;
  }

  public boolean inMethod() {
    return true;
  }

  public String toString(Tag tag) {
		return convert(tag, "Postcondition: ");
  }
}
