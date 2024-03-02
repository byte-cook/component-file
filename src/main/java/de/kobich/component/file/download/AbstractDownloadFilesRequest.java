package de.kobich.component.file.download;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;

import de.kobich.commons.net.ProxyProviderRequest;


/**
 * Base download files request.
 * @author ckorn
 */
public abstract class AbstractDownloadFilesRequest extends ProxyProviderRequest {
	public static final int INFINITIVE_LEVEL = 0;
	private final File targetDirectory;
	private boolean recursive;
	private int level;
	private final OutputStream logOutputStream;
	private final OutputStream errorOutputStream;
	private InputStream commandDefinitionStream;

	/**
	 * @param urls
	 */
	public AbstractDownloadFilesRequest(File targetDirectory, OutputStream logOutputStream, OutputStream errorOutputStream) {
		this.recursive = false;
		this.level = 1;
		this.targetDirectory = targetDirectory;
		this.logOutputStream = logOutputStream;
		this.errorOutputStream = errorOutputStream;
	}
	
	/**
	 * @return the targetDirectory
	 */
	public File getTargetDirectory() {
		return targetDirectory;
	}

	/**
	 * @return the logOutputStream
	 */
	public OutputStream getLogOutputStream() {
		return logOutputStream;
	}

	/**
	 * @return the errorOutputStream
	 */
	public OutputStream getErrorOutputStream() {
		return errorOutputStream;
	}

	/**
	 * @return the recursive
	 */
	public boolean isRecursive() {
		return recursive;
	}

	/**
	 * @param recursive the recursive to set
	 */
	public void setRecursive(boolean recursive) {
		this.recursive = recursive;
	}

	/**
	 * @return the level
	 */
	public int getLevel() {
		return level;
	}

	/**
	 * @param level the level to set
	 */
	public void setLevel(int level) {
		this.level = level;
	}

	/**
	 * @return the commandDefinitionStream
	 */
	public InputStream getCommandDefinitionStream() {
		return commandDefinitionStream;
	}

	/**
	 * @param commandDefinitionStream the commandDefinitionStream to set
	 */
	public void setCommandDefinitionStream(InputStream commandDefinitionStream) {
		this.commandDefinitionStream = commandDefinitionStream;
	}
}
