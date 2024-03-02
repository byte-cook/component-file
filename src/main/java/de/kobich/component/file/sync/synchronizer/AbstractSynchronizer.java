package de.kobich.component.file.sync.synchronizer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import de.kobich.commons.runtime.executor.ExecuteRequest;
import de.kobich.commons.runtime.executor.Executor;
import de.kobich.commons.runtime.executor.command.CommandBuilder;
import de.kobich.commons.runtime.executor.command.CommandVariable;
import de.kobich.component.file.FileException;
import de.kobich.component.file.sync.SyncTask;

public abstract class AbstractSynchronizer implements IFileSynchronizer {
	@Override
	public void synchronize(SyncTask task, InputStream definitionStream, OutputStream logOutputStream, OutputStream errorOutputStream)
			throws FileException {
		CommandBuilder cb = null;
		try {
			cb = new CommandBuilder(definitionStream);

			List<CommandVariable> params = new ArrayList<CommandVariable>();
			params.addAll(getCommandVariables(task));
			
			if (!StringUtils.isEmpty(task.getName())) {
				params.add(new CommandVariable("task.name", task.getName()));
			}
			params.add(new CommandVariable("task.source", task.getSource().getAbsolutePath()));
			params.add(new CommandVariable("task.target", task.getTarget().getAbsolutePath()));
			params.add(new CommandVariable("task.overwrite", "" + task.isOverwrite()));

			cb.createCommand(params);

			ExecuteRequest executeRequest = new ExecuteRequest(cb.getCommand(), logOutputStream, errorOutputStream);
			executeRequest.setEnv(cb.getEnvironment());
			Executor executor = new Executor();
			executor.executeCommand(executeRequest);
		}
		catch (IOException exc) {
			throw new FileException(FileException.COMMAND_IO_ERROR, exc, cb != null ? cb.getCommandDefinition().getCommand() : null);
		}
		catch (Exception exc) {
			throw new FileException(FileException.INTERNAL, exc);
		}
	}
	
	protected List<CommandVariable> getCommandVariables(SyncTask task) throws FileException {
		// no-op
		return new ArrayList<CommandVariable>();
	}

}
