package org.stenerud.hse.base.data;

import java.util.List;

import org.springframework.orm.hibernate3.HibernateTransactionManager;

/**
 * A mappingResources of mapping files used by Hibernate. <br>
 * Note: This is a placeholder class for use in autoconfiguring hibernate
 * resources.
 * 
 * @author Karl Stenerud
 */
public class HibernateConfiguration
{
	// ========== INJECTED MEMBERS ==========
	/**
	 * A mappingResources of mapping files, specified in the spring bean
	 * definition.
	 */
	private List mappingResources;
	private HibernateTransactionManager transactionManager;
	private List transactionDAOs;

	// ========== GETTERS AND SETTERS ==========

	public List getMappingResources()
	{
		return mappingResources;
	}

	public void setMappingResources(List mappingResources)
	{
		this.mappingResources = mappingResources;
	}

	public HibernateTransactionManager getTransactionManager()
	{
		return transactionManager;
	}

	public void setTransactionManager(HibernateTransactionManager transactionManager)
	{
		this.transactionManager = transactionManager;
	}

	public List getTransactionDAOs()
	{
		return transactionDAOs;
	}

	public void setTransactionDAOs(List transactionDAOs)
	{
		this.transactionDAOs = transactionDAOs;
	}
}
