/* This code was generated automatically with the MDA generator Version 0.2.
   Please do NOT add code outside the marked zones. All changes outside the 
   marked area will be discarded during the next execution of the MDA generator. 
*/


package ch.bbv.dnm.users.lw;

import java.util.Iterator;
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
    lw.setActive(item.getActive());
    lw.setName(item.getName());
    lw.setValidTo(item.getValidTo());
    lw.setValidFrom(item.getValidFrom());
    return lw;
  }
  
  public static UnitLw transform(Unit item) {
    UnitLw lw = new UnitLw(item.getId());
    lw.setName(item.getName());
    for (Iterator i = item.getUsers().iterator(); i.hasNext();) {
      lw.addUser(transform((User)i.next()));
    }
    for (Iterator i = item.getUnits().iterator(); i.hasNext();) {
      lw.addUnit(transform((Unit)i.next()));
    }
    return lw;
  }
  
  public static DomainLw transform(Domain item) {
    DomainLw lw = new DomainLw(item.getId());
    lw.setName(item.getName());
    for (Iterator i = item.getUnits().iterator(); i.hasNext();) {
      lw.addUnit(transform((Unit)i.next()));
    }
    return lw;
  }
  
  public static ProviderLw transform(Provider item) {
    ProviderLw lw = new ProviderLw(item.getId());
    lw.setName(item.getName());
    for (Iterator i = item.getDomains().iterator(); i.hasNext();) {
      lw.addDomain(transform((Domain)i.next()));
    }
    return lw;
  }
  
  public static RealmLw transform(Realm item) {
    RealmLw lw = new RealmLw(item.getId());
    lw.setName(item.getName());
    for (Iterator i = item.getProviders().iterator(); i.hasNext();) {
      lw.addProvider(transform((Provider)i.next()));
    }
    return lw;
  }
  
}
