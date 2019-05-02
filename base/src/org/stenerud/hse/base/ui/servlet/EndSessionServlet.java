package org.stenerud.hse.base.ui.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.stenerud.hse.base.tool.PropertyHelper;

/**
 * Servlet that invalidates the current session and redirects the user to
 * "endSession.html".
 * 
 * @author Karl Stenerud
 */
public class EndSessionServlet extends HttpServlet
{
	private static final long serialVersionUID = 1L;

	// ========== INJECTED MEMBERS ==========
	private PropertyHelper properties;

	// ========== IMPLEMENTATION ==========

	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException
	{
		try
		{
			req.getSession().invalidate();
			resp.sendRedirect(properties.getString("endSession.redirect", "endSession.html"));
		}
		catch ( Exception ex )
		{
			throw new ServletException(ex);
		}
	}

	public void init() throws ServletException
	{
		super.init();

		SpringServletHelper servletHelper = new SpringServletHelper(getServletContext());
		properties = (PropertyHelper)servletHelper.getBean("applicationProperties");
	}

	// ========== GETTERS AND SETTERS ==========

	public PropertyHelper getProperties()
	{
		return properties;
	}

	public void setProperties(PropertyHelper properties)
	{
		this.properties = properties;
	}
}
