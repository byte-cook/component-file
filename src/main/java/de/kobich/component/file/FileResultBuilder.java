package de.kobich.component.file;

import java.io.File;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class FileResultBuilder {
	public final Set<File> createdFiles;
	public final Set<File> deletedFiles;
	public final Set<File> failedFiles;
	
	public FileResultBuilder() {
		this.createdFiles = new HashSet<File>();
		this.deletedFiles = new HashSet<File>();
		this.failedFiles = new HashSet<File>();
	}
	
	public void setMissingAsFailed(Collection<File> allFiles) {
		this.failedFiles.addAll(allFiles);
		this.failedFiles.removeAll(this.createdFiles);
		this.failedFiles.removeAll(this.deletedFiles);
	}
	
	public FileResult build() {
		return new FileResult(createdFiles, deletedFiles, failedFiles);
	}
	
}
