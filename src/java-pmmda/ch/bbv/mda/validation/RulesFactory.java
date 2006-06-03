/* 
 * copyright 2000-20056 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence");
 * you may not use this file except in compliance with the License.
 * $Id$
 */

package ch.bbv.mda.validation;

import java.util.ResourceBundle;

import ch.bbv.application.Locator;
import ch.bbv.dog.validation.*;
import ch.bbv.mda.*;

import com.jgoodies.validation.Severity;

/**
 * The rules factory defines the validation rules the application requires on
 * behalf of the MDA model. The rules are registered on the default validator of
 * the application.
 * @author Marcel Baumann
 * @version $Revision$
 */
public class RulesFactory {
  /**
   * The resource bundle containing the messages for the validation messages.
   */
  public static ResourceBundle resources;

  static {
    resources = ResourceBundle.getBundle("ch.bbv.mda.validation.RulesMessages");
  }

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
   * name classes, properties and methods.
   */
  private static final String JAVA_IDENTIFIER_PATTERN = "[a-zA-Z$_][a-zA-Z0-9$_]*";

  /**
   * Defines the regular format for a package identifier. A package identifier
   * is used to name packages.
   */
  private static final String PACKAGE_IDENTIFIER_PATTERN = JAVA_IDENTIFIER_PATTERN
      + "(\\." + JAVA_IDENTIFIER_PATTERN + ")*";

  /**
   * Validation message in case of invalid package identifier
   */
  private static final String WRONG_PACKAGE_IDENTIFIER = "ch.bbv.mda.package-identifier-error";

  /**
   * The rule to verify the format of an identifier in the model.
   */
  private FormatRule identifierRule;

  /**
   * Package or type identifier rule for name validation.
   */
  private FormatRule packageIdentifierRule;

  /**
   * Name type not null or empty validation rule.
   */
  private StringNotNullOrNotEmptyRule nameTypeNotNullOrNotEmptyRule;

  /**
   * Description validation rule.
   */
  private DescriptionRule descriptionNotNullRule;

  /**
   * Registers all rules constraining the MDA model.
   */
  public void registerAllRules() {
    identifierRule = new FormatRule(false, Severity.ERROR,
        JAVA_IDENTIFIER_PATTERN);
    packageIdentifierRule = new FormatRule(false, Severity.ERROR,
        PACKAGE_IDENTIFIER_PATTERN, resources, WRONG_PACKAGE_IDENTIFIER);
    nameTypeNotNullOrNotEmptyRule = new StringNotNullOrNotEmptyRule(true,
        Severity.ERROR);
    descriptionNotNullRule = new DescriptionRule(false, Severity.WARNING);
    createRulesForMetaModel();
    createRulesForMetaPackage();
    createRulesForMetaClass();
    createRulesForMetaProperty();
    createRulesForMetaIndexedProperty();
  }

  /**
   * Registers the validation rules for the specified element type.
   */
  public void createRulesForMetaModel() {
    Validator validator = getValidator();
    validator.registerRule(new UniqueRule(NAME), MetaModel.class, "packages");
  }

  /**
   * Registers the validation rules for the specified element type.
   */
  public void createRulesForMetaPackage() {
    Validator validator = getValidator();
    validator.registerRule(descriptionNotNullRule, MetaPackage.class,
        DESCRIPTION);
    validator.registerRule(new UniqueRule(NAME), MetaPackage.class, "classes");
    validator.registerRule(packageIdentifierRule, MetaPackage.class, NAME);
  }

  /**
   * Registers the validation rules for the specified element type.
   */
  public void createRulesForMetaClass() {
    Validator validator = getValidator();
    validator
        .registerRule(descriptionNotNullRule, MetaClass.class, DESCRIPTION);
    validator.registerRule(new UniqueRule(NAME), MetaClass.class, "properties");
    validator.registerRule(new UniqueRule(NAME), MetaClass.class,
        "indexedProperties");
    validator.registerRule(identifierRule, MetaClass.class, NAME);
    validator.registerRule(new NothingToGenerateRule(), MetaClass.class, "");
  }

  /**
   * Registers the validation rules for the specified element type.
   */
  public void createRulesForMetaProperty() {
    Validator validator = getValidator();
    validator.registerRule(descriptionNotNullRule, MetaProperty.class,
        DESCRIPTION);
    validator.registerRule(new NotNullRule(true, Severity.ERROR),
        MetaProperty.class, "typeName");
    validator.registerRule(identifierRule, MetaProperty.class, NAME);
    validator.registerRule(nameTypeNotNullOrNotEmptyRule, MetaProperty.class,
        "typeName");
    validator.registerRule(new UndefinedTypeRule(true, Severity.ERROR),
        MetaProperty.class, "typeName");
  }

  /**
   * Registers the validation rules for the specified element type.
   */
  public void createRulesForMetaIndexedProperty() {
    Validator validator = getValidator();
    validator.registerRule(descriptionNotNullRule, MetaIndexedProperty.class,
        DESCRIPTION);
    validator.registerRule(new NotNullRule(true, Severity.ERROR),
        MetaIndexedProperty.class, "typeName");
    validator.registerRule(identifierRule, MetaIndexedProperty.class, NAME);
    validator.registerRule(nameTypeNotNullOrNotEmptyRule,
        MetaIndexedProperty.class, "typeName");
    validator.registerRule(new UndefinedTypeRule(true, Severity.ERROR),
        MetaIndexedProperty.class, "typeName");
  }

  /**
   * Returns the validator instance of the application.
   * @return the validator instance.
   */
  private Validator getValidator() {
    return (Validator) Locator.getInstance().getComponent(Validator.NAME);
  }
}