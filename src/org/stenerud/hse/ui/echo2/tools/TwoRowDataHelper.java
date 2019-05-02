package org.stenerud.hse.ui.echo2.tools;

import org.stenerud.hse.ui.Messages;
import org.stenerud.hse.ui.echo2.components.StaticTable;

import nextapp.echo2.app.Component;
import echopointng.LabelEx;

/**
 * Helper for 2-row data.
 * 
 * @author Karl Stenerud
 */
public class TwoRowDataHelper extends TableDataHelper
{
	// ========== IMPLEMENTATION ==========

	/**
	 * Constructor.
	 * 
	 * @param messages the localized messages.
	 * @param fieldNameStyle the style for a field name.
	 * @param fieldValueStyle the style for a field value.
	 */
	public TwoRowDataHelper(Messages messages, String fieldNameStyle, String fieldValueStyle)
	{
		super(messages, fieldNameStyle, fieldValueStyle);
	}

	public Component addComponent(StaticTable parent, String fieldName, Component component)
	{
		int colNum = parent.getNumColumns();
		parent.addColumn();
		LabelEx label = new LabelEx(messages.get(fieldName));
		label.setStyleName(fieldNameStyle);
		parent.setComponent(colNum, 0, label);
		component.setStyleName(fieldValueStyle);
		parent.setComponent(colNum, 1, component);
		return component;
	}
}
