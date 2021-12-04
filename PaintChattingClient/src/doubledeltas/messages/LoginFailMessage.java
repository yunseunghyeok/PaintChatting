package doubledeltas.messages;

import java.io.DataOutputStream;
import java.io.IOException;

public class LoginFailMessage extends Message
implements ClientRecievable
{	
	public static final byte
	UNKNOWN			= 0x00,
	NO_ID_FOUND 	= 0x01,
	PASSWORD_WRONG	= 0x02;
	
	private byte reason;
	
	public LoginFailMessage(byte reason) {
		this.type = TransferCode.LOGIN_FAIL;
		this.reason = reason;
	}
	
	@Override
	public void send(DataOutputStream dos) throws IOException {
		super.send(dos);
		dos.writeByte(reason);
	}
	
	public byte getReason() { return reason; }
}