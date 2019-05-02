package org.stenerud.hse.base.ui.echo2.tool;

import org.stenerud.hse.base.ui.Messages;
import org.stenerud.hse.base.ui.echo2.component.StaticTable;

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

	public Component addSeparator(StaticTable parent, String valueName)
	{
		int colNum = parent.getNumColumns();
		parent.addColumn();
		LabelEx label = new LabelEx();
		label.setStyleName(fieldNameStyle);
		parent.setComponent(colNum, 0, label);
		LabelEx component = new LabelEx(messages.get(valueName));
		component.setStyleName(fieldNameStyle);
		parent.setComponent(colNum, 1, component);
		return component;
	}
}
