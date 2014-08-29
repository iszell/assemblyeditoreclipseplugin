package hu.siz.assemblyeditor.popup.actions;

public class FormatSource extends AbstractSourceProcessor {

	private boolean isNewLine = true;
	private boolean isInComment = false;
	private boolean isInString = false;
	private boolean isInLabel = false;
	private boolean isWhiteSpace = false;
	private boolean suppressSpaces = false;
	private boolean writeChar = true;
	private boolean wasComma = false;

	/**
	 * Formats assembly source file.
	 */
	public FormatSource() {
		super();
		// msgText = "Formatted assembly source file.";
	}

	/**
	 * Process character
	 * <p>
	 * <p>
	 * Format rules:
	 * <dd>- Comments (beginning with semicolon) are not touched to end of line
	 * <dd>- Strings are not touched to end of strings. String delimiter is
	 * doubleqoute
	 * <dd>- Labels are starting at the 1st character of the line
	 * <dd>- After or instead of label there is a tabulator and rest of line
	 * from next non-whitespace character
	 */
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * hu.siz.assemblyeditor.popup.actions.AbstractSourceProcessor#processChar
	 * (java.lang.StringBuilder, int)
	 */
	@Override
	// TODO More complex, standard source formatter plugin
	void processChar(StringBuilder s, int b) {
		this.isWhiteSpace = isWhiteSpace(b);
		this.writeChar = true;
		// Handle beginning of line
		if (this.isNewLine) {
			this.isInComment = false;
			this.isInString = false;
			this.isInLabel = false;
			this.isNewLine = false;
			this.wasComma = false;

			// if (this.isWhiteSpace || b == '.' || b == '*') {
			if (this.isWhiteSpace) {
				this.suppressSpaces = true;
				this.writeChar = false;
				s.append('\t');
			} else {
				this.isInLabel = true;
			}
		}
		// End of label, insert tabulator
		if (this.isInLabel && this.isWhiteSpace) {
			this.suppressSpaces = true;
			this.isInLabel = false;
			s.append('\t');
		}
		switch (b) {
		case 0x0A: {
			this.isNewLine = true;
			break;
		}
		case ';': {
			this.isInComment = true;
			this.isInLabel = false;
			break;
		}
		case '"': {
			this.isInString = !this.isInString;
			this.isInLabel = false;
			break;
		}
		}
		if (!this.isWhiteSpace) {
			this.suppressSpaces = false;
		}
		if (this.wasComma && !this.isInComment && !this.isWhiteSpace
				&& b != 'x' && b != 'y') {
			s.append(' ');
		}
		if ((this.writeChar && (!this.isWhiteSpace || !this.suppressSpaces))
				|| this.isInString || this.isInComment)
			s.append((char) b);
		this.wasComma = (b == ',');
	}

	private boolean isWhiteSpace(int b) {
		return (b == ' ' || b == '\t' || b == '\n' || b == '\r');
	}
}
