package org.stenerud.hse.base.data;

/**
 * A factory that generates a DisconnectHandler from its class name.
 * 
 * @author Karl Stenerud
 */
public class DisconnectHandlerFactory
{
	// ========== PRIVATE MEMBERS ==========
	private DisconnectHandler handler;

	// ========== IMPLEMENTATION ==========

	/**
	 * Get the generated disconnect handler.
	 * 
	 * @return the disconnect handler.
	 */
	public DisconnectHandler getDisconnectHandler()
	{
		return handler;
	}

	// ========== GETTERS AND SETTERS ==========

	public void setHandlerClassName(String className)
	{
		try
		{
			handler = (DisconnectHandler)Class.forName(className).newInstance();
		}
		catch ( InstantiationException ex )
		{
			throw new IllegalArgumentException(className + " is abstract");
		}
		catch ( IllegalAccessException ex )
		{
			throw new IllegalArgumentException(className + " cannot access default constructor");
		}
		catch ( ClassNotFoundException ex )
		{
			throw new IllegalArgumentException(className + " cannot find class");
		}
	}

}
