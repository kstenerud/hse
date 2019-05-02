package org.stenerud.hse.base.tool;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;

/**
 * Helper class that uses introspection to call arbitraty methods on an object.
 * 
 * @author Karl Stenerud
 */
public class MethodCaller
{
	// ========== IMPLEMENTATION ==========

	/**
	 * Search for the first method that matches the specified name and
	 * parameters, then call it.
	 * 
	 * @param object the object to call the method on.
	 * @param methodName the name of the method.
	 * @param parameters the parameters to pass to the method (containing string
	 *           representations of the parameters).
	 * @return the rereturn value from the method.
	 * @throws IllegalArgumentException if no match is found.
	 * @throws RuntimeException wrapping an InvocationTargetException or
	 *            IllegalAccessException.
	 */
	public Object callMethod(Object object, String methodName, List parameters)
	{
		// Arguments to the method call.
		Object[] arguments = new Object[parameters.size()];

		// Go through all available methods, searching for a match.
		Method[] methods = object.getClass().getDeclaredMethods();
		try
		{
			for ( int i = 0; i < methods.length; i++ )
			{
				try
				{
					// First try: Match the method name.
					if ( methods[i].getName().equals(methodName)
							&& methods[i].getParameterTypes().length == parameters.size() )
					{
						// Second try: Attempt to convert the parameters to the
						// correct type.
						// If this fails, we catch the RuntimeException below.
						Class[] parameterTypes = methods[i].getParameterTypes();
						Iterator iter = parameters.iterator();
						for ( int j = 0; j < parameterTypes.length; j++ )
						{
							arguments[j] = convertType((String)iter.next(), parameterTypes[j]);
						}
						// At this point we are good to go. Invoke the method.
						return methods[i].invoke(object, arguments);
					}
				}
				catch ( RuntimeException ex )
				{
					// ex.printStackTrace();
					// Ignore. Try the next method.
				}
			}
		}
		catch ( InvocationTargetException ex )
		{
			throw new RuntimeException(ex);
		}
		catch ( IllegalAccessException ex )
		{
			throw new RuntimeException(ex);
		}

		// No matches were found. Throw an appropriate exception.
		throw new IllegalArgumentException(object.getClass().toString() + " has no method to take " + methodName + "("
				+ makeListString(parameters) + ")");
	}

	// ========== UTILITY METHODS ==========

	/**
	 * Convert the contents of a list into a string.
	 * 
	 * @param list the list to convert.
	 * @return the converted string.
	 */
	private String makeListString(List list)
	{
		StringBuffer buff = new StringBuffer();
		for ( Iterator iter = list.iterator(); iter.hasNext(); )
		{
			buff.append("[");
			buff.append(iter.next());
			buff.append("]");
			if ( iter.hasNext() )
			{
				buff.append(", ");
			}
		}
		return buff.toString();
	}

	/**
	 * Convert a string to an arbitrary class.
	 * 
	 * @param original the string to convert.
	 * @param convertTo the class to convert to.
	 * @return the converted object.
	 */
	private Object convertType(String original, Class convertTo)
	{
		if ( convertTo.equals(Boolean.class) || convertTo.equals(boolean.class) )
		{
			return new Boolean(original);
		}
		if ( convertTo.equals(Integer.class) || convertTo.equals(int.class) )
		{
			return new Integer(original);
		}
		if ( convertTo.equals(Long.class) || convertTo.equals(long.class) )
		{
			return new Long(original);
		}
		if ( convertTo.equals(Float.class) || convertTo.equals(float.class) )
		{
			return new Float(original);
		}

		if ( original.equals("null") )
		{
			return null;
		}

		if ( convertTo.equals(String.class) )
		{
			return original;
		}

		throw new IllegalArgumentException("Don't know how to convert " + original + " to " + convertTo);
	}
}
