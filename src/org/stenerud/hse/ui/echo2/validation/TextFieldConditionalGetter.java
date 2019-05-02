package org.stenerud.hse.ui.echo2.validation;

import org.stenerud.hse.ui.validation.ConditionalGetter;

import nextapp.echo2.app.TextField;

/**
 * A conditional getter for text fields.
 * 
 * @author Karl Stenerud
 */
public class TextFieldConditionalGetter implements ConditionalGetter
{
	// ========== INJECTED MEMBERS ==========
	private TextField field;
	private String trueValue;
	private boolean invert;

	// ========== IMPLEMENTATION ==========

	/**
	 * Constructor.
	 * 
	 * @param field the text field to get the value from.
	 * @param trueValue the value meaning TRUE.
	 * @param invert if true, invert the result.
	 */
	public TextFieldConditionalGetter(TextField field, String trueValue, boolean invert)
	{
		this.field = field;
		this.trueValue = trueValue;
		this.invert = invert;
	}

	public boolean getCondition()
	{
		return invert ^ trueValue.equals(field.getText());
	}
}
