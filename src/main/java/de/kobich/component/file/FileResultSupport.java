package de.kobich.component.file;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

public class FileResultSupport {
	public final Set<File> createdFiles;
	public final Set<File> deletedFiles;
	public final Set<File> failedFiles;
	
	public FileResultSupport() {
		this.createdFiles = new HashSet<File>();
		this.deletedFiles = new HashSet<File>();
		this.failedFiles = new HashSet<File>();
	}
	
	public FileResult createFileResult() {
		return new FileResult(createdFiles, deletedFiles, failedFiles);
	}
	
}
