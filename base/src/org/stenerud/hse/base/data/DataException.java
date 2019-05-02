package org.stenerud.hse.base.data;

/**
 * General exception class for most errors in the data layer.
 * 
 * @author Karl Stenerud
 */
public class DataException extends RuntimeException
{
	private static final long serialVersionUID = 1L;

	public DataException(String message)
	{
		super(message);
	}

	public DataException(Throwable cause)
	{
		super(cause);
	}

	public DataException(String message, Throwable cause)
	{
		super(message, cause);
	}
}
