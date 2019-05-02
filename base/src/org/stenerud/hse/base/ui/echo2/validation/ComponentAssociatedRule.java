package org.stenerud.hse.base.ui.echo2.validation;

import org.stenerud.hse.base.ui.Messages;
import org.stenerud.hse.base.ui.validation.ValidationException;
import org.stenerud.hse.base.ui.validation.ValidationRule;

import nextapp.echo2.app.Component;

/**
 * Wrapper rule that traps exceptions and throws a derived exception that
 * contains the associated component.
 * 
 * @author Karl Stenerud
 */
public class ComponentAssociatedRule implements ValidationRule
{
	// ========== INJECTED MEMBERS ==========
	private ValidationRule realRule;
	private Component component;

	// ========== IMPLEMENTATION ==========

	/**
	 * Constructor.
	 * 
	 * @param realRule the actual rule.
	 * @param component the associated component.
	 */
	public ComponentAssociatedRule(ValidationRule realRule, Component component)
	{
		this.realRule = realRule;
		this.component = component;
	}

	public void validate(Messages messages)
	{
		// Try to run the rule.
		try
		{
			realRule.validate(messages);
		}
		catch ( ValidationException ex )
		{
			// Wrap the exception.
			throw new ComponentValidationException(component, ex);
		}
	}

	public Component getComponent()
	{
		return component;
	}
}
