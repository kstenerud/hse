package org.stenerud.hse.security;

import org.stenerud.hse.data.group.Group;
import org.stenerud.hse.data.group.GroupDAO;
import org.stenerud.hse.data.security.Permission;
import org.stenerud.hse.data.security.SecurityDAO;
import org.stenerud.hse.data.user.User;
import org.stenerud.hse.data.user.UserDAO;

/**
 * A security system that manages logins and permissions.
 * 
 * @author Karl stenerud
 */
public class SecurityImpl implements Security
{
	// ========== INJECTED MEMBERS ==========
	private CurrentUser currentUser;
	private GroupDAO groupDAO;
	private SecurityDAO securityDAO;
	private UserDAO userDAO;

	// ========== IMPLEMENTATION ==========

	public void passwordAuthenticate(String username, String password)
	{
		User user = userDAO.getUser(username, password);
		if ( null == user )
		{
			throw new AuthenticationException("Login incorrect");
		}
		currentUser.set(user);
	}

	public void logout()
	{
		clearUser();
	}

	public void clearUser()
	{
		User user = new User();
		user.setUserGroup(new Group());
		currentUser.set(user);
	}

	public void requirePermission(Permission permission, int minimumLevel)
	{
		if ( !currentUser.get().getUserGroup().hasPermission(permission, minimumLevel) )
		{
			throw new NoPermissionException("Requires permission " + permission + ", level " + minimumLevel);
		}
	}

	public boolean hasPermission(Permission permission, int minimumLevel)
	{
		return currentUser.get().getUserGroup().hasPermission(permission, minimumLevel);
	}

	public boolean isLoggedIn()
	{
		String name = currentUser.get().getName();
		return null != name && name.length() > 0;
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

	public GroupDAO getGroupDAO()
	{
		return groupDAO;
	}

	public void setGroupDAO(GroupDAO groupDAO)
	{
		this.groupDAO = groupDAO;
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
