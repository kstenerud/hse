package org.stenerud.hse.standardsecurity.service.security;

/**
 * username/password based credentials.
 * 
 * @author Karl Stenerud
 */
public class PasswordCredentials implements Credentials
{
	// ========== INJECTED MEMBERS ==========
	private String username;
	private String password;

	// ========== GETTERS AND SETTERS ==========

	public String getPassword()
	{
		return password;
	}

	public void setPassword(String password)
	{
		this.password = password;
	}

	public String getUsername()
	{
		return username;
	}

	public void setUsername(String username)
	{
		this.username = username;
	}
}
