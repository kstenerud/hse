package org.stenerud.hse.ui.echo2.screen;

/**
 * Screens are "top-level" components that will generally cover most of the browser window. <br>
 * Echo2UserInterface will swap screens in and out of the main browser window as the user selects items from the main
 * menu.
 * 
 * @author Karl Stenerud
 */
public interface Screen
{
	/**
	 * Enter this "screen".
	 */
	public void enter();

	/**
	 * Leave this "screen".
	 * 
	 * @return true if it is ok to leave this screen.
	 */
	public boolean leave();

	/**
	 * Get this screen's title.
	 * 
	 * @return the title.
	 */
	public String getTitle();
}
