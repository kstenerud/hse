package org.stenerud.hse.base.ui.echo2.validation;

import org.stenerud.hse.base.ui.validation.ConditionalGetter;

import nextapp.echo2.app.CheckBox;

/**
 * A conditional getter for a CheckBox. <br>
 * Returns the current value of the checkbox (unless inverted).
 * 
 * @author Karl Stenerud
 */
public class CheckBoxConditionalGetter implements ConditionalGetter
{
	private CheckBox checkBox;
	private boolean invert;

	/**
	 * Constructor.
	 * 
	 * @param checkBox the checkbox to get a conditional value from.
	 * @param invert if true, invert the result of getCondition().
	 */
	public CheckBoxConditionalGetter(CheckBox checkBox, boolean invert)
	{
		this.checkBox = checkBox;
		this.invert = invert;
	}

	public boolean getCondition()
	{
		return invert ^ checkBox.isSelected();
	}

}
