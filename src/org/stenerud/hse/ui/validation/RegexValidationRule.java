package org.stenerud.hse.ui.validation;

import java.util.regex.Pattern;

import org.stenerud.hse.ui.Messages;

/**
 * Rule that matches against a regular expression. <br>
 * Note: this rule does not fire if the value is an empty string.
 * 
 * @author Karl Stenerud
 */
public class RegexValidationRule implements ValidationRule
{
	// ========== INJECTED MEMBERS ==========
	private String name;
	private ValueGetter getter;
	private String message;
	private String regex;

	// ========== PRIVATE MEMBERS ==========
	private Pattern pattern;

	// ========== IMPLEMENTATION ==========

	/**
	 * Constructor.
	 * 
	 * @param name the name of this rule.
	 * @param getter the getter that gets the value.
	 * @param regex the regular expression to match.
	 */
	public RegexValidationRule(String name, ValueGetter getter, String regex)
	{
		this(name, getter, regex, "validation.mustMatch");
	}

	/**
	 * Constructor.
	 * 
	 * @param name the name of this rule.
	 * @param getter the getter that gets the value.
	 * @param regex the regular expression to match.
	 * @param message the message to display if this rule fails. The first parameter to the message is the name, and
	 *            the second is the regular expression.
	 */
	public RegexValidationRule(String name, ValueGetter getter, String regex, String message)
	{
		this.name = name;
		this.getter = getter;
		this.regex = regex;
		pattern = Pattern.compile(regex);
		this.message = message;
	}

	public void validate(Messages messages)
	{
		String stringValue = getter.getValue();
		if ( stringValue.length() > 0 && !pattern.matcher(stringValue).matches() )
		{
			throw new ValidationException(messages.get(message, messages.get(name), regex));
		}
	}

}
