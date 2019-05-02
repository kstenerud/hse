package org.stenerud.hse.ui.echo2.screen.groups;

import org.stenerud.hse.ui.echo2.ColumnInformation;

/**
 * Group column information.
 * 
 * @author Karl Stenerud
 */
public class GroupColumnInformation extends ColumnInformation
{
	// ========== CONSTANTS ==========
	private static final String COLUMN_PREFIX = "group.column.";

	public static final String COMMANDS = COLUMN_PREFIX + "commands";
	public static final String NAME = COLUMN_PREFIX + "name";

	// ========== IMPLEMENTATION ==========

	{
		addColumn(COMMANDS, COLUMN_TYPE_COMMAND);
		addColumn(NAME, COLUMN_TYPE_STRING);
	}
}
