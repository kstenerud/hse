package org.stenerud.hse.base.ui.servlet;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.http.HttpSession;

import org.stenerud.hse.base.tool.MathHelper;
import org.stenerud.hse.base.ui.echo2.Echo2UserInterface;
import org.stenerud.hse.base.ui.echo2.UpdateTask;

import nextapp.echo2.app.ApplicationInstance;
import nextapp.echo2.app.TaskQueueHandle;
import nextapp.echo2.app.Window;
import nextapp.echo2.webcontainer.ContainerContext;
import nextapp.echo2.webrender.ClientProperties;

/**
 * The main application, subclassing from ApplicationInstance. <br>
 * This specialization provides some convenience methods, as well as task
 * queues. <br>
 * Use addTask() to add an update task to the queue, and removeTask() to remove
 * it once you're done. It's important to remove tasks that you no longer need
 * (such as when you leave a screen), or else you'll get unnecessary updates,
 * and most likely a resource leak.
 * 
 * @author Karl Stenerud
 */
public class Echo2Application extends ApplicationInstance
{
	private static final long serialVersionUID = 1L;

	// ========== CONSTANTS ==========

	/**
	 * The smallest polling interval allowed. Anything less than this gets set
	 * to the minimum.
	 */
	private static final int MINIMUM_POLLING_INTERVAL = 1000;

	public static final int BROWSER_INTERNET_EXPLORER = 0;
	public static final int BROWSER_FIREFOX = 1;
	public static final int BROWSER_MOZILLA = 2;
	public static final int BROWSER_KONQUEROR = 3;
	public static final int BROWSER_SAFARI = 4;
	public static final int BROWSER_OPERA = 5;

	// ========== INTERNAL CLASSES ==========

	/**
	 * Runnable implementation to update a task.
	 */
	private static class UpdateTaskRunnable implements Runnable
	{
		UpdateTask task;

		public UpdateTaskRunnable(UpdateTask task)
		{
			this.task = task;
		}

		public void run()
		{
			task.update();
		}
	}

	// ========== INJECTED MEMBERS ==========
	private Echo2UserInterface userInterface;

	// ========== PRIVATE MEMBERS ==========
	private boolean tasksEnabled = true;
	private TaskQueueHandle taskQueue;
	private Set updateTasks = new HashSet();
	private Map initialParameters;

	// ========== IMPLEMENTATION ==========

	public boolean isTasksEnabled()
	{
		return tasksEnabled;
	}

	public void setTasksEnabled(boolean tasksEnabled)
	{
		// System.out.println("setTasksEnabled " + tasksEnabled);
		if ( this.tasksEnabled != tasksEnabled )
		{
			this.tasksEnabled = tasksEnabled;
			updateTaskQueue();
		}
	}

	/**
	 * Add an update task to the active task list. <br>
	 * This method is used by UpdateTask and should not be called by the user.
	 * 
	 * @param task the task to add.
	 */
	public synchronized void addTask(UpdateTask task)
	{
		// System.out.println("add task " + task.getInterval());
		updateTasks.add(task);
		updateTaskQueue();
	}

	/**
	 * Remove a task from the active task list. <br>
	 * This method is used by UpdateTask and should not be called by the user.
	 * 
	 * @param task the task to remove.
	 */
	public synchronized void removeTask(UpdateTask task)
	{
		// System.out.println("remove task " + task.getInterval());
		updateTasks.remove(task);
		updateTaskQueue();
	}

	public synchronized void notifyTaskChanged()
	{
		updateTaskQueue();
	}

	/**
	 * Override hasQueuedTasks to send out updates. <br>
	 * This is a really ugly way to do things, but it's the way recommended by
	 * the Echo2 team. <br>
	 * Check each task, and if it is ready to be updated, schedule an update.
	 */
	public boolean hasQueuedTasks()
	{
		// System.out.println("hasQueuedTasks");
		if ( tasksEnabled )
		{
			for ( Iterator iter = getReadyTasks().iterator(); iter.hasNext(); )
			{
				UpdateTask task = (UpdateTask)iter.next();
				// System.out.println("Enqueueing task with interval " +
				// task.getInterval());
				enqueueTask(taskQueue, new UpdateTaskRunnable(task));
			}
		}
		return super.hasQueuedTasks();
	}

	/**
	 * Initialize the application. <br>
	 * This gets called by the Echo2 framework.
	 */
	public Window init()
	{
		ContainerContext containerContext = (ContainerContext)getContextProperty(ContainerContext.CONTEXT_PROPERTY_NAME);
		initialParameters = containerContext.getInitialRequestParameterMap();

		ClientProperties clientProperties = getClientProperties();
		if ( clientProperties.get(ClientProperties.LOCALES) != null )
		{
			Locale[] locales = (Locale[])clientProperties.get(ClientProperties.LOCALES);
			if ( null != locales && locales.length > 0 )
			{
				setLocale(locales[0]);
			}
		}

		userInterface.setSupportsPng(doesSupportPng());
		// Start the UI session.
		userInterface.beginSession();

		// Return the window, and let Echo2 start the render process.
		return userInterface.getWindow();
	}

	/**
	 * Get the underlying Http session.
	 * 
	 * @return the session.
	 */
	public HttpSession getSession()
	{
		return ((ContainerContext)getContextProperty(ContainerContext.CONTEXT_PROPERTY_NAME)).getSession();
	}

	public ClientProperties getClientProperties()
	{
		ContainerContext containerContext = (ContainerContext)getContextProperty(ContainerContext.CONTEXT_PROPERTY_NAME);
		return containerContext.getClientProperties();
	}

	/**
	 * Get initial parameters that were passed into the application through a
	 * URL.
	 * 
	 * @param parameter the name of the parameter.
	 * @return an array of all values having that parameter name.
	 */
	public String[] getInitialParameters(String parameter)
	{
		return (String[])initialParameters.get(parameter);
	}

	/**
	 * Get the first initial parameter with the specified name.
	 * 
	 * @param parameter the name of the parameter.
	 * @return the first parameter with the specified name.
	 */
	public String getInitialParameter(String parameter)
	{
		String[] params = getInitialParameters(parameter);
		if ( null == params || params.length == 0 )
		{
			return null;
		}
		return params[0];
	}

	// ========== UTILITY METHODS ==========

	private boolean doesSupportPng()
	{
		// Shamelessly stolen from ClientAnalyzerProcessor
		ContainerContext containerContext = (ContainerContext)getContextProperty(ContainerContext.CONTEXT_PROPERTY_NAME);
		ClientProperties clientProperties = containerContext.getClientProperties();
		String userAgent = clientProperties.getString(ClientProperties.NAVIGATOR_USER_AGENT).toLowerCase();

		boolean browserOpera = userAgent.indexOf("opera") != -1;
		boolean browserSafari = userAgent.indexOf("safari") != -1;
		boolean browserKonqueror = userAgent.indexOf("konqueror") != -1;

		// Note deceptive user agent fields:
		// - Konqueror and Safari UA fields contain "like Gecko"
		// - Opera UA field typically contains "MSIE"
		boolean deceptiveUserAgent = browserOpera || browserSafari || browserKonqueror;

		boolean browserMozilla = !deceptiveUserAgent && userAgent.indexOf("gecko") != -1;
		boolean browserInternetExplorer = !deceptiveUserAgent && userAgent.indexOf("msie") != -1;

		if ( browserOpera )
		{
			return true;
		}
		else if ( browserKonqueror )
		{
			return true;
		}
		else if ( browserSafari )
		{
			return true;
		}
		else if ( browserMozilla )
		{
			return true;
		}
		else if ( browserInternetExplorer )
		{
			if ( userAgent.indexOf("msie 7.") != -1 )
			{
				return true;
			}
			return false;
		}

		// Just assume true
		return true;
	}

	/**
	 * Update the polling interval for server push. <br>
	 * This sets the time between polls to the server when there are update
	 * tasks running to the GCD of all active tasks intervals.
	 */
	private void updateTaskQueue()
	{
		if ( null != taskQueue )
		{
			removeTaskQueue(taskQueue);
			taskQueue = null;
		}

		if ( tasksEnabled && updateTasks.size() > 0 )
		{
			// System.out.println("updating polling interval");
			TreeSet values = new TreeSet();
			for ( Iterator iter = updateTasks.iterator(); iter.hasNext(); )
			{
				UpdateTask task = (UpdateTask)iter.next();
				// System.out.println("Evaluating task interval " +
				// task.getInterval());
				values.add(new Long(task.getInterval()));
			}
			int pollingInterval = (int)MathHelper.gcd(values);
			if ( pollingInterval < MINIMUM_POLLING_INTERVAL )
			{
				pollingInterval = MINIMUM_POLLING_INTERVAL;
			}
			// System.out.println("Final polling interval = " + pollingInterval);

			taskQueue = createTaskQueue();

			ContainerContext containerContext = (ContainerContext)getContextProperty(ContainerContext.CONTEXT_PROPERTY_NAME);
			containerContext.setTaskQueueCallbackInterval(taskQueue, pollingInterval);
		}
	}

	/**
	 * Get a list of all tasks that are ready to run.
	 * 
	 * @return a list of ready tasks.
	 */
	private synchronized List getReadyTasks()
	{
		List readyTasks = new LinkedList();
		for ( Iterator iter = updateTasks.iterator(); iter.hasNext(); )
		{
			UpdateTask task = (UpdateTask)iter.next();
			if ( task.isReady() )
			{
				readyTasks.add(task);
			}
		}
		return readyTasks;
	}

	// ========== GETTERS AND SETTERS ==========

	public void setUserInterface(Echo2UserInterface userInterface)
	{
		this.userInterface = userInterface;
	}

	/**
	 * Get the current user interface.
	 * 
	 * @return the current user interface.
	 */
	public Echo2UserInterface getUserInterface()
	{
		return userInterface;
	}
}
