package org.stenerud.hse.base.data;

/**
 * Examines exceptions and throws a DisconnectException if the exception
 * indicates a loss of connectivity to the database.
 * 
 * @author Karl Stenerud
 */
public interface DisconnectHandler
{
	/**
	 * Examine an exception and throw a DisconnectException if the exception
	 * indicates a loss of connectivity to the database.
	 * 
	 * @param t the exception to examine.
	 * @throws DisconnectException if database connectivity has been lost.
	 */
	public void handleDisconnect(Throwable t);
}
