/* This code was generated automatically with the pmMDA generator. Please do 
   NOT add code outside the marked zones. All changes outside the marked areas 
   will be discarded during the next execution of the MDA generator. 
*/
   
package ch.bbv.pim;

import ch.bbv.dog.DataObjectManager;
import ch.bbv.dog.DataObjectType;
import ch.bbv.pim.lw.*;
import java.util.ArrayList;
import java.util.List;

/**
 * The type factory provides meta information about all data objects and
 * reference code defined in the package to which the factory belongs. The 
 * package approach scales for complex system.
 */
public class TypeFactory {
/* %% USER DEFINED DEFINITIONS START %% */
/* %% USER DEFINED DEFINITIONS END   %% */

  /** 
   * The data object manager responsible of the types defines in the package.
   */
  private DataObjectManager manager;
  
  /** 
   * Constructor of the class.
   * @param manager data object manager where the types should be created.
   */
  public TypeFactory(DataObjectManager manager) {
    this.manager = manager;
  }

  /** 
   * Registers all data object types defined in the package.
   */
  public void registerAllTypes() {
    createNodeTypeForOrganization();
    createNodeTypeForContact();
    createNodeTypeForPhoneNumber();
    createNodeTypeForAddressBook();
  }
  

  /** 
   * Creates a new data object type for the data object graph framework.
   */
  public void createNodeTypeForOrganization() {
    DataObjectType type = new DataObjectType(OrganizationLw.class,
      Organization.class, Organization.class, VisitorFunctor.class, 
      LwVisitorFunctor.class, LwTransformer.class);
    manager.register(type);
  }

  /** 
   * Creates a new data object type for the data object graph framework.
   */
  public void createNodeTypeForContact() {
    DataObjectType type = new DataObjectType(ContactLw.class,
      Contact.class, Contact.class, VisitorFunctor.class, 
      LwVisitorFunctor.class, LwTransformer.class);
    manager.register(type);
  }

  /** 
   * Creates a new data object type for the data object graph framework.
   */
  public void createNodeTypeForPhoneNumber() {
    DataObjectType type = new DataObjectType(PhoneNumberLw.class,
      PhoneNumber.class, PhoneNumber.class, VisitorFunctor.class, 
      LwVisitorFunctor.class, LwTransformer.class);
    manager.register(type);
  }

  /** 
   * Creates a new data object type for the data object graph framework.
   */
  public void createNodeTypeForAddressBook() {
    DataObjectType type = new DataObjectType(AddressBookLw.class,
      AddressBook.class, AddressBook.class, VisitorFunctor.class, 
      LwVisitorFunctor.class, LwTransformer.class);
    manager.register(type);
  }

  /** 
   * Returns the list of the classnames of all reference code types defined in 
   * the package.
   * @return list of class names of all reference code types in the package.
   */
  public List<String> getReferenceCodeTypes() {
    List<String> classnames = new ArrayList<String>();
    classnames.add("ch.bbv.pim.JournalEntryType");
    classnames.add("ch.bbv.pim.Country");
    classnames.add("ch.bbv.pim.Language");
    return classnames;
  }
}