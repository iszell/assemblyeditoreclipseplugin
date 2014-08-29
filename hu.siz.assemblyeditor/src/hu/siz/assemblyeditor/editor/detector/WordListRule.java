/**
 * 
 */
package hu.siz.assemblyeditor.editor.detector;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.Token;

/**
 * @author Siz
 * 
 */
public class WordListRule extends WholeWordRule {

	private Map<String, IToken> wordMap = new HashMap<String, IToken>();
	private boolean ignoreCase;

	public WordListRule() {
		this(false);
	}

	public WordListRule(boolean ignoreCase) {
		super(Token.UNDEFINED);
		this.ignoreCase = ignoreCase;
	}

	private String getRealWord(String word) {
		if (ignoreCase) {
			return word.toLowerCase();
		} else {
			return word;
		}
	}

	/**
	 * Add a word to the list
	 * 
	 * @param word
	 */
	public void addWord(String word, IToken token) {
		wordMap.put(getRealWord(word), token);
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
		return wordMap.get(getRealWord(word));
	}
}
