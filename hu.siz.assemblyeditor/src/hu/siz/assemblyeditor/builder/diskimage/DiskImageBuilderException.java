/**
 * 
 */
package hu.siz.assemblyeditor.builder.diskimage;

import hu.siz.assemblyeditor.AssemblyException;

/**
 * @author siz
 * 
 */
public class DiskImageBuilderException extends AssemblyException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5542372588548164330L;

	/**
	 * 
	 */
	public DiskImageBuilderException() {
		super();
	}

	/**
	 * @param message
	 */
	public DiskImageBuilderException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public DiskImageBuilderException(Throwable cause) {
		super(cause);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public DiskImageBuilderException(String message, Throwable cause) {
		super(message, cause);
	}

}
