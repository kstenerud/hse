package org.stenerud.hse.base.tool;

/**
 * Exception thrown if an error occurs while processing an XML file.
 * 
 * @author Karl Stenerud
 */
public class XmlException extends RuntimeException
{
	private static final long serialVersionUID = 1L;

	public XmlException()
	{
		// Placeholder for default constructor.
	}

	public XmlException(String message)
	{
		super(message);
	}

	public XmlException(Throwable cause)
	{
		super(cause);
	}

	public XmlException(String message, Throwable cause)
	{
		super(message, cause);
	}
}
