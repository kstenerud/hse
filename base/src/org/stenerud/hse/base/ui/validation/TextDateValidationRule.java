package org.stenerud.hse.base.ui.validation;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.stenerud.hse.base.ui.Messages;

/**
 * Rule for a text formatted date. <br>
 * Supports format matching, as well as a valid date range.
 * 
 * @author Karl Stenerud
 */
public class TextDateValidationRule implements ValidationRule
{
	// ========== INJECTED MEMBERS ==========
	private Date minDate;
	private Date maxDate;
	private String name;
	private String format;
	private ValueGetter getter;

	// ========== IMPLEMENTATION ==========

	/**
	 * Constructor.
	 * 
	 * @param name the name of this rule.
	 * @param getter the getter that has the value to validate.
	 * @param format the format that the date must be in.
	 * @see java.text.SimpleDateFormat for available formats.
	 */
	public TextDateValidationRule(String name, ValueGetter getter, String format)
	{
		this(name, getter, format, null, null);
	}

	/**
	 * Constructor.
	 * 
	 * @param name the name of this rule.
	 * @param getter the getter that has the value to validate.
	 * @param format the format that the date must be in.
	 * @param minDate the minimum date allowed.
	 * @param maxDate the maximum date allowed.
	 * @see java.text.SimpleDateFormat for available formats.
	 */
	public TextDateValidationRule(String name, ValueGetter getter, String format, Date minDate, Date maxDate)
	{
		this.name = name;
		this.getter = getter;
		this.format = format;
		this.minDate = minDate;
		this.maxDate = maxDate;
	}

	public void validate(Messages messages)
	{
		String stringValue = getter.getValue();
		if ( stringValue.length() > 0 )
		{
			Date value;
			try
			{
				SimpleDateFormat dateFormat = new SimpleDateFormat(format);
				dateFormat.setLenient(false);
				value = dateFormat.parse(stringValue);
			}
			catch ( Exception ex )
			{
				throw new ValidationException(messages.get("validation.mustBe.dateWithFormat", messages.get(name), format));
			}
			if ( null != minDate && value.compareTo(minDate) < 0 )
			{
				throw new ValidationException(messages.get("validation.mustBe.greaterThanOrEqual", messages.get(name),
						new SimpleDateFormat(format).format(minDate)));
			}
			if ( null != maxDate && value.compareTo(maxDate) > 0 )
			{
				throw new ValidationException(messages.get("validation.mustBe.lessThanOrEqual", messages.get(name),
						new SimpleDateFormat(format).format(maxDate)));
			}
		}
	}
}
