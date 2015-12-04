package reso.common;

public class AbstractMessageWithPayload
implements MessageWithPayload {

	private final Message payload;
	private final int protocol;
	
	public AbstractMessageWithPayload(int protocol, Message payload) {
		this.protocol= protocol;
		this.payload= payload;
	}
	
	@Override
	public Message getPayload() {
		return payload;
	}

	@Override
	public int getProtocol() {
		return protocol;
	}

}
