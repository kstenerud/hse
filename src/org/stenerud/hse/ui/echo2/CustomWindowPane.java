package org.stenerud.hse.ui.echo2;

import nextapp.echo2.app.Component;
import nextapp.echo2.app.Extent;
import nextapp.echo2.app.WindowPane;

import org.stenerud.hse.tools.ExceptionAnalyzer;
import org.stenerud.hse.ui.Messages;
import org.stenerud.hse.ui.echo2.components.OkRequestor;

/**
 * Custom WindowPane component to give some convenience methods.
 * 
 * @author Karl Stenerud
 */
public abstract class CustomWindowPane extends WindowPane
{
	private static final long serialVersionUID = 1L;

	// ========== INJECTED MEMBERS ==========
	protected Messages messages;

	// ========== PRIVATE MEMBERS ==========
	private boolean initialized = false;

	// ========== IMPLEMENTATION ==========

	/**
	 * Initialize the components in this window pane.
	 */
	protected abstract void initComponents();

	/**
	 * Reset the components in this window pane.
	 */
	protected abstract void resetComponents();

	/**
	 * Display this window.
	 */
	public final void display()
	{
		if ( !initialized )
		{
			initComponents();
			initialized = true;
		}
		resetComponents();
	}

	public CustomWindowPane()
	{
		super();
	}

	/**
	 * Constructor.
	 * 
	 * @param title the title of this window.
	 * @param width the width.
	 * @param height the height.
	 */
	public CustomWindowPane(String title, Extent width, Extent height)
	{
		super(title, width, height);
	}

	/**
	 * Set the currently focused component in this window.
	 * 
	 * @param component the component to set focus for.
	 */
	protected void setFocusedComponent(Component component)
	{
		ApplicationHelper.setFocus(component);
	}

	/**
	 * Pop up an external window.
	 * 
	 * @param uri the URI to display.
	 */
	protected void popupWindow(String uri)
	{
		ApplicationHelper.popupWindow(uri);
	}

	/**
	 * Handle a database disconnect exception.
	 */
	protected boolean handleException(Throwable t)
	{
		if ( ExceptionAnalyzer.isDatabaseDisconnect(t) )
		{
			OkRequestor requestor = new OkRequestor(messages.get("error.noConnection.title"), messages
					.get("error.noConnection.description"), messages.get("answer.ok"));
			ApplicationHelper.displayRequestor(requestor);
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
