/* 
 * copyright 2000-2006 Adrian Bachofen
 * Licensed under the Apache License, Version 2.0 (the "Licence");
 * you may not use this file except in compliance with the License.
 * $Id$
 */

package ch.bbv.mda.validation;

import java.util.Collection;
import java.util.ResourceBundle;

import ch.bbv.dog.validation.ValidationRule;
import ch.bbv.dog.validation.ValidationRuleImpl;
import ch.bbv.mda.MetaClass;
import ch.bbv.mda.MetaElement;
import ch.bbv.mda.MetaModel;
import ch.bbv.mda.TaggedValue;
import ch.bbv.mda.cartridges.BoConstants;

import com.jgoodies.validation.Severity;
import com.jgoodies.validation.ValidationResult;

/**
 * The rule reports an error if nothing will be generated (no bo-generate =
 * true).
 * @author Adrian Bachofen
 * @version $Revision$
 */
public class NothingToGenerateRule extends ValidationRuleImpl implements
    ValidationRule {
  /**
   * Key for validation result message.
   */
  private static final String NOTHING_TO_GENERATE = "ch.bbv.mda.nothing-to-generate";

  /**
   * The resource bundle containing the messages for the validation messages.
   */
  private static ResourceBundle resources;

  static {
    resources = ResourceBundle.getBundle("ch.bbv.mda.validation.RulesMessages");
  }

  /**
   * Counts the number of classes to be generated.
   */
  private int numberOfClassesToGenerate = 0;

  /**
   * Counts the number of classes/packages to be generated (bo-generate = true).
   */
  public NothingToGenerateRule() {
    this(false, Severity.ERROR);
  }

  /**
   * Counts the number of classes/packages to be generated (bo-generate = true).
   * @param immediate flag indicating if the rule is immediate or not
   * @param severity the severity level of the rule
   */
  public NothingToGenerateRule(boolean immediate, Severity severity) {
    super(immediate, severity);
  }

  @Override
  public boolean execute(Object bean, String property, ValidationResult results) {
    return validate(null, results, bean, property);
  }

  @Override
  public boolean execute(Object bean, String property, Object oldValue,
      Object newValue, ValidationResult results) {
    return validate((Collection) newValue, results, bean, property);
  }

  private boolean validate(Collection objects, ValidationResult results,
      Object bean, String property) {
    if (bean instanceof MetaClass) {
      MetaClass metaClass = (MetaClass) bean;
      TaggedValue tag = metaClass.getTaggedValue(BoConstants.BO_GENERATE);
      if (tag != null && tag.getValue().equalsIgnoreCase("true")) {
        boolean willBeGenerated = true;
        MetaElement metaElement = metaClass;
        while (!(metaElement.getContext() instanceof MetaModel)) {
          metaElement = metaElement.getContext();
          tag = metaElement.getTaggedValue(BoConstants.BO_GENERATE);
          if (tag != null && tag.getValue().equalsIgnoreCase("false")) {
            willBeGenerated = false;
            break;
          }
        }
        if (willBeGenerated) {
          numberOfClassesToGenerate++;
        }
      }
    }
    return true;
  }

  public void validationStart(ValidationResult results) {
    numberOfClassesToGenerate = 0;
  }

  public void validationEnd(ValidationResult results) {
    if (numberOfClassesToGenerate == 0) {
      addMessage(results, resources, NOTHING_TO_GENERATE, getSeverity(),
          new MetaClass(), "bo-generate", null, null);
    }
  }
}