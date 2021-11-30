package doubledeltas.messages;

import java.io.DataOutputStream;
import java.io.IOException;

import doubledeltas.environments.TransferCode;

public class ChatFailMessage extends Message
implements ClientRecievable
{
	public static final byte
	UNKNOWN = 1;
	
	private byte reason;
	
	public ChatFailMessage(byte reason) {
		this.type = TransferCode.CHAT_FAIL;
		this.reason = reason;
	}
	
	@Override
	public void send(DataOutputStream dos) throws IOException {
		super.send(dos);
		dos.writeByte(reason);
	}
	
	public byte getReason() { return reason; }
}