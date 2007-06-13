/* This code was generated automatically with the pmMDA generator. Please do 
   NOT add code outside the marked zones. All changes outside the marked areas 
   will be discarded during the next execution of the MDA generator. 
*/
   
package ch.bbv.client;

import ch.bbv.lw.*;
import ch.bbv.*;
import ch.bbv.dog.DataObjectMgr;
import ch.bbv.dog.DataObjectType;
import ch.bbv.explorer.PopupSelectorDefault;
import java.util.ArrayList;
import java.util.List;
import ch.bbv.explorer.*;
import ch.bbv.explorer.views.*;


/* %% USER DEFINED DECLARATIONS START %% */
/* %% USER DEFINED DECLARATIONS END   %% */

/** 
 * The type factory defines all the rich client types containing the data object
 * class and their lightweight association. The factory is used to initialize 
 * the OEX framework during runtime.
 */
public class TypeFactory extends AbstractTypeFactory {
/* %% USER DEFINED DEFINITIONS START %% */
/* %% USER DEFINED DEFINITIONS END   %% */

  /** 
   * Constructor of the class.
   * @param factory node type factory in which the nodes should be created
   * @param manager data object manager where the types should be created
   */
  public TypeFactory(UiNodeTypeFactory factory, DataObjectMgr manager) {
    super(factory, manager);
  }

  public void registerAllTypes() {
    createNodeTypeForRootClass();
    createNodeTypeForContact();
  }

  /** 
   * Creates a new user interface node type for the triplet describing the 
   * data object class, its lightweight representation, their common
   * interface and their user interface characteristics.
   */
  public void createNodeTypeForRootClass() {
    DataObjectType type = new DataObjectType(RootClassLw.class,
      RootClass.class, RootClass.class, VisitorFunctor.class, 
      LwVisitorFunctor.class, LwTransformer.class);
    type.setRoot(true);
    UiNodeTypeImpl nodeType = new UiNodeTypeImpl(type, "name", 
      GenericNodePropertiesView.class);
    addRelation(nodeType, "contacts", ContactLw.class, true, true);
    List<UiField> params = new ArrayList<UiField>();
    nodeType.registerView(new UiNodeViewType("default", nodeType, 
        NodeTableView.class, params, true));
    register(type, nodeType, new PopupSelectorDefault(nodeType), null);
  }

  /** 
   * Creates a new user interface node type for the triplet describing the 
   * data object class, its lightweight representation, their common
   * interface and their user interface characteristics.
   */
  public void createNodeTypeForContact() {
    DataObjectType type = new DataObjectType(ContactLw.class,
      Contact.class, Contact.class, VisitorFunctor.class, 
      LwVisitorFunctor.class, LwTransformer.class);
    type.setRoot(true);
    UiNodeTypeImpl nodeType = new UiNodeTypeImpl(type, "name", 
      GenericNodePropertiesView.class);
    List<UiField> params = new ArrayList<UiField>();
    nodeType.registerView(new UiNodeViewType("default", nodeType, 
        NodeTableView.class, params, true));
    register(type, nodeType, new PopupSelectorDefault(nodeType), null);
  }
}
