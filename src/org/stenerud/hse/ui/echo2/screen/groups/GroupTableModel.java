package org.stenerud.hse.ui.echo2.screen.groups;

import org.stenerud.hse.business.group.GroupBusiness;
import org.stenerud.hse.data.group.Group;
import org.stenerud.hse.ui.echo2.components.BasicTableModel;

/**
 * Groups table model.
 * 
 * @author Karl Stenerud
 */
public class GroupTableModel extends BasicTableModel
{
	private static final long serialVersionUID = 1L;

	// ========== INJECTED MEMBERS ==========
	private GroupBusiness groupBusiness;

	// ========== IMPLEMENTATION ==========

	public Object getValueAt(int column, int row)
	{
		Group group = getGroup(row);
		String columnName = getInternalColumnName(column);

		if ( columnName.equals(GroupColumnInformation.NAME) )
		{
			return group.getName();
		}
		return null;
	}

	public boolean canDelete(int row)
	{
		return groupBusiness.canDelete(getGroup(row));
	}

	public boolean canEdit(int row)
	{
		return groupBusiness.canUpdate(getGroup(row));
	}

	// ========== UTILITY METHODS ==========

	private Group getGroup(int row)
	{
		return (Group)getInternalValueAt(row);
	}

	// ========== GETTERS AND SETTERS ==========

	public GroupBusiness getGroupBusiness()
	{
		return groupBusiness;
	}

	public void setGroupBusiness(GroupBusiness groupBusiness)
	{
		this.groupBusiness = groupBusiness;
	}
}
