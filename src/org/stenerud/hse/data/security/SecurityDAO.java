package org.stenerud.hse.data.security;

import org.stenerud.hse.data.DataException;

/**
 * Security data access object.
 * 
 * @author Karl Stenerud
 */
public interface SecurityDAO
{
	/**
	 * Get a permission.
	 * 
	 * @param name the permission's name.
	 * @return the permission.
	 * @throws DataException if the permission was not found.
	 */
	public Permission getPermission(String name);

	/**
	 * Get a permission, creating it if it doesn't already exist.
	 * 
	 * @param name the permission's name.
	 * @return the permission.
	 */
	public Permission getOrCreatePermission(String name);

	/**
	 * Refresh a permission with the version in permanent storage.
	 * 
	 * @param permission the permission to refresh.
	 * @return a reference to the permission passed in.
	 */
	public Permission refresh(Permission permission);

	/**
	 * Refresh a permission level with a version from permanent storage.
	 * 
	 * @param permissionLevel the permission level to refresh.
	 * @return a reference to the permission level passed in.
	 */
	public PermissionLevel refresh(PermissionLevel permissionLevel);

	/**
	 * Create a permission level.
	 * 
	 * @param permissionLevel the permission level to create.
	 */
	public void create(PermissionLevel permissionLevel);

	/**
	 * Update a permission level.
	 * 
	 * @param permissionLevel the permission level to update.
	 */
	public void update(PermissionLevel permissionLevel);
}
