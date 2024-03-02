package de.kobich.component.file;

import java.io.File;
import java.util.Collections;
import java.util.Set;

/**
 * File result.
 * @author ckorn
 */
public class FileResult {
	private final Set<File> createdFiles;
	private final Set<File> deletedFiles;
	private final Set<File> failedFiles;

	public FileResult(Set<File> createdFiles, Set<File> deletedFiles, Set<File> failedFiles) {
		this.createdFiles = Collections.unmodifiableSet(createdFiles);
		this.deletedFiles = Collections.unmodifiableSet(deletedFiles);
		this.failedFiles = Collections.unmodifiableSet(failedFiles);
	}

	public Set<File> getCreatedFiles() {
		return createdFiles;
	}

	public Set<File> getDeletedFiles() {
		return deletedFiles;
	}
	
	public Set<File> getFailedFiles() {
		return failedFiles;
	}
}
