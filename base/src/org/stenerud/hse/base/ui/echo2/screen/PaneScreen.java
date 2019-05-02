package org.stenerud.hse.base.ui.echo2.screen;

import nextapp.echo2.app.Component;
import nextapp.echo2.app.ContentPane;
import nextapp.echo2.app.event.ActionEvent;
import nextapp.echo2.app.event.ActionListener;

import org.stenerud.hse.base.data.DataException;
import org.stenerud.hse.base.data.DisconnectException;
import org.stenerud.hse.base.ui.Messages;
import org.stenerud.hse.base.ui.echo2.BaseApplicationHelper;
import org.stenerud.hse.base.ui.echo2.component.OkRequestor;
import org.stenerud.hse.base.ui.echo2.component.TrueFalseRequestor;
import org.stenerud.hse.base.ui.echo2.validation.ComponentValidationException;
import org.stenerud.hse.base.ui.validation.ValidationException;

/**
 * A Screen based on a ContentPane. <br>
 * This provides some convenience methods for subclasses.
 * 
 * @author Karl Stenerud
 */
public abstract class PaneScreen extends ContentPane implements Screen
{
	private static final long serialVersionUID = 1L;

	// ========== INTERNAL CLASSES ==========

	/**
	 * A listener for validation exceptions.
	 */
	private static class ValidationExceptionListener implements ActionListener
	{
		private static final long serialVersionUID = 1L;

		private ComponentValidationException ex;

		public ValidationExceptionListener(ComponentValidationException ex)
		{
			this.ex = ex;
		}

		public void actionPerformed(ActionEvent e)
		{
			BaseApplicationHelper.setFocus(ex.getComponent());
		}
	}

	// ========== INJECTED MEMBERS ==========
	protected Messages messages;

	// ========== PRIVATE MEMBERS ==========
	private boolean initialized = false;

	// ========== IMPLEMENTATION ==========

	protected abstract void initComponents();

	protected abstract void resetComponents();

	public abstract String getTitle();

	public final void enter()
	{
		if ( !initialized )
		{
			initComponents();
			initialized = true;
		}
		resetComponents();
	}

	public boolean leave()
	{
		return true;
	}

	/**
	 * Open a modal information requestor.
	 * 
	 * @param title the title of the requestor.
	 * @param message the message to display in the requestor.
	 * @return the opened requestor.
	 */
	protected OkRequestor openInfoRequestor(String title, String message)
	{
		return BaseApplicationHelper.displayOkRequestor(title, message);
	}

	/**
	 * Open a modal requestor with a "Yes" and "No" button.
	 * 
	 * @param title the title of the requestor.
	 * @param message the message to display in the requestor.
	 * @param listener the lister to notify when the user clicks a button.
	 * @return the opened requestor.
	 */
	protected TrueFalseRequestor openYesNoRequestor(String title, String message, ActionListener listener)
	{
		return BaseApplicationHelper.displayYesNoRequestor(title, message, listener);
	}

	protected void setFocusedComponent(Component component)
	{
		BaseApplicationHelper.setFocus(component);
	}

	/**
	 * General handler for data exceptions.
	 * 
	 * @param exception the exception.
	 */
	protected void handleDataException(DataException exception)
	{
		handleDataException(exception, null);
	}

	/**
	 * General handler for data exceptions.
	 * 
	 * @param exception the exception.
	 * @param recordId any associated ID when the exception was thrown.
	 */
	protected void handleDataException(DataException exception, String recordId)
	{
		String message;
		if ( null != recordId )
		{
			message = messages.get("error.data.recordId", recordId) + ": " + exception.getMessage();
		}
		else
		{
			message = messages.get("error.data") + ": " + exception.getMessage();
		}

		openInfoRequestor(messages.get("error.error"), message);
	}

	/**
	 * Handle a validation exception.
	 * 
	 * @param ex the exception.
	 */
	protected void handleValidationException(ValidationException ex)
	{
		OkRequestor box = openInfoRequestor(messages.get("error.error"), ex.getMessage());
		if ( ex instanceof ComponentValidationException )
		{
			box.addActionListener(new ValidationExceptionListener((ComponentValidationException)ex));
		}
	}

	/**
	 * Handle a database disconnect exception.
	 */
	protected boolean handleException(Throwable t)
	{
		if ( t instanceof DisconnectException )
		{
			openInfoRequestor(messages.get("error.noDatabaseConnection.title"), messages
					.get("error.noDatabaseConnection.description"));
			return true;
		}
		return false;
	}

	// ========== GETTERS AND SETTERS ==========

	public Messages getMessages()
	{
		return messages;
	}

	public void setMessages(Messages messages)
	{
		this.messages = messages;
	}
}
