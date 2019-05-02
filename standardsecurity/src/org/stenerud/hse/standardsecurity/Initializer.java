package org.stenerud.hse.standardsecurity;

import org.stenerud.hse.base.tool.PropertyHelper;
import org.stenerud.hse.standardsecurity.data.group.GroupDAO;
import org.stenerud.hse.standardsecurity.data.user.UserDAO;

/**
 * Initializes the administration group and user.
 * 
 * @author Karl Stenerud
 */
public class Initializer
{
	// ========== CONSTANTS ==========
	private static final String DEFAULT_ADMIN_GROUP = "Administration";
	private static final String DEFAULT_ADMIN_USERNAME = "admin";
	private static final String DEFAULT_ADMIN_PASSWORD = "adminchangeme";

	// ========== INJECTED MEMBERS ==========
	private GroupDAO groupDAO;
	private PropertyHelper properties;
	private UserDAO userDAO;

	// ========== PRIVATE MEMBERS ==========
	private boolean initialized = false;

	// ========== IMPLEMENTATION ==========

	/**
	 * Initialize the administration group and user.
	 */
	public void init()
	{
		if ( !initialized )
		{
			// Make sure administration user and group exist.
			if ( null == groupDAO.getAdministrator() )
			{
				groupDAO.createAdministrator(DEFAULT_ADMIN_GROUP);
			}

			if ( null == userDAO.getAdministrator() )
			{
				userDAO.createAdministrator(properties.getString("administrator.defaultName", DEFAULT_ADMIN_USERNAME),
						properties.getString("administrator.defaultPassword", DEFAULT_ADMIN_PASSWORD));
			}
		}
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

	public PropertyHelper getProperties()
	{
		return properties;
	}

	public void setProperties(PropertyHelper properties)
	{
		this.properties = properties;
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
