/* copyright 2000-2004 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence");
 * you may not use this file except in compliance with the License.
 * $Id$
 */

package ch.bbv.mda.validation;

import ch.bbv.application.Locator;
import ch.bbv.dog.validation.*;
import ch.bbv.mda.*;

/**
 * The rules factory defines the validation rules the application requires on
 * behalf of the MDA model. The rules are registered on the default validator of
 * the application.
 * @author marcelbaumann
 * @version $Revision$
 */
public class RulesFactory {
  /**
   * Name of the name property of the meta element.
   */
  private static final String NAME = "name";

  /**
   * Defines the regular format for a Java identifier. An identifier is used to
   * name packages, classes, properties and methods.
   */
  private static final String JAVA_IDENTIFIER_PATTERN = "[a-zA-Z$_][a-zA-Z0-9$_]*";

  /**
   * The rule to verify the format of an identifier in the model.
   */
  private FormatRule identifierRule;

  /**
   * Registers all rules constraining the MDA model.
   */
  public void registerAllRules() {
    identifierRule = new FormatRule(false, JAVA_IDENTIFIER_PATTERN);
    createRulesForMetaModel();
    createRulesForMetaModel();
    createRulesForMetaClass();
    createRulesForMetaProperty();
    createRulesForMetaIndexedProperty();
  }

  /**
   * Registers the validation rules for the specified element type.
   */
  public void createRulesForMetaModel() {
    Validator validator = getValidator();
    validator.registerRule(new NotNullRule(true), MetaModel.class, NAME);
    validator.registerRule(identifierRule, MetaModel.class, NAME);
    validator.registerRule(new UniqueRule(NAME), MetaModel.class, "packages");
  }

  /**
   * Registers the validation rules for the specified element type.
   */
  public void createRulesForMetaPackage() {
    Validator validator = getValidator();
    validator.registerRule(new NotNullRule(true), MetaPackage.class, NAME);
    validator.registerRule(identifierRule, MetaPackage.class, NAME);
    validator.registerRule(new UniqueRule(NAME), MetaPackage.class, "classes");

  }

  /**
   * Registers the validation rules for the specified element type.
   */
  public void createRulesForMetaClass() {
    Validator validator = getValidator();
    validator.registerRule(new NotNullRule(true), MetaClass.class, NAME);
    validator.registerRule(identifierRule, MetaClass.class, NAME);
    validator.registerRule(new UniqueRule(NAME), MetaClass.class, "properties");
    validator.registerRule(new UniqueRule(NAME), MetaClass.class,
        "indexedProperties");
  }

  /**
   * Registers the validation rules for the specified element type.
   */
  public void createRulesForMetaProperty() {
    Validator validator = getValidator();
    validator.registerRule(new NotNullRule(true), MetaProperty.class, NAME);
    validator.registerRule(identifierRule, MetaProperty.class, NAME);
    validator.registerRule(new NotNullRule(true), MetaProperty.class,
        "typeName");

  }

  /**
   * Registers the validation rules for the specified element type.
   */
  public void createRulesForMetaIndexedProperty() {
    Validator validator = getValidator();
    validator.registerRule(new NotNullRule(true), MetaIndexedProperty.class,
        NAME);
    validator.registerRule(identifierRule, MetaIndexedProperty.class, NAME);
    validator.registerRule(new NotNullRule(true), MetaIndexedProperty.class,
        "typeName");
  }

  /**
   * Returns the validator instance of the application.
   * @return the validator instance.
   */
  private Validator getValidator() {
    return (Validator) Locator.getInstance().getComponent(Validator.NAME);
  }
}