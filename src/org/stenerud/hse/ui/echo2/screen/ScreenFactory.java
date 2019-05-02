package org.stenerud.hse.ui.echo2.screen;

public interface ScreenFactory
{

	/**
	 * Create a new screen.
	 * 
	 * @param name the name of the screen. This must be one of the public constants declared in Screens.
	 * @return the new screen.
	 */
	public abstract Screen createScreen(String name);

}
