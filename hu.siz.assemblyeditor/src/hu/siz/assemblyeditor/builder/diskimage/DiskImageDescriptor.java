/**
 * 
 */
package hu.siz.assemblyeditor.builder.diskimage;

import java.util.ArrayList;
import java.util.List;

/**
 * @author siz
 * 
 */
public class DiskImageDescriptor {

	/**
	 * Image part descriptor: holds a part of the disk image: number of sectors
	 * for a number of tracks
	 * 
	 * @author siz
	 * 
	 */
	private class ImagePartDescriptor {
		private int trackCount;
		private int sectorCount;

		/**
		 * @param trackCount
		 * @param sectorCount
		 */
		public ImagePartDescriptor(int trackCount, int sectorCount) {
			super();
			this.trackCount = trackCount;
			this.sectorCount = sectorCount;
		}

		/**
		 * @return the trackCount
		 */
		public int getTrackCount() {
			return this.trackCount;
		}

		/**
		 * @return the sectorCount
		 */
		public int getSectorCount() {
			return this.sectorCount;
		}
	}

	private int tracks;
	private int interleave;
	private List<DiskPointer> bamSectors;
	private List<DiskPointer> directorySectors;
	private byte dosVersion;
	private byte doubleSidedFlag;
	private DiskImageType diskImageType;

	private List<ImagePartDescriptor> sectorsPerTrack;

	/**
	 * 
	 */
	public DiskImageDescriptor() {
		this(DiskImageType.D64);
	}

	/**
	 * 
	 */
	public DiskImageDescriptor(DiskImageType diskImageType) {
		this.diskImageType = diskImageType;

		this.sectorsPerTrack = new ArrayList<ImagePartDescriptor>();
		this.interleave = 3;
		this.bamSectors = new ArrayList<DiskPointer>();
		this.directorySectors = new ArrayList<DiskPointer>();
		this.doubleSidedFlag = 0;

		if (this.diskImageType == DiskImageType.D64
				|| this.diskImageType == DiskImageType.D64_40
				|| this.diskImageType == DiskImageType.D71) {
			this.sectorsPerTrack.add(new ImagePartDescriptor(17, 21));
			this.sectorsPerTrack.add(new ImagePartDescriptor(7, 19));
			this.sectorsPerTrack.add(new ImagePartDescriptor(6, 18));
			this.sectorsPerTrack.add(new ImagePartDescriptor(5, 17));
			this.tracks = 35;
			this.bamSectors.add(new DiskPointer(18, 0));
			for (int i = 1; i < getNumberOfSectorsPerTrack(18); i++) {
				this.directorySectors.add(new DiskPointer(18, i));
			}
			this.dosVersion = 0x41;
		}
		if (this.diskImageType == DiskImageType.D64_40) {
			this.sectorsPerTrack.add(new ImagePartDescriptor(5, 17));
			this.tracks = 40;
		}
		if (this.diskImageType == DiskImageType.D71) {
			this.sectorsPerTrack.add(new ImagePartDescriptor(17, 21));
			this.sectorsPerTrack.add(new ImagePartDescriptor(7, 19));
			this.sectorsPerTrack.add(new ImagePartDescriptor(6, 18));
			this.sectorsPerTrack.add(new ImagePartDescriptor(5, 17));
			this.tracks = 70;
			// Hack for 0x80:
			this.doubleSidedFlag = -128;
			for (int i = 0; i < getNumberOfSectorsPerTrack(53); i++) {
				this.bamSectors.add(new DiskPointer(53, i));
			}
		}
		if (this.diskImageType == DiskImageType.D81) {
			this.sectorsPerTrack.add(new ImagePartDescriptor(80, 40));
			this.tracks = 80;
			this.interleave = 1;
			this.dosVersion = 0x44;
			this.bamSectors.add(new DiskPointer(40, 1));
			this.bamSectors.add(new DiskPointer(40, 2));
			for (int i = 3; i < getNumberOfSectorsPerTrack(40); i++) {
				this.directorySectors.add(new DiskPointer(40, i));
			}
		}
	}

	/**
	 * Get number of tracks
	 * 
	 * @return tracks - number of tracks for this image
	 */
	public int getNumberOfTracks() {
		return this.tracks;
	}

	/**
	 * Returns the number of sectors on a track
	 * 
	 * @param track
	 *            - track number
	 * @return number of sectors on that track
	 */
	public int getNumberOfSectorsPerTrack(int track) {
		int sectors = 0;
		int curtrack = 0;

		for (ImagePartDescriptor i : this.sectorsPerTrack) {
			curtrack += i.getTrackCount();
			if (curtrack >= track) {
				sectors = i.getSectorCount();
				break;
			}
		}

		return sectors;
	}

	/**
	 * Returns the total number of sectors in the image
	 * 
	 * @return number of sectors in the image
	 */
	public int getTotalNumberOfSectors() {
		int sectors = 0;

		for (ImagePartDescriptor i : this.sectorsPerTrack) {
			sectors += i.getSectorCount() * i.getTrackCount();
		}

		return sectors;
	}

	/**
	 * 
	 * @return Default sector interleave for image type
	 */
	public int getInterleave() {
		return this.interleave;
	}

	/**
	 * @return the bamSectors
	 */
	public List<DiskPointer> getBamSectors() {
		return this.bamSectors;
	}

	/**
	 * @return the directorySectors
	 */
	public List<DiskPointer> getDirectorySectors() {
		return this.directorySectors;
	}

	/**
	 * @return the dosVersion
	 */
	public byte getDosVersion() {
		return this.dosVersion;
	}

	/**
	 * @return the doubleSidedFlag
	 */
	public byte getDoubleSidedFlag() {
		return this.doubleSidedFlag;
	}

	public DiskImageType getImageType() {
		return this.diskImageType;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return diskImageType + "[tracks=" + tracks + ", interleave="
				+ interleave + "]";
	}
}
