package org.stenerud.hse.base.ui.validation;

/**
 * An exception thrown when a validation rule fails. <br>
 * The message should contain the reason for the validation failure.
 * 
 * @author Karl Stenerud
 */
public class ValidationException extends RuntimeException
{
	private static final long serialVersionUID = 1L;

	public ValidationException(String message)
	{
		super(message);
	}

	public ValidationException(Throwable cause)
	{
		super(cause);
	}

	public ValidationException(String message, Throwable cause)
	{
		super(message, cause);
	}
}
