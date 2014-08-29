/**
 * Package:	hu.siz.assemblyeditor.builder.diskimage
 * Project:	hu.siz.assemblyeditor
 * File:	DiskImageType.java
 * 
 * Created:	2010.10.21.
 */
package hu.siz.assemblyeditor.builder.diskimage;

/**
 * @author siz
 * 
 */
public enum DiskImageType {

	// Source: http://www.baltissen.org/newhtm/diskimag.htm
	// Other source: http://petlibrary.tripod.com/formats.htm

	/**
	 * This image handles an imaginary 1541-drive capable of handling 255 tracks
	 * with 256 sectors each. This is about 16 MB, therefore the "16" in the
	 * "D16"
	 */
	D16,

	/**
	 * For use with drives: 2040, 3040
	 */
	D40,
	/**
	 * For use with drives: 9060
	 */
	D60,
	/**
	 * For use with drives: 2031, 4040, 1540, 1541, 1551, 1570
	 */
	D64,
	/**
	 * 40 track D64 image
	 * 
	 * @see #D64
	 */
	D64_40,
	/**
	 * For use with drives: 1571
	 */
	D71,
	/**
	 * For use with drives: 8050
	 */
	D80,
	/**
	 * For use with drives: 1581
	 */
	D81,
	/**
	 * For use with drives: 8250, SFD-1001
	 */
	D82,
	/**
	 * For use with drives: 9090
	 */
	D90;
}
