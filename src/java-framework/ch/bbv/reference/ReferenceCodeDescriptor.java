/* copyright 2000-2005 Marcel Baumann 
 * Licensed under the Apache License, Version 2.0 (the "Licence"); 
 * you may not use this file except in compliance with the License. 
 *  $Id$
 */
package ch.bbv.reference;

import java.util.List;


/**
 * <p>The reference code descriptor defines a set of reference codes with their
 * values. Structural information such as the target class, the  persistency
 * are defined in it.</p>
 * @author Marcel Baumann
 * @version $Revision$
 */
public interface ReferenceCodeDescriptor {
	/** Returns the name of the reference code.
	 * @return name of the reference code.
	 */
	String getName();
	
  /** Returns the qualified name of the class implementing the reference code
   * described in this description.
   * @return qualified name of the reference code class.
   */
  String getReferenceCodeClassName();

  /** Returns the default value of the reference code.
   * @return default value of the reference code if defined otherwise null.
   */
  Integer getDefaultValue();

  /** Indicates if the reference code is hierarchical or not.
   * @return true if the reference is hierarchical otherwise false.
   */
  boolean isHierarchical();

  /** Returns the class implementing the reference code described in this
   * description.
   * @return the class implementing the reference code.
   */
  Class getReferenceCodeClass();

  /** Returns the description of the reference code.
   * @return description of the reference code.
   */
  String getDescription();
  
  List getCodes();
}
