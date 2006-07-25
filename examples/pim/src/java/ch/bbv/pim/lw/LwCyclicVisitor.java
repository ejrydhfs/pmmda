/* This code was generated automatically with the pmMDA generator. Please do 
   NOT add code outside the marked zones. All changes outside the marked areas 
   will be discarded during the next execution of the MDA generator. 
*/

package ch.bbv.pim.lw;

import ch.bbv.dog.LightweightObject;
import java.util.HashSet;
import java.util.Set;

/** 
 * The cyclic visitor is a wrapper class allowing to use visitors with graph
 * containing cycles. The list of all relevant classes is generated from the 
 * MDA model.
 */
public class LwCyclicVisitor implements LwVisitor {  
  /** 
   * Delegate visitor to protect.
   */
  private LwVisitor delegate; 

  /**
   * The objects already visited during the traversal of the graph.
   */
  private Set<LightweightObject> visitedObjects;
  
  /**
   * Constructor of the class, which initializes the functor.
   * @param delegate delegate visitor to execute in protected mode
   */
  public LwCyclicVisitor(LwVisitor delegate) {
    this.delegate = delegate;
	visitedObjects = new HashSet<LightweightObject>();
  }
  
  public int getDepth() {
    return delegate.getDepth();
  }
  
  public void setDepth(int depth) {
    delegate.setDepth(depth);
  }
    
  public void reset() {
	visitedObjects.clear();
  }
      
  public void visit(PhoneNumberLw item) {
    if (!visitedObjects.contains(item)) {
      visitedObjects.add(item);  
      delegate.visit(item);
    }
  }
  public void visit(AddressBookLw item) {
    if (!visitedObjects.contains(item)) {
      visitedObjects.add(item);  
      delegate.visit(item);
    }
  }
  public void visit(ContactLw item) {
    if (!visitedObjects.contains(item)) {
      visitedObjects.add(item);  
      delegate.visit(item);
    }
  }
  public void visit(OrganizationLw item) {
    if (!visitedObjects.contains(item)) {
      visitedObjects.add(item);  
      delegate.visit(item);
    }
  }
}
