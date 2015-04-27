/**
 * 
 */
package hu.siz.assemblyeditor.builder.diskimage;

/**
 * @author siz
 *
 */
public class DiskFullException extends DiskImageBuilderException {

	private static final long serialVersionUID = -1672946191482082962L;

	public DiskFullException() {
		super();
	}

	public DiskFullException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public DiskFullException(Throwable cause) {
		super(cause);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public DiskFullException(String message, Throwable cause) {
		super(message, cause);
	}
}
