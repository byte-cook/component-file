package de.kobich.component.file.sync;

import java.io.File;

/**
 * Sync task.
 * @author ckorn
 */
public class SyncTask {
	private String name;
	private File source;
	private File target;
	private boolean overwrite;
	
	public SyncTask(String name) {
		this.name = name;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the source
	 */
	public File getSource() {
		return source;
	}

	/**
	 * @param source the source to set
	 */
	public void setSource(File source) {
		this.source = source;
	}

	/**
	 * @return the target
	 */
	public File getTarget() {
		return target;
	}

	/**
	 * @param target the target to set
	 */
	public void setTarget(File target) {
		this.target = target;
	}

	/**
	 * @return the overwrite
	 */
	public boolean isOverwrite() {
		return overwrite;
	}

	/**
	 * @param overwrite the overwrite to set
	 */
	public void setOverwrite(boolean overwrite) {
		this.overwrite = overwrite;
	}
}
