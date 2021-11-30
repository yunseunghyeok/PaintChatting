package doubledeltas.messages;

import doubledeltas.environments.TransferCode;

public class ConnectionCutMessage extends Message
implements ClientRecievable, ServerRecievable
{
	static final int MSG_SIZE = 1;
	
	public ConnectionCutMessage(byte[] bytes) {
		if (bytes[0] != TransferCode.CONNECTION_CUT) return;
		if (bytes.length < MSG_SIZE) return;
		
		this.bytes = new byte[1];
		for (int i=0; i<MSG_SIZE; i++)
			this.bytes[i] = bytes[i];
	}
	public ConnectionCutMessage() {
		this.bytes = new byte[1];
		this.bytes[0] = TransferCode.CONNECTION_CUT;
	}
}
