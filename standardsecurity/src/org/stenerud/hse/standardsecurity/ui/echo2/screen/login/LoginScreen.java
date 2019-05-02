package org.stenerud.hse.standardsecurity.ui.echo2.screen.login;

import nextapp.echo2.app.Button;
import nextapp.echo2.app.PasswordField;
import nextapp.echo2.app.TextField;
import nextapp.echo2.app.event.ActionEvent;
import nextapp.echo2.app.event.ActionListener;

import org.stenerud.hse.base.ui.echo2.BaseApplicationHelper;
import org.stenerud.hse.base.ui.echo2.Echo2UserInterface;
import org.stenerud.hse.base.ui.echo2.Icons;
import org.stenerud.hse.base.ui.echo2.Theme;
import org.stenerud.hse.base.ui.echo2.component.StaticTable;
import org.stenerud.hse.base.ui.echo2.screen.PaneScreen;
import org.stenerud.hse.base.ui.echo2.tool.TwoColumnDataHelper;
import org.stenerud.hse.base.ui.echo2.validation.ValidationRuleMaker;
import org.stenerud.hse.base.ui.validation.ValidationException;
import org.stenerud.hse.base.ui.validation.Validator;
import org.stenerud.hse.standardsecurity.data.group.GroupDAO;
import org.stenerud.hse.standardsecurity.data.user.User;
import org.stenerud.hse.standardsecurity.service.security.AuthenticationException;
import org.stenerud.hse.standardsecurity.service.security.PasswordCredentials;
import org.stenerud.hse.standardsecurity.service.security.Security;
import org.stenerud.hse.standardsecurity.ui.echo2.UserChangeAware;

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

		TwoColumnDataHelper dataHelper = new TwoColumnDataHelper(messages, "TwoColumn.FieldName", "TwoColumn.FieldValue");

		usernameField = dataHelper.addTextField(table, "field.username", User.NAME_LENGTH, User.NAME_LENGTH);
		rules.addNotEmptyRule("field.username", usernameField);

		passwordField = dataHelper.addPasswordField(table, "field.password", User.PASSWORD_LENGTH, User.PASSWORD_LENGTH);
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

		Button button = new PushButton(messages.get("button.login"), Theme.getIcon16(Icons.LOGIN));
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
		BaseApplicationHelper.setFocus(usernameField);
	}

	// ========== EVENT HANDLERS ==========

	private void processLogin()
	{
		Echo2UserInterface ui = BaseApplicationHelper.getActiveUserInterface();
		try
		{
			validator.validate(messages);
			PasswordCredentials credentials = new PasswordCredentials();
			credentials.setUsername(usernameField.getText());
			credentials.setPassword(passwordField.getText());

			security.login(credentials);
			((UserChangeAware)ui).notifyUserChanged();
			ui.showMainScreen();
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
		catch ( ClassCastException ex )
		{
			if ( !(ui instanceof UserChangeAware) )
			{
				throw new IllegalArgumentException("User interface " + ui.getClass().getName() + " does not implement "
						+ UserChangeAware.class.getName());
			}
			throw ex;
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
