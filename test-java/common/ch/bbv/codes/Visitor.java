/* This code was generated automatically with the pmMDA generator. Please do 
   NOT add code outside the marked zones. All changes outside the marked areas 
   will be discarded during the next execution of the MDA generator. 
   $Id$
*/

package ch.bbv.codes;

/* %% USER DEFINED DECLARATIONS START %% */
/* %% USER DEFINED DECLARATIONS END   %% */

/** 
 * The visitor class implements the visitor pattern for the graph of all 
 * data object classes defined in the package. The list of all relevant
 * classes is generated from the MDA model.
 */
public interface Visitor {

/* %% USER DEFINED DEFINITIONS START %% */
/* %% USER DEFINED DEFINITIONS END   %% */

  /** 
   * Returns the current depth in the graph when traversing it with the 
   * visitor. The depth is a positive integer with a minimal value of 1.
   * @return the current depth of the visitor in the graph.
   */
  int getDepth();

  /** 
   * Sets the current depth in the graph when traversing it with the visitor. 
   * The depth is a positive integer with a minimal value of 1.	
   * @param depth current depth of the visitor in the traversed graph.
   */
  void setDepth(int depth);
  

  /** 
   * Visits the class ReferenceCodeDescriptor as part of the visitor pattern.
   * @param item item to visit.
   */
  void visit(ReferenceCodeDescriptor item);

  /** 
   * Visits the class ReferenceCodeGroup as part of the visitor pattern.
   * @param item item to visit.
   */
  void visit(ReferenceCodeGroup item);

  /** 
   * Visits the class ValueDescriptor as part of the visitor pattern.
   * @param item item to visit.
   */
  void visit(ValueDescriptor item);
}
