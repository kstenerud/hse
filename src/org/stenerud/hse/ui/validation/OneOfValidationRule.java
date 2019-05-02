package org.stenerud.hse.ui.validation;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.stenerud.hse.ui.Messages;

/**
 * Rule to check if a value is within a set of allowed values. <br>
 * Note: this rule does not fire if the value is an empty string.
 * 
 * @author Karl Stenerud
 */
public class OneOfValidationRule implements ValidationRule
{
	// ========== INJECTED MEMBERS ==========
	private ValueGetter getter;
	private String name;
	private Set validValues;
	private boolean caseSensitive;

	// ========== PRIVATE MEMBERS ==========
	private Set comparisonValues;

	// ========== IMPLEMENTATION ==========

	/**
	 * Constructor.
	 * 
	 * @param name the name of the rule.
	 * @param getter the getter that gets the value.
	 * @param validValues the set of allowed values.
	 * @param caseSensitive if true, the comparison will be case sensitive.
	 */
	public OneOfValidationRule(String name, ValueGetter getter, Set validValues, boolean caseSensitive)
	{
		this.name = name;
		this.getter = getter;
		this.validValues = validValues;
		this.caseSensitive = caseSensitive;
		if ( caseSensitive )
		{
			comparisonValues = validValues;
		}
		else
		{
			comparisonValues = new HashSet();
			for ( Iterator iter = validValues.iterator(); iter.hasNext(); )
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
			if ( !comparisonValues.contains(comparison) )
			{
				throw new ValidationException(messages.get("validation.mustBe.oneOf", messages.get(name),
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
		for ( Iterator iter = validValues.iterator(); iter.hasNext(); )
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
