package org.stenerud.hse.base.ui.echo2.tool;

import nextapp.echo2.app.Table;
import nextapp.echo2.app.button.AbstractButton;
import nextapp.echo2.app.event.ActionEvent;
import nextapp.echo2.app.event.ActionListener;
import nextapp.echo2.app.list.AbstractListComponent;
import nextapp.echo2.app.text.TextComponent;

/**
 * Helper class for action listeners.
 * 
 * @author Karl Stenerud
 */
public class ActionHelper
{
	// ========== IMPLEMENTATION ==========

	/**
	 * Adds a blank action listener to a component so that its default behavior
	 * will not be invoked.
	 * 
	 * @param component the component to set a blank action listener for
	 */
	public static void clearAction(TextComponent component)
	{
		component.addActionListener(makeBlankListener());
	}

	/**
	 * Adds a blank action listener to a component so that its default behavior
	 * will not be invoked.
	 * 
	 * @param component the component to set a blank action listener for
	 */
	public static void clearAction(AbstractButton component)
	{
		component.addActionListener(makeBlankListener());
	}

	/**
	 * Adds a blank action listener to a component so that its default behavior
	 * will not be invoked.
	 * 
	 * @param component the component to set a blank action listener for
	 */
	public static void clearAction(AbstractListComponent component)
	{
		component.addActionListener(makeBlankListener());
	}

	/**
	 * Adds a blank action listener to a component so that its default behavior
	 * will not be invoked.
	 * 
	 * @param component the component to set a blank action listener for
	 */
	public static void clearAction(Table component)
	{
		component.addActionListener(makeBlankListener());
	}

	// ========== UTILITY METHODS ==========

	private static ActionListener makeBlankListener()
	{
		return new ActionListener()
		{
			private static final long serialVersionUID = 1L;

			public void actionPerformed(ActionEvent e)
			{
				// Blank action.
			}
		};
	}
}
