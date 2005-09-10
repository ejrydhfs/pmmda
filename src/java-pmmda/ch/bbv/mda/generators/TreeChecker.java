/* 
 * copyright 2000-2005 Marcel Baumann 
 * Licensed under the Apache License, Version 2.0 (the "Licence"); 
 * you may not use this file except in compliance with the License. 
 * $Id$ 
 */
package ch.bbv.mda.generators;

import ch.bbv.mda.*;

/**
 * The tree checker completes the tagged values list of each meta element in the
 * model. This mechanism is used to dynamically support new cartridges or to
 * migrate models created with older versions of the application. It also
 * completes the tagged values received from the external model and extend them
 * with their tagged value type information.
 * @author marcelbaumann
 * @version $Revision$
 */
public class TreeChecker implements Visitor {
  /**
   * The item factory used to initialize the MDA model tag values.
   */
  private ItemFactory itemFactory;

  /**
   * Default constructor of the class.
   */
  public TreeChecker() {
    itemFactory = CartridgeFactory.getInstance().getItemFactory();
  }

  public void visit(MetaModel metaModel) {
    itemFactory.initialize(metaModel, MetaModel.class);
  }

  public void visit(MetaPackage metaPackage) {
    itemFactory.initialize(metaPackage, MetaPackage.class);
  }

  public void visit(MetaClass metaClass) {
    itemFactory.initialize(metaClass, MetaClass.class);
  }

  public void visit(MetaIndexedProperty property) {
    itemFactory.initialize(property, MetaIndexedProperty.class);
  }

  public void visit(MetaProperty property) {
    itemFactory.initialize(property, MetaProperty.class);
  }

  public void visit(MetaDatatype datatype) {
    // no MDA initialization for such items
  }

  public void visit(TaggedValue taggedValue) {
    // no MDA initialization for such items
  }
}