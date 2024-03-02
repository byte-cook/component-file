package de.kobich.component.file.sync;

import java.io.InputStream;
import java.io.OutputStream;

import de.kobich.component.file.ant.FileAntOutputLevel;

/**
 * Synchronization request.
 * 
 * @author ckorn
 */
public abstract class AbstractSyncRequest {
	private final FileAntOutputLevel outputLevel;
	private final OutputStream logOutputStream;
	private final OutputStream errorOutputStream;
	private InputStream commandDefinitionStream;

	public AbstractSyncRequest(FileAntOutputLevel outputLevel, OutputStream logOutputStream, OutputStream errorOutputStream) {
		this.outputLevel = outputLevel;
		this.logOutputStream = logOutputStream;
		this.errorOutputStream = errorOutputStream;
	}

	/**
	 * @return the outputLevel
	 */
	public FileAntOutputLevel getOutputLevel() {
		return outputLevel;
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
	 * @return the commandDefinitionStream
	 */
	public InputStream getCommandDefinitionStream() {
		return commandDefinitionStream;
	}

	/**
	 * @param commandDefinitionStream
	 *            the commandDefinitionStream to set
	 */
	public void setCommandDefinitionStream(InputStream commandDefinitionStream) {
		this.commandDefinitionStream = commandDefinitionStream;
	}

}
