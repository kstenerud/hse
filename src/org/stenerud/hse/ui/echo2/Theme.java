package org.stenerud.hse.ui.echo2;

import nextapp.echo2.app.ImageReference;
import nextapp.echo2.app.ResourceImageReference;

/**
 * A very basic themeing interface. <br>
 * Icons and images are stored in the classpath under hse/theme/[theme name]/ <br>
 * Images must have both png and gif representations (to keep IE 6 happy). <br>
 * Icons must start with "Icon16". I may extend this in future to allow specification of the desired icon size.
 * 
 * @author Karl Stenerud
 */
public class Theme
{
	// ========== CONSTANTS ==========
	private static final String IMAGE_PATH = "hse/theme/";
	private static final String GIF = "gif";
	private static final String PNG = "png";

	// ========== IMPLEMENTATION ==========

	/**
	 * Get an icon.
	 * 
	 * @param name the name of the icon.
	 * @return the icon.
	 */
	public static ImageReference getIcon(String name)
	{
		return getImage("Icon16" + name);
	}

	/**
	 * Get an image.
	 * 
	 * @param name the name of the image.
	 * @return the image.
	 */
	public static ImageReference getImage(String name)
	{
		Echo2UserInterface ui = Echo2UserInterface.getCurrent();
		String path = IMAGE_PATH + ui.getTheme() + "/" + name + "." + (ui.isSupportsPng() ? PNG : GIF);
		return new ResourceImageReference(path);
	}

}
