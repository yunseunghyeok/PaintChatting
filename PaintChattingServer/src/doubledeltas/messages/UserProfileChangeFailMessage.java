package doubledeltas.messages;

import java.io.DataOutputStream;
import java.io.IOException;

public class UserProfileChangeFailMessage extends Message
implements ClientRecievable
{
	public static final byte
	UNKNOWN = 0x00;
	
	private byte reason;
	
	public UserProfileChangeFailMessage(byte reason) {
		this.type = TransferCode.USER_PROFILE_CHANGE_FAIL;
		this.reason = reason;
	}
	
	@Override
	public void send(DataOutputStream dos) throws IOException {
		super.send(dos);
		dos.writeByte(reason);
	}
	
	public byte getReason() { return reason; }
}

