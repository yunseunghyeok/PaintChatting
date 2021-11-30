package doubledeltas.messages;

import doubledeltas.environments.TransferCode;

public class RegisterFailMessage extends Message
implements ClientRecievable
{
	static final int MSG_SIZE = 1+1;
	enum RegisterFailReason {
		UNKNOWN(0x00),
		DUPLICATED_ID(0x01);
		
		private final int value;
		private RegisterFailReason(int value) {
			this.value = value;
		}
		public byte getByte() { return (byte)value; }
		public static RegisterFailReason get(byte id) {
			for (RegisterFailReason r : RegisterFailReason.values()) {
				if (id == r.getByte()) return r;
			}
			return null;
		}
	}
	
	private RegisterFailReason reason;
	
	public RegisterFailMessage(byte[] bytes) {
		if (bytes[0] != TransferCode.REGISTER_FAIL) return;
		if (bytes.length < MSG_SIZE) return;

		reason = RegisterFailReason.get(bytes[0]);
		if (reason == null) return;
		
		this.bytes = new byte[MSG_SIZE];
		for (int i=0; i<MSG_SIZE; i++)
			this.bytes[i] = bytes[i];
	}
	
	public RegisterFailMessage(RegisterFailReason reason) {
		this.bytes = new byte[MSG_SIZE];
		this.bytes[0] = TransferCode.REGISTER_FAIL;
		this.bytes[1] = reason.getByte();
		
		this.reason = reason;
	}
	
	public RegisterFailReason getReason() { return reason; }
}

