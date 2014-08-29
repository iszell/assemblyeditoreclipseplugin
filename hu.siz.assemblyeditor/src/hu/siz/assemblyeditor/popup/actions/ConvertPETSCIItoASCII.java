package hu.siz.assemblyeditor.popup.actions;

import hu.siz.assemblyeditor.utils.StringUtils;

public class ConvertPETSCIItoASCII extends AbstractSourceProcessor {

	/**
	 * AbstractSourceProcessor source file from ASCII to PETSCII coding.
	 */
	public ConvertPETSCIItoASCII() {
		super();
//		msgText = "Converted characters from CBM PETSCII to ASCII.";
	}

	/**
	 * Process one character
	 * <p>
	 * <p>Converts upper and lower case characters from ASCII to PETSCII and 
	 * changes CR (0x0D) characters to CR+LF (0x0D+0x0A)
	 */
	/* (non-Javadoc)
	 * @see hu.siz.assemblyeditor.popup.actions.AbstractSourceProcessor#processChar(java.lang.StringBuilder, int)
	 */
	@Override
	void processChar(StringBuilder s, int b) {
		s.append(StringUtils.convertCharPETSCIItoASCII(b));
	}
}
