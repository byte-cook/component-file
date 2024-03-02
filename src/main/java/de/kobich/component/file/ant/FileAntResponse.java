package de.kobich.component.file.ant;

import java.util.List;

/**
 * Ant request.
 */
public class FileAntResponse {
	private String defaultTarget;
	private List<String> targets;

	/**
	 * Constructor
	 */
	public FileAntResponse(List<String> targets, String defaultTarget) {
		this.targets = targets;
		this.defaultTarget = defaultTarget;
	}

	/**
	 * @return the defaultTarget
	 */
	public String getDefaultTarget() {
		return defaultTarget;
	}

	/**
	 * @return the targets
	 */
	public List<String> getTargets() {
		return targets;
	}

}
