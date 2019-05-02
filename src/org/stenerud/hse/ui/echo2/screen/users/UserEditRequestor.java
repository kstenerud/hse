package org.stenerud.hse.ui.echo2.screen.users;

import java.util.Iterator;

import nextapp.echo2.app.Column;
import nextapp.echo2.app.Extent;
import nextapp.echo2.app.SelectField;
import nextapp.echo2.app.TextField;
import nextapp.echo2.app.event.ActionEvent;

import org.stenerud.hse.business.user.UserBusiness;
import org.stenerud.hse.data.group.Group;
import org.stenerud.hse.data.user.User;
import org.stenerud.hse.security.CurrentUser;
import org.stenerud.hse.ui.Messages;
import org.stenerud.hse.ui.echo2.components.StaticTable;
import org.stenerud.hse.ui.echo2.components.TrueFalseRequestor;
import org.stenerud.hse.ui.echo2.tools.SelectFieldHelper;
import org.stenerud.hse.ui.echo2.tools.TwoColumnDataHelper;
import org.stenerud.hse.ui.echo2.validation.ValidationRuleMaker;
import org.stenerud.hse.ui.validation.ValidationException;
import org.stenerud.hse.ui.validation.ValidationRule;
import org.stenerud.hse.ui.validation.Validator;

/**
 * Group edit requestor.
 * 
 * @author Karl Stenerud
 */
public class UserEditRequestor extends TrueFalseRequestor
{
	private static final long serialVersionUID = 1L;

	// ========== CONSTANTS ==========
	private static final int WIDTH = 430;
	private static final int HEIGHT = 200;

	// ========== INTERNAL CLASSES ==========
	private class DuplicateRule implements ValidationRule
	{
		public void validate(Messages messagesIn)
		{
			if ( !nameField.getText().equals(name) && userBusiness.exists(nameField.getText()) )
			{
				throw new ValidationException(messagesIn.get("validation.exists", nameField.getText()));
			}
		}
	}

	// ========== INJECTED MEMBERS ==========
	private CurrentUser currentUser;
	private UserBusiness userBusiness;
	private String name;
	private String password;
	private Group group;
	private boolean administrator;
	private boolean createNew;

	// ========== PRIVATE MEMBERS ==========
	private TextField nameField;
	private TextField passwordField;
	private SelectFieldHelper groupFieldHelper;
	private Validator validator = new Validator();

	// ========== IMPLEMENTATION ==========

	protected void initContents(Column contents)
	{
		setWidth(new Extent(WIDTH));
		setHeight(new Extent(HEIGHT));
		setTrueString(messages.get("answer.ok"));
		setFalseString(messages.get("answer.cancel"));
		setResizable(true);

		ValidationRuleMaker rules = new ValidationRuleMaker(validator);

		StaticTable table = new StaticTable(2);
		contents.add(table);
		table.setStyleName("TwoColumn.Table");

		TwoColumnDataHelper dataHelper = new TwoColumnDataHelper(messages, "TwoColumn.FieldName",
				"TwoColumn.FieldValue");

		groupFieldHelper = new SelectFieldHelper();
		dataHelper.addSelectField(table, "field.group", groupFieldHelper);

		nameField = dataHelper.addTextField(table, "field.username", User.NAME_LENGTH, false);
		nameField.setWidth(new Extent(300, Extent.PX));
		rules.addNotEmptyRule("field.username", nameField);

		passwordField = dataHelper.addTextField(table, "field.password", User.PASSWORD_LENGTH, false);
		passwordField.setWidth(new Extent(300, Extent.PX));
		rules.addNotEmptyRule("field.password", passwordField);

		rules.addComponentRule(new DuplicateRule(), nameField);
	}

	protected void resetComponents()
	{
		try
		{
			buildGroupField(groupFieldHelper.getManagedField());
			if ( isCreateNew() )
			{
				this.setTitle(messages.get("prompt.user.new.title"));
				this.setMessage(messages.get("prompt.user.new.description"));
				nameField.setText("");
				passwordField.setText("");
				administrator = false;

				// Get the group field off the administrator group, if possible.
				if ( groupFieldHelper.getSize() > 1 )
				{
					groupFieldHelper.setSelectedIndex(1);
				}
				else
				{
					groupFieldHelper.setSelectedIndex(0);
				}
			}
			else
			{
				this.setTitle(messages.get("prompt.user.edit.title"));
				this.setMessage(messages.get("prompt.user.edit.description"));
				nameField.setText(name);
				passwordField.setText(password);
				groupFieldHelper.setSelectedItem(group);
			}
			setFocusedComponent(nameField);
		}
		catch ( RuntimeException ex )
		{
			if ( !handleException(ex) )
			{
				throw ex;
			}
			getParent().remove(UserEditRequestor.this);
		}
	}

	protected boolean handleAction(ActionEvent ev)
	{
		if ( !ev.getActionCommand().equals(COMMAND_FALSE) )
		{
			try
			{
				validator.validate(messages);
				name = nameField.getText();
				password = passwordField.getText();
				group = (Group)groupFieldHelper.getSelectedItem();
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

	private void buildGroupField(SelectField field)
	{
		groupFieldHelper = new SelectFieldHelper();
		groupFieldHelper.manageField(field);
		if ( administrator )
		{
			groupFieldHelper.addItem(group.getName(), group);
		}
		else if ( userBusiness.canCreateInAnyGroup() )
		{
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

	// ========== GETTERS AND SETTERS ==========

	public boolean isAdministrator()
	{
		return administrator;
	}

	public void setAdministrator(boolean administrator)
	{
		this.administrator = administrator;
	}

	public boolean isCreateNew()
	{
		return createNew;
	}

	public void setCreateNew(boolean createNew)
	{
		this.createNew = createNew;
	}

	public CurrentUser getCurrentUser()
	{
		return currentUser;
	}

	public void setCurrentUser(CurrentUser currentUser)
	{
		this.currentUser = currentUser;
	}

	public Group getGroup()
	{
		return group;
	}

	public void setGroup(Group group)
	{
		this.group = group;
	}

	public String getPassword()
	{
		return password;
	}

	public void setPassword(String password)
	{
		this.password = password;
	}

	public UserBusiness getUserBusiness()
	{
		return userBusiness;
	}

	public void setUserBusiness(UserBusiness userBusiness)
	{
		this.userBusiness = userBusiness;
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
