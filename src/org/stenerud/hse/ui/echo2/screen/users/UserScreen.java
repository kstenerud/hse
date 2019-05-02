package org.stenerud.hse.ui.echo2.screen.users;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import nextapp.echo2.app.Button;
import nextapp.echo2.app.Column;
import nextapp.echo2.app.Extent;
import nextapp.echo2.app.Label;
import nextapp.echo2.app.Row;
import nextapp.echo2.app.SelectField;
import nextapp.echo2.app.SplitPane;
import nextapp.echo2.app.event.ActionEvent;
import nextapp.echo2.app.event.ActionListener;

import org.stenerud.hse.business.user.UserBusiness;
import org.stenerud.hse.data.group.Group;
import org.stenerud.hse.data.user.User;
import org.stenerud.hse.security.CurrentUser;
import org.stenerud.hse.ui.echo2.ApplicationHelper;
import org.stenerud.hse.ui.echo2.Icons;
import org.stenerud.hse.ui.echo2.RowListener;
import org.stenerud.hse.ui.echo2.Theme;
import org.stenerud.hse.ui.echo2.components.TrueFalseRequestor;
import org.stenerud.hse.ui.echo2.screen.PaneScreen;
import org.stenerud.hse.ui.echo2.tools.SelectFieldHelper;
import org.stenerud.hse.ui.validation.ValidationException;

import echopointng.PushButton;
import echopointng.Strut;

/**
 * User editor screen.
 * 
 * @author Karl Stenerud
 */
public class UserScreen extends PaneScreen
{
	private static final long serialVersionUID = 1L;

	// ========== CONSTANTS ==========
	private static final int CONTROLS_HEIGHT = 66;
	private static final String ALL_GROUPS = "All Groups";

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
	private UserBusiness userBusiness;
	private CurrentUser currentUser;
	private UserTableModel tableModel;
	private UserTable table;
	private UserEditRequestor editRequestor;
	private UserEditRequestor newRequestor;

	// ========== PRIVATE MEMBERS ==========
	private Button newButton;
	private SelectFieldHelper groupFieldHelper;
	private ArrayList users;

	// ========== IMPLEMENTATION ==========

	public String getTitle()
	{
		return messages.get("screen.userEditor");
	}

	protected void initComponents()
	{
		SplitPane outerContainer = new SplitPane(SplitPane.ORIENTATION_VERTICAL_TOP_BOTTOM, new Extent(CONTROLS_HEIGHT,
				Extent.PX));
		add(outerContainer);

		Column controls = new Column();
		outerContainer.add(controls);

		// New button
		newButton = new PushButton(messages.get("button.newUser"), Theme.getIcon(Icons.NEW));
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

		controls.add(new Strut(1, 10));

		Row row = new Row();
		controls.add(row);
		row.add(new Label(messages.get("field.group")));
		row.add(new Strut(4, 1));
		SelectField selectField = new SelectField();
		row.add(selectField);
		selectField.addActionListener(new ActionListener()
		{
			private static final long serialVersionUID = 1L;

			public void actionPerformed(ActionEvent arg0)
			{
				try
				{
					processView();
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
		groupFieldHelper = new SelectFieldHelper();
		groupFieldHelper.manageField(selectField);

		// Results table
		table.setEditButtonListener(new EditButtonListener());
		table.setDeleteButtonListener(new DeleteButtonListener());
		tableModel = (UserTableModel)table.getModel();
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
			buildGroupField(groupFieldHelper.getManagedField());
			groupFieldHelper.setSelectedIndex(0);
			newButton.setVisible(userBusiness.canCreateInOwnGroup());
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

	private void buildGroupField(SelectField field)
	{
		groupFieldHelper = new SelectFieldHelper();
		groupFieldHelper.manageField(field);
		if ( userBusiness.canListAllUsers() )
		{
			groupFieldHelper.addItem(ALL_GROUPS, ALL_GROUPS);
			for ( Iterator iter = userBusiness.getGroups().iterator(); iter.hasNext(); )
			{
				Group nextGroup = (Group)iter.next();
				groupFieldHelper.addItem(nextGroup.getName(), nextGroup);
			}
		}
		else
		{
			Group nextGroup = currentUser.get().getUserGroup();
			groupFieldHelper.addItem(nextGroup.getName(), nextGroup);
		}
	}

	private void doView()
	{
		Object selectedItem = groupFieldHelper.getSelectedItem();
		List newUsers;
		if ( selectedItem.equals(ALL_GROUPS) )
		{
			newUsers = userBusiness.getUsers();
		}
		else
		{
			newUsers = userBusiness.getUsers((Group)selectedItem);
		}

		tableModel.setData(newUsers);
		users = new ArrayList(newUsers);
	}

	// ========== EVENT HANDLERS ==========

	private void processView()
	{
		doView();
	}

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
		User user = new User();
		user.setName(newRequestor.getName());
		user.setPassword(newRequestor.getPassword());
		user.setUserGroup(newRequestor.getGroup());
		userBusiness.create(user);

		doView();
	}

	private int editRow;

	private void processEditConfirm(int row)
	{
		editRow = row;
		User user = (User)users.get(editRow);
		editRequestor.setName(user.getName());
		editRequestor.setPassword(user.getPassword());
		editRequestor.setGroup(user.getUserGroup());
		editRequestor.setAdministrator(user.isAdministrator());
		ApplicationHelper.displayRequestor(editRequestor);
	}

	private void processEdit()
	{
		User user = (User)users.get(editRow);
		user.setName(editRequestor.getName());
		user.setPassword(editRequestor.getPassword());
		user.setUserGroup(editRequestor.getGroup());
		userBusiness.update(user);

		doView();
	}

	private int deleteRow;

	private void processDeleteConfirm(int row)
	{
		deleteRow = row;
		openYesNoRequestor(messages.get("prompt.user.delete.title"), messages.get("prompt.user.delete.description"),
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
		userBusiness.delete((User)users.get(deleteRow));
		doView();
	}

	// ========== GETTERS AND SETTERS ==========

	public CurrentUser getCurrentUser()
	{
		return currentUser;
	}

	public void setCurrentUser(CurrentUser currentUser)
	{
		this.currentUser = currentUser;
	}

	public UserEditRequestor getEditRequestor()
	{
		return editRequestor;
	}

	public void setEditRequestor(UserEditRequestor editRequestor)
	{
		this.editRequestor = editRequestor;
	}

	public UserEditRequestor getNewRequestor()
	{
		return newRequestor;
	}

	public void setNewRequestor(UserEditRequestor newRequestor)
	{
		this.newRequestor = newRequestor;
	}

	public UserTable getTable()
	{
		return table;
	}

	public void setTable(UserTable table)
	{
		this.table = table;
	}

	public UserBusiness getUserBusiness()
	{
		return userBusiness;
	}

	public void setUserBusiness(UserBusiness userBusiness)
	{
		this.userBusiness = userBusiness;
	}
}
