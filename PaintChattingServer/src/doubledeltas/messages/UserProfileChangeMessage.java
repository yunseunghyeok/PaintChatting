package doubledeltas.messages;

import java.io.DataOutputStream;
import java.io.IOException;

import doubledeltas.environments.TransferCode;

public class UserProfileChangeMessage extends Message
implements ServerRecievable
{
	private String id, fileName;
	
	public UserProfileChangeMessage(String id, String fileName) {
		this.type = TransferCode.USER_NICK_CHANGE;
		this.id=new String(id);
		this.fileName=new String(fileName);
	}
	
	@Override
	public void send(DataOutputStream dos) throws IOException {
		super.send(dos);
		dos.writeUTF(id);
		dos.writeUTF(fileName);
	}
	
	public String getID() { return id; }
	public String getFileName() { return fileName; }
 }
