package org.stenerud.hse.base.ui.echo2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Information about columns.
 * 
 * @author Karl Stenerud
 */
public class ColumnInformation
{
	// ========== CONSTANTS ==========
	public static final int COLUMN_TYPE_NUMBER = 0;
	public static final int COLUMN_TYPE_STRING = 1;
	public static final int COLUMN_TYPE_DATE = 2;
	public static final int COLUMN_TYPE_DATETIME = 3;
	public static final int COLUMN_TYPE_COMMAND = 4;
	public static final int COLUMN_TYPE_CURRENCY = 5;
	public static final int COLUMN_TYPE_BOOLEAN = 6;
	public static final int COLUMN_TYPE_JULIANDATE = 7;
	public static final int COLUMN_TYPE_DATETIME_MS = 8;
	public static final int COLUMN_TYPE_TIME = 9;
	public static final int COLUMN_TYPE_TIME_MS = 10;
	public static final int COLUMN_TYPE_DATE_DAYOFWEEK = 11;

	// ========== INTERNAL CLASSES ==========

	private static class LocalColumnInfo
	{
		public String name;
		public int type;
		public int size;

		public LocalColumnInfo(String name, int type, int size)
		{
			this.name = name;
			this.type = type;
			this.size = size;
		}
	}

	// ========== PRIVATE MEMBERS ==========
	private List columns = new ArrayList();
	private Map columnNames = new HashMap();

	// ========== IMPLEMENTATION ==========

	/**
	 * Get the number of columns in this implementation.
	 * 
	 * @return the number of columns.
	 */
	public int getNumColumns()
	{
		return columns.size();
	}

	/**
	 * Get the name of a column.
	 * 
	 * @param col the column.
	 * @return the name.
	 */
	public String getColumnName(int col)
	{
		return ((LocalColumnInfo)columns.get(col)).name;
	}

	/**
	 * Get the type of a column.
	 * 
	 * @param col the column.
	 * @return the type.
	 */
	public int getColumnType(int col)
	{
		return ((LocalColumnInfo)columns.get(col)).type;
	}

	/**
	 * Get the size of a column.
	 * 
	 * @param col the column.
	 * @return the size.
	 */
	public int getColumnSize(int col)
	{
		return ((LocalColumnInfo)columns.get(col)).size;
	}

	/**
	 * Get the column number from its name.
	 * 
	 * @param name the column name.
	 * @return the column number.
	 */
	public int getColumnNumber(String name)
	{
		return ((Integer)columnNames.get(name)).intValue();
	}

	// ========== UTILITY METHODS ==========

	protected void addColumn(String name, int type)
	{
		addColumn(name, type, 0);
	}

	protected void addColumn(String name, int type, int size)
	{
		columnNames.put(name, new Integer(columns.size()));
		columns.add(new LocalColumnInfo(name, type, size));
	}
}
