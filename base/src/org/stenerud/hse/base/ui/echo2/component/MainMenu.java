package org.stenerud.hse.base.ui.echo2.component;

import org.stenerud.hse.base.ui.echo2.Images;
import org.stenerud.hse.base.ui.echo2.Theme;

import nextapp.echo2.app.event.ActionListener;
import echopointng.Menu;
import echopointng.MenuBar;
import echopointng.MenuItem;

/**
 * A "Main Menu" bar.
 * 
 * @author Karl Stenerud
 */
public class MainMenu extends MenuBar
{
	private static final long serialVersionUID = 1L;

	/**
	 * Add a menu.
	 * 
	 * @param parentIn the parent to add the menu to. Null = this MainMenu.
	 * @param style the style to apply.
	 * @param name the name to display.
	 * @param clickToOpen if true, the menu must be clicked to open.
	 * @param showSubMenuImage If true, show the submenu image beside the name.
	 * @return the new menu.
	 */
	public Menu addMenu(Menu parentIn, String style, String name, boolean clickToOpen, boolean showSubMenuImage)
	{
		Menu menu = new Menu(name);
		menu.setStyleName(style);
		menu.setVisible(false);

		menu.setMenuAlwaysOnTop(true);
		menu.setClickToOpen(clickToOpen);
		if ( !showSubMenuImage )
		{
			menu.setSubmenuImage(Theme.getImage(Images.EMPTY));
		}

		Menu parent = null == parentIn ? this : parentIn;
		parent.add(menu);
		parent.setVisible(true);

		return menu;
	}

	/**
	 * Add a menu item.
	 * 
	 * @param parentIn the parent to add the menu to. Null = this MainMenu.
	 * @param style the style to apply.
	 * @param name the name to display.
	 * @return the new menu item
	 */
	public MenuItem addItem(Menu parentIn, String style, String name)
	{
		MenuItem item = new MenuItem(name);
		item.setStyleName(style);

		Menu parent = null == parentIn ? this : parentIn;
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
