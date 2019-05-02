package org.stenerud.hse.base.tool;

import org.springframework.beans.factory.ListableBeanFactory;

/**
 * Helper class to add some Spring utility.
 * 
 * @author Karl Stenerud
 */
public class SpringHelper
{
	// ========== PRIVATE MEMBERS ==========
	private ListableBeanFactory beanFactory;

	// ========== IMPLEMENTATION ==========

	/**
	 * Constructor.
	 * 
	 * @param beanFactory the bean factory.
	 */
	public SpringHelper(ListableBeanFactory beanFactory)
	{
		this.beanFactory = beanFactory;
	}

	/**
	 * Get a bean from the Spring framework.
	 * 
	 * @param name the name of the bean.
	 * @return an instance of the bean.
	 */
	public Object getBean(String name)
	{
		return beanFactory.getBean(name);
	}

	/**
	 * Get a unique instance of a particular class. There must be only one
	 * instance of that class in the spring configuration.
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
	 * Get the name of a unique instance of a particular class. There must be
	 * only one instance of that class in the spring configuration.
	 * 
	 * @param clazz the class to find an instance of.
	 * @return the name of that instance.
	 * @throws IllegalArgumentException if there is <> 1 instance of that class.
	 */
	public String getUniqueInstanceName(Class clazz)
	{
		String[] beanNames = beanFactory.getBeanNamesForType(clazz);
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

	public ListableBeanFactory getBeanFactory()
	{
		return beanFactory;
	}
}
