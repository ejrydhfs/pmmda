/* 
 * copyright 2000-2006 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence");
 * you may not use this file except in compliance with the License.
 * $Id$
 */

package ch.bbv.dog.validation;

import java.util.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ch.bbv.application.ComponentImpl;
import ch.bbv.dog.AbstractVisitorFunctor;
import ch.bbv.dog.DataObject;
import ch.bbv.dog.DataObjectType;

/**
 * The validator component is responsible to apply validation rules on business
 * object graphs. The name of the validator identifies the component in the
 * application and is used to uniquely identify the set of rules it uses for a
 * property or a class. This mechanism allows the definition of multiple
 * validation rule sets in the same application. All rules are registered in the
 * same bean info instance.
 * @author MarcelBaumann
 * @version $Revision$
 */
public class Validator extends ComponentImpl {
  /**
   * The default name of the validator compoenent.
   */
  public static final String NAME = "Validator";

  /**
   * Logger for all instances of the class.
   */
  private static Log log = LogFactory.getLog(Validator.class);

  /**
   * The rules defined at class scope for this validator.
   */
  private Map<Class, ValidationInfo> infos;

  static class ValidationInfo {
    private List<ValidationRule> classRules;

    private Map<String, List<ValidationRule>> propertyRules;

    /**
     * Default constructor of the class.
     */
    public ValidationInfo() {
      classRules = new ArrayList<ValidationRule>();
      propertyRules = new HashMap<String, List<ValidationRule>>();
    }

    /**
     * Returns the validation rules of the class.
     * @return the list of validation rules or an empty list.
     */
    public List<ValidationRule> getRules() {
      return classRules;
    }

    /**
     * Returns the property rules for all property of the class.
     * @return the map of all property rules.
     */
    public Map<String, List<ValidationRule>> getPropertyRules() {
      return propertyRules;
    }

    /**
     * Returns the validation rules of the property.
     * @param property property which validation rules are requested.
     * @return the list of defined rules or an empty list.
     */
    public List<ValidationRule> getRules(String property) {
      List<ValidationRule> rules = propertyRules.get(property);
      if (rules == null) {
        rules = Collections.emptyList();
      }
      return rules;
    }

    /**
     * Registers a validation rule defined for a class.
     * @param rule rule to register.
     * @pre (rule != null) && (clazz != null)
     */
    public void registerRule(ValidationRule rule) {
      assert rule != null;
      classRules.add(rule);
    }

    /**
     * Registers a validation rule.
     * @param rule rule to register.
     * @param property property on which the rule will be applied.
     * @pre (rule != null) && (clazz != null) && (property != null)
     */
    public void registerRule(ValidationRule rule, String property) {
      assert (rule != null) && (property != null);
      List<ValidationRule> rules = propertyRules.get(property);
      if (rules == null) {
        rules = new ArrayList<ValidationRule>();
        propertyRules.put(property, rules);
      }
      if (!rules.contains(rule)) {
        rules.add(rule);
      }
    }
  }

  /**
   * Creates a new validator in the application. Multiple validators can be used
   * to manage different sets of validation rules.
   * @param name unique name identifying the validator as component.
   * @pre name != null
   */
  public Validator(String name) {
    super(name);
    infos = new HashMap<Class, ValidationInfo>();
  }

  /**
   * Validates the data object and all its simple and indexed properties.
   * @param object data object to validate.
   * @param results list to which validation rule results are added.
   * @pre (object != null) && (results != null)
   */
  public void validate(DataObject object, List<ValidationRuleResult> results) {
    assert (object != null) && (results != null);
    ValidationInfo info = infos.get(object.getClass());
    if (info != null) {
      for (ValidationRule rule : info.getRules()) {
        rule.execute(object, results);
      }
      Map<String, List<ValidationRule>> rules = info.getPropertyRules();
      for (String property : rules.keySet()) {
        for (ValidationRule rule : rules.get(property)) {
          rule.execute(object, property, results);
        }
      }
    }
  }

  /**
   * Validates the data object graph with all its nodes and their simple and
   * indexed properties. The graph is traversed using the visitor pattern.
   * @param root root data object of the graph to visit.
   * @param visitor visitor used to traverse the graph.
   * @param results list to which validation rule results are added.
   * @pre (root != null) && (visitor != null) && (results != null)
   */
  public void validate(DataObject root, AbstractVisitorFunctor visitor,
      List<ValidationRuleResult> results) {
    assert (root != null) && (visitor != null) && (results != null);
    if (log.isTraceEnabled()) {
      StringBuilder buffer = new StringBuilder();
      buffer.append("Start validation for ").append(root.getClass())
          .append(" ").append(root.getId());
      log.trace(buffer.toString());
    }
    ValidateFunctor functor = new ValidateFunctor(this);
    visitor.setFunctor(functor);
    validationStart(results);
    DataObjectType.invokeVisitor(root, visitor);
    validationEnd(results);
    results.addAll(functor.getResults());
    if (log.isTraceEnabled()) {
      StringBuilder buffer = new StringBuilder();
      buffer.append("End validation for ").append(root.getClass()).append(" ")
          .append(root.getId());
      log.trace(buffer.toString());
    }
  }

  /**
   * Registers a validation rule defined for a class.
   * @param rule rule to register.
   * @param clazz class on which instances the rule will be applied.
   * @pre (rule != null) && (clazz != null)
   */
  public void registerRule(ValidationRule rule, Class clazz) {
    assert (rule != null) && (clazz != null);
    ValidationInfo info = infos.get(clazz);
    if (info == null) {
      info = new ValidationInfo();
      infos.put(clazz, info);
    }
    info.registerRule(rule);
  }

  /**
   * Registers a validation rule.
   * @param rule rule to register.
   * @param clazz class on which instances the rule will be applied.
   * @param property property on which the rule will be applied.
   * @pre (rule != null) && (clazz != null) && (property != null)
   */
  public void registerRule(ValidationRule rule, Class clazz, String property) {
    assert (rule != null) && (clazz != null) && (property != null);
    ValidationInfo info = infos.get(clazz);
    if (info == null) {
      info = new ValidationInfo();
      infos.put(clazz, info);
    }
    info.registerRule(rule, property);
  }

  /**
   * Indicates that validation starts now. This can be used by rules to perform
   * preprocessing.
   * @param results list to which validation rule results are added.
   */
  private void validationStart(List<ValidationRuleResult> results) {
    for (ValidationInfo info : infos.values()) {
      for (ValidationRule rule : info.getRules()) {
        rule.validationStart(results);
      }
      Map<String, List<ValidationRule>> rules = info.getPropertyRules();
      for (String property : rules.keySet()) {
        for (ValidationRule rule : rules.get(property)) {
          rule.validationStart(results);
        }
      }
    }
  }

  /**
   * Indicates that validation is complete now. This can be used by rules to
   * perform postprocessing.
   * @param results list to which validation rule results are added.
   */
  private void validationEnd(List<ValidationRuleResult> results) {
    for (ValidationInfo info : infos.values()) {
      for (ValidationRule rule : info.getRules()) {
        rule.validationEnd(results);
      }
      Map<String, List<ValidationRule>> rules = info.getPropertyRules();
      for (String property : rules.keySet()) {
        for (ValidationRule rule : rules.get(property)) {
          rule.validationEnd(results);
        }
      }
    }
  }
}