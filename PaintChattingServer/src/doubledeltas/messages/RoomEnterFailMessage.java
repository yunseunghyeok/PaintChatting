package doubledeltas.messages;

import doubledeltas.environments.TransferCode;

public class RoomEnterFailMessage extends Message
implements ClientRecievable
{
	enum RoomEnterFailReason {
		UNKNOWN(0x00);
		
		private final int value;
		private RoomEnterFailReason(int value) {
			this.value = value;
		}
		public byte getByte() { return (byte)value; }
		public static RoomEnterFailReason get(byte id) {
			for (RoomEnterFailReason r : RoomEnterFailReason.values()) {
				if (id == r.getByte()) return r;
			}
			return null;
		}
	}
	
	private static final int MSG_SIZE = 1+1;
	private RoomEnterFailReason reason;
	
	public RoomEnterFailMessage(byte[] bytes) {
		if (bytes[0] != TransferCode.ROOM_ENTER_FAIL.getByte()) return;
		if (bytes.length < MSG_SIZE) return;

		reason = RoomEnterFailReason.get(bytes[0]);
		if (reason == null) return;
		
		this.bytes = new byte[MSG_SIZE];
		for (int i=0; i<MSG_SIZE; i++)
			this.bytes[i] = bytes[i];
	}
	
	public RoomEnterFailMessage(RoomEnterFailReason reason) {
		this.bytes = new byte[MSG_SIZE];
		this.bytes[0] = TransferCode.ROOM_ENTER_FAIL.getByte();
		this.bytes[1] = reason.getByte();
		
		this.reason = reason;
	}
	
	public RoomEnterFailReason getReason() { return reason; }
}

