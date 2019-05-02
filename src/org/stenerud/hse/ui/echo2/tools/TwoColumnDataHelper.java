package org.stenerud.hse.ui.echo2.tools;

import org.stenerud.hse.ui.Messages;
import org.stenerud.hse.ui.echo2.components.StaticTable;

import nextapp.echo2.app.Component;
import echopointng.LabelEx;

/**
 * Helper for 2-column data.
 * 
 * @author Karl Stenerud
 */
public class TwoColumnDataHelper extends TableDataHelper
{
	// ========== IMPLEMENTATION ==========

	/**
	 * Constructor.
	 * 
	 * @param messages the localized messages.
	 * @param fieldNameStyle the style for a field name.
	 * @param fieldValueStyle the style for a field value.
	 */
	public TwoColumnDataHelper(Messages messages, String fieldNameStyle, String fieldValueStyle)
	{
		super(messages, fieldNameStyle, fieldValueStyle);
	}

	public Component addComponent(StaticTable parent, String fieldName, Component component)
	{
		int rowNum = parent.getNumRows();
		parent.addRow();
		LabelEx label = new LabelEx(messages.get(fieldName));
		label.setStyleName(fieldNameStyle);
		parent.setComponent(0, rowNum, label);
		component.setStyleName(fieldValueStyle);
		parent.setComponent(1, rowNum, component);
		return component;
	}
}
