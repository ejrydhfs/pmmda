/* 
 * copyright 2000-2005 Marcel Baumann
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
   * Name of the description property of the meta element.
   */
  private static final String DESCRIPTION = "description";

  /**
   * Defines the regular format for a Java identifier. An identifier is used to
   * name packages, classes, properties and methods.
   */
  private static final String JAVA_IDENTIFIER_PATTERN = "[a-zA-Z$_][a-zA-Z0-9$_]*";

  /**
   * The rule to verify the format of an identifier in the model.
   */
  private FormatRule identifierRule;

  private NotNullRule nameNotNullRule;
  
  private DescriptionRule descriptionNotNullRule;


  /**
   * Registers all rules constraining the MDA model.
   */
  public void registerAllRules() {
    identifierRule = new FormatRule(false, ValidationRule.ERROR,
        JAVA_IDENTIFIER_PATTERN);
    nameNotNullRule = new NotNullRule(true, ValidationRule.ERROR);
    descriptionNotNullRule = new DescriptionRule(false, ValidationRule.WARNING);
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
    validator.registerRule(nameNotNullRule, MetaModel.class, NAME);
    validator.registerRule(identifierRule, MetaModel.class, NAME);
    validator.registerRule(new UniqueRule(NAME), MetaModel.class, "packages");
  }

  /**
   * Registers the validation rules for the specified element type.
   */
  public void createRulesForMetaPackage() {
    Validator validator = getValidator();
    validator.registerRule(nameNotNullRule, MetaPackage.class, NAME);
    validator.registerRule(identifierRule, MetaPackage.class, NAME);
    validator.registerRule(new UniqueRule(NAME), MetaPackage.class, "classes");
    validator.registerRule(descriptionNotNullRule, MetaPackage.class,
        DESCRIPTION);
  }

  /**
   * Registers the validation rules for the specified element type.
   */
  public void createRulesForMetaClass() {
    Validator validator = getValidator();
    validator.registerRule(nameNotNullRule, MetaClass.class, NAME);
    validator.registerRule(identifierRule, MetaClass.class, NAME);
    validator.registerRule(new UniqueRule(NAME), MetaClass.class, "properties");
    validator.registerRule(new UniqueRule(NAME), MetaClass.class,
        "indexedProperties");
    validator
        .registerRule(descriptionNotNullRule, MetaClass.class, DESCRIPTION);
  }

  /**
   * Registers the validation rules for the specified element type.
   */
  public void createRulesForMetaProperty() {
    Validator validator = getValidator();
    validator.registerRule(nameNotNullRule, MetaProperty.class, NAME);
    validator.registerRule(identifierRule, MetaProperty.class, NAME);
    validator.registerRule(new NotNullRule(true, ValidationRule.ERROR),
        MetaProperty.class, "typeName");
    validator.registerRule(descriptionNotNullRule, MetaProperty.class,
        DESCRIPTION);

  }

  /**
   * Registers the validation rules for the specified element type.
   */
  public void createRulesForMetaIndexedProperty() {
    Validator validator = getValidator();
    validator.registerRule(nameNotNullRule, MetaIndexedProperty.class, NAME);
    validator.registerRule(identifierRule, MetaIndexedProperty.class, NAME);
    validator.registerRule(new NotNullRule(true, ValidationRule.ERROR),
        MetaIndexedProperty.class, "typeName");
    validator.registerRule(descriptionNotNullRule, MetaIndexedProperty.class,
        DESCRIPTION);
  }

  /**
   * Returns the validator instance of the application.
   * @return the validator instance.
   */
  private Validator getValidator() {
    return (Validator) Locator.getInstance().getComponent(Validator.NAME);
  }
}