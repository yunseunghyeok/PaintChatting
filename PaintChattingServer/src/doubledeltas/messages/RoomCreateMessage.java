package doubledeltas.messages;

import java.io.DataOutputStream;
import java.io.IOException;

public class RoomCreateMessage extends Message
implements ServerRecievable
{
	private String id;
	
	public RoomCreateMessage(String id) {
		this.type = TransferCode.ROOM_CREATE;
		this.id=new String(id);
	}

	@Override
	public void send(DataOutputStream dos) throws IOException {
		super.send(dos);
		dos.writeUTF(id);
	}
	
	public String getID() { return id; }
 }
