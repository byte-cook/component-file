package de.kobich.component.file;

import java.io.Serializable;

/**
 * Represents meta data about a file.
 * @author ckorn
 */
public interface IMetaData extends Serializable {
	
	IMetaData clone();
}
