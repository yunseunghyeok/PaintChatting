package doubledeltas.messages;

import java.io.DataOutputStream;
import java.io.IOException;

public class LoginMessage extends Message
implements ServerRecievable
{
	private String id, pw;
	
	public LoginMessage(String id, String pw) {
		this.type = TransferCode.LOGIN;
		this.id = new String(id);
		this.pw = new String(pw);
	}

	@Override
	public void send(DataOutputStream dos) throws IOException {
		super.send(dos);
		dos.writeUTF(id);
		dos.writeUTF(pw);
	}
	
	public String getID() { return id; }
	public String getPassword() { return pw; }
 }
