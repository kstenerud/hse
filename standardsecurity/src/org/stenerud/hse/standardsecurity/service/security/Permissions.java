package org.stenerud.hse.standardsecurity.service.security;

import java.util.Set;
import java.util.TreeSet;

import org.stenerud.hse.standardsecurity.data.security.Permission;
import org.stenerud.hse.standardsecurity.data.security.SecurityDAO;

/**
 * All permissions used by this application.
 * 
 * @author Karl Stenerud
 */
public class Permissions
{
	// ========== CONSTANTS ==========
	public final Permission GROUPS;
	public final Permission USERS;
	public final Permission USERS_ALL;

	// ========== PROPERTIES ==========
	private Set permissions = new TreeSet();

	// ========== IMPLEMENTATION ==========

	/**
	 * Add a permission, creating if necessary.
	 * 
	 * @param securityDAO the security DAO.
	 * @param name the name of the permission.
	 */
	private Permission addPermission(SecurityDAO securityDAO, String name)
	{
		Permission permission = securityDAO.getOrCreatePermission(name);
		permissions.add(permission);
		return permission;
	}

	/**
	 * Constructor.
	 * 
	 * @param securityDAO the security DAO (injected)
	 */
	public Permissions(SecurityDAO securityDAO)
	{
		GROUPS = addPermission(securityDAO, "permission.groups");
		USERS = addPermission(securityDAO, "permission.users");
		USERS_ALL = addPermission(securityDAO, "permission.users.all");
	}

	// ========== GETTERS AND SETTERS ==========

	public Set getPermissions()
	{
		return permissions;
	}
}
