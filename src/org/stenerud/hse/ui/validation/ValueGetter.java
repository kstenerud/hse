package org.stenerud.hse.ui.validation;

/**
 * An interface for getting a value. <br>
 * This is used by a validation rule to get a value to be validated.
 * 
 * @author Karl Stenerud
 */
public interface ValueGetter
{
	/**
	 * Get the value that will be validated.
	 * 
	 * @return the value.
	 */
	public String getValue();
}
