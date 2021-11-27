package doubledeltas.messages;

import doubledeltas.environments.TransferCode;

public class RoomCreateSucMessage extends Message
implements ClientRecievable
{
	private static final int MSG_SIZE = 1;
	
	public RoomCreateSucMessage(byte[] bytes) {
		if (bytes[0] != TransferCode.ROOM_CREATE_SUC.getByte()) return;
		if (bytes.length < MSG_SIZE) return;
		
		this.bytes = new byte[1];
		for (int i=0; i<MSG_SIZE; i++)
			this.bytes[i] = bytes[i];
	}
	
	public RoomCreateSucMessage() {
		this.bytes = new byte[1];
		this.bytes[0] = TransferCode.ROOM_CREATE_SUC.getByte();
	}
}
