package doubledeltas.messages;

import java.io.DataOutputStream;
import java.io.IOException;

public class LoginSucMessage extends Message
implements ClientRecievable
{	
	private String[] rooms;
	
	public LoginSucMessage(String[] rooms) {
		this.type = TransferCode.LOGIN_SUC;
		this.rooms = rooms;
	}
	
	@Override
	public void send(DataOutputStream dos) throws IOException {
		super.send(dos);
		dos.writeInt(rooms.length);
		for (int i=0; i<rooms.length; i++) {
			dos.writeUTF(rooms[i]);
		}
	}
}
