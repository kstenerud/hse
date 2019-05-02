package org.stenerud.hse.standardsecurity.ui.echo2;

/**
 * Interface to allow an object to be notified when the current user changes.
 * 
 * @author Karl Stenerud
 */
public interface UserChangeAware
{
	/**
	 * Receive notification that the current user has changed.
	 */
	public void notifyUserChanged();
}
