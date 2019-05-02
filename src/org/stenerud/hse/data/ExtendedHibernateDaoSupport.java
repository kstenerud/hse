package org.stenerud.hse.data;

import java.util.List;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * A further enhancement upon Spring's HibernateDaoSupport.
 * 
 * @author Karl Stenerud
 */
public abstract class ExtendedHibernateDaoSupport extends HibernateDaoSupport
{
	/**
	 * Get the first object from a query.
	 * 
	 * @param query the HQL query.
	 * @return the first object from the result or null if there were no results.
	 */
	protected Object getFirst(String query)
	{
		List result = getHibernateTemplate().find(query);
		if ( null == result || result.size() == 0 )
			return null;
		return result.get(0);
	}

	/**
	 * Get an object by its ID in the table.
	 * 
	 * @param clazz the class of the object to get.
	 * @param id the object's ID.
	 * @return the corresponding object or null if none was found.
	 */
	protected Object getById(Class clazz, int id)
	{
		return getHibernateTemplate().get(clazz, new Integer(id));
	}
}
