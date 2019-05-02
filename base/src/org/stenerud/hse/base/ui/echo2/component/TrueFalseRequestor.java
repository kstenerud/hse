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
 * A modal message box with buttons for "True" and "False" responses.
 */
public class TrueFalseRequestor extends MessageRequestor
{
	private static final long serialVersionUID = 1L;

	// ========== CONSTANTS ==========
	public static final String COMMAND_TRUE = Boolean.TRUE.toString();
	public static final String COMMAND_FALSE = Boolean.FALSE.toString();

	// ========== INJECTED MEMBERS ==========
	private String trueString;
	private String falseString;

	// ========== PRIVATE MEMBERS ==========
	private Button trueButton;
	private Button falseButton;

	// ========== IMPLEMENTATION ==========

	/**
	 * Constructor.
	 * 
	 * @param title the title of the message box
	 * @param message the message to display
	 * @param trueString the string to display in the "True" button.
	 * @param falseString the string to display in the "False" button.
	 */
	public TrueFalseRequestor(String title, String message, String trueString, String falseString)
	{
		super(title, message, new Extent(320), new Extent(160));
		this.trueString = trueString;
		this.falseString = falseString;
	}

	/**
	 * Default Constructor.
	 */
	public TrueFalseRequestor()
	{
		super();
	}

	protected void initControls(Row controls)
	{
		trueButton = new PushButton(trueString, Theme.getIcon16(Icons.YES));
		trueButton.setActionCommand(COMMAND_TRUE);
		addActionComponent(trueButton);
		controls.add(trueButton);

		falseButton = new PushButton(falseString, Theme.getIcon16(Icons.NO));
		falseButton.setActionCommand(COMMAND_FALSE);
		addActionComponent(falseButton);
		controls.add(falseButton);
	}

	protected void initContents(Column contents)
	{
		// Nothing to do
	}

	protected boolean handleAction(ActionEvent e)
	{
		return true;
	}

	/**
	 * Check if an action event corresponds to the "True" result.
	 * 
	 * @param e the action event to test.
	 * @return true if the event corresponds to "True".
	 */
	public static boolean commandIsTrue(ActionEvent e)
	{
		return e.getActionCommand().equals(COMMAND_TRUE);
	}

	/**
	 * Enable/disable the "True" button.
	 * 
	 * @param enable if true, enable.
	 */
	protected void setTrueButtonEnable(boolean enable)
	{
		trueButton.setEnabled(enable);
	}

	// ========== GETTERS AND SETTERS ==========

	public void setFalseString(String falseString)
	{
		this.falseString = falseString;
		falseButton.setText(falseString);
	}

	public void setTrueString(String trueString)
	{
		this.trueString = trueString;
		trueButton.setText(trueString);
	}
}
