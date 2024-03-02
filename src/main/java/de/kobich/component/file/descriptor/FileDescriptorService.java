package de.kobich.component.file.descriptor;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import de.kobich.commons.misc.rename.Renamer;
import de.kobich.commons.misc.rename.rule.IRenameRule;
import de.kobich.commons.monitor.progress.IServiceProgressMonitor;
import de.kobich.commons.monitor.progress.ProgressData;
import de.kobich.commons.monitor.progress.ProgressSupport;
import de.kobich.commons.utils.RelativePathUtils;
import de.kobich.component.file.FileDescriptor;
import de.kobich.component.file.FileDescriptorResultSupport;
import de.kobich.component.file.FileException;

/**
 * Manages file descriptors.
 * @author ckorn
 */
@Service
public class FileDescriptorService {
	private static final Logger logger = Logger.getLogger(FileDescriptorService.class);

	/**
	 * Deletes all files
	 * @param request the request
	 */
	public void deleteFiles(DeleteFileDescriptorsRequest request) throws FileException {
		List<FileDescriptor> audioFiles = request.getFileDescriptors();
		
		try {
			for (FileDescriptor audioFile : audioFiles) {
				File file = audioFile.getFile();
				if (file != null) {
					FileUtils.forceDelete(file);
				}
			}
		} 
		catch (IOException exc) {
			throw new FileException(FileException.IO_ERROR);
		}
	}

	/**
	 * Reads all files recursively from file system below the start directory 
	 * @param request the request
	 * @return audio files
	 * @throws FileException
	 */
	public Set<FileDescriptor> readFiles(File startDirectory, FileFilter fileFilter, IServiceProgressMonitor monitor) throws FileException {
		logger.info("Open files of: " + startDirectory.getAbsolutePath());
		try {
			ProgressSupport progressSupport = new ProgressSupport(monitor);
			progressSupport.monitorBeginTask(new ProgressData("Reading files..."));
			
			FileDescriptorFileVisitor visitor = new FileDescriptorFileVisitor(startDirectory, fileFilter, progressSupport);
			Files.walkFileTree(Paths.get(startDirectory.toURI()), visitor);
			
			progressSupport.monitorEndTask(new ProgressData("All files read"));

			return visitor.getFileDescriptors();
		}
		catch (IOException exc) {
			throw new FileException(FileException.IO_ERROR);
		}
	}
	
	/**
	 * Renames all files by renaming rules
	 * @param files
	 * @param renameRules
	 * @return
	 * @throws FileException
	 */
	public FileDescriptorResult renameFiles(Set<? extends IFileDescriptorRenameable> files, List<IRenameRule> renameRules, IServiceProgressMonitor monitor) throws FileException {
		ProgressSupport progressSupport = new ProgressSupport(monitor);
		progressSupport.monitorBeginTask(new ProgressData("Renaming files..."));
		
		// set file names
		Renamer.rename(files, renameRules);
		
		// rename files
		FileDescriptorResultSupport result = new FileDescriptorResultSupport();
		while (true) {
			// loop as long as either all files are renamed or no more files can be renamed
			// Necessary if a new file name already exists and the corresponding file will be renamed later.
			// Example: file1 + file2 is given and should be renamed as follows: file1 -> file2 and file2 -> file3
			
			FileDescriptorResult subResult = renameFiles(files, monitor);
			result.removedFileDescriptors.addAll(subResult.getRemovedFiles());
			result.addedFileDescriptors.addAll(subResult.getAddedFiles());
			result.replacedFiles.putAll(subResult.getReplacedFiles());
			
			// stop loop if no file could be renamed or if all files are renamed
			int fileRenamedCount = subResult.getReplacedFiles().size();
			if (fileRenamedCount == 0 || fileRenamedCount == files.size()) {
				break;
			}
		}
		
		// set failed files
		if (result.replacedFiles.size() < files.size()) {
			for (IFileDescriptorRenameable renameFile : files) {
				boolean renamed = result.replacedFiles.containsKey(renameFile.getFileDescriptor());
				boolean nothingToDo = renameFile.getOriginalName().equals(renameFile.getName());
				if (!renamed && !nothingToDo) {
					result.failedFileDescriptors.add(renameFile.getFileDescriptor());			
				}
			}
		}
		
		progressSupport.monitorEndTask(new ProgressData("Renaming done"));
		return result.createFileDescriptorResult();
	}
	
	/**
	 * Renames all files as set in <code>RenameFileDescriptor.getName()</code>
	 * @param files
	 * @param monitor
	 * @return
	 * @throws FileException
	 */
	public FileDescriptorResult renameFiles(Set<? extends IFileDescriptorRenameable> files, IServiceProgressMonitor monitor) throws FileException {
		ProgressSupport progressSupport = new ProgressSupport(monitor);
		progressSupport.monitorBeginTask(new ProgressData("Renaming files...", files.size()));

		FileDescriptorResultSupport result = new FileDescriptorResultSupport();
		List<IFileDescriptorRenameable> fileList = new ArrayList<IFileDescriptorRenameable>(files);
		Collections.sort(fileList);
		
		// rename files
		for (IFileDescriptorRenameable renameFile : fileList) {
			FileDescriptor fileDescriptor = renameFile.getFileDescriptor();
			File file = fileDescriptor.getFile();
			String newName = renameFile.getName();

			// check new file name
			File newFile = new File(file.getParent(), newName);
			if (file.equals(newFile)) {
				// nothing to do
				continue;
			}
			if (newFile.exists()) {
				// do not overwrite existing files
				result.failedFileDescriptors.add(fileDescriptor);
				continue;
			}
			
			// monitor sub task
			ProgressData monitorData = new ProgressData("Renaming file: " + fileDescriptor.getRelativePath(), 1);
			progressSupport.monitorSubTask(monitorData);
			
			boolean state = file.renameTo(newFile);
			if (state) {
				String relativePath = RelativePathUtils.getParent(fileDescriptor.getRelativePath()) + RelativePathUtils.separator + newFile.getName();
				
				FileDescriptor newFileDescriptor = new FileDescriptor(newFile, relativePath);
				newFileDescriptor.setMetaData(fileDescriptor.getMetaData().clone());
				
				result.removedFileDescriptors.add(fileDescriptor);
				result.addedFileDescriptors.add(newFileDescriptor);
				result.replacedFiles.put(fileDescriptor, newFileDescriptor);
			}
			else {
				result.failedFileDescriptors.add(fileDescriptor);
			}
		}
		
		progressSupport.monitorEndTask(new ProgressData("Renaming done"));
		return result.createFileDescriptorResult();
	}

}
