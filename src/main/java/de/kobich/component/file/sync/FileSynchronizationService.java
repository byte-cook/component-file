package de.kobich.component.file.sync;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.springframework.stereotype.Service;

import de.kobich.commons.Reject;
import de.kobich.commons.runtime.executor.command.CommandLineTool;
import de.kobich.commons.utils.StreamUtils;
import de.kobich.component.file.FileException;
import de.kobich.component.file.sync.synchronizer.IFileSynchronizer;

/**
 * Sync service.
 * @author ckorn
 */
@Service
public class FileSynchronizationService {
	private FileSynchronizers synchronizers;
	
	/**
	 * Returns the tool to use or null, if no suitable tool can be found
	 * @param request
	 * @return
	 */
	public CommandLineTool mayConvert() {
		IFileSynchronizer synchronizer = synchronizers.findSynchronizer();
		if (synchronizer != null) {
			return synchronizer.getCommandLineTool();
		}
		return null;
	}
	
	/**
	 * Sync files by ant
	 * @param request
	 * @return response
	 */
	public void syncDirectory(SyncDirectoryRequest request) throws FileException {
		SyncTask task = new SyncTask("");
		task.setSource(request.getSourceDirectory());
		task.setTarget(request.getTargetDirectory());
		task.setOverwrite(request.isOverwriteNewerTargetFiles());
		
		syncTasks(Collections.singletonList(task), request);
	}
	
	/**
	 * Sync tasks by ant
	 * @param request
	 * @throws FileException
	 */
	public void syncByDefinitionFile(SyncByDefinitionFileRequest request) throws FileException {
		Reject.ifNull(request, "Request is not set");
		try {
			FileInputStream inStream = new FileInputStream(request.getSyncDefinitionFile());
			Properties p = new Properties();
			p.load(inStream);
			
			List<SyncTask> tasks = getTasksFromProperties(p);
			syncTasks(tasks, request);
		}
		catch (IOException exc) {
			throw new FileException(FileException.IO_ERROR, exc);
		}
	}
	
	private void syncTasks(List<SyncTask> tasks, AbstractSyncRequest request) throws FileException {
		InputStream definitionStream = null;
		OutputStream logOutputStream = null;
		OutputStream errorOutputStream = null;
		try {
			IFileSynchronizer synchronizer = synchronizers.findSynchronizer();
			if (synchronizer == null) {
				throw new FileException(FileException.SYNCHRONIZATION_ERROR);
			}
			
			// read command definition
			definitionStream = request.getCommandDefinitionStream();
			if (definitionStream == null) {
				CommandLineTool tool = synchronizer.getCommandLineTool();
				definitionStream = tool.getInternalDefinitionStream(FileSynchronizationService.class);
				if (definitionStream == null) {
					throw new FileException(FileException.SYNCHRONIZATION_ERROR);
				}
			}
			
			logOutputStream = request.getLogOutputStream();
			errorOutputStream = request.getErrorOutputStream();
			for (SyncTask task : tasks) {
				synchronizer.synchronize(task, definitionStream, logOutputStream, errorOutputStream);
			}
		}
		finally {
			StreamUtils.forceClose(definitionStream);
			StreamUtils.forceClose(logOutputStream);
			StreamUtils.forceClose(errorOutputStream);
		}
	}
	
	/**
	 * Returns tasks from property file
	 * @param p
	 * @return
	 */
	private List<SyncTask> getTasksFromProperties(Properties p) {
		Map<String, SyncTask> name2Task = new HashMap<String, SyncTask>();
		for (String key : p.stringPropertyNames()) {
			String[] items = key.split("\\.");
			String taskName = items[0];
			String property = items[1];
			
			SyncTask task = null;
			if (!name2Task.containsKey(taskName)) {
				task = new SyncTask(taskName);
				name2Task.put(taskName, task);
			}
			task = name2Task.get(taskName);
			
			if ("source".equalsIgnoreCase(property)) {
				task.setSource(new File(p.getProperty(key)));
			}
			else if ("target".equalsIgnoreCase(property)) {
				task.setTarget(new File(p.getProperty(key)));
			}
			else if ("overwrite".equalsIgnoreCase(property)) {
				task.setOverwrite(Boolean.parseBoolean(p.getProperty(key)));
			}				
		}
		return new ArrayList<SyncTask>(name2Task.values());
	}
}
