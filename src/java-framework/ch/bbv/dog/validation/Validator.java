/* copyright 2000-2004 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence");
 * you may not use this file except in compliance with the License.
 * $Id$
 */

package ch.bbv.dog.validation;

import java.beans.*;
import java.util.ArrayList;
import java.util.List;

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
   * Creates a new validator in the application. Multiple validators can be used
   * to manage different sets of validation rules.
   * @param name unique name identifying the validator as component.
   */
  public Validator(String name) {
    super(name);
    key = "Validator-Rules-" + name;
  }

  /**
   * Validates the data objects and all its simple and indexed properties.
   * @param object data object to validate.
   * @param results list to which validation rule results are added.
   */
  public void validate(DataObject object, List results) {
    PropertyDescriptor[] descriptors = getBeanInfo(object.getClass())
        .getPropertyDescriptors();
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
      List results) {
    if (log.isTraceEnabled()) {
      StringBuffer buffer = new StringBuffer();
      buffer.append("Start validation for ").append(root.getClass())
          .append(" ").append(root.getId());
      log.trace(buffer.toString());
    }
    ValidateFunctor functor = new ValidateFunctor(this);
    visitor.setFunctor(functor);
    DataObjectType.invokeVisitor(root, visitor);
    results.addAll(functor.getResults());
    if (log.isTraceEnabled()) {
      StringBuffer buffer = new StringBuffer();
      buffer.append("End validation for ").append(root.getClass())
          .append(" ").append(root.getId());
      log.trace(buffer.toString());
    }
  }

  /**
   * Registers a validation rule.
   * @param rule rule to register.
   * @param clazz class on which instances the rule will be applied.
   * @param property property on which the rule will be applied.
   */
  public void registerRule(ValidationRule rule, Class clazz, String property) {
    List availableRules = findOrCreate(clazz, property);
    availableRules.add(rule);
  }

  /**
   * Registers a set of validation rules.
   * @param rules rules to register.
   * @param clazz class on which instances the rule will be applied.
   * @param property property on which the rule will be applied.
   */
  public void registerRules(List rules, Class clazz, String property) {
    List availableRules = findOrCreate(clazz, property);
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
  private List findOrCreate(Class clazz, String property) {
    PropertyDescriptor descriptor = getDescriptor(clazz, property);
    List availableRules = (List) descriptor.getValue(key);
    if (availableRules == null) {
      availableRules = new ArrayList();
      descriptor.setValue(key, availableRules);
    }
    return availableRules;
  }
}