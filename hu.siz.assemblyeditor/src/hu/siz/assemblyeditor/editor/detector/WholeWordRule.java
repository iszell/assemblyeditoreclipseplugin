/**
 * 
 */
package hu.siz.assemblyeditor.editor.detector;

import org.eclipse.jface.text.rules.ICharacterScanner;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.Token;

/**
 * @author Siz
 * 
 */
public class WholeWordRule implements IRule {

	private final static WhitespaceOrDelimiterDetector wd = new WhitespaceOrDelimiterDetector();
	private IToken token;

	public WholeWordRule(IToken token) {
		this.token = token;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.text.rules.IRule#evaluate(org.eclipse.jface.text.rules
	 * .ICharacterScanner)
	 */
	@Override
	public IToken evaluate(ICharacterScanner scanner) {
		scanner.unread();
		int p = scanner.read();
		if (wd.isWhitespace(p)) {
			String word = getWholeWord(scanner);
			if (word != null) {
				IToken resultToken = evaluateWord(word);
				if (resultToken != null) {
					return resultToken;
				} else {
					for (int i = 0; i < word.length(); i++) {
						scanner.unread();
					}
				}
			}
		}
		return Token.UNDEFINED;
	}

	/**
	 * Returns the token for the word
	 * 
	 * @param word
	 * @return
	 */
	protected IToken evaluateWord(String word) {
		return token;
	}

	/**
	 * Get a whole word
	 * 
	 * @param scanner
	 * @return
	 */
	private String getWholeWord(ICharacterScanner scanner) {
		final StringBuffer sb = new StringBuffer();
		int c = scanner.read();
		while (!wd.isWhitespace(c)) {
			sb.append((char) c);
			c = scanner.read();
		}
		scanner.unread();
		if (sb.length() == 0) {
			return null;
		}

		return sb.toString();
	}

	/**
	 * @return the token
	 */
	protected IToken getToken() {
		return token;
	}
}
