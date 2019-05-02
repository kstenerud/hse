package org.stenerud.hse.ui.echo2.components;

import java.util.Iterator;
import java.util.Vector;

import nextapp.echo2.app.Component;
import nextapp.echo2.app.Table;
import nextapp.echo2.app.table.AbstractTableModel;
import nextapp.echo2.app.table.TableCellRenderer;

/**
 * A simple table implementation that maintains its own internal TableModel. <br>
 * This is designed mostly for UI placement.
 * 
 * @author Karl Stenerud
 */
public class StaticTable extends Table
{
	private static final long serialVersionUID = 1L;

	// ========== PRIVATE MEMBERS ==========
	private int numColumns;
	private Vector rows = new Vector();

	private AbstractTableModel tableModel = new AbstractTableModel()
	{
		private static final long serialVersionUID = 1L;

		public int getColumnCount()
		{
			return numColumns;
		}

		public int getRowCount()
		{
			return getNumRows();
		}

		public Object getValueAt(int column, int row)
		{
			return null;
		}
	};

	private TableCellRenderer tableCellRenderer = new TableCellRenderer()
	{
		private static final long serialVersionUID = 1L;

		public Component getTableCellRendererComponent(Table table, Object value, int column, int row)
		{
			return getComponent(column, row);
		}
	};

	// ========== IMPLEMENTATION ==========

	/**
	 * Constructor.
	 */
	public StaticTable()
	{
		super();
		setModel(tableModel);
		setDefaultRenderer(Object.class, tableCellRenderer);
		this.setHeaderVisible(false);
	}

	/**
	 * Constructor.
	 * 
	 * @param numColumns the number of columns to use initially.
	 */
	public StaticTable(int numColumns)
	{
		this(numColumns, 0);
	}

	/**
	 * Constructor.
	 * 
	 * @param numColumns the number of columns to use initially.
	 * @param numRows the number of rows to use initially.
	 */
	public StaticTable(int numColumns, int numRows)
	{
		super();
		setModel(tableModel);
		setDefaultRenderer(Object.class, tableCellRenderer);
		this.setHeaderVisible(false);
		setNumColumns(numColumns);
		setNumRows(numRows);
	}

	/**
	 * Set the number of rows of this table.
	 * 
	 * @param numRows the number of rows to set to.
	 */
	public void setNumRows(int numRows)
	{
		int oldRows = getNumRows();
		int extraRows = numRows - oldRows;

		rows.setSize(numRows);
		for ( int i = 0; i < extraRows; i++ )
		{
			Vector row = new Vector(numColumns);
			row.setSize(numColumns);
			rows.set(oldRows + i, row);
		}
		tableModel.fireTableDataChanged();
	}

	/**
	 * Add a row to this table.
	 */
	public void addRow()
	{
		Vector row = new Vector(numColumns);
		row.setSize(numColumns);
		rows.add(row);
		tableModel.fireTableDataChanged();
	}

	/**
	 * Get the current number of rows.
	 * 
	 * @return the current number of rows.
	 */
	public int getNumRows()
	{
		return rows.size();
	}

	/**
	 * Set the number of columns of this table.
	 * 
	 * @param numColumns the number of columns.
	 */
	public void setNumColumns(int numColumns)
	{
		for ( Iterator iter = rows.iterator(); iter.hasNext(); )
		{
			((Vector)iter.next()).setSize(numColumns);
		}
		this.numColumns = numColumns;
		tableModel.fireTableStructureChanged();
	}

	/**
	 * Add a column to this table.
	 */
	public void addColumn()
	{
		setNumColumns(getNumColumns() + 1);
	}

	/**
	 * Get the current number of columns.
	 * 
	 * @return the current number of columns.
	 */
	public int getNumColumns()
	{
		return numColumns;
	}

	/**
	 * Set a component into the table.
	 * 
	 * @param column the column to set at.
	 * @param row the row to set at.
	 * @param component the component to set.
	 */
	public void setComponent(int column, int row, Component component)
	{
		Vector col = (Vector)rows.get(row);
		col.set(column, component);
		tableModel.fireTableDataChanged();
	}

	/**
	 * Get a component in the table.
	 * 
	 * @param column the column of the table.
	 * @param row the row of the table.
	 * @return the component at the specified row and column.
	 */
	public Component getComponent(int column, int row)
	{
		return (Component)((Vector)rows.get(row)).get(column);
	}
}
