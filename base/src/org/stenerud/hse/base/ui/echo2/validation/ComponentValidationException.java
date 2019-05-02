package org.stenerud.hse.base.ui.echo2.validation;

import org.stenerud.hse.base.ui.validation.ValidationException;

import nextapp.echo2.app.Component;

/**
 * Validation exception containing the associated component.
 * 
 * @author Karl Stenerud
 */
public class ComponentValidationException extends ValidationException
{
	private static final long serialVersionUID = 1L;

	// ========== INJECTED MEMBERS ==========
	private Component component;

	// ========== IMPLEMENTATION ==========

	/**
	 * Constructor.
	 * 
	 * @param component the associated component.
	 * @param ex the real exception.
	 */
	public ComponentValidationException(Component component, Exception ex)
	{
		super(ex.getMessage(), ex);
		this.component = component;
	}

	public Component getComponent()
	{
		return component;
	}
}
