package doubledeltas.messages;

import doubledeltas.environments.TransferCode;

public class LoginSucMessage extends Message
implements ClientRecievable
{
	static final int MSG_SIZE = 1;
	
	public LoginSucMessage(byte[] bytes) {
		if (bytes[0] != TransferCode.LOGIN_SUC) return;
		if (bytes.length < MSG_SIZE) return;
		
		this.bytes = new byte[1];
		for (int i=0; i<MSG_SIZE; i++)
			this.bytes[i] = bytes[i];
	}
	
	public LoginSucMessage() {
		this.bytes = new byte[1];
		this.bytes[0] = TransferCode.LOGIN_SUC;
	}
}
