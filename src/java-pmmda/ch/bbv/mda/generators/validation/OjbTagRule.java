/* copyright 2000-2004 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence");
 * you may not use this file except in compliance with the License.
 * $Id$
 */

package ch.bbv.mda.generators.validation;

import java.util.List;

import ch.bbv.mda.*;
import ch.bbv.mda.generators.JdbcTypeCode;
import ch.bbv.mda.generators.OjbCartridge;

/**
 * The OJB tag rule implements all validation rules for the tag values defined
 * in the OJB cartridge.
 * @author MarcelBaumann
 * @version $Revision$
 */
public class OjbTagRule extends TagRule {
  /**
   * Key for validation result message.
   */
  private static final String ERROR_MISSING_TABLE = "ojb.error.missing-table";

  /**
   * Key for validation result message.
   */
  private static final String ERROR_MISSING_COLUMN = "ojb.error.mssing-column";

  /**
   * Key for validation result message.
   */
  private static final String ERROR_MISSING_TYPE = "ojb.error.missing-jdbc-type";

  /**
   * Key for validation result message.
   */
  private static final String ERROR_MISSING_LENGTH = "ojb.error.missing-jdbc-length";

  /**
   * Key for validation result message.
   */
  private static final String WARNING_SUPERFLUOUS_LENGTH = "ojb.warning.superflous-jdbc-length";

  /**
   * Default constructor of the class.
   */
  public OjbTagRule() {
    super(OjbCartridge.PREFIX);
  }

  protected boolean validate(MetaPackage packages, List results) {
    return true;
  }

  /**
   * The following rules are defined for the element.
   * <ul>
   * <li>If the class is persistent, it must have a table name.</li>
   * </ul>
   * @param clazz element to validate.
   * @param results the validation results.
   * @return true if no error were found otherwise false.
   */
  protected boolean validate(MetaClass clazz, List results) {
    boolean sucess = true;
    if (clazz.booleanValueOfTag(OjbCartridge.IS_PERSISTENT_TAG)) {
      if (!exists(clazz, OjbCartridge.TABLE_NAME_TAG)) {
        sucess = false;
        addErrorMessage(results, ERROR_MISSING_TABLE,
            OjbCartridge.IS_PERSISTENT_TAG, clazz.getName());
      }
    }
    return sucess;
  }

  /**
   * The following rules are defined for the element.
   * <ul>
   * <li>If the property is persistent, it must have a column name.</li>
   * <li>If the property is persistent, it must have a JDBC type.</li>
   * <li>If the JDBC type is string, it must have a size.</li>
   * <li>If the JDBC type is not string, it should not have a size.</li>
   * <li>If the property is locking, its JDBC type must be either integer or
   * timestamp.</li>
   * </ul>
   * @param property element to validate.
   * @param results the validation results.
   * @return true if no error were found otherwise false.
   */
  protected boolean validate(MetaProperty property, List results) {
    boolean sucess = true;
    if (!property.isTransient()) {
      if (!exists(property, OjbCartridge.COLUMN_NAME_TAG)) {
        sucess = false;
        addErrorMessage(results, ERROR_MISSING_COLUMN,
            OjbCartridge.COLUMN_NAME_TAG, property.getName());
      }
      if (!exists(property, OjbCartridge.COLUMN_TYPE_TAG)) {
        sucess = false;
        addErrorMessage(results, ERROR_MISSING_TYPE,
            OjbCartridge.COLUMN_TYPE_TAG, property.getName());
      }
      String value = property.valueOfTag(OjbCartridge.COLUMN_TYPE_TAG);
      if (value.equals(JdbcTypeCode.getCode(JdbcTypeCode.STRING)
          .getShortDescription())) {
        if (!exists(property, OjbCartridge.COLUMN_SIZE_TAG)) {
          sucess = false;
          addErrorMessage(results, ERROR_MISSING_LENGTH,
              OjbCartridge.COLUMN_SIZE_TAG, property.getName());
        }
      } else {
        if (exists(property, OjbCartridge.COLUMN_SIZE_TAG)) {
          sucess = false;
          addErrorMessage(results, WARNING_SUPERFLUOUS_LENGTH,
              OjbCartridge.COLUMN_SIZE_TAG, property.getName());
        }
      }
    }
    return sucess;
  }

  /**
   * The following rules are defined for the element.
   * <ul>
   * <li>The indexed property is persistent must have a column name. This name
   * is used to generate the name of the foreign key columns.</li>
   * </ul>
   * @param property element to validate.
   * @param results the validation results.
   * @return true if no error were found otherwise false.
   */
  protected boolean validate(MetaIndexedProperty property, List results) {
    boolean sucess = true;
    if (!property.isTransient()) {
      if (!exists(property, OjbCartridge.COLUMN_NAME_TAG)) {
        sucess = false;
        addErrorMessage(results, ERROR_MISSING_COLUMN,
            OjbCartridge.COLUMN_NAME_TAG, property.getName());
      }
    }
    return sucess;
  }

  /**
   * Checks if the tag is defined and its length is greater than zero.
   * @param element element containing the tag to check.
   * @param tag name of the tag to check.
   * @return true if the tag contains a value otherwise false.
   */
  private boolean exists(MetaElement element, String tag) {
    String value = element.valueOfTag(tag);
    return (value != null && value.trim().length() > 0);
  }
}