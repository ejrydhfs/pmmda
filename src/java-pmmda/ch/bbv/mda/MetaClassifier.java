/* copyright 2000-2005 Marcel Baumann 
 * Licensed under the Apache License, Version 2.0 (the "Licence"); 
 * you may not use this file except in compliance with the License. 
 * $Id$ 
 */
package ch.bbv.mda;

/**
 * The classifier class defines types in a model. The definition is the same as
 * the one defined the UML MOF definition.
 * @author MarcelBaumann
 * @version $Revision$
 */
public abstract class MetaClassifier extends MetaElement {
  /**
   * @see MetaElement#qualifiedName()
   */
  public String getQualifiedName() {
    return qualifiedName();
  }
}
