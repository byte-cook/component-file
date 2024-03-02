package de.kobich.component.file.crc;

import java.io.File;
import java.util.List;

import de.kobich.commons.monitor.progress.ProgressMonitorRequest;
import de.kobich.component.file.FileDescriptor;

/**
 * Request to check files for CRC checksum.
 */
public class CheckFilesRequest extends ProgressMonitorRequest {
	private List<FileDescriptor> fileDescriptors;
	private File crcFile;

	/**
	 * Constructor
	 * @param crcFile the crc file
	 * @param fileDescriptors
	 */
	public CheckFilesRequest(File crcFile, List<FileDescriptor> fileDescriptors) {
		this.crcFile = crcFile;
		this.fileDescriptors = fileDescriptors;
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
}
