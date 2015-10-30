/**
 * 
 */
package hu.siz.assemblyeditor.editor.assembly;

import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.RuleBasedScanner;
import org.eclipse.jface.text.rules.SingleLineRule;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.text.rules.WhitespaceRule;
import org.eclipse.swt.SWT;

import hu.siz.assemblyeditor.editor.AssemblyColorManager;
import hu.siz.assemblyeditor.editor.IColorConstants;
import hu.siz.assemblyeditor.editor.detector.HexadecimalNumberRule;
import hu.siz.assemblyeditor.editor.detector.LabelRule;
import hu.siz.assemblyeditor.editor.detector.WhitespaceDetector;
import hu.siz.assemblyeditor.editor.detector.WordListRule;

/**
 * @author Siz
 *
 */
public abstract class AssemblyScanner extends RuleBasedScanner {

	/**
	 * 6502 assembly operation code mnemonics
	 */
	@SuppressWarnings("nls")
	private static final String[] OPCODES = { "adc", "and", "asl", "bcc", "bcs", "beq", "bit", "bmi", "bne", "bpl",
			"brk", "bvc", "bvs", "clc", "cld", "cli", "clv", "cmp", "cpx", "cpy", "dec", "dex", "dey", "eor", "inc",
			"inx", "iny", "jmp", "jsr", "lda", "ldx", "ldy", "lsr", "nop", "ora", "pha", "php", "pla", "plp", "rol",
			"ror", "rti", "rts", "sbc", "sec", "sed", "sei", "sta", "stx", "sty", "tax", "tay", "tsx", "txa", "txs",
			"tya" };

	public AssemblyScanner(AssemblyColorManager manager, String[] keywords) {
		super();
		
		IToken srcOpCode = new Token(new TextAttribute(manager.getColor(IColorConstants.ASM_OPCODE), null, SWT.BOLD));
		IToken srcDirective = new Token(
				new TextAttribute(manager.getColor(IColorConstants.ASM_DIRECTIVE), null, SWT.BOLD));
		IToken srcNumber = new Token(new TextAttribute(manager.getColor(IColorConstants.ASM_NUMBER), null, SWT.NULL));
		IToken srcLabel = new Token(new TextAttribute(manager.getColor(IColorConstants.ASM_LABEL), null, SWT.BOLD));
		IToken asmComment = new Token(new TextAttribute(manager.getColor(IColorConstants.ASM_COMMENT), null, SWT.BOLD));
		IToken asmString = new Token(new TextAttribute(manager.getColor(IColorConstants.STRING), null, SWT.BOLD));

		// Add Directive detection.
		final WordListRule wr = new WordListRule(true);
		for (String keyword : keywords) {
			wr.addWord(keyword, srcDirective);
		}

		IRule[] rules = new IRule[7];
		
		int rule = 0;
		
		rules[rule++] = new SingleLineRule(";", "", asmComment); //$NON-NLS-1$ //$NON-NLS-2$
		rules[rule++] = new SingleLineRule(".comment", "", asmComment); //$NON-NLS-1$ //$NON-NLS-2$
		rules[rule++] = new SingleLineRule("\"", "\"", asmString); //$NON-NLS-1$ //$NON-NLS-2$
		
		// Add OpCode detection.
		for (String keyword : OPCODES) {
			wr.addWord(keyword, srcOpCode);
		}
		rules[rule++] = wr;

		// Add hexadecimal number detection
		rules[rule++] = new HexadecimalNumberRule(srcNumber);

		// Add label detection
		rules[rule++] = new LabelRule(srcLabel);

		// Add generic whitespace rule.
		rules[rule++] = new WhitespaceRule(new WhitespaceDetector());

		setRules(rules);
	}
}
