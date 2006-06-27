/* 
 * copyright 2000-2006 Adrian Bachofen
 * Licensed under the Apache License, Version 2.0 (the "Licence");
 * you may not use this file except in compliance with the License.
 * $Id$
 */

package ch.bbv.mda.cartridges.dotNet.validation;

import java.util.Collection;
import java.util.ResourceBundle;

import ch.bbv.dog.validation.ValidationRule;
import ch.bbv.dog.validation.ValidationRuleImpl;
import ch.bbv.mda.MetaElement;
import ch.bbv.mda.cartridges.BoConstants;
import ch.bbv.utilities.ReflectUtilities;

import com.jgoodies.validation.Severity;
import com.jgoodies.validation.ValidationResult;

/**
 * The rule insures that there are no classes on model level, because in .NET we
 * need namespaces.
 * 
 * @author Adrian Bachofen
 * @version $Revision$
 */
public class MustHaveNoElementsRule extends ValidationRuleImpl implements
		ValidationRule {
	/**
	 * Key for validation result message.
	 */
	private static final String HAS_ELEMENTS = "ch.bbv.mda.cartridges.dotNet.must-have-no-elements";

	/**
	 * The resource bundle containing the messages for the validation messages.
	 */
	private static ResourceBundle resources;

	static {
		resources = ResourceBundle
				.getBundle("ch.bbv.mda.cartridges.dotNet.validation.RulesMessages");
	}

	/**
	 * Constructor of the class where classes without namespaces are considered.
	 */
	public MustHaveNoElementsRule() {
		this(false, Severity.ERROR);
	}

	/**
	 * Constructor of the class where classes without namespaces are considered.
	 * 
	 * @param immediate
	 *          flag indicating if the rule is immediate or not.
	 * @param severity
	 *          the severity level of the rule.
	 */
	public MustHaveNoElementsRule(boolean immediate, Severity severity) {
		super(immediate, severity);
	}

	@Override
	public boolean execute(Object bean, String property, ValidationResult results) {
		Collection objects = (Collection) ReflectUtilities.getProperty(bean,
				property);
		return validate(objects, results, bean, property);
	}

	@Override
	public boolean execute(Object bean, String property, Object oldValue,
			Object newValue, ValidationResult results) {
		return validate((Collection) newValue, results, bean, property);
	}

	private boolean validate(Collection objects, ValidationResult results,
			Object bean, String property) {
		for (Object object : objects) {
			MetaElement metaElement = (MetaElement) object;
			if (metaElement.hasStereotype(BoConstants.DATA_OBJECT_STEREOTYPE)
					&& metaElement.getTaggedValue(BoConstants.BO_GENERATE).getValue()
							.equalsIgnoreCase("true")) {
				addMessage(results, resources, HAS_ELEMENTS, getSeverity(), bean,
						property, null, null);
				return false;
			}
		}
		return true;
	}
}