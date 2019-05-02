package org.stenerud.hse.ui.echo2;

import nextapp.echo2.app.Command;
import nextapp.echo2.app.Component;
import nextapp.echo2.app.Window;
import nextapp.echo2.webcontainer.command.BrowserOpenWindowCommand;
import nextapp.echo2.webcontainer.command.BrowserRedirectCommand;

import org.stenerud.hse.ui.servlet.Echo2Application;

/**
 * Helper class for most application functions.
 * 
 * @author Karl Stenerud
 */
public class ApplicationHelper
{
	// ========== IMPLEMENTATION ==========

	/**
	 * Set the input focus.
	 * 
	 * @param component the component to set focus on.
	 */
	public static void setFocus(Component component)
	{
		getApplication().setFocusedComponent(component);
	}

	/**
	 * Get the current component with focus.
	 * 
	 * @return the component that has focus.
	 */
	public static Component getFocus()
	{
		return getApplication().getFocusedComponent();
	}

	/**
	 * Display a modal requestor.
	 * 
	 * @param requestor the requestor.
	 */
	public static void displayRequestor(CustomWindowPane requestor)
	{
		getWindow().getContent().add(requestor);
		requestor.display();
	}

	/**
	 * Set the text in the title area of the application.
	 * 
	 * @param value the title to set.
	 */
	public static void setTitle(String value)
	{
		getUserInterface().setTitle(value);
	}

	public static void popupWindow(String uri)
	{
		enqueueCommand(new BrowserOpenWindowCommand(uri, null, "menubar,resizable,scrollbars"));
	}

	public static void enqueueCommand(Command command)
	{
		getApplication().enqueueCommand(command);
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
		Echo2UserInterface.getCurrent().showMainScreen();
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
		return Echo2UserInterface.getCurrent().getScreenWidth();
	}

	public static int getScreenHeight()
	{
		return Echo2UserInterface.getCurrent().getScreenHeight();
	}

	// ========== UTILITY METHODS ==========

	private static Echo2Application getApplication()
	{
		return Echo2Application.getCurrent();
	}

	private static Echo2UserInterface getUserInterface()
	{
		return Echo2Application.getUserInterface();
	}

	private static Window getWindow()
	{
		return getUserInterface().getWindow();
	}
}
