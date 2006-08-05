/* This code was generated automatically with the pmMDA generator. Please do 
   NOT add code outside the marked zones. All changes outside the marked areas 
   will be discarded during the next execution of the MDA generator. 
*/

package ch.bbv.pim;

import ch.bbv.dog.DataObject;

/* %% USER DEFINED DECLARATIONS START %% */
/* %% USER DEFINED DECLARATIONS END   %% */

/** 
 * The visitor class implements the visitor pattern for the graph of all data
 * object classes for all root classes defined in the package. The list of all 
 * relevant classes is generated from the MDA model.
 */
public interface Visitor {

/* %% USER DEFINED DEFINITIONS START %% */
/* %% USER DEFINED DEFINITIONS END   %% */

  /** 
   * Returns the current depth in the graph when traversing it with the 
   * visitor. The depth is a positive integer with a minimal value of 1.
   * @return the current depth of the visitor in the graph
   * @see #setDepth(int)
   */
  int getDepth();

  /** 
   * Sets the current depth in the graph when traversing it with the visitor. 
   * The depth is a positive integer with a minimal value of 1.
   * @param depth current depth of the visitor in the traversed graph
   * @see #getDepth()
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
  boolean shouldVisit(DataObject object);

  /** 
   * Visits the class JournalEntry as part of the visitor pattern.
   * @param item item to visit
   */
  void visit(JournalEntry item);

  /** 
   * Visits the class AddressBook as part of the visitor pattern.
   * @param item item to visit
   */
  void visit(AddressBook item);

  /** 
   * Visits the class PhoneNumber as part of the visitor pattern.
   * @param item item to visit
   */
  void visit(PhoneNumber item);

  /** 
   * Visits the class Address as part of the visitor pattern.
   * @param item item to visit
   */
  void visit(Address item);

  /** 
   * Visits the class Contact as part of the visitor pattern.
   * @param item item to visit
   */
  void visit(Contact item);

  /** 
   * Visits the class Organization as part of the visitor pattern.
   * @param item item to visit
   */
  void visit(Organization item);
}
