/* copyright 2000-2005 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence");
 * you may not use this file except in compliance with the License.
 * $Id$
 */

package ch.bbv.mda.generators.validation;

import ch.bbv.mda.MetaClass;
import ch.bbv.mda.MetaIndexedProperty;
import ch.bbv.mda.MetaPackage;
import ch.bbv.mda.MetaProperty;
import ch.bbv.mda.cartridges.OrmCartridge;

import com.jgoodies.validation.ValidationResult;

/**
 * The CTO tag rule implements all validation rules for the tag values defined
 * in the CORBA transfer object cartridge.
 * @author MarcelBaumann
 * @version $Revision$
 */
public class CtoTagRule extends TagRule {
  /**
   * Default constructor of the class.
   */
  public CtoTagRule() {
    super(OrmCartridge.PREFIX);
  }

  protected boolean validate(MetaPackage packages, ValidationResult results) {
    return true;
  }

  protected boolean validate(MetaClass clazz, ValidationResult results) {
    return true;
  }

  protected boolean validate(MetaProperty property, ValidationResult results) {
    return true;
  }

  protected boolean validate(MetaIndexedProperty property,
      ValidationResult results) {
    return true;
  }
}