/* This code was generated automatically with the pmMDA generator. Please do 
   NOT add code outside the marked zones. All changes outside the marked areas 
   will be discarded during the next execution of the MDA generator. 
   $Id$
*/

package ch.bbv.dnm.users;

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
   * @see #setDepth(int)
   */
  int getDepth();

  /** 
   * Sets the current depth in the graph when traversing it with the visitor. 
   * The depth is a positive integer with a minimal value of 1.
   * @param depth current depth of the visitor in the traversed graph.
   * @see #getDepth()
   */
  void setDepth(int depth);
  

  /** 
   * Visits the class User as part of the visitor pattern.
   * @param item item to visit.
   */
  void visit(User item);

  /** 
   * Visits the class Role as part of the visitor pattern.
   * @param item item to visit.
   */
  void visit(Role item);

  /** 
   * Visits the class Unit as part of the visitor pattern.
   * @param item item to visit.
   */
  void visit(Unit item);

  /** 
   * Visits the class Domain as part of the visitor pattern.
   * @param item item to visit.
   */
  void visit(Domain item);

  /** 
   * Visits the class Provider as part of the visitor pattern.
   * @param item item to visit.
   */
  void visit(Provider item);

  /** 
   * Visits the class Realm as part of the visitor pattern.
   * @param item item to visit.
   */
  void visit(Realm item);

  /** 
   * Visits the class UserRole as part of the visitor pattern.
   * @param item item to visit.
   */
  void visit(UserRole item);

  /** 
   * Visits the class NamedEntity as part of the visitor pattern.
   * @param item item to visit.
   */
  void visit(NamedEntity item);

  /** 
   * Visits the class ResourceType as part of the visitor pattern.
   * @param item item to visit.
   */
  void visit(ResourceType item);
}
