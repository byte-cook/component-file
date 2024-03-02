package de.kobich.component.file.sync.synchronizer;

import java.io.InputStream;
import java.io.OutputStream;

import de.kobich.commons.runtime.executor.command.CommandLineTool;
import de.kobich.component.file.FileException;
import de.kobich.component.file.sync.SyncTask;

public interface IFileSynchronizer {
	void synchronize(SyncTask task, InputStream definitionStream, OutputStream logOutputStream, OutputStream errorOutputStream) throws FileException;
	
	boolean supports();
	
	CommandLineTool getCommandLineTool();
}
