package org.stenerud.hse.base.ui.echo2.screen;

/**
 * Superclass for screen and category descriptions.
 * 
 * @author Karl Stenerud
 */
public abstract class BaseDescription
{
	private ScreenAccess access = new ScreenAccess()
	{
		public boolean hasAccess()
		{
			return false;
		}
	};
	private String message = "";

	public boolean hasAccess()
	{
		return access.hasAccess();
	}

	public ScreenAccess getAccess()
	{
		return access;
	}

	public void setAccess(ScreenAccess access)
	{
		this.access = access;
	}

	public String getMessage()
	{
		return message;
	}

	public void setMessage(String message)
	{
		this.message = message;
	}
}
