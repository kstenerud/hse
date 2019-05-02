package org.stenerud.hse.base.tool;

import java.util.List;

import org.springframework.beans.PropertyValue;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.RuntimeBeanReference;

public class SpringPropertyHelper
{

	private Object internalGetPropertyValue(BeanDefinition beanDefinition, String beanName, String propertyName,
			Class type, boolean allowNotExist)
	{
		PropertyValue propertyValue = beanDefinition.getPropertyValues().getPropertyValue(propertyName);
		if ( null == propertyValue )
		{
			if ( !allowNotExist )
			{
				throw new IllegalArgumentException(beanDefinition.getResourceDescription() + ": " + beanName
						+ " must define property \"" + propertyName + "\"");
			}
			return null;
		}
		Object value = propertyValue.getValue();
		if ( !type.isAssignableFrom(type) )
		{
			throw new IllegalArgumentException(beanDefinition.getResourceDescription() + ": " + beanName + ": property \""
					+ propertyName + "\" must be of type " + type + " (was type " + value.getClass() + ")");
		}
		return value;
	}

	public Object getProperty(BeanDefinition beanDefinition, String beanName, String propertyName)
	{
		return internalGetPropertyValue(beanDefinition, beanName, propertyName, Object.class, false);
	}

	public Object getProperty(BeanDefinition beanDefinition, String beanName, String propertyName, boolean allowNotExist)
	{
		return internalGetPropertyValue(beanDefinition, beanName, propertyName, Object.class, allowNotExist);
	}

	public String getString(BeanDefinition beanDefinition, String beanName, String propertyName)
	{
		return (String)internalGetPropertyValue(beanDefinition, beanName, propertyName, String.class, false);
	}

	public String getString(BeanDefinition beanDefinition, String beanName, String propertyName, boolean allowNotExist)
	{
		return (String)internalGetPropertyValue(beanDefinition, beanName, propertyName, String.class, allowNotExist);
	}

	public List getList(BeanDefinition beanDefinition, String beanName, String propertyName)
	{
		return (List)internalGetPropertyValue(beanDefinition, beanName, propertyName, List.class, false);
	}

	public List getList(BeanDefinition beanDefinition, String beanName, String propertyName, boolean allowNotExist)
	{
		return (List)internalGetPropertyValue(beanDefinition, beanName, propertyName, List.class, allowNotExist);
	}

	public RuntimeBeanReference getBeanReference(BeanDefinition beanDefinition, String beanName, String propertyName)
	{
		return (RuntimeBeanReference)internalGetPropertyValue(beanDefinition, beanName, propertyName,
				RuntimeBeanReference.class, false);
	}

	public RuntimeBeanReference getBeanReference(BeanDefinition beanDefinition, String beanName, String propertyName,
			boolean allowNotExist)
	{
		return (RuntimeBeanReference)internalGetPropertyValue(beanDefinition, beanName, propertyName,
				RuntimeBeanReference.class, allowNotExist);
	}
}
