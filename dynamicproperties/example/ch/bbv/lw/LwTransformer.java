/* This code was generated automatically with the pmMDA generator. Please do 
   NOT add code outside the marked zones. All changes outside the marked areas 
   will be discarded during the next execution of the MDA generator. 
*/

package ch.bbv.lw;

import ch.bbv.Contact;
import ch.bbv.RootClass;

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
  public static ContactLw transform(Contact item) {
    assert item != null;
    ContactLw lw = new ContactLw(item.getId());
    synchronize(item, lw);
    return lw;
  }
  
  /** 
   * Transforms a data object specific class into its lightweight representation.
   * @param item data object instance to synchronize.
   * @return the lightweight representation of the data object instance
   */
  public static RootClassLw transform(RootClass item) {
    assert item != null;
    RootClassLw lw = new RootClassLw(item.getId());
    synchronize(item, lw);
    return lw;
  }
  
  /** 
   * Synchronizes a data object specific class into its lightweight representation.
   * @param item data object instance to synchronize.
   * @param lw the lightweight representation to synchronize.
   */
  public static void synchronize(Contact item, ContactLw lw) {
    assert (item != null) && (lw != null);
    lw.setName(item.getName());
    lw.setCity(item.getCity());
  }
  
  /** 
   * Synchronizes a data object specific class into its lightweight representation.
   * @param item data object instance to synchronize.
   * @param lw the lightweight representation to synchronize.
   */
  public static void synchronize(RootClass item, RootClassLw lw) {
    assert (item != null) && (lw != null);
    lw.setName(item.getName());
    for (Contact i : item.getContacts()) {
      lw.addContact((i != null) ? transform(i) : null);
    }
  }
  
}
