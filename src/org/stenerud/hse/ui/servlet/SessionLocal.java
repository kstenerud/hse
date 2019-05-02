package org.stenerud.hse.ui.servlet;

import javax.servlet.http.HttpSession;

import nextapp.echo2.app.ApplicationInstance;
import nextapp.echo2.webcontainer.ContainerContext;

/**
 * A session-local container inspired by ThreadLocal. <br>
 * Objects are maintained only for the current session.
 * 
 * @see ThreadLocal ThreadLocal
 * @author Karl Stenerud
 */
public class SessionLocal
{
	// ========== PRIVATE MEMBERS ==========
	private String uniqueId;

	// ========== IMPLEMENTATION ==========

	/**
	 * Constructor.
	 * 
	 * @param uniqueId an ID that is unique across all data that will be held in the same session.
	 */
	public SessionLocal(String uniqueId)
	{
		this.uniqueId = uniqueId;
	}

	public Object get()
	{
		return getSession().getAttribute(uniqueId);
	}

	public void set(Object value)
	{
		getSession().setAttribute(uniqueId, value);
	}

	private HttpSession getSession()
	{
		// Use ApplicationInstance to get the container context.
		ApplicationInstance application = ApplicationInstance.getActive();
		ContainerContext context = (ContainerContext)application
				.getContextProperty(ContainerContext.CONTEXT_PROPERTY_NAME);
		return context.getSession();
	}
}
