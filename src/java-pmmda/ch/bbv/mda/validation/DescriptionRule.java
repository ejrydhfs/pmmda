/* copyright 2000-2005 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence");
 * you may not use this file except in compliance with the License.
 * $Id$
 */

package ch.bbv.mda.validation;

import java.util.List;

import ch.bbv.dog.validation.ValidationRule;
import ch.bbv.dog.validation.ValidationRuleImpl;
import ch.bbv.dog.validation.ValidationRuleResult;
import ch.bbv.mda.MetaClass;
import ch.bbv.mda.MetaPackage;
import ch.bbv.mda.MetaProperty;

/**
 * The description rule verifies that packages and classes have a description.
 * Only non system packages and classes are verified.
 * @author Marcel Baumann
 * @version $Revision$
 */
public class DescriptionRule extends ValidationRuleImpl implements
    ValidationRule {
  private static final String PACKAGE_DESCRIPTION_WARNING = "ch.bbv.mda.package-description-warning";

  private static final String CLASS_DESCRIPTION_WARNING = "ch.bbv.mda.class-description-warning";

  private static final String PROPERTY_DESCRIPTION_WARNING = "ch.bbv.mda.property-description-warning";

  /**
   * Constructor of the class.
   * @param immediate true if the rule is immediate otherwise false.
   * @param severity the severity level of the rule.
   */
  public DescriptionRule(boolean immediate, int severity) {
    super(immediate, severity);
  }

  public boolean execute(Object bean, String property,
      List<ValidationRuleResult> results) {
    boolean success = true;
    if (bean instanceof MetaPackage) {
      MetaPackage element = (MetaPackage) bean;
      if (!element.isSystem() && (element.getClasses().size() > 0)
          && element.getDescription() == null) {
        addMessage(results, RulesFactory.resources, PACKAGE_DESCRIPTION_WARNING,
            getSeverity(), bean, property, null, null);
        success = false;

      }
    } else if (bean instanceof MetaClass) {
      MetaClass element = (MetaClass) bean;
      if (!element.isSystem() && element.getDescription() == null) {
        addMessage(results, RulesFactory.resources, CLASS_DESCRIPTION_WARNING,
            getSeverity(), bean, property, null, null);
        success = false;
      }
    } else if (bean instanceof MetaProperty) {
      MetaProperty element = (MetaProperty) bean;
      MetaClass clazz = (MetaClass) element.getContext();
      if (!clazz.isSystem() && element.getDescription() == null) {
        addMessage(results, RulesFactory.resources, PROPERTY_DESCRIPTION_WARNING,
            getSeverity(), bean, property, null, null);
        success = false;
      }
    }
    return success;
  }

}