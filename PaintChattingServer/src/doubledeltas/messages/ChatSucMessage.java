package doubledeltas.messages;

import doubledeltas.environments.TransferCode;
import doubledeltas.utils.ByteStringReader;

public class ChatSucMessage extends Message
implements ClientRecievable
{
	static final int MSG_SIZE = 1+4+45+1024+45+45;
	private int roomID;
	private String userID, text, font, imgFileName;
	
	public ChatSucMessage(byte[] bytes) {
		if (bytes[0] != TransferCode.CHAT_SUC) return;
		if (bytes.length < MSG_SIZE) return;
		
		this.bytes = new byte[MSG_SIZE];
		for (int i=0; i<MSG_SIZE; i++)
			this.bytes[i] = bytes[i];
		
		ByteStringReader bsr = new ByteStringReader(this.bytes);
		bsr.setCursor(1);
		this.roomID = bsr.readInInteger();
		this.userID = bsr.readInString(45);
		this.text = bsr.readInString(1024);
		this.font = bsr.readInString(45);
		this.imgFileName = bsr.readInString(45);
	}
	
	public ChatSucMessage(int roomID, String userID, String text, String font, String imgFileName) {
		bytes = new byte[MSG_SIZE];
		ByteStringReader bsr = new ByteStringReader(bytes);
		
		bytes[0] = TransferCode.CHAT_SUC;
		
		bsr.setCursor(1);
		bsr.writeInteger(roomID);
		bsr.writeString(userID, false);
		bsr.moveCursor(45);
		bsr.writeString(text, false);
		bsr.moveCursor(1024);
		bsr.writeString(font, false);
		bsr.moveCursor(45);
		bsr.writeString(imgFileName, false);
		
		this.roomID = roomID;
		this.userID = new String(userID);
		this.text = new String(text);
		this.font = new String(font);
		this.imgFileName = new String(imgFileName);
		
	}
	
	public int getRoomID() { return roomID; }
	public String getUserID() { return userID; }
	public String getText() { return text; }
	public String getFont() { return font; }
	public String getImageFileName() { return imgFileName; }
 }
