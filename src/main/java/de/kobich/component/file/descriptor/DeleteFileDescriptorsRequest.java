package de.kobich.component.file.descriptor;

import java.util.List;

import de.kobich.component.file.FileDescriptor;


/**
 * Request to delete files.
 * @author ckorn
 */
public class DeleteFileDescriptorsRequest {
	private List<FileDescriptor> fileDescriptors;
	
	/**
	 * Constructor
	 * @param fileDescriptors
	 */
	public DeleteFileDescriptorsRequest(List<FileDescriptor> fileDescriptors) {
		this.fileDescriptors = fileDescriptors;
	}

	/**
	 * @return the fileDescriptors
	 */
	public List<FileDescriptor> getFileDescriptors() {
		return fileDescriptors;
	}
}
