/**
 * 
 */
package hu.siz.assemblyeditor.editor.detector;

import org.eclipse.jface.text.rules.ICharacterScanner;

/**
 * @author Siz
 * 
 */
public class WhitespaceOrDelimiterDetector extends WhitespaceDetector {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * hu.siz.assemblyeditor.editor.detector.WhitespaceDetector#isWhitespace
	 * (char)
	 */
	@Override
	public boolean isWhitespace(char c) {
		return super.isWhitespace(c)
				|| (c == '#' || c == '$' || c == '%' || c == '=' || c == '+'
						|| c == '-' || c == ',' || c == -1);
	}

	/**
	 * Convenience method for {@link #isWhitespace(char)} with EOF checking
	 * 
	 * @param c
	 * @return true if whitespace or EOF
	 * @see ICharacterScanner#EOF
	 */
	public boolean isWhitespace(int c) {
		return c == ICharacterScanner.EOF || isWhitespace((char) c);
	}
}
