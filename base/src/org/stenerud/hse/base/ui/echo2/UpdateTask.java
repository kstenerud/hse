package org.stenerud.hse.base.ui.echo2;

import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.stenerud.hse.base.ui.servlet.Echo2Application;

import nextapp.echo2.app.event.ActionEvent;
import nextapp.echo2.app.event.ActionListener;

/**
 * TODO: Make a factory, remove direct link to application. Works with
 * Echo2Application to schedule recurring server push events. <br>
 * Upon calling start(), the task will hook into the application's server
 * polling mechanism, and will notify all listeners each time it is updated.
 * 
 * @author Karl Stenerud
 * @see Echo2Application Echo2Application
 */
public class UpdateTask
{
	// ========== PRIVATE MEMBERS ==========
	private List eventListeners = new LinkedList();
	private long interval;
	private long nextUpdate;

	// ========== IMPLEMENTATION ==========

	/**
	 * Constructor. <br>
	 * Note: The interval should be set to a minimum of 500 ms to ensure smooth
	 * operation.
	 * 
	 * @param interval the interval between task updates in milliseconds.
	 * @see Echo2Application Echo2Application
	 */
	public UpdateTask(long interval)
	{
		this.interval = interval;
	}

	/**
	 * Update this task. <br>
	 * This is called from within Echo2Application and should not be called by
	 * the user.
	 * 
	 * @see Echo2Application Echo2Application
	 */
	public void update()
	{
		long currentTime = new Date().getTime();
		for ( Iterator iter = eventListeners.iterator(); iter.hasNext(); )
		{
			((ActionListener)iter.next()).actionPerformed(new ActionEvent(this, ""));
		}
		nextUpdate = currentTime + interval;
	}

	/**
	 * Start this task. <br>
	 * When started, this task's update will be called repeatedly after the
	 * interval period.
	 */
	public void start()
	{
		nextUpdate = new Date().getTime() + interval;
		BaseApplicationHelper.getActiveApplication().addTask(this);
	}

	/**
	 * Stop this task.
	 */
	public void stop()
	{
		BaseApplicationHelper.getActiveApplication().removeTask(this);
	}

	/**
	 * Check if this task is ready to be updated. <br>
	 * This method is used internally by Echo2Application.
	 * 
	 * @return true if the task is ready.
	 * @see Echo2Application Echo2Application
	 */
	public boolean isReady()
	{
		return new Date().getTime() >= nextUpdate;
	}

	/**
	 * Add a listener to this task.
	 * 
	 * @param listener the listener to add.
	 */
	public void addListener(ActionListener listener)
	{
		eventListeners.add(listener);
	}

	/**
	 * Remove a listener from this task.
	 * 
	 * @param listener the listener to remove.
	 */
	public void removeListener(ActionListener listener)
	{
		eventListeners.remove(listener);
	}

	/**
	 * Get the interval between updates.
	 * 
	 * @return the interval in milliseconds.
	 */
	public long getInterval()
	{
		return interval;
	}

	/**
	 * Set the interval between updtes.
	 * 
	 * @param interval the interval in milliseconds.
	 */
	public void setInterval(long interval)
	{
		this.interval = interval;
		BaseApplicationHelper.getActiveApplication().notifyTaskChanged();
	}
}
