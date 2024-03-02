package de.kobich.component.file;

import de.kobich.commons.exception.ApplicationException;
import de.kobich.commons.exception.ErrorCode;

public class FileException extends ApplicationException {
	public static final ErrorCode INTERNAL = new ErrorCode("file.internalError", "There was an internal problem");
	public static final ErrorCode IO_ERROR = new ErrorCode("file.ioError", "I/O error");
	public static final ErrorCode COMMAND_IO_ERROR = new ErrorCode("file.commandIOError", "I/O error: \nMake sure '{0}' is installed and on your PATH");
	public static final ErrorCode FILE_CREATE_FAILED = new ErrorCode("file.createFailed", "File could not be created"); 
	public static final ErrorCode FILE_DELETE_FAILED = new ErrorCode("file.deleteFailed", "File could not be deleted");
	public static final ErrorCode FILE_COPY_FAILED = new ErrorCode("file.copyFailed", "File could not be copied");
	public static final ErrorCode FILE_RENAMING_FAILED = new ErrorCode("file.renameFailed", "File could not be renamed");
	public static final ErrorCode FOLDER_ALREADY_EXISTS = new ErrorCode("file.renameAlreadyExists", "Destination folder already exists");
	public static final ErrorCode FILE_ALREADY_EXISTS = new ErrorCode("file.renameAlreadyExists", "Destination file already exists");
	public static final ErrorCode ANT_BUILD_ERROR = new ErrorCode("file.antBuildFailed", "Build error");
	public static final ErrorCode EXECUTION_ERROR = new ErrorCode("file.executeFailed", "Execution error");
	public static final ErrorCode DOWNLOAD_ERROR = new ErrorCode("file.downloadFailed", "Download error");
	public static final ErrorCode ILLEGAL_FORMAT = new ErrorCode("file.illegalFormat", "Format is not supported");
	public static final ErrorCode SYNCHRONIZATION_ERROR = new ErrorCode("file.syncFailed", "Synchronization error");
	public static final ErrorCode FILE_EXPORT_ERROR = new ErrorCode("file.exportFailed", "Error while exporting files");
	public static final ErrorCode XML_ERROR = new ErrorCode("file.loadXMLFailed", "XML file could not be loaded");

	private static final long serialVersionUID = -5238782004634985009L;

	public FileException(ErrorCode errorCode, Object... params) {
		super(errorCode, params);
	}

	public FileException(ErrorCode errorCode, Throwable cause, Object... params) {
		super(errorCode, cause, params);
	}


}
