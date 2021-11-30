package doubledeltas.messages;

import java.io.DataOutputStream;
import java.io.IOException;

import doubledeltas.environments.TransferCode;

public class RoomCreateSucMessage extends Message
implements ClientRecievable
{	
	public RoomCreateSucMessage() {
		this.type = TransferCode.ROOM_CREATE;
	}
	
	@Override
	public void send(DataOutputStream dos) throws IOException {
		super.send(dos);
	}	
}
