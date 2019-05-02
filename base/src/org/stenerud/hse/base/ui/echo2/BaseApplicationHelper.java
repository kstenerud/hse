package org.stenerud.hse.base.ui.echo2;

import nextapp.echo2.app.ApplicationInstance;
import nextapp.echo2.app.Command;
import nextapp.echo2.app.Component;
import nextapp.echo2.app.event.ActionListener;
import nextapp.echo2.webcontainer.command.BrowserOpenWindowCommand;
import nextapp.echo2.webcontainer.command.BrowserRedirectCommand;

import org.stenerud.hse.base.ui.echo2.component.CustomWindowPane;
import org.stenerud.hse.base.ui.echo2.component.OkRequestor;
import org.stenerud.hse.base.ui.echo2.component.TrueFalseRequestor;
import org.stenerud.hse.base.ui.servlet.Echo2Application;

/**
 * Helper class for most application functions.
 * 
 * @author Karl Stenerud
 */
public class BaseApplicationHelper
{
	// ========== IMPLEMENTATION ==========

	/**
	 * Get the currently active application.
	 * 
	 * @return the currently active application.
	 */
	public static Echo2Application getActiveApplication()
	{
		return (Echo2Application)ApplicationInstance.getActive();
	}

	/**
	 * Get the currently active user interface.
	 * 
	 * @return the currently active user interface.
	 */
	public static Echo2UserInterface getActiveUserInterface()
	{
		return getActiveApplication().getUserInterface();
	}

	/**
	 * Set the input focus.
	 * 
	 * @param component the component to set focus on.
	 */
	public static void setFocus(Component component)
	{
		getActiveApplication().setFocusedComponent(component);
	}

	/**
	 * Get the current component with focus.
	 * 
	 * @return the component that has focus.
	 */
	public static Component getFocus()
	{
		return getActiveApplication().getFocusedComponent();
	}

	/**
	 * Display a modal requestor.
	 * 
	 * @param requestor the requestor.
	 */
	public static void displayRequestor(CustomWindowPane requestor)
	{
		getActiveUserInterface().displayRequestor(requestor);
	}

	/**
	 * Display a modal requestor with an "OK" button.
	 * 
	 * @param title the title of the requestor.
	 * @param message the message to display inside the requestor.
	 * @return the requestor.
	 */
	public static OkRequestor displayOkRequestor(String title, String message)
	{
		return getActiveUserInterface().displayOkRequestor(title, message);
	}

	/**
	 * Display a modal requestor with a "Yes" and "No" button.
	 * 
	 * @param title the title of the requestor.
	 * @param message the message to display in the requestor.
	 * @param listener the lister to notify when the user clicks a button.
	 * @return the opened requestor.
	 */
	public static TrueFalseRequestor displayYesNoRequestor(String title, String message, ActionListener listener)
	{
		return getActiveUserInterface().displayYesNoRequestor(title, message, listener);
	}

	/**
	 * Set the text in the title area of the application.
	 * 
	 * @param value the title to set.
	 */
	public static void setTitle(String value)
	{
		getActiveUserInterface().setTitle(value);
	}

	public static void popupWindow(String uri)
	{
		enqueueCommand(new BrowserOpenWindowCommand(uri, null, "menubar,resizable,scrollbars"));
	}

	public static void enqueueCommand(Command command)
	{
		getActiveApplication().enqueueCommand(command);
	}

	public static void redirect(String uri)
	{
		enqueueCommand(new BrowserRedirectCommand(uri));
	}

	/**
	 * Return to the main screen.
	 */
	public static void returnToMain()
	{
		getActiveUserInterface().showMainScreen();
	}

	/**
	 * End the current session.
	 */
	public static void endSession()
	{
		redirect("endSession");
	}

	public static int getScreenWidth()
	{
		return getActiveUserInterface().getScreenWidth();
	}

	public static int getScreenHeight()
	{
		return getActiveUserInterface().getScreenHeight();
	}
}
