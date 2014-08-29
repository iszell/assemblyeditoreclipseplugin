package hu.siz.assemblyeditor.popup.actions;

import hu.siz.assemblyeditor.utils.StringUtils;

public class ConvertASCIItoPETSCII extends AbstractSourceProcessor {

	/**
	 * AbstractSourceProcessor source file from ASCII to PETSCII coding.
	 */
	public ConvertASCIItoPETSCII() {
		super();
		// msgText = "Converted characters from ASCII to CBM PETSCII.";
	}

	/**
	 * Process one character
	 * <p>
	 * <p>
	 * Converts upper and lower case characters from ASCII to PETSCII and strips
	 * LF (0x0A) characters
	 */
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * hu.siz.assemblyeditor.popup.actions.AbstractSourceProcessor#processChar
	 * (java.lang.StringBuilder, int)
	 */
	@Override
	void processChar(StringBuilder s, int b) {
		s.append(StringUtils.convertCharASCIItoPETSCII(b));
	}
}
