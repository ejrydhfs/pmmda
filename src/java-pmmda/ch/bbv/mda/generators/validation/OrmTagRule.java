/* copyright 2000-2005 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence");
 * you may not use this file except in compliance with the License.
 * $Id$
 */

package ch.bbv.mda.generators.validation;

import static ch.bbv.mda.cartridges.OrmConstants.COLUMN_NAME;
import static ch.bbv.mda.cartridges.OrmConstants.COLUMN_SIZE;
import static ch.bbv.mda.cartridges.OrmConstants.COLUMN_TYPE;

import java.util.List;

import ch.bbv.dog.validation.ValidationRuleResult;
import ch.bbv.mda.*;
import ch.bbv.mda.cartridges.*;
import ch.bbv.mda.generators.UmlConstants;

/**
 * The orm tag rule implements all validation rules for the tag values defined
 * in the orm cartridge. The rules are only applied to persistent data objects.
 * @author MarcelBaumann
 * @version $Revision$
 */
public class OrmTagRule extends TagRule {
  /**
   * Key for validation result message.
   */
  private static final String ERROR_MISSING_TABLE = "orm.error.missing-table";

  /**
   * Key for validation result message.
   */
  private static final String ERROR_MISSING_COLUMN = "orm.error.mssing-column";

  /**
   * Key for validation result message.
   */
  private static final String ERROR_MISSING_TYPE = "orm.error.missing-jdbc-type";

  /**
   * Key for validation result message.
   */
  private static final String ERROR_MISSING_LENGTH = "orm.error.missing-jdbc-length";

  /**
   * Key for validation result message.
   */
  private static final String WARNING_SUPERFLUOUS_LENGTH = "orm.warning.superflous-jdbc-length";

  /**
   * Default constructor of the class.
   */
  public OrmTagRule() {
    super(OrmCartridge.PREFIX);
  }

  protected boolean validate(MetaPackage packages,
      List<ValidationRuleResult> results) {
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
  protected boolean validate(MetaClass clazz, List<ValidationRuleResult> results) {
    boolean sucess = true;
    if (clazz.booleanValueOfTag(UmlConstants.PERSISTENCE_TAG)) {
      if (!exists(clazz, OrmConstants.TABLE_NAME)) {
        sucess = false;
        addErrorMessage(results, ERROR_MISSING_TABLE,
            UmlConstants.PERSISTENCE_TAG, clazz.qualifiedName());
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
  protected boolean validate(MetaProperty property,
      List<ValidationRuleResult> results) {
    boolean sucess = true;
    if (property.booleanValueOfTag(UmlConstants.PERSISTENCE_TAG)) {
      if (!exists(property, COLUMN_NAME)) {
        sucess = false;
        addErrorMessage(results, ERROR_MISSING_COLUMN, COLUMN_NAME, property
            .qualifiedName());
      }
      if (!BoConstants.DATA_OBJECT_STEREOTYPE.equals(property.getClassifier()
          .getStereotype())) {
        if (!exists(property, COLUMN_TYPE)) {
          sucess = false;
          addErrorMessage(results, ERROR_MISSING_TYPE, COLUMN_TYPE, property
              .qualifiedName());
        }
        String value = property.valueOfTag(COLUMN_TYPE);
        if (value.equalsIgnoreCase(JdbcTypeCode.VARCHAR.getShortDescription())
            || value.equalsIgnoreCase(JdbcTypeCode.CHAR.getShortDescription())) {
          if (!exists(property, COLUMN_SIZE)) {
            sucess = false;
            addErrorMessage(results, ERROR_MISSING_LENGTH, COLUMN_SIZE,
                property.qualifiedName());
          }
        } else {
          if (intValue(property, COLUMN_SIZE) != 0) {
            sucess = false;
            addErrorMessage(results, WARNING_SUPERFLUOUS_LENGTH, COLUMN_SIZE,
                property.qualifiedName());
          }
        }
      }
    }
    return sucess;
  }

  /**
   * No rules are defined for the element.
   * <ul>
   * <li>none</li>
   * </ul>
   * @param property element to validate.
   * @param results the validation results.
   * @return true if no error were found otherwise false.
   */
  protected boolean validate(MetaIndexedProperty property,
      List<ValidationRuleResult> results) {
    return true;
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

  /**
   * Returns the value of a numerical tag value. The default value follows the
   * convention of major programming languages.
   * @param element element containing the tag to check.
   * @param tag name of the tag to check.
   * @return the numerical value of the tag if defined otherwise 0.
   */
  private int intValue(MetaElement element, String tag) {
    int value = 0;
    if (exists(element, tag)) {
      Integer.parseInt(element.valueOfTag(tag));
    }
    return value;
  }
}