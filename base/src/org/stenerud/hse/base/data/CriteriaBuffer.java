package org.stenerud.hse.base.data;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

/**
 * Helper class for filling in HQL criteria.
 * 
 * @author Karl Stenerud
 */
public class CriteriaBuffer
{
	// ========== PRIVATE MEMBERS ==========
	private StringBuffer baseQuery = new StringBuffer();
	private StringBuffer criteria = new StringBuffer();
	private StringBuffer suffix = new StringBuffer();
	private boolean firstCriteria = true;

	// ========== IMPLEMENTATION ==========

	/**
	 * Constructor.
	 * 
	 * @param baseQuery the base query up to (but not including) the WHERE
	 *           clause.
	 */
	public CriteriaBuffer(String baseQuery)
	{
		this(baseQuery, null);
	}

	/**
	 * Constructor.
	 * 
	 * @param baseQuery the base query up to (but not including) the WHERE
	 *           clause.
	 * @param suffix the suffic portion, containing "ORDER BY", "GROUP BY" etc.
	 */
	public CriteriaBuffer(String baseQuery, String suffix)
	{
		this.baseQuery.append(baseQuery);
		if ( null != suffix && suffix.length() != 0 )
		{
			this.suffix.append(" ");
			this.suffix.append(suffix);
		}
	}

	/**
	 * Add a new generic criteria to the query.
	 * 
	 * @param criteriaString the criteria to add.
	 */
	public void addCriteria(String criteriaString)
	{
		if ( firstCriteria )
		{
			firstCriteria = false;
			this.criteria.append(" WHERE ");
		}
		else
		{
			this.criteria.append(" AND ");
		}
		this.criteria.append(criteriaString);
	}

	/**
	 * Add new criteria to the query.
	 * 
	 * @param criteriaString the criteria up to and including the comparison
	 *           operator.
	 * @param comparator the value to compare.
	 */
	public void addCriteria(String criteriaString, String comparator)
	{
		internalAddCriteria(criteriaString, makeHQLFriendly(comparator));
	}

	/**
	 * Add new criteria to the query.
	 * 
	 * @param criteriaString the criteria up to and including the comparison
	 *           operator.
	 * @param comparator the value to compare.
	 */
	public void addCriteria(String criteriaString, int comparator)
	{
		internalAddCriteria(criteriaString, makeHQLFriendly(comparator));
	}

	/**
	 * Add new criteria to the query.
	 * 
	 * @param criteriaString the criteria up to and including the comparison
	 *           operator.
	 * @param comparator the value to compare.
	 */
	public void addCriteria(String criteriaString, long comparator)
	{
		internalAddCriteria(criteriaString, makeHQLFriendly(comparator));
	}

	/**
	 * Add new criteria to the query.
	 * 
	 * @param criteriaString the criteria up to and including the comparison
	 *           operator.
	 * @param comparator the value to compare.
	 */
	public void addCriteria(String criteriaString, boolean comparator)
	{
		internalAddCriteria(criteriaString, makeHQLFriendly(comparator));
	}

	/**
	 * Add new criteria to the query.
	 * 
	 * @param criteriaString the criteria up to and including the comparison
	 *           operator.
	 * @param comparator the value to compare.
	 */
	public void addCriteria(String criteriaString, Date comparator)
	{
		internalAddCriteria(criteriaString, makeHQLFriendly(comparator));
	}

	public void addCriteria(String criteriaString, Collection comparator)
	{
		internalAddCriteria(criteriaString, makeHQLFriendly(comparator));
	}

	/**
	 * Get the completed query.
	 * 
	 * @return the query.
	 */
	public String getQuery()
	{
		StringBuffer buff = new StringBuffer();
		buff.append(baseQuery);
		buff.append(criteria);
		buff.append(suffix);
		return buff.toString();
	}

	// ========== UTILITY METHODS ==========

	/**
	 * Add a criteria entry.
	 * 
	 * @param left the left-hand side, including operator.
	 * @param right the right-hand side.
	 */
	private void internalAddCriteria(String left, String right)
	{
		if ( firstCriteria )
		{
			firstCriteria = false;
			this.criteria.append(" WHERE ");
		}
		else
		{
			this.criteria.append(" AND ");
		}
		this.criteria.append(left);
		this.criteria.append(' ');
		this.criteria.append(right);
	}

	/**
	 * Make an HQL-friendly string from a string.
	 * 
	 * @param value the value to convert.
	 * @return the converted value.
	 */
	private String makeHQLFriendly(String value)
	{
		// Be sure to escape single quotes to avoid query mischief.
		return "'" + value.replaceAll("'", "''") + "'";
	}

	/**
	 * Make an HQL-friendly string from an int.
	 * 
	 * @param value the value to convert.
	 * @return the converted value.
	 */
	private String makeHQLFriendly(int value)
	{
		return String.valueOf(value);
	}

	/**
	 * Make an HQL-friendly string from a long.
	 * 
	 * @param value the value to convert.
	 * @return the converted value.
	 */
	private String makeHQLFriendly(long value)
	{
		return String.valueOf(value);
	}

	/**
	 * Make an HQL-friendly string from a boolean. <br>
	 * NOTE: Thie returns the string contants 'true' or 'false'. If your
	 * database does not support this (e.g. Derby), you will have to set
	 * database.substitutions in the application.properties file.
	 * 
	 * @param value the value to convert.
	 * @return the converted value.
	 */
	private String makeHQLFriendly(boolean value)
	{
		return String.valueOf(value);
	}

	/**
	 * Make an HQL-friendly string from a date.
	 * 
	 * @param date the value to convert.
	 * @return the converted value.
	 */
	private String makeHQLFriendly(Date date)
	{
		return makeHQLFriendly(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(date));
	}

	/**
	 * Make an HQL-friendly string from a collection of objects.
	 * 
	 * @param collection the collection to convert.
	 * @return the converted value.
	 */
	private String makeHQLFriendly(Collection collection)
	{
		StringBuffer buff = new StringBuffer("(");
		for ( Iterator iter = collection.iterator(); iter.hasNext(); )
		{
			Object o = iter.next();
			if ( o instanceof String )
			{
				buff.append(makeHQLFriendly((String)o));
			}
			else
			{
				buff.append(o.toString());
			}
			if ( iter.hasNext() )
			{
				buff.append(",");
			}
		}
		buff.append(")");
		return buff.toString();
	}
}
