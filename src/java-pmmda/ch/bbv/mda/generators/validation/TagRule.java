/* 
 * copyright 2000-2005 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence");
 * you may not use this file except in compliance with the License.
 * $Id$
 */

package ch.bbv.mda.generators.validation;

import java.text.MessageFormat;
import java.util.ResourceBundle;

import ch.bbv.dog.validation.ValidationRule;
import ch.bbv.dog.validation.ValidationRuleImpl;
import ch.bbv.dog.validation.Validator;
import ch.bbv.mda.MetaClass;
import ch.bbv.mda.MetaIndexedProperty;
import ch.bbv.mda.MetaPackage;
import ch.bbv.mda.MetaProperty;
import ch.bbv.mda.operators.ModelOperators;

import com.jgoodies.validation.Severity;
import com.jgoodies.validation.ValidationMessage;
import com.jgoodies.validation.ValidationResult;
import com.jgoodies.validation.message.SimpleValidationMessage;

/**
 * The tag rule implements mechanisms to validate tagged values in the validator
 * framework. All messages about tag rule results have three parameters. The
 * prefix of the cartridge, the tag name and a parameter. The properties file
 * for the derived classes must be located in this package. Developers are free
 * to define their own directory tree to seperate messages from their source
 * code artifacts.
 * @author MarcelBaumann
 * @version $Revision$
 */
public abstract class TagRule extends ValidationRuleImpl implements
    ValidationRule {
  /**
   * The path to the package containing all message property files.
   */
  private static final String MESSAGES_PACKAGAGE_NAME = "ch.bbv.mda.generators.validation.";

  /**
   * The prefix of the cartridge which tagged value validation rules are defined
   * in the class.
   */
  private String prefix;

  /**
   * The resource bundle containing the messages for the validation messages.
   */
  private ResourceBundle resources;

  /**
   * Registers the tag rules for all elements of the meta model.
   * @param validator validator where the rules are registered.
   */
  public void register(Validator validator) {
    validator.registerRule(this, MetaPackage.class);
    validator.registerRule(this, MetaClass.class);
    validator.registerRule(this, MetaProperty.class);
    validator.registerRule(this, MetaIndexedProperty.class);
  }

  /**
   * Constructor of the class.
   * @param prefix prefix of the cartridge owning the rules.
   */
  public TagRule(String prefix) {
    super(false, Severity.ERROR);
    this.prefix = prefix;
    StringBuilder buffer = new StringBuilder();
    buffer.append(MESSAGES_PACKAGAGE_NAME).append(
        ModelOperators.identifierFirstUpper(prefix)).append("RulesMessages");
    resources = ResourceBundle.getBundle(buffer.toString());
  }

  public boolean isImmediate() {
    return false;
  }

  @Override
  public boolean execute(Object bean, ValidationResult results) {
    boolean sucess = true;
    if (bean instanceof MetaPackage) {
      sucess = validate((MetaPackage) bean, results);
    } else if (bean instanceof MetaClass) {
      sucess = validate((MetaClass) bean, results);
    } else if (bean instanceof MetaIndexedProperty) {
      sucess = validate((MetaIndexedProperty) bean, results);
    } else if (bean instanceof MetaProperty) {
      sucess = validate((MetaProperty) bean, results);
    }
    return sucess;
  }

  public boolean execute(Object bean, String property, ValidationResult results) {
    boolean sucess = true;
    if (bean instanceof MetaPackage) {
      sucess = validate((MetaPackage) bean, results);
    } else if (bean instanceof MetaClass) {
      sucess = validate((MetaClass) bean, results);
    } else if (bean instanceof MetaIndexedProperty) {
      sucess = validate((MetaIndexedProperty) bean, results);
    } else if (bean instanceof MetaProperty) {
      sucess = validate((MetaProperty) bean, results);
    }
    return sucess;
  }

  public boolean execute(Object bean, String property, Object oldValue,
      Object newValue, ValidationResult results) {
    return execute(bean, property, results);
  }

  /**
   * Validates the element with the registered rules and stores the findings in
   * the result list.
   * @param element element to validate
   * @param results results containing the findings of the validation
   * @return true if no errors were found otherwise false
   * @pre (element != null) && (results != null)
   */
  protected abstract boolean validate(MetaPackage element,
      ValidationResult results);

  /**
   * Validates the element with the registered rules and stores the findings in
   * the result list.
   * @param element element to validate
   * @param results results containing the findings of the validation
   * @return true if no errors were found otherwise false
   * @pre (element != null) && (results != null)
   */
  protected abstract boolean validate(MetaClass element,
      ValidationResult results);

  /**
   * Validates the element with the registered rules and stores the findings in
   * the result list.
   * @param element element to validate
   * @param results results containing the findings of the validation
   * @return true if no errors were found otherwise false
   * @pre (element != null) && (results != null)
   */
  protected abstract boolean validate(MetaProperty element,
      ValidationResult results);

  /**
   * Validates the element with the registered rules and stores the findings in
   * the result list.
   * @param element element to validate
   * @param results results containing the findings of the validation
   * @return true if no errors were found otherwise false
   * @pre (element != null) && (results != null)
   */
  protected abstract boolean validate(MetaIndexedProperty element,
      ValidationResult results);

  /**
   * Returns the message associated with the given key.
   * @param key key of the message to retrieve
   * @return the requested message if found otherwise null
   * @pre key != null
   */
  protected String getString(String key) {
    assert key != null;
    return resources.getString(key);
  }

  /**
   * Adds an error message to the list of results.
   * @param results results containing the messages
   * @param key key of the message
   * @param tag tag origin of the error
   * @param name name of the item
   */
  protected void addErrorMessage(ValidationResult results, String key,
      String tag, String name) {
    addMessage(results, Severity.ERROR, key, tag, name);
  }

  /**
   * Adds a warning message to the list of results.
   * @param results results containing the messages.
   * @param key key of the message.
   * @param tag tag origin of the error.
   * @param name name of the item.
   */
  protected void addWarningMessage(ValidationResult results, String key,
      String tag, String name) {
    addMessage(results, Severity.WARNING, key, tag, name);
  }

  /**
   * Adds an error message to the list of results.
   * @param results results containing the messages
   * @param severity severity of the message
   * @param key key of the message
   * @param tag tag origin of the error
   * @param name name of the item
   */
  protected void addMessage(ValidationResult results, Severity severity,
      String key, String tag, String name) {
    Object[] parameters = { prefix, tag, name };
    String text = MessageFormat.format(resources.getString(key), parameters);
    ValidationMessage message = new SimpleValidationMessage(text, severity);
  }
}