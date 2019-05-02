package org.stenerud.hse.ui.echo2.screen.main;

import org.stenerud.hse.ui.echo2.screen.PaneScreen;

import nextapp.echo2.app.Label;

/**
 * The main screen of the application.
 * 
 * @author Karl Stenerud
 */
public class MainScreen extends PaneScreen
{
	private static final long serialVersionUID = 1L;

	// ========== IMPLEMENTATION ==========

	public String getTitle()
	{
		return messages.get("screen.main");
	}

	protected void initComponents()
	{
		add(new Label(messages.get("prompt.main")));
	}

	protected void resetComponents()
	{
		// Nothing to do.
	}
}
