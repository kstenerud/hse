package org.stenerud.hse.ui.echo2.screen.users;

import org.stenerud.hse.business.user.UserBusiness;
import org.stenerud.hse.data.user.User;
import org.stenerud.hse.ui.echo2.components.BasicTableModel;

/**
 * Users table model.
 * 
 * @author Karl Stenerud
 */
public class UserTableModel extends BasicTableModel
{
	private static final long serialVersionUID = 1L;

	// ========== INJECTED MEMBERS ==========
	private UserBusiness userBusiness;

	// ========== IMPLEMENTATION ==========

	public Object getValueAt(int column, int row)
	{
		User user = getUser(row);
		String columnName = getInternalColumnName(column);

		if ( columnName.equals(UserColumnInformation.NAME) )
		{
			return user.getName();
		}

		if ( columnName.equals(UserColumnInformation.GROUP) )
		{
			return user.getUserGroup().getName();
		}
		return null;
	}

	public boolean canDelete(int row)
	{
		return userBusiness.canDelete(getUser(row));
	}

	public boolean canUpdate(int row)
	{
		return userBusiness.canUpdate(getUser(row));
	}

	// ========== UTILITY METHODS ==========

	private User getUser(int row)
	{
		return (User)getInternalValueAt(row);
	}

	// ========== GETTERS AND SETTERS ==========

	public UserBusiness getUserBusiness()
	{
		return userBusiness;
	}

	public void setUserBusiness(UserBusiness userBusiness)
	{
		this.userBusiness = userBusiness;
	}
}
