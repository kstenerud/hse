package org.stenerud.hse.ui.echo2;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Formats a table column based on the column type.
 * 
 * @see ColumnInformation ColumnInformation
 * @author Karl Stenerud
 */
public class ColumnFormatter
{
	// ========== IMPLEMENTATION ==========

	/**
	 * Format a value.
	 * 
	 * @param value the value to format.
	 * @param type the value's type.
	 * @return the formatted value.
	 */
	public String format(Object value, int type)
	{
		if ( null == value )
		{
			return null;
		}

		if ( value instanceof String && ((String)value).length() == 0 )
		{
			return null;
		}

		if ( type == ColumnInformation.COLUMN_TYPE_DATE )
		{
			return new SimpleDateFormat("yyyy-MMM-dd").format((Date)value);
		}

		if ( type == ColumnInformation.COLUMN_TYPE_DATE_DAYOFWEEK )
		{
			return new SimpleDateFormat("yyyy-MMM-dd (EEE)").format((Date)value);
		}

		if ( type == ColumnInformation.COLUMN_TYPE_JULIANDATE )
		{
			return new SimpleDateFormat("0yyDDD").format((Date)value);
		}

		if ( type == ColumnInformation.COLUMN_TYPE_DATETIME )
		{
			return new SimpleDateFormat("yyyy-MMM-dd HH:mm:ss").format((Date)value);
		}

		if ( type == ColumnInformation.COLUMN_TYPE_DATETIME_MS )
		{
			return new SimpleDateFormat("yyyy-MMM-dd HH:mm:ss.SSS").format((Date)value);
		}

		if ( type == ColumnInformation.COLUMN_TYPE_TIME )
		{
			return new SimpleDateFormat("HH:mm:ss").format((Date)value);
		}

		if ( type == ColumnInformation.COLUMN_TYPE_TIME_MS )
		{
			return new SimpleDateFormat("HH:mm:ss.SSS").format((Date)value);
		}

		if ( type == ColumnInformation.COLUMN_TYPE_CURRENCY )
		{
			long result;
			if ( value instanceof Integer )
			{
				result = ((Integer)value).longValue();
			}
			else if ( value instanceof Long )
			{
				result = ((Long)value).longValue();
			}
			else
			{
				result = Long.parseLong((String)value);
			}
			return NumberFormat.getCurrencyInstance().format(((double)result) / 100);
		}

		return value.toString();
	}

	/**
	 * Get a style name for a value type.
	 * 
	 * @param type the type.
	 * @return the style name.
	 */
	public String getStyle(int type)
	{
		if ( isNumeric(type) )
		{
			return "EditTable.Basic.Number";
		}

		return "EditTable.Basic.String";
	}

	/**
	 * Check if a type represents a numeric value.
	 * 
	 * @param type the type.
	 * @return true if the type is numeric.
	 */
	public boolean isNumeric(int type)
	{
		return type == ColumnInformation.COLUMN_TYPE_NUMBER || type == ColumnInformation.COLUMN_TYPE_CURRENCY;
	}
}
