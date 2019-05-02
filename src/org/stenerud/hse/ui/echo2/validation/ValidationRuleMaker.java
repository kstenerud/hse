package org.stenerud.hse.ui.echo2.validation;

import java.util.Date;
import java.util.Set;

import org.stenerud.hse.ui.echo2.tools.SelectFieldHelper;
import org.stenerud.hse.ui.validation.ConditionalGetter;
import org.stenerud.hse.ui.validation.ConditionalRule;
import org.stenerud.hse.ui.validation.EqualToValidationRule;
import org.stenerud.hse.ui.validation.IntegerValidationRule;
import org.stenerud.hse.ui.validation.LengthValidationRule;
import org.stenerud.hse.ui.validation.NotEmptyValidationRule;
import org.stenerud.hse.ui.validation.NotEqualToValidationRule;
import org.stenerud.hse.ui.validation.NotOneOfValidationRule;
import org.stenerud.hse.ui.validation.OneOfValidationRule;
import org.stenerud.hse.ui.validation.RegexValidationRule;
import org.stenerud.hse.ui.validation.StrippingValueGetter;
import org.stenerud.hse.ui.validation.TextDateValidationRule;
import org.stenerud.hse.ui.validation.ValidationRule;
import org.stenerud.hse.ui.validation.Validator;
import org.stenerud.hse.ui.validation.ValueGetter;

import nextapp.echo2.app.Component;
import nextapp.echo2.app.TextField;

/**
 * Validation rule maker. <br>
 * Manages a validator, providing convenience methods for adding common rules.
 * 
 * @author Karl Stenerud
 */
public class ValidationRuleMaker
{
	// ========== INJECTED MEMBERS ==========
	private Validator validator;

	// ========== IMPLEMENTATION ==========

	/**
	 * Default Constructor.
	 */
	public ValidationRuleMaker()
	{
		validator = new Validator();
	}

	/**
	 * Constructor.
	 * 
	 * @param validator the validator to use.
	 */
	public ValidationRuleMaker(Validator validator)
	{
		this.validator = validator;
	}

	/**
	 * Add a validation rule.
	 * 
	 * @param rule the rule.
	 */
	public void addRule(ValidationRule rule)
	{
		validator.addRule(rule);
	}

	/**
	 * Add a component-associated rule. <br>
	 * If a component-associated rule throws a ValidationException, the associated component's focus is set.
	 * 
	 * @param rule the rule.
	 * @param component the component to set focus to.
	 */
	public void addComponentRule(ValidationRule rule, Component component)
	{
		validator.addRule(new ComponentAssociatedRule(rule, component));
	}

	/**
	 * Add a conditional rule. <br>
	 * Conditional rules are only checked if the conditional getter returns "true".
	 * 
	 * @param rule the rule.
	 * @param getter the conditional getter.
	 */
	public void addConditionalRule(ValidationRule rule, ConditionalGetter getter)
	{
		addRule(makeConditionalRule(rule, getter));
	}

	/**
	 * Add a rule, conditional on a TextField. <br>
	 * The rule will fire if the text box contains "Y".
	 * 
	 * @param rule the rule.
	 * @param conditionalField the tect field.
	 * @param invert if false, invert the test on the text field (fire if the field doesn't contain "Y").
	 */
	public void addConditionalRule(ValidationRule rule, TextField conditionalField, boolean invert)
	{
		addConditionalRule(rule, conditionalField, "Y", invert);
	}

	/**
	 * Add a rule, conditional on a TextField. <br>
	 * The rule will fire if the text box contains the specified value.
	 * 
	 * @param rule the rule.
	 * @param conditionalField the tect field.
	 * @param value the value to test for.
	 * @param invert if false, invert the test on the text field (fire if the field doesn't contain the specified
	 *            value).
	 */
	public void addConditionalRule(ValidationRule rule, TextField conditionalField, String value, boolean invert)
	{
		addRule(makeConditionalRule(rule, new TextFieldConditionalGetter(conditionalField, value, invert)));
	}

	/**
	 * Add a rule, conditional on a SelectFieldHelper. <br>
	 * The rule will fire if the value "Y" is selected.
	 * 
	 * @param rule the rule.
	 * @param conditionalField the field helper.
	 * @param invert if false, invert the test (fire if "Y" isn't selected).
	 */
	public void addConditionalRule(ValidationRule rule, SelectFieldHelper conditionalField, boolean invert)
	{
		addRule(makeConditionalRule(rule, new SelectFieldHelperConditionalGetter(conditionalField, "Y", invert)));
	}

	/**
	 * Add a length rule.
	 * 
	 * @param name the rule name.
	 * @param field the field to check.
	 * @param minLength the minimum length allowed (-1 means minLength is same as maxLength).
	 * @param maxLength the maximum length allowed.
	 */
	public void addLengthRule(String name, TextField field, int minLength, int maxLength)
	{
		addRule(makeLengthRule(name, field, minLength, maxLength));
	}

	/**
	 * Add an exact length rule.
	 * 
	 * @param name the rule name.
	 * @param field the field to check.
	 * @param exactLength the exact length required.
	 */
	public void addLengthRule(String name, TextField field, int exactLength)
	{
		addRule(makeLengthRule(name, field, exactLength));
	}

	/**
	 * Add an integer rule.
	 * 
	 * @param name the rule name.
	 * @param field the field to check.
	 * @param minValue the minimum value allowed (null = ignore).
	 * @param maxValue the maximum value allowed (null = ignore).
	 */
	public void addIntegerRule(String name, TextField field, String minValue, String maxValue)
	{
		addRule(makeIntegerRule(name, field, minValue, maxValue));
	}

	/**
	 * Add an integer rule. <br>
	 * This rule only requires that the value be an integer.
	 * 
	 * @param name the rule name.
	 * @param field the field to check.
	 */
	public void addIntegerRule(String name, TextField field)
	{
		addIntegerRule(name, field, null, null);
	}

	/**
	 * Add a positive integer rule. <br>
	 * This rule only requires that the value be an integer and >= 0.
	 * 
	 * @param name the rule name.
	 * @param field the field to check.
	 */
	public void addPositiveIntegerRule(String name, TextField field)
	{
		addIntegerRule(name, field, "0", null);
	}

	/**
	 * Add a "not empty" rule.
	 * 
	 * @param name the rule name.
	 * @param field the field to check.
	 */
	public void addNotEmptyRule(String name, TextField field)
	{
		addRule(makeNotEmptyRule(name, field));
	}

	/**
	 * Add a "not empty after stripping" rule.
	 * 
	 * @param name the rule name.
	 * @param field the field to check.
	 * @param filler the value used for padding.
	 * @param prePad if true, padding is on the front. Else padding is on the back.
	 */
	public void addNotEmptyStrippedRule(String name, TextField field, char filler, boolean prePad)
	{
		addRule(makeNotEmptyStrippedRule(name, field, filler, prePad));
	}

	/**
	 * Add a "not zeroes or empty" rule. <br>
	 * The rule will fail if the value is empty, or contains only 0s.
	 * 
	 * @param name the rule name.
	 * @param field the field to check.
	 */
	public void addNotZeroesOrEmptyRule(String name, TextField field)
	{
		addRule(makeNotZeroesOrEmptyRule(name, field));
	}

	/**
	 * Add a "not spaces or empty" rule. <br>
	 * The rule will fail if the value is empty, or contains only spaces.
	 * 
	 * @param name the rule name.
	 * @param field the field to check.
	 */
	public void addNotSpacesOrEmptyRule(String name, TextField field)
	{
		addRule(makeNotSpacesOrEmptyRule(name, field));
	}

	/**
	 * Add a "not spaces or zeroes or empty" rule. <br>
	 * The rule will fail if the value is empty, contains only spaces, or contains only 0s.
	 * 
	 * @param name the rule name.
	 * @param field the field to check.
	 */
	public void addNotSpacesOrZeroesOrEmptyRule(String name, TextField field)
	{
		addNotSpacesOrEmptyRule(name, field);
		addNotZeroesOrEmptyRule(name, field);
	}

	/**
	 * Add a date rule.
	 * 
	 * @param name the rule name.
	 * @param field the field to check.
	 * @param format the date format as per DateConverter.
	 * @param minDate the minimum date allowed (null = ignore).
	 * @param maxDate the maximum date allowed (null = ignore).
	 */
	public void addDateRule(String name, TextField field, String format, Date minDate, Date maxDate)
	{
		addRule(makeDateRule(name, field, format, minDate, maxDate));
	}

	/**
	 * Add a date rule.
	 * 
	 * @param name the rule name.
	 * @param field the field to check.
	 * @param format the date format as per DateConverter.
	 */
	public void addDateRule(String name, TextField field, String format)
	{
		addRule(makeDateRule(name, field, format));
	}

	/**
	 * Add a "one of" rule. The value must match one of the valid values.
	 * 
	 * @param name the rule name.
	 * @param field the field to check.
	 * @param validValues the values that are considered valid.
	 * @param caseSensitive if true, the comparison is case sensitive.
	 */
	public void addOneOfRule(String name, TextField field, Set validValues, boolean caseSensitive)
	{
		addRule(makeOneOfRule(name, field, validValues, caseSensitive));
	}

	/**
	 * Add a "one of" rule. The value must match one of the valid values.
	 * 
	 * @param name the rule name.
	 * @param field the field to check.
	 * @param validValues the values that are considered valid.
	 * @param caseSensitive if true, the comparison is case sensitive.
	 */
	public void addOneOfRule(String name, SelectFieldHelper field, Set validValues, boolean caseSensitive)
	{
		addRule(makeOneOfRule(name, field, validValues, caseSensitive));
	}

	/**
	 * Add a "not one of" rule. The value must NOT match one of the invalid values.
	 * 
	 * @param name the rule name.
	 * @param field the field to check.
	 * @param invalidValues the values that are considered valid.
	 * @param caseSensitive if true, the comparison is case sensitive.
	 */
	public void addNotOneOfRule(String name, TextField field, Set invalidValues, boolean caseSensitive)
	{
		addRule(makeNotOneOfRule(name, field, invalidValues, caseSensitive));
	}

	/**
	 * Add a "not one of" rule. The value must NOT match one of the invalid values.
	 * 
	 * @param name the rule name.
	 * @param field the field to check.
	 * @param invalidValues the values that are considered valid.
	 * @param caseSensitive if true, the comparison is case sensitive.
	 */
	public void addNotOneOfRule(String name, SelectFieldHelper field, Set invalidValues, boolean caseSensitive)
	{
		addRule(makeNotOneOfRule(name, field, invalidValues, caseSensitive));
	}

	/**
	 * Add an "equal to" rule.
	 * 
	 * @param name the rule name.
	 * @param field the field to check.
	 * @param value the value to match against.
	 * @param caseSensitive if true, the comparison is case sensitive.
	 */
	public void addEqualToRule(String name, TextField field, String value, boolean caseSensitive)
	{
		addRule(makeEqualToRule(name, field, value, caseSensitive));
	}

	/**
	 * Add an "equal to" rule.
	 * 
	 * @param name the rule name.
	 * @param field the field to check.
	 * @param value the value to match against.
	 * @param caseSensitive if true, the comparison is case sensitive.
	 */
	public void addEqualToRule(String name, SelectFieldHelper field, String value, boolean caseSensitive)
	{
		addRule(makeEqualToRule(name, field, value, caseSensitive));
	}

	/**
	 * Add a "not equal to" rule.
	 * 
	 * @param name the rule name.
	 * @param field the field to check.
	 * @param value the value to match against.
	 * @param caseSensitive if true, the comparison is case sensitive.
	 */
	public void addNotEqualToRule(String name, TextField field, String value, boolean caseSensitive)
	{
		addRule(makeNotEqualToRule(name, field, value, caseSensitive));
	}

	/**
	 * Add a "not equal to" rule.
	 * 
	 * @param name the rule name.
	 * @param field the field to check.
	 * @param value the value to match against.
	 * @param caseSensitive if true, the comparison is case sensitive.
	 */
	public void addNotEqualToRule(String name, SelectFieldHelper field, String value, boolean caseSensitive)
	{
		addRule(makeNotEqualToRule(name, field, value, caseSensitive));
	}

	/**
	 * Add a regular expression rule. The value must match with the regular expression.
	 * 
	 * @param name the rule name.
	 * @param field the field to check.
	 * @param regex The regular expression to match against.
	 */
	public void addRegexRule(String name, TextField field, String regex)
	{
		addRule(makeRegexRule(name, field, regex));
	}

	/**
	 * Add a regular expression rule. The value must match with the regular expression.
	 * 
	 * @param name the rule name.
	 * @param field the field to check.
	 * @param regex The regular expression to match against.
	 * @param message The message to set in the validation exception.
	 */
	public void addRegexRule(String name, TextField field, String regex, String message)
	{
		addRule(makeRegexRule(name, field, regex, message));
	}

	/**
	 * Make a value getter from a component. <br>
	 * The following classes are supported:
	 * <ul>
	 * <li>CurrencyField</li>
	 * <li>TextField</li>
	 * <li>SelectFieldHelper</li>
	 * </ul>
	 * 
	 * @param component the component.
	 * @return a value getter for that component.
	 */
	public ValueGetter makeValueGetter(Object component)
	{
		if ( component instanceof TextField )
		{
			return new TextFieldValueGetter((TextField)component);
		}
		else if ( component instanceof SelectFieldHelper )
		{
			return new SelectFieldHelperValueGetter((SelectFieldHelper)component);
		}
		else
		{
			throw new IllegalArgumentException("Cannot create ValueGetter for class " + component.getClass().getName());
		}
	}

	/**
	 * make a conditional rule. <br>
	 * Conditional rules are only checked if the conditional getter returns "true".
	 * 
	 * @param rule the rule.
	 * @param getter the conditional getter.
	 * @return the rule.
	 */
	public ValidationRule makeConditionalRule(ValidationRule rule, ConditionalGetter getter)
	{
		if ( rule instanceof ComponentAssociatedRule )
		{
			return new ConditionalRule(makeComponentRule(rule, ((ComponentAssociatedRule)rule).getComponent()), getter);
		}

		return new ConditionalRule(rule, getter);
	}

	/**
	 * Make a component-associated rule. <br>
	 * If a component-associated rule throws a ValidationException, the associated component's focus is set.
	 * 
	 * @param rule the rule.
	 * @param component the component to set focus to.
	 * @return the rule.
	 */
	public ComponentAssociatedRule makeComponentRule(ValidationRule rule, Component component)
	{
		return new ComponentAssociatedRule(rule, component);
	}

	/**
	 * Make a length rule.
	 * 
	 * @param name the rule name.
	 * @param field the field to check.
	 * @param minLength the minimum length allowed (-1 means minLength is same as maxLength).
	 * @param maxLength the maximum length allowed.
	 * @return the rule.
	 */
	public ValidationRule makeLengthRule(String name, TextField field, int minLength, int maxLength)
	{
		return makeComponentRule(new LengthValidationRule(name, makeValueGetter(field), minLength, maxLength), field);
	}

	/**
	 * Make an exact length rule.
	 * 
	 * @param name the rule name.
	 * @param field the field to check.
	 * @param exactLength the exact length required.
	 * @return the rule.
	 */
	public ValidationRule makeLengthRule(String name, TextField field, int exactLength)
	{
		return makeComponentRule(new LengthValidationRule(name, makeValueGetter(field), exactLength), field);
	}

	/**
	 * Make an integer rule.
	 * 
	 * @param name the rule name.
	 * @param field the field to check.
	 * @param minValue the minimum value allowed (null = ignore).
	 * @param maxValue the maximum value allowed (null = ignore).
	 * @return the rule.
	 */
	public ValidationRule makeIntegerRule(String name, TextField field, String minValue, String maxValue)
	{
		return makeComponentRule(new IntegerValidationRule(name, makeValueGetter(field), minValue, maxValue), field);
	}

	/**
	 * Make an integer rule. <br>
	 * This rule only requires that the value be an integer.
	 * 
	 * @param name the rule name.
	 * @param field the field to check.
	 * @return the rule.
	 */
	public ValidationRule makeIntegerRule(String name, TextField field)
	{
		return makeIntegerRule(name, field, null, null);
	}

	/**
	 * Make a positive integer rule. <br>
	 * This rule only requires that the value be an integer and >= 0.
	 * 
	 * @param name the rule name.
	 * @param field the field to check.
	 * @return the rule.
	 */
	public ValidationRule makePositiveIntegerRule(String name, TextField field)
	{
		return makeIntegerRule(name, field, "0", null);
	}

	/**
	 * Make a "not empty" rule.
	 * 
	 * @param name the rule name.
	 * @param field the field to check.
	 * @return the rule.
	 */
	public ValidationRule makeNotEmptyRule(String name, TextField field)
	{
		return makeComponentRule(new NotEmptyValidationRule(name, makeValueGetter(field)), field);
	}

	/**
	 * Make a "not empty after stripping" rule.
	 * 
	 * @param name the rule name.
	 * @param field the field to check.
	 * @param filler the value used for padding.
	 * @param prePad if true, padding is on the front. Else padding is on the back.
	 * @return the rule.
	 */
	public ValidationRule makeNotEmptyStrippedRule(String name, TextField field, char filler, boolean prePad)
	{
		ValueGetter getter = new StrippingValueGetter(new TextFieldValueGetter(field), filler, prePad);
		return makeComponentRule(new NotEmptyValidationRule(name, getter), field);
	}

	/**
	 * Make a "not zeroes or empty" rule. <br>
	 * The rule will fail if the value is empty, or contains only 0s.
	 * 
	 * @param name the rule name.
	 * @param field the field to check.
	 * @return the rule.
	 */
	public ValidationRule makeNotZeroesOrEmptyRule(String name, TextField field)
	{
		ValueGetter getter = new StrippingValueGetter(new TextFieldValueGetter(field), '0', true);
		return makeComponentRule(new NotEmptyValidationRule(name, getter, "validation.cannotBe.zeroesOrEmpty"), field);
	}

	/**
	 * Make a "not spaces or empty" rule. <br>
	 * The rule will fail if the value is empty, or contains only spaces.
	 * 
	 * @param name the rule name.
	 * @param field the field to check.
	 * @return the rule.
	 */
	public ValidationRule makeNotSpacesOrEmptyRule(String name, TextField field)
	{
		ValueGetter getter = new StrippingValueGetter(new TextFieldValueGetter(field), ' ', false);
		return makeComponentRule(new NotEmptyValidationRule(name, getter), field);
	}

	/**
	 * Make a date rule.
	 * 
	 * @param name the rule name.
	 * @param field the field to check.
	 * @param format the date format as per DateConverter.
	 * @return the rule.
	 */
	public ValidationRule makeDateRule(String name, TextField field, String format)
	{
		return makeComponentRule(new TextDateValidationRule(name, makeValueGetter(field), format), field);
	}

	/**
	 * Make a date rule.
	 * 
	 * @param name the rule name.
	 * @param field the field to check.
	 * @param format the date format as per DateConverter.
	 * @param minDate the minimum date allowed (null = ignore).
	 * @param maxDate the maximum date allowed (null = ignore).
	 * @return the rule.
	 */
	public ValidationRule makeDateRule(String name, TextField field, String format, Date minDate, Date maxDate)
	{
		return makeComponentRule(new TextDateValidationRule(name, makeValueGetter(field), format, minDate, maxDate),
				field);
	}

	/**
	 * Make a "one of" rule. The value must match one of the valid values.
	 * 
	 * @param name the rule name.
	 * @param field the field to check.
	 * @param validValues the values that are considered valid.
	 * @param caseSensitive if true, the comparison is case sensitive.
	 * @return the rule.
	 */
	public ValidationRule makeOneOfRule(String name, TextField field, Set validValues, boolean caseSensitive)
	{
		return makeComponentRule(new OneOfValidationRule(name, makeValueGetter(field), validValues, caseSensitive),
				field);
	}

	/**
	 * Make a "one of" rule. The value must match one of the valid values.
	 * 
	 * @param name the rule name.
	 * @param field the field to check.
	 * @param validValues the values that are considered valid.
	 * @param caseSensitive if true, the comparison is case sensitive.
	 * @return the rule.
	 */
	public ValidationRule makeOneOfRule(String name, SelectFieldHelper field, Set validValues, boolean caseSensitive)
	{
		return makeComponentRule(new OneOfValidationRule(name, makeValueGetter(field), validValues, caseSensitive),
				field.getManagedField());
	}

	/**
	 * Make a "not one of" rule. The value must NOT match one of the invalid values.
	 * 
	 * @param name the rule name.
	 * @param field the field to check.
	 * @param invalidValues the values that are considered valid.
	 * @param caseSensitive if true, the comparison is case sensitive.
	 * @return the rule.
	 */
	public ValidationRule makeNotOneOfRule(String name, TextField field, Set invalidValues, boolean caseSensitive)
	{
		return makeComponentRule(
				new NotOneOfValidationRule(name, makeValueGetter(field), invalidValues, caseSensitive), field);
	}

	/**
	 * Make a "not one of" rule. The value must NOT match one of the invalid values.
	 * 
	 * @param name the rule name.
	 * @param field the field to check.
	 * @param invalidValues the values that are considered valid.
	 * @param caseSensitive if true, the comparison is case sensitive.
	 * @return the rule.
	 */
	public ValidationRule makeNotOneOfRule(String name, SelectFieldHelper field, Set invalidValues,
			boolean caseSensitive)
	{
		return makeComponentRule(
				new NotOneOfValidationRule(name, makeValueGetter(field), invalidValues, caseSensitive), field
						.getManagedField());
	}

	/**
	 * Make an "equal to" rule.
	 * 
	 * @param name the rule name.
	 * @param field the field to check.
	 * @param value the value to match against.
	 * @param caseSensitive if true, the comparison is case sensitive.
	 * @return the rule.
	 */
	public ValidationRule makeEqualToRule(String name, TextField field, String value, boolean caseSensitive)
	{
		return makeComponentRule(new EqualToValidationRule(name, makeValueGetter(field), value, caseSensitive), field);
	}

	/**
	 * Make an "equal to" rule.
	 * 
	 * @param name the rule name.
	 * @param field the field to check.
	 * @param value the value to match against.
	 * @param caseSensitive if true, the comparison is case sensitive.
	 * @return the rule.
	 */
	public ValidationRule makeEqualToRule(String name, SelectFieldHelper field, String value, boolean caseSensitive)
	{
		return makeComponentRule(new EqualToValidationRule(name, makeValueGetter(field), value, caseSensitive), field
				.getManagedField());
	}

	/**
	 * Make a "not equal to" rule.
	 * 
	 * @param name the rule name.
	 * @param field the field to check.
	 * @param value the value to match against.
	 * @param caseSensitive if true, the comparison is case sensitive.
	 * @return the rule.
	 */
	public ValidationRule makeNotEqualToRule(String name, TextField field, String value, boolean caseSensitive)
	{
		return makeComponentRule(new NotEqualToValidationRule(name, makeValueGetter(field), value, caseSensitive),
				field);
	}

	/**
	 * Make a "not equal to" rule.
	 * 
	 * @param name the rule name.
	 * @param field the field to check.
	 * @param value the value to match against.
	 * @param caseSensitive if true, the comparison is case sensitive.
	 * @return the rule.
	 */
	public ValidationRule makeNotEqualToRule(String name, SelectFieldHelper field, String value, boolean caseSensitive)
	{
		return makeComponentRule(new NotEqualToValidationRule(name, makeValueGetter(field), value, caseSensitive),
				field.getManagedField());
	}

	/**
	 * Make a regular expression rule. The value must match with the regular expression.
	 * 
	 * @param name the rule name.
	 * @param field the field to check.
	 * @param regex The regular expression to match against.
	 * @return the rule.
	 */
	public ValidationRule makeRegexRule(String name, TextField field, String regex)
	{
		return makeComponentRule(new RegexValidationRule(name, makeValueGetter(field), regex), field);
	}

	/**
	 * Make a regular expression rule. The value must match with the regular expression.
	 * 
	 * @param name the rule name.
	 * @param field the field to check.
	 * @param regex The regular expression to match against.
	 * @param message The message to set in the validation exception.
	 * @return the rule.
	 */
	public ValidationRule makeRegexRule(String name, TextField field, String regex, String message)
	{
		return makeComponentRule(new RegexValidationRule(name, makeValueGetter(field), regex, message), field);
	}

	// ========== GETTERS AND SETTERS ==========

	public Validator getValidator()
	{
		return validator;
	}

	public void setValidator(Validator validator)
	{
		this.validator = validator;
	}
}
