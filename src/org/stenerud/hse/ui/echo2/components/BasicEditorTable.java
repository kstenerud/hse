package org.stenerud.hse.ui.echo2.components;

import org.stenerud.hse.ui.Messages;
import org.stenerud.hse.ui.echo2.ColumnFormatter;
import org.stenerud.hse.ui.echo2.ColumnInformation;

import nextapp.echo2.app.Component;
import nextapp.echo2.app.Label;
import nextapp.echo2.app.Table;
import nextapp.echo2.app.table.TableCellRenderer;

/**
 * Basic editor table. <br>
 * This implementation provides a default header, localized messages, and support for ColumnInformation. Its style is
 * set to "EditTable".
 * 
 * @author Karl Stenerud
 */
public abstract class BasicEditorTable extends Table
{
	private static final long serialVersionUID = 1L;

	// ========== INJECTED MEMBERS ==========
	private ColumnInformation columnInformation;
	protected Messages messages;

	// ========== PRIVATE MEMBERS ==========
	private ColumnFormatter formatter = new ColumnFormatter();

	/**
	 * Basic TableCellRenderer. <br>
	 * This implementation stringifies the value and sets its style to "EditTable.Header".
	 */
	private TableCellRenderer tableHeaderRenderer = new TableCellRenderer()
	{
		private static final long serialVersionUID = 1L;

		public Component getTableCellRendererComponent(Table table, Object value, int column, int row)
		{
			Label label = new Label(value.toString());
			label.setStyleName("EditTable.Header");
			return label;
		}
	};

	// ========== IMPLEMENTATION ==========

	/**
	 * Constructor.
	 */
	public BasicEditorTable()
	{
		super();
		setStyleName("EditTable.Table");
		this.setDefaultHeaderRenderer(tableHeaderRenderer);
	}

	/**
	 * Get the name of a column.
	 * 
	 * @param column the column number.
	 * @return the column's name.
	 */
	protected String getColumnName(int column)
	{
		return columnInformation.getColumnName(column);
	}

	/**
	 * Get the type of a column.
	 * 
	 * @param column the column number.
	 * @return the column's type.
	 */
	protected int getColumnType(int column)
	{
		return columnInformation.getColumnType(column);
	}

	/**
	 * Use a column formatter to render the default component for an object.
	 * 
	 * @param column the column number.
	 * @param value the object to render.
	 * @return a component representing the value.
	 */
	protected Component renderDefaultComponent(int column, Object value)
	{
		int type = columnInformation.getColumnType(column);

		Label label = new Label(formatter.format(value, type));
		label.setStyleName(formatter.getStyle(type));
		return label;
	}

	// ========== GETTERS AND SETTERS ==========

	public ColumnInformation getColumnInformation()
	{
		return columnInformation;
	}

	public void setColumnInformation(ColumnInformation columnInformation)
	{
		this.columnInformation = columnInformation;
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
