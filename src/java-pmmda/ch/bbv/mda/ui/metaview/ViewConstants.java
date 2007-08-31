/* copyright 2000-2007 Bruno Persi
 * Licensed under the Apache License, Version 2.0 (the "Licence"); 
 * you may not use this file except in compliance with the License. 
 * $Id: 
 */
package ch.bbv.mda.ui.metaview;

/**
 * Defines constants necessary for storing and retrieving view related information in tagged values of properties.
 * Information about views is stored in a tagged value of the contained properties. Each property in a view has an XML entry for that view.
 * If the property is contained in several views then several XML entries are contained within that tagged value.
 * @author BrunoPersi & Adrian Bachofen
 *
 */
public interface ViewConstants {

	  public final String VIEW_TAG = "views"; // name of the tag for all views. Outer tag for all views.
	  public final String VIEW_NODE = "view"; // name of the tag for one view. Inner tag per view.
	  public final String REFERENCE = "reference"; // Tag name for a reference to an inner view.
	  public final String NAME = "name"; // name element per view - fully qualified name of the view.
	  public final String DESCRIPTION = "description"; // description element per view.
	  public final String VISIBILITY = "visibility"; // visibility element per view. 
}
