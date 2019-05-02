package org.stenerud.hse.base.ui.validation;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.stenerud.hse.base.ui.Messages;

/**
 * Rule to check if a value is not within a set of disallowed values. <br>
 * Note: this rule does not fire if the value is an empty string.
 * 
 * @author Karl Stenerud
 */
public class NotOneOfValidationRule implements ValidationRule
{
	// ========== INJECTED MEMBERS ==========
	private ValueGetter getter;
	private String name;
	private Set invalidValues;
	private boolean caseSensitive;

	// ========== PRIVATE MEMBERS ==========
	private Set comparisonValues;

	// ========== IMPLEMENTATION ==========

	/**
	 * Constructor.
	 * 
	 * @param name the name of the rule.
	 * @param getter the getter that gets the value.
	 * @param invalidValues the set of disallowed values.
	 * @param caseSensitive if true, the comparison will be case sensitive.
	 */
	public NotOneOfValidationRule(String name, ValueGetter getter, Set invalidValues, boolean caseSensitive)
	{
		this.name = name;
		this.getter = getter;
		this.invalidValues = invalidValues;
		this.caseSensitive = caseSensitive;
		if ( caseSensitive )
		{
			comparisonValues = invalidValues;
		}
		else
		{
			comparisonValues = new HashSet();
			for ( Iterator iter = invalidValues.iterator(); iter.hasNext(); )
			{
				comparisonValues.add(((String)iter.next()).toUpperCase());
			}
		}
	}

	public void validate(Messages messages)
	{
		String comparison = caseSensitive ? getter.getValue() : getter.getValue().toUpperCase();

		if ( comparison.length() > 0 )
		{
			if ( comparisonValues.contains(comparison) )
			{
				throw new ValidationException(messages.get("validation.cannotBe.oneOf", messages.get(name),
						makeValidValuesString()));
			}
		}
	}

	/**
	 * Make a string containing a list of values in the format "[a] [b] [c] ..."
	 * 
	 * @return the stringified list.
	 */
	private String makeValidValuesString()
	{
		StringBuffer buff = new StringBuffer();
		for ( Iterator iter = invalidValues.iterator(); iter.hasNext(); )
		{
			buff.append("[");
			buff.append(iter.next());
			buff.append("]");
			if ( iter.hasNext() )
			{
				buff.append(" ");
			}
		}
		return buff.toString();
	}
}
