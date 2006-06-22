/* 
 * copyright 2000-2005 Marcel Baumann 
 * Licensed under the Apache License, Version 2.0 (the "Licence"); 
 * you may not use this file except in compliance with the License. 
 * $Id$ 
 */
package ch.bbv.mda;

import ch.bbv.dog.AbstractVisitorFunctor;
import ch.bbv.dog.DoFunctor;

/**
 * The visitor functor is the standard functor implementation for the MDA
 * visitor.
 * @author marcelbaumann
 * @version $Revision$
 */
public class VisitorFunctor implements AbstractVisitorFunctor, Visitor {
  /**
   * Functor of the visitor.
   */
  private DoFunctor functor;

  /**
   * The current depth of the visitor when traversing a graph.
   */
  private int depth;

  public int getDepth() {
    return depth;
  }

  /**
   * Sets the current depth of the visitor when executed.
   * @param depth the depth of the visitor.
   * @see #getDepth()
   */
  public void setDepth(int depth) {
    this.depth = depth;
  }

  public Object getFunctor() {
    return functor;
  }

  /**
   * Sets the functor of the visitor functor.
   * @param functor the new functor of the visitor.
   * @see #getFunctor()
   */
  public void setFunctor(Object functor) {
    this.functor = (DoFunctor) functor;
  }

  public void visit(MetaModel item) {
    functor.execute(item);
  }

  public void visit(MetaPackage item) {
    functor.execute(item);
  }

  public void visit(MetaClass item) {
    functor.execute(item);
  }

  public void visit(MetaProperty item) {
    functor.execute(item);
  }

  public void visit(MetaIndexedProperty item) {
    functor.execute(item);
  }

  public void visit(MetaDatatype item) {
    functor.execute(item);
  }

  public void visit(Stereotype item) {
    functor.execute(item);
  }

  public void visit(TaggedValue item) {
    functor.execute(item);
  }
}