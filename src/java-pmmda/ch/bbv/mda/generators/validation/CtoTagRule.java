/* copyright 2000-2005 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence");
 * you may not use this file except in compliance with the License.
 * $Id$
 */

package ch.bbv.mda.generators.validation;

import java.util.List;

import ch.bbv.dog.validation.ValidationRuleResult;
import ch.bbv.mda.*;
import ch.bbv.mda.cartridges.OjbCartridge;

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
    super(OjbCartridge.PREFIX);
  }

  protected boolean validate(MetaPackage packages,
      List<ValidationRuleResult> results) {
    return true;
  }

  protected boolean validate(MetaClass clazz, List<ValidationRuleResult> results) {
    return true;
  }

  protected boolean validate(MetaProperty property,
      List<ValidationRuleResult> results) {
    return true;
  }

  protected boolean validate(MetaIndexedProperty property,
      List<ValidationRuleResult> results) {
    return true;
  }
}