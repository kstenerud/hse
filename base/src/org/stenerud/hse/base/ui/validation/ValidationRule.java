package org.stenerud.hse.base.ui.validation;

import org.stenerud.hse.base.ui.Messages;

/**
 * A rule for validation.
 * 
 * @author Karl Stenerud
 */
public interface ValidationRule
{
	/**
	 * Validate this rule.
	 * 
	 * @param messages the localized messages to use.
	 * @throws ValidationException if the validation fails.
	 */
	public void validate(Messages messages);
}
