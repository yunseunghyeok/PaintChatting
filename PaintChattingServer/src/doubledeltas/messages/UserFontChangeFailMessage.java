package doubledeltas.messages;

import java.io.DataOutputStream;
import java.io.IOException;

public class UserFontChangeFailMessage extends Message
implements ClientRecievable
{
	public static final byte
	UNKNOWN = 0x00,
	SAME_FONT = 0x01;
	
	private byte reason;
	
	public UserFontChangeFailMessage(byte reason) {
		this.type = TransferCode.USER_FONT_CHANGE_FAIL;
		this.reason = reason;
	}
	
	@Override
	public void send(DataOutputStream dos) throws IOException {
		super.send(dos);
		dos.writeByte(reason);
	}
	
	public byte getReason() { return reason; }
}