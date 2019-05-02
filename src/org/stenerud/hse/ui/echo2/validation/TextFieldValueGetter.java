package org.stenerud.hse.ui.echo2.validation;

import org.stenerud.hse.ui.validation.ValueGetter;

import nextapp.echo2.app.TextField;

/**
 * Value getter for a text field.
 * 
 * @author Karl Stenerud
 */
public class TextFieldValueGetter implements ValueGetter
{
	// ========== INJECTED MEMBERS ==========
	private TextField field;

	// ========== IMPLEMENTATION ==========

	/**
	 * Constructor.
	 * 
	 * @param field the text field to get the value from.
	 */
	public TextFieldValueGetter(TextField field)
	{
		this.field = field;
	}

	public String getValue()
	{
		return field.getText();
	}

}
