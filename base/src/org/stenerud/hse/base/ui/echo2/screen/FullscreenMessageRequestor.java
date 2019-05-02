package org.stenerud.hse.base.ui.echo2.screen;

import org.stenerud.hse.base.data.DataException;
import org.stenerud.hse.base.ui.echo2.validation.ComponentValidationException;
import org.stenerud.hse.base.ui.validation.ValidationException;

import echopointng.Strut;

import nextapp.echo2.app.Color;
import nextapp.echo2.app.Column;
import nextapp.echo2.app.Label;
import nextapp.echo2.app.SplitPane;

/**
 * A modal requestor. <br>
 * This is the base class for most modal dialog boxes.
 */
public abstract class FullscreenMessageRequestor extends FullscreenRequestor
{
	private static final long serialVersionUID = 1L;

	// ========== PRIVATE MEMBERS ==========
	private Label messageLabel = new Label();
	private Color originalColor = messageLabel.getForeground();

	// ========== IMPLEMENTATION ==========

	/**
	 * Constructor.
	 * 
	 * @param title the title of the message box
	 * @param message the message to display
	 */
	public FullscreenMessageRequestor(String title, String message)
	{
		super(title);
		messageLabel.setText(message);
	}

	/**
	 * Default Constructor.
	 */
	public FullscreenMessageRequestor()
	{
		super();
	}

	protected void initContents(SplitPane container)
	{
		Column contents = new Column();
		container.add(contents);
		contents.add(messageLabel);
		contents.add(new Strut());

		initContents(contents);
	}

	protected void resetComponents()
	{
		clearStatus();
	}

	/**
	 * Initialize the main contents of the requestor.
	 * 
	 * @param contents the component containing the main contents. This will
	 *           have the main message lable in it already.
	 */
	protected abstract void initContents(Column contents);

	/**
	 * Set the message in this requestor.
	 * 
	 * @param message the message.
	 */
	protected void setMessage(String message)
	{
		messageLabel.setText(message);
	}

	/**
	 * Set an error message.
	 * 
	 * @param message the message.
	 */
	protected void setErrorStatus(String message)
	{
		messageLabel.setText(message);
		messageLabel.setForeground(Color.RED);
	}

	/**
	 * Clear the status.
	 */
	protected void clearStatus()
	{
		messageLabel.setForeground(originalColor);
	}

	/**
	 * Handle a validation exception. <br>
	 * his provides a centralized way to deal with validation exceptions.
	 * 
	 * @param ex the validation exception.
	 */
	protected void handleValidationException(ValidationException ex)
	{
		setErrorStatus(ex.getMessage());
		if ( ex instanceof ComponentValidationException )
		{
			setFocusedComponent(((ComponentValidationException)ex).getComponent());
		}
	}

	/**
	 * Handle a generic data exception.
	 * 
	 * @param exception the exception.
	 */
	protected void handleDataException(DataException exception)
	{
		handleDataException(exception, null);
	}

	/**
	 * Handle a data exception.
	 * 
	 * @param exception the exception.
	 * @param recordId the associated record ID, if any.
	 */
	protected void handleDataException(DataException exception, String recordId)
	{
		if ( null != recordId )
		{
			setErrorStatus(messages.get("error.data.recordId", recordId) + ": " + exception.getMessage());
		}
		else
		{
			setErrorStatus(messages.get("error.data") + ": " + exception.getMessage());
		}
	}
}
