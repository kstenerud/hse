package org.stenerud.hse.base.ui.servlet;

import javax.servlet.ServletContext;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.stenerud.hse.base.tool.SpringHelper;

/**
 * Helper class to add Spring support to a servlet.
 * 
 * @author Karl Stenerud
 */
public class SpringServletHelper extends SpringHelper
{
	/**
	 * Constructor.
	 * 
	 * @param servletContext the servlet context.
	 */
	public SpringServletHelper(ServletContext servletContext)
	{
		super(WebApplicationContextUtils.getWebApplicationContext(servletContext));
	}

	public ApplicationContext getApplicationContext()
	{
		return (ApplicationContext)getBeanFactory();
	}
}
