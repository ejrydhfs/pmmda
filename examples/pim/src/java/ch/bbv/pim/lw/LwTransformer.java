/* This code was generated automatically with the pmMDA generator. Please do 
   NOT add code outside the marked zones. All changes outside the marked areas 
   will be discarded during the next execution of the MDA generator. 
*/

package ch.bbv.pim.lw;

import ch.bbv.pim.AddressBook;
import ch.bbv.pim.Contact;
import ch.bbv.pim.Organization;
import ch.bbv.pim.PhoneNumber;

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
  public static OrganizationLw transform(Organization item) {
    assert item != null;
    OrganizationLw lw = new OrganizationLw(item.getId());
    synchronize(item, lw);
    return lw;
  }
  
  /** 
   * Transforms a data object specific class into its lightweight representation.
   * @param item data object instance to synchronize.
   * @return the lightweight representation of the data object instance.
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
   * @return the lightweight representation of the data object instance.
   */
  public static PhoneNumberLw transform(PhoneNumber item) {
    assert item != null;
    PhoneNumberLw lw = new PhoneNumberLw(item.getId());
    synchronize(item, lw);
    return lw;
  }
  
  /** 
   * Transforms a data object specific class into its lightweight representation.
   * @param item data object instance to synchronize.
   * @return the lightweight representation of the data object instance.
   */
  public static AddressBookLw transform(AddressBook item) {
    assert item != null;
    AddressBookLw lw = new AddressBookLw(item.getId());
    synchronize(item, lw);
    return lw;
  }
  
  /** 
   * Synchronizes a data object specific class into its lightweight representation.
   * @param item data object instance to synchronize.
   * @param lw the lightweight representation to synchronize.
   */
  public static void synchronize(Organization item, OrganizationLw lw) {
    assert (item != null) && (lw != null);
    lw.setName(item.getName());
    if (item.getMainNumber() != null) {
      lw.setMainNumber(transform(item.getMainNumber()));
    }
    for (Contact i : item.getContacts()) {
      lw.addContact((i != null) ? transform(i) : null);
    }
  }
  
  /** 
   * Synchronizes a data object specific class into its lightweight representation.
   * @param item data object instance to synchronize.
   * @param lw the lightweight representation to synchronize.
   */
  public static void synchronize(Contact item, ContactLw lw) {
    assert (item != null) && (lw != null);
    lw.setFirstname(item.getFirstname());
    lw.setLastname(item.getLastname());
    lw.setMiddlename(item.getMiddlename());
    lw.setWorkEmail(item.getWorkEmail());
    if (item.getWorkNumber() != null) {
      lw.setWorkNumber(transform(item.getWorkNumber()));
    }
  }
  
  /** 
   * Synchronizes a data object specific class into its lightweight representation.
   * @param item data object instance to synchronize.
   * @param lw the lightweight representation to synchronize.
   */
  public static void synchronize(PhoneNumber item, PhoneNumberLw lw) {
    assert (item != null) && (lw != null);
    lw.setNumber(item.getNumber());
  }
  
  /** 
   * Synchronizes a data object specific class into its lightweight representation.
   * @param item data object instance to synchronize.
   * @param lw the lightweight representation to synchronize.
   */
  public static void synchronize(AddressBook item, AddressBookLw lw) {
    assert (item != null) && (lw != null);
    lw.setName(item.getName());
    for (Organization i : item.getOrganizations()) {
      lw.addOrganization((i != null) ? transform(i) : null);
    }
  }
  
}
