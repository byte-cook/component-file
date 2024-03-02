package de.kobich.component.file.sync.synchronizer;

import org.springframework.stereotype.Service;

import de.kobich.commons.runtime.executor.command.CommandLineTool;
import de.kobich.commons.utils.SystemUtils;
import de.kobich.component.file.FileTool;

/**
 * Sync service.
 * @author ckorn
 */
@Service
public class RobocopySynchronizer extends AbstractSynchronizer implements IFileSynchronizer {

	@Override
	public boolean supports() {
		return SystemUtils.isWindowsSystem();
	}

	@Override
	public CommandLineTool getCommandLineTool() {
		return FileTool.ROBOCOPY;
	}
	
}
