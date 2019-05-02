package org.stenerud.hse.standardsecurity.servlet;

import org.stenerud.hse.base.ui.servlet.SessionLocal;
import org.stenerud.hse.standardsecurity.data.user.User;
import org.stenerud.hse.standardsecurity.service.security.CurrentUser;

/**
 * Session local implementation of the current user. <br>
 * This allows one logged in user per session.
 * 
 * @author Karl Stenerud
 */
public class CurrentUserSessionLocal implements CurrentUser
{
	// ========== INJECTED MEMBERS ==========
	private SessionLocal sessionLocal;

	// ========== IMPLEMENTATION ==========

	/**
	 * Get the current user.
	 * 
	 * @return the current user.
	 */
	public User get()
	{
		return (User)sessionLocal.get();
	}

	/**
	 * Set the current user.
	 * 
	 * @param user the current user.
	 */
	public void set(User user)
	{
		sessionLocal.set(user);
	}

	// ========== GETTERS AND SETTERS ==========

	public SessionLocal getSessionLocal()
	{
		return sessionLocal;
	}

	public void setSessionLocal(SessionLocal sessionLocal)
	{
		this.sessionLocal = sessionLocal;
		sessionLocal.setUniqueId(this.getClass().getName());
	}
}
