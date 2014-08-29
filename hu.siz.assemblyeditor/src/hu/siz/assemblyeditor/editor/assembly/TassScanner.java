package hu.siz.assemblyeditor.editor.assembly;

import hu.siz.assemblyeditor.editor.AssemblyColorManager;
import hu.siz.assemblyeditor.editor.IColorConstants;
import hu.siz.assemblyeditor.editor.detector.HexadecimalNumberRule;
import hu.siz.assemblyeditor.editor.detector.LabelRule;
import hu.siz.assemblyeditor.editor.detector.WhitespaceDetector;
import hu.siz.assemblyeditor.editor.detector.WordListRule;

import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.text.rules.WhitespaceRule;
import org.eclipse.swt.SWT;

public class TassScanner extends AssemblyScanner {

	@SuppressWarnings("nls")
	private final static String[] TASS_KEYWORDS = { ".byte", ".text", ".char",
			".shift", ".null", ".rta", ".word", ".int", ".offs", ".macro",
			".endm", ".for", ".next", ".if", ".ifne", ".ifeq", ".ifpl",
			".ifmi", ".else", ".elseif", ".fi", ".endif", ".rept", ".include",
			".binary", ".comment", ".endc", ".page", ".endp", ".logical",
			".as", ".al", ".xs", ".xl", ".error", ".warn", ".cerror", ".cwarn",
			".proc", ".pend", ".databank", ".dpage", ".fill", ".align", ".enc",
			".cpu", ".global", ".assert", ".check" };

	public TassScanner(AssemblyColorManager manager) {
		IToken srcOpCode = new Token(new TextAttribute(
				manager.getColor(IColorConstants.ASM_OPCODE), null, SWT.BOLD));
		IToken srcDirective = new Token(
				new TextAttribute(
						manager.getColor(IColorConstants.ASM_DIRECTIVE), null,
						SWT.BOLD));
		IToken srcNumber = new Token(new TextAttribute(
				manager.getColor(IColorConstants.ASM_NUMBER), null, SWT.NULL));
		IToken srcLabel = new Token(new TextAttribute(
				manager.getColor(IColorConstants.ASM_LABEL), null, SWT.BOLD));

		IRule[] rules = new IRule[4];

		// Add Directive detection.
		// TODO Make keywords compiler specific in Editor
		WordListRule wr = new WordListRule(true);
		for (String keyword : TASS_KEYWORDS) {
			wr.addWord(keyword, srcDirective);
		}
		// Add OpCode detection.
		for (String keyword : OPCODES) {
			wr.addWord(keyword, srcOpCode);
		}
		rules[0] = wr;

		// Add hexadecimal number detection
		rules[1] = new HexadecimalNumberRule(srcNumber);

		// Add label detection
		rules[2] = new LabelRule(srcLabel);

		// Add generic whitespace rule.
		rules[3] = new WhitespaceRule(new WhitespaceDetector());

		setRules(rules);
	}
}
