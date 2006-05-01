/* This code was generated automatically with the pmMDA generator. Please do 
   NOT add code outside the marked zones. All changes outside the marked areas 
   will be discarded during the next execution of the MDA generator. 
*/

package ch.bbv.dnm.users.lw;

import ch.bbv.dnm.users.*;

/** 
 * The transformer is responsible to transform a tree of data objects into its
 * lighthweight tree representation.
 */
public class LwTransformer {
  /** 
   * Transforms a data object specific class into its lightweight representation.
   * @param item data object instance to synchronize.
   * @return the lightweight representation of the data object instance.
   */
  public static UserLw transform(User item) {
    assert item != null;
    UserLw lw = new UserLw(item.getId());
    synchronize(item, lw);
    return lw;
  }
  
  /** 
   * Transforms a data object specific class into its lightweight representation.
   * @param item data object instance to synchronize.
   * @return the lightweight representation of the data object instance.
   */
  public static RoleLw transform(Role item) {
    assert item != null;
    RoleLw lw = new RoleLw(item.getId());
    synchronize(item, lw);
    return lw;
  }
  
  /** 
   * Transforms a data object specific class into its lightweight representation.
   * @param item data object instance to synchronize.
   * @return the lightweight representation of the data object instance.
   */
  public static UnitLw transform(Unit item) {
    assert item != null;
    UnitLw lw = new UnitLw(item.getId());
    synchronize(item, lw);
    return lw;
  }
  
  /** 
   * Transforms a data object specific class into its lightweight representation.
   * @param item data object instance to synchronize.
   * @return the lightweight representation of the data object instance.
   */
  public static DomainLw transform(Domain item) {
    assert item != null;
    DomainLw lw = new DomainLw(item.getId());
    synchronize(item, lw);
    return lw;
  }
  
  /** 
   * Transforms a data object specific class into its lightweight representation.
   * @param item data object instance to synchronize.
   * @return the lightweight representation of the data object instance.
   */
  public static ProviderLw transform(Provider item) {
    assert item != null;
    ProviderLw lw = new ProviderLw(item.getId());
    synchronize(item, lw);
    return lw;
  }
  
  /** 
   * Transforms a data object specific class into its lightweight representation.
   * @param item data object instance to synchronize.
   * @return the lightweight representation of the data object instance.
   */
  public static RealmLw transform(Realm item) {
    assert item != null;
    RealmLw lw = new RealmLw(item.getId());
    synchronize(item, lw);
    return lw;
  }
  
  /** 
   * Transforms a data object specific class into its lightweight representation.
   * @param item data object instance to synchronize.
   * @return the lightweight representation of the data object instance.
   */
  public static NamedEntityLw transform(NamedEntity item) {
    assert item != null;
    NamedEntityLw lw = new NamedEntityLw(item.getId());
    synchronize(item, lw);
    return lw;
  }
  
  /** 
   * Synchronizes a data object specific class into its lightweight representation.
   * @param item data object instance to synchronize.
   * @param lw the lightweight representation to synchronize.
   */
  public static void synchronize(User item, UserLw lw) {
    assert (item != null) && (lw != null);
    synchronize((NamedEntity)item, (NamedEntityLw)lw); 
    lw.setLogin(item.getLogin());
    lw.setActive(item.isActive());
    lw.setValidTo(item.getValidTo());
    lw.setValidFrom(item.getValidFrom());
  }
  
  /** 
   * Synchronizes a data object specific class into its lightweight representation.
   * @param item data object instance to synchronize.
   * @param lw the lightweight representation to synchronize.
   */
  public static void synchronize(Role item, RoleLw lw) {
    assert (item != null) && (lw != null);
    lw.setName(item.getName());
  }
  
  /** 
   * Synchronizes a data object specific class into its lightweight representation.
   * @param item data object instance to synchronize.
   * @param lw the lightweight representation to synchronize.
   */
  public static void synchronize(Unit item, UnitLw lw) {
    assert (item != null) && (lw != null);
    synchronize((NamedEntity)item, (NamedEntityLw)lw); 
    for (User i : item.getUsers()) {
      lw.addUser((i != null) ? transform(i) : null);
    }
    for (Unit i : item.getUnits()) {
      lw.addUnit((i != null) ? transform(i) : null);
    }
  }
  
  /** 
   * Synchronizes a data object specific class into its lightweight representation.
   * @param item data object instance to synchronize.
   * @param lw the lightweight representation to synchronize.
   */
  public static void synchronize(Domain item, DomainLw lw) {
    assert (item != null) && (lw != null);
    synchronize((NamedEntity)item, (NamedEntityLw)lw); 
    for (Unit i : item.getUnits()) {
      lw.addUnit((i != null) ? transform(i) : null);
    }
  }
  
  /** 
   * Synchronizes a data object specific class into its lightweight representation.
   * @param item data object instance to synchronize.
   * @param lw the lightweight representation to synchronize.
   */
  public static void synchronize(Provider item, ProviderLw lw) {
    assert (item != null) && (lw != null);
    synchronize((NamedEntity)item, (NamedEntityLw)lw); 
    lw.setTrusted(item.getTrusted());
    lw.setCost(item.getCost());
    for (Domain i : item.getDomains()) {
      lw.addDomain((i != null) ? transform(i) : null);
    }
  }
  
  /** 
   * Synchronizes a data object specific class into its lightweight representation.
   * @param item data object instance to synchronize.
   * @param lw the lightweight representation to synchronize.
   */
  public static void synchronize(Realm item, RealmLw lw) {
    assert (item != null) && (lw != null);
    lw.setName(item.getName());
    for (Provider i : item.getProviders()) {
      lw.addProvider((i != null) ? transform(i) : null);
    }
  }
  
  /** 
   * Synchronizes a data object specific class into its lightweight representation.
   * @param item data object instance to synchronize.
   * @param lw the lightweight representation to synchronize.
   */
  public static void synchronize(NamedEntity item, NamedEntityLw lw) {
    assert (item != null) && (lw != null);
    lw.setName(item.getName());
  }
  
}
