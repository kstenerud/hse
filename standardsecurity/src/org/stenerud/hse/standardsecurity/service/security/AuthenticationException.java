package org.stenerud.hse.standardsecurity.service.security;

/**
 * Exception thrown when user authentication fails.
 * 
 * @author Karl Stenerud
 */
public class AuthenticationException extends SecurityException
{
	private static final long serialVersionUID = 1L;

	public AuthenticationException()
	{
		super();
	}

	public AuthenticationException(String message)
	{
		super(message);
	}
}
