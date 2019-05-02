package org.stenerud.hse.base.ui.echo2.screen;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.core.io.Resource;
import org.stenerud.hse.base.tool.AttributeHelper;
import org.stenerud.hse.base.tool.MethodCaller;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Gets screen definitions from an XML file and presents them in a hierarchical
 * way.
 * 
 * @author Karl Stenerud
 */
public class ScreenManager implements BeanFactoryAware
{
	// ========== CONSTANTS ==========
	private static final long DEFAULT_CHECK_INTERVAL = 600000; // 60 seconds

	// ========== INJECTED MEMBERS ==========
	private File file;
	private long checkInterval = DEFAULT_CHECK_INTERVAL;
	private BeanFactory beanFactory;

	// ========== PRIVATE MEMBERS ==========
	private long lastChecked = 0;
	private long lastModified = 0;
	private List screens = new LinkedList();
	private String initialScreenName;
	private String mainScreenName;

	private class SimpleScreenAccess implements ScreenAccess
	{
		private boolean access;

		public boolean hasAccess()
		{
			return access;
		}

		public void setAccess(boolean access)
		{
			this.access = access;
		}
	}

	private class PropertyScreenAccess implements ScreenAccess
	{
		private BeanWrapper bean;
		private String property = "";
		private Pattern match = Pattern.compile(String.valueOf(true));

		public boolean hasAccess()
		{
			return match.matcher(bean.getPropertyValue(property).toString()).matches();
		}

		public void setBean(Object bean)
		{
			this.bean = new BeanWrapperImpl(bean);
		}

		public void setMatch(String match)
		{
			this.match = Pattern.compile(match);
		}

		public void setProperty(String property)
		{
			this.property = property;
		}
	}

	private class MethodScreenAccess implements ScreenAccess
	{
		private Object bean;
		private String methodName = "";
		private Pattern match = Pattern.compile(String.valueOf(true));
		private List parameters = new LinkedList();

		public boolean hasAccess()
		{
			MethodCaller caller = new MethodCaller();
			return match.matcher(caller.callMethod(bean, methodName, parameters).toString()).matches();
		}

		public void setBean(Object bean)
		{
			this.bean = bean;
		}

		public void setMatch(String match)
		{
			this.match = Pattern.compile(match);
		}

		public void setMethodName(String methodName)
		{
			this.methodName = methodName;
		}

		public void addParameter(String parameter)
		{
			parameters.add(parameter);
		}
	}

	private class XmlHandler extends DefaultHandler
	{
		private LinkedList queue = new LinkedList();

		public void endElement(String uri, String localName, String qName) throws SAXException
		{
			if ( "category".equals(qName) || "screen".equals(qName) )
			{
				if ( queue.size() > 0 )
				{
					queue.removeLast();
				}
			}
		}

		public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException
		{
			AttributeHelper attributeHelper = new AttributeHelper(attributes);
			if ( "category".equals(qName) )
			{
				handleCategory(attributeHelper);
			}
			else if ( "screen".equals(qName) )
			{
				handleScreen(attributeHelper);
			}
			else if ( "accessible".equals(qName) )
			{
				handleAccessible(attributeHelper);
			}
			else if ( "accessible-method".equals(qName) )
			{
				handleAccessibleMethod(attributeHelper);
			}
			else if ( "accessible-property".equals(qName) )
			{
				handleAccessibleProperty(attributeHelper);
			}
			else if ( "parameter".equals(qName) )
			{
				handleParameter(attributeHelper);
			}
			else if ( "initial-screen".equals(qName) )
			{
				handleInitialScreen(attributeHelper);
			}
			else if ( "main-screen".equals(qName) )
			{
				handleMainScreen(attributeHelper);
			}
			else if ( "screens".equals(qName) )
			{
				// Ignore
			}
			else
			{
				throw new IllegalArgumentException(qName + ": unknown element");
			}
		}

		private void addChild(BaseDescription child)
		{
			if ( queue.size() == 0 )
			{
				screens.add(child);
				queue.add(child);
			}
			else
			{
				CategoryDescription desc = (CategoryDescription)queue.getLast();
				desc.children.add(child);
				queue.add(child);
			}
		}

		private void handleCategory(AttributeHelper attributes)
		{
			CategoryDescription desc = new CategoryDescription();
			desc.setMessage(attributes.getString("message"));
			addChild(desc);
		}

		private void handleScreen(AttributeHelper attributes)
		{
			ScreenDescription desc = new ScreenDescription();
			desc.setMessage(attributes.getString("message"));
			desc.screenName = attributes.getString("bean");
			addChild(desc);
		}

		private void handleAccessible(AttributeHelper attributes)
		{
			BaseDescription desc = (BaseDescription)queue.getLast();
			SimpleScreenAccess access = new SimpleScreenAccess();
			access.setAccess(attributes.getBoolean("value"));
			desc.setAccess(access);
		}

		private void handleAccessibleProperty(AttributeHelper attributes)
		{
			BaseDescription desc = (BaseDescription)queue.getLast();
			PropertyScreenAccess access = new PropertyScreenAccess();
			access.setBean(beanFactory.getBean(attributes.getString("bean")));
			access.setProperty(attributes.getString("property"));
			access.setMatch(attributes.getString("match", String.valueOf(true)));
			desc.setAccess(access);
		}

		private void handleAccessibleMethod(AttributeHelper attributes)
		{
			BaseDescription desc = (BaseDescription)queue.getLast();
			MethodScreenAccess access = new MethodScreenAccess();
			access.setBean(beanFactory.getBean(attributes.getString("bean")));
			access.setMethodName(attributes.getString("method"));
			access.setMatch(attributes.getString("match", String.valueOf(true)));
			desc.setAccess(access);
		}

		private void handleParameter(AttributeHelper attributes)
		{
			BaseDescription desc = (BaseDescription)queue.getLast();
			MethodScreenAccess access = (MethodScreenAccess)desc.getAccess();
			access.addParameter(attributes.getString("value"));
		}

		private void handleInitialScreen(AttributeHelper attributes)
		{
			initialScreenName = attributes.getString("bean");
		}

		private void handleMainScreen(AttributeHelper attributes)
		{
			mainScreenName = attributes.getString("bean");
		}
	}

	// ========== IMPLEMENTATION ==========

	public List getScreens()
	{
		checkRefresh();
		return screens;
	}

	public Screen getScreen(String name)
	{
		return (Screen)beanFactory.getBean(name);
	}

	public Screen getInitialScreen()
	{
		checkRefresh();
		return (Screen)beanFactory.getBean(initialScreenName);
	}

	public Screen getMainScreen()
	{
		checkRefresh();
		return (Screen)beanFactory.getBean(mainScreenName);
	}

	// ========== UTILITY METHODS ==========

	/**
	 * Load the xml file.
	 * 
	 * @param inputStream the stream to load the xml file from. If null, it
	 *           loads from the member "file".
	 */
	private void loadXml(InputStream inputStream)
	{
		screens.clear();
		try
		{
			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser saxParser = factory.newSAXParser();
			if ( null == inputStream )
			{
				saxParser.parse(file, new XmlHandler());
			}
			else
			{
				saxParser.parse(inputStream, new XmlHandler());
			}
		}
		catch ( SAXException ex )
		{
			throw new RuntimeException(ex);
		}
		catch ( ParserConfigurationException ex )
		{
			throw new RuntimeException(ex);
		}
		catch ( IOException ex )
		{
			throw new RuntimeException(ex);
		}
	}

	/**
	 * Check if the resource file has changed, and reload if necessary. <br>
	 * This will only do an actual check once every check interval, and only if
	 * the resource is a file on the filesystem and checkInterval is > 0
	 */
	private void checkRefresh()
	{
		if ( null != file && checkInterval > 0 )
		{
			long nowTime = new Date().getTime();
			if ( nowTime - lastChecked > checkInterval )
			{
				if ( file.lastModified() != lastModified )
				{
					loadXml(null);
				}
				lastChecked = nowTime;
			}
		}
	}

	// ========== GETTERS AND SETTERS ==========

	/**
	 * Get the interval that the resource is checked for changes.
	 * 
	 * @return the interval in milliseconds.
	 */
	public long getCheckInterval()
	{
		return checkInterval;
	}

	/**
	 * Set the interval that the resource is checked for changes.
	 * 
	 * @param checkInterval the interval in milliseconds.
	 */
	public void setCheckInterval(long checkInterval)
	{
		this.checkInterval = checkInterval;
	}

	public void setResource(Resource resource) throws IOException
	{
		// Try to get the resource as a file.
		file = resource.getFile();
		if ( null == file )
		{
			// If it's not a file, get the input stream and load from there.
			// There will be no refreshing if this happens.
			loadXml(resource.getInputStream());
		}
	}

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
