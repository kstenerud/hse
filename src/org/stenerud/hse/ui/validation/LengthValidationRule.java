package org.stenerud.hse.ui.validation;

import org.stenerud.hse.ui.Messages;

/**
 * Rule to enforce length restrictions on a value. <br>
 * Note: this rule does not fire if the value is an empty string.
 * 
 * @author Karl Stenerud
 */
public class LengthValidationRule implements ValidationRule
{
	private int minLength;
	private int maxLength;
	private ValueGetter getter;
	private String name;

	/**
	 * Constructor.
	 * 
	 * @param name the name of this rule.
	 * @param getter the getter that will get the value.
	 * @param exactLength the exact length that the value must be.
	 */
	public LengthValidationRule(String name, ValueGetter getter, int exactLength)
	{
		this(name, getter, -1, exactLength);
	}

	/**
	 * Constructor.
	 * 
	 * @param name the name of this rule.
	 * @param getter the getter that will get the value.
	 * @param minLength the minimum string length allowed (-1 = minLength is same as maxLength).
	 * @param maxLength the maximum string length allowed.
	 */
	public LengthValidationRule(String name, ValueGetter getter, int minLength, int maxLength)
	{
		this.name = name;
		this.getter = getter;
		this.minLength = minLength;
		this.maxLength = maxLength;

		if ( this.minLength < 0 )
		{
			this.minLength = this.maxLength;
		}
	}

	public void validate(Messages messages)
	{
		int length = getter.getValue().length();
		if ( length > 0 )
		{
			if ( minLength == maxLength && length != minLength )
			{
				throw new ValidationException(messages.get("validation.mustBe.exactLength", messages.get(name), String
						.valueOf(minLength)));
			}
			if ( length < minLength )
			{
				throw new ValidationException(messages.get("validation.mustBe.atLeastLength", messages.get(name),
						String.valueOf(minLength)));
			}
			if ( length > maxLength )
			{
				throw new ValidationException(messages.get("validation.mustBe.atMostLength", messages.get(name), String
						.valueOf(maxLength)));
			}
		}
	}
}
