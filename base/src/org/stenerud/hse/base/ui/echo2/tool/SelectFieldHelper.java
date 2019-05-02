package org.stenerud.hse.base.ui.echo2.tool;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import nextapp.echo2.app.SelectField;
import nextapp.echo2.app.list.DefaultListModel;

/**
 * Helper class for SelectFields. <br>
 * This class maintains mappings between text contents (displayed as selectable
 * fields in the SelectField) and arbitrary objects. <br>
 * This allows easy mapping between objects and fields in the SelectField.
 * 
 * @author Karl Stenerud
 */
public class SelectFieldHelper implements Cloneable
{
	// ========== INTERNAL CLASSES ==========

	private static class ItemDescriptor implements Comparable
	{
		public int index;
		public String text;
		public Object item;
		public boolean visible;
		public int visibleIndex;

		public ItemDescriptor(int index, String text, Object item, boolean visible)
		{
			this.index = index;
			this.text = text;
			this.item = item;
			this.visible = visible;
		}

		public boolean equals(Object o)
		{
			if ( !(o instanceof ItemDescriptor) )
			{
				return false;
			}
			return this.index == ((ItemDescriptor)o).index;
		}

		public int compareTo(Object o)
		{
			if ( !(o instanceof ItemDescriptor) )
			{
				return -1;
			}
			return this.index - ((ItemDescriptor)o).index;
		}
	}

	// ========== INJECTED MEMBERS ==========
	private SelectField field;

	// ========== PRIVATE MEMBERS ==========
	private DefaultListModel listModel = new DefaultListModel();
	private Map byText = new HashMap();
	private Map byItem = new HashMap();
	private Map byIndex = new TreeMap();
	private int currentIndex = 0;

	// ========== IMPLEMENTATION ==========

	/**
	 * Add an item to the select field.
	 * 
	 * @param text the text to display in the select field.
	 * @param item the associated item.
	 */
	public void addItem(String text, Object item)
	{
		if ( null == text )
		{
			throw new NullPointerException("text is null");
		}
		if ( null == item )
		{
			throw new NullPointerException("item is null");
		}
		ItemDescriptor descriptor = new ItemDescriptor(currentIndex++, text, item, true);

		byText.put(descriptor.text, descriptor);
		byItem.put(descriptor.item, descriptor);
		byIndex.put(new Integer(descriptor.index), descriptor);
		if ( descriptor.visible )
		{
			descriptor.visibleIndex = listModel.size();
			listModel.add(descriptor.visibleIndex, descriptor.text);
		}
	}

	/**
	 * Set the visibility of an item.
	 * 
	 * @param item the item.
	 * @param visible if true, it is visible.
	 */
	public void setItemVisible(Object item, boolean visible)
	{
		ItemDescriptor descriptor = (ItemDescriptor)byItem.get(item);
		if ( descriptor.visible != visible )
		{
			descriptor.visible = visible;
			rebuildModel();
		}
	}

	/**
	 * Manage a select field. <br>
	 * The select field will be populated by whatever associations are contained
	 * in the helper.
	 * 
	 * @param selectField the select field to manage.
	 */
	public void manageField(SelectField selectField)
	{
		this.field = selectField;
		int index = selectField.getSelectedIndex();
		selectField.setModel(listModel);
		if ( index >= 0 )
			selectField.setSelectedIndex(index);
	}

	/**
	 * Get the currently managed select field.
	 * 
	 * @return the select field.
	 */
	public SelectField getManagedField()
	{
		return field;
	}

	/**
	 * Get the item associated with the currently selected field.
	 * 
	 * @return the current item.
	 */
	public Object getSelectedItem()
	{
		if ( field.getModel().size() == 0 )
		{
			return null;
		}
		return ((ItemDescriptor)byText.get(field.getSelectedItem())).item;
	}

	/**
	 * Get the text associated with the currently selected field.
	 * 
	 * @return the current text.
	 */
	public String getSelectedText()
	{
		if ( field.getModel().size() == 0 )
		{
			return null;
		}
		return ((ItemDescriptor)byText.get(field.getSelectedItem())).text;
	}

	/**
	 * Check if this helper contains an item.
	 * 
	 * @param item the item to check for.
	 * @return true if this helper contains the item.
	 */
	public boolean contains(Object item)
	{
		return byItem.containsKey(item);
	}

	/**
	 * Get the number of entries in this helper.
	 * 
	 * @return the size.
	 */
	public int getSize()
	{
		return byIndex.size();
	}

	/**
	 * Check if the select field is empty.
	 * 
	 * @return true if it is empty.
	 */
	public boolean isEmpty()
	{
		return getSize() == 0;
	}

	/**
	 * Set the selected index of the SelectField. <br>
	 * This sets the index according to the SelectField's indexing, NOT the
	 * helper's. <br>
	 * Any non-visible items will not be present in the SelectField.
	 * 
	 * @param index the index to select.
	 */
	public void setSelectedIndex(int index)
	{
		field.setSelectedIndex(index);
	}

	/**
	 * Set the currently selected field by its associated item.
	 * 
	 * @param item the item to use in selecting the field.
	 */
	public void setSelectedItem(Object item)
	{
		if ( null == item )
		{
			throw new NullPointerException("item is null");
		}
		ItemDescriptor descriptor = (ItemDescriptor)byItem.get(item);
		field.setSelectedIndex(descriptor.visibleIndex);
	}

	/**
	 * Warning: This is NOT a deep clone!
	 */
	public Object clone()
	{
		try
		{
			return super.clone();
		}
		catch ( CloneNotSupportedException e )
		{
			// Will never happen
			throw new RuntimeException(e);
		}
	}

	// ========== UTILITY METHODS ==========

	/**
	 * Rebuild the list model for this select field.
	 */
	private void rebuildModel()
	{
		listModel.removeAll();
		for ( Iterator iter = byIndex.values().iterator(); iter.hasNext(); )
		{
			ItemDescriptor descriptor = (ItemDescriptor)iter.next();
			if ( descriptor.visible )
			{
				descriptor.visibleIndex = listModel.size();
				listModel.add(descriptor.visibleIndex, descriptor.text);
			}
		}
	}
}
