package org.stenerud.hse.ui.echo2.screen.groups;

import nextapp.echo2.app.Button;
import nextapp.echo2.app.Component;
import nextapp.echo2.app.Row;
import nextapp.echo2.app.Table;
import nextapp.echo2.app.table.TableCellRenderer;

import org.stenerud.hse.ui.echo2.ColumnInformation;
import org.stenerud.hse.ui.echo2.Icons;
import org.stenerud.hse.ui.echo2.RowListener;
import org.stenerud.hse.ui.echo2.Theme;
import org.stenerud.hse.ui.echo2.components.BasicEditorTable;

import echopointng.PushButton;

/**
 * Groups table.
 * 
 * @author Karl Stenerud
 */
public class GroupTable extends BasicEditorTable
{
	private static final long serialVersionUID = 1L;

	// ========== INJECTED MEMBERS ==========
	private RowListener editButtonListener;
	private RowListener deleteButtonListener;

	// ========== IMPLEMENTATION ==========

	/**
	 * Constructor.
	 */
	public GroupTable()
	{
		super();
		setDefaultRenderer(Object.class, tableCellRenderer);
	}

	private TableCellRenderer tableCellRenderer = new TableCellRenderer()
	{
		private static final long serialVersionUID = 1L;

		public Component getTableCellRendererComponent(Table table, Object value, int column, int row)
		{
			int columnType = getColumnType(column);

			if ( columnType == ColumnInformation.COLUMN_TYPE_COMMAND )
			{
				GroupTableModel model = (GroupTableModel)getModel();
				Row rowComponent = new Row();
				Button button;
				RowListener listener;
				if ( model.canEdit(row) )
				{
					button = new PushButton(Theme.getIcon(Icons.EDIT));
					button.setToolTipText(messages.get("tip.edit"));
					listener = (RowListener)editButtonListener.clone();
					listener.setRow(row);
					button.addActionListener(listener);
					rowComponent.add(button);
				}
				if ( model.canDelete(row) )
				{
					button = new PushButton(Theme.getIcon(Icons.DELETE));
					button.setToolTipText(messages.get("tip.delete"));
					listener = (RowListener)deleteButtonListener.clone();
					listener.setRow(row);
					button.addActionListener(listener);
					rowComponent.add(button);
				}
				return rowComponent;
			}

			return renderDefaultComponent(column, value);
		}
	};

	// ========== GETTERS AND SETTERS ==========

	public RowListener getDeleteButtonListener()
	{
		return deleteButtonListener;
	}

	public void setDeleteButtonListener(RowListener deleteButtonListener)
	{
		this.deleteButtonListener = deleteButtonListener;
	}

	public RowListener getEditButtonListener()
	{
		return editButtonListener;
	}

	public void setEditButtonListener(RowListener editButtonListener)
	{
		this.editButtonListener = editButtonListener;
	}
}
