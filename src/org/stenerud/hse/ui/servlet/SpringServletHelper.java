package org.stenerud.hse.ui.servlet;

import javax.servlet.ServletContext;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * Helper class to add Spring support to a servlet.
 * 
 * @author Karl Stenerud
 */
public class SpringServletHelper
{
	// ========== PRIVATE MEMBERS ==========
	private ApplicationContext context;

	// ========== IMPLEMENTATION ==========

	/**
	 * Constructor.
	 * 
	 * @param servletContext the servlet context.
	 */
	public SpringServletHelper(ServletContext servletContext)
	{
		context = WebApplicationContextUtils.getWebApplicationContext(servletContext);
	}

	/**
	 * Get the Spring application context.
	 * 
	 * @return the Spring application context.
	 */
	public ApplicationContext getApplicationContext()
	{
		return context;
	}

	/**
	 * Get a bean from the Spring framework.
	 * 
	 * @param name the name of the bean.
	 * @return an instance of the bean.
	 */
	public Object getBean(String name)
	{
		return context.getBean(name);
	}

	/**
	 * Get a unique instance of a particular class. There must be only one instance of that class in the spring
	 * configuration.
	 * 
	 * @param clazz the class to find an instance of.
	 * @return the instance.
	 * @throws IllegalArgumentException if there is <> 1 instance of that class.
	 */
	public Object getUniqueInstance(Class clazz)
	{
		return getBean(getUniqueInstanceName(clazz));
	}

	/**
	 * Get the name of a unique instance of a particular class. There must be only one instance of that class in the
	 * spring configuration.
	 * 
	 * @param clazz the class to find an instance of.
	 * @return the name of that instance.
	 * @throws IllegalArgumentException if there is <> 1 instance of that class.
	 */
	public String getUniqueInstanceName(Class clazz)
	{
		String[] beanNames = context.getBeanNamesForType(clazz);
		if ( beanNames == null || beanNames.length == 0 )
		{
			throw new IllegalArgumentException("There must be one bean of type " + clazz.getName()
					+ " declared in the application context.");
		}
		if ( beanNames.length > 1 )
		{
			throw new IllegalArgumentException("There must be only one bean of type " + clazz.getName()
					+ " declared in the application context.");
		}
		return beanNames[0];
	}
}
