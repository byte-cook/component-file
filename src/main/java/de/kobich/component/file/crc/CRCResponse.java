package de.kobich.component.file.crc;

import java.util.List;

import de.kobich.component.file.Message;

/**
 * Request to check files for CRC checksum.
 * @author ckorn
 */
public class CRCResponse {
	private boolean succeeded;
	private List<Message> messages;

	/**
	 * Constructor
	 * @param succeeded 
	 * @param messages
	 */
	public CRCResponse(boolean succeeded, List<Message> messages) {
		this.succeeded = succeeded;
		this.messages = messages;
	}

	/**
	 * @return the succeeded
	 */
	public boolean isSucceeded() {
		return succeeded;
	}

	/**
	 * @return the messages
	 */
	public List<Message> getMessages() {
		return messages;
	}
}
