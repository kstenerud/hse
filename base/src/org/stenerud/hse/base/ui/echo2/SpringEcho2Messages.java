package org.stenerud.hse.base.ui.echo2;

import nextapp.echo2.app.ApplicationInstance;

import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.stenerud.hse.base.ui.Messages;

/**
 * A Spring interface to localized messages.
 * 
 * @author Karl Stenerud
 */
public class SpringEcho2Messages implements Messages
{
	// ========== INJECTED MEMBERS ==========
	private MessageSource messageSource;

	// ========== IMPLEMENTATION ==========

	public String get(String name)
	{
		try
		{
			return messageSource.getMessage(name, null, ApplicationInstance.getActive().getLocale());
		}
		catch ( NoSuchMessageException ex )
		{
			return handleNoSuchMessage(name);
		}
	}

	public String getOrDefault(String name, String defaultValue)
	{
		return messageSource.getMessage(name, null, defaultValue, ApplicationInstance.getActive().getLocale());
	}

	public String get(String name, String[] params)
	{
		try
		{
			return messageSource.getMessage(name, params, ApplicationInstance.getActive().getLocale());
		}
		catch ( NoSuchMessageException ex )
		{
			return handleNoSuchMessage(name);
		}
	}

	public String get(String name, String parameter)
	{
		try
		{
			return messageSource.getMessage(name, new String[]
				{ parameter }, ApplicationInstance.getActive().getLocale());
		}
		catch ( NoSuchMessageException ex )
		{
			return handleNoSuchMessage(name);
		}
	}

	public String get(String name, String parameter1, String parameter2)
	{
		try
		{
			return messageSource.getMessage(name, new String[]
				{ parameter1, parameter2 }, ApplicationInstance.getActive().getLocale());
		}
		catch ( NoSuchMessageException ex )
		{
			return handleNoSuchMessage(name);
		}
	}

	public String get(String name, String parameter1, String parameter2, String parameter3)
	{
		try
		{
			return messageSource.getMessage(name, new String[]
				{ parameter1, parameter2, parameter3 }, ApplicationInstance.getActive().getLocale());
		}
		catch ( NoSuchMessageException ex )
		{
			return handleNoSuchMessage(name);
		}
	}

	// ========== UTILITY METHODS ==========

	private String handleNoSuchMessage(String name)
	{
		return "[MESSAGE NOT FOUND: " + name + "]";
	}

	// ========== GETTERS AND SETTERS ==========

	public MessageSource getMessageSource()
	{
		return messageSource;
	}

	public void setMessageSource(MessageSource messageSource)
	{
		this.messageSource = messageSource;
	}
}
