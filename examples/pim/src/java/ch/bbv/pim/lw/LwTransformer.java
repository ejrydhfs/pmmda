/* This code was generated automatically with the pmMDA generator. Please do 
   NOT add code outside the marked zones. All changes outside the marked areas 
   will be discarded during the next execution of the MDA generator. 
   $Id$
*/

package ch.bbv.pim.lw;

import ch.bbv.pim.*;

/** 
 * The transformer is responsible to transform a tree of data objects into its
 * lighthweight tree representation.
 * @version $Revision$
 */
public class LwTransformer {
  public static OrganizationLw transform(Organization item) {
    OrganizationLw lw = new OrganizationLw(item.getId());
    lw.setName(item.getName());
    for (Contact i : item.getContacts()) {
      lw.addContact(transform(i));
    }
    return lw;
  }
  
  public static ContactLw transform(Contact item) {
    ContactLw lw = new ContactLw(item.getId());
    lw.setFirstname(item.getFirstname());
    lw.setLastname(item.getLastname());
    lw.setMiddlename(item.getMiddlename());
    return lw;
  }
  
}
