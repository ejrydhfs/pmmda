/* copyright 2000-2005 Marcel Baumann 
 * Licensed under the Apache License, Version 2.0 (the "Licence"); 
 * you may not use this file except in compliance with the License. 
 * $Id$ 
 */
package ch.bbv.mda;

/**
 * The meta data type models a primitive type of the model. Data types are build
 * in types in the target language.
 * @author MarcelBaumann
 * @version $Revision$
 */
public class MetaDatatype extends MetaClassifier {
  /**
   * Serial version identifier coded as back compatibility date.
   */
  private static final long serialVersionUID = 20041201;

  public void accept(Visitor visitor) {
    visitor.visit(this);
  }
}
