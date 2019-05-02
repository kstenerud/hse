package org.stenerud.hse.ui.echo2;

import java.util.ArrayList;
import java.util.List;

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

		public LocalColumnInfo(String name, int type)
		{
			this.name = name;
			this.type = type;
		}
	}

	// ========== PRIVATE MEMBERS ==========
	private List columns = new ArrayList();

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

	// ========== UTILITY METHODS ==========

	protected void addColumn(String name, int type)
	{
		columns.add(new LocalColumnInfo(name, type));
	}
}
