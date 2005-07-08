/* copyright 2000-2005 Marcel Baumann 
 * Licensed under the Apache License, Version 2.0 (the "Licence"); 
 * you may not use this file except in compliance with the License. 
 * $Id$
 */
package ch.bbv.dog.reference;

import java.util.List;

/**
 * The hierarchical reference code defines the interface for all hierarchical
 * key code of the application. All hierarchical codes of the application should
 * implement this interface.
 * @author Marcel Baumann
 * @version $Revision$
 */
public interface HierarchicalReferenceCode extends ReferenceCode {
  /**
   * Returns the parentId value of the reference code.
   * @return parentId value of the code.
   */
  public Integer getParentCode();

  /**
   * Returns all direct descendants of the code instance. The children are
   * sorted based on their order information.
   * @return sorted list of all children.
   */
  public List getChildren();
}
