package org.stenerud.hse.ui.echo2.screen.groups;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import nextapp.echo2.app.Button;
import nextapp.echo2.app.Column;
import nextapp.echo2.app.Extent;
import nextapp.echo2.app.SplitPane;
import nextapp.echo2.app.event.ActionEvent;
import nextapp.echo2.app.event.ActionListener;

import org.stenerud.hse.business.group.GroupBusiness;
import org.stenerud.hse.data.group.Group;
import org.stenerud.hse.data.security.Permission;
import org.stenerud.hse.data.security.PermissionLevel;
import org.stenerud.hse.data.security.SecurityDAO;
import org.stenerud.hse.ui.echo2.ApplicationHelper;
import org.stenerud.hse.ui.echo2.Icons;
import org.stenerud.hse.ui.echo2.RowListener;
import org.stenerud.hse.ui.echo2.Theme;
import org.stenerud.hse.ui.echo2.components.TrueFalseRequestor;
import org.stenerud.hse.ui.echo2.screen.PaneScreen;
import org.stenerud.hse.ui.validation.ValidationException;

import echopointng.PushButton;

/**
 * Group edit screen.
 * 
 * @author Karl Stenerud
 */
public class GroupScreen extends PaneScreen
{
	private static final long serialVersionUID = 1L;

	// ========== CONSTANTS ==========
	private static final int CONTROLS_HEIGHT = 46;

	// ========== INTERNAL CLASSES ==========

	private class EditButtonListener implements RowListener
	{
		private static final long serialVersionUID = 1L;

		private int row;

		public void actionPerformed(ActionEvent e)
		{
			try
			{
				processEditConfirm(row);
			}
			catch ( RuntimeException ex )
			{
				if ( !handleException(ex) )
				{
					throw ex;
				}
			}
		}

		public void setRow(int row)
		{
			this.row = row;
		}

		public Object clone()
		{
			try
			{
				return super.clone();
			}
			catch ( CloneNotSupportedException e )
			{
				return null; // Will never happen
			}
		}
	}

	private class DeleteButtonListener implements RowListener
	{
		private static final long serialVersionUID = 1L;

		private int row;

		public void actionPerformed(ActionEvent e)
		{
			try
			{
				processDeleteConfirm(row);
			}
			catch ( RuntimeException ex )
			{
				if ( !handleException(ex) )
				{
					throw ex;
				}
			}
		}

		public void setRow(int row)
		{
			this.row = row;
		}

		public Object clone()
		{
			try
			{
				return super.clone();
			}
			catch ( CloneNotSupportedException e )
			{
				return null; // Will never happen
			}
		}
	}

	// ========== INJECTED MEMBERS ==========
	private GroupBusiness groupBusiness;
	private SecurityDAO securityDAO;
	private GroupTableModel tableModel;
	private GroupTable table;
	private GroupEditRequestor editRequestor;
	private GroupEditRequestor newRequestor;

	// ========== PRIVATE MEMBERS ==========
	private Button newButton;
	private ArrayList groups;

	// ========== IMPLEMENTATION ==========

	public String getTitle()
	{
		return messages.get("screen.groupEditor");
	}

	protected void initComponents()
	{
		SplitPane outerContainer = new SplitPane(SplitPane.ORIENTATION_VERTICAL_TOP_BOTTOM, new Extent(CONTROLS_HEIGHT,
				Extent.PX));
		add(outerContainer);

		Column controls = new Column();
		outerContainer.add(controls);

		// New button
		newButton = new PushButton(messages.get("button.newGroup"), Theme.getIcon(Icons.NEW));
		controls.add(newButton);
		newButton.addActionListener(new ActionListener()
		{
			private static final long serialVersionUID = 1L;

			public void actionPerformed(ActionEvent e)
			{
				try
				{
					processNewConfirm();
				}
				catch ( RuntimeException ex )
				{
					if ( !handleException(ex) )
					{
						throw ex;
					}
				}
			}
		});

		// Results table
		table.setEditButtonListener(new EditButtonListener());
		table.setDeleteButtonListener(new DeleteButtonListener());
		tableModel = (GroupTableModel)table.getModel();
		outerContainer.add(table);

		editRequestor.setCreateNew(false);
		editRequestor.addActionListener(new ActionListener()
		{
			private static final long serialVersionUID = 1L;

			public void actionPerformed(ActionEvent ev)
			{
				try
				{
					if ( ev.getActionCommand().equals(TrueFalseRequestor.COMMAND_TRUE) )
					{
						processEdit();
					}
				}
				catch ( RuntimeException ex )
				{
					if ( !handleException(ex) )
					{
						throw ex;
					}
				}
			}
		});

		newRequestor.setCreateNew(true);
		newRequestor.addActionListener(new ActionListener()
		{
			private static final long serialVersionUID = 1L;

			public void actionPerformed(ActionEvent ev)
			{
				try
				{
					if ( ev.getActionCommand().equals(TrueFalseRequestor.COMMAND_TRUE) )
					{
						processNew();
					}
				}
				catch ( RuntimeException ex )
				{
					if ( !handleException(ex) )
					{
						throw ex;
					}
				}
			}
		});
	}

	protected void resetComponents()
	{
		try
		{
			newButton.setVisible(groupBusiness.canCreate());
			tableModel.clear();
			doView();
		}
		catch ( RuntimeException ex )
		{
			if ( !handleException(ex) )
			{
				throw ex;
			}
			ApplicationHelper.returnToMain();
		}
	}

	// ========== UTILITY METHODS ==========

	/**
	 * Update a group's permissions to the specified set.
	 * 
	 * @param permissionLevels the permissions and levels to update to.
	 * @param group the group to update.
	 */
	private void updatePermissions(Set permissionLevels, Group group)
	{
		Map permissionMap = new HashMap();

		for ( Iterator iter = group.getPermissionLevels().iterator(); iter.hasNext(); )
		{
			PermissionLevel perm = (PermissionLevel)iter.next();
			permissionMap.put(perm.getPermission(), perm);
		}

		for ( Iterator iter = permissionLevels.iterator(); iter.hasNext(); )
		{
			PermissionLevel newPermLevel = (PermissionLevel)iter.next();
			int level = newPermLevel.getLevel();
			Permission perm = newPermLevel.getPermission();
			PermissionLevel permLevel = (PermissionLevel)permissionMap.get(perm);
			if ( null == permLevel )
			{
				permLevel = new PermissionLevel();
				permLevel.setPermission(securityDAO.refresh(perm));
				permLevel.setLevel(level);
				securityDAO.create(permLevel);
				group.getPermissionLevels().add(permLevel);
			}
			else if ( permLevel.getLevel() != level )
			{
				permLevel = securityDAO.refresh(permLevel);
				permLevel.setLevel(level);
				securityDAO.update(permLevel);
			}
		}
	}

	private void doView()
	{
		Collection newGroups = groupBusiness.getGroups();

		tableModel.setData(newGroups);
		groups = new ArrayList(newGroups);
	}

	// ========== EVENT HANDLERS ==========

	private void processNewConfirm()
	{
		try
		{
			ApplicationHelper.displayRequestor(newRequestor);
		}
		catch ( ValidationException ex )
		{
			handleValidationException(ex);
		}
	}

	private void processNew()
	{
		Group group = new Group();
		group.setName(newRequestor.getName());
		updatePermissions(newRequestor.getPermissionLevels(), group);
		groupBusiness.create(group);

		doView();
	}

	private int editRow;

	private void processEditConfirm(int row)
	{
		editRow = row;
		Group group = (Group)groups.get(editRow);
		editRequestor.setName(group.getName());
		groupBusiness.refresh(group);
		editRequestor.setPermissionLevels(group.getPermissionLevels());
		ApplicationHelper.displayRequestor(editRequestor);
	}

	private void processEdit()
	{
		Group group = (Group)groups.get(editRow);
		group.setName(editRequestor.getName());
		updatePermissions(editRequestor.getPermissionLevels(), group);
		groupBusiness.update(group);

		doView();
	}

	private int deleteRow;

	private void processDeleteConfirm(int row)
	{
		deleteRow = row;
		openYesNoRequestor(messages.get("prompt.group.delete.title"), messages.get("prompt.group.delete.description"),
				new ActionListener()
				{
					private static final long serialVersionUID = 1L;

					public void actionPerformed(ActionEvent e)
					{
						try
						{
							if ( TrueFalseRequestor.commandIsTrue(e) )
							{
								processDelete();
							}
						}
						catch ( RuntimeException ex )
						{
							if ( !handleException(ex) )
							{
								throw ex;
							}
						}
					}
				});
	}

	private void processDelete()
	{
		groupBusiness.delete((Group)groups.get(deleteRow));
		doView();
	}

	// ========== GETTERS AND SETTERS ==========

	public GroupEditRequestor getEditRequestor()
	{
		return editRequestor;
	}

	public void setEditRequestor(GroupEditRequestor editRequestor)
	{
		this.editRequestor = editRequestor;
	}

	public GroupBusiness getGroupBusiness()
	{
		return groupBusiness;
	}

	public void setGroupBusiness(GroupBusiness groupBusiness)
	{
		this.groupBusiness = groupBusiness;
	}

	public GroupEditRequestor getNewRequestor()
	{
		return newRequestor;
	}

	public void setNewRequestor(GroupEditRequestor newRequestor)
	{
		this.newRequestor = newRequestor;
	}

	public SecurityDAO getSecurityDAO()
	{
		return securityDAO;
	}

	public void setSecurityDAO(SecurityDAO securityDAO)
	{
		this.securityDAO = securityDAO;
	}

	public GroupTable getTable()
	{
		return table;
	}

	public void setTable(GroupTable table)
	{
		this.table = table;
	}
}
