package org.stenerud.hse.standardsecurity.data.security;

/**
 * A permission represents permission to do something.
 * 
 * @author Karl Stenerud
 */
public class Permission implements Comparable
{
	// ========== PROPERTIES ==========
	private int id = -1;
	private String name;

	// ========== IMPLEMENTATION ==========

	public int hashCode()
	{
		return getId();
	}

	public boolean equals(Object o)
	{
		if ( !(o instanceof Permission) )
		{
			return false;
		}
		return this.getId() == ((Permission)o).getId();
	}

	public int compareTo(Object o)
	{
		if ( !(o instanceof Permission) )
		{
			return -1;
		}
		return this.getId() - ((Permission)o).getId();
	}

	public String toString()
	{
		return getName();
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

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}
}
