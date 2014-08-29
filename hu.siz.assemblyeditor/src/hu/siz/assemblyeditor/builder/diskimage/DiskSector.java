/**
 * 
 */
package hu.siz.assemblyeditor.builder.diskimage;

/**
 * @author siz
 * 
 */
public class DiskSector {

	/**
	 * Size of <b>data</b> in a sector (excluding track and sector pointers)
	 */
	public static final int SECTOR_SIZE = 254;

	private DiskPointer nextSector;
	private int errorCode;
	private byte data[];

	/**
	 * 
	 * @param nextTrack
	 * @param nextSector
	 * @param data
	 * @param errorCode
	 */
	public DiskSector(int nextTrack, int nextSector, byte[] data, int errorCode) {
		super();
		this.nextSector = new DiskPointer(nextTrack, nextSector);
		this.errorCode = errorCode;
		this.data = data;
	}

	/**
	 * 
	 * @param data
	 */
	public DiskSector(byte[] data) {
		this(0, 0, data, 0);
	}

	/**
	 * 
	 */
	public DiskSector() {
		this(0, 0, new byte[SECTOR_SIZE], 0);
	}

	/**
	 * @return the errorCode
	 */
	public int getErrorCode() {
		return this.errorCode;
	}

	/**
	 * @param errorCode
	 *            the errorCode to set
	 */
	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}

	/**
	 * @return the nextSector
	 */
	public DiskPointer getNextSector() {
		return this.nextSector;
	}

	/**
	 * @param nextSector
	 *            the nextSector to set
	 */
	public void setNextSector(DiskPointer nextSector) {
		this.nextSector = nextSector;
	}

	/**
	 * @param track
	 *            the track to set
	 * @param sector
	 *            the sector to set
	 */
	public void setNextSector(int track, int sector) {
		this.nextSector.setTrack(track);
		this.nextSector.setSector(sector);
	}

	/**
	 * @return the data
	 */
	public byte[] getData() {
		return this.data;
	}

	/**
	 * @param data
	 *            the data to set
	 */
	public void setData(byte[] data) {
		this.data = data;
	}
}
