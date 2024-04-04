package de.kobich.component.file.io;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;

import de.kobich.component.file.FileException;
import de.kobich.component.file.FileResult;
import de.kobich.component.file.FileResultBuilder;

/**
 * The file I/O service.
 * 
 * @author ckorn
 */
@Service
public class FileIOService {
	/**
	 * Create new folders
	 * @param request
	 */
	public FileResult createNewFolders(FilesRequest request) throws FileException {
		try {
			FileResultBuilder result = new FileResultBuilder();
			for (File file : request.getFiles()) {
				if (!file.exists()) {
					FileUtils.forceMkdir(file);
					result.createdFiles.add(file);
				}
			}
			return result.build();
		}
		catch (IOException exc) {
			throw new FileException(FileException.FILE_CREATE_FAILED, exc);
		}
	}

	/**
	 * Create new files
	 * 
	 * @param request
	 * @throws FileException
	 */
	public FileResult createNewFiles(FilesRequest request) throws FileException {
		try {
			FileResultBuilder result = new FileResultBuilder();
			for (File file : request.getFiles()) {
				if (!file.exists()) {
					file.createNewFile();
					result.createdFiles.add(file);
				}
			}
			return result.build();
		}
		catch (IOException exc) {
			throw new FileException(FileException.FILE_CREATE_FAILED, exc);
		}
	}

	/**
	 * Rename file
	 * 
	 * @param request
	 * @throws FileException
	 */
	public FileResult renameFiles(RenameFileRequest request) throws FileException {
		FileResultBuilder result = new FileResultBuilder();
		File oldFile = request.getOldFile();
		File newFile = request.getNewFile();

		if (!oldFile.equals(newFile)) {
			boolean state = oldFile.renameTo(newFile);
			if (!state) {
				throw new FileException(FileException.FILE_RENAMING_FAILED);
			}
			else {
				result.createdFiles.add(newFile);
				result.deletedFiles.add(oldFile);
			}
		}
		return result.build();
	}

	/**
	 * Copies/move folders
	 * 
	 * @param request
	 * @throws FileException
	 */
	public FileResult copyFolder(CopyFileRequest request) throws FileException {
		File srcDir = request.getSourceFile();
		File destDir = request.getTargetFile();
		FileCreationType type = request.getType();

		if (destDir.exists()) {
			throw new FileException(FileException.FOLDER_ALREADY_EXISTS);
		}

		try {
			FileResultBuilder result = new FileResultBuilder();
			if (FileCreationType.COPY.equals(type)) {
				FileUtils.copyDirectory(srcDir, destDir);
				result.createdFiles.add(destDir);
			}
			else if (FileCreationType.MOVE.equals(type)) {
				FileUtils.copyDirectory(srcDir, destDir);
				FileUtils.deleteDirectory(srcDir);
				result.createdFiles.add(destDir);
				result.deletedFiles.add(srcDir);
			}
			return result.build();
		}
		catch (IOException exc) {
			throw new FileException(FileException.FILE_COPY_FAILED, exc);
		}
	}

	/**
	 * Copies/moves files
	 * 
	 * @param request
	 * @throws FileException
	 */
	public FileResult copyFile(CopyFileRequest request) throws FileException {
		File srcFile = request.getSourceFile();
		File destFile = request.getTargetFile();
		FileCreationType type = request.getType();

		// create parent folder
		File parentFolder = destFile.getParentFile();
		FilesRequest folderRequest = new FilesRequest(parentFolder);
		createNewFolders(folderRequest);

		if (destFile.exists()) {
			throw new FileException(FileException.FILE_ALREADY_EXISTS);
		}

		try {
			FileResultBuilder result = new FileResultBuilder();
			// copy file
			if (FileCreationType.COPY.equals(type)) {
				FileUtils.copyFile(srcFile, destFile);
				result.createdFiles.add(destFile);
			}
			else if (FileCreationType.MOVE.equals(type)) {
				FileUtils.copyFile(srcFile, destFile);
				FileUtils.forceDelete(srcFile);
				result.createdFiles.add(destFile);
				result.deletedFiles.add(srcFile);
			}
			return result.build();
		}
		catch (IOException exc) {
			throw new FileException(FileException.FILE_COPY_FAILED, exc);
		}
	}

	/**
	 * Delete files
	 * 
	 * @param request
	 * @throws FileException
	 */
	public FileResult deleteFiles(FilesRequest request) throws FileException {
		try {
			FileResultBuilder result = new FileResultBuilder();
			for (File file : request.getFiles()) {
				if (file.exists()) {
					FileUtils.forceDelete(file);
					result.deletedFiles.add(file);
				}
			}
			return result.build();
		}
		catch (IOException exc) {
			throw new FileException(FileException.FILE_DELETE_FAILED, exc);
		}
	}
}
