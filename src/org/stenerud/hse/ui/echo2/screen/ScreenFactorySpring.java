package org.stenerud.hse.ui.echo2.screen;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;

/**
 * Factory class for generating the main application screens.
 * 
 * @author Karl Stenerud
 */
public class ScreenFactorySpring implements BeanFactoryAware, ScreenFactory
{
	// ========== INJECTED MEMBERS ==========
	private BeanFactory beanFactory;

	// ========== IMPLEMENTATION ==========

	public Screen createScreen(String name)
	{
		return (Screen)beanFactory.getBean(name);
	}

	// ========== GETTERS AND SETTERS ==========

	/**
	 * Bean factory setter called automagically by Spring upon instantiation.
	 * 
	 * @param beanFactory the bean factory.
	 */
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException
	{
		this.beanFactory = beanFactory;
	}
}
