package org.stenerud.hse.standardsecurity.service.security;

import org.stenerud.hse.standardsecurity.data.security.Permission;
import org.stenerud.hse.standardsecurity.data.security.SecurityDAO;
import org.stenerud.hse.standardsecurity.data.user.User;
import org.stenerud.hse.standardsecurity.data.user.UserDAO;

/**
 * Group/User security, using username/password for logins.
 * 
 * @author Karl stenerud
 */
public class BasicPasswordSecurity implements Security
{
	// ========== INJECTED MEMBERS ==========
	private CurrentUser currentUser;
	private SecurityDAO securityDAO;
	private UserDAO userDAO;

	// ========== IMPLEMENTATION ==========

	public void login(Credentials credentials)
	{
		if ( credentials instanceof PasswordCredentials )
		{
			PasswordCredentials realCredentials = (PasswordCredentials)credentials;

			User user = userDAO.getUser(realCredentials.getUsername(), realCredentials.getPassword());
			if ( null == user )
			{
				throw new AuthenticationException("Login incorrect");
			}
			currentUser.set(user);
		}
		else
		{
			throw new IllegalArgumentException(this.getClass().getName() + " does not accept "
					+ credentials.getClass().getName());
		}
	}

	public void logout()
	{
		currentUser.set(null);
	}

	public void requirePermission(Permission permission, int minimumLevel)
	{
		User user = currentUser.get();
		if ( null == user )
		{
			throw new NoPermissionException("User is not logged in");
		}

		if ( !user.getUserGroup().hasPermission(permission, minimumLevel) )
		{
			throw new NoPermissionException("Requires permission " + permission + ", level " + minimumLevel);
		}
	}

	public boolean hasPermission(Permission permission, int minimumLevel)
	{
		User user = currentUser.get();

		return null != user && user.getUserGroup().hasPermission(permission, minimumLevel);
	}

	public boolean isLoggedIn()
	{
		return null != currentUser.get();
	}

	// ========== GETTERS AND SETTERS ==========

	public CurrentUser getCurrentUser()
	{
		return currentUser;
	}

	public void setCurrentUser(CurrentUser currentUser)
	{
		this.currentUser = currentUser;
	}

	public UserDAO getUserDAO()
	{
		return userDAO;
	}

	public void setUserDAO(UserDAO userDAO)
	{
		this.userDAO = userDAO;
	}

	public SecurityDAO getSecurityDAO()
	{
		return securityDAO;
	}

	public void setSecurityDAO(SecurityDAO securityDAO)
	{
		this.securityDAO = securityDAO;
	}
}
