/* This code was generated automatically with the pmMDA generator. Please do 
   NOT add code outside the marked zones. All changes outside the marked areas 
   will be discarded during the next execution of the MDA generator. 
*/

package ch.bbv.unittests.indexedprops.lw;

import ch.bbv.dog.LightweightObject;

/* %% USER DEFINED DECLARATIONS START %% */
/* %% USER DEFINED DECLARATIONS END   %% */

/** 
 * The visitor class implements the visitor pattern for the graph of all 
 * data object classes defined in the package. The list of all relevant
 * classes is generated from the MDA model.
 */
public interface LwVisitor {

/* %% USER DEFINED DEFINITIONS START %% */
/* %% USER DEFINED DEFINITIONS END   %% */

  /** 
   * Returns the current depth in the graph when traversing it with the 
   * visitor. The depth is a positive integer with a minimal value of 1.
   * @return the current depth of the visitor in the graph
   */
  int getDepth();

  /** 
   * Sets the current depth in the graph when traversing it with the visitor. 
   * The depth is a positive integer with a minimal value of 1.
   * @param depth current depth of the visitor in the traversed graph
   */
  void setDepth(int depth);
  
  /**
   * Resets the visitor so that it can be used to traverse a graph.
   */
  void reset();
  
  /**
   * Returns true if the object should be visited otherwise false. This method
   * is used to detect cycles.
   * @param object data object to visit
   * @pre object != null
   */
  boolean shouldVisit(LightweightObject object);

  /** 
   * Visits the class IPListItemLw as part of the visitor pattern.
   * @param item item to visit
   */
  void visit(IPListItemLw item);

  /** 
   * Visits the class IPRootLw as part of the visitor pattern.
   * @param item item to visit
   */
  void visit(IPRootLw item);

  /** 
   * Visits the class IPSingleItemLw as part of the visitor pattern.
   * @param item item to visit
   */
  void visit(IPSingleItemLw item);

  /** 
   * Visits the class IPTopItemLw as part of the visitor pattern.
   * @param item item to visit
   */
  void visit(IPTopItemLw item);
}
