package org.stenerud.hse.standardsecurity.data.security;

/**
 * A permission level detemines the degree of permissiveness.
 * 
 * @author Karl Stenerud
 */
public class PermissionLevel
{
	// ========== PROPERTIES ==========
	private int id = -1;
	private Permission permission = new Permission();
	private int level;

	// ========== IMPLEMENTATION ==========

	public int hashCode()
	{
		return getPermission().hashCode();
	}

	public boolean equals(Object o)
	{
		if ( !(o instanceof PermissionLevel) )
		{
			return false;
		}
		PermissionLevel that = (PermissionLevel)o;
		return this.getPermission().equals(that.getPermission()) && this.getLevel() == that.getLevel();
	}

	public int compareTo(Object o)
	{
		if ( !(o instanceof PermissionLevel) )
		{
			return -1;
		}
		PermissionLevel that = (PermissionLevel)o;
		int result = this.getPermission().compareTo(that.getPermission());
		if ( result == 0 )
		{
			result = this.getLevel() - that.getLevel();
		}
		return result;
	}

	public String toString()
	{
		return getPermission().toString() + ": " + getLevel();
	}

	// ========== GETTERS AND SETTERS ==========

	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
	}

	public int getLevel()
	{
		return level;
	}

	public void setLevel(int level)
	{
		this.level = level;
	}

	public Permission getPermission()
	{
		return permission;
	}

	public void setPermission(Permission permission)
	{
		this.permission = permission;
	}
}
