package org.stenerud.hse.standardsecurity.data.security;

import org.stenerud.hse.base.data.CriteriaBuffer;
import org.stenerud.hse.base.data.DataException;
import org.stenerud.hse.base.data.ExtendedHibernateDaoSupport;

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
		try
		{
			CriteriaBuffer criteria = new CriteriaBuffer("FROM Permission permission");
			criteria.addCriteria("permission.name =", name);

			Permission result = (Permission)getFirst(criteria.getQuery());
			if ( null == result )
				throw new DataException("No permission with name " + name);
			return result;
		}
		catch ( RuntimeException ex )
		{
			checkForDisconnect(ex);
			throw ex;
		}
	}

	public Permission getOrCreatePermission(String name)
	{
		try
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
		catch ( RuntimeException ex )
		{
			checkForDisconnect(ex);
			throw ex;
		}
	}

	public Permission refresh(Permission permission)
	{
		try
		{
			getHibernateTemplate().refresh(permission);
			return permission;
		}
		catch ( RuntimeException ex )
		{
			checkForDisconnect(ex);
			throw ex;
		}
	}

	public void create(PermissionLevel permissionLevel)
	{
		try
		{
			getHibernateTemplate().save(permissionLevel);
		}
		catch ( RuntimeException ex )
		{
			checkForDisconnect(ex);
			throw ex;
		}
	}

	public void update(PermissionLevel permissionLevel)
	{
		try
		{
			getHibernateTemplate().update(permissionLevel);
		}
		catch ( RuntimeException ex )
		{
			checkForDisconnect(ex);
			throw ex;
		}
	}

	public PermissionLevel refresh(PermissionLevel permissionLevel)
	{
		try
		{
			getHibernateTemplate().refresh(permissionLevel);
			return permissionLevel;
		}
		catch ( RuntimeException ex )
		{
			checkForDisconnect(ex);
			throw ex;
		}
	}
}
