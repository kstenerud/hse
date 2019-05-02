package org.stenerud.hse.base.ui.echo2.component;

import java.util.EventListener;

import nextapp.echo2.app.Extent;
import nextapp.echo2.app.Row;
import nextapp.echo2.app.SplitPane;
import nextapp.echo2.app.TextField;
import nextapp.echo2.app.button.AbstractButton;
import nextapp.echo2.app.event.ActionEvent;
import nextapp.echo2.app.event.ActionListener;

/**
 * A modal requestor. <br>
 * This is to be the base class for most modal dialog boxes.
 * 
 * @author Karl Stenerud
 */
public abstract class Requestor extends CustomWindowPane
{
	private static final long serialVersionUID = 1L;

	// ========== INTERNAL CLASSES ==========

	private ActionListener actionListener = new ActionListener()
	{
		private static final long serialVersionUID = 1L;

		public void actionPerformed(ActionEvent e)
		{
			if ( !handleAction(e) )
			{
				return;
			}
			getParent().remove(Requestor.this);
			EventListener[] listeners = getEventListenerList().getListeners(ActionListener.class);
			ActionEvent event = new ActionEvent(this, e.getActionCommand());
			for ( int i = 0; i < listeners.length; ++i )
			{
				((ActionListener)listeners[i]).actionPerformed(event);
			}
		}
	};

	// ========== IMPLEMENTATION ==========

	/**
	 * Handle an action from a control component. <br>
	 * This method gets called by the internal action listener, and allows the
	 * implementation to add extra processing in response to an action. <br>
	 * If true is returned, the requestor is closed, and all registered event
	 * listeners are notified. <br>
	 * If false is returned, the requestor remains open, and the registered
	 * event listeners are NOT notified.
	 * 
	 * @param ev the event
	 * @return true if processing is to continue.
	 */
	protected abstract boolean handleAction(ActionEvent ev);

	/**
	 * Add a component that will notify the base action listener.
	 * 
	 * @param component the component to add.
	 */
	protected void addActionComponent(AbstractButton component)
	{
		component.addActionListener(actionListener);
	}

	/**
	 * Add a component that will notify the base action listener.
	 * 
	 * @param component the component to add.
	 */
	protected void addActionComponent(TextField component)
	{
		component.addActionListener(actionListener);
	}

	/**
	 * Add a component that will notify the base action listener.
	 * 
	 * @param component the component to add.
	 * @param action the action command to use.
	 */
	protected void addActionComponent(TextField component, String action)
	{
		component.setActionCommand(action);
		component.addActionListener(actionListener);
	}

	/**
	 * Force an action event.
	 * 
	 * @param ev the event.
	 */
	protected void forceAction(ActionEvent ev)
	{
		actionListener.actionPerformed(ev);
	}

	/**
	 * Constructor.
	 * 
	 * @param title the title of the message box
	 * @param width the width of the requestor
	 * @param height the height of the requestor
	 */
	public Requestor(String title, Extent width, Extent height)
	{
		super(title, width, height);
		setResizable(false);
		setModal(true);
	}

	/**
	 * Default Constructor.
	 */
	public Requestor()
	{
		super();
		setResizable(false);
		setModal(true);
	}

	protected void initComponents()
	{
		setStyleName("Requestor");

		SplitPane splitPane = new SplitPane(SplitPane.ORIENTATION_VERTICAL_BOTTOM_TOP, new Extent(32));
		add(splitPane);

		Row controlsRow = new Row();
		controlsRow.setStyleName("Requestor");
		splitPane.add(controlsRow);

		initControls(controlsRow);
		initContents(splitPane);
	}

	/**
	 * Initialize the controls row.
	 * 
	 * @param controls the row that contains the controls.
	 */
	protected abstract void initControls(Row controls);

	/**
	 * Initialize the main contents of the requestor.
	 * 
	 * @param container a container that can contain the main contents.
	 */
	protected abstract void initContents(SplitPane container);

	/**
	 * Add a listener to this object.
	 * 
	 * @param l the listener
	 */
	public void addActionListener(ActionListener l)
	{
		getEventListenerList().addListener(ActionListener.class, l);
	}

	/**
	 * Remove a listener from this object.
	 * 
	 * @param l the listener
	 */
	public void removeActionListener(ActionListener l)
	{
		getEventListenerList().removeListener(ActionListener.class, l);
	}
}
