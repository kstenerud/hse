package org.stenerud.hse.ui.validation;

import org.stenerud.hse.ui.Messages;

/**
 * Rule to check for inequality. <br>
 * Note: this rule does not fire if the value is an empty string.
 * 
 * @author Karl Stenerud
 */
public class NotEqualToValidationRule implements ValidationRule
{
	// ========== INJECTED MEMBERS ==========
	private ValueGetter getter;
	private String name;
	private String value;
	private boolean caseSensitive;

	// ========== IMPLEMENTATION ==========

	/**
	 * Constructor.
	 * 
	 * @param name the name of this rule.
	 * @param getter the getter that will get the value.
	 * @param value the value to compare against.
	 * @param caseSensitive if true, the comparison is case sensitive.
	 */
	public NotEqualToValidationRule(String name, ValueGetter getter, String value, boolean caseSensitive)
	{
		this.name = name;
		this.getter = getter;
		this.value = value;
		this.caseSensitive = caseSensitive;
	}

	public void validate(Messages messages)
	{
		String stringValue = caseSensitive ? getter.getValue() : getter.getValue().toUpperCase();

		if ( stringValue.length() > 0 )
		{
			String compareValue = caseSensitive ? this.value : this.value.toUpperCase();

			if ( compareValue.equals(stringValue) )
			{
				throw new ValidationException(messages.get("validation.cannotBe.equalTo", messages.get(name),
						this.value));
			}
		}
	}
}
