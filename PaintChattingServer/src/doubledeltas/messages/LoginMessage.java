package doubledeltas.messages;

import doubledeltas.environments.TransferCode;
import doubledeltas.utils.ByteStringReader;

public class LoginMessage extends Message
implements ServerRecievable
{
	static final int MSG_SIZE = 1+45+45;
	private String id, pw;
	
	public LoginMessage(byte[] bytes) {
		if (bytes[0] != TransferCode.LOGIN) return;
		if (bytes.length < MSG_SIZE) return;
		
		this.bytes = new byte[MSG_SIZE];
		for (int i=0; i<MSG_SIZE; i++)
			this.bytes[i] = bytes[i];
		
		ByteStringReader bsr = new ByteStringReader(this.bytes);
		bsr.setCursor(1);
		this.id = bsr.readInString(45);
		this.pw = bsr.readInString(45);
	}
	
	public LoginMessage(String id, String pw) {
		bytes = new byte[MSG_SIZE];
		ByteStringReader bsr = new ByteStringReader(bytes);
		
		bytes[0] = TransferCode.LOGIN;
		
		bsr.setCursor(1);
		bsr.writeString(id, false);
		bsr.moveCursor(45);
		bsr.writeString(pw, false);
		
		this.id=new String(id);
		this.pw=new String(pw);
	}
	
	public String getID() { return id; }
	public String getPassword() { return pw; }
 }
