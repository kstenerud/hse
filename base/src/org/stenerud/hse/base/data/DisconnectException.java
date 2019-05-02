package org.stenerud.hse.base.data;

/**
 * Exception thrown when a database disconnect occurs.
 * 
 * @author Karl Stenerud
 */
public class DisconnectException extends DataException
{
	private static final long serialVersionUID = 1L;

	public DisconnectException(String message)
	{
		super(message);
	}

	public DisconnectException(Throwable cause)
	{
		super(cause);
	}

	public DisconnectException(String message, Throwable cause)
	{
		super(message, cause);
	}
}
