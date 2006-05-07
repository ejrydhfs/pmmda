/* copyright 2000-2005 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence");
 * you may not use this file except in compliance with the License.
 * $Id$
 */

package ch.bbv.mda.generators.validation;

import java.util.List;

import ch.bbv.dog.validation.ValidationRuleResult;
import ch.bbv.mda.*;
import ch.bbv.mda.cartridges.BoCartridge;
import ch.bbv.mda.cartridges.BoConstants;

/**
 * The DOG tag rule implements all validation rules for the tag values defined
 * in the data object graph cartridge.
 * @author MarcelBaumann
 * @version $Revision$
 */
public class DogTagRule extends TagRule {
  /**
   * Key for validation result message.
   */
  private static final String ERROR_INCONSISTENT_TAG = "dog.error.inconsistent-persistence";

  /**
   * Key for validation result message.
   */
  private static final String ERROR_RESERVED_IDENTIFIER = "dog.error.reserved-identifier";

  /**
   * Reserved property name for the cartridge. User models should not use them.
   */
  private static final String IDENTIFIER = "id";

  /**
   * Reserved property name for the cartridge. User models should not use them.
   */
  private static final String MODIFIED = "modified";

  /**
   * Reserved property name for the cartridge. User models should not use them.
   */
  private static final String TIMESTAMP = "timestamp";

  /**
   * Default constructor of the class.
   */
  public DogTagRule() {
    super(BoCartridge.PREFIX);
  }

  protected boolean validate(MetaPackage packages, List results) {
    return true;
  }

  protected boolean validate(MetaClass clazz, List results) {
    return true;
  }

  /**
   * The following rules are defined for the element if its class is a data
   * object.
   * <ul>
   * <li>The property names "id", "modified" and "timestamp" are reserved for
   * internal functions of the cartridge.</li>
   * <li>If the property is persistent, it cannot be transient. If it is
   * transient, it cannot be persistent.</li>
   * </ul>
   * @param property element to validate.
   * @param results the validation results.
   * @return true if no error were found otherwise false.
   */
  protected boolean validate(MetaProperty property,
      List<ValidationRuleResult> results) {
    boolean success = true;
    MetaClass clazz = (MetaClass) property.getContext();
    if (clazz.hasStereotype(BoConstants.DATA_OBJECT_STEREOTYPE)) {
      if (property.booleanValueOfTag(UmlConstants.PERSISTENCE_TAG)
          && property.isTransient()) {
        success = false;
        addErrorMessage(results, ERROR_INCONSISTENT_TAG,
            UmlConstants.PERSISTENCE_TAG, property.qualifiedName());
      }
      String name = property.getName();
      if (name.equalsIgnoreCase(IDENTIFIER) || name.equalsIgnoreCase(TIMESTAMP)
          || name.equalsIgnoreCase(MODIFIED)) {
        success = false;
        addErrorMessage(results, ERROR_RESERVED_IDENTIFIER, BoCartridge.PREFIX,
            property.qualifiedName());
      }
    }
    return success;
  }

  protected boolean validate(MetaIndexedProperty property,
      List<ValidationRuleResult> results) {
    return true;
  }
}