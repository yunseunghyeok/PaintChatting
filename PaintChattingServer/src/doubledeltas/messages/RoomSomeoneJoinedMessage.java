package doubledeltas.messages;

import doubledeltas.environments.TransferCode;
import doubledeltas.utils.ByteStringReader;

public class RoomSomeoneJoinedMessage extends Message
implements Broadcastable, ClientRecievable
{
	private static final int MSG_SIZE = 1+45;
	private String nick;
	
	public RoomSomeoneJoinedMessage(byte[] bytes) {
		if (bytes[0] != TransferCode.ROOM_SOMEONE_JOINED.getByte()) return;
		if (bytes.length < MSG_SIZE) return;
		
		this.bytes = new byte[MSG_SIZE];
		for (int i=0; i<MSG_SIZE; i++)
			this.bytes[i] = bytes[i];
		
		ByteStringReader bsr = new ByteStringReader(this.bytes);
		bsr.setCursor(1);
		this.nick = bsr.readInString(45);
	}
	
	public RoomSomeoneJoinedMessage(String nick) {
		bytes = new byte[MSG_SIZE];
		ByteStringReader bsr = new ByteStringReader(bytes);
		
		bytes[0] = TransferCode.ROOM_SOMEONE_JOINED.getByte();
		
		bsr.setCursor(1);
		bsr.writeString(nick, false);
		
		this.nick=new String(nick);
	}
	
	public String getNickname() { return nick; }
 }
