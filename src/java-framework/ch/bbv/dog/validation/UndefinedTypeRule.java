/* copyright 2000-2006 Adrian Bachofen
 * Licensed under the Apache License, Version 2.0 (the "Licence");
 * you may not use this file except in compliance with the License.
 * $Id$
 */
package ch.bbv.dog.validation;

import java.util.List;
import java.util.ResourceBundle;

import ch.bbv.mda.MetaElement;
import ch.bbv.mda.MetaModel;
import ch.bbv.utilities.ReflectUtilities;

/**
 * The rule verifies that a property or an indexed property have a defined type (type within model)
 * @author Adrian Bachofen
 * @version $Revision$
 */
public class UndefinedTypeRule extends ValidationRuleImpl implements
    ValidationRule {
  /**
   * Key for validation result message.
   */
  private static final String UNDEFINED_TYPE = "ch.bbv.dog.undefined-type";

  /**
   * The resource bundle containing the messages for the validation messages.
   */
  private static ResourceBundle resources;

  static {
    resources = ResourceBundle.getBundle("ch.bbv.dog.validation.RulesMessages");
  }

  /**
   * Constructor of the class.
   * @param immediate true if the rule is immediate otherwise false.
   * @param severity the severity level of the rule.
   */
  public UndefinedTypeRule(boolean immediate, int severity) {
    super(immediate, severity);
  }

  @Override
  public boolean execute(Object bean, String property,
      List<ValidationRuleResult> results) {
    String value = (String) ReflectUtilities.getProperty(bean, property);
    if (value != null && !value.equals("")) {
      MetaModel metaModel = ((MetaElement)bean).getModel();
      if(metaModel.getClass(value) == null && metaModel.findDatatype(value) == null) {
        addMessage(results, resources, UNDEFINED_TYPE, getSeverity(),
            bean, property, null, null);
        return false;
      }
    }
    return true;
  }

  @Override
  public boolean execute(Object bean, String property, Object oldValue,
      Object newValue, List<ValidationRuleResult> results) {
    if(newValue != null && newValue instanceof String) {
      String value = (String)newValue;
      if (value != null && !value.equals("")) {
        MetaModel metaModel = ((MetaElement)bean).getModel();
        if(metaModel.getClass(value) == null && metaModel.findDatatype(value) == null) {
          addMessage(results, resources, UNDEFINED_TYPE, getSeverity(),
              bean, property, null, null);
          return false;
        }
      }
    }
    return true;
  }
}
