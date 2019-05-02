package org.stenerud.hse.base.ui.echo2.tool;

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
	protected static final int MAX_TEXT_CHARACTER_WIDTH = 14;
	protected static final int MAX_NUMERIC_CHARACTER_WIDTH = 10;

	// ========== IMPLEMENTATION ==========

	/**
	 * Build a text field.
	 * 
	 * @param actualWidth the width of the field (-1 = browser default).
	 * @param visibleWidth the visible width to set (-1 = browser default).
	 * @return the text field.
	 */
	public TextField buildTextField(int actualWidth, int visibleWidth)
	{
		TextField field = new TextField();
		setTextFieldWidth(field, actualWidth, visibleWidth);
		return field;
	}

	/**
	 * Build a password field.
	 * 
	 * @param actualWidth the width of the field (-1 = browser default).
	 * @param visibleWidth the visible width to set (-1 = browser default).
	 * @return the password field.
	 */
	public PasswordField buildPasswordField(int actualWidth, int visibleWidth)
	{
		PasswordField field = new PasswordField();
		setTextFieldWidth(field, actualWidth, visibleWidth);
		return field;
	}

	/**
	 * Build an integer text field.
	 * 
	 * @param actualWidth the width of the field (-1 = browser default).
	 * @param visibleWidth the visible width to set (-1 = browser default).
	 * @return the text field.
	 */
	public TextField buildIntegerTextField(int actualWidth, int visibleWidth)
	{
		TextField field = new TextField();
		field.setAlignment(new Alignment(Alignment.RIGHT, Alignment.CENTER));
		setNumericFieldWidth(field, actualWidth, visibleWidth);
		return field;
	}

	/**
	 * Set the width of a text field.
	 * 
	 * @param field the field to resize.
	 * @param actualWidth the width of the field (-1 = browser default).
	 * @param visibleWidth the visible width to set (-1 = browser default).
	 */
	public void setTextFieldWidth(TextField field, int actualWidth, int visibleWidth)
	{
		if ( actualWidth >= 0 )
		{
			field.setMaximumLength(actualWidth);
		}
		if ( visibleWidth >= 0 )
		{
			field.setWidth(new Extent(visibleWidth * MAX_TEXT_CHARACTER_WIDTH, Extent.PX));
		}
	}

	/**
	 * Set the width of a numeric field.
	 * 
	 * @param field the field to resize.
	 * @param actualWidth the width of the field (-1 = browser default).
	 * @param visibleWidth the visible width to set (-1 = browser default).
	 */
	public void setNumericFieldWidth(TextField field, int actualWidth, int visibleWidth)
	{
		if ( actualWidth >= 0 )
		{
			field.setMaximumLength(actualWidth);
		}
		if ( visibleWidth >= 0 )
		{
			field.setWidth(new Extent(visibleWidth * MAX_NUMERIC_CHARACTER_WIDTH, Extent.PX));
		}
	}
}
