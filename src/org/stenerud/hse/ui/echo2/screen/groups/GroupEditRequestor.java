package org.stenerud.hse.ui.echo2.screen.groups;

import java.util.HashSet;
import java.util.Set;

import nextapp.echo2.app.Column;
import nextapp.echo2.app.Component;
import nextapp.echo2.app.Extent;
import nextapp.echo2.app.TextField;
import nextapp.echo2.app.event.ActionEvent;

import org.stenerud.hse.business.group.GroupBusiness;
import org.stenerud.hse.data.group.Group;
import org.stenerud.hse.security.Permissions;
import org.stenerud.hse.ui.Messages;
import org.stenerud.hse.ui.echo2.components.BasicImageTabModel;
import org.stenerud.hse.ui.echo2.components.StaticTable;
import org.stenerud.hse.ui.echo2.components.TrueFalseRequestor;
import org.stenerud.hse.ui.echo2.tools.TwoColumnDataHelper;
import org.stenerud.hse.ui.echo2.validation.ValidationRuleMaker;
import org.stenerud.hse.ui.validation.ValidationException;
import org.stenerud.hse.ui.validation.ValidationRule;
import org.stenerud.hse.ui.validation.Validator;

import echopointng.TabbedPane;
import echopointng.tabbedpane.DefaultTabModel;

/**
 * Group edit requestor.
 * 
 * @author Karl Stenerud
 */
public class GroupEditRequestor extends TrueFalseRequestor
{
	private static final long serialVersionUID = 1L;

	// ========== CONSTANTS ==========
	private static final int WIDTH = 400;
	private static final int HEIGHT = 400;

	// ========== INTERNAL CLASSES ==========
	private class DuplicateRule implements ValidationRule
	{
		public void validate(Messages messagesIn)
		{
			if ( !nameField.getText().equals(name) && groupBusiness.exists(nameField.getText()) )
			{
				throw new ValidationException(messagesIn.get("validation.exists", nameField.getText()));
			}
		}
	}

	// ========== INJECTED MEMBERS ==========
	private GroupBusiness groupBusiness;
	private Permissions permissions;
	private PermissionTable permissionTable;
	private String name;
	private boolean createNew;
	private Set permissionLevels;

	// ========== PRIVATE MEMBERS ==========

	private TextField nameField;
	private TabbedPane tabbedPane;
	private Validator validator = new Validator();

	// ========== IMPLEMENTATION ==========

	protected void initContents(Column contents)
	{
		setWidth(new Extent(WIDTH));
		setHeight(new Extent(HEIGHT));
		setTrueString(messages.get("answer.ok"));
		setFalseString(messages.get("answer.cancel"));
		setResizable(true);

		initPermissionTable();

		/*
		 * Edit controls
		 */
		DefaultTabModel tabModel;
		tabbedPane = new TabbedPane();
		tabModel = new DefaultTabModel();
		tabbedPane.setModel(new BasicImageTabModel(tabModel));
		contents.add(tabbedPane);

		tabModel.addTab(messages.get("screen.details"), buildDetailsPane());
		tabModel.addTab(messages.get("screen.permissions"), getpermissionsPane());
	}

	protected void resetComponents()
	{
		if ( isCreateNew() )
		{
			this.setTitle(messages.get("prompt.group.new.title"));
			this.setMessage(messages.get("prompt.group.new.description"));
			nameField.setText("");
			permissionTable.setPermissionLevels(new HashSet());
		}
		else
		{
			this.setTitle(messages.get("prompt.group.edit.title"));
			this.setMessage(messages.get("prompt.group.edit.description"));
			nameField.setText(name);
			permissionTable.setPermissionLevels(permissionLevels);
		}
		tabbedPane.setSelectedIndex(0);
		setFocusedComponent(nameField);
	}

	protected boolean handleAction(ActionEvent ev)
	{
		if ( !ev.getActionCommand().equals(COMMAND_FALSE) )
		{
			try
			{
				validator.validate(messages);
				name = nameField.getText();
				permissionLevels = permissionTable.getPermissionLevels();
			}
			catch ( ValidationException ex )
			{
				handleValidationException(ex);
				return false;
			}
			catch ( RuntimeException ex )
			{
				if ( !handleException(ex) )
				{
					throw ex;
				}
				return false;
			}
		}
		return true;

	}

	// ========== UTILITY METHODS ==========

	private void initPermissionTable()
	{
		permissionTable.addHeading("heading.administration");
		permissionTable.addPermission(permissions.USERS, PermissionTable.TYPE_READ_WRITE, false);
		permissionTable.addPermission(permissions.USERS_ALL, PermissionTable.TYPE_READ_WRITE, false);
		permissionTable.addPermission(permissions.GROUPS, PermissionTable.TYPE_READ_WRITE, false);
	}

	/**
	 * Get the "permissions" editor pane.
	 * 
	 * @return the top component of the permissions pane.
	 */
	private Component getpermissionsPane()
	{
		return permissionTable.getComponent();
	}

	/**
	 * Build the "details" editor pane. <br>
	 * 
	 * @return the top component of the details pane.
	 */
	private Component buildDetailsPane()
	{
		ValidationRuleMaker rules = new ValidationRuleMaker(validator);

		StaticTable table = new StaticTable(2);
		table.setStyleName("TwoColumn.Table");

		TwoColumnDataHelper dataHelper = new TwoColumnDataHelper(messages, "TwoColumn.FieldName",
				"TwoColumn.FieldValue");

		nameField = dataHelper.addTextField(table, "field.name", Group.NAME_LENGTH, false);
		nameField.setWidth(new Extent(300, Extent.PX));

		rules.addNotEmptyRule("field.name", nameField);
		rules.addComponentRule(new DuplicateRule(), nameField);

		return table;
	}

	// ========== GETTERS AND SETTERS ==========

	public boolean isCreateNew()
	{
		return createNew;
	}

	public void setCreateNew(boolean createNew)
	{
		this.createNew = createNew;
	}

	public GroupBusiness getGroupBusiness()
	{
		return groupBusiness;
	}

	public void setGroupBusiness(GroupBusiness groupBusiness)
	{
		this.groupBusiness = groupBusiness;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public Permissions getPermissions()
	{
		return permissions;
	}

	public void setPermissions(Permissions permissions)
	{
		this.permissions = permissions;
	}

	public PermissionTable getPermissionTable()
	{
		return permissionTable;
	}

	public void setPermissionTable(PermissionTable permissionTable)
	{
		this.permissionTable = permissionTable;
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
