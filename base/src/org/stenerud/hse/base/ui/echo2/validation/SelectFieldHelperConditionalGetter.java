package org.stenerud.hse.base.ui.echo2.validation;

import org.stenerud.hse.base.ui.echo2.tool.SelectFieldHelper;
import org.stenerud.hse.base.ui.validation.ConditionalGetter;

/**
 * Conditional getter for a select field.
 * 
 * @author Karl Stenerud
 */
public class SelectFieldHelperConditionalGetter implements ConditionalGetter
{
	// ========== INJECTED MEMBERS ==========
	private SelectFieldHelper field;
	private String trueValue;
	private boolean invert;

	// ========== IMPLEMENTATION ==========

	/**
	 * Constructor.
	 * 
	 * @param field the field to get a value from.
	 * @param trueValue the value meaning TRUE.
	 * @param invert if true, invert the result.
	 */
	public SelectFieldHelperConditionalGetter(SelectFieldHelper field, String trueValue, boolean invert)
	{
		this.field = field;
		this.trueValue = trueValue;
		this.invert = invert;
	}

	public boolean getCondition()
	{
		return invert ^ trueValue.equals(field.getSelectedItem());
	}
}
