package org.stenerud.hse.standardsecurity.service.security;

import org.stenerud.hse.standardsecurity.data.user.User;

/**
 * Gets and sets the user currently logged into the system.
 * 
 * @author Karl Stenerud
 */
public interface CurrentUser
{
	/**
	 * Get the currently logged in user.
	 * 
	 * @return the currently logged in user.
	 */
	public User get();

	/**
	 * Set the currently logged in user.
	 * 
	 * @param user the currently logged in user.
	 */
	public void set(User user);
}
