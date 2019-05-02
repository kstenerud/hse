package org.stenerud.hse.base.ui.echo2.screen;

import java.util.LinkedList;
import java.util.List;

/**
 * Description of a category, which can contain other categories or screens.
 * 
 * @author Karl Stenerud
 */
public class CategoryDescription extends BaseDescription
{
	public List children = new LinkedList();

	public List getChildren()
	{
		return children;
	}

	public void setChildren(List children)
	{
		this.children = children;
	}
}
