package org.stenerud.hse.data.user;

import org.stenerud.hse.data.group.Group;

/**
 * A system user.
 * 
 * @author Karl Stenerud
 */
public class User implements Comparable
{
	// ========== CONSTANTS ==========
	public static final int NAME_LENGTH = 20;
	public static final int PASSWORD_LENGTH = 20;

	// ========== PROPERTIES ==========
	private int id = -1;
	private String name = "";
	private String password = "";
	private boolean administrator;
	private Group userGroup;

	// ========== IMPLEMENTATION ==========

	public int hashCode()
	{
		return getName().hashCode();
	}

	public boolean equals(Object o)
	{
		if ( !(o instanceof User) )
		{
			return false;
		}
		return this.getName().equals(((User)o).getName());
	}

	public int compareTo(Object o)
	{
		if ( !(o instanceof User) )
		{
			return -1;
		}
		return this.getName().compareTo(((User)o).getName());
	}

	public String toString()
	{
		return getName();
	}

	// ========== GETTERS AND SETTERS ==========

	public boolean isAdministrator()
	{
		return administrator;
	}

	public void setAdministrator(boolean administrator)
	{
		this.administrator = administrator;
	}

	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getPassword()
	{
		return password;
	}

	public void setPassword(String password)
	{
		this.password = password;
	}

	public Group getUserGroup()
	{
		return userGroup;
	}

	public void setUserGroup(Group userGroup)
	{
		this.userGroup = userGroup;
	}
}
