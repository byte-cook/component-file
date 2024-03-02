package de.kobich.component.file;


/**
 * Progress message.
 * @author ckorn
 */
public class Message {
	public static enum MessageType { INFO, ERROR, WARN }
	private final MessageType messageType;
	private final String message;
	private final String expectedValue;
	private final String actualValue;
	
	/**
	 * Constructor
	 * @param messageType
	 * @param message
	 */
	public Message(MessageType messageType, String message) {
		this(messageType, message, null, null);
	}
	
	/**
	 * Constructor
	 * @param messageType
	 * @param message
	 * @param expectedValue
	 * @param actualValue
	 * @param fileDescriptor
	 */
	public Message(MessageType messageType, String message, String expectedValue, String actualValue) {
		this.messageType = messageType;
		this.message = message;
		this.expectedValue = expectedValue;
		this.actualValue = actualValue;
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @return the expectedValue
	 */
	public String getExpectedValue() {
		return expectedValue;
	}

	/**
	 * @return the realValue
	 */
	public String getActualValue() {
		return actualValue;
	}

	/**
	 * @return the messageType
	 */
	public MessageType getMessageType() {
		return messageType;
	}
}
