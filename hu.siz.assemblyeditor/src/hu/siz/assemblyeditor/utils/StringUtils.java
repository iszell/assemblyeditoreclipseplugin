/**
 * 
 */
package hu.siz.assemblyeditor.utils;

/**
 * Utilities for string operations
 * 
 * @author siz
 * 
 */
public abstract class StringUtils {

	private static final String EMPTY_STRING = ""; //$NON-NLS-1$
	private static final String CRLF = "\r\n"; //$NON-NLS-1$

	/**
	 * Convert a character from ASCII encoding to PETSCII
	 * <p>
	 * Strips LF characters
	 * 
	 * @param charcode
	 * @return a String representing the character
	 */
	public static String convertCharASCIItoPETSCII(int charcode) {
		String value = EMPTY_STRING;
		if ((charcode >= 0x41 && charcode <= 0x5a)) {
			value += (char) (charcode | 0x80);
		} else if ((charcode >= 0x61 && charcode <= 0x7a)) {
			value += (char) (charcode ^ 0x20);
		} else if (charcode == 0x0a) {
			// ignore LF, CBM only uses CR
		} else {
			value += (char) charcode;
		}
		return value;
	}

	/**
	 * Convert a character from PETSCII encoding to ASCII
	 * <p>
	 * Replaces CR with CRLF
	 * 
	 * @param charcode
	 * @return a String representing the character
	 */
	public static String convertCharPETSCIItoASCII(int charcode) {
		String value = EMPTY_STRING;
		if ((charcode >= 0x41 && charcode <= 0x5a)) {
			value += (char) (charcode | 0x20);
		} else if ((charcode & 0xff) >= 0xc1 && (charcode & 0xff) <= 0xda) {
			value += (char) (charcode & 0x7f);
		} else if ((charcode & 0xff) == 0x0d) {
			value += CRLF;
		} else {
			value += (char) charcode;
		}
		return value;
	}

	/**
	 * Converts an ASCII string to PETSCII
	 * 
	 * @param string
	 *            the string to be converted
	 * @param minLength
	 *            minimum length of the resulting string or <code>null</code> if
	 *            there is no minimum length
	 * @param maxLength
	 *            maximum length of the resulting string or <code>null</code> if
	 *            there is no naximum length
	 * @param paddingChar
	 *            the character used for padding the string to the minimum
	 *            length or <code>null</code> for space
	 * @return the PETSCII string
	 */
	public static String convertStringtoPETSCII(String string,
			Integer minLength, Integer maxLength, Character paddingChar) {
		StringBuffer sb = new StringBuffer();

		for (char c : string.toCharArray()) {
			if (maxLength == null || sb.length() < maxLength) {
				sb.append(convertCharASCIItoPETSCII(c));
			}
		}
		if (minLength != null) {
			char actualChar = ' ';

			if (paddingChar != null) {
				actualChar = paddingChar.charValue();
			}
			while (sb.length() < minLength) {
				sb.append(actualChar);
			}
		}

		return sb.toString();
	}

	/**
	 * Convenience method for
	 * {@link #convertStringtoPETSCII(String, Integer, Integer, Character)}
	 * 
	 * @param string
	 *            the string to be converted
	 * @return the PETSCII string
	 */
	public static String convertStringtoPETSCII(String string) {
		return convertStringtoPETSCII(string, null, null, null);
	}

	/**
	 * Converts a string to a byte array by chaining the lowest bytes of the
	 * characters
	 * 
	 * @param str
	 *            the string to be converted
	 * @return the byte array representation of the string or <code>null</code>
	 *         for empty strings
	 */
	public static byte[] toByteArray(String str) {
		byte[] result = null;
		if (str != null && str.length() > 0) {
			result = new byte[str.length()];
			int ptr = 0;
			for (char c : str.toCharArray()) {
				result[ptr++] = (byte) (c & 0xff);
			}
		}

		return result;
	}
}
