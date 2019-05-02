package org.stenerud.hse.ui.validation;

import org.stenerud.hse.tools.Padder;

/**
 * A value getter that strips any padding off the value.
 * 
 * @author Karl Stenerud
 */
public class StrippingValueGetter implements ValueGetter
{
	// ========== INJECTED MEMBERS ==========
	private ValueGetter getter;

	// ========== PRIVATE MEMBERS ==========
	private Padder padder;

	// ========== IMPLEMENTATION ==========

	/**
	 * Constructor.
	 * 
	 * @param getter the real value getter.
	 * @param paddingValue the character used for padding.
	 * @param prePad if true, padding is at the front of the value. Otherwise it's at the end.
	 */
	public StrippingValueGetter(ValueGetter getter, char paddingValue, boolean prePad)
	{
		this.getter = getter;
		padder = new Padder(paddingValue, prePad);
	}

	public String getValue()
	{
		return padder.strip(getter.getValue());
	}
}
