package org.stenerud.hse.base.data;

import org.springframework.orm.hibernate3.HibernateTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.DefaultTransactionStatus;

/**
 * Hibernate transaction manager overrided to handle disconnects.
 * 
 * @author Karl Stenerud
 */
public class DisconnectHandlingTransactionManager extends HibernateTransactionManager
{
	private static final long serialVersionUID = 1L;

	// ========== INJECTED MEMBERS ==========
	private DisconnectHandlerFactory disconnectHandlerFactory;

	// ========== IMPLEMENTATION ==========

	protected void doBegin(Object arg0, TransactionDefinition arg1)
	{
		try
		{
			super.doBegin(arg0, arg1);
		}
		catch ( RuntimeException ex )
		{
			disconnectHandlerFactory.getDisconnectHandler().handleDisconnect(ex);
			throw ex;
		}
	}

	protected void doCleanupAfterCompletion(Object arg0)
	{
		try
		{
			super.doCleanupAfterCompletion(arg0);
		}
		catch ( RuntimeException ex )
		{
			disconnectHandlerFactory.getDisconnectHandler().handleDisconnect(ex);
			throw ex;
		}
	}

	protected void doCommit(DefaultTransactionStatus arg0)
	{
		try
		{
			super.doCommit(arg0);
		}
		catch ( RuntimeException ex )
		{
			disconnectHandlerFactory.getDisconnectHandler().handleDisconnect(ex);
			throw ex;
		}
	}

	protected Object doGetTransaction()
	{
		try
		{
			return super.doGetTransaction();
		}
		catch ( RuntimeException ex )
		{
			disconnectHandlerFactory.getDisconnectHandler().handleDisconnect(ex);
			throw ex;
		}
	}

	protected void doRollback(DefaultTransactionStatus arg0)
	{
		try
		{
			super.doRollback(arg0);
		}
		catch ( RuntimeException ex )
		{
			disconnectHandlerFactory.getDisconnectHandler().handleDisconnect(ex);
			throw ex;
		}
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
