/* This code was generated automatically with the pmMDA generator. Please do 
   NOT add code outside the marked zones. All changes outside the marked areas 
   will be discarded during the next execution of the MDA generator. 
   $Id$
*/
   
package ch.bbv.dnm.users;

import ch.bbv.dnm.users.lw.*;
import ch.bbv.dog.DataObjectManager;
import ch.bbv.dog.DataObjectType;
import java.util.ArrayList;
import java.util.List;

/**
 * The type factory provides meta information about all data objects and
 * reference code defined in the package to which the factory belongs. The 
 * package approach scales for complex system.
 * @version $Revision$
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
    createNodeTypeForUser();
    createNodeTypeForRole();
    createNodeTypeForUnit();
    createNodeTypeForDomain();
    createNodeTypeForProvider();
    createNodeTypeForRealm();
  }
  

  /** 
   * Creates a new data object type for the data object graph framework.
   */
  public void createNodeTypeForUser() {
    DataObjectType type = new DataObjectType(UserLw.class,
      User.class, User.class, VisitorFunctor.class, 
      LwVisitorFunctor.class, LwTransformer.class);
    manager.register(type);
  }

  /** 
   * Creates a new data object type for the data object graph framework.
   */
  public void createNodeTypeForRole() {
    DataObjectType type = new DataObjectType(RoleLw.class,
      Role.class, Role.class, VisitorFunctor.class, 
      LwVisitorFunctor.class, LwTransformer.class);
    manager.register(type);
  }

  /** 
   * Creates a new data object type for the data object graph framework.
   */
  public void createNodeTypeForUnit() {
    DataObjectType type = new DataObjectType(UnitLw.class,
      Unit.class, Unit.class, VisitorFunctor.class, 
      LwVisitorFunctor.class, LwTransformer.class);
    manager.register(type);
  }

  /** 
   * Creates a new data object type for the data object graph framework.
   */
  public void createNodeTypeForDomain() {
    DataObjectType type = new DataObjectType(DomainLw.class,
      Domain.class, Domain.class, VisitorFunctor.class, 
      LwVisitorFunctor.class, LwTransformer.class);
    manager.register(type);
  }

  /** 
   * Creates a new data object type for the data object graph framework.
   */
  public void createNodeTypeForProvider() {
    DataObjectType type = new DataObjectType(ProviderLw.class,
      Provider.class, Provider.class, VisitorFunctor.class, 
      LwVisitorFunctor.class, LwTransformer.class);
    manager.register(type);
  }

  /** 
   * Creates a new data object type for the data object graph framework.
   */
  public void createNodeTypeForRealm() {
    DataObjectType type = new DataObjectType(RealmLw.class,
      Realm.class, Realm.class, VisitorFunctor.class, 
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
    return classnames;
  }
}