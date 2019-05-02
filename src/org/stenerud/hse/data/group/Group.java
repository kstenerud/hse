package org.stenerud.hse.data.group;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.stenerud.hse.data.security.Permission;
import org.stenerud.hse.data.security.PermissionLevel;

/**
 * A group has permissions that a user gains by membership to the group.
 * 
 * @author Karl Stenerud
 */
public class Group implements Comparable
{
	// ========== CONSTANTS ==========
	public static final int NAME_LENGTH = 255;

	// ========== PROPERTIES ==========
	private int id = -1;
	private String name;
	private boolean administrator;
	private Set permissionLevels = new HashSet();

	// ========== IMPLEMENTATION ==========

	/**
	 * Check if this group has the specified permission and at minimum the specified level.
	 * 
	 * @param permission the permission.
	 * @param minimumLevel the minimum level.
	 * @return true if the group has the permission at the required level.
	 */
	public boolean hasPermission(Permission permission, int minimumLevel)
	{
		// I could optimize this, but it's not a critical path.
		for ( Iterator iter = getPermissionLevels().iterator(); iter.hasNext(); )
		{
			PermissionLevel permissionLevel = (PermissionLevel)iter.next();
			if ( permissionLevel.getPermission().equals(permission) )
			{
				return permissionLevel.getLevel() >= minimumLevel;
			}
		}
		return false;
	}

	/**
	 * Add a permission level.
	 * 
	 * @param permissionLevel the permission level to add.
	 */
	public void addPermissionLevel(PermissionLevel permissionLevel)
	{
		getPermissionLevels().add(permissionLevel);
	}

	/**
	 * Set the level of a permission.
	 * 
	 * @param permission the permission.
	 * @param level the level.
	 */
	public void setPermissionLevel(Permission permission, int level)
	{
		for ( Iterator iter = getPermissionLevels().iterator(); iter.hasNext(); )
		{
			PermissionLevel permissionLevel = (PermissionLevel)iter.next();
			if ( permissionLevel.getPermission().equals(permission) )
			{
				permissionLevel.setLevel(level);
				return;
			}
		}
	}

	public int hashCode()
	{
		return this.getName().hashCode();
	}

	public boolean equals(Object o)
	{
		if ( !(o instanceof Group) )
		{
			return false;
		}
		return this.getName().equals(((Group)o).getName());
	}

	public int compareTo(Object o)
	{
		if ( !(o instanceof Group) )
		{
			return -1;
		}
		return this.getName().compareTo(((Group)o).getName());
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

	public Set getPermissionLevels()
	{
		return permissionLevels;
	}

	public void setPermissionLevels(Set permissionLevels)
	{
		this.permissionLevels = permissionLevels;
	}
}
