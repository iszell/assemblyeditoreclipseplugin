package hu.siz.assemblyeditor.editor;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;

/**
 * Color manager for the assembly plugin
 * 
 * @author Siz
 * 
 */
public class AssemblyColorManager {

	protected Map<RGB, Color> colorTable = new HashMap<RGB, Color>(10);

	/**
	 * Disposes cached colors
	 */
	public void dispose() {
		for (Color c : this.colorTable.values()) {
			c.dispose();
		}
	}

	/**
	 * Returns a cached color entry or creates a new one and puts it into the
	 * cache
	 * 
	 * @param rgb
	 *            the <code>RGB</code> color code for the requested color
	 * @return the <code>Color</code> object
	 */
	public Color getColor(RGB rgb) {
		Color color = this.colorTable.get(rgb);
		if (color == null) {
			color = new Color(Display.getCurrent(), rgb);
			this.colorTable.put(rgb, color);
		}
		return color;
	}
}
