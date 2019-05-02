package org.stenerud.hse.base.ui.echo2;

import java.io.Serializable;
import java.util.LinkedList;

import nextapp.echo2.app.Component;
import nextapp.echo2.app.Window;
import nextapp.echo2.app.WindowPane;
import nextapp.echo2.app.componentxml.ComponentXmlException;
import nextapp.echo2.app.componentxml.StyleSheetLoader;
import nextapp.echo2.app.event.ActionEvent;
import nextapp.echo2.app.event.ActionListener;
import nextapp.echo2.webrender.ClientProperties;

import org.stenerud.hse.base.tool.PropertyHelper;
import org.stenerud.hse.base.ui.Messages;
import org.stenerud.hse.base.ui.echo2.component.CustomWindowPane;
import org.stenerud.hse.base.ui.echo2.component.OkRequestor;
import org.stenerud.hse.base.ui.echo2.component.Requestor;
import org.stenerud.hse.base.ui.echo2.component.TrueFalseRequestor;
import org.stenerud.hse.base.ui.echo2.screen.Screen;
import org.stenerud.hse.base.ui.echo2.screen.ScreenManager;

/**
 * An abstract Echo2 user interface to the application. <br>
 * This class will build the main window and call buildMainContainer(). The
 * concrete implementation then adds the main UI components.
 * 
 * @author Karl Stenerud
 */
public abstract class Echo2UserInterface implements Serializable
{
	private static final long serialVersionUID = 1L;

	// ========== CONSTANTS ==========
	private static final String THEME_PATH = "hse/theme/";

	// ========== INJECTED MEMBERS ==========
	protected PropertyHelper properties;
	protected boolean supportsPng;
	protected ScreenManager screenManager;
	protected Messages messages;

	// ========== PRIVATE MEMBERS ==========
	private Window window;
	private Screen content;
	private int screenWidth;
	private int screenHeight;
	private String theme;
	private LinkedList subscreenStack = new LinkedList();

	// ========== IMPLEMENTATION ==========

	/**
	 * Set the text in the title area of the application.
	 * 
	 * @param value the title to set.
	 */
	public abstract void setTitle(String value);

	/**
	 * Get the component that will contain the current screen. <br>
	 * This must be a Pane subclass if any of your screens derive from Pane.
	 * 
	 * @return the component.
	 */
	protected abstract Component getScreenContainer();

	/**
	 * Build the user interface (Add all the main components to the window).
	 */
	protected abstract void buildUserInterface();

	/**
	 * Used by Echo2Application to get the main echo2 window.
	 * 
	 * @return the echo2 window.
	 */
	public Window getWindow()
	{
		return window;
	}

	/**
	 * Begin the user interface session.
	 */
	public void beginSession()
	{
		window = new Window();

		ClientProperties clientProperties = BaseApplicationHelper.getActiveApplication().getClientProperties();
		screenWidth = clientProperties.getInt(ClientProperties.SCREEN_WIDTH, 0);
		screenHeight = clientProperties.getInt(ClientProperties.SCREEN_HEIGHT, 0);

		buildUserInterface();
		showInitialScreen();
	}

	/**
	 * Show the initial screen that is displayed when a new session is started.
	 */
	public void showInitialScreen()
	{
		setScreen(screenManager.getInitialScreen());
	}

	/**
	 * Show the main screen when a user is logged in.
	 */
	public void showMainScreen()
	{
		setScreen(screenManager.getMainScreen());
	}

	/**
	 * End the user interface session. <br>
	 * If "endSession.redirect" is set in application.properties, redirect
	 * there. Otherwise show the initial screen.
	 */
	public void endSession()
	{
		removeAllWindowPanes();

		if ( properties.hasProperty("endSession.redirect") )
		{
			// End the session and redirect the user as per
			// application.properties.
			BaseApplicationHelper.redirect("endSession");
		}
		else
		{
			// Return to the initial screen. The session is preserved.
			showInitialScreen();
		}
	}

	/**
	 * Set the currently displayed screen.
	 * 
	 * @param screen the screen to display.
	 */
	protected void setScreen(Screen screen)
	{
		subscreenStack.clear();

		if ( null != content )
		{
			if ( !content.leave() )
			{
				return;
			}

			if ( screen == content )
			{
				return;
			}

			getScreenContainer().remove((Component)content);
		}

		content = screen;
		getScreenContainer().add((Component)content);
		setTitle(content.getTitle());
		this.content.enter();
	}

	public void showSubScreen(Screen screen)
	{
		if ( null != content )
		{
			subscreenStack.add(content);
			getScreenContainer().remove((Component)content);
		}
		content = screen;
		getScreenContainer().add((Component)content);
		setTitle(content.getTitle());
		this.content.enter();
	}

	public void showParentScreen()
	{
		if ( subscreenStack.size() > 0 )
		{
			if ( null != content )
			{
				if ( !content.leave() )
				{
					return;
				}

				getScreenContainer().remove((Component)content);
			}

			content = (Screen)subscreenStack.removeLast();
			getScreenContainer().add((Component)content);
			setTitle(content.getTitle());
			this.content.enter();
		}
	}

	/**
	 * Display a WindowPane.
	 * 
	 * @param windowPane the WindowPane to display
	 */
	public void displayWindow(WindowPane windowPane)
	{
		window.getContent().add(windowPane);
	}

	/**
	 * Display a modal requestor.
	 * 
	 * @param requestor the requestor.
	 */
	public void displayRequestor(CustomWindowPane requestor)
	{
		// If this is a requestor class, disable tasks and set an action listener
		// to re-enable them.
		if ( Requestor.class.isAssignableFrom(requestor.getClass()) )
		{
			if ( BaseApplicationHelper.getActiveApplication().isTasksEnabled() && requestor.isModal() )
			{
				BaseApplicationHelper.getActiveApplication().setTasksEnabled(false);
				((Requestor)requestor).addActionListener(new ActionListener()
				{
					private static final long serialVersionUID = 1L;

					public void actionPerformed(ActionEvent arg0)
					{
						BaseApplicationHelper.getActiveApplication().setTasksEnabled(true);
					}

				});
			}
		}

		window.getContent().add(requestor);
		requestor.display();
	}

	/**
	 * Display a modal requestor with an "OK" button.
	 * 
	 * @param title the title of the requestor.
	 * @param message the message to display inside the requestor.
	 * @return the requestor.
	 */
	public OkRequestor displayOkRequestor(String title, String message)
	{
		OkRequestor requestor = new OkRequestor(title, message, messages.get("answer.ok"));
		displayRequestor(requestor);
		return requestor;
	}

	/**
	 * Display a modal requestor with a "Yes" and "No" button.
	 * 
	 * @param title the title of the requestor.
	 * @param message the message to display in the requestor.
	 * @param listener the lister to notify when the user clicks a button.
	 * @return the opened requestor.
	 */
	public TrueFalseRequestor displayYesNoRequestor(String title, String message, ActionListener listener)
	{
		TrueFalseRequestor requestor = new TrueFalseRequestor(title, message, messages.get("answer.yes"), messages
				.get("answer.no"));
		requestor.addActionListener(listener);
		BaseApplicationHelper.displayRequestor(requestor);
		return requestor;
	}

	/**
	 * Remove all open WindowPanes.
	 */
	public void removeAllWindowPanes()
	{
		Component windowContent = window.getContent();
		Component[] components = windowContent.getComponents();
		for ( int i = 0; i < components.length; i++ )
		{
			if ( components[i] instanceof WindowPane )
			{
				windowContent.remove(components[i]);
			}
		}
	}

	// ========== GETTERS AND SETTERS ==========

	/**
	 * Set the theme for this instance.
	 * 
	 * @param theme the name of the theme.
	 */
	public void setTheme(String theme)
	{
		this.theme = theme;
		try
		{
			BaseApplicationHelper.getActiveApplication().setStyleSheet(
					StyleSheetLoader.load(THEME_PATH + theme + "/stylesheet.xml", Thread.currentThread()
							.getContextClassLoader()));
		}
		catch ( ComponentXmlException ex )
		{
			throw new RuntimeException(ex);
		}
	}

	public String getTheme()
	{
		return theme;
	}

	public PropertyHelper getProperties()
	{
		return properties;
	}

	public void setProperties(PropertyHelper properties)
	{
		this.properties = properties;
	}

	public boolean isSupportsPng()
	{
		return supportsPng;
	}

	public void setSupportsPng(boolean supportsPng)
	{
		this.supportsPng = supportsPng;
	}

	public int getScreenHeight()
	{
		return screenHeight;
	}

	public int getScreenWidth()
	{
		return screenWidth;
	}

	public ScreenManager getScreenManager()
	{
		return screenManager;
	}

	public void setScreenManager(ScreenManager screenManager)
	{
		this.screenManager = screenManager;
	}

	public Messages getMessages()
	{
		return messages;
	}

	public void setMessages(Messages messages)
	{
		this.messages = messages;
	}
}
