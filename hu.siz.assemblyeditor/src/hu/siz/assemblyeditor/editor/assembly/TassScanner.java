package hu.siz.assemblyeditor.editor.assembly;

import hu.siz.assemblyeditor.editor.AssemblyColorManager;

public class TassScanner extends AssemblyScanner {

	@SuppressWarnings("nls")
	private final static String[] TASS_KEYWORDS = { ".byte", ".text", ".char", ".shift", ".null", ".rta", ".word",
			".int", ".offs", ".macro", ".endm", ".for", ".next", ".if", ".ifne", ".ifeq", ".ifpl", ".ifmi", ".else",
			".elseif", ".fi", ".endif", ".rept", ".include", ".binary", ".comment", ".endc", ".page", ".endp",
			".logical", ".as", ".al", ".xs", ".xl", ".error", ".warn", ".cerror", ".cwarn", ".proc", ".pend",
			".databank", ".dpage", ".fill", ".align", ".enc", ".cpu", ".global", ".assert", ".check" };

	public TassScanner(AssemblyColorManager manager) {
		super(manager, TASS_KEYWORDS);
	}
}
