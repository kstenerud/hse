package org.stenerud.hse.ui.servlet;

import org.stenerud.hse.data.user.User;
import org.stenerud.hse.security.CurrentUser;

/**
 * Session local implementation of the current user. <br>
 * This allows one logged in user per session.
 * 
 * @author Karl Stenerud
 */
public class CurrentUserSessionLocal implements CurrentUser
{
	// ========== PRIVATE MEMBERS ==========
	private static SessionLocal localInstance = new SessionLocal(User.class.getName());

	// ========== IMPLEMENTATION ==========

	public User get()
	{
		return (User)localInstance.get();
	}

	public void set(User user)
	{
		localInstance.set(user);
	}
}
