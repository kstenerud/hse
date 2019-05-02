package org.stenerud.hse.base.tool;

/**
 * Tool to add or strip padding from a value.
 * 
 * @author Karl Stenerud
 */
public class Padder
{
	// ========== PRIVATE MEMBERS ==========
	private char filler;
	private boolean prePad;

	// ========== IMPLEMENTATION ==========

	/**
	 * Constructor.
	 * 
	 * @param filler the value to use for filler.
	 * @param prePad if true, padding is from the front of the value.
	 */
	public Padder(char filler, boolean prePad)
	{
		this.filler = filler;
		this.prePad = prePad;
	}

	/**
	 * Pad a value.
	 * 
	 * @param value the value to pad.
	 * @param length the length to pad to.
	 * @return the padded value.
	 */
	public String pad(Object value, int length)
	{
		return pad(value.toString(), length);
	}

	/**
	 * Pad a value.
	 * 
	 * @param value the value to pad.
	 * @param length the length to pad to.
	 * @return the padded value.
	 */
	public String pad(int value, int length)
	{
		return pad(String.valueOf(value), length);
	}

	/**
	 * Pad a value.
	 * 
	 * @param value the value to pad.
	 * @param length the length to pad to.
	 * @return the padded value.
	 */
	public String pad(long value, int length)
	{
		return pad(String.valueOf(value), length);
	}

	/**
	 * Pad a value.
	 * 
	 * @param value the value to pad.
	 * @param length the length to pad to.
	 * @return the padded value.
	 */
	public String pad(String value, int length)
	{
		if ( value.length() >= length )
		{
			return value;
		}

		StringBuffer buff = new StringBuffer(length);
		if ( !prePad )
		{
			buff.append(value);
		}

		for ( int i = value.length(); i < length; i++ )
		{
			buff.append(filler);
		}

		if ( prePad )
		{
			buff.append(value);
		}

		return buff.toString();
	}

	/**
	 * Strip padding from a value.
	 * 
	 * @param value the value to strip.
	 * @return the stripped value.
	 */
	public String strip(String value)
	{
		int length = value.length();
		int start;
		int end;

		if ( length == 0 )
		{
			return value;
		}

		if ( prePad )
		{
			start = 0;
			end = length;
			while ( start < end && value.charAt(start) == filler )
			{
				start++;
			}
		}
		else
		{
			start = 0;
			end = length - 1;
			while ( end >= start && value.charAt(end) == filler )
			{
				end--;
			}
			end++;
		}

		if ( start == end )
		{
			return "";
		}

		return value.substring(start, end);
	}
}
