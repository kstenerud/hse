package org.stenerud.hse.ui.echo2.components;

import java.util.ArrayList;
import java.util.Collection;

import org.stenerud.hse.ui.Messages;
import org.stenerud.hse.ui.echo2.ColumnInformation;

import nextapp.echo2.app.table.AbstractTableModel;

/**
 * Basic table model. <br>
 * Provides support for ColumnInformation, localized messages, and stores row data.
 * 
 * @author Karl Stenerud
 */
public abstract class BasicTableModel extends AbstractTableModel
{
	private static final long serialVersionUID = 1L;

	// ========== INJECTED MEMBERS ==========
	private ColumnInformation columnInformation;
	protected Messages messages;

	// ========== PRIVATE MEMBERS ==========
	private ArrayList data = new ArrayList();

	// ========== IMPLEMENTATION ==========

	/**
	 * Get the internal column name (before being run through the localizer)
	 * 
	 * @param column the column number.
	 * @return the name.
	 */
	protected String getInternalColumnName(int column)
	{
		return columnInformation.getColumnName(column);
	}

	/**
	 * Get the raw value at the specified row.
	 * 
	 * @param row the row.
	 * @return the value.
	 */
	protected Object getInternalValueAt(int row)
	{
		return data.get(row);
	}

	/**
	 * Get the number of columns in this model.
	 * 
	 * @return the number of columns in this model.
	 */
	public int getColumnCount()
	{
		return columnInformation.getNumColumns();
	}

	/**
	 * Get the number of rows in this model.
	 * 
	 * @return the number of rows in this model.
	 */
	public int getRowCount()
	{
		return data.size();
	}

	/**
	 * Clear this model of all data.
	 */
	public void clear()
	{
		data = new ArrayList();
		this.fireTableDataChanged();
	}

	/**
	 * Get the name of a column.
	 * 
	 * @param column the column index.
	 * @return the name.
	 */
	public String getColumnName(int column)
	{
		return messages.get(columnInformation.getColumnName(column));
	}

	// ========== GETTERS AND SETTERS ==========

	/**
	 * Set this model's row data.
	 * 
	 * @param data the row data.
	 */
	public void setData(Collection data)
	{
		this.data = new ArrayList(data);

		this.fireTableDataChanged();
	}

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
