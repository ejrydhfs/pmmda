/* copyright 2000-2004 Marcel Baumann 
 * Licensed under the Apache License, Version 2.0 (the "Licence"); 
 * you may not use this file except in compliance with the License. 
 * $Id$ 
 */
package ch.bbv.dog;

/**
 * The functor defines the interface of a functor object, meaning the object
 * oriented representation of a function.
 * @author MarcelBaumann
 * @version $Revision$
 */
public interface DoFunctor {
  /**
   * Execute the functor on the given data object.
   * @param object data object instance to process.
   */
  void execute(DataObject object);
}