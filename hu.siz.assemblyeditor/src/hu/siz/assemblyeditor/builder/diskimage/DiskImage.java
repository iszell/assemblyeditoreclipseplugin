/**
 * 
 */
package hu.siz.assemblyeditor.builder.diskimage;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;

import hu.siz.assemblyeditor.utils.AssemblyUtils;
import hu.siz.assemblyeditor.utils.StringUtils;

/**
 * @author siz
 * 
 */
public class DiskImage {

	private static final int DIRECTORY_ENTRY_SIZE = 32;

	public static final DiskSector RESERVED_SECTOR = new DiskSector();

	private DiskImageDescriptor imageDescriptor;
	private List<DiskTrack> tracks;
	private Integer interleave;
	private List<DiskFile> files;
	private String name;
	private String id;
	private boolean useDirTrack;
	private int fileCount;
	private boolean ascendingTrackSearch;
	private int dirSectors;

	/**
	 * Default constructor
	 */
	public DiskImage() {
	}

	/**
	 * @param name
	 * @param id
	 * @param imageDescriptor
	 * @param interleave
	 * @param useDirTrack
	 */
	public DiskImage(DiskImage model) {
		super();
		this.name = model.getName();
		this.id = model.getId();
		this.imageDescriptor = model.getImageDescriptor();
		this.interleave = model.getInterleave();
		this.useDirTrack = model.isUseDirTrack();
		this.fileCount = model.getFiles() != null ? model.getFiles().size() : 0;
		this.files = new ArrayList<DiskFile>();
		this.tracks = new ArrayList<DiskTrack>();
		for (int t = 1; t <= imageDescriptor.getNumberOfTracks(); t++) {
			this.tracks.add(new DiskTrack(imageDescriptor.getNumberOfSectorsPerTrack(t)));
		}
		reserveSectorsForBAMAndDir();
	}

	/**
	 * Reserve sectors for BAM and Directory
	 */
	private void reserveSectorsForBAMAndDir() {
		for (DiskPointer sector : imageDescriptor.getBamSectors()) {
			setSector(sector, RESERVED_SECTOR);
		}
		if (!useDirTrack) {
			for (DiskPointer sector : imageDescriptor.getDirectorySectors()) {
				setSector(sector, RESERVED_SECTOR);
			}
		} else {
			try {
				int dirEntriesPerSector = DiskSector.SECTOR_SIZE / DIRECTORY_ENTRY_SIZE;
				this.dirSectors = fileCount / dirEntriesPerSector + (fileCount % dirEntriesPerSector == 0 ? 0 : 1);
				if (this.dirSectors == 0) {
					this.dirSectors = 1;
				}
				DiskPointer sector = imageDescriptor.getDirectorySectors().get(0);
				for (int i = 0; i < this.dirSectors; i++) {
					setSector(sector, RESERVED_SECTOR);
					sector = getNextAvailableSector(sector, true);
				}

			} catch (DiskFullException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Add new file to the image
	 * 
	 * @param file
	 * @throws IOException
	 * @throws CoreException
	 */
	public void add(String name, IFile file, DiskFileType type, boolean locked, boolean closed)
			throws IOException, CoreException, DiskFullException {

		DiskFile newFile = new DiskFile(file);
		newFile.setName(name);
		newFile.setLocked(locked);
		newFile.setClosed(closed);
		newFile.setType(type);
		DiskPointer currentPointer = getFirstAvailableSector();
		newFile.setFirstSector(currentPointer);
		DiskSector prevSector = null;

		for (DiskSector sector : newFile.toDiskSectors()) {
			currentPointer = getNextAvailableSector(currentPointer, false);
			sector.setNextSector(0, sector.getData().length + 1);
			setSector(currentPointer, sector);
			if (prevSector != null) {
				prevSector.setNextSector(currentPointer);
			}
			prevSector = sector;
		}

		this.files.add(newFile);
	}

	/**
	 * Set the data for a sector
	 * 
	 * @param pointer
	 * @param sector
	 */
	private void setSector(DiskPointer pointer, DiskSector sector) {
		this.tracks.get(pointer.getTrack() - 1).setSector(pointer.getSector(), sector);
	}

	/**
	 * Searches for an available sector to start a chain
	 * 
	 * @return DiskPointer to the sector
	 */
	private DiskPointer getFirstAvailableSector() throws DiskFullException {
		return getNextAvailableSector(null, false);
	}

	/**
	 * Search for the next available sector
	 * 
	 * @param lastSector
	 *            the pointer to the last sector used
	 * @return DiskPointer to the next available sector
	 */
	private DiskPointer getNextAvailableSector(DiskPointer lastSector, boolean allowReserved) throws DiskFullException {
		DiskPointer bamStart = this.imageDescriptor.getBamSectors().get(0);

		int trackNumber;
		int sectorNumber;
		// Use last sectors' data if any
		if (lastSector != null) {
			trackNumber = lastSector.getTrack();
			sectorNumber = lastSector.getSector();
		} else {
			// Default values for track and sector: track=BAM track-1, sector=0
			trackNumber = bamStart.getTrack() - 1;
			sectorNumber = 0;
		}

		// Find a track with at least one empty sector
		DiskTrack track = this.tracks.get(trackNumber - 1);
		while (track.isFull(allowReserved)) {
			// Search for track below the BAM track first by stepping down
			if (!ascendingTrackSearch) {
				trackNumber--;
				if (trackNumber < 1) {
					ascendingTrackSearch = true;
					if (useDirTrack) {
						trackNumber = bamStart.getTrack();
					} else {
						trackNumber = bamStart.getTrack() + 1;
					}
				}
			} else {
				trackNumber++;
				if (trackNumber > this.imageDescriptor.getNumberOfTracks()) {
					// No track found with available sector
					throw new DiskFullException("Disk has no available sectors");
				}
			}
			track = this.tracks.get(trackNumber - 1);
		}

		if (sectorNumber >= track.getNumberOfSectors()) {
			sectorNumber = 0;
		}
		DiskSector sector = track.getSector(sectorNumber);
		int firstSectorInTrack = -1;
		while (isBAM(trackNumber, sectorNumber) || (!allowReserved && sector != null) || (allowReserved && sector != RESERVED_SECTOR)) {
			sectorNumber += getInterleaveValue();
			if (sectorNumber >= track.getNumberOfSectors()) {
				sectorNumber = ++firstSectorInTrack;
			}
			sector = track.getSector(sectorNumber);
		}

		return new DiskPointer(trackNumber, sectorNumber);
	}
	
	private boolean isBAM(int track, int sector) {
		for(DiskPointer ptr : this.imageDescriptor.getBamSectors()) {
			if(ptr.getTrack() == track && ptr.getSector() == sector ) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Writes the disk image to an output stream
	 * 
	 * @return
	 * @throws IOException
	 * @throws DiskFullException
	 * @throws CoreException
	 */
	public InputStream getImage() throws IOException, DiskFullException, CoreException {

		createDirectory();
		createBam();

		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		for (int t = 1; t <= this.imageDescriptor.getNumberOfTracks(); t++) {
			DiskTrack track = this.tracks.get(t - 1);
			for (int s = 0; s < this.imageDescriptor.getNumberOfSectorsPerTrack(t); s++) {
				DiskSector sector = track.getSector(s);
				if (sector != null) {
					baos.write(sector.getNextSector().toByteArray());
					baos.write(sector.getData());
					for (int i = sector.getData().length; i < DiskSector.SECTOR_SIZE; i++) {
						baos.write(0);
					}
				} else {
					for (int i = 0; i < 256; i++) {
						baos.write(0);
					}
				}
			}
		}
		return new ByteArrayInputStream(baos.toByteArray());
	}

	/**
	 * Create directory sectors
	 * 
	 * @throws DiskFullException
	 * @throws CoreException
	 */
	private void createDirectory() throws DiskFullException, CoreException {
		DiskSector sector = new DiskSector();
		sector.setNextSector(0, 255);
		DiskPointer dirPointer = this.imageDescriptor.getDirectorySectors().get(0);
		setSector(dirPointer, sector);

		ByteArrayOutputStream data = new ByteArrayOutputStream(DiskSector.SECTOR_SIZE);

		for (DiskFile file : this.files) {
			try {
				byte[] entry = getDirectoryEntry(file);
				if (data.size() + entry.length > DiskSector.SECTOR_SIZE) {
					dirPointer = getNextAvailableSector(dirPointer, true);
					sector.setNextSector(dirPointer);
					sector.setData(data.toByteArray());
					data.reset();

					sector = new DiskSector();
					setSector(dirPointer, sector);
				}
				if (data.size() != 0) {
					data.write(0);
					data.write(0);
				}
				data.write(entry);
			} catch (IOException e) {
				AssemblyUtils.createLogEntry(e);
			}
		}
		sector.setNextSector(0, data.size());
		sector.setData(data.toByteArray());
	}

	/**
	 * Creates a directory entry for a file
	 * 
	 * @param file
	 *            the file
	 * @return the entry as a byte array
	 * @throws IOException
	 * @throws CoreException
	 */
	private byte[] getDirectoryEntry(DiskFile file) throws IOException, CoreException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream(30);

		// File type
		byte type = (byte) (file.getType().getValue() & 0xff);
		if (file.isClosed()) {
			type += 128;
		}
		if (file.isLocked()) {
			type += 64;
		}
		baos.write(type);

		// First sector pointer
		baos.write(file.getFirstSector().toByteArray());

		// Name
		baos.write(convertName(file.getName()));

		// REL super side block
		baos.write(0);
		baos.write(0);
		// REL record length
		baos.write(0);

		// Unused
		baos.write(0);
		baos.write(0);
		baos.write(0);
		baos.write(0);
		baos.write(0);
		baos.write(0);

		// File size
		baos.write((byte) (file.getNumberOfSectors() & 0xff));
		baos.write((byte) ((file.getNumberOfSectors() >> 8) & 0xff));

		return baos.toByteArray();
	}

	/**
	 * Create BAM sectors
	 */
	private void createBam() {
		DiskSector sector = new DiskSector();
		DiskPointer bamPointer = this.imageDescriptor.getBamSectors().get(0);
		sector.setNextSector(this.imageDescriptor.getDirectorySectors().get(0));
		setSector(bamPointer, sector);

		ByteArrayOutputStream data = new ByteArrayOutputStream(DiskSector.SECTOR_SIZE);

		addBamHeader(data);

		// TODO Befejezni a BAM összeállítást
		for (int trackNum = 1; trackNum <= this.tracks.size(); trackNum++) {
			DiskTrack track = this.tracks.get(trackNum - 1);
			byte freeSectors = 0;
			byte sectorMap = 0;
			byte[] sectorMapArray = new byte[3];
			int sectorMapPtr = 0;
			for (int sectorNum = 0; sectorNum < track.getNumberOfSectors(); sectorNum++) {
				if (sectorNum != 0 && sectorNum % 8 == 0) {
					sectorMapArray[sectorMapPtr++] = sectorMap;
					sectorMap = 0;
				}
				DiskSector sec = track.getSector(sectorNum);
				sectorMap = (byte) (sectorMap << 1);
				if (sec == null) {
					sectorMap |= 1;
					freeSectors++;
				}
			}
			sectorMapArray[sectorMapPtr++] = sectorMap;
			try {
				data.write(freeSectors);
				data.write(sectorMapArray);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		addDiskHeader(data);

		sector.setData(data.toByteArray());
	}

	private void addBamHeader(ByteArrayOutputStream stream) {
		stream.write(this.imageDescriptor.getDosVersion());
		stream.write(0);
	}

	private void addDiskHeader(ByteArrayOutputStream stream) {
		try {
			// Disk name
			stream.write(convertName(this.name));
			stream.write((char) 0xa0);
			// Disk ATTRNAME_ID
			stream.write(StringUtils.toByteArray(StringUtils.convertStringtoPETSCII(this.id, 2, 2, (char) 0xa0)));
			stream.write((char) 0xa0);
			// DOS version
			stream.write(StringUtils.toByteArray(StringUtils.convertStringtoPETSCII(
					Integer.toHexString(this.imageDescriptor.getDosVersion()), 2, 2, (char) 0xa0)));
			stream.write((char) 0xa0);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * @param name
	 * @return
	 */
	private byte[] convertName(String name) {
		return StringUtils.toByteArray(StringUtils.convertStringtoPETSCII(name, 16, 16, (char) 0xa0));
	}

	private int getInterleaveValue() {
		if (this.interleave != null) {
			return this.interleave.intValue();
		}
		return this.imageDescriptor.getInterleave();
	}

	/**
	 * @return the imageDescriptor
	 */
	public DiskImageDescriptor getImageDescriptor() {
		return this.imageDescriptor;
	}

	/**
	 * @param imageDescriptor
	 *            the imageDescriptor to set
	 */
	public void setImageDescriptor(DiskImageDescriptor imageDescriptor) {
		this.imageDescriptor = imageDescriptor;
	}

	/**
	 * @return the interleave
	 */
	public Integer getInterleave() {
		return this.interleave;
	}

	/**
	 * @param interleave
	 *            the interleave to set
	 */
	public void setInterleave(Integer interleave) {
		this.interleave = interleave;
	}

	/**
	 * @return the files
	 */
	public List<DiskFile> getFiles() {
		return this.files;
	}

	/**
	 * @param files
	 *            the files to set
	 */
	public void setFiles(List<DiskFile> files) {
		this.files = files;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return this.id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	public boolean isUseDirTrack() {
		return useDirTrack;
	}

	public void setUseDirTrack(boolean useDirTrack) {
		this.useDirTrack = useDirTrack;
	}

	@Override
	public String toString() {
		return "DiskImage [" + imageDescriptor + ", name=" + name + ", id=" + id + "]";
	}
}
