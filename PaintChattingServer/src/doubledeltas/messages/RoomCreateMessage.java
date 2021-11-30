package doubledeltas.messages;

import doubledeltas.environments.TransferCode;
import doubledeltas.utils.ByteStringReader;

public class RoomCreateMessage extends Message
implements ServerRecievable
{
	static final int MSG_SIZE = 1+45;
	private String id;
	
	public RoomCreateMessage(byte[] bytes) {
		if (bytes[0] != TransferCode.ROOM_CREATE) return;
		if (bytes.length < MSG_SIZE) return;
		
		this.bytes = new byte[MSG_SIZE];
		for (int i=0; i<MSG_SIZE; i++)
			this.bytes[i] = bytes[i];
		
		ByteStringReader bsr = new ByteStringReader(this.bytes);
		bsr.setCursor(1);
		this.id = bsr.readInString(45);
	}
	
	public RoomCreateMessage(String id) {
		bytes = new byte[MSG_SIZE];
		ByteStringReader bsr = new ByteStringReader(bytes);
		
		bytes[0] = TransferCode.ROOM_CREATE;
		
		bsr.setCursor(1);
		bsr.writeString(id, false);
		
		this.id=new String(id);
	}
	
	public String getID() { return id; }
 }
