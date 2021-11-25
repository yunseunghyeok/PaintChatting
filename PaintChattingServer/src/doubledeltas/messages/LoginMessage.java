package doubledeltas.messages;

import doubledeltas.environments.TransferCode;
import doubledeltas.utils.ByteStringReader;

public class LoginMessage extends Message {
	private static final int MSG_SIZE = 1+45+45;
	private String id, pw;
	
	public LoginMessage(byte[] bytes) {
		if (bytes[0] != TransferCode.LOGIN.getByte()) return;
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
		ByteStringReader bsr;
		bytes = new byte[MSG_SIZE];
		bytes[0] = TransferCode.LOGIN.getByte();
		
		byte[] buf;
		int cur = 1;
		buf = id.getBytes();
		for (int i=0; i<buf.length; i++) bytes[cur+i] = buf[i];
		cur += 45;
		buf = pw.getBytes();
		for (int i=0; i<buf.length; i++) bytes[cur+i] = buf[i];
		
		this.id=id;
		this.pw=pw;
	}
	
	public String getID() { return id; }
	public String getPassword() { return pw; }
 }
