package org.stenerud.hse.base.ui.validation;

import org.stenerud.hse.base.ui.Messages;

/**
 * A rule that only gets processed if a condition is met.
 * 
 * @author Karl Stenerud
 */
public class ConditionalRule implements ValidationRule
{
	// ========== INJECTED MEMBERS ==========
	private ValidationRule validationRule;
	private ConditionalGetter conditionalGetter;

	// ========== IMPLEMENTATION ==========

	/**
	 * Constructor.
	 * 
	 * @param validationRule the rule to run if the condition is met.
	 * @param conditionalGetter the condition to meet.
	 */
	public ConditionalRule(ValidationRule validationRule, ConditionalGetter conditionalGetter)
	{
		this.validationRule = validationRule;
		this.conditionalGetter = conditionalGetter;
	}

	public void validate(Messages messages)
	{
		if ( conditionalGetter.getCondition() )
		{
			validationRule.validate(messages);
		}
	}
}
