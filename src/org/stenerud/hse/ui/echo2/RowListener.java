package org.stenerud.hse.ui.echo2;

import nextapp.echo2.app.event.ActionListener;

/**
 * ActionListener that stores the row number.
 */
public interface RowListener extends ActionListener, Cloneable
{
	/**
	 * Set the row associated with an action.
	 * 
	 * @param row the row.
	 */
	public void setRow(int row);

	public Object clone();
}
