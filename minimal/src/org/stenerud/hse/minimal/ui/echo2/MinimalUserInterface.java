package org.stenerud.hse.minimal.ui.echo2;

import java.util.Iterator;
import java.util.List;

import org.stenerud.hse.base.ui.Messages;
import org.stenerud.hse.base.ui.echo2.Echo2UserInterface;
import org.stenerud.hse.base.ui.echo2.component.MainMenu;
import org.stenerud.hse.base.ui.echo2.screen.BaseDescription;
import org.stenerud.hse.base.ui.echo2.screen.CategoryDescription;
import org.stenerud.hse.base.ui.echo2.screen.ScreenDescription;

import echopointng.Menu;
import nextapp.echo2.app.Column;
import nextapp.echo2.app.Component;
import nextapp.echo2.app.ContentPane;
import nextapp.echo2.app.Extent;
import nextapp.echo2.app.Label;
import nextapp.echo2.app.SplitPane;
import nextapp.echo2.app.event.ActionEvent;
import nextapp.echo2.app.event.ActionListener;
import nextapp.echo2.app.layout.SplitPaneLayoutData;

/**
 * A minimal HSE user interface. <br>
 * This class builds the main application window, sets up a spot for screens to
 * be displayed, and builds a menu containing links to all visible screens.
 * <br>
 * The screens, and their visibility methods, are defined in screen.properties.
 * 
 * @author Karl Stenerud
 */
public class MinimalUserInterface extends Echo2UserInterface
{
	private static final long serialVersionUID = 1L;

	// ========== CONSTANTS ==========
	private static final String DEFAULT_THEME = "redmondx";

	private static final int TITLE_HEIGHT = 46;
	private static final int NAVMENU_HEIGHT = 24;
	private static final int SPACER_HEIGHT = 8;

	// ========== INJECTED MEMBERS ==========
	private Messages messages;

	// ========== PRIVATE MEMBERS ==========
	private ContentPane screenContainer = new ContentPane();
	private Component mainMenuContainer;
	private MainMenu mainMenu;
	private Label title;

	// ========== INTERNAL CLASSES ==========

	private class ScreenActivator implements ActionListener
	{
		private static final long serialVersionUID = 1L;

		private String name;

		public ScreenActivator(String name)
		{
			this.name = name;
		}

		public void actionPerformed(ActionEvent arg0)
		{
			setScreen(screenManager.getScreen(name));
		}
	}

	// ========== IMPLEMENTATION ==========

	public void setTitle(String value)
	{
		title.setText(value);
	}

	public void notifyUserChanged()
	{
		buildMainMenu();
	}

	protected Component getScreenContainer()
	{
		return screenContainer;
	}

	protected void buildUserInterface()
	{
		/*
		 * Build a user interface with a title at the top, a menu containing
		 * links to all of the visible screens, and leave the rest for screen
		 * real estate.
		 */
		setTheme(DEFAULT_THEME);

		ContentPane pane = new ContentPane();
		getWindow().setContent(pane);

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

		// SplitPane for the menu.
		SplitPane menuSplitPane = new SplitPane(SplitPane.ORIENTATION_VERTICAL_TOP_BOTTOM, new Extent(NAVMENU_HEIGHT));
		noScrollbars(menuSplitPane);
		outerSplitPane.add(menuSplitPane);

		// Navigation Menu
		mainMenuContainer = new ContentPane();
		mainMenuContainer.setStyleName("Menu");
		noScrollbars(mainMenuContainer);
		menuSplitPane.add(mainMenuContainer);
		buildMainMenu();

		// Spacer between the menu and the screen area.
		SplitPane spacerSplitPane = new SplitPane(SplitPane.ORIENTATION_VERTICAL_TOP_BOTTOM, new Extent(SPACER_HEIGHT));
		noScrollbars(spacerSplitPane);
		menuSplitPane.add(spacerSplitPane);
		spacerSplitPane.add(new ContentPane());

		// Screen container.
		noScrollbars(screenContainer);
		spacerSplitPane.add(screenContainer);
	}

	// ========== UTILITY METHODS ==========

	/**
	 * Build a menu from a list of screens and categories. <br>
	 * Recursively calls itself for any categories encountered. <br>
	 * Any categories with no visible screens are not shown. <br>
	 * The menus will have their style name set to baseStyleName + level, to a
	 * maximum of maxLevels. After that point, all sublevels will be set to
	 * maxLevels.
	 * 
	 * @param menu the parent menu to attach items/submenus to.
	 * @param screens the screens and categories to add
	 * @param baseStyleName the base style name.
	 * @param levelIn the level number to append to the style name.
	 * @param maxLevels the maximum level to allow.
	 */
	private int buildMenu(Menu menu, List screens, String baseStyleName, int levelIn, int maxLevels)
	{
		int level = levelIn > maxLevels ? maxLevels : levelIn;

		int numScreens = 0;
		for ( Iterator iter = screens.iterator(); iter.hasNext(); )
		{
			BaseDescription base = (BaseDescription)iter.next();
			if ( base.hasAccess() )
			{
				if ( base instanceof CategoryDescription )
				{
					CategoryDescription desc = (CategoryDescription)base;
					Menu subMenu = mainMenu.addMenu(menu, baseStyleName + level, messages.get(desc.getMessage()), true,
							level > 1);
					if ( buildMenu(subMenu, desc.getChildren(), baseStyleName, level + 1, maxLevels) == 0 )
					{
						menu.remove(subMenu);
					}
					else
					{
						numScreens++;
					}
				}
				else
				{
					ScreenDescription desc = (ScreenDescription)base;
					mainMenu.addItem(menu, baseStyleName + level, messages.get(desc.getMessage()), new ScreenActivator(desc
							.getScreenName()));
					numScreens++;
				}
			}
		}
		return numScreens;
	}

	/**
	 * Build the main menu. <br>
	 * This uses the screen manager to automatically build the list of screens
	 * into a series of menus and submenus.
	 */
	private void buildMainMenu()
	{
		if ( null != mainMenu )
		{
			mainMenuContainer.remove(mainMenu);
		}

		mainMenu = new MainMenu();
		mainMenu.setStyleName("Menu.Level1");
		mainMenuContainer.add(mainMenu);

		List screens = screenManager.getScreens();
		buildMenu(mainMenu, screens, "Menu.Level", 1, 2);
	}

	/**
	 * Stop a component from generating scrollbars if placed in a SplitPane.
	 */
	private void noScrollbars(Component component)
	{
		SplitPaneLayoutData layout = new SplitPaneLayoutData();
		layout.setOverflow(SplitPaneLayoutData.OVERFLOW_HIDDEN);
		component.setLayoutData(layout);
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
