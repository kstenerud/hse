package org.stenerud.hse.base.tool;

import java.util.Map;
import java.util.HashMap;

import org.xml.sax.Attributes;

/**
 * A helper class to make it easier to access xml element attributes through
 * SAX. <br>
 * Attributes can be retrieved in a number of common formats.
 * 
 * @author Karl Stenerud
 */
public class AttributeHelper
{
	// ========== PRIVATE MEMBERS ==========
	private Map attributes = new HashMap();

	// ========== IMPLEMENTATION ==========

	/**
	 * Constructor.
	 * 
	 * @param atts A collection of attributes provided by SAX.
	 */
	public AttributeHelper(Attributes atts)
	{
		int length = atts.getLength();
		for ( int i = 0; i < length; i++ )
			attributes.put(atts.getQName(i), atts.getValue(i));
	}

	/**
	 * Check if a particular attribute is present.
	 * 
	 * @param name The name of the attribute.
	 * @return true if the attribute exists.
	 */
	public boolean contains(String name)
	{
		return attributes.containsKey(name);
	}

	/**
	 * Get an attribute as a string.
	 * 
	 * @param name The attribute's name.
	 * @return The attribute value.
	 */
	public String getString(String name)
	{
		String result = (String)attributes.get(name);
		if ( null == result )
			throw new XmlException("Attribute " + name + " not found");
		return result;
	}

	/**
	 * Get an attribute as a string.
	 * 
	 * @param name The attribute's name.
	 * @param defaultValue The value to return if the attribute doesn't exist.
	 * @return The attribute value, or the default value if the attribute
	 *         doesn't exist.
	 */
	public String getString(String name, String defaultValue)
	{
		String result = (String)attributes.get(name);
		if ( null == result )
			return defaultValue;
		return result;
	}

	/**
	 * Get an attribute as a boolean.
	 * 
	 * @param name The attribute's name.
	 * @return The attribute value.
	 */
	public boolean getBoolean(String name)
	{
		return "true".equals(getString(name));
	}

	/**
	 * Get an attribute as a boolean.
	 * 
	 * @param name The attribute's name.
	 * @param defaultValue The value to return if the attribute doesn't exist.
	 * @return The attribute value, or the default value if the attribute
	 *         doesn't exist.
	 */
	public boolean getBoolean(String name, boolean defaultValue)
	{
		String result = (String)attributes.get(name);
		if ( null == result )
			return defaultValue;
		return "true".equals(result);
	}

	/**
	 * Get an attribute as an integer.
	 * 
	 * @param name The attribute's name.
	 * @return The attribute value.
	 */
	public int getInteger(String name)
	{
		return Integer.parseInt(getString(name));
	}

	/**
	 * Get an attribute as an integer.
	 * 
	 * @param name The attribute's name.
	 * @param defaultValue The value to return if the attribute doesn't exist.
	 * @return The attribute value, or the default value if the attribute
	 *         doesn't exist.
	 */
	public int getInteger(String name, int defaultValue)
	{
		String result = (String)attributes.get(name);
		if ( null == result )
			return defaultValue;
		return Integer.parseInt(result);
	}

	/**
	 * Get an attribute as a long.
	 * 
	 * @param name The attribute's name.
	 * @return The attribute value.
	 */
	public long getLong(String name)
	{
		return Long.parseLong(getString(name));
	}

	/**
	 * Get an attribute as a long.
	 * 
	 * @param name The attribute's name.
	 * @param defaultValue The value to return if the attribute doesn't exist.
	 * @return The attribute value, or the default value if the attribute
	 *         doesn't exist.
	 */
	public long getLong(String name, long defaultValue)
	{
		String result = (String)attributes.get(name);
		if ( null == result )
			return defaultValue;
		return Long.parseLong(result);
	}

	/**
	 * Get an attribute as a floating point number.
	 * 
	 * @param name The attribute's name.
	 * @return The attribute value.
	 */
	public float getFloat(String name)
	{
		return Float.parseFloat(getString(name));
	}

	/**
	 * Get an attribute as a floating point number.
	 * 
	 * @param name The attribute's name.
	 * @param defaultValue The value to return if the attribute doesn't exist.
	 * @return The attribute value, or the default value if the attribute
	 *         doesn't exist.
	 */
	public float getFloat(String name, float defaultValue)
	{
		String result = (String)attributes.get(name);
		if ( null == result )
			return defaultValue;
		return Float.parseFloat(result);
	}
}
