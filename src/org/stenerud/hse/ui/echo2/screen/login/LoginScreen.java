package org.stenerud.hse.ui.echo2.screen.login;

import nextapp.echo2.app.Button;
import nextapp.echo2.app.PasswordField;
import nextapp.echo2.app.TextField;
import nextapp.echo2.app.event.ActionEvent;
import nextapp.echo2.app.event.ActionListener;

import org.stenerud.hse.data.group.GroupDAO;
import org.stenerud.hse.data.user.User;
import org.stenerud.hse.security.AuthenticationException;
import org.stenerud.hse.security.Security;
import org.stenerud.hse.ui.echo2.ApplicationHelper;
import org.stenerud.hse.ui.echo2.Echo2UserInterface;
import org.stenerud.hse.ui.echo2.Icons;
import org.stenerud.hse.ui.echo2.Theme;
import org.stenerud.hse.ui.echo2.components.StaticTable;
import org.stenerud.hse.ui.echo2.screen.PaneScreen;
import org.stenerud.hse.ui.echo2.tools.TwoColumnDataHelper;
import org.stenerud.hse.ui.echo2.validation.ValidationRuleMaker;
import org.stenerud.hse.ui.validation.ValidationException;
import org.stenerud.hse.ui.validation.Validator;

import echopointng.PushButton;

/**
 * The login screen.
 * 
 * @author Karl Stenerud
 */
public class LoginScreen extends PaneScreen
{
	private static final long serialVersionUID = 1L;

	// ========== INJECTED MEMBERS ==========
	private GroupDAO groupDAO;
	private Security security;

	// ========== PRIVATE MEMBERS ==========
	private TextField usernameField;
	private PasswordField passwordField;
	private Validator validator = new Validator();

	// ========== IMPLEMENTATION ==========

	public String getTitle()
	{
		return messages.get("screen.login");
	}

	protected void initComponents()
	{
		ValidationRuleMaker rules = new ValidationRuleMaker(validator);

		StaticTable table = new StaticTable(2);
		add(table);
		table.setStyleName("TwoColumn.Table");

		TwoColumnDataHelper dataHelper = new TwoColumnDataHelper(messages, "TwoColumn.FieldName",
				"TwoColumn.FieldValue");

		usernameField = dataHelper.addTextField(table, "field.username", User.NAME_LENGTH);
		rules.addNotEmptyRule("field.username", usernameField);

		passwordField = dataHelper.addPasswordField(table, "field.password", User.PASSWORD_LENGTH);
		rules.addNotEmptyRule("field.password", passwordField);
		passwordField.addActionListener(new ActionListener()
		{
			private static final long serialVersionUID = 1L;

			public void actionPerformed(ActionEvent event)
			{
				try
				{
					processLogin();
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

		Button button = new PushButton(messages.get("button.login"), Theme.getIcon(Icons.LOGIN));
		dataHelper.addComponent(table, "field.blank", button);
		button.addActionListener(new ActionListener()
		{
			private static final long serialVersionUID = 1L;

			public void actionPerformed(ActionEvent event)
			{
				try
				{
					processLogin();
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
		usernameField.setText("");
		passwordField.setText("");
		ApplicationHelper.setFocus(usernameField);
	}

	// ========== EVENT HANDLERS ==========

	private void processLogin()
	{
		try
		{
			validator.validate(messages);
			String username = usernameField.getText();
			String password = passwordField.getText();

			security.passwordAuthenticate(username, password);
			Echo2UserInterface.getCurrent().notifyChangedUser();
			Echo2UserInterface.getCurrent().showMainScreen();
			return;
		}
		catch ( AuthenticationException ex )
		{
			openInfoRequestor(messages.get("error.error"), messages.get("error.loginFail"));
		}
		catch ( ValidationException ex )
		{
			handleValidationException(ex);
		}
	}

	// ========== GETTERS AND SETTERS ==========

	public GroupDAO getGroupDAO()
	{
		return groupDAO;
	}

	public void setGroupDAO(GroupDAO groupDAO)
	{
		this.groupDAO = groupDAO;
	}

	public Security getSecurity()
	{
		return security;
	}

	public void setSecurity(Security security)
	{
		this.security = security;
	}
}
