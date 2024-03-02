package de.kobich.component.file.crc;

import java.io.File;
import java.util.List;

import de.kobich.commons.monitor.progress.ProgressMonitorRequest;
import de.kobich.component.file.FileDescriptor;

/**
 * Request to check files for CRC checksum.
 * @author ckorn
 */
public class CreateCRCFileRequest extends ProgressMonitorRequest {
	private final List<FileDescriptor> fileDescriptors;
	private final File crcFile;
	private CRCMode mode;

	/**
	 * Constructor
	 * @param crcFile the crc file
	 * @param fileDescriptors
	 */
	public CreateCRCFileRequest(File crcFile, List<FileDescriptor> fileDescriptors) {
		this.crcFile = crcFile;
		this.fileDescriptors = fileDescriptors;
		this.mode = CRCMode.ADLER_32;
	}

	/**
	 * @return the fileDescriptors
	 */
	public List<FileDescriptor> getFileDescriptors() {
		return fileDescriptors;
	}

	/**
	 * @return the crcFile
	 */
	public File getCrcFile() {
		return crcFile;
	}

	/**
	 * @return the mode
	 */
	public CRCMode getMode() {
		return mode;
	}

	/**
	 * @param mode the mode to set
	 */
	public void setMode(CRCMode mode) {
		this.mode = mode;
	}
}
