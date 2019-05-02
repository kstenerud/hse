package org.stenerud.hse.data.group;

import java.util.Iterator;
import java.util.List;

import org.stenerud.hse.data.CriteriaBuffer;
import org.stenerud.hse.data.ExtendedHibernateDaoSupport;
import org.stenerud.hse.data.security.Permission;
import org.stenerud.hse.data.security.PermissionLevel;
import org.stenerud.hse.data.security.SecurityDAO;
import org.stenerud.hse.security.PermissionLevels;
import org.stenerud.hse.security.Permissions;

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
		CriteriaBuffer criteria = new CriteriaBuffer(STANDARD_FROM);
		criteria.addCriteria("group.administrator =", true);

		return (Group)this.getFirst(criteria.getQuery());
	}

	public int getAdministratorId()
	{
		CriteriaBuffer criteria = new CriteriaBuffer("SELECT group.id FROM Group group");
		criteria.addCriteria("group.administrator =", true);

		Integer result = (Integer)this.getFirst(criteria.getQuery());
		return null == result ? -1 : result.intValue();
	}

	public Group createAdministrator(String name)
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

	public Group getGroup(String name)
	{
		CriteriaBuffer criteria = new CriteriaBuffer(STANDARD_FROM);
		criteria.addCriteria("group.name =", name);

		return (Group)getFirst(criteria.getQuery());
	}

	public List getGroups()
	{
		CriteriaBuffer criteria = new CriteriaBuffer(STANDARD_FROM, "ORDER BY group.administrator DESC, group.name");

		return getHibernateTemplate().find(criteria.getQuery());
	}

	public boolean hasUsers(Group group)
	{
		CriteriaBuffer criteria = new CriteriaBuffer("SELECT count(*) from User user", null);
		criteria.addCriteria("user.userGroup =", group.getId());
		return ((Integer)this.getFirst(criteria.getQuery())).intValue() > 0;
	}

	public Group refresh(Group group)
	{
		getHibernateTemplate().refresh(group);
		return group;
	}

	public void create(Group group)
	{
		getHibernateTemplate().save(group);
	}

	public void update(Group group)
	{
		getHibernateTemplate().update(group);
	}

	public void delete(Group group)
	{
		getHibernateTemplate().delete(group);
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
