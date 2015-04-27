/**
 * Package:	hu.siz.assemblyeditor.builder.diskimage
 * Project:	hu.siz.assemblyeditor
 * File:	DiskFileType.java
 * 
 * Created:	2010.10.22.
 */
package hu.siz.assemblyeditor.builder.diskimage;

/**
 * Disk File type enum
 * 
 * @author siz
 * 
 */
public enum DiskFileType {

	DEL(0), SEQ(1), PRG(2), USR(3), REL(4);

	private int value;

	/**
	 * Create an enum for an <code>int</code> value
	 * 
	 * @param value
	 */
	DiskFileType(int value) {
		this.value = value;
	}

	/**
	 * Return the <code>int</code> value of the enum instance
	 * 
	 * @return the value
	 */
	public int getValue() {
		return this.value;
	}
}
