/* copyright 2000-2004 Marcel Baumann 
 * Licensed under the Apache License, Version 2.0 (the "Licence"); 
 * you may not use this file except in compliance with the License. 
 * $Id$ 
 */
package ch.bbv.mda;

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
  public void visit(MetaModel metaModel) {
    MetaFactory.getInstance().initialize(metaModel);
  }

  public void visit(MetaPackage metaPackage) {
    MetaFactory.getInstance().initialize(metaPackage);
  }

  public void visit(MetaClass metaClass) {
    MetaFactory.getInstance().initialize(metaClass);
  }

  public void visit(MetaIndexedProperty metaIndexedProperty) {
    MetaFactory.getInstance().initialize(metaIndexedProperty);
  }

  public void visit(MetaProperty metaProperty) {
    MetaFactory.getInstance().initialize(metaProperty);
  }

  public void visit(TaggedValue taggedValue) {
    // no MDA initialization for such items
  }
}