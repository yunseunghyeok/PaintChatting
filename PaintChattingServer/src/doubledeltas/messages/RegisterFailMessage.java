package doubledeltas.messages;

import java.io.DataOutputStream;
import java.io.IOException;

public class RegisterFailMessage extends Message
implements ClientRecievable
{	
	public static final byte
	UNKNOWN			= 0x00,
	DUPLICATED_ID	= 0x01;

	private byte reason;
	
	public RegisterFailMessage(byte reason) {
		this.type = TransferCode.REGISTER_FAIL;
		this.reason = reason;
	}

	@Override
	public void send(DataOutputStream dos) throws IOException {
		super.send(dos);
		dos.writeByte(reason);
	}
	
	public byte getReason() { return reason; }
}

