/* copyright 2000-2005 Marcel Baumann 
 * Licensed under the Apache License, Version 2.0 (the "Licence"); 
 * you may not use this file except in compliance with the License. 
 * $Id$ 
 */
package ch.bbv.dog.functors;

import ch.bbv.dog.DataObject;
import ch.bbv.dog.DoFunctor;

/**
 * The is modified functor is a simple functor which indicates if at least one
 * object in the graph of data object was modified.
 * @author MarcelBaumann
 * @version $Revision$
 */
public class IsModifiedFlagFunctor implements DoFunctor {
  /**
   * Flag indicating if at least one object was modified in the tree of data
   * objects.
   */
  private boolean modified;

  /**
   * Resets the modified flag to false.
   */
  public void reset() {
    modified = false;
  }

  /**
   * Returns the value of the modified flag.
   * @return value of the flag.
   */
  public boolean isModified() {
    return modified;
  }

  public void execute(DataObject object) {
    modified |= object.isModified();

  }

}
