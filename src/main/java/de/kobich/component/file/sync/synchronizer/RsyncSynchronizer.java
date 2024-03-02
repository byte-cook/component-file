package de.kobich.component.file.sync.synchronizer;

import org.springframework.stereotype.Service;

import de.kobich.commons.runtime.executor.command.CommandLineTool;
import de.kobich.commons.utils.SystemUtils;
import de.kobich.component.file.FileTool;

@Service
public class RsyncSynchronizer extends AbstractSynchronizer implements IFileSynchronizer {

	@Override
	public boolean supports() {
		return SystemUtils.isLinuxSystem();
	}

	@Override
	public CommandLineTool getCommandLineTool() {
		return FileTool.RSYNC;
	}

}
