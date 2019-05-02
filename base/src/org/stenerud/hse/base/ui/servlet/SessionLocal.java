package org.stenerud.hse.base.ui.servlet;

import javax.servlet.http.HttpSession;

/**
 * A session-local container inspired by ThreadLocal. <br>
 * Objects are maintained only for the current session.
 * 
 * @see ThreadLocal ThreadLocal
 * @author Karl Stenerud
 */
public abstract class SessionLocal
{
	// ========== INJECTED MEMBERS ==========
	private String uniqueId;

	// ========== IMPLEMENTATION ==========

	/**
	 * Get the current session.
	 * 
	 * @return the current session.
	 */
	protected abstract HttpSession getSession();

	/**
	 * Get the object from the session.
	 * 
	 * @return the objet.
	 */
	public Object get()
	{
		return getSession().getAttribute(uniqueId);
	}

	/**
	 * Set the object onto the session.
	 * 
	 * @param value the object.
	 */
	public void set(Object value)
	{
		getSession().setAttribute(uniqueId, value);
	}

	// ========== GETTERS AND SETTERS ==========

	public String getUniqueId()
	{
		return uniqueId;
	}

	/**
	 * Set this session-local's unique ID.
	 * 
	 * @param uniqueId an ID that is unique across all data that will be held in
	 *           the same session.
	 */
	public void setUniqueId(String uniqueId)
	{
		this.uniqueId = uniqueId;
	}
}
