package doubledeltas.messages;

import doubledeltas.environments.TransferCode;

public class UserProfileChangeFailMessage extends Message
implements ClientRecievable
{
	static final int MSG_SIZE = 1+1;
	enum UserProfileChangeFailReason {
		UNKNOWN(0x00);
		
		private final int value;
		private UserProfileChangeFailReason(int value) {
			this.value = value;
		}
		public byte getByte() { return (byte)value; }
		public static UserProfileChangeFailReason get(byte id) {
			for (UserProfileChangeFailReason r : UserProfileChangeFailReason.values()) {
				if (id == r.getByte()) return r;
			}
			return null;
		}
	}
	
	private UserProfileChangeFailReason reason;
	
	public UserProfileChangeFailMessage(byte[] bytes) {
		if (bytes[0] != TransferCode.USER_NICK_CHANGE_FAIL) return;
		if (bytes.length < MSG_SIZE) return;

		reason = UserProfileChangeFailReason.get(bytes[0]);
		if (reason == null) return;
		
		this.bytes = new byte[MSG_SIZE];
		for (int i=0; i<MSG_SIZE; i++)
			this.bytes[i] = bytes[i];
	}
	
	public UserProfileChangeFailMessage(UserProfileChangeFailReason reason) {
		this.bytes = new byte[MSG_SIZE];
		this.bytes[0] = TransferCode.USER_NICK_CHANGE_FAIL;
		this.bytes[1] = reason.getByte();
		
		this.reason = reason;
	}
	
	public UserProfileChangeFailReason getReason() { return reason; }
}

