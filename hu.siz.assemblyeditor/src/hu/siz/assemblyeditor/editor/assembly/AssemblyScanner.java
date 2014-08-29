/**
 * 
 */
package hu.siz.assemblyeditor.editor.assembly;

import org.eclipse.jface.text.rules.RuleBasedScanner;

/**
 * @author Siz
 *
 */
public abstract class AssemblyScanner extends RuleBasedScanner {

	@SuppressWarnings("nls")
	protected static final String[] OPCODES = { "adc", "and", "asl", "bcc",
				"bcs", "beq", "bit", "bmi", "bne", "bpl", "brk", "bvc", "bvs",
				"clc", "cld", "cli", "clv", "cmp", "cpx", "cpy", "dec", "dex",
				"dey", "eor", "inc", "inx", "iny", "jmp", "jsr", "lda", "ldx",
				"ldy", "lsr", "nop", "ora", "pha", "php", "pla", "plp", "rol",
				"ror", "rti", "rts", "sbc", "sec", "sed", "sei", "sta", "stx",
				"sty", "tax", "tay", "tsx", "txa", "txs", "tya" };

}
