package org.stenerud.hse.base.ui.servlet;

import javax.servlet.ServletException;

import nextapp.echo2.app.ApplicationInstance;
import nextapp.echo2.webcontainer.WebContainerServlet;

/**
 * Servlet providing the Echo2 application. <br>
 * The servlet will search the Spring context for a single class that is an
 * instance of ApplicationInstance, and provide that to its superclass. <br>
 * IMPORTANT: There must be ONLY ONE class extending ApplicationInstance in the
 * bean context!
 * 
 * @author Karl Stenerud
 */
public class Echo2Servlet extends WebContainerServlet
{
	private static final long serialVersionUID = 1L;

	// ========== PRIVATE MEMBERS ==========
	SpringServletHelper servletHelper;
	private String applicationName;

	// ========== IMPLEMENTATION ==========

	/**
	 * Returns the ApplicationInstance object defined within the Spring context.
	 */
	public ApplicationInstance newApplicationInstance()
	{
		return (ApplicationInstance)servletHelper.getBean(applicationName);
	}

	public void init() throws ServletException
	{
		super.init();

		servletHelper = new SpringServletHelper(getServletContext());

		// Initialize the base application
		applicationName = servletHelper.getUniqueInstanceName(ApplicationInstance.class);
	}
}
