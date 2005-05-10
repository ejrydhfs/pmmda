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
public class ModifiedFlagFunctor implements DoFunctor {
  /**
   * Flag indicating if at least one object was modified in the tree of data
   * objects.
   */
  private boolean modified;

  /**
   * Set the new value of the property modified.
   * @param modified the new value of the property.
   * @see #isModified()
   */
  public void setModified(boolean modified) {
    this.modified = modified;
  }
  
  /**
   * Returns the value of the modified flag.
   * @return value of the flag.
   * @see #setModified(boolean)
   */
  public boolean isModified() {
    return modified;
  }

  public void execute(DataObject object) {
    object.setModified(modified);

  }

}
