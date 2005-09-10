/* This code was generated automatically with the pmMDA generator. Please do 
   NOT add code outside the marked zones. All changes outside the marked areas 
   will be discarded during the next execution of the MDA generator. 
   $Id$
*/

package ch.bbv.dnm.users.lw;

import ch.bbv.dnm.users.*;

/** 
 * The transformer is responsible to transform a tree of data objects into its
 * lighthweight tree representation.
 * @version $Revision$
 */
public class LwTransformer {
  public static UserLw transform(User item) {
    UserLw lw = new UserLw(item.getId());
    lw.setLogin(item.getLogin());
    lw.setActive(item.isActive());
    lw.setValidTo(item.getValidTo());
    lw.setValidFrom(item.getValidFrom());
    return lw;
  }
  
  public static RoleLw transform(Role item) {
    RoleLw lw = new RoleLw(item.getId());
    lw.setName(item.getName());
    return lw;
  }
  
  public static UnitLw transform(Unit item) {
    UnitLw lw = new UnitLw(item.getId());
    for (User i : item.getUsers()) {
      lw.addUser(transform(i));
    }
    for (Unit i : item.getUnits()) {
      lw.addUnit(transform(i));
    }
    return lw;
  }
  
  public static DomainLw transform(Domain item) {
    DomainLw lw = new DomainLw(item.getId());
    for (Unit i : item.getUnits()) {
      lw.addUnit(transform(i));
    }
    return lw;
  }
  
  public static ProviderLw transform(Provider item) {
    ProviderLw lw = new ProviderLw(item.getId());
    for (Domain i : item.getDomains()) {
      lw.addDomain(transform(i));
    }
    return lw;
  }
  
  public static RealmLw transform(Realm item) {
    RealmLw lw = new RealmLw(item.getId());
    lw.setName(item.getName());
    for (Provider i : item.getProviders()) {
      lw.addProvider(transform(i));
    }
    return lw;
  }
  
  public static NamedEntityLw transform(NamedEntity item) {
    NamedEntityLw lw = new NamedEntityLw(item.getId());
    lw.setName(item.getName());
    return lw;
  }
  
}
