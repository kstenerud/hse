package org.stenerud.hse.base.data;

/**
 * Handles PostgreSQL database disconnects.
 * 
 * @author Karl Stenerud
 */
public class PostgresqlDisconnectHandler implements DisconnectHandler
{
	// ========== IMPLEMENTATION ==========

	public void handleDisconnect(Throwable t)
	{
		if ( isDisconnect(t) )
		{
			throw new DisconnectException(t);
		}
	}

	// ========== UTILITY METHODS ==========

	/**
	 * Check if an exception is the result of a disconnect.
	 * 
	 * @return true if there was a disconnect.
	 */
	private boolean isDisconnect(Throwable t)
	{
		Throwable cause = findCause(t, java.net.SocketException.class);
		if ( null != cause )
		{
			return true;
		}

		cause = findCause(t, java.io.IOException.class);
		if ( null != cause )
		{
			return true;
		}

		cause = findCause(t, org.hibernate.exception.GenericJDBCException.class);
		if ( null != cause )
		{
			return cause.getMessage().indexOf("Cannot open connection") >= 0;
		}

		return false;
	}

	/**
	 * Look for a specific cause in an exception's stack trace.
	 * 
	 * @param t the exception to examine.
	 * @param cause the cause to search fo.
	 * @return the cause, or null if none found.
	 */
	private Throwable findCause(Throwable t, Class cause)
	{
		for ( Throwable currentCause = t; currentCause != null; currentCause = currentCause.getCause() )
		{
			if ( currentCause.getClass().equals(cause) )
			{
				return currentCause;
			}
		}
		return null;
	}
}
