package org.stenerud.hse.base.ui.echo2.component;

import org.stenerud.hse.base.ui.echo2.Icons;
import org.stenerud.hse.base.ui.echo2.Theme;

import echopointng.PushButton;

import nextapp.echo2.app.Button;
import nextapp.echo2.app.Column;
import nextapp.echo2.app.Extent;
import nextapp.echo2.app.Row;
import nextapp.echo2.app.event.ActionEvent;

/**
 * A modal message box that displays a simple message and an "OK" button.
 */
public class OkRequestor extends MessageRequestor
{
	private static final long serialVersionUID = 1L;

	// ========== INJECTED MEMBERS ==========
	private String okButtonText;

	// ========== PRIVATE MEMBERS ==========
	private Button okButton;

	// ========== IMPLEMENTATION ==========

	/**
	 * Constructor.
	 * 
	 * @param title the title of the message box
	 * @param message the message to display
	 * @param okButtonText The text to display in the OK button.
	 */
	public OkRequestor(String title, String message, String okButtonText)
	{
		super(title, message, new Extent(320), new Extent(160));
		this.okButtonText = okButtonText;
	}

	/**
	 * Default Constructor.
	 */
	public OkRequestor()
	{
		super();
	}

	protected void initControls(Row controls)
	{
		okButton = new PushButton(okButtonText, Theme.getIcon16(Icons.YES));
		okButton.setActionCommand("ok");
		addActionComponent(okButton);
		controls.add(okButton);
	}

	protected void initContents(Column contents)
	{
		// Nothing to do.
	}

	protected boolean handleAction(ActionEvent e)
	{
		return true;
	}

	// ========== GETTERS AND SETTERS ==========

	public void setOkButtonText(String okButtonText)
	{
		this.okButtonText = okButtonText;
		okButton.setText(okButtonText);
	}
}
