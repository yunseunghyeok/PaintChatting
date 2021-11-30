package doubledeltas.messages;

import java.io.DataOutputStream;
import java.io.IOException;

import doubledeltas.environments.TransferCode;

public class RoomEnterFailMessage extends Message
implements ClientRecievable
{
	public static final byte
	UNKNOWN = 0x00;
	
	private byte reason;
	
	public RoomEnterFailMessage(byte reason) {
		this.type = TransferCode.ROOM_ENTER_FAIL;
		this.reason = reason;
	}
	
	@Override
	public void send(DataOutputStream dos) throws IOException {
		super.send(dos);
		dos.writeByte(reason);
	}
	
	public byte getReason() { return reason; }
}

