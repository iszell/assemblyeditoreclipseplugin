package hu.siz.assemblyeditor.editor.assembly;

import org.eclipse.jface.text.rules.IPredicateRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.RuleBasedPartitionScanner;
import org.eclipse.jface.text.rules.SingleLineRule;
import org.eclipse.jface.text.rules.Token;

public class AssemblyPartitionScanner extends RuleBasedPartitionScanner {
	public final static String ASM_COMMENT = "__asm_comment"; //$NON-NLS-1$
	public final static String ASM_STRING = "__asm_string"; //$NON-NLS-1$

	private final static String[] contentTypes = { ASM_COMMENT, ASM_STRING };

	public AssemblyPartitionScanner() {

		IToken asmComment = new Token(ASM_COMMENT);
		IToken asmString = new Token(ASM_STRING);

		IPredicateRule[] rules = new IPredicateRule[3];

		rules[0] = new SingleLineRule(";", null, asmComment); //$NON-NLS-1$ //$NON-NLS-2$
		rules[1] = new SingleLineRule(".comment", null, asmComment); //$NON-NLS-1$ //$NON-NLS-2$
		rules[2] = new SingleLineRule("\"", "\"", asmString); //$NON-NLS-1$ //$NON-NLS-2$

		setPredicateRules(rules);
	}

	public static String[] getContentTypes() {
		return contentTypes;
	}
}
