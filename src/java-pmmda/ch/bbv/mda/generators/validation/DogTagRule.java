/* copyright 2000-2004 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence");
 * you may not use this file except in compliance with the License.
 * $Id$
 */

package ch.bbv.mda.generators.validation;

import java.util.List;

import ch.bbv.mda.*;
import ch.bbv.mda.generators.DogCartridge;

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
   * Default constructor of the class.
   */
  public DogTagRule() {
    super(DogCartridge.PREFIX);
  }

  protected boolean validate(MetaPackage packages, List results) {
    return true;
  }

  protected boolean validate(MetaClass clazz, List results) {
    return true;
  }

  /**
   * The following rules are defined for the element.
   * <ul>
   * <li>If the property is persistent, it cannot be transient. If it is
   * transient, it cannot be peristent.</li>
   * </ul>
   * @param property element to validate.
   * @param results the validation results.
   * @return true if no error were found otherwise false.
   */
  protected boolean validate(MetaProperty property, List results) {
    boolean success = true;
    if (property.booleanValueOfTag(DogCartridge.PERSISTENCE_TAG)
        && property.isTransient()) {
      success = false;
      addErrorMessage(results, ERROR_INCONSISTENT_TAG,
          DogCartridge.PERSISTENCE_TAG, property.qualifiedName());
    }
    return success;
  }

  protected boolean validate(MetaIndexedProperty property, List results) {
    return true;
  }
}