package org.stenerud.hse.standardsecurity.service.security;

import org.stenerud.hse.standardsecurity.data.security.Permission;

/**
 * Security system for the application. <br>
 * This class handles user authentication, permissions, and logout.
 * 
 * @author Karl Stenerud
 */
public interface Security
{
	/**
	 * Log into the system.
	 * 
	 * @param credentials the credentials presented by the user who is logging
	 *           in.
	 * @throws AuthenticationException if the credentials were invalid.
	 */
	public void login(Credentials credentials);

	/**
	 * Log out of the system.
	 */
	public void logout();

	/**
	 * Require a certain permission level from a user.
	 * 
	 * @param permission the permission to require.
	 * @param minimumLevel the permission level to require.
	 * @throws NoPermissionException if the current user does not have the
	 *            required permission and level.
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
