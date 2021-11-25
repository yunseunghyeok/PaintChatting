package doubledeltas.messages;

import doubledeltas.environments.TransferCode;
import doubledeltas.utils.ByteStringReader;

public class RegisterMessage extends Message {
	private static final int MSG_SIZE = 1+45+45+45;
	private String id, pw, nick;
	
	public RegisterMessage(byte[] bytes) {
		if (bytes[0] != TransferCode.REGISTER.getByte()) return;
		if (bytes.length < MSG_SIZE) return;
		
		this.bytes = new byte[MSG_SIZE];
		for (int i=0; i<MSG_SIZE; i++)
			this.bytes[i] = bytes[i];
		
		ByteStringReader bsr = new ByteStringReader(this.bytes);
		bsr.setCursor(1);
		this.id = bsr.readInString(45);
		this.pw = bsr.readInString(45);
		this.nick = bsr.readInString(45);
	}
	
	public RegisterMessage(String id, String pw, String nick) {
		ByteStringReader bsr;
		bytes = new byte[MSG_SIZE];
		bytes[0] = TransferCode.REGISTER.getByte();
		
		byte[] buf;
		int cur = 1;
		buf = id.getBytes();
		for (int i=0; i<buf.length; i++) bytes[cur+i] = buf[i];
		cur += 45;
		buf = pw.getBytes();
		for (int i=0; i<buf.length; i++) bytes[cur+i] = buf[i];
		cur += 45;
		buf = nick.getBytes();
		for (int i=0; i<buf.length; i++) bytes[cur+i] = buf[i];
		
		this.id=id;
		this.pw=pw;
		this.nick=nick;
	}
	
	public String getID() { return id; }
	public String getPassword() { return pw; }
	public String getNickname() { return nick; }
}
