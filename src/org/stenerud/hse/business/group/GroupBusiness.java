package org.stenerud.hse.business.group;

import java.util.List;

import org.stenerud.hse.data.DataException;
import org.stenerud.hse.data.group.Group;
import org.stenerud.hse.data.group.GroupDAO;
import org.stenerud.hse.data.security.SecurityDAO;
import org.stenerud.hse.security.PermissionLevels;
import org.stenerud.hse.security.Permissions;
import org.stenerud.hse.security.Security;
import org.stenerud.hse.tools.PropertyHelper;

/**
 * Business logic for managing groups.
 * 
 * @author Karl Stenerud
 */
public class GroupBusiness
{
	// ========== INJECTED MEMBERS ==========
	private GroupDAO groupDAO;
	private Permissions permissions;
	private PropertyHelper properties;
	private Security security;
	private SecurityDAO securityDAO;

	// ========== IMPLEMENTATION ==========

	/**
	 * Check if the current user can create groups.
	 * 
	 * @return true if the current user has permission.
	 */
	public boolean canCreate()
	{
		return hasWritePermission();
	}

	/**
	 * Check if the current user can view groups.
	 * 
	 * @return true if the current user has permission.
	 */
	public boolean canView()
	{
		return hasReadPermission();
	}

	/**
	 * Check if the current user can update groups.
	 * 
	 * @return true if the current user has permission.
	 */
	public boolean canUpdate()
	{
		return hasWritePermission();
	}

	/**
	 * Check if the current user can update a group.
	 * 
	 * @param group the group
	 * @return true if the current user has permission.
	 */
	public boolean canUpdate(Group group)
	{
		return hasWritePermission()
				&& (!group.isAdministrator() || properties.getBoolean("administrator.group.allowUpdate", true));
	}

	/**
	 * Check if the current user can delete a group.
	 * 
	 * @param group the group
	 * @return true if the current user has permission.
	 */
	public boolean canDelete(Group group)
	{
		return hasWritePermission() && !group.isAdministrator() && !groupDAO.hasUsers(group);
	}

	/**
	 * Check if a group exists.
	 * 
	 * @param name the group name.
	 * @return true if the group exists.
	 */
	public boolean exists(String name)
	{
		checkReadPermissions();

		return groupDAO.getGroup(name) != null;
	}

	/**
	 * Get all groups. Administrator is returned first, followed by the rest in alphabetical order.
	 * 
	 * @return a list of all groups.
	 */
	public List getGroups()
	{
		checkReadPermissions();

		return groupDAO.getGroups();
	}

	/**
	 * Refresh a group with a version from permanent storage.
	 * 
	 * @param group the group to refresh.
	 * @return a reference to the group passed in.
	 */
	public Group refresh(Group group)
	{
		checkReadPermissions();

		return groupDAO.refresh(group);
	}

	/**
	 * Create a new group.
	 * 
	 * @param group the group to create.
	 */
	public void create(Group group)
	{
		checkWritePermissions();

		disallowMultipleAdminFlags(group);

		groupDAO.create(group);
	}

	/**
	 * Update a group.
	 * 
	 * @param group the group to update.
	 */
	public void update(Group group)
	{
		checkWritePermissions();

		disallowMultipleAdminFlags(group);

		// Minimum administrator permissions.
		if ( group.isAdministrator() )
		{
			if ( !group.hasPermission(permissions.GROUPS, PermissionLevels.WRITE) )
			{
				group.setPermissionLevel(permissions.GROUPS, PermissionLevels.WRITE);
			}
		}

		groupDAO.update(group);
	}

	/**
	 * Delete a group.
	 * 
	 * @param group the group to delete.
	 */
	public void delete(Group group)
	{
		checkWritePermissions();

		if ( group.isAdministrator() )
		{
			throw new DataException("Cannot delete administrator group");
		}

		groupDAO.delete(group);
	}

	// ========== UTILITY METHODS ==========

	/**
	 * Check if the current user has read permission.
	 * 
	 * @return true if the user has permission.
	 */
	private boolean hasReadPermission()
	{
		return security.hasPermission(permissions.GROUPS, PermissionLevels.READ);
	}

	/**
	 * Check if the current user has write permission.
	 * 
	 * @return true if the user has permission.
	 */
	private boolean hasWritePermission()
	{
		return security.hasPermission(permissions.GROUPS, PermissionLevels.WRITE);
	}

	/**
	 * Make sure the administrator flag hasn't been tampered with.
	 * 
	 * @param group the group to check
	 */
	private void disallowMultipleAdminFlags(Group group)
	{
		int administratorId = groupDAO.getAdministratorId();

		if ( group.isAdministrator() && group.getId() != administratorId )
		{
			throw new DataException("Only ApplicationInitializer may set administrator flag");
		}

		if ( !group.isAdministrator() && group.getId() == administratorId )
		{
			throw new DataException("Cannot remove administrator flag");
		}
	}

	/**
	 * Make sure the current user has read permission.
	 */
	private void checkReadPermissions()
	{
		security.requirePermission(permissions.GROUPS, PermissionLevels.READ);
	}

	/**
	 * Make sure the current user has write permission.
	 */
	private void checkWritePermissions()
	{
		security.requirePermission(permissions.GROUPS, PermissionLevels.WRITE);
	}

	// ========== GETTERS AND SETTERS ==========

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

	public SecurityDAO getSecurityDAO()
	{
		return securityDAO;
	}

	public void setSecurityDAO(SecurityDAO securityDAO)
	{
		this.securityDAO = securityDAO;
	}
}
