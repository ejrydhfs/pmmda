/* copyright 2000-2004 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence");
 * you may not use this file except in compliance with the License.
 * $Id$
 */

package ch.bbv.mda.generators.validation;

import java.util.List;

import ch.bbv.mda.*;
import ch.bbv.mda.generators.OjbCartridge;

/**
 * The BOG tag rule implements all validation rules for the tag values defined
 * in the BOG cartridge.
 * @author MarcelBaumann
 * @version $Revision$
 */
public class CtoTagRule extends TagRule {
  /**
   * Key for validation result message.
   */
  private static final String ERROR_MISSING_TABLE = "ojb.error.missing-table";

  /**
   * Default constructor of the class.
   */
  public CtoTagRule() {
    super(OjbCartridge.PREFIX);
  }

  protected boolean validate(MetaPackage packages, List results) {
    return true;
  }

  protected boolean validate(MetaClass clazz, List results) {
    return true;
  }

  protected boolean validate(MetaProperty property, List results) {
    return true;
  }

  protected boolean validate(MetaIndexedProperty property, List results) {
    return true;
  }
}