package org.stenerud.hse.business.user;

import java.util.List;

import org.stenerud.hse.data.DataException;
import org.stenerud.hse.data.group.Group;
import org.stenerud.hse.data.group.GroupDAO;
import org.stenerud.hse.data.user.User;
import org.stenerud.hse.data.user.UserDAO;
import org.stenerud.hse.security.CurrentUser;
import org.stenerud.hse.security.PermissionLevels;
import org.stenerud.hse.security.Permissions;
import org.stenerud.hse.security.Security;
import org.stenerud.hse.tools.PropertyHelper;

/**
 * Business logic for users.
 * 
 * @author Karl Stenerud
 */
public class UserBusiness
{
	// ========== INJECTED MEMBERS ==========
	private CurrentUser currentUser;
	private GroupDAO groupDAO;
	private Permissions permissions;
	private PropertyHelper properties;
	private Security security;
	private UserDAO userDAO;

	// ========== IMPLEMENTATION ==========

	/**
	 * Check if the current user can view users.
	 * 
	 * @return true if the current user has permission.
	 */
	public boolean canView()
	{
		return hasReadPermission();
	}

	/**
	 * Check if the current user can list users in his own group.
	 * 
	 * @return true if the current user has permission.
	 */
	public boolean canListOwnGroupsUsers()
	{
		return hasReadPermission();
	}

	/**
	 * Check if the current user can list users in any group.
	 * 
	 * @return true if the current user has permission.
	 */
	public boolean canListAllUsers()
	{
		return hasReadAllPermission();
	}

	/**
	 * Check if the current user can create users in his own group.
	 * 
	 * @return true if the current user has permission.
	 */
	public boolean canCreateInOwnGroup()
	{
		return hasWritePermission();
	}

	/**
	 * Check if the current user can create users in any group.
	 * 
	 * @return true if the current user has permission.
	 */
	public boolean canCreateInAnyGroup()
	{
		return hasWriteAllPermission();
	}

	/**
	 * Check if the current user can view a user.
	 * 
	 * @param user the user to view.
	 * @return true if the current user has permission.
	 */
	public boolean canView(User user)
	{
		return hasReadPermission(user);
	}

	/**
	 * Check if the current user can update a user.
	 * 
	 * @param user the user to update.
	 * @return true if the current user has permission.
	 */
	public boolean canUpdate(User user)
	{
		return hasWritePermission(user)
				&& (!user.isAdministrator() || properties.getBoolean("administrator.user.allowUpdate", true));
	}

	/**
	 * Check if the current user can change a user's group.
	 * 
	 * @param user the user to update.
	 * @return true if the current user has permission.
	 */
	public boolean canChangeGroup(User user)
	{
		return hasWritePermission(user) && !user.isAdministrator();
	}

	/**
	 * Check if the current user can delete a user.
	 * 
	 * @param user the user to delete.
	 * @return true if the current user has permission.
	 */
	public boolean canDelete(User user)
	{
		return hasWritePermission(user) && !user.isAdministrator();
	}

	/**
	 * Check if a user exists.
	 * 
	 * @param username the username.
	 * @return true if the user exists.
	 */
	public boolean exists(String username)
	{
		checkReadPermissions();

		return userDAO.getUser(username) != null;
	}

	/**
	 * Get a list of users in a group. Users are returned in alphabetical order.
	 * 
	 * @param group the group to match, or null to ignore.
	 * @return the matching users.
	 */
	public List getUsers(Group group)
	{
		if ( null == group )
		{
			return getUsers();
		}

		checkReadPermissions();

		return userDAO.getUsers(group);
	}

	/**
	 * Get a list of all users. Users are returned in alphabetical order.
	 * 
	 * @return a list of all users.
	 */
	public List getUsers()
	{
		checkReadAllPermissions();

		return userDAO.getUsers();
	}

	/**
	 * Refresh a user with a version from permanent storage.
	 * 
	 * @param user the user to refresh.
	 * @return a reference to the user passed in.
	 */
	public User refresh(User user)
	{
		checkReadPermissions(user);

		return userDAO.refresh(user);
	}

	/**
	 * Create a user.
	 * 
	 * @param user the user to create.
	 */
	public void create(User user)
	{
		checkWritePermissions(user);
		disallowMultipleAdminFlags(user);

		userDAO.create(user);
	}

	/**
	 * Update a user.
	 * 
	 * @param user the user to update.
	 */
	public void update(User user)
	{
		checkWritePermissions(user);
		disallowMultipleAdminFlags(user);
		if ( user.isAdministrator() && groupDAO.getAdministratorId() != user.getUserGroup().getId() )
		{
			throw new DataException("Cannot change the administrator's group");
		}

		userDAO.update(user);
	}

	/**
	 * Delete a user.
	 * 
	 * @param user the user to delete.
	 */
	public void delete(User user)
	{
		checkWritePermissions(user);
		if ( user.isAdministrator() )
		{
			throw new DataException("Cannot delete administrator user");
		}

		userDAO.delete(user);
	}

	/**
	 * Get all groups. <br>
	 * This is only called in order to set a group outside of the current user's group, so we check for WRITE ALL.
	 * 
	 * @return a list of all groups.
	 */
	public List getGroups()
	{
		checkReadAllPermissions();

		return groupDAO.getGroups();
	}

	// ========== UTILITY METHODS ==========

	/**
	 * Check if the current user has read permission for users in his group.
	 * 
	 * @return true if the current user has permission.
	 */
	private boolean hasReadPermission()
	{
		return security.hasPermission(permissions.USERS, PermissionLevels.READ);
	}

	/**
	 * Check if the current user has read permission for all users.
	 * 
	 * @return true if the current user has permission.
	 */
	private boolean hasReadAllPermission()
	{
		return security.hasPermission(permissions.USERS_ALL, PermissionLevels.READ);
	}

	/**
	 * Check if the current user has write permission for users in his group.
	 * 
	 * @return true if the current user has permission.
	 */
	private boolean hasWritePermission()
	{
		return security.hasPermission(permissions.USERS, PermissionLevels.WRITE);
	}

	/**
	 * Check if the current user has write permission for all users.
	 * 
	 * @return true if the current user has permission.
	 */
	private boolean hasWriteAllPermission()
	{
		return security.hasPermission(permissions.USERS_ALL, PermissionLevels.WRITE);
	}

	/**
	 * Check if the current user has read permission for the specified user.
	 * 
	 * @param user the user to check.
	 * @return true if the current user has permission.
	 */
	private boolean hasReadPermission(User user)
	{
		if ( !hasReadPermission() )
		{
			return false;
		}
		if ( user.getUserGroup().getId() != currentUser.get().getUserGroup().getId() )
		{
			return hasReadAllPermission();
		}
		return true;
	}

	/**
	 * Check if the current user has write permission for the specified user.
	 * 
	 * @param user the user to check.
	 * @return true if the current user has permission.
	 */
	private boolean hasWritePermission(User user)
	{
		if ( !hasWritePermission() )
		{
			return false;
		}
		if ( user.getUserGroup().getId() != currentUser.get().getUserGroup().getId() )
		{
			return hasWriteAllPermission();
		}
		return true;
	}

	/**
	 * Make sure the current user has read permission for users in his group.
	 */
	private void checkReadPermissions()
	{
		security.requirePermission(permissions.USERS, PermissionLevels.READ);
	}

	/**
	 * Make sure the current user has read permission for all users.
	 */
	private void checkReadAllPermissions()
	{
		security.requirePermission(permissions.USERS_ALL, PermissionLevels.READ);
	}

	/**
	 * Make sure the current user has read permission for the specified user.
	 * 
	 * @param user the user to check
	 */
	private void checkReadPermissions(User user)
	{
		checkReadPermissions();
		if ( user.getUserGroup().getId() != currentUser.get().getUserGroup().getId() )
		{
			checkReadAllPermissions();
		}
	}

	/**
	 * Make sure the current user has write permission for users in his group.
	 */
	private void checkWritePermissions()
	{
		security.requirePermission(permissions.USERS, PermissionLevels.WRITE);
	}

	/**
	 * Make sure the current user has write permission for all users.
	 */
	private void checkWriteAllPermissions()
	{
		security.requirePermission(permissions.USERS_ALL, PermissionLevels.WRITE);
	}

	/**
	 * Make sure the current user has write permission for the specified user.
	 * 
	 * @param user the user to check
	 */
	private void checkWritePermissions(User user)
	{
		checkWritePermissions();
		if ( user.getUserGroup().getId() != currentUser.get().getUserGroup().getId() )
		{
			checkWriteAllPermissions();
		}
	}

	/**
	 * Make sure the administrator flag hasn't been tampered with.
	 * 
	 * @param user the user to check
	 */
	private void disallowMultipleAdminFlags(User user)
	{
		int administratorId = userDAO.getAdministratorId();

		if ( user.isAdministrator() && user.getId() != administratorId )
		{
			throw new DataException("Only ApplicationInitializer may set administrator flag");
		}

		if ( !user.isAdministrator() && user.getId() == administratorId )
		{
			throw new DataException("Cannot remove administrator flag");
		}
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

	public Permissions getPermissions()
	{
		return permissions;
	}

	public void setPermissions(Permissions permissions)
	{
		this.permissions = permissions;
	}

	public PropertyHelper getProperties()
	{
		return properties;
	}

	public void setProperties(PropertyHelper properties)
	{
		this.properties = properties;
	}

	public Security getSecurity()
	{
		return security;
	}

	public void setSecurity(Security security)
	{
		this.security = security;
	}

	public UserDAO getUserDAO()
	{
		return userDAO;
	}

	public void setUserDAO(UserDAO userDAO)
	{
		this.userDAO = userDAO;
	}
}
