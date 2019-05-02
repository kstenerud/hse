package org.stenerud.hse.base.ui.servlet;

import javax.servlet.http.HttpSession;

import nextapp.echo2.app.ApplicationInstance;
import nextapp.echo2.webcontainer.ContainerContext;

/**
 * Echo2 implementation of SessionLocal.
 * 
 * @author Karl Stenerud
 */
public class Echo2SessionLocal extends SessionLocal
{
	// ========== IMPLEMENTATION ==========

	protected HttpSession getSession()
	{
		// Use ApplicationInstance to get the container context.
		ApplicationInstance application = ApplicationInstance.getActive();
		ContainerContext context = (ContainerContext)application
				.getContextProperty(ContainerContext.CONTEXT_PROPERTY_NAME);
		return context.getSession();
	}
}
