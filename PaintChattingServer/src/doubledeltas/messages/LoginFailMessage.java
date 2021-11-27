package doubledeltas.messages;

import doubledeltas.environments.TransferCode;

public class LoginFailMessage extends Message
implements ClientRecievable
{
	enum LoginFailReason {
		UNKNOWN(0x00),
		NO_ID_FOUND(0x01),
		PASSWORD_WRONG(0x02);
		
		private final int value;
		private LoginFailReason(int value) {
			this.value = value;
		}
		public byte getByte() { return (byte)value; }
		public static LoginFailReason get(byte id) {
			for (LoginFailReason r : LoginFailReason.values()) {
				if (id == r.getByte()) return r;
			}
			return null;
		}
	}
	
	private static final int MSG_SIZE = 1+1;
	private LoginFailReason reason;
	
	public LoginFailMessage(byte[] bytes) {
		if (bytes[0] != TransferCode.LOGIN_FAIL.getByte()) return;
		if (bytes.length < MSG_SIZE) return;

		reason = LoginFailReason.get(bytes[0]);
		if (reason == null) return;
		
		this.bytes = new byte[MSG_SIZE];
		for (int i=0; i<MSG_SIZE; i++)
			this.bytes[i] = bytes[i];
	}
	
	public LoginFailMessage(LoginFailReason reason) {
		this.bytes = new byte[MSG_SIZE];
		this.bytes[0] = TransferCode.LOGIN_FAIL.getByte();
		this.bytes[1] = reason.getByte();
		
		this.reason = reason;
	}
	
	public LoginFailReason getReason() { return reason; }
}