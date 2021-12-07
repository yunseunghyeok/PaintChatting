package doubledeltas.messages;

import java.io.DataOutputStream;
import java.io.IOException;

public class UserNickChangeFailMessage extends Message
implements ClientRecievable
{
	public static final byte
<<<<<<< HEAD
	UNKNOWN		= 0x00,
	SAME_NICK	= 0x01;
=======
	UNKNOWN = 0x00;
>>>>>>> hakfe
	
	public byte reason;
	
	public UserNickChangeFailMessage(byte reason) {
		this.type = TransferCode.USER_NICK_CHANGE_FAIL;
		this.reason = reason;
	}
	
	@Override
	public void send(DataOutputStream dos) throws IOException {
		super.send(dos);
		dos.writeByte(reason);
	}
	
	public byte getReason() { return reason; }
}

