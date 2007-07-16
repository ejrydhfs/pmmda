/* This code was generated automatically with the pmMDA generator. Please do 
   NOT add code outside the marked zones. All changes outside the marked areas 
   will be discarded during the next execution of the MDA generator. 
*/
   
package ch.bbv.performancetests.list;

import ch.bbv.dog.DataObjectMgr;
import ch.bbv.dog.DataObjectType;
import ch.bbv.performancetests.list.lw.*;
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
  private DataObjectMgr manager;
  
  /** 
   * Constructor of the class.
   * @param manager data object manager where the types should be created
   */
  public TypeFactory(DataObjectMgr manager) {
    this.manager = manager;
  }

  /** 
   * Registers all data object types defined in the package.
   */
  public void registerAllTypes() {
    createNodeTypeForMaster();
    createNodeTypeForSlave();
  }
  

  /** 
   * Creates a new data object type for the data object graph framework.
   */
  public void createNodeTypeForMaster() {
    DataObjectType type = new DataObjectType(MasterLw.class,
      Master.class, Master.class, VisitorFunctor.class, 
      LwVisitorFunctor.class, LwTransformer.class);
    manager.register(type);
  }

  /** 
   * Creates a new data object type for the data object graph framework.
   */
  public void createNodeTypeForSlave() {
    DataObjectType type = new DataObjectType(SlaveLw.class,
      Slave.class, Slave.class, VisitorFunctor.class, 
      LwVisitorFunctor.class, LwTransformer.class);
    manager.register(type);
  }

  /** 
   * Returns the list of the classnames of all reference code types defined in 
   * the package.
   * @return list of class names of all reference code types in the package
   */
  public List<String> getReferenceCodeTypes() {
    List<String> classnames = new ArrayList<String>();
    return classnames;
  }
}