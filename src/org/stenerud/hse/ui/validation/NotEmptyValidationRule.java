package org.stenerud.hse.ui.validation;

import org.stenerud.hse.ui.Messages;

/**
 * Rule to check that a value is not empty (size = 0)
 * 
 * @author Karl Stenerud
 */
public class NotEmptyValidationRule implements ValidationRule
{
	// ========== INJECTED MEMBERS ==========
	private String name;
	private ValueGetter getter;
	private String message;

	// ========== IMPLEMENTATION ==========

	/**
	 * Constructor.
	 * 
	 * @param name the name of this rule.
	 * @param getter the getter that will get the value.
	 */
	public NotEmptyValidationRule(String name, ValueGetter getter)
	{
		this(name, getter, "validation.cannotBe.empty");
	}

	/**
	 * Constructor.
	 * 
	 * @param name the name of this rule.
	 * @param getter the getter that will get the value.
	 * @param message the message to display if this rule fails.
	 */
	public NotEmptyValidationRule(String name, ValueGetter getter, String message)
	{
		this.name = name;
		this.getter = getter;
		this.message = message;
	}

	public void validate(Messages messages)
	{
		if ( getter.getValue().length() == 0 )
		{
			throw new ValidationException(messages.get(message, messages.get(name)));
		}
	}

}
