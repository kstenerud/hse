package org.stenerud.hse.ui;

import java.util.Locale;

import org.springframework.context.MessageSource;

/**
 * A Spring interface to localized messages.
 * 
 * @author Karl Stenerud
 */
public class SpringMessages implements Messages
{
	// ========== INJECTED MEMBERS ==========
	private MessageSource messageSource;
	private Locale locale;

	// ========== IMPLEMENTATION ==========

	public String get(String name)
	{
		return messageSource.getMessage(name, null, locale);
	}

	public String get(String name, String[] params)
	{
		return messageSource.getMessage(name, params, locale);
	}

	public String get(String name, String parameter)
	{
		return messageSource.getMessage(name, new String[]
			{ parameter }, locale);
	}

	public String get(String name, String parameter1, String parameter2)
	{
		return messageSource.getMessage(name, new String[]
			{ parameter1, parameter2 }, locale);
	}

	public String get(String name, String parameter1, String parameter2, String parameter3)
	{
		return messageSource.getMessage(name, new String[]
			{ parameter1, parameter2, parameter3 }, locale);
	}

	// ========== GETTERS AND SETTERS ==========

	public Locale getLocale()
	{
		return locale;
	}

	/**
	 * Set the locale for all messages.
	 * 
	 * @param locale the locale to set.
	 */
	public void setLocale(Locale locale)
	{
		this.locale = locale;
	}

	public MessageSource getMessageSource()
	{
		return messageSource;
	}

	public void setMessageSource(MessageSource messageSource)
	{
		this.messageSource = messageSource;
	}
}
