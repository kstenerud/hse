package org.stenerud.hse.security;

/**
 * Exception to mark permission violations.
 * 
 * @author Karl Stenerud
 */
public class NoPermissionException extends SecurityException
{
	private static final long serialVersionUID = 1L;

	public NoPermissionException()
	{
		super();
	}

	public NoPermissionException(String message)
	{
		super(message);
	}
}
