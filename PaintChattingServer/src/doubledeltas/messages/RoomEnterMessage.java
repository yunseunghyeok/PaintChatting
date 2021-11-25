package doubledeltas.messages;

import doubledeltas.environments.TransferCode;
import doubledeltas.utils.ByteStringReader;

public class RoomEnterMessage extends Message {
	private static final int MSG_SIZE = 1+45+4;
	private String userID;
	private int roomID;
	
	public RoomEnterMessage(byte[] bytes) {
		if (bytes[0] != TransferCode.ROOM_ENTER.getByte()) return;
		if (bytes.length < MSG_SIZE) return;
		
		this.bytes = new byte[MSG_SIZE];
		for (int i=0; i<MSG_SIZE; i++)
			this.bytes[i] = bytes[i];
		
		ByteStringReader bsr = new ByteStringReader(this.bytes);
		bsr.setCursor(1);
		this.userID = bsr.readInString(45);
		this.roomID = bsr.readInInteger(4);
	}
	
	public RoomEnterMessage(String userID, int roomID) {
		ByteStringReader bsr;
		bytes = new byte[MSG_SIZE];
		bytes[0] = TransferCode.ROOM_ENTER.getByte();
		
		byte[] buf;
		int cur = 1;
		buf = userID.getBytes();
		for (int i=0; i<buf.length; i++) bytes[cur+i] = buf[i];
		cur += 45;
		
		
		this.id=id;
		this.pw=pw;
	}
	
}
