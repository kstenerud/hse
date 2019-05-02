package org.stenerud.hse.ui.echo2;

import java.io.Serializable;

import nextapp.echo2.app.Column;
import nextapp.echo2.app.Component;
import nextapp.echo2.app.ContentPane;
import nextapp.echo2.app.Extent;
import nextapp.echo2.app.Label;
import nextapp.echo2.app.SplitPane;
import nextapp.echo2.app.Window;
import nextapp.echo2.app.componentxml.ComponentXmlException;
import nextapp.echo2.app.componentxml.StyleSheetLoader;
import nextapp.echo2.app.event.ActionEvent;
import nextapp.echo2.app.event.ActionListener;
import nextapp.echo2.app.layout.SplitPaneLayoutData;
import nextapp.echo2.webcontainer.ContainerContext;
import nextapp.echo2.webrender.ClientProperties;

import org.stenerud.hse.business.group.GroupBusiness;
import org.stenerud.hse.business.user.UserBusiness;
import org.stenerud.hse.security.CurrentUser;
import org.stenerud.hse.security.Security;
import org.stenerud.hse.tools.PropertyHelper;
import org.stenerud.hse.ui.Messages;
import org.stenerud.hse.ui.echo2.components.TrueFalseRequestor;
import org.stenerud.hse.ui.echo2.screen.Screen;
import org.stenerud.hse.ui.echo2.screen.ScreenFactory;
import org.stenerud.hse.ui.echo2.screen.Screens;
import org.stenerud.hse.ui.servlet.Echo2Application;

import echopointng.Menu;

/**
 * An Echo2 user interface to the application.
 * 
 * @author Karl Stenerud
 */
public class Echo2UserInterface implements Serializable
{
	private static final long serialVersionUID = 1L;

	// ========== CONSTANTS ==========
	private static final String THEME_PATH = "hse/theme/";
	private static final String DEFAULT_THEME = "RedmondX";

	private static final int TITLE_HEIGHT = 46;
	private static final int NAVMENU_HEIGHT = 24;
	private static final int SPACER_HEIGHT = 8;

	// ========== INJECTED MEMBERS ==========
	private CurrentUser currentUser;
	private GroupBusiness groupBusiness;
	private Messages messages;
	private PropertyHelper properties;
	private ScreenFactory screenFactory;
	private Security security;
	private UserBusiness userBusiness;
	private boolean supportsPng;

	// ========== PRIVATE MEMBERS ==========
	private Window window;
	private Component mainMenuContainer;
	private MainMenu mainMenu;
	private ContentPane mainContainer;
	private Screen content;
	private Label title;
	private int screenWidth;
	private int screenHeight;
	private String theme;

	// ========== IMPLEMENTATION ==========

	public static Echo2UserInterface getCurrent()
	{
		return Echo2Application.getUserInterface();
	}

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
	 * Set the text in the title area of the application.
	 * 
	 * @param value the title to set.
	 */
	public void setTitle(String value)
	{
		title.setText(value);
	}

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
			Echo2Application.getCurrent().setStyleSheet(
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

	public void beginSession()
	{
		setTheme(DEFAULT_THEME);

		window = new Window();

		ContainerContext containerContext = (ContainerContext)Echo2Application.getCurrent().getContextProperty(
				ContainerContext.CONTEXT_PROPERTY_NAME);
		ClientProperties clientProperties = containerContext.getClientProperties();
		screenWidth = clientProperties.getInt(ClientProperties.SCREEN_WIDTH, 0);
		screenHeight = clientProperties.getInt(ClientProperties.SCREEN_HEIGHT, 0);

		buildMainContainer();
	}

	/**
	 * Accept notification that the user has changed. <br>
	 * This resets parts of the application that depend on the user's permissions etc.
	 */
	public void notifyChangedUser()
	{
		buildMainMenu();
	}

	public void showLoginScreen()
	{
		setScreen(screenFactory.createScreen(Screens.LOGIN));
	}

	public void showMainScreen()
	{
		setScreen(screenFactory.createScreen(Screens.MAIN));
	}

	// ========== EVENT HANDLERS ==========

	private void logoutConfirm()
	{
		TrueFalseRequestor box = new TrueFalseRequestor(messages.get("prompt.logout.title"), messages
				.get("prompt.logout.description"), messages.get("answer.yes"), messages.get("answer.no"));
		box.addActionListener(new ActionListener()
		{
			private static final long serialVersionUID = 1L;

			public void actionPerformed(ActionEvent e)
			{
				if ( e.getActionCommand() == TrueFalseRequestor.COMMAND_TRUE )
				{
					logout();
				}
			}
		});
		displayRequestor(box);
	}

	private void logout()
	{
		security.logout();
		notifyChangedUser();

		if ( properties.getBoolean("logout.returnToLogin", true) )
		{
			// Return to the login screen. The session is preserved.
			showLoginScreen();
		}
		else
		{
			// End the session and redirect the user as per application.properties.
			ApplicationHelper.redirect("endSession");
		}
	}

	// ========== UTILITY METHODS ==========

	private void buildMainContainer()
	{

		ContentPane pane = new ContentPane();
		window.setContent(pane);

		// Outer container. Holds outer structure
		SplitPane outerSplitPane = new SplitPane(SplitPane.ORIENTATION_VERTICAL_TOP_BOTTOM, new Extent(TITLE_HEIGHT));
		pane.add(outerSplitPane);

		// Logo and top menu
		pane = new ContentPane();
		noScrollbars(pane);
		outerSplitPane.add(pane);

		// Title
		pane.setStyleName("TitleBackground");
		Column column = new Column();
		pane.add(column);
		title = new Label();
		title.setStyleName("Title");
		column.add(title);

		SplitPane menuSplitPane = new SplitPane(SplitPane.ORIENTATION_VERTICAL_TOP_BOTTOM, new Extent(NAVMENU_HEIGHT));
		noScrollbars(menuSplitPane);
		outerSplitPane.add(menuSplitPane);

		// Navigation Menu
		mainMenuContainer = new ContentPane();
		mainMenuContainer.setStyleName(MainMenu.STYLE_MENUBAR);
		noScrollbars(mainMenuContainer);
		menuSplitPane.add(mainMenuContainer);
		buildMainMenu();

		// Spacer
		SplitPane spacerSplitPane = new SplitPane(SplitPane.ORIENTATION_VERTICAL_TOP_BOTTOM, new Extent(SPACER_HEIGHT));
		noScrollbars(spacerSplitPane);
		menuSplitPane.add(spacerSplitPane);
		spacerSplitPane.add(new ContentPane());

		// Main container. Holds status bar in fixed portion (bottom)
		mainContainer = new ContentPane();
		noScrollbars(mainContainer);
		spacerSplitPane.add(mainContainer);
	}

	/**
	 * Build the main menu.
	 */
	private void buildMainMenu()
	{
		if ( null != mainMenu )
		{
			mainMenuContainer.remove(mainMenu);
		}

		// If a user is not logged in, short circuit.
		if ( !security.isLoggedIn() )
		{
			return;
		}

		mainMenu = new MainMenu();
		mainMenuContainer.add(mainMenu);

		Menu topBarMenu;

		// Administration
		topBarMenu = mainMenu.addMenu(null, MainMenu.STYLE_MENUBAR, messages.get("category.administration"));

		if ( userBusiness.canView() )
		{
			mainMenu.addItem(topBarMenu, MainMenu.STYLE_MENU, messages.get("screen.userEditor"), new ActionListener()
			{
				private static final long serialVersionUID = 1L;

				public void actionPerformed(ActionEvent e)
				{
					setScreen(screenFactory.createScreen(Screens.USER_EDITOR));
				}
			});
		}

		if ( groupBusiness.canView() )
		{
			mainMenu.addItem(topBarMenu, MainMenu.STYLE_MENU, messages.get("screen.groupEditor"), new ActionListener()
			{
				private static final long serialVersionUID = 1L;

				public void actionPerformed(ActionEvent e)
				{
					setScreen(screenFactory.createScreen(Screens.GROUP_EDITOR));
				}
			});
		}

		topBarMenu.setVisible(topBarMenu.getComponents().length > 0);

		// Logout
		mainMenu.addItem(null, MainMenu.STYLE_MENUBAR, messages.get("screen.logout"), new ActionListener()
		{
			private static final long serialVersionUID = 1L;

			public void actionPerformed(ActionEvent e)
			{
				logoutConfirm();
			}
		});
	}

	private void noScrollbars(Component component)
	{
		SplitPaneLayoutData layout = new SplitPaneLayoutData();
		layout.setOverflow(SplitPaneLayoutData.OVERFLOW_HIDDEN);
		component.setLayoutData(layout);
	}

	/**
	 * Set the contents of the main application container. <br>
	 * This is a convenience method to aid in switching between "screens".
	 * 
	 * @param content the content to set.
	 */
	private void setScreen(Screen content)
	{
		if ( null != this.content )
		{
			if ( !this.content.leave() )
			{
				return;
			}

			if ( content == this.content )
			{
				return;
			}

			mainContainer.remove((Component)this.content);
		}

		this.content = content;
		mainContainer.add((Component)this.content);
		setTitle(content.getTitle());
		this.content.enter();
	}

	/**
	 * Display a modal requestor.
	 * 
	 * @param requestor the requestor.
	 */
	private void displayRequestor(CustomWindowPane requestor)
	{
		window.getContent().add(requestor);
		requestor.display();
	}

	// ========== GETTERS AND SETTERS ==========

	public CurrentUser getCurrentUser()
	{
		return currentUser;
	}

	public void setCurrentUser(CurrentUser currentUser)
	{
		this.currentUser = currentUser;
	}

	public GroupBusiness getGroupBusiness()
	{
		return groupBusiness;
	}

	public void setGroupBusiness(GroupBusiness groupBusiness)
	{
		this.groupBusiness = groupBusiness;
	}

	public Messages getMessages()
	{
		return messages;
	}

	public void setMessages(Messages messages)
	{
		this.messages = messages;
	}

	public PropertyHelper getProperties()
	{
		return properties;
	}

	public void setProperties(PropertyHelper properties)
	{
		this.properties = properties;
	}

	public ScreenFactory getScreenFactory()
	{
		return screenFactory;
	}

	public void setScreenFactory(ScreenFactory screenFactory)
	{
		this.screenFactory = screenFactory;
	}

	public Security getSecurity()
	{
		return security;
	}

	public void setSecurity(Security security)
	{
		this.security = security;
	}

	public boolean isSupportsPng()
	{
		return supportsPng;
	}

	public void setSupportsPng(boolean supportsPng)
	{
		this.supportsPng = supportsPng;
	}

	public UserBusiness getUserBusiness()
	{
		return userBusiness;
	}

	public void setUserBusiness(UserBusiness userBusiness)
	{
		this.userBusiness = userBusiness;
	}

	public int getScreenHeight()
	{
		return screenHeight;
	}

	public int getScreenWidth()
	{
		return screenWidth;
	}
}
