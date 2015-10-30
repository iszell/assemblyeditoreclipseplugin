/**
 * 
 */
package hu.siz.assemblyeditor.editor.detector;

import org.eclipse.jface.text.rules.ICharacterScanner;
import org.eclipse.jface.text.rules.IPredicateRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.Token;

/**
 * @author Siz
 * 
 */
public class LabelRule implements IPredicateRule {

	private static final WhitespaceOrDelimiterDetector wd = new WhitespaceOrDelimiterDetector();
	private IToken token;

	/**
	 * @param token
	 */
	public LabelRule(IToken token) {
		super();
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
		return evaluate(scanner, false);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.text.rules.IPredicateRule#getSuccessToken()
	 */
	@Override
	public IToken getSuccessToken() {
		return token;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.text.rules.IPredicateRule#evaluate(org.eclipse.jface
	 * .text.rules.ICharacterScanner, boolean)
	 */
	@Override
	public IToken evaluate(ICharacterScanner scanner, boolean resume) {
		if (isFirstNonSpaceInLine(scanner)) {
			int c = scanner.read();
			if (!wd.isWhitespace(c)) {
				final StringBuilder sb = new StringBuilder();
				while (!wd.isWhitespace(c)) {
					sb.append((char) c);
					c = scanner.read();
				}
				if (c != ICharacterScanner.EOF) {
					scanner.unread();
				}
//				final String word = sb.toString();
//				if(word.charAt(0)!='.') {
					return getSuccessToken();
//				}
			} else {
				scanner.unread();
			}
		}
		return Token.UNDEFINED;
	}

	/**
	 * Checks if current character is the first non-whitespace character in line
	 * 
	 * @param scanner
	 * @return
	 */
	private boolean isFirstNonSpaceInLine(ICharacterScanner scanner) {
		final int column = scanner.getColumn();
		boolean whiteSpaceOnly = true;
		while (scanner.getColumn() > 0) {
			scanner.unread();
		}
		while (scanner.getColumn() < column) {
			int c = scanner.read();
			if (!wd.isWhitespace(c)) {
				whiteSpaceOnly = false;
			}
		}
		return whiteSpaceOnly;
	}
}
