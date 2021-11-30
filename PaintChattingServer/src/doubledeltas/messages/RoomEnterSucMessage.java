package doubledeltas.messages;

import java.io.DataOutputStream;
import java.io.IOException;

public class RoomEnterSucMessage extends Message
implements ClientRecievable
{
	public RoomEnterSucMessage() {
		this.type = TransferCode.ROOM_ENTER_SUC;
	}
	
	@Override
	public void send(DataOutputStream dos) throws IOException {
		super.send(dos);
	}
}
