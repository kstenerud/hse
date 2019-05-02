package org.stenerud.hse.ui.echo2.tools;

import org.stenerud.hse.ui.Messages;
import org.stenerud.hse.ui.echo2.components.StaticTable;

import nextapp.echo2.app.CheckBox;
import nextapp.echo2.app.Component;
import nextapp.echo2.app.Label;
import nextapp.echo2.app.PasswordField;
import nextapp.echo2.app.SelectField;
import nextapp.echo2.app.TextField;
import nextapp.echo2.app.event.ActionEvent;
import nextapp.echo2.app.event.ActionListener;
import nextapp.echo2.app.text.TextComponent;
import echopointng.DateField;

/**
 * Abstract helper class for generating tabular data representations.
 * 
 * @author Karl Stenerud
 */
public abstract class TableDataHelper
{
	// ========== INJECTED MEMBERS ==========
	protected TextFieldBuilder inputComponentManager = new TextFieldBuilder();
	protected Messages messages;
	protected String fieldNameStyle;
	protected String fieldValueStyle;

	// ========== IMPLEMENTATION ==========

	public abstract Component addComponent(StaticTable parent, String fieldName, Component component);

	/**
	 * Constructor.
	 * 
	 * @param messages the localized messages.
	 * @param fieldNameStyle the style for a field name.
	 * @param fieldValueStyle the style for a field value.
	 */
	public TableDataHelper(Messages messages, String fieldNameStyle, String fieldValueStyle)
	{
		this.messages = messages;
		this.fieldNameStyle = fieldNameStyle;
		this.fieldValueStyle = fieldValueStyle;
	}

	/**
	 * Adds a blank action listener to a component so that its default behavior will not be invoked.
	 * 
	 * @param component the component to set a blank action listener for
	 */
	private void clearAction(TextComponent component)
	{
		component.addActionListener(new ActionListener()
		{
			private static final long serialVersionUID = 1L;

			public void actionPerformed(ActionEvent e)
			{
				// Blank action.
			}
		});
	}

	/**
	 * Add a string field to a 2-column table.
	 * 
	 * @param parent the component to add the field to.
	 * @param fieldName the name of the field that is edited by this component.
	 * @param length the maximum length allowed for this field.
	 * @return A text field that will edit the specified field.
	 */
	public TextField addTextField(StaticTable parent, String fieldName, int length)
	{
		return addTextField(parent, fieldName, length, true);
	}

	/**
	 * Add a string field to a 2-column table.
	 * 
	 * @param parent the component to add the field to.
	 * @param fieldName the name of the field that is edited by this component.
	 * @param length the maximum length allowed for this field.
	 * @param resize if true, resize the text field to fit.
	 * @return A text field that will edit the specified field.
	 */
	public TextField addTextField(StaticTable parent, String fieldName, int length, boolean resize)
	{
		TextField textField = inputComponentManager.buildTextField(length, resize);
		clearAction(textField);
		return (TextField)addComponent(parent, fieldName, textField);
	}

	/**
	 * Add a password field to a 2-column table.
	 * 
	 * @param parent the component to add the field to.
	 * @param fieldName the name of the field that is edited by this component.
	 * @param length the maximum length allowed for this field.
	 * @return A password field that will edit the specified field.
	 */
	public PasswordField addPasswordField(StaticTable parent, String fieldName, int length)
	{
		return addPasswordField(parent, fieldName, length, true);
	}

	/**
	 * Add a password field to a 2-column table.
	 * 
	 * @param parent the component to add the field to.
	 * @param fieldName the name of the field that is edited by this component.
	 * @param length the maximum length allowed for this field.
	 * @param resize if true, resize the text field to fit.
	 * @return A password field that will edit the specified field.
	 */
	public PasswordField addPasswordField(StaticTable parent, String fieldName, int length, boolean resize)
	{
		PasswordField passwordField = inputComponentManager.buildPasswordField(length, resize);
		clearAction(passwordField);
		return (PasswordField)addComponent(parent, fieldName, passwordField);
	}

	/**
	 * Add an integer field to a 2-column table.
	 * 
	 * @param parent the component to add the field to.
	 * @param fieldName the name of the field that is edited by this component.
	 * @param length the maximum length allowed for this field.
	 * @return A text field that will edit the specified field.
	 */
	public TextField addIntegerField(StaticTable parent, String fieldName, int length)
	{
		TextField textField = inputComponentManager.buildIntegerTextField(length, true);
		clearAction(textField);
		return (TextField)addComponent(parent, fieldName, textField);
	}

	/**
	 * Add a boolean field to a 2-column table.
	 * 
	 * @param parent the component to add the field to.
	 * @param fieldName the name of the field that is edited by this component.
	 * @return A checkbox field that will edit the specified field.
	 */
	public CheckBox addBooleanField(StaticTable parent, String fieldName)
	{
		CheckBox field = new CheckBox();
		return (CheckBox)addComponent(parent, fieldName, field);
	}

	/**
	 * Add a date field to a 2-column table.
	 * 
	 * @param parent the component to add the field to.
	 * @param fieldName the name of the field that is edited by this component.
	 * @return A date field that will edit the specified field.
	 */
	public DateField addDateField(StaticTable parent, String fieldName)
	{
		DateField field = new DateField();
		return (DateField)addComponent(parent, fieldName, field);
	}

	/**
	 * Add a select field to a 2-column table.
	 * 
	 * @param parent the component to add the field to.
	 * @param fieldName the name of the field that is edited by this component.
	 * @param helper the select field helper.
	 * @return a select field.
	 */
	public SelectField addSelectField(StaticTable parent, String fieldName, SelectFieldHelper helper)
	{
		SelectField field = new SelectField();
		helper.manageField(field);
		return (SelectField)addComponent(parent, fieldName, field);
	}

	/**
	 * Add a label to a 2-row table.
	 * 
	 * @param parent the component to add the field to.
	 * @param fieldName the name of the field that is edited by this component.
	 * @return A label.
	 */
	public Label addLabel(StaticTable parent, String fieldName)
	{
		Label label = new Label();
		return (Label)addComponent(parent, fieldName, label);
	}

	// ========== GETTERS AND SETTERS ==========

	public String getFieldValueStyle()
	{
		return fieldValueStyle;
	}

	public void setFieldValueStyle(String fieldValueStyle)
	{
		this.fieldValueStyle = fieldValueStyle;
	}

	public String getFieldNameStyle()
	{
		return fieldNameStyle;
	}

	public void setFieldNameStyle(String fieldNameStyle)
	{
		this.fieldNameStyle = fieldNameStyle;
	}

	public Messages getMessages()
	{
		return messages;
	}

	public void setMessages(Messages messages)
	{
		this.messages = messages;
	}

	public TextFieldBuilder getInputComponentManager()
	{
		return inputComponentManager;
	}

	public void setInputComponentManager(TextFieldBuilder inputComponentManager)
	{
		this.inputComponentManager = inputComponentManager;
	}
}
