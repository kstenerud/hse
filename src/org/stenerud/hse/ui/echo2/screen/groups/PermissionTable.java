package org.stenerud.hse.ui.echo2.screen.groups;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import nextapp.echo2.app.Component;
import nextapp.echo2.app.SelectField;

import org.stenerud.hse.data.security.Permission;
import org.stenerud.hse.data.security.PermissionLevel;
import org.stenerud.hse.security.PermissionLevels;
import org.stenerud.hse.ui.Messages;
import org.stenerud.hse.ui.echo2.components.StaticTable;
import org.stenerud.hse.ui.echo2.tools.SelectFieldHelper;
import org.stenerud.hse.ui.echo2.tools.TwoColumnDataHelper;

/**
 * A component for editing permission levels.
 * 
 * @author Karl Stenerud
 */
public class PermissionTable
{
	// ========== CONSTANTS ==========

	/** Access type: Read, Write, or None */
	public static final int TYPE_READ_WRITE = 0;

	/** Access type: Allow or Deny */
	public static final int TYPE_ACCESS = 1;

	// ========== INTERNAL CLASSES ==========

	private static class SelectDescriptor
	{
		Permission permission;
		SelectFieldHelper helper;
	}

	// ========== INJECTED MEMBERS ==========
	private Messages messages;

	// ========== PRIVATE MEMBERS ==========
	private SelectFieldHelper accessSelectHelper = new SelectFieldHelper();
	private SelectFieldHelper readWriteSelectHelper = new SelectFieldHelper();
	private List descriptors = new LinkedList();
	private StaticTable table = new StaticTable(2);

	// ========== IMPLEMENTATION ==========

	public void init()
	{
		accessSelectHelper.addItem(messages.get("access.allow"), new Integer(PermissionLevels.READ));
		accessSelectHelper.addItem(messages.get("access.deny"), new Integer(PermissionLevels.NONE));

		readWriteSelectHelper.addItem(messages.get("access.none"), new Integer(PermissionLevels.NONE));
		readWriteSelectHelper.addItem(messages.get("access.read"), new Integer(PermissionLevels.READ));
		readWriteSelectHelper.addItem(messages.get("access.write"), new Integer(PermissionLevels.WRITE));
	}

	/**
	 * Constructor.
	 */
	public PermissionTable()
	{
		table.setStyleName("GroupEdit.Table");
	}

	/**
	 * Get the top level component for this table.
	 * 
	 * @return the top level component.
	 */
	public Component getComponent()
	{
		return table;
	}

	/**
	 * Add a permission to the table.
	 * 
	 * @param permission the permission.
	 * @param type the type of permission.
	 * @param major if true, this is a major item in the table (and is rendered differently).
	 */
	public void addPermission(Permission permission, int type, boolean major)
	{
		String style = "GroupEdit." + (major ? "Major" : "Minor");
		TwoColumnDataHelper dataHelper = new TwoColumnDataHelper(messages, style, style);

		SelectFieldHelper helper;
		if ( type == TYPE_READ_WRITE )
		{
			helper = (SelectFieldHelper)readWriteSelectHelper.clone();
		}
		else
		{
			helper = (SelectFieldHelper)accessSelectHelper.clone();
		}

		SelectField selectField = dataHelper.addSelectField(table, permission.getName(), helper);
		selectField.setSelectedIndex(0);

		SelectDescriptor descriptor = new SelectDescriptor();
		descriptor.helper = helper;
		descriptor.permission = permission;
		descriptors.add(descriptor);
	}

	public void addHeading(String name)
	{
		String style = "GroupEdit.Major";
		TwoColumnDataHelper dataHelper = new TwoColumnDataHelper(messages, style, style);
		dataHelper.addLabel(table, name);
	}

	/**
	 * Set the current permission and level settings.
	 * 
	 * @param levels permissions and levels
	 */
	public void setPermissionLevels(Set levels)
	{
		Map permissionMap = new HashMap();

		for ( Iterator iter = levels.iterator(); iter.hasNext(); )
		{
			PermissionLevel permissionLevel = (PermissionLevel)iter.next();
			permissionMap.put(permissionLevel.getPermission(), permissionLevel);
		}

		for ( Iterator iter = descriptors.iterator(); iter.hasNext(); )
		{
			SelectDescriptor desc = (SelectDescriptor)iter.next();
			int level = ((Integer)desc.helper.getSelectedItem()).intValue();
			Permission permission = desc.permission;
			PermissionLevel permissionLevel = (PermissionLevel)permissionMap.get(permission);
			if ( null == permissionLevel )
			{
				permissionLevel = new PermissionLevel();
				permissionLevel.setPermission(permission);
				permissionLevel.setLevel(PermissionLevels.NONE);
			}
			else if ( permissionLevel.getLevel() == PermissionLevels.WRITE
					&& !desc.helper.contains(new Integer(PermissionLevels.WRITE)) )
			{
				// Handle case where the DB contains a WRITE permission but only READ is allowed.
				permissionLevel.setLevel(PermissionLevels.READ);
			}

			if ( permissionLevel.getLevel() != level )
			{
				desc.helper.setSelectedItem(new Integer(permissionLevel.getLevel()));
			}
		}
	}

	/**
	 * Get the current permissions and levels.
	 * 
	 * @return the permissions and levels
	 */
	public Set getPermissionLevels()
	{
		Set results = new HashSet();
		for ( Iterator iter = descriptors.iterator(); iter.hasNext(); )
		{
			SelectDescriptor desc = (SelectDescriptor)iter.next();
			PermissionLevel permissionLevel = new PermissionLevel();
			permissionLevel.setPermission(desc.permission);
			permissionLevel.setLevel(((Integer)desc.helper.getSelectedItem()).intValue());
			results.add(permissionLevel);
		}
		return results;
	}

	// ========== GETTERS AND SETTERS ==========

	public Messages getMessages()
	{
		return messages;
	}

	public void setMessages(Messages messages)
	{
		this.messages = messages;
	}
}
