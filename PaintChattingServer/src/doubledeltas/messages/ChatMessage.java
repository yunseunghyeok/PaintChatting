package doubledeltas.messages;

import doubledeltas.environments.TransferCode;
import doubledeltas.utils.ByteStringReader;

public class ChatMessage extends Message
implements ServerRecievable
{
	private static final int MSG_SIZE = 1+4+45+1024;
	private int roomID;
	private String userID, text;
	
	public ChatMessage(byte[] bytes) {
		if (bytes[0] != TransferCode.CHAT.getByte()) return;
		if (bytes.length < MSG_SIZE) return;
		
		this.bytes = new byte[MSG_SIZE];
		for (int i=0; i<MSG_SIZE; i++)
			this.bytes[i] = bytes[i];
		
		ByteStringReader bsr = new ByteStringReader(this.bytes);
		bsr.setCursor(1);
		this.roomID = bsr.readInInteger();
		this.userID = bsr.readInString(45);
		this.text = bsr.readInString(1024);
	}
	
	public ChatMessage(int roomID, String userID, String text) {
		bytes = new byte[MSG_SIZE];
		ByteStringReader bsr = new ByteStringReader(bytes);
		
		bytes[0] = TransferCode.CHAT.getByte();
		
		bsr.setCursor(1);
		bsr.writeInteger(roomID);
		bsr.writeString(userID, false);
		bsr.moveCursor(45);
		bsr.writeString(text, false);
		
		this.roomID = roomID;
		this.userID = new String(userID);
		this.text = new String(text);
	}
	
	public int getRoomID() { return roomID; }
	public String getUserID() { return userID; }
	public String getText() { return text; }
 }
