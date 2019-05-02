package org.stenerud.hse.ui.validation;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

import org.stenerud.hse.ui.Messages;

/**
 * Maintains a collection of validation rules and validates them.
 * 
 * @author Karl Stenerud
 */
public class Validator implements Cloneable
{
	// ========== PRIVATE MEMBERS ==========
	private LinkedList rules = new LinkedList();

	// ========== IMPLEMENTATION ==========

	/**
	 * Add a rule to this validator.
	 * 
	 * @param rule the rule to add.
	 */
	public void addRule(ValidationRule rule)
	{
		rules.add(rule);
	}

	/**
	 * Validate the rules in this validator.
	 * 
	 * @param messages the localized messages to use.
	 * @throws ValidationException if a validation rule fails.
	 */
	public void validate(Messages messages)
	{
		for ( Iterator iter = rules.iterator(); iter.hasNext(); )
		{
			((ValidationRule)iter.next()).validate(messages);
		}
	}

	/**
	 * Get the rules currently managed by this valiador.
	 * 
	 * @return the rules.
	 */
	public Collection getRules()
	{
		return rules;
	}

	/**
	 * Add a collection of rules to this validator.
	 * 
	 * @param rulesIn the rules to add.
	 */
	public void addRules(Collection rulesIn)
	{
		this.rules.addAll(rulesIn);
	}

	public Object clone()
	{
		try
		{
			Validator clone = (Validator)super.clone();
			clone.rules = (LinkedList)rules.clone();
			return clone;
		}
		catch ( CloneNotSupportedException ex )
		{
			// Will never happen
			return null;
		}
	}
}
