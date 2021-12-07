package doubledeltas.messages;

import java.io.DataOutputStream;
import java.io.IOException;

public class RoomEnterFailMessage extends Message
implements ClientRecievable
{
	public static final byte
<<<<<<< HEAD
	UNKNOWN = 0x00,
	NO_MATCHED_ROOM = 0x01,
	ALREADY_JOINED = 0x02;
=======
	UNKNOWN = 0x00;
>>>>>>> hakfe
	
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

