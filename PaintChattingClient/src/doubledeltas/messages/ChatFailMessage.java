package doubledeltas.messages;

import java.io.DataOutputStream;
import java.io.IOException;

public class ChatFailMessage extends Message
implements ClientRecievable
{
	public static final byte
	UNKNOWN			= 0;

	
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