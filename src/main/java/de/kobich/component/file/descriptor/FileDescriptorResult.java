package de.kobich.component.file.descriptor;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

import de.kobich.component.file.FileDescriptor;

/**
 * File descriptor result.
 */
public class FileDescriptorResult {
	private final Set<FileDescriptor> addedFiles;
	private final Set<FileDescriptor> removedFiles;
	private final Set<FileDescriptor> updatedFiles;
	private final Map<FileDescriptor, FileDescriptor> replacedFiles;
	private final Set<FileDescriptor> failedFiles;
	
	public FileDescriptorResult(Set<FileDescriptor> addedFiles, Set<FileDescriptor> removedFiles, Set<FileDescriptor> updatedFiles, Map<FileDescriptor, FileDescriptor> replacedFiles, Set<FileDescriptor> failedFiles) {
		this.addedFiles = Collections.unmodifiableSet(addedFiles);
		this.removedFiles = Collections.unmodifiableSet(removedFiles);
		this.updatedFiles = Collections.unmodifiableSet(updatedFiles);
		this.replacedFiles = Collections.unmodifiableMap(replacedFiles);
		this.failedFiles = Collections.unmodifiableSet(failedFiles);
	}

	public Set<FileDescriptor> getAddedFiles() {
		return addedFiles;
	}

	public Set<FileDescriptor> getRemovedFiles() {
		return removedFiles;
	}

	public Set<FileDescriptor> getUpdatedFiles() {
		return updatedFiles;
	}

	public Map<FileDescriptor, FileDescriptor> getReplacedFiles() {
		return replacedFiles;
	}

	public Set<FileDescriptor> getFailedFiles() {
		return failedFiles;
	}
}
