package org.stenerud.hse.security;

import org.stenerud.hse.data.security.Permission;

/**
 * Security system for the application. <br>
 * This class handles user authentication, permissions, and logout.
 * 
 * @author Karl Stenerud
 */
public interface Security
{
	/**
	 * Authenticate using a username and password.
	 * 
	 * @param username the username
	 * @param password the password
	 */
	public void passwordAuthenticate(String username, String password);

	/**
	 * Logout of the system.
	 */
	public void logout();

	/**
	 * Clear the current user. <br>
	 * This is used internally and should not be called by the user.
	 */
	public void clearUser();

	/**
	 * Require a certain permission level from a user.
	 * 
	 * @param permission the permission to require.
	 * @param minimumLevel the permission level to require.
	 * @throws NoPermissionException if the current user does not have the required permission and level.
	 */
	public void requirePermission(Permission permission, int minimumLevel);

	/**
	 * Check if a user has the specfied permission and level.
	 * 
	 * @param permission the permission to check.
	 * @param minimumLevel the permission level to check.
	 * @return true if the user has the permission and level.
	 */
	public boolean hasPermission(Permission permission, int minimumLevel);

	/**
	 * Check if a user is logged in on the current session.
	 * 
	 * @return true if a user is logged in.
	 */
	public boolean isLoggedIn();

}
