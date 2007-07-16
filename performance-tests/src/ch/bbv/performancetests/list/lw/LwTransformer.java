/* This code was generated automatically with the pmMDA generator. Please do 
   NOT add code outside the marked zones. All changes outside the marked areas 
   will be discarded during the next execution of the MDA generator. 
*/

package ch.bbv.performancetests.list.lw;

import ch.bbv.performancetests.list.Master;
import ch.bbv.performancetests.list.Slave;

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
  public static MasterLw transform(Master item) {
    assert item != null;
    MasterLw lw = new MasterLw(item.getId());
    synchronize(item, lw);
    return lw;
  }
  
  /** 
   * Transforms a data object specific class into its lightweight representation.
   * @param item data object instance to synchronize.
   * @return the lightweight representation of the data object instance
   */
  public static SlaveLw transform(Slave item) {
    assert item != null;
    SlaveLw lw = new SlaveLw(item.getId());
    synchronize(item, lw);
    return lw;
  }
  
  /** 
   * Synchronizes a data object specific class into its lightweight representation.
   * @param item data object instance to synchronize.
   * @param lw the lightweight representation to synchronize.
   */
  public static void synchronize(Master item, MasterLw lw) {
    assert (item != null) && (lw != null);
    for (Slave i : item.getHas()) {
      lw.addHa((i != null) ? transform(i) : null);
    }
  }
  
  /** 
   * Synchronizes a data object specific class into its lightweight representation.
   * @param item data object instance to synchronize.
   * @param lw the lightweight representation to synchronize.
   */
  public static void synchronize(Slave item, SlaveLw lw) {
    assert (item != null) && (lw != null);
    lw.setVal(item.getVal());
  }
  
}
