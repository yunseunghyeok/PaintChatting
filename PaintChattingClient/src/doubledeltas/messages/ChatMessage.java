package doubledeltas.messages;

import java.io.DataOutputStream;
import java.io.IOException;

public class ChatMessage extends Message
implements ServerRecievable
{
	private int roomID;
<<<<<<< HEAD
	private String userID, text, imageFileName;
	
	public ChatMessage(int roomID, String userID, String text, String imageFileName) {
=======
	private String userID, text;
	
	public ChatMessage(int roomID, String userID, String text) {
>>>>>>> hakfe
		this.type = TransferCode.CHAT;
		this.roomID = roomID;
		this.userID = new String(userID);
		this.text = new String(text);
<<<<<<< HEAD
		this.imageFileName = new String(imageFileName);
	}
	
	public ChatMessage(int roomID, String userID, String text) {
		this(roomID, userID, text, null);
=======
>>>>>>> hakfe
	}

	@Override
	public void send(DataOutputStream dos) throws IOException {
		super.send(dos);
		dos.write(roomID);
		dos.writeUTF(userID);
		dos.writeUTF(text);
	}
	
	public int getRoomID() { return roomID; }
	public String getUserID() { return userID; }
	public String getText() { return text; }
<<<<<<< HEAD
	public String getImageFileName() { return imageFileName; }	// nullable
}
=======
 }
>>>>>>> hakfe
