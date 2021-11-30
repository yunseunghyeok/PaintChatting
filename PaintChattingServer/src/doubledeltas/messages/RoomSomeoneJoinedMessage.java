package doubledeltas.messages;

import java.io.DataOutputStream;
import java.io.IOException;

public class RoomSomeoneJoinedMessage extends Message
implements Broadcastable, ClientRecievable
{
	private String nick;
	
	public RoomSomeoneJoinedMessage(String nick) {
		this.type = TransferCode.ROOM_SOMEONE_JOINED;
		this.nick = new String(nick);
	}
	
	@Override
	public void send(DataOutputStream dos) throws IOException {
		super.send(dos);
		dos.writeUTF(nick);
	}
	
	public String getNickname() { return nick; }
 }
