package org.stenerud.hse.data.security;

import org.stenerud.hse.data.CriteriaBuffer;
import org.stenerud.hse.data.DataException;
import org.stenerud.hse.data.ExtendedHibernateDaoSupport;

/**
 * Hibernate implementation of the security DAO.
 * 
 * @author Karl Stenerud
 */
public class SecurityDAODatabase extends ExtendedHibernateDaoSupport implements SecurityDAO
{
	// ========== IMPLEMENTATION ==========

	public Permission getPermission(String name)
	{
		CriteriaBuffer criteria = new CriteriaBuffer("FROM Permission permission");
		criteria.addCriteria("permission.name =", name);

		Permission result = (Permission)getFirst(criteria.getQuery());
		if ( null == result )
			throw new DataException("No permission with name " + name);
		return result;
	}

	public Permission getOrCreatePermission(String name)
	{
		CriteriaBuffer criteria = new CriteriaBuffer("FROM Permission permission");
		criteria.addCriteria("permission.name =", name);

		Permission result = (Permission)getFirst(criteria.getQuery());
		if ( null == result )
		{
			Permission permission = new Permission();
			permission.setName(name);
			getHibernateTemplate().save(permission);
			result = (Permission)getFirst(criteria.getQuery());
			if ( null == result )
			{
				throw new DataException("Failed to create permission " + name);
			}
		}
		return result;
	}

	public Permission refresh(Permission permission)
	{
		getHibernateTemplate().refresh(permission);
		return permission;
	}

	public void create(PermissionLevel permissionLevel)
	{
		getHibernateTemplate().save(permissionLevel);
	}

	public void update(PermissionLevel permissionLevel)
	{
		getHibernateTemplate().update(permissionLevel);
	}

	public PermissionLevel refresh(PermissionLevel permissionLevel)
	{
		getHibernateTemplate().refresh(permissionLevel);
		return permissionLevel;
	}
}
