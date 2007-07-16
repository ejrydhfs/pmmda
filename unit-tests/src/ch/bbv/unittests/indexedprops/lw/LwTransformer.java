/* This code was generated automatically with the pmMDA generator. Please do 
   NOT add code outside the marked zones. All changes outside the marked areas 
   will be discarded during the next execution of the MDA generator. 
*/

package ch.bbv.unittests.indexedprops.lw;

import ch.bbv.unittests.indexedprops.*;

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
  public static IPListItemLw transform(IPListItem item) {
    assert item != null;
    IPListItemLw lw = new IPListItemLw(item.getId());
    synchronize(item, lw);
    return lw;
  }
  
  /** 
   * Transforms a data object specific class into its lightweight representation.
   * @param item data object instance to synchronize.
   * @return the lightweight representation of the data object instance
   */
  public static IPRootLw transform(IPRoot item) {
    assert item != null;
    IPRootLw lw = new IPRootLw(item.getId());
    synchronize(item, lw);
    return lw;
  }
  
  /** 
   * Transforms a data object specific class into its lightweight representation.
   * @param item data object instance to synchronize.
   * @return the lightweight representation of the data object instance
   */
  public static IPSingleItemLw transform(IPSingleItem item) {
    assert item != null;
    IPSingleItemLw lw = new IPSingleItemLw(item.getId());
    synchronize(item, lw);
    return lw;
  }
  
  /** 
   * Transforms a data object specific class into its lightweight representation.
   * @param item data object instance to synchronize.
   * @return the lightweight representation of the data object instance
   */
  public static IPTopItemLw transform(IPTopItem item) {
    assert item != null;
    IPTopItemLw lw = new IPTopItemLw(item.getId());
    synchronize(item, lw);
    return lw;
  }
  
  /** 
   * Synchronizes a data object specific class into its lightweight representation.
   * @param item data object instance to synchronize.
   * @param lw the lightweight representation to synchronize.
   */
  public static void synchronize(IPListItem item, IPListItemLw lw) {
    assert (item != null) && (lw != null);
    lw.setIpListItemAttribute(item.getIpListItemAttribute());
  }
  
  /** 
   * Synchronizes a data object specific class into its lightweight representation.
   * @param item data object instance to synchronize.
   * @param lw the lightweight representation to synchronize.
   */
  public static void synchronize(IPRoot item, IPRootLw lw) {
    assert (item != null) && (lw != null);
    for (IPTopItem i : item.getItems()) {
      lw.addItem((i != null) ? transform(i) : null);
    }
  }
  
  /** 
   * Synchronizes a data object specific class into its lightweight representation.
   * @param item data object instance to synchronize.
   * @param lw the lightweight representation to synchronize.
   */
  public static void synchronize(IPSingleItem item, IPSingleItemLw lw) {
    assert (item != null) && (lw != null);
    lw.setIpSingleItemAttribute(item.getIpSingleItemAttribute());
    for (IPListItem i : item.getItems()) {
      lw.addItem((i != null) ? transform(i) : null);
    }
  }
  
  /** 
   * Synchronizes a data object specific class into its lightweight representation.
   * @param item data object instance to synchronize.
   * @param lw the lightweight representation to synchronize.
   */
  public static void synchronize(IPTopItem item, IPTopItemLw lw) {
    assert (item != null) && (lw != null);
    if (item.getItem() != null) {
      lw.setItem(transform(item.getItem()));
    }
  }
  
}
