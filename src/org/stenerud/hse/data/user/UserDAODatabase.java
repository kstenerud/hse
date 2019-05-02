package org.stenerud.hse.data.user;

import java.util.Iterator;
import java.util.List;

import org.stenerud.hse.data.CriteriaBuffer;
import org.stenerud.hse.data.DataException;
import org.stenerud.hse.data.ExtendedHibernateDaoSupport;
import org.stenerud.hse.data.group.Group;
import org.stenerud.hse.data.group.GroupDAO;
import org.stenerud.hse.data.security.PermissionLevel;

/**
 * Hibernate implementation of the user DAO.
 * 
 * @author Karl Stenerud
 */
public class UserDAODatabase extends ExtendedHibernateDaoSupport implements UserDAO
{
	// ========== CONSTANTS ==========
	private static final String STANDARD_FROM = "FROM User user";
	private static final String STANDARD_ORDER = "ORDER BY user.name";

	// ========== INJECTED MEMBERS ==========
	private GroupDAO groupDAO;

	// ========== IMPLEMENTATION ==========
	public User getAdministrator()
	{
		CriteriaBuffer criteria = new CriteriaBuffer(STANDARD_FROM);
		criteria.addCriteria("user.administrator =", true);

		return (User)getFirst(criteria.getQuery());
	}

	public int getAdministratorId()
	{
		CriteriaBuffer criteria = new CriteriaBuffer("SELECT user.id FROM User user");
		criteria.addCriteria("user.administrator =", true);

		Integer result = (Integer)getFirst(criteria.getQuery());
		return null == result ? -1 : result.intValue();
	}

	public User createAdministrator(String name, String password)
	{
		User administratorUser = new User();
		administratorUser.setName(name);
		administratorUser.setPassword(password);
		administratorUser.setAdministrator(true);
		Group administratorGroup = groupDAO.getAdministrator();
		if ( null == administratorGroup )
		{
			throw new DataException("Cannot create administrator user until administrator group present");
		}
		administratorUser.setUserGroup(administratorGroup);
		create(administratorUser);
		return getAdministrator();
	}

	public User getUser(String username)
	{
		CriteriaBuffer criteria = new CriteriaBuffer(STANDARD_FROM);
		criteria.addCriteria("user.name =", username);

		return forceLoad((User)getFirst(criteria.getQuery()));
	}

	public User getUser(String name, String password)
	{
		CriteriaBuffer criteria = new CriteriaBuffer(STANDARD_FROM);
		criteria.addCriteria("user.name =", name);
		criteria.addCriteria("user.password =", password);

		return forceLoad((User)getFirst(criteria.getQuery()));
	}

	public List getUsers(Group group)
	{
		CriteriaBuffer criteria = new CriteriaBuffer(STANDARD_FROM, STANDARD_ORDER);
		criteria.addCriteria("user.userGroup.id =", group.getId());
		return getHibernateTemplate().find(criteria.getQuery());
	}

	public List getUsers()
	{
		CriteriaBuffer criteria = new CriteriaBuffer(STANDARD_FROM, STANDARD_ORDER);
		return getHibernateTemplate().find(criteria.getQuery());
	}

	public User refresh(User user)
	{
		getHibernateTemplate().refresh(user);
		return user;
	}

	public void create(User user)
	{
		getHibernateTemplate().save(user);
	}

	public void update(User user)
	{
		getHibernateTemplate().update(user);
	}

	public void delete(User user)
	{
		getHibernateTemplate().delete(user);
	}

	// ========== UTILITY METHODS ==========

	/**
	 * Force the lazy loader to get the complete user and referenced objects.
	 * 
	 * @param user the user to force load.
	 * @return the user that was passed in.
	 */
	private User forceLoad(User user)
	{
		if ( null != user )
		{
			Group group = user.getUserGroup();
			if ( null != group )
			{
				for ( Iterator iter = user.getUserGroup().getPermissionLevels().iterator(); iter.hasNext(); )
				{
					PermissionLevel permissionLevel = (PermissionLevel)iter.next();
					permissionLevel.getPermission();
				}
			}
		}
		return user;
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
}
