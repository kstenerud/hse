package org.stenerud.hse.standardsecurity.data.group;

import java.util.List;

/**
 * Group data access object.
 * 
 * @author Karl Stenerud
 */
public interface GroupDAO
{
	/**
	 * Get the administrator group.
	 * 
	 * @return the administrator group, or null if none found.
	 */
	public Group getAdministrator();

	/**
	 * Get the administrator group's id.
	 * 
	 * @return the administrator group's id, or -1 if not found.
	 */
	public int getAdministratorId();

	/**
	 * Create an administrator group.
	 * 
	 * @param name the name of the group.
	 * @return the new group.
	 */
	public Group createAdministrator(String name);

	/**
	 * Get a group by name.
	 * 
	 * @param name the group name.
	 * @return the group, or null if not found.
	 */
	public Group getGroup(String name);

	/**
	 * Get all groups. Administrator is returned first, followed by the rest in
	 * alphabetical order.
	 * 
	 * @return a list of all groups.
	 */
	public List getGroups();

	/**
	 * Check if a group has users associated with it.
	 * 
	 * @param group the group.
	 * @return true if there are associated users.
	 */
	public boolean hasUsers(Group group);

	/**
	 * Refresh a group with a version from permanent storage.
	 * 
	 * @param group the group to refresh.
	 * @return a reference to the group passed in.
	 */
	public Group refresh(Group group);

	/**
	 * Create a group.
	 * 
	 * @param group the group to create.
	 */
	public void create(Group group);

	/**
	 * Update a group.
	 * 
	 * @param group the group to update.
	 */
	public void update(Group group);

	/**
	 * Delete a group.
	 * 
	 * @param group the group to delete.
	 */
	public void delete(Group group);
}
