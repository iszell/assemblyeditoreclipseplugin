/**
 * 
 */
package hu.siz.assemblyeditor.editor.detector;

import org.eclipse.jface.text.rules.IToken;

/**
 * @author Siz
 * 
 */
public class HexadecimalNumberRule extends WholeWordRule {

	public HexadecimalNumberRule(IToken token) {
		super(token);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * hu.siz.assemblyeditor.editor.detector.WholeWordRule#evaluateWord(java
	 * .lang.String)
	 */
	@Override
	protected IToken evaluateWord(String word) {
		for (char c : word.toCharArray()) {
			if (!checkCharacter(c)) {
				return null;
			}
		}
		return getToken();
	}

	private boolean checkCharacter(int c) {
		return (c >= '0' && c <= '9') || (c >= 'a' && c <= 'f')
				|| (c >= 'A' && c <= 'F');
	}
}
