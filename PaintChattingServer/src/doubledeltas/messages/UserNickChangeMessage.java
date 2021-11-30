package doubledeltas.messages;

import java.io.DataOutputStream;
import java.io.IOException;

public class UserNickChangeMessage extends Message
implements ServerRecievable
{
	private String id, newNick;
	
	public UserNickChangeMessage(String id, String newNick) {
		this.type = TransferCode.USER_NICK_CHANGE;
		this.id=new String(id);
		this.newNick=new String(newNick);
	}
	
	@Override
	public void send(DataOutputStream dos) throws IOException {
		super.send(dos);
		dos.writeUTF(id);
		dos.writeUTF(newNick);
	}
	
	public String getID() { return id; }
	public String getNewNickname() { return newNick; }
 }
