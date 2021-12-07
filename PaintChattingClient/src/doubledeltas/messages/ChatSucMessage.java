package doubledeltas.messages;

import java.io.DataOutputStream;
import java.io.IOException;

public class ChatSucMessage extends Message
implements ClientRecievable, Broadcastable
{
	private int roomID;
	private String userID, text, font, imgFileName;
		
	public ChatSucMessage(int roomID, String userID, String text, String font, String imgFileName) {
		this.type = TransferCode.CHAT_SUC;
		this.roomID = roomID;
		this.userID = new String(userID);
		this.text = new String(text);
		this.font = new String(font);
		this.imgFileName = new String(imgFileName);
	}	
	}
	

	@Override
	public void send(DataOutputStream dos) throws IOException {
		super.send(dos);
		dos.writeInt(roomID);
		dos.writeUTF(userID);
		dos.writeUTF(text);
		dos.writeUTF(font);
		dos.writeUTF(imgFileName);
	}
	
	public int getRoomID() { return roomID; }
	public String getUserID() { return userID; }
	public String getText() { return text; }
	public String getFont() { return font; }
	public String getImageFileName() { return imgFileName; }
 }
