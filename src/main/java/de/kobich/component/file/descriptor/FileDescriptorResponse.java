package de.kobich.component.file.descriptor;

import java.util.List;

import de.kobich.component.file.FileDescriptor;


/**
 * File descriptor response.
 * @author ckorn
 */
public class FileDescriptorResponse {
	private List<FileDescriptor> succeededFiles;
	private List<FileDescriptor> failedFiles;
	
	public FileDescriptorResponse(List<FileDescriptor> succeededFiles, List<FileDescriptor> failedFiles) {
		this.succeededFiles = succeededFiles;
		this.failedFiles = failedFiles;
	}

	/**
	 * @return the succeededFiles
	 */
	public List<FileDescriptor> getSucceededFiles() {
		return succeededFiles;
	}

	/**
	 * @return the failedFiles
	 */
	public List<FileDescriptor> getFailedFiles() {
		return failedFiles;
	}
}
