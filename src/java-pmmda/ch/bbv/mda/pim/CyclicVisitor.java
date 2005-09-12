/* This code was generated automatically with the pmMDA generator. Please do 
 NOT add code outside the marked zones. All changes outside the marked areas 
 will be discarded during the next execution of the MDA generator. 
 $Id$
 */

package ch.bbv.mda.pim;

import java.util.HashSet;
import java.util.Set;

import ch.bbv.dog.DataObject;

/**
 * The cyclic visitor is a wrapper class allowing to use visitors with graph
 * containing cycles. The list of all relevant classes is generated from the MDA
 * model.
 * @version $Revision$
 */
public class CyclicVisitor implements Visitor {
  /**
   * Delegate visitor to protect.
   */
  private Visitor delegate;

  /**
   * The objects already visited during the traversal of the graph.
   */
  private Set<DataObject> visitedObjects;

  /**
   * Constructor of the class, which initializes the functor.
   * @param delegate delegate visitor to execute in protected mode.
   */
  public CyclicVisitor(Visitor delegate) {
    this.delegate = delegate;
    visitedObjects = new HashSet<DataObject>();
  }

  /**
   * Resets the internal structure of the visitor. Upon reset the visitor can be
   * used for a new graph traversal.
   */
  public void reset() {
    visitedObjects.clear();
  }

  public int getDepth() {
    return delegate.getDepth();
  }

  public void setDepth(int depth) {
    delegate.setDepth(depth);
  }

  public void visit(CodeType item) {
    if (!visitedObjects.contains(item)) {
      visitedObjects.add(item);
      delegate.visit(item);
    }
  }

  public void visit(CodeValue item) {
    if (!visitedObjects.contains(item)) {
      visitedObjects.add(item);
      delegate.visit(item);
    }
  }
}
