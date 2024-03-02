package de.kobich.component.file.ant;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DefaultLogger;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.ProjectHelper;
import org.apache.tools.ant.Target;
import org.springframework.stereotype.Service;

import de.kobich.commons.Reject;
import de.kobich.commons.runtime.executor.ExecuteRequest;
import de.kobich.commons.runtime.executor.Executor;
import de.kobich.commons.runtime.executor.command.CommandBuilder;
import de.kobich.commons.runtime.executor.command.CommandLineTool;
import de.kobich.commons.runtime.executor.command.CommandVariable;
import de.kobich.commons.utils.StreamUtils;
import de.kobich.component.file.FileException;
import de.kobich.component.file.FileTool;

/**
 * Ant service.<p>
 * Ant version: 1.8.0
 * @author ckorn
 */
@Service
public class FileAntService {
	private static final String ANT_FILE_PROP = "ant.file";
	private static final String ANT_PRJ_HELPER_PROP = "ant.projectHelper";

	/**
	 * Returns the tool to use or null, if no suitable tool can be found
	 * @param request
	 * @return
	 */
	public CommandLineTool mayConvert() {
		return FileTool.ANT;
	}

	/**
	 * Returns all targets of the given build file
	 * @param buildFile
	 * @return targets
	 * @throws FileException
	 */
	public FileAntResponse getTargets(File buildFile) throws FileException {
		try {
			Project project = new Project();
			project.setUserProperty(ANT_FILE_PROP, buildFile.getAbsolutePath());
			project.init();

			ProjectHelper helper = ProjectHelper.getProjectHelper();
			project.addReference(ANT_PRJ_HELPER_PROP, helper);
			
			// parse build.xml file
			helper.parse(project, buildFile);
			
			String defaultTarget = project.getDefaultTarget();
			Hashtable<String, Target> targetsMap = project.getTargets();
			List<String> targets = new ArrayList<String>();
			for (String target : targetsMap.keySet()) {
				if (StringUtils.isNotBlank(target)) {
					targets.add(target);
				}
			}
			return new FileAntResponse(targets, defaultTarget);
		}
		catch (BuildException exc) {
			throw new FileException(FileException.ANT_BUILD_ERROR, exc);
		}
		catch (RuntimeException exc) {
			throw new FileException(FileException.INTERNAL, exc);
		}
	}

	/**
	 * Runs ant as internal process
	 * @param request
	 * @throws FileException
	 */
	public void runAntInternally(FileAntInternallyRequest request) throws FileException {
		Project project = null;
		try {
			project = new Project();
			project.setUserProperty(ANT_FILE_PROP, request.getBuildFile().getAbsolutePath());
			
			DefaultLogger consoleLogger = new DefaultLogger();
			consoleLogger.setOutputPrintStream(new PrintStream(request.getLogOutputStream()));
			consoleLogger.setErrorPrintStream(new PrintStream(request.getErrorOutputStream()));
			consoleLogger.setMessageOutputLevel(getOutputLevel(request));
			project.addBuildListener(consoleLogger);
			
			project.fireBuildStarted();
			project.init();
			
			ProjectHelper helper = ProjectHelper.getProjectHelper();
			project.addReference(ANT_PRJ_HELPER_PROP, helper);
			
			// set properties
			Properties properties = request.getProperties();
			if (properties != null) {
				for (String name : properties.stringPropertyNames()) {
					String value = properties.getProperty(name);
					project.setProperty(name, value);
				}
			}

			// parse build.xml file
			helper.parse(project, request.getBuildFile());
			
			// execute ant target
			if (StringUtils.isNotBlank(request.getTarget())) {
				project.executeTarget(request.getTarget());
			}
			else {
				project.executeTarget(project.getDefaultTarget());
			}

			project.fireBuildFinished(null);
		}
		catch (BuildException exc) {
			throw new FileException(FileException.ANT_BUILD_ERROR, exc);
		}
		catch (RuntimeException exc) {
			throw new FileException(FileException.INTERNAL, exc);
		}
	}
	
	/**
	 * Returns the output level
	 * @param request
	 * @return
	 */
	private int getOutputLevel(FileAntInternallyRequest request) {
		FileAntOutputLevel level = request.getOutputLevel();
		switch (level) {
			case DEBUG:
				return Project.MSG_DEBUG;
			case ERR:
				return Project.MSG_ERR;
			case INFO:
				return Project.MSG_INFO;
			case VERBOSE:
				return Project.MSG_VERBOSE;
			case WARN:
				return Project.MSG_WARN;
		}
		return Project.MSG_INFO;
	}
	
	/**
	 * Runs ant as external process
	 * @param request
	 * @throws FileException
	 */
	public void runAntExternally(FileAntExternallyRequest request) throws FileException {
		Reject.ifNull(request, "Request is not set");
		
		InputStream definitionStream = null;
		CommandBuilder cb = null;
		try {
			definitionStream = request.getCommandDefinitionStream();
			// read command definition
			if (definitionStream == null) {
				definitionStream = FileTool.ANT.getInternalDefinitionStream(FileAntService.class);
				if (definitionStream == null) {
					throw new FileException(FileException.ANT_BUILD_ERROR);
				}
			}
			
			cb = new CommandBuilder(definitionStream);
			
			List<CommandVariable> params = new ArrayList<CommandVariable>();
			params.add(new CommandVariable("buildfile", request.getBuildFile().getAbsolutePath()));
			
			FileAntOutputLevel level = request.getOutputLevel();
			if (FileAntOutputLevel.DEBUG.equals(level)) {
				params.add(new CommandVariable("debug"));
			}
			if (FileAntOutputLevel.VERBOSE.equals(level)) {
				params.add(new CommandVariable("verbose"));
			}
			
			Properties properties = request.getProperties();
			if (properties != null) {
				for (String name : properties.stringPropertyNames()) {
					String value = properties.getProperty(name);
					params.add(new CommandVariable("properties", Arrays.asList(name, value)));
				}
			}
			
			if (StringUtils.isNotBlank(request.getTarget())) {
				params.add(new CommandVariable("target", request.getTarget()));
			}
			
			cb.createCommand(params);
			
			ExecuteRequest executeRequest  = new ExecuteRequest(cb.getCommand(), request.getLogOutputStream(), request.getErrorOutputStream());
			executeRequest.setEnv(cb.getEnvironment());
			Executor executor = new Executor();
			executor.executeCommand(executeRequest);
		}
		catch (IOException exc) {
			throw new FileException(FileException.COMMAND_IO_ERROR, exc, cb != null ? cb.getCommandDefinition().getCommand() : null);
		}
		catch (FileException exc) {
			throw exc;
		}
		catch (Exception exc) {
			throw new FileException(FileException.ANT_BUILD_ERROR, exc);
		}
		finally {
			StreamUtils.forceClose(definitionStream);
		}
	}
}
