package doubledeltas.messages;

import java.io.DataOutputStream;
import java.io.IOException;

import doubledeltas.environments.TransferCode;

public class RoomEnterMessage extends Message
implements ServerRecievable
{
	private String userID;
	private int roomID;
	
	public RoomEnterMessage(String userID, int roomID) {
		this.type = TransferCode.ROOM_ENTER;
		this.userID = new String(userID);
		this.roomID = roomID;
	}
	
	@Override
	public void send(DataOutputStream dos) throws IOException {
		super.send(dos);
		dos.writeUTF(userID);
		dos.writeInt(roomID);
	}
	
	public String getUserID() { return userID; }
	public int getRoomID() { return roomID; }
}
