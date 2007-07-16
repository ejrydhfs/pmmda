/* This code was generated automatically with the pmMDA generator. Please do 
   NOT add code outside the marked zones. All changes outside the marked areas 
   will be discarded during the next execution of the MDA generator. 
*/

package ch.bbv.unittests.props.lw;

import ch.bbv.unittests.props.*;

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
  public static SimpleAttributesLw transform(SimpleAttributes item) {
    assert item != null;
    SimpleAttributesLw lw = new SimpleAttributesLw(item.getId());
    synchronize(item, lw);
    return lw;
  }
  
  /** 
   * Transforms a data object specific class into its lightweight representation.
   * @param item data object instance to synchronize.
   * @return the lightweight representation of the data object instance
   */
  public static ParentLw transform(Parent item) {
    assert item != null;
    ParentLw lw = new ParentLw(item.getId());
    synchronize(item, lw);
    return lw;
  }
  
  /** 
   * Transforms a data object specific class into its lightweight representation.
   * @param item data object instance to synchronize.
   * @return the lightweight representation of the data object instance
   */
  public static SubItemLw transform(SubItem item) {
    assert item != null;
    SubItemLw lw = new SubItemLw(item.getId());
    synchronize(item, lw);
    return lw;
  }
  
  /** 
   * Transforms a data object specific class into its lightweight representation.
   * @param item data object instance to synchronize.
   * @return the lightweight representation of the data object instance
   */
  public static ItemLw transform(Item item) {
    assert item != null;
    ItemLw lw = new ItemLw(item.getId());
    synchronize(item, lw);
    return lw;
  }
  
  /** 
   * Synchronizes a data object specific class into its lightweight representation.
   * @param item data object instance to synchronize.
   * @param lw the lightweight representation to synchronize.
   */
  public static void synchronize(SimpleAttributes item, SimpleAttributesLw lw) {
    assert (item != null) && (lw != null);
    lw.setBooleanAttribute(item.isBooleanAttribute());
    lw.setIntAttribute(item.getIntAttribute());
    lw.setFloatAttribute(item.getFloatAttribute());
    lw.setDoubleAttribute(item.getDoubleAttribute());
    lw.setStringAttribute(item.getStringAttribute());
    lw.setLongAttribute(item.getLongAttribute());
  }
  
  /** 
   * Synchronizes a data object specific class into its lightweight representation.
   * @param item data object instance to synchronize.
   * @param lw the lightweight representation to synchronize.
   */
  public static void synchronize(Parent item, ParentLw lw) {
    assert (item != null) && (lw != null);
    lw.setParentAttribute(item.getParentAttribute());
  }
  
  /** 
   * Synchronizes a data object specific class into its lightweight representation.
   * @param item data object instance to synchronize.
   * @param lw the lightweight representation to synchronize.
   */
  public static void synchronize(SubItem item, SubItemLw lw) {
    assert (item != null) && (lw != null);
    lw.setSubItemAttribute(item.getSubItemAttribute());
  }
  
  /** 
   * Synchronizes a data object specific class into its lightweight representation.
   * @param item data object instance to synchronize.
   * @param lw the lightweight representation to synchronize.
   */
  public static void synchronize(Item item, ItemLw lw) {
    assert (item != null) && (lw != null);
    lw.setItemAttribute(item.getItemAttribute());
  }
  
}
