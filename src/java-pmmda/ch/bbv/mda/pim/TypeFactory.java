/* This code was generated automatically with the pmMDA generator. Please do 
   NOT add code outside the marked zones. All changes outside the marked areas 
   will be discarded during the next execution of the MDA generator. 
   $Id$
*/
   
package ch.bbv.mda.pim;

import java.util.ArrayList;
import java.util.List;

import ch.bbv.dog.DataObjectMgr;

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
  private DataObjectMgr manager;
  
  /** 
   * Constructor of the class.
   * @param manager data object manager where the types should be created.
   */
  public TypeFactory(DataObjectMgr manager) {
    this.manager = manager;
  }

  /** 
   * Registers all data object types defined in the package.
   */
  public void registerAllTypes() {
  }
  

  /** 
   * Returns the list of the classnames of all reference code types defined in 
   * the package.
   * @return list of class names of all reference code types in the package.
   */
  public List<String> getReferenceCodeTypes() {
    List<String> classnames = new ArrayList<String>();
    classnames.add("ch.bbv.mda.pim.StorageKind");
    return classnames;
  }
}