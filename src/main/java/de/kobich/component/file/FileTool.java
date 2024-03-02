package de.kobich.component.file;

import de.kobich.commons.runtime.executor.command.CommandLineTool;

public class FileTool {
	public static final CommandLineTool ANT = new CommandLineTool("Ant", "1.8.0", "ant");
	public static final CommandLineTool WGET = new CommandLineTool("Wget", "1.11.4", "wget");
	public static final CommandLineTool ROBOCOPY = new CommandLineTool("Robocopy", "", "robocopy");
	public static final CommandLineTool RSYNC = new CommandLineTool("rsync", "", "rsync");
}
