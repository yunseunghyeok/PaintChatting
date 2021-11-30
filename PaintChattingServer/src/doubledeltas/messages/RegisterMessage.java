package doubledeltas.messages;

import java.io.DataOutputStream;
import java.io.IOException;

import doubledeltas.environments.TransferCode;

public class RegisterMessage extends Message
implements ServerRecievable
{
	private String id, pw, nick;
	
	public RegisterMessage(String id, String pw, String nick) {
		this.type = TransferCode.REGISTER;
		this.id=new String(id);
		this.pw=new String(pw);
		this.nick=new String(nick);
	}

	@Override
	public void send(DataOutputStream dos) throws IOException {
		super.send(dos);
		dos.writeUTF(id);
		dos.writeUTF(pw);
		dos.writeUTF(nick);
	}
	
	public String getID() { return id; }
	public String getPassword() { return pw; }
	public String getNickname() { return nick; }
}
