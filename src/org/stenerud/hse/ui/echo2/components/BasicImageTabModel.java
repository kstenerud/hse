package org.stenerud.hse.ui.echo2.components;

import org.stenerud.hse.ui.echo2.Images;
import org.stenerud.hse.ui.echo2.Theme;

import nextapp.echo2.app.Component;
import nextapp.echo2.app.FillImage;
import nextapp.echo2.app.event.ChangeListener;
import echopointng.ButtonEx;
import echopointng.TabbedPane;
import echopointng.tabbedpane.TabImageRenderer;
import echopointng.tabbedpane.TabModel;

/**
 * A facade that overrides tab rendering, replacing the normal component with a background-imaged button.
 * 
 * @author Karl Stenerud
 */
public class BasicImageTabModel implements TabModel
{
	// ========== INJECTED MEMBERS ==========
	private TabModel model;

	// ========== IMPLEMENTATION ==========

	/**
	 * Constructor.
	 * 
	 * @param model the TabModel to provide a facade for.
	 */
	public BasicImageTabModel(TabModel model)
	{
		this.model = model;
	}

	public TabImageRenderer getTabImageRenderer()
	{
		return model.getTabImageRenderer();
	}

	public int size()
	{
		return model.size();
	}

	public Component getTabAt(TabbedPane tabbedPane, int index, boolean isSelected)
	{
		/*
		 * Setting the background color of one tab seems to affect the entire tab panel, so use images instead.
		 */
		ButtonEx component = (ButtonEx)model.getTabAt(tabbedPane, index, isSelected);
		if ( isSelected )
		{
			component.setBackgroundImage(new FillImage(Theme.getImage(Images.TAB_SELECTED)));
		}
		else
		{
			component.setBackgroundImage(new FillImage(Theme.getImage(Images.TAB_UNSELECTED)));
		}
		return component;
	}

	public Component getTabContentAt(int index)
	{
		Component component = model.getTabContentAt(index);
		return component;
	}

	public void releaseTabAt(int index)
	{
		model.releaseTabAt(index);
	}

	public int indexOfTab(Component tabComponent)
	{
		return model.indexOfTab(tabComponent);
	}

	public void addChangeListener(ChangeListener listener)
	{
		model.addChangeListener(listener);
	}

	public void removeChangeListener(ChangeListener listener)
	{
		model.removeChangeListener(listener);
	}

	public int indexOfTabContent(Component arg0)
	{
		return model.indexOfTab(arg0);
	}
}
