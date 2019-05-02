package org.stenerud.hse.ui.echo2;

import nextapp.echo2.app.event.ActionListener;
import echopointng.Menu;
import echopointng.MenuBar;
import echopointng.MenuItem;

/**
 * The application's main menu bar.
 * 
 * @author Karl Stenerud
 */
public class MainMenu extends MenuBar
{
	private static final long serialVersionUID = 1L;

	// ========== CONSTANTS ==========
	public static final String STYLE_MENUBAR = "MenuBar";
	public static final String STYLE_MENU = "Menu";

	{
		setStyleName(STYLE_MENUBAR);
	}

	/**
	 * Add a menu.
	 * 
	 * @param parent the parent to add the menu to. Null = this MainMenu.
	 * @param style the style to apply.
	 * @param name the name to display.
	 * @return the new menu.
	 */
	public Menu addMenu(Menu parent, String style, String name)
	{
		Menu menu = new Menu(name);
		menu.setStyleName(style);
		menu.setVisible(false);

		menu.setMenuAlwaysOnTop(true);
		menu.setClickToOpen(true);

		if ( STYLE_MENUBAR.equals(style) )
		{
			menu.setSubmenuImage(Theme.getImage(Images.EMPTY));
		}

		if ( null == parent )
		{
			parent = this;
		}
		parent.add(menu);
		parent.setVisible(true);

		return menu;
	}

	/**
	 * Add a menu item.
	 * 
	 * @param parent the parent to add the menu to. Null = this MainMenu.
	 * @param style the style to apply.
	 * @param name the name to display.
	 * @return the new menu item
	 */
	public MenuItem addItem(Menu parent, String style, String name)
	{
		MenuItem item = new MenuItem(name);
		item.setStyleName(style);

		if ( null == parent )
		{
			parent = this;
		}
		parent.add(item);
		parent.setVisible(true);

		return item;
	}

	/**
	 * Add a menu item.
	 * 
	 * @param parent the parent to add the menu to. Null = this MainMenu.
	 * @param style the style to apply.
	 * @param name the name to display.
	 * @param listener the action listenet to notify when this item is clicked.
	 * @return the new menu item
	 */
	public MenuItem addItem(Menu parent, String style, String name, ActionListener listener)
	{
		MenuItem item = addItem(parent, style, name);
		item.addActionListener(listener);
		return item;
	}
}
