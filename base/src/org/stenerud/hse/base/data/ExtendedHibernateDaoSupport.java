package org.stenerud.hse.base.data;

import java.util.List;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * A further enhancement upon Spring's HibernateDaoSupport.
 * 
 * @author Karl Stenerud
 */
public abstract class ExtendedHibernateDaoSupport extends HibernateDaoSupport
{
	// ========== INJECTED MEMBERS ==========
	private DisconnectHandlerFactory disconnectHandlerFactory;

	// ========== IMPLEMENTATION ==========

	/**
	 * Get the first object from a query.
	 * 
	 * @param query the HQL query.
	 * @return the first object from the result or null if there were no
	 *         results.
	 */
	protected Object getFirst(String query)
	{
		List result = getHibernateTemplate().find(query);
		if ( null == result || result.size() == 0 )
			return null;
		return result.get(0);
	}

	/**
	 * Get the number of records matching a query.
	 * 
	 * @param query the query to count results for.
	 * @return the number of results.
	 */
	protected int getCount(String query)
	{
		return ((Integer)getFirst("select count(*) " + query)).intValue();
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

	/**
	 * Check to see if the database has been disconnected, and throw a
	 * DisconnectException if it has.
	 * 
	 * @param t the thrown exception.
	 * @throws DisconnectException if the database has been disconnected.
	 */
	protected void checkForDisconnect(Throwable t)
	{
		disconnectHandlerFactory.getDisconnectHandler().handleDisconnect(t);
	}

	// ========== GETTERS AND SETTERS ==========

	public DisconnectHandlerFactory getDisconnectHandlerFactory()
	{
		return disconnectHandlerFactory;
	}

	public void setDisconnectHandlerFactory(DisconnectHandlerFactory disconnectHandlerFactory)
	{
		this.disconnectHandlerFactory = disconnectHandlerFactory;
	}
}
