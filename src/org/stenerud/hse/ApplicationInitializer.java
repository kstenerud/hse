package org.stenerud.hse;

import org.stenerud.hse.data.group.GroupDAO;
import org.stenerud.hse.data.security.SecurityDAO;
import org.stenerud.hse.data.user.UserDAO;
import org.stenerud.hse.tools.PropertyHelper;

/**
 * Initializes the application. <br>
 * Ensures that the minimum required data is in the database.
 * 
 * @author Karl Stenerud
 */
public class ApplicationInitializer
{
	// ========== CONSTANTS ==========
	private static final String DEFAULT_ADMIN_GROUP = "Administration";
	private static final String DEFAULT_ADMIN_USERNAME = "admin";
	private static final String DEFAULT_ADMIN_PASSWORD = "admin";

	// ========== INJECTED MEMBERS ==========
	private GroupDAO groupDAO;
	private PropertyHelper properties;
	private SecurityDAO securityDAO;
	private UserDAO userDAO;

	// ========== PRIVATE MEMBERS ==========
	private boolean initialized = false;

	// ========== IMPLEMENTATION ==========

	/**
	 * Initialize the application.
	 */
	public void initialize()
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

	public SecurityDAO getSecurityDAO()
	{
		return securityDAO;
	}

	public void setSecurityDAO(SecurityDAO securityDAO)
	{
		this.securityDAO = securityDAO;
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
