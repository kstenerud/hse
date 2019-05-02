package org.stenerud.hse.base.data;

import java.util.List;

import org.hibernate.SessionFactory;

/**
 * Bean used to publish interests in hibernate mapping lists. <br>
 * Note: This is a placeholder class for use in autoconfiguring hibernate
 * resources.
 * 
 * @author Karl Stenerud
 */
public class HibernateConfigurationInterest
{
	// ========== INJECTED MEMBERS ==========
	private SessionFactory sessionFactory;
	private DisconnectHandlerFactory disconnectHandlerFactory;
	private List configurationMatchers;

	// ========== GETTERS AND SETTERS ==========

	public List getConfigurationMatchers()
	{
		return configurationMatchers;
	}

	public void setConfigurationMatchers(List configurationMatchers)
	{
		this.configurationMatchers = configurationMatchers;
	}

	public DisconnectHandlerFactory getDisconnectHandlerFactory()
	{
		return disconnectHandlerFactory;
	}

	public void setDisconnectHandlerFactory(DisconnectHandlerFactory disconnectHandlerFactory)
	{
		this.disconnectHandlerFactory = disconnectHandlerFactory;
	}

	public SessionFactory getSessionFactory()
	{
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory)
	{
		this.sessionFactory = sessionFactory;
	}
}
