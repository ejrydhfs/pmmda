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
   * Registers all rules constraining the MDA model.
   */
  public void registerAllRules() {
    createRulesForMetaModel();
    createRulesForMetaModel();
    createRulesForMetaClass();
    createRulesForMetaProperty();
    createRulesForMetaIndexedProperty();
  }

  public void createRulesForMetaModel() {
    Validator validator = getValidator();
    validator.registerRule(new NotNullRule(true), MetaModel.class, "name");
    validator.registerRule(new UniqueRule("name"), MetaModel.class, "packages");
  }

  public void createRulesForMetaPackage() {
    Validator validator = getValidator();
    validator.registerRule(new NotNullRule(true), MetaPackage.class, "name");
    validator
        .registerRule(new UniqueRule("name"), MetaPackage.class, "classes");

  }

  public void createRulesForMetaClass() {
    Validator validator = getValidator();
    validator.registerRule(new NotNullRule(true), MetaClass.class, "name");
    validator.registerRule(new UniqueRule("name"), MetaClass.class,
        "properties");
    validator.registerRule(new UniqueRule("name"), MetaClass.class,
        "indexedProperties");
  }

  public void createRulesForMetaProperty() {
    Validator validator = getValidator();
    validator.registerRule(new NotNullRule(true), MetaProperty.class, "name");
    validator.registerRule(new NotNullRule(true), MetaProperty.class,
        "typeName");

  }

  public void createRulesForMetaIndexedProperty() {
    Validator validator = getValidator();
    validator.registerRule(new NotNullRule(true), MetaIndexedProperty.class,
        "name");
    validator.registerRule(new NotNullRule(true), MetaIndexedProperty.class,
        "typeName");
  }

  private Validator getValidator() {
    return (Validator) Locator.getInstance().getComponent(Validator.NAME);
  }
}