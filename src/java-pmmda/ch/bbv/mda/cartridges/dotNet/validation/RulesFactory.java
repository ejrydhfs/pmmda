/* 
 * copyright 2000-20056 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence");
 * you may not use this file except in compliance with the License.
 * $Id$
 */

package ch.bbv.mda.cartridges.dotNet.validation;

import java.util.ResourceBundle;

import ch.bbv.application.Locator;
import ch.bbv.dog.validation.*;
import ch.bbv.mda.*;

/**
 * The rules factory defines the validation rules related to Microsoft .NET. The rules
 * ensure that a model is as perfect for generation as must be.
 * @author Adrian Bachofen
 * @version $Revision$
 */
public class RulesFactory {
  /**
   * The resource bundle containing the messages for the validation messages.
   */
  public static ResourceBundle resources;

  static {
    resources = ResourceBundle.getBundle("ch.bbv.mda.cartridges.dotNet.validation.RulesMessages");
  }

  /**
   * Registers all rules constraining the MDA model.
   */
  public void registerAllRules() {
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
    validator.registerRule(new MustHaveNoElementsRule(), MetaModel.class, "classes");
  }

  /**
   * Registers the validation rules for the specified element type.
   */
  public void createRulesForMetaPackage() {
    Validator validator = getValidator();
  }

  /**
   * Registers the validation rules for the specified element type.
   */
  public void createRulesForMetaClass() {
    Validator validator = getValidator();
  }

  /**
   * Registers the validation rules for the specified element type.
   */
  public void createRulesForMetaProperty() {
    Validator validator = getValidator();
  }

  /**
   * Registers the validation rules for the specified element type.
   */
  public void createRulesForMetaIndexedProperty() {
    Validator validator = getValidator();
  }

  /**
   * Returns the validator instance of the application.
   * @return the validator instance.
   */
  private Validator getValidator() {
    return (Validator) Locator.getInstance().getComponent(Validator.NAME);
  }
}