/* copyright 2000-2004 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence");
 * you may not use this file except in compliance with the License.
 * $Id$
 */

package ch.bbv.mda.generators.validation;

import java.util.List;
import java.util.ResourceBundle;

import ch.bbv.dog.validation.ValidationRule;
import ch.bbv.dog.validation.ValidationRuleResult;
import ch.bbv.mda.*;

/**
 * @author MarcelBaumann
 * @version $Revision$
 */
public abstract class TagRule implements ValidationRule {
  /**
   * The prefix of the cartridge which tagged value validation rules are defined
   * in the class.
   */
  private String prefix;

  private ResourceBundle resources;

  public TagRule(String prefix) {
    this.prefix = prefix;
    resources = ResourceBundle.getBundle(prefix + "-rules");

  }

  public boolean isImmediate() {
    return false;
  }

  public boolean execute(Object bean, String property, List results) {
    boolean sucess = true;
    if (bean instanceof MetaPackage) {
      validate((MetaPackage) bean, results);
    } else if (bean instanceof MetaClass) {
      validate((MetaClass) bean, results);
    } else if (bean instanceof MetaIndexedProperty) {
      validate((MetaIndexedProperty) bean, results);
    } else if (bean instanceof MetaProperty) {
      validate((MetaProperty) bean, results);
    }
    return sucess;
  }

  public boolean execute(Object bean, String property, Object oldValue,
      Object newValue, List results) {
    return true;
  }

  protected abstract boolean validate(MetaPackage packages, List results);

  protected abstract boolean validate(MetaClass clazz, List results);

  protected abstract boolean validate(MetaProperty property, List results);

  protected abstract boolean validate(MetaIndexedProperty property, List results);

  protected String getString(String key) {
    return resources.getString(key);
  }

  protected void addErrorMessage(List results, String key, String itemName) {
    addMessage(results, ValidationRuleResult.ERROR, key, itemName);
  }

  protected void addWarningMessage(List results, String key, String itemName) {
    addMessage(results, ValidationRuleResult.WARNING, key, itemName);
  }

  protected void addMessage(List results, int severity, String key, String itemName) {
    ValidationRuleResult result = new ValidationRuleResult();
    result.setSeverity(severity);
    result.setKey(key);
    Object[] parameters = { itemName };
    result.setText(getString(key), parameters);
    results.add(result);
  }
}