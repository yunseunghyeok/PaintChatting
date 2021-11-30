package doubledeltas.messages;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import doubledeltas.environments.TransferCode;

public class ChatFailMessage extends Message
implements ClientRecievable
{
	static final int MSG_SIZE = 1+1;
	enum ChatFailReason {
		UNKNOWN(0x00);
		
		private final int value;
		private ChatFailReason(int value) {
			this.value = value;
		}
		public byte getByte() { return (byte)value; }
		public static ChatFailReason get(byte id) {
			for (ChatFailReason r : ChatFailReason.values()) {
				if (id == r.getByte()) return r;
			}
			return null;
		}
	}
	
	private ChatFailReason reason;
	
	public ChatFailMessage(byte[] bytes) {
		if (bytes[0] != TransferCode.CHAT_FAIL) return;
		if (bytes.length < MSG_SIZE) return;

		reason = ChatFailReason.get(bytes[0]);
		if (reason == null) return;
		
		//this.bytes = new byte[MSG_SIZE];
		//for (int i=0; i<MSG_SIZE; i++)
		//	this.bytes[i] = bytes[i];
		this.bytes = Arrays.copyOfRange(bytes, 0, MSG_SIZE);
	}
	
	public ChatFailMessage(ChatFailReason reason) {
		this.bytes = new byte[MSG_SIZE];
		this.bytes[0] = TransferCode.CHAT_FAIL;
		this.bytes[1] = reason.getByte();
		
		this.reason = reason;
	}
	
	public ChatFailReason getReason() { return reason; }
}