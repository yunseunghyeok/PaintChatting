package doubledeltas.messages;

import doubledeltas.environments.TransferCode;

public class UserNickChangeFailMessage extends Message
implements ClientRecievable
{
	enum UserNickChangeFailReason {
		UNKNOWN(0x00);
		
		private final int value;
		private UserNickChangeFailReason(int value) {
			this.value = value;
		}
		public byte getByte() { return (byte)value; }
		public static UserNickChangeFailReason get(byte id) {
			for (UserNickChangeFailReason r : UserNickChangeFailReason.values()) {
				if (id == r.getByte()) return r;
			}
			return null;
		}
	}
	
	private static final int MSG_SIZE = 1+1;
	private UserNickChangeFailReason reason;
	
	public UserNickChangeFailMessage(byte[] bytes) {
		if (bytes[0] != TransferCode.USER_NICK_CHANGE_FAIL.getByte()) return;
		if (bytes.length < MSG_SIZE) return;

		reason = UserNickChangeFailReason.get(bytes[0]);
		if (reason == null) return;
		
		this.bytes = new byte[MSG_SIZE];
		for (int i=0; i<MSG_SIZE; i++)
			this.bytes[i] = bytes[i];
	}
	
	public UserNickChangeFailMessage(UserNickChangeFailReason reason) {
		this.bytes = new byte[MSG_SIZE];
		this.bytes[0] = TransferCode.USER_NICK_CHANGE_FAIL.getByte();
		this.bytes[1] = reason.getByte();
		
		this.reason = reason;
	}
	
	public UserNickChangeFailReason getReason() { return reason; }
}

