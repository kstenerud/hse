package org.stenerud.hse.ui.validation;

/**
 * A getter for a conditional value. <br>
 * This is used by conditional rules.
 * 
 * @see ConditionalRule
 * @author Karl Stenerud
 */
public interface ConditionalGetter
{
	/**
	 * Get the condition.
	 * 
	 * @return the condition.
	 */
	public boolean getCondition();
}
