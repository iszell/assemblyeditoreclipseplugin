/**
 * 
 */
package hu.siz.assemblyeditor.utils;

import hu.siz.assemblyeditor.AssemblyEditorPlugin;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

/**
 * @author siz
 * 
 */
public class AssemblyUtils {

	/**
	 * Creates an Eclipse log message
	 * 
	 * @param code
	 *            severity code
	 * @param message
	 * @see IStatus
	 */
	public static void createLogEntry(int code, String message) {
		IStatus s = new Status(code, AssemblyEditorPlugin.PLUGIN_ID, message);
		AssemblyEditorPlugin.getDefault().getLog().log(s);
	}

	/**
	 * Creates a log entry with error severity using an Exception message
	 * 
	 * @param e
	 * @see #createLogEntry(int, String)
	 */
	public static void createLogEntry(Exception e) {
		createLogEntry(IStatus.ERROR, e.getLocalizedMessage());
	}
}
