/* copyright 2000-2005 Marcel Baumann 
 * Licensed under the Apache License, Version 2.0 (the "Licence"); 
 * you may not use this file except in compliance with the License. 
 * $Id$
 */
package ch.bbv.documentor.taglets;

import java.util.Map;

import com.sun.javadoc.Tag;
import com.sun.tools.doclets.Taglet;

/** The taglet implements the invariant tag introduced to support programming 
 * by contract. The writer should use the OCL syntax.
 * Consult the documentation of the Doclet interface provided with JavaDoc for
 * a description of the methods of this class.
 * @author Marcel Baumann
 * @version $Revision$
 */
public class InvTaglet extends GenericTaglet implements Taglet {
	/** Name of the taglet.
	 */
  private static final String NAME = "inv";

	/** Registers the taglet in the taglet map.
	 * @param tagletMap map of all registered taglets.
	 */
  public static void register(Map<String, Taglet> tagletMap) {
  	GenericTaglet.register(new InvTaglet(), tagletMap);
  }

	public InvTaglet() {
		super(NAME);
	}
	
  public boolean inField() {
    return true;
  }

  public boolean inOverview() {
    return true;
  }

  public boolean inType() {
    return true;
  }

  public String toString(Tag tag) {
		return convert(tag, "Invariant: ");
  }
}
