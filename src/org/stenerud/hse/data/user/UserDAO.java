package org.stenerud.hse.data.user;

import java.util.List;

import org.stenerud.hse.data.group.Group;

/**
 * Data access for users.
 * 
 * @author Karl Stenerud
 */
public interface UserDAO
{
	/**
	 * Get the administrator user.
	 * 
	 * @return the administrator user, or null if none found.
	 */
	public User getAdministrator();

	/**
	 * Get the administrator user's id.
	 * 
	 * @return the administrator user's id, or -1 if not found.
	 */
	public int getAdministratorId();

	/**
	 * Create an administrator user.
	 * 
	 * @param name the name of the user.
	 * @param password the user's password.
	 * @return the administrator user.
	 */
	public User createAdministrator(String name, String password);

	/**
	 * Get a user by name. <br>
	 * This method also ensures all group and permission information is present.
	 * 
	 * @param username the name of the user.
	 * @return the user or null if not found.
	 */
	public User getUser(String username);

	/**
	 * Get a user by name and password. <br>
	 * This method also ensures all group and permission information is present.
	 * 
	 * @param name the name of the user.
	 * @param password the user's password.
	 * @return the user or null if not found.
	 */
	public User getUser(String name, String password);

	/**
	 * Get a list of users in the specified group. Users are returned in alphabetical order.
	 * 
	 * @param group the group to match, or null to ignore.
	 * @return the matching users.
	 */
	public List getUsers(Group group);

	/**
	 * Get all users. Users are returned in alphabetical order.
	 * 
	 * @return a list of all users.
	 */
	public List getUsers();

	/**
	 * Refresh a user with a version from permanent storage.
	 * 
	 * @param user the user to refresh.
	 * @return a reference to the user passed in.
	 */
	public User refresh(User user);

	/**
	 * Create a new user
	 * 
	 * @param user the user to create.
	 */
	public void create(User user);

	/**
	 * Update a user.
	 * 
	 * @param user the user to update.
	 */
	public void update(User user);

	/**
	 * Delete a user.
	 * 
	 * @param user the user to delete.
	 */
	public void delete(User user);
}
