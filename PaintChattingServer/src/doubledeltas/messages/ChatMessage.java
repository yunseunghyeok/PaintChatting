package doubledeltas.messages;

import java.io.DataOutputStream;
import java.io.IOException;

public class ChatMessage extends Message
implements ServerRecievable
{
	private int roomID;
	private String userID, text, imageFileName;
	
	public ChatMessage(int roomID, String userID, String text, String imageFileName) {
		this.type = TransferCode.CHAT;
		this.roomID = roomID;
		this.userID = new String(userID);
		this.text = new String(text);
		this.imageFileName = new String(imageFileName);
	}
	
	public ChatMessage(int roomID, String userID, String text) {
		this.type = TransferCode.CHAT;
		this.roomID = roomID;
		this.userID = new String(userID);
		this.text = new String(text);
		this.imageFileName = "0";
	}

	@Override
	public void send(DataOutputStream dos) throws IOException {
		super.send(dos);
		dos.writeInt(roomID);
		dos.writeUTF(userID);
		dos.writeUTF(text);
		dos.writeUTF(imageFileName);
	}
	
	public int getRoomID() { return roomID; }
	public String getUserID() { return userID; }
	public String getText() { return text; }
	public String getImageFileName() { return imageFileName; }	// nullable
}
