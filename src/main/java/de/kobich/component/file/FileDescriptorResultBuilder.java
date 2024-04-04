package de.kobich.component.file;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import de.kobich.component.file.descriptor.FileDescriptorResult;

public class FileDescriptorResultBuilder {
	public final Set<FileDescriptor> addedFileDescriptors;
	public final Set<FileDescriptor> removedFileDescriptors;
	public final Set<FileDescriptor> updatedFileDescriptors;
	public final Map<FileDescriptor, FileDescriptor> replacedFiles;
	public final Set<FileDescriptor> failedFileDescriptors;
	
	public FileDescriptorResultBuilder() {
		this.addedFileDescriptors = new HashSet<FileDescriptor>();
		this.removedFileDescriptors = new HashSet<FileDescriptor>();
		this.updatedFileDescriptors = new HashSet<FileDescriptor>();
		this.replacedFiles = new HashMap<FileDescriptor, FileDescriptor>();
		this.failedFileDescriptors = new HashSet<FileDescriptor>();
	}
	
	public void setMissingAsFailed(Collection<FileDescriptor> allFiles) {
		this.failedFileDescriptors.addAll(allFiles);
		this.failedFileDescriptors.removeAll(this.addedFileDescriptors);
		this.failedFileDescriptors.removeAll(this.removedFileDescriptors);
		this.failedFileDescriptors.removeAll(this.updatedFileDescriptors);
		this.failedFileDescriptors.removeAll(this.replacedFiles.keySet());
	}
	
	public FileDescriptorResult build() {
		return new FileDescriptorResult(addedFileDescriptors, removedFileDescriptors, updatedFileDescriptors, replacedFiles, failedFileDescriptors);
	}
}
