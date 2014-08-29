/**
 * 
 */
package hu.siz.assemblyeditor;

/**
 * @author siz
 *
 */
public class AssemblyException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1923528891758914247L;

	/**
	 * 
	 */
	public AssemblyException() {
		super();
	}

	/**
	 * @param message
	 */
	public AssemblyException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public AssemblyException(Throwable cause) {
		super(cause);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public AssemblyException(String message, Throwable cause) {
		super(message, cause);
	}
}
