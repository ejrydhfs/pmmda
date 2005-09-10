/* This code was generated automatically with the pmMDA generator. Please do 
   NOT add code outside the marked zones. All changes outside the marked areas 
   will be discarded during the next execution of the MDA generator. 
   $Id$
*/

package ch.bbv.dnm.users.lw;

import ch.bbv.dog.AbstractVisitorFunctor;
import ch.bbv.dog.LwFunctor;

/** 
 * The visitor with functor class implements the visitor pattern for the graph 
 * of all data object classes defined in the package. The list of all relevant
 * classes is generated from the MDA model.
 */
public class LwVisitorFunctor implements LwVisitor, AbstractVisitorFunctor {  
  /** 
   * Functor of the visitor.
   */
  private LwFunctor functor; 

  /** 
   * The current depth of the visitor when traversing a graph.
   */
  private int depth;

  public int getDepth() {
    return depth;
  }
  
  public void setDepth(int depth) {
    this.depth = depth;
  }

  public Object getFunctor() {
    return functor;
  }
  
  public void setFunctor(Object functor) {
    this.functor = (LwFunctor)functor;
  }
    
  public void visit(UserLw item) {
    functor.execute(item);
  }
  public void visit(RoleLw item) {
    functor.execute(item);
  }
  public void visit(UnitLw item) {
    functor.execute(item);
  }
  public void visit(DomainLw item) {
    functor.execute(item);
  }
  public void visit(ProviderLw item) {
    functor.execute(item);
  }
  public void visit(RealmLw item) {
    functor.execute(item);
  }
  public void visit(NamedEntityLw item) {
    functor.execute(item);
  }
}
