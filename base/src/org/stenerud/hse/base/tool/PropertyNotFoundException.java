package org.stenerud.hse.base.tool;

/**
 * Exception thrown when an expected property is not found.
 * 
 * @author Karl Stenerud
 */
public class PropertyNotFoundException extends RuntimeException
{
	private static final long serialVersionUID = 1L;

	public PropertyNotFoundException()
	{
		super();
	}

	public PropertyNotFoundException(String message, Throwable cause)
	{
		super(message, cause);
	}

	public PropertyNotFoundException(String message)
	{
		super(message);
	}

	public PropertyNotFoundException(Throwable cause)
	{
		super(cause);
	}
}
