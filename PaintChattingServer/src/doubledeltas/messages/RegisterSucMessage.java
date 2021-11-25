package doubledeltas.messages;

import doubledeltas.environments.TransferCode;

public class RegisterSucMessage extends Message {
	private static final int MSG_SIZE = 1;
	
	public RegisterSucMessage(byte[] bytes) {
		if (bytes[0] != TransferCode.REGISTER_SUC.getByte()) return;
		if (bytes.length < MSG_SIZE) return;
		
		this.bytes = new byte[1];
		for (int i=0; i<MSG_SIZE; i++)
			this.bytes[i] = bytes[i];
	}
}
