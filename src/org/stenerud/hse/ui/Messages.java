package org.stenerud.hse.ui;

/**
 * A friendlier interface to localized messages.
 * 
 * @author Karl Stenerud
 */
public interface Messages
{
	/**
	 * Get a localized message.
	 * 
	 * @param name the name of the resource to get.
	 * @return the localized message.
	 */
	public String get(String name);

	/**
	 * Get a localized message.
	 * 
	 * @param name the name of the resource to get.
	 * @param params the parameters needed by the resource.
	 * @return the localized message.
	 */
	public String get(String name, String[] params);

	/**
	 * Get a localized message.
	 * 
	 * @param name the name of the resource to get.
	 * @param parameter the parameter needed by the resource.
	 * @return the localized message.
	 */
	public String get(String name, String parameter);

	/**
	 * Get a localized message.
	 * 
	 * @param name the name of the resource to get.
	 * @param parameter1 the first parameter needed by the resource.
	 * @param parameter2 the second parameter needed by the resource.
	 * @return the localized message.
	 */
	public String get(String name, String parameter1, String parameter2);

	/**
	 * Get a localized message.
	 * 
	 * @param name the name of the resource to get.
	 * @param parameter1 the first parameter needed by the resource.
	 * @param parameter2 the second parameter needed by the resource.
	 * @param parameter3 the third parameter needed by the resource.
	 * @return the localized message.
	 */
	public String get(String name, String parameter1, String parameter2, String parameter3);
}
