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
 * @author MarcelBaumann
 * @version $Revision$
 */
public class OjbTagRule extends TagRule {
  public static final String ERROR_MISSING_TABLE = "ojb.error.missing-table";
  public static final String ERROR_MISSING_COLUMN = "ojb.error.mssing-column";
  public static final String ERROR_MISSING_TYPE = "ojb.error.missing-jdbc-type";
  public static final String ERROR_MISSING_LENGTH = "ojb.error.missing-jdbc-length";
  
  public static final String WARNING_SUPERFLUOUS_LENGTH = "ojb.warning.superflous-jdbc-length";
  
  public OjbTagRule() {
    super(OjbCartridge.PREFIX);
  }
  
  protected boolean validate(MetaPackage packages, List results) {
    return true;
  }

  protected boolean validate(MetaClass clazz, List results) {
    if (clazz.booleanValueOfTag(OjbCartridge.IS_PERSISTENT_TAG)) {
      if (!exists(clazz, OjbCartridge.TABLE_NAME_TAG)) {
        
      }
    }
    return true;
  }

  protected boolean validate(MetaProperty property, List results) {
    if (property.booleanValueOfTag(OjbCartridge.IS_PERSISTENT_TAG)) {
      if (!exists(property, OjbCartridge.COLUMN_NAME_TAG)) {
        // TODO add error
      }
      if (!exists(property, OjbCartridge.COLUMN_TYPE_TAG)) {
      }
      String value = property.valueOfTag(OjbCartridge.COLUMN_TYPE_TAG);
      if (value.equals(JdbcTypeCode.getCode(JdbcTypeCode.STRING)
          .getShortDescription())) {
        if (!exists(property, OjbCartridge.COLUMN_SIZE_TAG)) {
          // TODO add error
        }
      } else {
        if (!exists(property, OjbCartridge.COLUMN_SIZE_TAG)) {
          // TODO add warning
        }
      }
    }
    return true;
  }

  protected boolean validate(MetaIndexedProperty property, List results) {
    return true;
  }

  private boolean exists(MetaElement element, String tag) {
    String value = element.valueOfTag(tag);
    return (value != null && value.trim().length() > 0);
  }
}