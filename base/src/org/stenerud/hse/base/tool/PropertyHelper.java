package org.stenerud.hse.base.tool;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.core.io.Resource;

/**
 * Auto-refreshing properties access class that provides a more functional
 * interface than Properties. <br>
 * Will auto-refresh if the injected resource is on the filesystem (not in a
 * jar) and the check interval is > 0.
 * 
 * @author Karl Stenerud
 */
public class PropertyHelper
{
	// ========== CONSTANTS ==========
	private static final long DEFAULT_CHECK_INTERVAL = 600000; // 60 seconds

	/**
	 * Pattern to capture escape sequences. Escape sequences are in the format
	 * "${anystring}".
	 */
	private static final Pattern escapePattern = Pattern.compile("\\Q${\\E([^}]*)}");

	// ========== INJECTED MEMBERS ==========
	private Properties properties = new Properties();
	private File file;
	private long checkInterval = DEFAULT_CHECK_INTERVAL;

	// ========== PRIVATE MEMBERS ==========
	private long lastChecked = 0;
	private long lastModified = 0;

	// ========== IMPLEMENTATION ==========

	/**
	 * Get the names of all the properties available to this loader.
	 * 
	 * @return a list of the property names
	 */
	public List getPropertyNames()
	{
		checkRefresh();
		List list = new LinkedList();
		for ( Enumeration names = properties.propertyNames(); names.hasMoreElements(); )
		{
			list.add(names.nextElement());
		}
		return list;
	}

	/**
	 * Get the names of all properties that match a regular expression.
	 * 
	 * @param regex the regular expression.
	 * @return a list of property names.
	 */
	public Collection getPropertyNames(String regex)
	{
		checkRefresh();
		Pattern pattern = Pattern.compile(regex);
		Set results = new TreeSet();
		for ( Enumeration names = properties.propertyNames(); names.hasMoreElements(); )
		{
			String propertyName = (String)names.nextElement();
			if ( pattern.matcher(propertyName).matches() )
			{
				results.add(propertyName);
			}
		}
		return results;
	}

	/**
	 * Check if a property exists.
	 * 
	 * @param name the name of the property.
	 * @return true if the property exists.
	 */
	public boolean hasProperty(String name)
	{
		checkRefresh();
		return properties.containsKey(name);
	}

	/**
	 * Get a property as a string.
	 * 
	 * @param name the name of the property.
	 * @return the property value.
	 * @throws PropertyNotFoundException if the property is not found.
	 */
	public String getString(String name)
	{
		checkRefresh();
		String result = properties.getProperty(name);
		if ( null == result )
		{
			throw new PropertyNotFoundException(name + ": No such property");
		}
		return expandString(result);
	}

	/**
	 * Get a property as a string.
	 * 
	 * @param name the name of the property.
	 * @param defaultValue the value to return if the property was not found.
	 * @return the property value or defaultValue if the property was not found.
	 */
	public String getString(String name, String defaultValue)
	{
		checkRefresh();
		try
		{
			return expandString(properties.getProperty(name, defaultValue));
		}
		catch ( PropertyNotFoundException ex )
		{
			return defaultValue;
		}
	}

	/**
	 * Get a property as an integer.
	 * 
	 * @param name the name of the property.
	 * @return the property value.
	 * @throws PropertyNotFoundException if the property is not found.
	 */
	public int getInteger(String name)
	{
		return Integer.parseInt(getString(name));
	}

	/**
	 * Get a property as an integer.
	 * 
	 * @param name the name of the property.
	 * @param defaultValue the value to return if the property was not found.
	 * @return the property value or defaultValue if the property was not found.
	 */
	public int getInteger(String name, int defaultValue)
	{
		String result = getString(name, null);
		return null == result ? defaultValue : Integer.parseInt(result);
	}

	/**
	 * Get a property as a long.
	 * 
	 * @param name the name of the property.
	 * @return the property value.
	 * @throws PropertyNotFoundException if the property is not found.
	 */
	public long getLong(String name)
	{
		return Long.parseLong(getString(name));
	}

	/**
	 * Get a property as a long.
	 * 
	 * @param name the name of the property.
	 * @param defaultValue the value to return if the property was not found.
	 * @return the property value or defaultValue if the property was not found.
	 */
	public long getLong(String name, long defaultValue)
	{
		String result = getString(name, null);
		return null == result ? defaultValue : Integer.parseInt(result);
	}

	/**
	 * Get a property as a file.
	 * 
	 * @param name the name of the property.
	 * @return the property value.
	 * @throws PropertyNotFoundException if the property is not found.
	 */
	public File getFile(String name)
	{
		return new File(getString(name));
	}

	/**
	 * Get a property as a file.
	 * 
	 * @param name the name of the property.
	 * @param defaultValue the value to return if the property was not found.
	 * @return the property value or defaultValue if the property was not found.
	 */
	public File getFile(String name, File defaultValue)
	{
		String result = getString(name, null);
		return null == result ? defaultValue : new File(result);
	}

	/**
	 * Get a property as a boolean.
	 * 
	 * @param name the name of the property.
	 * @return the property value.
	 * @throws PropertyNotFoundException if the property is not found.
	 */
	public boolean getBoolean(String name)
	{
		return Boolean.valueOf(getString(name)).booleanValue();
	}

	/**
	 * Get a property as a boolean.
	 * 
	 * @param name the name of the property.
	 * @param defaultValue the value to return if the property was not found.
	 * @return the property value or defaultValue if the property was not found.
	 */
	public boolean getBoolean(String name, boolean defaultValue)
	{
		String result = getString(name, null);
		return null == result ? defaultValue : Boolean.valueOf(result).booleanValue();
	}

	/**
	 * Get a list of strings from a property.
	 * 
	 * @param name the name of the property.
	 * @param separator the value used to separate values in the property.
	 * @return a list of strings.
	 */
	public List getStringList(String name, String separator)
	{
		return makeStringList(getString(name), separator);
	}

	/**
	 * Get a list of strings from a property.
	 * 
	 * @param name the name of the property.
	 * @param separator the value used to separate values in the property.
	 * @param defaultValue the default to return if the property was not found.
	 * @return a list of strings.
	 */
	public List getStringList(String name, String separator, List defaultValue)
	{
		String value = getString(name, null);
		if ( null == value )
		{
			return defaultValue;
		}
		return makeStringList(value, separator);
	}

	/**
	 * Get a list of integers from a property.
	 * 
	 * @param name the name of the property.
	 * @param separator the value used to separate values in the property.
	 * @return a list of integers.
	 * @throws PropertyNotFoundException if the property is not found.
	 */
	public List getIntegerList(String name, String separator)
	{
		return makeIntegerList(getStringList(name, separator));
	}

	/**
	 * Get a list of integers from a property.
	 * 
	 * @param name the name of the property.
	 * @param separator the value used to separate values in the property.
	 * @param defaultValue the default to return if the property was not found.
	 * @return a list of integers.
	 */
	public List getIntegerList(String name, String separator, List defaultValue)
	{
		List stringList = getStringList(name, separator, null);
		if ( null == stringList )
		{
			return defaultValue;
		}
		return makeIntegerList(stringList);

	}

	/**
	 * Routine to interpret and expand any escaped strings in a value. <br>
	 * Escape strings are in the format "${something}". All escaped strings must
	 * refer to a property in the properties file. <br>
	 * For example, "${path.data}" will be replaced with the value of property
	 * "path.data" in the properties file.
	 * 
	 * @param valueIn the string to expand.
	 * @return the expanded string.
	 */
	public String expandString(String valueIn)
	{
		// return null for null
		if ( null == valueIn )
		{
			return null;
		}

		String value = valueIn;

		// Re-run the expansion until there are no more expansions to do.
		for ( ;; )
		{
			Matcher matcher = escapePattern.matcher(value);
			// No more expansions to do. Return the result.
			if ( !matcher.find() )
			{
				return value;
			}

			// Make a new matcher to use for picking out the matches.
			matcher = escapePattern.matcher(value);

			StringBuffer result = new StringBuffer();
			int lastEnd = 0;

			// Replace all escaped strings with property values
			while ( matcher.find() )
			{
				int start = matcher.start();
				if ( start != lastEnd )
				{
					result.append(value.substring(lastEnd, start));
				}
				String propertyName = matcher.group(1);
				result.append(getString(propertyName));
				lastEnd = matcher.end();
			}

			// Fill in any remaining text
			if ( value.length() > lastEnd )
			{
				result.append(value.substring(lastEnd, value.length()));
			}

			// Save the current result for the next iteration.
			value = result.toString();
		}
	}

	/**
	 * Add all of the properties of a Properties to this PropertyLoader. If a
	 * key already exists in this PropertyLoader, it is not added.
	 * 
	 * @param newProperties the new properties to add.
	 */
	public void addProperties(Properties newProperties)
	{
		for ( Iterator iter = newProperties.entrySet().iterator(); iter.hasNext(); )
		{
			Map.Entry entry = (Map.Entry)iter.next();
			if ( !properties.containsKey(entry.getKey()) )
			{
				properties.put(entry.getKey(), entry.getValue());
			}
		}
	}

	/**
	 * Add all of the properties of a PropertyLoader to this PropertyLoader. If
	 * a key already exists in this PropertyLoader, it is not added.
	 * 
	 * @param newProperties the new properties to add.
	 */
	public void addProperties(PropertyHelper newProperties)
	{
		addProperties(newProperties.properties);
	}

	// ========== UTILITY METHODS ==========

	/**
	 * Check if the properties file has changed, and reload if necessary. <br>
	 * This will only do an actual check once every check interval.
	 */
	private void checkRefresh()
	{
		if ( null != file && checkInterval > 0 )
		{
			long nowTime = new Date().getTime();
			if ( nowTime - lastChecked > checkInterval )
			{
				if ( file.lastModified() != lastModified )
				{
					lastModified = file.lastModified();
					FileInputStream stream = null;
					try
					{
						properties = new Properties();
						stream = new FileInputStream(file);
						properties.load(stream);
					}
					catch ( IOException ex )
					{
						throw new PropertyNotFoundException("Properties file " + file + " not found");
					}
					finally
					{
						if ( null != stream )
						{
							try
							{
								stream.close();
							}
							catch ( IOException ex )
							{
								// Ignore
							}
						}
					}
				}
				lastChecked = nowTime;
			}
		}
	}

	/**
	 * Make a list of strings from a source string, using the specified
	 * separator.
	 * 
	 * @param value the string to split.
	 * @param separator the separatoe value.
	 * @return the split string list.
	 */
	private List makeStringList(String value, String separator)
	{
		List list = new LinkedList();
		StringTokenizer st = new StringTokenizer(value, separator);
		while ( st.hasMoreTokens() )
		{
			String token = st.nextToken().trim();
			list.add(token);
		}
		return list;
	}

	/**
	 * Convert a list of strings into a list of integers.
	 * 
	 * @param strings the strings to convert.
	 * @return a list of integers.
	 */
	private List makeIntegerList(List strings)
	{
		List result = new LinkedList();
		for ( Iterator iter = strings.iterator(); iter.hasNext(); )
		{
			result.add(new Integer((String)iter.next()));
		}
		return result;
	}

	// ========== GETTERS AND SETTERS ==========

	public Properties getProperties()
	{
		return properties;
	}

	public void setProperties(Properties properties)
	{
		this.file = null;
		this.properties = properties;
	}

	/**
	 * Get the interval that the property helper checks the property file for
	 * changes.
	 * 
	 * @return the interval in milliseconds.
	 */
	public long getCheckInterval()
	{
		return checkInterval;
	}

	/**
	 * Set the interval that the property helper checks the property file for
	 * changes.
	 * 
	 * @param checkInterval the interval in milliseconds.
	 */
	public void setCheckInterval(long checkInterval)
	{
		this.checkInterval = checkInterval;
	}

	public void setResource(Resource resource) throws IOException
	{
		file = resource.getFile();
		properties = new Properties();
		properties.load(resource.getInputStream());
	}
}
