package org.stenerud.hse.ui.echo2.validation;

import org.stenerud.hse.ui.echo2.tools.SelectFieldHelper;
import org.stenerud.hse.ui.validation.ValueGetter;

/**
 * A value getter for a select field helper.
 * 
 * @author Karl Stenerud
 */
public class SelectFieldHelperValueGetter implements ValueGetter
{
	// ========== INJECTED MEMBERS ==========
	private SelectFieldHelper field;

	// ========== IMPLEMENTATION ==========

	/**
	 * Constructor.
	 * 
	 * @param field the select field helper.
	 */
	public SelectFieldHelperValueGetter(SelectFieldHelper field)
	{
		this.field = field;
	}

	public String getValue()
	{
		return (String)field.getSelectedItem();
	}
}
