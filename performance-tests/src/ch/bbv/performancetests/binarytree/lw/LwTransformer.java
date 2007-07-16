/* This code was generated automatically with the pmMDA generator. Please do 
   NOT add code outside the marked zones. All changes outside the marked areas 
   will be discarded during the next execution of the MDA generator. 
*/

package ch.bbv.performancetests.binarytree.lw;

import ch.bbv.performancetests.binarytree.Forest;
import ch.bbv.performancetests.binarytree.Node;

/** 
 * The transformer is responsible to transform a tree of data objects into its
 * lighthweight tree representation.
 */
public class LwTransformer {
  /** 
   * Transforms a data object specific class into its lightweight representation.
   * @param item data object instance to synchronize.
   * @return the lightweight representation of the data object instance
   */
  public static ForestLw transform(Forest item) {
    assert item != null;
    ForestLw lw = new ForestLw(item.getId());
    synchronize(item, lw);
    return lw;
  }
  
  /** 
   * Transforms a data object specific class into its lightweight representation.
   * @param item data object instance to synchronize.
   * @return the lightweight representation of the data object instance
   */
  public static NodeLw transform(Node item) {
    assert item != null;
    NodeLw lw = new NodeLw(item.getId());
    synchronize(item, lw);
    return lw;
  }
  
  /** 
   * Synchronizes a data object specific class into its lightweight representation.
   * @param item data object instance to synchronize.
   * @param lw the lightweight representation to synchronize.
   */
  public static void synchronize(Forest item, ForestLw lw) {
    assert (item != null) && (lw != null);
    for (Node i : item.getTrees()) {
      lw.addTree((i != null) ? transform(i) : null);
    }
  }
  
  /** 
   * Synchronizes a data object specific class into its lightweight representation.
   * @param item data object instance to synchronize.
   * @param lw the lightweight representation to synchronize.
   */
  public static void synchronize(Node item, NodeLw lw) {
    assert (item != null) && (lw != null);
    lw.setValue(item.getValue());
  }
  
}
