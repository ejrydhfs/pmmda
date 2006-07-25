/* copyright 2000-2006 Marcel Baumann 
 * Licensed under the Apache License, Version 2.0 (the "Licence"); 
 * you may not use this file except in compliance with the License. 
 * $Id$ 
 */

package ch.bbv.mda.persistence.mdr;

import java.util.Collection;
import java.util.Iterator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.omg.uml.UmlPackage;

import ch.bbv.mda.MetaClass;
import ch.bbv.mda.MetaPackage;

public class MdrFacade {

  /**
   * umlPackage
   */
  private UmlPackage umlPackage;

  /**
   * Logger
   */
  private Log log = LogFactory.getLog(MdrFacade.class);

  public MdrFacade(UmlPackage umlPackage) {
    this.umlPackage = umlPackage;
  }

  public MetaPackage getPackage(String qualifiedName) {
    Collection collection = umlPackage.getCore().refAllPackages();
    // Collection collection =
    // umlPackage.getCore().getNamespace().refAllOfType();
    Iterator iter = collection.iterator();

    return null;
  }

  /**
   * addPackages
   * @param qualifiedName
   */
  public void addPackage(String qualifiedName) {
    log.info(qualifiedName);
    log.error(qualifiedName);
  }

  public void deletePackage(MetaPackage packages) {

  }

  public void movePackage(MetaPackage packages) {

  }

  public void renamePackage(MetaPackage packages) {

  }

  public void modifyPackage(MetaPackage packages) {

  }

  public void addClass(String qualifiedName) {

  }

  public void deleteClass(MetaClass clazz) {

  }

  public void moveClass(MetaClass clazz) {

  }

  public void renameClass(MetaClass clazz, String newName) {

  }

  public void modifyClass(MetaClass clazz, String newName) {

  }
}
