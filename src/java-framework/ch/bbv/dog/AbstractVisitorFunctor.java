/* copyright 2000-2004 Marcel Baumann 
 * Licensed under the Apache License, Version 2.0 (the "Licence); 
 * you may not use this file except in compliance with the License. 
 * $Id$ 
 */
package ch.bbv.dog;

/**
 * The abstract visitor functor defines the interface of a visitor which applies
 * a functor to each visited data object.
 * @author MarcelBaumann
 * @version $Revision$
 */
public interface AbstractVisitorFunctor {
  /**
   * Returns the current depth in the graph when traversing it with the visitor.
   * The depth is a positive integer with a minimal value of 1.
   * @return the current depth of the visitor in the graph.
   */
  int getDepth();

  /**
   * Sets the functor for the visitor.
   * @param functor new functor used in the visitor.
   * @see #getFunctor
   */
  void setFunctor(Object functor);

  /**
   * Returns the functor used in the visitor.
   * @return the functor of the visitor.
   * @see #setFunctor
   */
  Object getFunctor();
}