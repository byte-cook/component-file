package de.kobich.component.file.descriptor;

import java.util.HashMap;
import java.util.Map;

import de.kobich.commons.monitor.progress.ProgressMonitorRequest;
import de.kobich.component.file.FileDescriptor;

/**
 * Request to rename files.
 * @author ckorn
 */
public class RenameFileDescriptorRequest extends ProgressMonitorRequest {
	private Map<FileDescriptor, String> file2NameMap;
	
	/**
	 * Constructor
	 * @param fileDescriptor the file descriptor
	 * @param name the new name
	 */
	public RenameFileDescriptorRequest(FileDescriptor fileDescriptor, String name) {
		this.file2NameMap = new HashMap<FileDescriptor, String>();
		this.file2NameMap.put(fileDescriptor, name);
	}
	
	/**
	 * Constructor
	 * @param file2NameMap
	 */
	public RenameFileDescriptorRequest(Map<FileDescriptor, String> file2NameMap) {
		this.file2NameMap = file2NameMap;
	}

	/**
	 * @return the file2NameMap
	 */
	public Map<FileDescriptor, String> getFile2NameMap() {
		return file2NameMap;
	}
}
