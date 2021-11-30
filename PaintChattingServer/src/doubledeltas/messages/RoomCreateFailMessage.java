package doubledeltas.messages;

import doubledeltas.environments.TransferCode;

public class RoomCreateFailMessage extends Message
implements ClientRecievable
{
	static final int MSG_SIZE = 1+1;
	enum RoomCreateFailReason {
		UNKNOWN(0x00);
		
		private final int value;
		private RoomCreateFailReason(int value) {
			this.value = value;
		}
		public byte getByte() { return (byte)value; }
		public static RoomCreateFailReason get(byte id) {
			for (RoomCreateFailReason r : RoomCreateFailReason.values()) {
				if (id == r.getByte()) return r;
			}
			return null;
		}
	}
	
	private RoomCreateFailReason reason;
	
	public RoomCreateFailMessage(byte[] bytes) {
		if (bytes[0] != TransferCode.ROOM_CREATE_FAIL) return;
		if (bytes.length < MSG_SIZE) return;

		reason = RoomCreateFailReason.get(bytes[0]);
		if (reason == null) return;
		
		this.bytes = new byte[MSG_SIZE];
		for (int i=0; i<MSG_SIZE; i++)
			this.bytes[i] = bytes[i];
	}
	
	public RoomCreateFailMessage(RoomCreateFailReason reason) {
		this.bytes = new byte[MSG_SIZE];
		this.bytes[0] = TransferCode.ROOM_CREATE_FAIL;
		this.bytes[1] = reason.getByte();
		
		this.reason = reason;
	}
	
	public RoomCreateFailReason getReason() { return reason; }
}

