/* 
 * copyright 2000-2005 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence");
 * you may not use this file except in compliance with the License.
 * $Id$
 */

package ch.bbv.dog.validation;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
   * Key of the validator used to identify the set of rules it uses.
   */
  private String key;

  /**
   * The rules defined at class scope for this validator.
   */
  private Map<BeanInfo, List<ValidationRule>> rules;

  /**
   * Creates a new validator in the application. Multiple validators can be used
   * to manage different sets of validation rules.
   * @param name unique name identifying the validator as component.
   */
  public Validator(String name) {
    super(name);
    key = "Validator-Rules-" + name;
    rules = new HashMap<BeanInfo, List<ValidationRule>>();
  }

  /**
   * Validates the data objects and all its simple and indexed properties.
   * @param object data object to validate.
   * @param results list to which validation rule results are added.
   */
  public void validate(DataObject object, List<ValidationRuleResult> results) {
    BeanInfo beanInfo = getBeanInfo(object.getClass());
    List<ValidationRule> beanRules = rules.get(beanInfo);
    if (beanRules != null) {
      for (int i = 0; i < beanRules.size(); i++) {
        beanRules.get(i).execute(object, results);

      }
    }
    PropertyDescriptor[] descriptors = beanInfo.getPropertyDescriptors();
    for (int i = 0; i < descriptors.length; i++) {
      List rules = (List) descriptors[i].getValue(key);
      if (rules != null) {
        for (int j = 0, size = rules.size(); j < size; j++) {
          ((ValidationRule) rules.get(j)).execute(object, descriptors[i]
              .getName(), results);
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
   */
  public void validate(DataObject root, AbstractVisitorFunctor visitor,
      List<ValidationRuleResult> results) {
    if (log.isTraceEnabled()) {
      StringBuilder buffer = new StringBuilder();
      buffer.append("Start validation for ").append(root.getClass())
          .append(" ").append(root.getId());
      log.trace(buffer.toString());
    }
    ValidateFunctor functor = new ValidateFunctor(this);
    visitor.setFunctor(functor);
    DataObjectType.invokeVisitor(root, visitor);
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
   */
  public void registerRule(ValidationRule rule, Class clazz) {
    BeanInfo beanInfo = getBeanInfo(clazz);
    List<ValidationRule> availableRules = rules.get(beanInfo);
    if (availableRules == null) {
      availableRules = new ArrayList<ValidationRule>();
      rules.put(beanInfo, availableRules);
    }
    availableRules.add(rule);
  }

  /**
   * Registers a validation rule.
   * @param rule rule to register.
   * @param clazz class on which instances the rule will be applied.
   * @param property property on which the rule will be applied.
   */
  public void registerRule(ValidationRule rule, Class clazz, String property) {
    List<ValidationRule> availableRules = findOrCreate(clazz, property);
    availableRules.add(rule);
  }

  /**
   * Registers a set of validation rules.
   * @param rules rules to register.
   * @param clazz class on which instances the rule will be applied.
   * @param property property on which the rule will be applied.
   */
  public void registerRules(List<ValidationRule> rules, Class clazz,
      String property) {
    List<ValidationRule> availableRules = findOrCreate(clazz, property);
    availableRules.addAll(rules);
  }

  /**
   * Returns the bean info associated with the class.
   * @param clazz class which bean info is requested.
   * @return the requested bean info.
   */
  protected BeanInfo getBeanInfo(Class clazz) {
    try {
      return Introspector.getBeanInfo(clazz);
    } catch (IntrospectionException e) {
      log.error(e);
    }
    return null;
  }

  /**
   * Returns the property descriptor associated with the property.
   * @param clazz class containing the requested property.
   * @param property name of the property.
   * @return the requested property descriptor.
   * @throws IllegalArgumentException the property does not exist.
   */
  protected PropertyDescriptor getDescriptor(Class clazz, String property) {
    PropertyDescriptor[] descriptors = getBeanInfo(clazz)
        .getPropertyDescriptors();
    for (int i = 0; i < descriptors.length; ++i) {
      if (descriptors[i].getName().equals(property)) {
        return descriptors[i];
      }
    }
    throw new IllegalArgumentException("Property not found " + clazz.getName()
        + "." + property);
  }

  /**
   * Returns the list of rules associated with the property. If none exists, a
   * new one is created and returned.
   * @param clazz class containing the requested property.
   * @param property name of the property.
   * @return the list of rules of the property.
   */
  private List<ValidationRule> findOrCreate(Class clazz, String property) {
    PropertyDescriptor descriptor = getDescriptor(clazz, property);
    List<ValidationRule> availableRules = (List<ValidationRule>) descriptor
        .getValue(key);
    if (availableRules == null) {
      availableRules = new ArrayList<ValidationRule>();
      descriptor.setValue(key, availableRules);
    }
    return availableRules;
  }
}