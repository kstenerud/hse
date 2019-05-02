package org.stenerud.hse;

/**
 * A RuntimeException version of ParseException.
 * 
 * @author Karl Stenerud
 */
public class ParseException extends RuntimeException
{
	private static final long serialVersionUID = 1L;

	private java.text.ParseException checkedException;

	public ParseException(String s, int errorOffset)
	{
		this(new java.text.ParseException(s, errorOffset));
	}

	public ParseException(java.text.ParseException checkedException)
	{
		super(checkedException);
		this.checkedException = checkedException;
	}

	public int getErrorOffset()
	{
		return checkedException.getErrorOffset();
	}

	public String getMessage()
	{
		return checkedException.getMessage();
	}

	public String getLocalizedMessage()
	{
		return checkedException.getLocalizedMessage();
	}
}
