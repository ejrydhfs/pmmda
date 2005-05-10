/* This code was generated automatically with the pmMDA generator. Please do 
   NOT add code outside the marked zones. All changes outside the marked areas 
   will be discarded during the next execution of the MDA generator. 
   $Id$
*/
   
package ch.bbv.mda.pim;

import ch.bbv.dog.AbstractVisitorFunctor;
import ch.bbv.dog.DoFunctor;

/** 
 * The visitor with functor class implements the visitor pattern for the graph 
 * of all data object classes defined in the package. The list of all relevant
 * classes is generated from the MDA model.
 */
public class VisitorFunctor implements Visitor, AbstractVisitorFunctor {  
  /** 
   * Functor of the visitor.
   */
  private DoFunctor functor; 

  /**
   * Default constructor of the class.
   */
  public VisitorFunctor() {
  }
  
  /**
   * Constructor of the class, which initializes the functor.
   * @param functor functor of the visitor.
   */
  public VisitorFunctor(DoFunctor functor) {
    this.functor = functor;
  }
	
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
    this.functor = (DoFunctor)functor;
  }
    
  public void visit(CodeType item) {
    functor.execute(item);
  }
  public void visit(CodeValue item) {
    functor.execute(item);
  }
}
