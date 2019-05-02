package org.stenerud.hse.ui.echo2.screen.users;

import org.stenerud.hse.ui.echo2.ColumnInformation;

/**
 * User column information.
 * 
 * @author Karl Stenerud
 */
public class UserColumnInformation extends ColumnInformation
{
	// ========== CONSTANTS ==========
	private static final String COLUMN_PREFIX = "user.column.";

	public static final String COMMANDS = COLUMN_PREFIX + "commands";
	public static final String NAME = COLUMN_PREFIX + "name";
	public static final String GROUP = COLUMN_PREFIX + "group";

	// ========== IMPLEMENTATION ==========

	{
		addColumn(COMMANDS, COLUMN_TYPE_COMMAND);
		addColumn(NAME, COLUMN_TYPE_STRING);
		addColumn(GROUP, COLUMN_TYPE_STRING);
	}
}
