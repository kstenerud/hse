package org.stenerud.hse.standardsecurity.data.group;

import java.util.Iterator;
import java.util.List;

import org.stenerud.hse.base.data.CriteriaBuffer;
import org.stenerud.hse.base.data.ExtendedHibernateDaoSupport;
import org.stenerud.hse.standardsecurity.data.security.Permission;
import org.stenerud.hse.standardsecurity.data.security.PermissionLevel;
import org.stenerud.hse.standardsecurity.data.security.SecurityDAO;
import org.stenerud.hse.standardsecurity.service.security.PermissionLevels;
import org.stenerud.hse.standardsecurity.service.security.Permissions;

/**
 * Database implementation of the group DAO.
 * 
 * @author Karl Stenerud
 */
public class GroupDAODatabase extends ExtendedHibernateDaoSupport implements GroupDAO
{
	// ========== CONSTANTS ==========
	private static final String STANDARD_FROM = "FROM Group group";

	// ========== INJECTED MEMBERS ==========
	private Permissions permissions;
	private SecurityDAO securityDAO;

	// ========== IMPLEMENTATION ==========
	public Group getAdministrator()
	{
		try
		{
			CriteriaBuffer criteria = new CriteriaBuffer(STANDARD_FROM);
			criteria.addCriteria("group.administrator =", true);

			return (Group)this.getFirst(criteria.getQuery());
		}
		catch ( RuntimeException ex )
		{
			checkForDisconnect(ex);
			throw ex;
		}
	}

	public int getAdministratorId()
	{
		try
		{
			CriteriaBuffer criteria = new CriteriaBuffer("SELECT group.id FROM Group group");
			criteria.addCriteria("group.administrator =", true);

			Integer result = (Integer)this.getFirst(criteria.getQuery());
			return null == result ? -1 : result.intValue();
		}
		catch ( RuntimeException ex )
		{
			checkForDisconnect(ex);
			throw ex;
		}
	}

	public Group createAdministrator(String name)
	{
		try
		{
			Group administratorGroup = new Group();
			administratorGroup.setName(name);
			administratorGroup.setAdministrator(true);

			// Add all permissions to the administrator.
			for ( Iterator iter = permissions.getPermissions().iterator(); iter.hasNext(); )
			{
				PermissionLevel permissionLevel = new PermissionLevel();
				permissionLevel.setPermission((Permission)iter.next());
				permissionLevel.setLevel(PermissionLevels.WRITE);
				securityDAO.create(permissionLevel);
				administratorGroup.addPermissionLevel(permissionLevel);
			}
			create(administratorGroup);
			return getAdministrator();
		}
		catch ( RuntimeException ex )
		{
			checkForDisconnect(ex);
			throw ex;
		}
	}

	public Group getGroup(String name)
	{
		try
		{
			CriteriaBuffer criteria = new CriteriaBuffer(STANDARD_FROM);
			criteria.addCriteria("group.name =", name);

			return (Group)getFirst(criteria.getQuery());
		}
		catch ( RuntimeException ex )
		{
			checkForDisconnect(ex);
			throw ex;
		}
	}

	public List getGroups()
	{
		try
		{
			CriteriaBuffer criteria = new CriteriaBuffer(STANDARD_FROM, "ORDER BY group.administrator DESC, group.name");

			return getHibernateTemplate().find(criteria.getQuery());
		}
		catch ( RuntimeException ex )
		{
			checkForDisconnect(ex);
			throw ex;
		}
	}

	public boolean hasUsers(Group group)
	{
		try
		{
			CriteriaBuffer criteria = new CriteriaBuffer("SELECT count(*) from User user", null);
			criteria.addCriteria("user.userGroup =", group.getId());
			return ((Integer)this.getFirst(criteria.getQuery())).intValue() > 0;
		}
		catch ( RuntimeException ex )
		{
			checkForDisconnect(ex);
			throw ex;
		}
	}

	public Group refresh(Group group)
	{
		try
		{
			getHibernateTemplate().refresh(group);
			return group;
		}
		catch ( RuntimeException ex )
		{
			checkForDisconnect(ex);
			throw ex;
		}
	}

	public void create(Group group)
	{
		try
		{
			getHibernateTemplate().save(group);
		}
		catch ( RuntimeException ex )
		{
			checkForDisconnect(ex);
			throw ex;
		}
	}

	public void update(Group group)
	{
		try
		{
			getHibernateTemplate().update(group);
		}
		catch ( RuntimeException ex )
		{
			checkForDisconnect(ex);
			throw ex;
		}
	}

	public void delete(Group group)
	{
		try
		{
			getHibernateTemplate().delete(group);
		}
		catch ( RuntimeException ex )
		{
			checkForDisconnect(ex);
			throw ex;
		}
	}

	// ========== GETTERS AND SETTERS ==========

	public Permissions getPermissions()
	{
		return permissions;
	}

	public void setPermissions(Permissions permissions)
	{
		this.permissions = permissions;
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
