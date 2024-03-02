package de.kobich.component.file.descriptor;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashSet;
import java.util.Set;

import de.kobich.commons.monitor.progress.ProgressData;
import de.kobich.commons.monitor.progress.ProgressSupport;
import de.kobich.component.file.FileDescriptor;

public class FileDescriptorFileVisitor implements FileVisitor<Path> {
	private final FileFilter filter;
	private final File startDirectory;
	private final ProgressSupport progressSupport;
	public final Set<FileDescriptor> fileDescriptors;
	
	public FileDescriptorFileVisitor(File startDirectory, FileFilter filter, ProgressSupport progressSupport) {
		this.startDirectory = startDirectory;
		this.filter = filter;
		this.progressSupport = progressSupport;
		this.fileDescriptors = new HashSet<FileDescriptor>();
	}

	@Override
	public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
		return FileVisitResult.CONTINUE;
	}

	@Override
	public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
		File f = file.toFile();
		if (filter.accept(f)) {
			FileDescriptor fileDescriptor = new FileDescriptor(f, startDirectory);
			fileDescriptors.add(fileDescriptor);
			
			ProgressData subTask = new ProgressData("Reading file #" + fileDescriptors.size() + ": " + f.getName() + " \n" +
					f.getAbsolutePath());
			progressSupport.monitorSubTask(subTask);
		}
		return FileVisitResult.CONTINUE;
	}

	@Override
	public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
		return FileVisitResult.CONTINUE;
	}

	@Override
	public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
		return FileVisitResult.CONTINUE;
	}

	public Set<FileDescriptor> getFileDescriptors() {
		return fileDescriptors;
	}

}
