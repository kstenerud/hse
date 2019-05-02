package org.stenerud.hse.base.data;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.stenerud.hse.base.tool.SpringPropertyHelper;

/**
 * Searches for HibernateConfigurations and HibernateConfigurationInterests,
 * and automatically matches them to each other, adding hibernate mappings and
 * setting configurations.
 * 
 * @author Karl Stenerud
 */
public class HibernateConfigurer implements BeanFactoryPostProcessor
{
	// ========== PRIVATE MEMBERS ==========
	private Log log = LogFactory.getLog(this.getClass());

	// ========== IMPLEMENTATION ==========

	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException
	{
		SpringPropertyHelper propsHelper = new SpringPropertyHelper();

		// Check for multiple configurers.
		if ( beanFactory.getBeanNamesForType(HibernateConfigurer.class, true, false).length > 1 )
		{
			log.error("Found multiple instances of HibernateConfigurer");
			String[] names = beanFactory.getBeanNamesForType(HibernateConfigurer.class, true, false);
			for ( int i = 0; i < names.length; i++ )
			{
				BeanDefinition bean = beanFactory.getBeanDefinition(names[i]);
				log.error(names[i] + " in " + bean.getResourceDescription());
			}
			throw new IllegalArgumentException("Can only have one instance of HibernateConfigurer");
		}

		// Get all configurations.
		Map configurations = new HashMap();
		String[] beanNames = beanFactory.getBeanNamesForType(HibernateConfiguration.class, false, false);
		for ( int i = 0; i < beanNames.length; i++ )
		{
			configurations.put(beanNames[i], beanFactory.getBeanDefinition(beanNames[i]));
		}

		// Go through each interest.
		String[] interestNames = beanFactory.getBeanNamesForType(HibernateConfigurationInterest.class, true, false);
		for ( int i = 0; i < interestNames.length; i++ )
		{
			log.debug("Examining interest " + interestNames[i]);
			BeanDefinition interest = beanFactory.getBeanDefinition(interestNames[i]);
			RuntimeBeanReference sessionFactoryRef = propsHelper.getBeanReference(interest, interestNames[i],
					"sessionFactory");
			RuntimeBeanReference disconnectHandlerFactoryRef = propsHelper.getBeanReference(interest, interestNames[i],
					"disconnectHandlerFactory");
			BeanDefinition sessionFactory = null == sessionFactoryRef ? null : beanFactory
					.getBeanDefinition(sessionFactoryRef.getBeanName());

			// For each interest, get the list of matchers.
			for ( Iterator matchIterator = propsHelper.getList(interest, interestNames[i], "configurationMatchers")
					.iterator(); matchIterator.hasNext(); )
			{
				Pattern pattern = Pattern.compile((String)matchIterator.next());
				// With each matcher, look for matches on each mapping list.
				for ( Iterator configurationIterator = configurations.entrySet().iterator(); configurationIterator
						.hasNext(); )
				{
					Map.Entry entry = (Map.Entry)configurationIterator.next();
					String configName = (String)entry.getKey();
					if ( pattern.matcher(configName).matches() )
					{
						log.debug("Interest pattern " + pattern.pattern() + " matched configuration " + configName);
						BeanDefinition configuration = (BeanDefinition)entry.getValue();

						if ( null != sessionFactoryRef )
						{
							log.debug("Adding hibernate mappings from " + configName + " to "
									+ sessionFactoryRef.getBeanName());
							// If there's a session bean name specified, add entries
							// to it.
							List mappingResources = propsHelper.getList(sessionFactory, sessionFactoryRef.getBeanName(),
									"mappingResources");
							mappingResources.addAll(propsHelper.getList(configuration, configName, "mappingResources"));

							// Add session factory and disconnect handler references
							// to all transaction enabled DAOs.
							List transactionDAOs = propsHelper.getList(configuration, configName, "transactionDAOs");
							for ( Iterator daoIterator = transactionDAOs.iterator(); daoIterator.hasNext(); )
							{
								RuntimeBeanReference proxyRef = (RuntimeBeanReference)daoIterator.next();
								log.debug("Configuring " + proxyRef.getBeanName());
								BeanDefinition proxy = beanFactory.getBeanDefinition(proxyRef.getBeanName());
								BeanDefinitionHolder daoRef = (BeanDefinitionHolder)propsHelper.getProperty(proxy, proxyRef
										.getBeanName(), "target");
								BeanDefinition dao = daoRef.getBeanDefinition();
								dao.getPropertyValues().addPropertyValue("sessionFactory", sessionFactoryRef);
								dao.getPropertyValues().addPropertyValue("disconnectHandlerFactory",
										disconnectHandlerFactoryRef);
							}

							RuntimeBeanReference transactionManagerRef = propsHelper.getBeanReference(configuration,
									configName, "transactionManager");
							if ( null != transactionManagerRef )
							{
								log.debug("Configuring " + configName + "'s transaction manager");
								BeanDefinition transactionManager = beanFactory.getBeanDefinition(transactionManagerRef
										.getBeanName());
								transactionManager.getPropertyValues().addPropertyValue("sessionFactory", sessionFactoryRef);
								transactionManager.getPropertyValues().addPropertyValue("disconnectHandlerFactory",
										disconnectHandlerFactoryRef);
							}
						}

						// Remove the mapping list from the available pool.
						configurationIterator.remove();
					}
				}
			}
		}
	}
}
