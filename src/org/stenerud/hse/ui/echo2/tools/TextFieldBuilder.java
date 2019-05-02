package org.stenerud.hse.ui.echo2.tools;

import nextapp.echo2.app.Alignment;
import nextapp.echo2.app.Extent;
import nextapp.echo2.app.PasswordField;
import nextapp.echo2.app.TextField;

/**
 * Helper class for building text fields.
 * 
 * @author Karl Stenerud
 */
public class TextFieldBuilder
{
	// ========== CONSTANTS ==========

	// Guesstimates for text pixel widths.
	private static final int MAX_TEXT_CHARACTER_WIDTH = 14;
	private static final int MAX_NUMERIC_CHARACTER_WIDTH = 10;

	// ========== IMPLEMENTATION ==========

	/**
	 * Build a text field.
	 * 
	 * @param length the length of the field.
	 * @param resizeField if true, resize the field to fit the length.
	 * @return the text field.
	 */
	public TextField buildTextField(int length, boolean resizeField)
	{
		TextField textField = new TextField();
		if ( resizeField )
		{
			setTextWidth(textField, length);
		}
		return textField;
	}

	/**
	 * Build a text field.
	 * 
	 * @param length the length of the field.
	 * @param resizeField if true, resize the field to fit the length.
	 * @return the text field.
	 */
	public PasswordField buildPasswordField(int length, boolean resizeField)
	{
		PasswordField passwordField = new PasswordField();
		if ( resizeField )
		{
			setTextWidth(passwordField, length);
		}
		return passwordField;
	}

	/**
	 * Build an integer text field.
	 * 
	 * @param length the length of the field.
	 * @param resizeField if true, resize the field to fit the length.
	 * @return the text field.
	 */
	public TextField buildIntegerTextField(int length, boolean resizeField)
	{
		TextField textField = new TextField();
		textField.setAlignment(new Alignment(Alignment.RIGHT, Alignment.CENTER));
		if ( resizeField )
		{
			setNumericWidth(textField, length);
		}
		return textField;
	}

	/**
	 * Automatically set the maximum allowed size, and the width of a text field component.
	 * 
	 * @param text the text field whose size and width to set.
	 * @param length the maximum length of the field.
	 */
	public void setTextWidth(TextField text, int length)
	{
		text.setMaximumLength(length);
		setTextVisibleWidth(text, length);
	}

	/**
	 * Automatically set the maximum allowed size, and the width of a text field component.
	 * 
	 * @param text the text field whose size and width to set.
	 * @param length the maximum length of the field.
	 */
	public void setNumericWidth(TextField text, int length)
	{
		text.setMaximumLength(length);
		setNumericVisibleWidth(text, length);
	}

	/**
	 * Set the visible width of a text field.
	 * 
	 * @param text the text field whose size and width to set.
	 * @param length the maximum length of the field.
	 */
	public void setTextVisibleWidth(TextField text, int length)
	{
		text.setWidth(new Extent(length * MAX_TEXT_CHARACTER_WIDTH, Extent.PX));
	}

	/**
	 * Set the visible width of a numeric field.
	 * 
	 * @param text the text field whose size and width to set.
	 * @param length the maximum length of the field.
	 */
	public void setNumericVisibleWidth(TextField text, int length)
	{
		text.setWidth(new Extent(length * MAX_NUMERIC_CHARACTER_WIDTH, Extent.PX));
	}

}
