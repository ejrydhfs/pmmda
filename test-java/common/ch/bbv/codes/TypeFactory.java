/* This code was generated automatically with the MDA generator Version 0.2.
   Please do NOT add code outside the marked zones. All changes outside the 
   marked area will be discarded during the next execution of the MDA generator. 
*/
   
package ch.bbv.codes;

import ch.bbv.codes.lw.*;
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

  /** The data object manager responsible of the types defines in the package.
   */
  private DataObjectManager manager;
  
  /** Constructor of the class.
   * @param manager data object manager where the types should be created.
   */
  public TypeFactory(DataObjectManager manager) {
    this.manager = manager;
  }

  /** Registers all data object types defined in the package.
   */
  public void registerAllTypes() {
    createNodeTypeForReferenceCodeDescriptor();
    createNodeTypeForReferenceCodeGroup();
    createNodeTypeForValueDescriptor();
  }
  

  /** Creates a new data object type for the data object graph framework.
   */
  public void createNodeTypeForReferenceCodeDescriptor() {
    DataObjectType type = new DataObjectType(ReferenceCodeDescriptorLw.class,
      ReferenceCodeDescriptor.class, ReferenceCodeDescriptor.class, VisitorFunctor.class, 
      LwVisitorFunctor.class, LwTransformer.class);
    manager.register(type);
  }

  /** Creates a new data object type for the data object graph framework.
   */
  public void createNodeTypeForReferenceCodeGroup() {
    DataObjectType type = new DataObjectType(ReferenceCodeGroupLw.class,
      ReferenceCodeGroup.class, ReferenceCodeGroup.class, VisitorFunctor.class, 
      LwVisitorFunctor.class, LwTransformer.class);
    manager.register(type);
  }

  /** Creates a new data object type for the data object graph framework.
   */
  public void createNodeTypeForValueDescriptor() {
    DataObjectType type = new DataObjectType(ValueDescriptorLw.class,
      ValueDescriptor.class, ValueDescriptor.class, VisitorFunctor.class, 
      LwVisitorFunctor.class, LwTransformer.class);
    manager.register(type);
  }

  /** Returns the list of the classnames of all reference code types defined in 
   * the package.
   * @return list of class names of all reference code types in the package.
   */
  public List getReferenceCodeTypes() {
    List classnames = new ArrayList();
    classnames.add("ch.bbv.codes.CodeKind");
    return classnames;
  }
}