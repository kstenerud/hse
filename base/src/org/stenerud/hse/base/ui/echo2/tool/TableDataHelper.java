package org.stenerud.hse.base.ui.echo2.tool;

import org.stenerud.hse.base.ui.Messages;
import org.stenerud.hse.base.ui.echo2.component.StaticTable;

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
	private TextFieldBuilder textFieldBuilder = new TextFieldBuilder();
	protected Messages messages;
	protected String fieldNameStyle;
	protected String fieldValueStyle;

	// ========== IMPLEMENTATION ==========

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
	 * Add a component to a table.
	 * 
	 * @param table the table to add the component to.
	 * @param fieldName the name of the field.
	 * @param component the component to add.
	 * @return the component.
	 */
	public abstract Component addComponent(StaticTable table, String fieldName, Component component);

	/**
	 * Add a separator to the table
	 * 
	 * @param parent the table to add a separator to.
	 * @param valueName the name of the value to use in the separator.
	 * @return the component used as a separator.
	 */
	public abstract Component addSeparator(StaticTable parent, String valueName);

	/**
	 * Add a string field to a 2-column table.
	 * 
	 * @param table the component to add the field to.
	 * @param fieldName the name of the field that is edited by this component.
	 * @param actualWidth the width of the field (-1 = browser default).
	 * @param visibleWidth the visible width to set (-1 = browser default).
	 * @return A text field that will edit the specified field.
	 */
	public TextField addTextField(StaticTable table, String fieldName, int actualWidth, int visibleWidth)
	{
		TextField textField = textFieldBuilder.buildTextField(actualWidth, visibleWidth);
		clearAction(textField);
		return (TextField)addComponent(table, fieldName, textField);
	}

	/**
	 * Add a password field to a 2-column table.
	 * 
	 * @param table the component to add the field to.
	 * @param fieldName the name of the field that is edited by this component.
	 * @param actualWidth the width of the field (-1 = browser default).
	 * @param visibleWidth the visible width to set (-1 = browser default).
	 * @return A password field that will edit the specified field.
	 */
	public PasswordField addPasswordField(StaticTable table, String fieldName, int actualWidth, int visibleWidth)
	{
		PasswordField passwordField = textFieldBuilder.buildPasswordField(actualWidth, visibleWidth);
		clearAction(passwordField);
		return (PasswordField)addComponent(table, fieldName, passwordField);
	}

	/**
	 * Add an integer field to a 2-column table.
	 * 
	 * @param table the component to add the field to.
	 * @param fieldName the name of the field that is edited by this component.
	 * @param actualWidth the width of the field (-1 = browser default).
	 * @param visibleWidth the visible width to set (-1 = browser default).
	 * @return A text field that will edit the specified field.
	 */
	public TextField addIntegerField(StaticTable table, String fieldName, int actualWidth, int visibleWidth)
	{
		TextField textField = textFieldBuilder.buildIntegerTextField(actualWidth, visibleWidth);
		clearAction(textField);
		return (TextField)addComponent(table, fieldName, textField);
	}

	/**
	 * Add a boolean field to a 2-column table.
	 * 
	 * @param table the component to add the field to.
	 * @param fieldName the name of the field that is edited by this component.
	 * @return A checkbox field that will edit the specified field.
	 */
	public CheckBox addBooleanField(StaticTable table, String fieldName)
	{
		CheckBox field = new CheckBox();
		return (CheckBox)addComponent(table, fieldName, field);
	}

	/**
	 * Add a date field to a 2-column table.
	 * 
	 * @param table the component to add the field to.
	 * @param fieldName the name of the field that is edited by this component.
	 * @return A date field that will edit the specified field.
	 */
	public DateField addDateField(StaticTable table, String fieldName)
	{
		DateField field = new DateField();
		return (DateField)addComponent(table, fieldName, field);
	}

	/**
	 * Add a select field to a 2-column table.
	 * 
	 * @param table the component to add the field to.
	 * @param fieldName the name of the field that is edited by this component.
	 * @param helper the select field helper.
	 * @return a select field.
	 */
	public SelectField addSelectField(StaticTable table, String fieldName, SelectFieldHelper helper)
	{
		SelectField field = new SelectField();
		helper.manageField(field);
		return (SelectField)addComponent(table, fieldName, field);
	}

	/**
	 * Add a label to a 2-row table.
	 * 
	 * @param table the component to add the field to.
	 * @param fieldName the name of the field that is edited by this component.
	 * @return A label.
	 */
	public Label addLabel(StaticTable table, String fieldName)
	{
		Label label = new Label();
		return (Label)addComponent(table, fieldName, label);
	}

	// ========== UTILITY METHODS ==========

	/**
	 * Adds a blank action listener to a component so that its default behavior
	 * will not be invoked.
	 * 
	 * @param component the component to set a blank action listener for
	 */
	protected void clearAction(TextComponent component)
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
}
