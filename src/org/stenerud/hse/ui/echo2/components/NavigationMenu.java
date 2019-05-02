package org.stenerud.hse.ui.echo2.components;

import echopointng.MenuBar;
import nextapp.echo2.app.Column;
import nextapp.echo2.app.Component;

/**
 * A navigation menu builder. <br>
 * This class maintains a menu bar, ensuring that no more than a specified number of items appear on a row.
 * 
 * @author Karl Stenerud
 */
public class NavigationMenu extends Column
{
	private static final long serialVersionUID = 1L;

	// ========== PRIVATE MEMBERS ==========
	private int maxIndex;
	private String styleName;
	private int currentIndex = 0;
	private MenuBar currentMenuBar;

	// ========== IMPLEMENTATION ==========

	/**
	 * Constructor.
	 * 
	 * @param maxItemsPerLine the maximum number of components to allow on a line.
	 * @param styleName the name of the style to apply to all items.
	 */
	public NavigationMenu(int maxItemsPerLine, String styleName)
	{
		this.maxIndex = maxItemsPerLine - 1;
		this.styleName = styleName;

		setStyleName(styleName);
		currentMenuBar = new MenuBar();
		currentMenuBar.setMenuAlwaysOnTop(true);
		currentMenuBar.setStyleName(styleName);
		super.add(currentMenuBar);
	}

	/**
	 * Add a component to the menu.
	 * 
	 * @param c the component to add.
	 */
	public void add(Component c)
	{
		if ( currentIndex > maxIndex )
		{
			currentMenuBar = new MenuBar();
			currentMenuBar.setMenuAlwaysOnTop(true);
			currentMenuBar.setStyleName(styleName);
			super.add(currentMenuBar);
			currentIndex = 0;
		}
		currentMenuBar.add(c);
		currentIndex++;
	}
}
