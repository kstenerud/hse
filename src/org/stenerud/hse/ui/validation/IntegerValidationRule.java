package org.stenerud.hse.ui.validation;

import java.math.BigInteger;

import org.stenerud.hse.ui.Messages;

/**
 * Rule to check that a value is an integer. <br>
 * Any value that will fit in a BigInteger is acceptable. <br>
 * Note: this rule does not fire if the value is an empty string.
 * 
 * @author Karl Stenerud
 */
public class IntegerValidationRule implements ValidationRule
{
	// ========== INJECTED MEMBERS ==========
	private BigInteger minValue;
	private BigInteger maxValue;
	private ValueGetter getter;
	private String name;

	// ========== IMPLEMENTATION ==========

	/**
	 * Constructor.
	 * 
	 * @param name the name of this rule.
	 * @param getter the getter that will get the value to check.
	 * @param minValue the minimum integer value allowed.
	 * @param maxValue the maximum integer value allowed.
	 */
	public IntegerValidationRule(String name, ValueGetter getter, String minValue, String maxValue)
	{
		this.name = name;
		this.getter = getter;

		if ( null != minValue )
		{
			this.minValue = new BigInteger(minValue);
		}
		if ( null != maxValue )
		{
			this.maxValue = new BigInteger(maxValue);
		}
	}

	public void validate(Messages messages)
	{
		String stringValue = getter.getValue();
		if ( stringValue.length() > 0 )
		{
			try
			{
				BigInteger value = new BigInteger(stringValue);

				if ( null != minValue && value.compareTo(minValue) < 0 )
				{
					throw new ValidationException(messages.get("validation.mustBe.greaterThanOrEqual", messages
							.get(name), String.valueOf(minValue)));
				}
				if ( null != maxValue && value.compareTo(maxValue) > 0 )
				{
					throw new ValidationException(messages.get("validation.mustBe.lessThanOrEqual", messages.get(name),
							String.valueOf(maxValue)));
				}
			}
			catch ( NumberFormatException ex )
			{
				throw new ValidationException(messages.get("validation.mustBe.integer", messages.get(name)));
			}
		}
	}
}
