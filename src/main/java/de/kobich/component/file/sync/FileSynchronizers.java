package de.kobich.component.file.sync;

import java.util.List;

import de.kobich.component.file.sync.synchronizer.IFileSynchronizer;


public class FileSynchronizers {
	private List<IFileSynchronizer> synchronizers;
	
	public IFileSynchronizer findSynchronizer() {
		for (IFileSynchronizer codec : synchronizers) {
			if (codec.supports()) {
				return codec;
			}
		}
		return null;
	}

	public List<IFileSynchronizer> getSynchronizers() {
		return synchronizers;
	}
	
	/**
	 * @param synchronizers the synchronizers to set
	 */
	public void setSynchronizers(List<IFileSynchronizer> synchronizers) {
		this.synchronizers = synchronizers;
	}
}
